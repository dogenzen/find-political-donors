#!/bin/bash
#
# Use this shell script to compile (if necessary) your code and then execute it. Below is an example of what might be found in this file if your program was written in Python
#
#python ./src/find_political_donors.py ./input/itcont.txt ./output/medianvals_by_zip.txt ./output/medianvals_by_date.txt
mkdir -p target/classes
javac -d target/classes -sourcepath src/main/java src/main/java/**/*.java src/main/java/*.java
java -cp target/classes Main ./input/itcont.txt ./output/medianvals_by_zip.txt ./output/medianvals_by_date.txt
