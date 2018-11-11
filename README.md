# classfileversion

a small programm to list the classfile versions of classes contained in jar archives.

## license

 Copyright 2015-2018 Peter-Josef Meisch (pj.meisch@sothawo.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

## building the program

the program is built using maven with the command

'mvn package'

the target directory will then contain the runnable jar file. The program is built for Java 5.

## running the program

after building, the program can be run on the commandline by calling

_java -jar classfileversions-a.b.c.jar options_

a.b.c. is the version of this program.

The options are the names of the jar files to analyse with eventually mixed in sort flags. The output can be sorted
in two ways: by full qualified class name or by class file version number. Default ordering is by class name. The
order can be switched by uisng -v or -n flags. Options like this:

-v archive1.jar archive2.jar -n archive3.jar -v archive4.jar

would list the info of the four jars, one after each other, with the output for archive3 sorted by name, the other
ones by version.
