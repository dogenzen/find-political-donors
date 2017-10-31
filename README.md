# Table of Contents
1. [Insight Coding Challenge](README.md#insight-coding-challenge)
2. [Run Instructions](README.md#run-instructions)
3. [Dependencies](README.md#dependencies)
4. [Approach](README.md#approach)

# Insight Coding Challenge

Refer to this [Insight link](https://github.com/InsightDataScience/find-political-donors) for details of this coding challenge.  

## Run Instruction

`./run.sh` compiles and executes the program

The directory from which `./run.sh` to be executed *expects* the following directory structure

#### Directory Structure Dependency

    ├── run.sh
    ├── src
    │   └── main
    │       └── java
    ├── input
    │   └── itcont.txt
    ├── output
    |   └── medianvals_by_zip.txt
    |   └── medianvals_by_date.txt

- `./run.sh` compiles the source files under src/main/java
- It creates target/classes folder to store the compiled classes
- Expects the input file to be stored under input directory as  input/itcont.txt
- Expects the output files to be created under output directory as output/medianvals_by_zip.txt and output/medianvals_by_date.txt

## Dependencies
`Standard Java-8 Package`

- **No external libraries are used in this program.**
- Only Java-8 functional programming support and standard java language libraries are used.


## Approach

### Assumptions

- It's assumed that negative numbers are allowed as valid contributions.
- It's assumed that dates behave as per Java's string to date conversion where months and days rollover. For example, a month value of 13 rolls over to next year 1 (January) etc.
- It's assumed that all the processed data will fit in memory.

### Design Considerations
The code is structured as a set of layered modules, which provides a general framework for processing each contribution for any running and aggregate computations. This is achieved with proper abstraction and encapsulation of classes with internal data-structures.

##### Currency computation     
Currency computation is done by converting each number to cents (by multiplying with 100) and then storing it as `long`.
The value is converted back after all additions or averages are computed for reporting and rounded to whole dollar.

##### Sorting
Sorting and aggregation of dates is done using a map of maps.

Recipient Id is stored as key in a `TreeMap` so that they are sorted alphabetically. The values are stored as `HashMap` with contribution date as key. The entries are then sorted chronologically using a custom comparator.  

##### Mock Stream
The donations data is read from the input file line by line and a Java stream class `Stream<String>` is used to mock as if the data is read from a stream.

### Space considerations
It's *assumed* that the processed donation entries will fit in memory.

The following data is extracted and stored in memory
- All the individual contribution amounts, and running count and total for each recipient and zipcode combination.
- All the individual contribution amounts, and aggregate count and total for each recipient and date combination.

### Runtime considerations
For computing runtime median from a stream requires consideration of insertion time to store the data and computing the median.

Using a max-heap and a min-heap for each combination requires O(log n) time for insertion and O(1) constant time to compute the median.
