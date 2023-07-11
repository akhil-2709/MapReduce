// package final_milestone_files;
package application.wordcount;
import mapreduce.Reducer;

import java.io.*;
import java.util.*;
// import final_milestone_files.Mapper;

public class reducerwc implements Reducer {

    public reducerwc() {
        super();
    }

    //user defined reduce method
    public ArrayList<Integer> reduce(String key, ArrayList<Integer> valueList) {

        ArrayList<Integer> result = new ArrayList<Integer>();
        int count = 0;
        // add all the counts in list to get the total count
        for (int val : valueList) {
            try {
                count += val;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.add(count);

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
