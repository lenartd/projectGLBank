package sk.itsovy.UserInterface;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sk.itsovy.Database.Database;

import java.util.Random;

public class NewClientController {

    public TextField fnameBox;
    public TextField lnameBox;
    public TextField mailBox;

    Database db = Database.getInstanceDB();
    public void addClient(ActionEvent actionEvent)
    {
        int idnew = db.addClient(fnameBox.getText(), lnameBox.getText(), mailBox.getText());
        if(idnew >- 1)
        {
            String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            String pass = "";
            String username = fnameBox.getText();
            Random random = new Random();
            for(int i = 0; i<8; i++)
            {
                pass += alphabet.charAt(random.nextInt(62));
            }
            for(int i = 0; i<3; i++)
            {
                username += Integer.toString(random.nextInt(11));
            }
            db.newClientLogin(idnew, username, pass);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Client creation");
            alert.setHeaderText("Client added successfully");
            alert.setContentText("Username:  " + username + "\nPassword:   " + pass);
            alert.showAndWait();
        }
        else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Client creation");
                alert.setHeaderText("Failed to add client");
                alert.showAndWait();
            }
    }

    public void deleteText(MouseEvent mouseEvent)
    {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2)
            {
                TextField tfield = (TextField) mouseEvent.getSource();
                tfield.setText("");
            }
        }
    }
}
