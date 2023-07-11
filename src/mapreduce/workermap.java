// package final_milestone_files;
package mapreduce;
import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.*;
import java.lang.reflect.Constructor;

public class workermap {

    // method to read from input file and write to intermediate file
    public void execute(Mapper obj, String inputFile, int num_processes, String process_id) {

        try {
            HashMap<String, ArrayList<Integer>> output = obj.map(process_id, inputFile);
            List<BufferedWriter> bufferedWriters = new ArrayList<>();

            String intermediate_split_file_name = "src/intermediate_splits/IntermediateSplit_";
            // String a = intermediate_split_file_name.concat(process_id).concat(".txt");

            for (int i = 0; i < num_processes; i++) {
                String a = intermediate_split_file_name.concat(String.valueOf(i)).concat(".txt");

                File file = new File(a);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bufferedWriters.add(bw);

            }
            output.forEach((key, value) -> {
                // hash key (String) to an integer between 0 and N
                // This ensures that all keys go to the same file
                // No shuffling of keys required later.
                int hash_value = Math.abs(key.hashCode()) % num_processes;
                // value.forEach(val -> {
                try {
                    BufferedWriter bufferedWriter = bufferedWriters.get(hash_value);
                    bufferedWriter.write(key + ":" + value);
                    bufferedWriter.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // });
            });
            for (BufferedWriter bufferedWriter : bufferedWriters) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {

        String mapperType = args[0];
        String inputFile = args[1];
        String num_processes = args[2];
        String process_id = args[3];

        MRMaster mr = new MRMaster();

        Class<?> obj = null;
        obj = Class.forName(mapperType);

        //getting user defined mapper object

        Constructor<?> cons = obj.getConstructor();
        // Constructor<?> cons = obj.getConstructor(String.class);
        Mapper object = (Mapper) cons.newInstance();

        workermap mapworker = new workermap();
        //running user defined mapper functions
        mapworker.execute(object, inputFile, Integer.parseInt(num_processes), process_id);

    }

}
