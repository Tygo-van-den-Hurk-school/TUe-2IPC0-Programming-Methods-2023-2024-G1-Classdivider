```
Made by Tygo van den Hurk, a student of the Technical University of Eindhoven, The Netherlands.
With Student Number: 1705709
 
On 12th of december 2023, at 11:35 CET
 
for the assignment of 2IPC0 Programming methods, 
G1a: Add Different Class Dividing Strategies to the Classdivider Project
```

# Classdivider

*Classdivider* divides a class of students into groups of a given size, +/- a
given deviation. For example, to divide the example class listed in
`students.lst` into groups of 4 +/- 2 students, run *classdivider* as
follows:
  
```bash
java -jar target/classdivider-0.7.jar -g 4 -d 2 students.lst
```

## Usage

```
Usage: classdivider [-hV] [-d=<deviation>] -g=<groupSize> [-s=<strategy>]
                    <studentsFile>
Divide a class of students into groups.
      <studentsFile>   path to file with students data in CSV format
  -d, --deviation=<deviation>
                       allowed difference of number of students in a group  and
                         the target group size. Defaults to 1.
  -g, --group-size=<groupSize>
                       target group size.
  -h, --help           Show this help message and exit.
  -s, --strategy=<strategy>
                       class division strategy. Pick one of: Random. Defaults
                         to Random.
  -V, --version        Print version information and exit.
```

## Building and running *classdivider*

Because *classdivider* uses two external libraries, one for reading CSV files
and one to make nice command-line interfaces, you need to package the
dependencies when building the program.

On the terminal, run:
  
```bash
mvn clean compile assembly:single
```

This actions creates Jar file `target/classdivider-0.7.jar`.

We created a "Maven action" for this in NetBeans called *build*. So, you can
build this project with all dependencies also via the project context menu
*Run Maven* → *build*.

After building the program, you run it in a terminal. In the terminal, change
directory to the project's root directory and run:

```bash
java -jar target/classdivider-0.7.jar -g 4 -d 2 students.lst
```
