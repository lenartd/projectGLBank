package sk.itsovy.UserInterface;

import javafx.scene.control.Label;
import sk.itsovy.Database.Database;

public class mainController {
    public Label userlabel;

    Database dbase = Database.getInstanceDB();

    public void initialize()
    {
        userlabel.setText(userlabel.getText() +  dbase.getEmployee().getFirstname() + " " + dbase.getEmployee().getLastname());
    }


}