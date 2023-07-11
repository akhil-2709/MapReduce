package mapreduce;
// package final_milestone_files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MRMaster {
    String mapperType;
    String reducerType;

    public void mapReduce(int N, String input_file, String mapperType, String reducerType) {

        List<Process> map_processes = new ArrayList<>();
        List<Process> reducer_processes = new ArrayList<>();
        Properties properties = new Properties();

        int numlines;
        int num_mapper_reducer = N;

        int faulty_mapper_id = (int) Math.floor(Math.random() * num_mapper_reducer);


        //Deleting older intermedite redundant files for new process run
        for (File file : new java.io.File("src/input_splits").listFiles())
            if (!file.isDirectory())
                file.delete();

        for (File file : new java.io.File("src/intermediate_splits").listFiles())
            if (!file.isDirectory())
                file.delete();

        for (File file : new java.io.File("src/output_splits").listFiles())
            if (!file.isDirectory())
                file.delete();

        //Creating new input split files for each mapper to work on
        try {
            BufferedReader br_read = new BufferedReader(new FileReader(input_file));
            List<String> linesList = new ArrayList<String>();
            String line;
            while ((line = br_read.readLine()) != null) {
                linesList.add(line.replaceAll("[-+.^:;,]", ""));
            }

            numlines = linesList.size();
            try {
                BufferedWriter[] br_write = new BufferedWriter[num_mapper_reducer];

                for (int s = 0; s < num_mapper_reducer; s++) {
                    String input_split_file_name = "src/input_splits/InputSplit_";
                    String a = input_split_file_name.concat(String.valueOf(s)).concat(".txt");
                    br_write[s] = new BufferedWriter(new FileWriter(a));
                }

                int group_id;
                String linee;

                for (int k = 0; k < numlines; k++) {

                    group_id = (k + 1) % num_mapper_reducer;
                    linee = linesList.get(k);
                    br_write[group_id].newLine();
                    br_write[group_id].write(linee);
                }

                for (int i = 0; i < num_mapper_reducer; i++) {
                    br_write[i].close();
                }

            } catch (IOException ex) {
                System.err.println(ex);
            }
            br_read.close();

        } catch (IOException ex) {
            System.err.println(ex);
        }

        //Creating new processes and running mapper workers
        for (int i = 0; i < num_mapper_reducer; i++) {
            String input_split_file_name = "src/input_splits/InputSplit_";
            String inputFileName = input_split_file_name.concat(String.valueOf(i)).concat(".txt");
            ProcessBuilder pbMapper = new ProcessBuilder("java","-classpath", "src",
                    "mapreduce/workermap", mapperType,
                    inputFileName, String.valueOf(num_mapper_reducer), String.valueOf(i));

            Process mapperProcess;
            try {
                mapperProcess = pbMapper.inheritIO().start();
                map_processes.add(mapperProcess);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        int timeout = 40000;

        // handling a faulty mapper

        try {
            for (int mp = 0; mp < map_processes.size(); mp++) {
                boolean isTimedOut = false;
                if (!map_processes.get(mp).waitFor(timeout, TimeUnit.MILLISECONDS)) {
                    map_processes.get(mp).destroy();
                    isTimedOut = true;
                }
                if (mp < num_mapper_reducer && (isTimedOut == true || mp == faulty_mapper_id)) {
                    System.out.println("Mapper" + "_" + faulty_mapper_id + " failed!");
                    // Thread.sleep(200);
                    System.out.println("Trying again...");
                    String input_split_file_name = "src/input_splits/InputSplit_";
                    String inputFileName = input_split_file_name.concat(String.valueOf(mp)).concat(".txt");

                    ProcessBuilder pbMapper = new ProcessBuilder("java","-classpath", "src",
                            "mapreduce/workermap", mapperType,
                            inputFileName, String.valueOf(num_mapper_reducer), String.valueOf(mp));
                    Process mapperProcess = pbMapper.inheritIO().start();
                    map_processes.add(mapperProcess);
                    // System.out.println("Mapper" + "_" + faulty_mapper_id + " finished
                    // successfully!");
                } else {
                    int k;
                    k = mp >= num_mapper_reducer ? faulty_mapper_id : mp;
                    System.out.println("Mapper" + "_" + k + " finished successfully!");
                    continue;
                }
            }

            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // After mapper processes complete
        // start reducers
        //Creating new processes and running reducer workers
        for (int j = 0; j < num_mapper_reducer; j++) {
            String intermediate_split_file_name = "src/intermediate_splits/IntermediateSplit_";
            String intermediateFileName = intermediate_split_file_name.concat(String.valueOf(j)).concat(".txt");
            ProcessBuilder pbReducer = new ProcessBuilder("java","-classpath", "src",
                    "mapreduce/workerreduce", reducerType,
                    intermediateFileName, String.valueOf(num_mapper_reducer), String.valueOf(j));

            Process reducerProcess;
            try {
                reducerProcess = pbReducer.inheritIO().start();
                reducer_processes.add(reducerProcess);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        //Ensuring all reeducers completed successfully
        try {
            for (int rp = 0; rp < reducer_processes.size(); rp++) {
                boolean isTimedOut = false;
                if (!reducer_processes.get(rp).waitFor(timeout, TimeUnit.MILLISECONDS)) {
                    reducer_processes.get(rp).destroy();
                    isTimedOut = true;
                } else if (reducer_processes.get(rp).exitValue() == 0) {
                    System.out.println("Reducer" + "_" + rp + " finished successfully!");
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // merging all output files
        String final_op_file_name = "src/final_output/";
        String temp_map = mapperType.split("\\.")[2];
        String temp_reduce = reducerType.split("\\.")[2]; 
        String finalOutputFileName = final_op_file_name.concat(temp_map).concat("_").concat(temp_reduce)
                .concat(".txt");
        HashMap<String, String> final_hm = new HashMap<>();

        //Merging outputs by each reducer to form final output
        for (int s = 0; s < num_mapper_reducer; s++) {

            String output_split_file_name = "src/output_splits/OutputSplit_";
            String outputFileName = output_split_file_name.concat(String.valueOf(s)).concat(".txt");
            try {
                BufferedReader br_read = new BufferedReader(new FileReader(outputFileName));

                String line;

                while ((line = br_read.readLine()) != null) {
                    String[] parts = line.split(":");
                    String key = parts[0];
                    String val = parts[1];
                    if (!final_hm.containsKey(key)) {
                        final_hm.put(key, val);
                    } else {
                        continue;
                    }
                }
                br_read.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(finalOutputFileName));
            for (Map.Entry outputEntry : final_hm.entrySet()) {

                bw.write(outputEntry.getKey() + ":" + outputEntry.getValue() + "\n");

            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mapperType == "application.wordcount.mapperwc") {
            System.out.println("\nMap Reduce job completed for Wordcount\n");
        } else if (mapperType == "application.wordlength.mapperwl") {
            System.out.println("\nMap Reduce job completed for Word Length\n");
        } else if (mapperType == "application.minmaxtemp.mapperweather") {
            System.out.println("\nMap Reduce job completed for Temperature Min-Max\n");
        }

    }
}
