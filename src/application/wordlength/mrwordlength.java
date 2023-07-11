// package final_milestone_files;
package application.wordlength;
import mapreduce.MRMaster;
// import final_milestone_files.MRMaster;
// import final_milestone_files.*;

import java.io.File;
import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileWriter;
import java.io.File;

public class mrwordlength {
    public static void main(String[] args) {

        Properties properties = new Properties();
        String inputdata, intermediate_data;
        try {
            FileReader config_file = new FileReader("src/config");
            properties.load(config_file);

            int N = Integer.parseInt(properties.getProperty("N"));
            inputdata = properties.getProperty("inputdata_wc");
            intermediate_data = properties.getProperty("intermediate_wc");
            String output_dir = "";
            String mapperType = "application.wordlength.mapperwl";
            String reducerType = "application.wordlength.reducerwl";

            MRMaster obj = new MRMaster();
            //calling map reduce functions
            obj.mapReduce(N, inputdata, mapperType, reducerType);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
