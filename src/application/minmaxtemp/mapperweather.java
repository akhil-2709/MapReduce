// package final_milestone_files;
package application.minmaxtemp;
import mapreduce.Mapper;

import java.io.*;
import java.util.*;
// import final_milestone_files.Mapper;

public class mapperweather implements Mapper {

    public mapperweather() {
        super();
    }

    public HashMap<String, ArrayList<Integer>> map(String process_id, String fileLocation) {
        HashMap<String, ArrayList<Integer>> hm = new HashMap<String, ArrayList<Integer>>();
        try {
            BufferedReader br_read = new BufferedReader(new FileReader(fileLocation));
            Scanner sc = new Scanner(new File(fileLocation));
            // sc.useDelimiter(",");
            sc.useDelimiter("\\n");
            while (sc.hasNext()) {
                String x = sc.next().toString();
                String[] ar = new String[2];
                // ar = x.split(",");
                ar = x.split(" ");
                ar[1] = ar[1].toString().trim();
                String year = ar[0].substring(ar[0].length() - 4);

                ArrayList<Integer> al1 = new ArrayList<Integer>();
                if (!hm.containsKey(year))
                    hm.put(year, al1);

                if (ar[1] != null && !ar[1].isEmpty()) {
                    int t = Integer.parseInt(ar[1]);
                    ArrayList<Integer> temp = hm.get(year);
                    temp.add(t);
                    hm.put(year, temp);
                }

            }
            sc.close();

        } catch (IOException ex) {
            System.err.println(ex);
        }
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
