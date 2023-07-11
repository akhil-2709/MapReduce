package mapreduce;
// package final_milestone_files;
import java.util.*;

public interface Reducer {
    ArrayList<Integer> reduce(String key, ArrayList<Integer> values);
}
