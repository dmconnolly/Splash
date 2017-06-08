package splash.stores;

public class loggedIn{
    private boolean loggedIn = false;
    private String username = null;
    private String name = null;
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getUsername(){
        return this.username;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void logIn(){
        this.loggedIn = true;
    }
    
    public void logOut(){
        this.loggedIn = false;
    }
    
    public boolean getLoggedIn(){
        return this.loggedIn;
    }
}
