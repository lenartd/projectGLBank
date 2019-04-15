package sk.itsovy.UserInterface;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sk.itsovy.Database.Database;

public class NewClientController {

    public TextField fnameBox;
    public TextField lnameBox;
    public TextField mailBox;

    public void addClient(ActionEvent actionEvent)
    {
        Database db = Database.getInstanceDB();
        if(db.addClient(fnameBox.getText(), lnameBox.getText(), mailBox.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Client creation");
            alert.setHeaderText("Client added successfully");
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
