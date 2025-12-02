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
            st.executeUpdate("PRAGMA foreign_key = ON;");
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
    //Method to check User during Login
    public boolean checkUser(String UserName, String Password) throws SQLException{
        String a="";
        if(Password.equals(a)){
            return false;
        }
        String Query = "SELECT Password FROM Users WHERE UserName='"+UserName+"';";
        ResultSet temprs = st.executeQuery(Query);
        String temp_pass = temprs.getString("Password");
        if(temp_pass.equals(Password)){
            return true;
        }
        return false;
        //Returns true for Correct UN-PW combination and false otherwise
    }
    //Methods to pass SELECT queries to the DB
    public ResultSet searchByTitle(String Title) throws SQLException{
        String Query = "SELECT id, Title FROM Recipies WHERE Title LIKE '%"+Title+"%' ORDER BY CASE WHEN Bookmark = 1 THEN 0 ELSE 1 END, Title;";
        rs = st.executeQuery(Query);
        return rs;
    }
    public ResultSet searchByIngredient(String Ingredients) throws SQLException{
        String Query = "SELECT id, Title FROM Recipies WHERE Ingredients LIKE '%"+Ingredients+"%' ORDER BY CASE WHEN Bookmark = 1 THEN 0 ELSE 1 END, Title;";
        rs = st.executeQuery(Query);
        return rs;
    }
    public ResultSet getRecipeById(int id) throws SQLException{
        String fQuery = String.format("SELECT * FROM Recipies WHERE id=%d;",id);
        rs = st.executeQuery(fQuery);
        return rs;
    }
    public ResultSet getRecipeByTitle(String Title) throws SQLException{
        String Query = "SELECT * FROM Recipies WHERE Title LIKE '%"+Title+"%';";
        rs = st.executeQuery(Query);
        return rs;
    }
    public ResultSet getBookmarks() throws SQLException{
        String Query = "SELECT * FROM Recipies WHERE Bookmark=1;";
        rs = st.executeQuery(Query);
        return rs;
    }
    public ResultSet getHistory() throws SQLException{
        String Query = "SELECT * FROM History;";
        rs = st.executeQuery(Query);
        return rs;
    }
    public ResultSet getUsers() throws SQLException{
        String Query = "SELECT * FROM Users;";
        rs = st.executeQuery(Query);
        return rs;
    }
    //Methods to pass INSERT queries to the DB
    public void addRecipe(String Title, String Ingredients, String Instructions, String Image_Name) throws SQLException{
        int id = nextRid();
        String Query = "INSERT INTO Recipies VALUES ("+id+",'"+Title+"','"+Ingredients+"','"+Instructions+"','"+Image_Name+"');";
        st.executeUpdate(Query);
    }
    public void addBookmark(int id) throws SQLException{
        String Query = "UPDATE Recipies SET Bookmark = 1 WHERE id="+id+";";
        st.executeUpdate(Query);
    }
    public void removeBookmark(int id) throws SQLException{
        String Query = "UPDATE Recipies SET Bookmark = 0 WHERE id="+id+";";
        st.executeUpdate(Query);
    }
    public void addHistory(int rid, int uid) throws SQLException{
        String Query = "SELECT MAX(aid) FROM History;";
        ResultSet temprs = st.executeQuery(Query);
        int nextAid = temprs.getInt(0);
        Query = "INSERT INTO History VALUES ("+nextAid+","+rid+","+uid+");";
        st.executeUpdate(Query);
    }
    /*The connection from the database will be closed by using the close method
    --of the SQLInteractor. The object will be of no use after using close method */
    public void close(){
        try{
            if(con!=null){
                con.close();
                System.out.println("\n...Connection to the Database closed");
            }
        }
        catch(SQLException e){
            System.out.println("SQL Exception Occured: "+ e.getMessage());
        }
        catch(Exception e){
            System.out.println("A Random Exception Occured: "+e.getMessage());
        }
    }
    //Other methods
    private int nextRid() throws SQLException{
        String Query = "SELECT MAX(id) FROM Recipies;";
        ResultSet temprs = st.executeQuery(Query);
        int current_max_id = temprs.getInt(0);
        return (current_max_id+1);
    }
}