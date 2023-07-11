#!/bin/bash
#javac *.java $@
javac src/**/*.java
# javac reducer.java $@
# javac wordcountmr.java $@
# javac wordlengthmr.java $@
# javac minmaxmr.java $@


printf "\n"
echo "Running Wordcount test"
java -classpath src application/wordcount/mrwordcount  

echo "Running Wordlength test"
java -classpath src application/wordlength/mrwordlength  

echo "Running Temperature Min-Max test"
java -classpath src application/minmaxtemp/mrweather    
