// package final_milestone_files;
// package final_milestone_files;
package application.minmaxtemp;
import mapreduce.Reducer;

import java.io.*;
import java.util.*;
// import final_milestone_files.Mapper;

public class reducerweather implements Reducer {

    public reducerweather() {
        super();
    }

    //user defined reduce method
    public ArrayList<Integer> reduce(String key, ArrayList<Integer> valueList) {

        ArrayList<Integer> result = new ArrayList<Integer>();
        int min = Collections.min(valueList);
        int max = Collections.max(valueList);
        result.add(min);
        result.add(max);

        return result;

    }
     //removing special characters from intermediate text file for input to reducer
    static ArrayList<String> data_processing(BufferedReader wordfile) {
        ArrayList<String> linesList = new ArrayList<String>();
        String line = null;
        try {
            while ((line = wordfile.readLine()) != null) {
                linesList.add(line.replaceAll("[-+.^:;,]", ""));
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return linesList;
    }
}
