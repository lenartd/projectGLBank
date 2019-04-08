package sk.itsovy;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sk.itsovy.Database.Database;

import java.awt.*;

public class Controller {
    public TextField usernameField;
    public PasswordField passwordField;
    public Label errorlabel;

    public void verifyUser(ActionEvent actionEvent)
    {
        Database dbase = Database.getInstanceDB();
        if(dbase.matchUser(usernameField.getText(), passwordField.getText()))
        {
            System.out.println("User exists");
            errorlabel.setVisible(false);
            usernameField.setText("");
            passwordField.setText("");
        }
        else
            {
                System.out.println("User does not exist");
                errorlabel.setVisible(true);
            }
    }
}
