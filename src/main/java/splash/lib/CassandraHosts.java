package splash.lib;

import com.datastax.driver.core.*;

import java.util.Iterator;
import java.util.Set;

public final class CassandraHosts{
    private static Cluster cluster;
    
    private static final String HOST = "127.0.0.1";
    //static String Host = "52.19.178.188";
    //static String Host = "52.31.166.123";
    //static String Host = "52.31.171.234";

    public CassandraHosts(){
        // Empty constructor
    }

    public static String getHost(){
        return(HOST);
    }

    public static String[] getHosts(Cluster cluster){
        if(cluster == null){
            cluster = Cluster.builder().addContactPoint(HOST).build();
        }
        Metadata mdata = cluster.getMetadata();
        Set<Host> hosts = mdata.getAllHosts();
        String sHosts[] = new String[hosts.size()];

        Iterator<Host> it = hosts.iterator();
        int i = 0;
        while(it.hasNext()){
            Host ch = it.next();
            sHosts[i] = (String) ch.getAddress().toString();
            i++;
        }
        return sHosts;
    }

    public static Cluster getCluster(){
        cluster = Cluster.builder().addContactPoint(HOST).build();
        getHosts(cluster);
        Keyspaces.SetUpKeySpaces(cluster);
        return cluster;
    }

    public void close(){
        cluster.close();
    }
}