import resources.LoginFrame;
import resources.SQLInteractor;

import javax.swing.SwingUtilities;


public class Main{
    public static void main(String[] args) {
        SQLInteractor db =new SQLInteractor();
        if(db.getcon()){
            SwingUtilities.invokeLater(()->new LoginFrame(db));
        }
        else{
            db.close();
        }
    }
}

/*
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
*/

/*    
Old main function
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        SQLInteractor db = new SQLInteractor();
        SwingUtilities.invokeLater(LoginFrame::new);
        while(db.getcon()){
            System.out.print("Search:");
            String Title = sc.nextLine();
            if(Title==""){
                break;
            }
            try{
                ResultSet result_set = db.searchByTitle(Title);
                while(result_set.next()){
                    System.out.println("ID:"+result_set.getInt("id"));
                    System.out.println("Title:"+result_set.getString("Title"));
                    String ImageFileName = result_set.getString("Image_Name")+".jpg";
                    System.out.println("Image Name:"+ImageFileName);
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
    }*/