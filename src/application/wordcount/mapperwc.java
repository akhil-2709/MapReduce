// package final_milestone_files;
package application.wordcount;
import mapreduce.Mapper;

import java.io.*;
import java.util.*;
// import final_milestone_files.Mapper;

public class mapperwc implements Mapper {

    public mapperwc() {
        super();
    }

    public HashMap<String, ArrayList<Integer>> map(String process_id, String fileLocation) {
        HashMap<String, ArrayList<Integer>> hm = new HashMap<String, ArrayList<Integer>>();
        try {
            BufferedReader br_read = new BufferedReader(new FileReader(fileLocation));
            ArrayList<String> linesList = data_processing(br_read);

            for (int i = 0; i < linesList.size(); i++) {
                String[] arr = linesList.get(i).toString().split(" ");
                for (int j = 0; j < arr.length; j++) {
                    String key = arr[j];
                    ArrayList<Integer> x = new ArrayList<Integer>();
                    if (!hm.containsKey(key)) {
                        x.add(0);
                        hm.put(key, x);
                    }
                    ArrayList<Integer> temp = hm.get(key);
                    temp.set(0, temp.get(0) + 1);

                    hm.put(key, temp);
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        hm.remove("");
        return hm;

    }

        //removing special characters from input text file for input to mapper
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
