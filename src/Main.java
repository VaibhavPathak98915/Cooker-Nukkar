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