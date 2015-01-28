/*
 Copyright 2015 Peter-Josef Meisch (pj.meisch@sothawo.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/package com.sothawo.classfileversions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the application.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com).
 */
public class ClassFileVersions {
    /** program arguments */
    private final List<String> arguments;

    /**
     *
     * @param args program call arguments
     */
    public ClassFileVersions(String[] args) {
        arguments = Arrays.asList(args);
    }

    public static void main(String[] args) {
        new ClassFileVersions(args).run();
    }

    private void run() {
        if (0 == arguments.size()) {
            System.out.println("arguments: jar files optionally prepended by sort flag -v or -n");
        }
    }

}
