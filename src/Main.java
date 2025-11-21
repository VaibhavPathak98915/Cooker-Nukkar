import resources.SQLInteractor;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main{    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        SQLInteractor db = new SQLInteractor();
        while(db.getcon()){
            System.out.print("Enter Title:");
            String Title = sc.nextLine();
            if(Title==""){
                break;
            }
            try{
                ResultSet result_set = db.getRecipeByTitle(Title);
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