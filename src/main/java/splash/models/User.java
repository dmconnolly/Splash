package splash.models;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import splash.lib.CassandraHosts;

public class User{
    private final Cluster cluster;
    
    public User(){
        this.cluster = CassandraHosts.getCluster();
    }
    
    public void registerUser(String username, String password, String name){
        try(Session session = cluster.connect("Splash")) {
            PreparedStatement ps = session.prepare("INSERT INTO user (username, password, name) Values(?,?,?)");
            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute(boundStatement.bind(username, password, name));
        }
    }
    
    public boolean checkExisting(String username){
        try(Session session = cluster.connect("Splash")){
            PreparedStatement ps = session.prepare("SELECT username FROM user WHERE username=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            ResultSet rs = session.execute(boundStatement.bind(username));
            return !rs.isExhausted();
        }
    }
    
    public boolean checkPassword(String password, String username){
        ResultSet rs;
        try(Session session = cluster.connect("Splash")){
            PreparedStatement ps = session.prepare("SELECT password FROM user WHERE username=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
        }
        return rs.one().getString("password").equals(password);
    }
    
    public String getName(String username){
        ResultSet rs;
        String name;
        try(Session session = cluster.connect("Splash")) {
            PreparedStatement ps = session.prepare("SELECT name FROM user WHERE username=?");
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute(boundStatement.bind(username));
            name = rs.one().getString("name");
        }
        return name;
    }
}
