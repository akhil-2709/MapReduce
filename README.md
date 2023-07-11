
We have implemented the following 3 test cases using MapReduce System:

    1. WordCount: Frequency of every unique word in the document.
    2. WordLength: Number of words in the document for each word length.
    3. Minimum and Maximum Temperature for every year.

Following are the java files involved in the project so far:

    • Library programs:

    a) MRMaster.java (Library): Functions as the master and splits the data and communicates and manages the workers. Also performs             fault tolerance.
    b) Mapper.java (Library): Contains the general Mapper class interface.
    c) Reducer.java (Library): Contains the general Reducer class interface.
    d) workermap.java (Library): Executes the map function and writes intermediate files.
    e) workerreduce.java (Library): Executes the reduce function and writes output files.

    • Test case programs:

    1. wordcountmr.java: Runs wordcount test-case using the map & reduce functions from the library programs.
    2. wordlengthmr.java: Runs word length test-case using the map & reduce functions from the library programs.
    3. Minmaxmr.java: Runs yearly-minmax temperature test-case using the map & reduce functions from the library programs.

    • Testing script: test_script_mr.sh


How to run the test cases:

    ● Download the code from the “project-1-group_akhilprasannmuneer” folder.
    ● Run the following commands in terminal to execute the 3 test cases:
        ○  chmod +x test_script_mr.sh (To set execute permissions for the testing script)
        ○  ./src/test_script_mr.sh (Command to run testing script)
    ● Once the test cases run, check the output files for each map reduce job in the “final_outputs” folder.
    ● The input splits which the master makes can be found in the “input_splits” folder.
    ● You may use the config file to configure the MapReduce task. For example, you may change the value of N to test with different             number of map/reduce workers
    ● If you wish to run each test case separately, then do the following:
        ○ For Word Count, run the following commands
            ■ javac src/**/*.java
            ■ java -classpath src application/wordcount/mrwordcount
        ○ For Word Length, run the following commands
            ■ javac src/**/*.java
            ■ java -classpath src application/wordcount/mrwordlength
        ○ For Temperature Min-Max, run the following commands
            ■ javac src/**/*.java
            ■ java -classpath src application/wordcount/mrweather


