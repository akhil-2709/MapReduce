// package final_milestone_files;
package mapreduce;
import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.*;
import java.lang.*;
import java.lang.reflect.Constructor;

public class workerreduce {

    //method to read from intermediate files and after reduce process and write to output files
    private HashMap<String, ArrayList<Integer>> convertFileToHashMap(String intermediateFilePath) {
        File dir = new File(intermediateFilePath);
        HashMap<String, ArrayList<Integer>> hm_intermediate = new HashMap<>();
        try {

            FileReader fw = new FileReader(dir);
            BufferedReader br = new BufferedReader(fw);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String key = parts[0];
                String temp = parts[1].substring(1, parts[1].length() - 1);
                String[] values = temp.split(",");
                ArrayList<Integer> x = new ArrayList<Integer>();

                for (String s : values) {

                    x.add(Integer.parseInt(s.trim()));
                }
                hm_intermediate.put(key, x);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hm_intermediate;

    }

    // execute function of the reducer

    public void execute(Reducer obj, HashMap<String, ArrayList<Integer>> intermediateFilehHashMap, int num_processes,
            String process_id) {

        String outFileName = "src/output_splits/OutputSplit_";
        String intermediateFileName = "src/intermediate_splits/IntermediateSplit_";
        int process_n = Integer.parseInt(process_id);
        String outputFileLocation = outFileName.concat(process_id).concat(".txt");

        // For all keys in the intermediate hashmap, we need to search all intermediate
        // files for that key
        // and update the current hashmap

        for (Map.Entry entry : intermediateFilehHashMap.entrySet()) {
            for (int t = 0; t < num_processes; t++) {
                if (t == process_n) {
                    continue;
                } else {
                    String intermediateFileLocation = intermediateFileName.concat(Integer.toString(t))
                            .concat(".txt");
                    HashMap<String, ArrayList<Integer>> hm = convertFileToHashMap(intermediateFileLocation);
                    ArrayList<Integer> temp = new ArrayList<Integer>();
                    ArrayList<Integer> temp2 = new ArrayList<Integer>();
                    String k = entry.getKey().toString();

                    if (hm.containsKey(k)) {
                        temp = hm.get(k);
                        temp2 = intermediateFilehHashMap.get(k);
                        temp2.addAll(temp);
                        intermediateFilehHashMap.put(k, temp2);

                    } else {
                        continue;
                    }

                }

            }

        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileLocation));

            for (Map.Entry mapItem : intermediateFilehHashMap.entrySet()) {
                ArrayList<Integer> reduceOutput = null;
                String temp = null, val = null;

                try {
                    String keyWord = mapItem.getKey().toString();

                    ArrayList<Integer> valueArrayList = intermediateFilehHashMap.get(keyWord);

                    reduceOutput = obj.reduce(keyWord, valueArrayList);
                    temp = reduceOutput.toString();
                    val = temp.substring(1, temp.length() - 1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    bw.write(mapItem.getKey() + ":" + val + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        String reducerType = args[0];
        String intermediateFile = args[1];
        String num_processes = args[2];
        String process_id = args[3];

        MRMaster mr = new MRMaster();

        //getting user defined reducer object
        Class<?> obj = null;
        obj = Class.forName(reducerType);

        Constructor<?> cons = obj.getConstructor();
        Reducer object = (Reducer) cons.newInstance();

        workerreduce reduceworker = new workerreduce();

        HashMap<String, ArrayList<Integer>> tempData = reduceworker.convertFileToHashMap(intermediateFile);

           //running user defined reducer functions
        reduceworker.execute(object, tempData, Integer.parseInt(num_processes), process_id);

    }

}
