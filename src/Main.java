import resources.SQLInteractor;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main{
    private static final String JDBC_URL = "jdbc:sqlite:Recipies.db";
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        SQLInteractor db = new SQLInteractor();
        while(db.getcon()){
            System.out.print("Enter Recipe id:");
            int rid = sc.nextInt();
            try{
                ResultSet result_set = db.getRecipeById(rid);
                while(result_set.next()){
                    System.out.println("ID:"+result_set.getInt("id"));
                    System.out.println("Title:"+result_set.getString("Title"));
                    System.out.println("Ingredients:"+result_set.getString("Ingredients"));
                    System.out.println("Instructions:"+result_set.getString("Instructions"));
                }
            }
            catch(SQLException e){
                System.out.println("SQL Exception Occured:"+e.getMessage());
            }
            catch(Exception e){
                System.out.println("Some Random Error Occured:"+e.getMessage());
            }
        }
        sc.close();
        db.close();
    }
}