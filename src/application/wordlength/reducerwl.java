// package final_milestone_files;
package application.wordlength;
import mapreduce.Reducer;

import java.io.*;
import java.util.*;
// import final_milestone_files.Mapper;

public class reducerwl implements Reducer {

    public reducerwl() {
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

}
