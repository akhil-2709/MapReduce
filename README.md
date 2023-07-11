Design Document (Intermediate milestone)


Project contents so far:

We have implemented the following 3 test cases (Single Process Single Threaded) for the intermediate milestone:

    1. WordCount: Frequency of every unique word in the document.
    2. WordLength: Number of words in the document for each word length.
    3. Minimum and Maximum Temperature for every year.

Following are the java files involved in the project so far:

    • Library programs:

    a) mapper.java (Library) : Contains all the map functions for the 3 required applications.
    b) meducer.java (Library): Contains all the reduce functions for the 3 required applications.

    • Test case programs:

    1. wordcountmr.java: Runs wordcount test-case using the map & reduce functions from the library programs.
    2. wordlengthmr.java: Runs word length test-case using the map & reduce functions from the library programs.
    3. Minmaxmr.java: Runs yearly-minmax temperature test-case using the map & reduce functions from the library programs.

    • Testing script: test_script_mr.sh

How do these programs work:

    • All the Test case programs run in a similar fashion. The mapper object takes a file reader object of an input data file and runs the application specific map function. 
    • The map function runs the user defined logic for that particular application and outputs the intermediate data into intermediate files.
    • The application specific reduce function of the reducer class then reads the intermediate data files and performs the required aggregation. Once the operations are complete, the final results are written to an output file.



How to run the test cases:

    • Download the code from the “project-1-group_akhilprasannmuneer” folder and set the current directory to the downloaded folder.
    • Run the following commands in terminal to execute the 3 test cases:
        ◦ chmod +x test_script_mr.sh  (To set execute permissions for the testing script)
        ◦ ./test_script_mr.sh (Command to run testing script)
    • Once the test cases run, check the output files in the doc folder.


