package resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLInteractor{
    private static final String JDBC_URL = "jdbc:sqlite:Recipies.db";
    private Connection con = null;
    private ResultSet rs = null;
    private Statement st = null;
    /*The connection with the sqlite db will be established by making the
    --Object of the SQLInteractor class by using the default constructor */
    public SQLInteractor(){
        try{
            con = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connection to Database Established...");
            st = con.createStatement();
            st.setQueryTimeout(30);
        }
        catch(SQLException e){
            System.out.println("An SQL Exception Occured: "+e.getMessage());
        }
        catch(Exception e){
            System.out.println("A Random Exception Occured: "+e.getMessage());
        }
    }
    //Method to check connection status
    public boolean getcon(){
        if(con!=null){
            return true;
        }
        return false;
        //Returns true for connected and false for Disconnected
    }
    //Methods to pass queries to the DB
    public ResultSet getRecipeById(int id) throws SQLException{
        String fQuery = String.format("SELECT * FROM Recipies WHERE id=%d;",id);
        rs = st.executeQuery(fQuery);
        return rs;
    }
    public ResultSet getRecipeByTitle(String Title) throws SQLException{
        String fQuery = String.format("SELECT * FROM Recipies WHERE Title LIKE '%%s%';",Title);
        rs = st.executeQuery(fQuery);
        return rs;
    }
    /*The connection from the database will be closed by using the close method
    --of the SQLInteractor. The object will be of no use after using close method */
    public void close(){
        try{
            if(con!=null){
                con.close();
                System.out.println("...Connection to the Database closed");
            }
        }
        catch(SQLException e){
            System.out.println("SQL Exception Occured: "+ e.getMessage());
        }
        catch(Exception e){
            System.out.println("A Random Exception Occured: "+e.getMessage());
        }
    }
}