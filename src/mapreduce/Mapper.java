// package final_milestone_files;
package mapreduce;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Mapper extends Remote {

    HashMap<String, ArrayList<Integer>> map(String k, String v) throws RemoteException;

}
