/*
 Copyright 2015-2021 Peter-Josef Meisch (pj.meisch@sothawo.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.sothawo.classfileversions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * the application.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class ClassFileVersions {
// ------------------------------ FIELDS ------------------------------

    /** magic number in class files */
    private final static byte[] magicNumber = new byte[]{(byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe};
    /** program arguments */
    private final List<String> arguments;
    /** mapping from major version to java version */
    private final Map<Integer, String> javaVersions = new HashMap<Integer, String>();
// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param args
     *         program call arguments
     */
    public ClassFileVersions(String[] args) {
        arguments = Arrays.asList(args);
        javaVersions.put(44, "1.0");
        javaVersions.put(45, "1.1");
        javaVersions.put(46, "1.2");
        javaVersions.put(47, "1.3");
        javaVersions.put(48, "1.4");
        javaVersions.put(49, "5");
        javaVersions.put(50, "6");
        javaVersions.put(51, "7");
        javaVersions.put(52, "8");
        javaVersions.put(53, "9");
        javaVersions.put(54, "10");
        javaVersions.put(55, "11");
        javaVersions.put(56, "12");
        javaVersions.put(57, "13");
        javaVersions.put(58, "14");
        javaVersions.put(59, "15");
        javaVersions.put(60, "16");
        javaVersions.put(61, "17");
    }

// -------------------------- ENUMERATIONS --------------------------

    /**
     * sort order
     */
    enum Order {
        /** sort by version */
        VERSION,
        /** sort by name */
        NAME
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args) {
        new ClassFileVersions(args).run();
    }

    /**
     * converts a major version to the normal version string
     * @param version the major version
     * @return String
     */
    private String versionToString(int version) {
        String s = javaVersions.get(version);
        if (null == s) {
            s = "?.?";
        }
        return s;
    }
    private void run() {
        if (0 == arguments.size()) {
            System.out.println("arguments: jar files optionally prepended by sort flag -v or -n");
        }

        Order order = Order.NAME;
        for (String argument : arguments) {
            if ("-v".equals(argument)) {
                order = Order.VERSION;
            } else if ("-n".equals(argument)) {
                order = Order.NAME;
            } else {
                try {
                    processFile(argument, order);
                } catch (IOException e) {
                    System.err.println("processFile " + argument);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * processes the given jar file and prints the info in the specified order
     *
     * @param filename
     *         the name of the jar file
     * @param order
     *         the order for the output
     */
    private void processFile(String filename, Order order) throws IOException {
        System.out.println("jar file: " + filename);
        // to collect the versions for the files (filenames should be unique in jar)
        // use a TreeMap to get the entries sorted by name
        Map<String, Integer> fileVersions = new TreeMap<String, Integer>();
        // to collect the files for the version (multiple files per version possible)
        // use a TreeMap to get the entries sorted
        Map<Integer, Set<String>> versionFiles = new TreeMap<Integer, Set<String>>();

        ZipInputStream zipStream = null;
        try {
            zipStream = new ZipInputStream(new FileInputStream(filename));
            ZipEntry zipEntry = null;
            while (null != (zipEntry = zipStream.getNextEntry())) {
                if (zipEntry.getName().endsWith(".class")) {
                    // process the class file
                    byte[] buffer = new byte[4];
                    // read 4 bytes; magic number
                    zipStream.read(buffer, 0, 4);
                    if (!Arrays.equals(buffer, magicNumber)) {
                        System.err.println("invalid class file " + zipEntry.getName() + " in " + filename);
                    } else {
                        // read major and minor number, only major is needed
                        zipStream.read(buffer, 0, 4);
                        int major = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);

                        // store in map
                        fileVersions.put(zipEntry.getName(), major);
                        Set<String> files = versionFiles.get(major);
                        if (null == files) {
                            // use a TreeSet to get the entries sorted
                            files = new TreeSet<String>();
                            versionFiles.put(major, files);
                        }
                        files.add(zipEntry.getName());
                    }
                }
                zipStream.closeEntry();
            }
            switch (order) {
                case NAME:
                    for (Map.Entry<String, Integer>entry : fileVersions.entrySet()) {
                        System.out.println(entry.getKey() + ": " + versionToString(entry.getValue()));
                    }
                    break;
                case VERSION:
                    for (Map.Entry<Integer, Set<String>> entry : versionFiles.entrySet()) {
                        for (String name : entry.getValue()) {
                            System.out.println(versionToString(entry.getKey()) + ": " + name);
                        }
                    }
                    break;
                default:
                    break;
            }
        } finally {
            if (null != zipStream) {
                try {
                    zipStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
