package sk.itsovy.UserInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sk.itsovy.Database.Database;
import sk.itsovy.Employee;
import sk.itsovy.Client;

import java.util.ArrayList;


public class mainController{
    public Label userlabel;
    public Button logoutb;
    public ComboBox clientdrp;
    public Label fnameLabel;
    public Label lnameLabel;
    public Label mailLabel;

    Database dbase = Database.getInstanceDB();

    public void initialize()
    {
        fillUpDropdown();
        //Employee myemp = dbase.getEmployee();
        //userlabel.setText("Logged in as: " +  myemp.getFirstname() + " " + myemp.getLastname());
    }


    public void logout(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            Database.getInstanceDB().closeConnection();
            root = FXMLLoader.load(getClass().getResource("loginForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("GL Bank  Managagement");
            stage.setScene(new Scene(root, 380, 330));
            stage.setResizable(false);
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Stage stage = (Stage) logoutb.getScene().getWindow();
        stage.hide();
    }

    public void fillUpDropdown()
    {
        ArrayList <Client> clientList = dbase.getClients();
        ObservableList<String> cblist = FXCollections.observableArrayList();

        for(int i=0; i<clientList.size(); i++)
        {
            cblist.add(clientList.get(i).getFirstname() + " " + clientList.get(i).getLastname() + " (" + clientList.get(i).getId() + ")");
        }
        clientdrp.setItems(cblist);
    }

    public void showClientData()
    {
        ArrayList <Client> clientList = dbase.getClients();

    }

    public void newClientWindow(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            root = FXMLLoader.load(getClass().getResource("newClientForm.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create New Client");
            stage.setScene(new Scene(root, 330, 265));
            stage.setResizable(false);
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showClientInfo(ActionEvent actionEvent)
    {
        String selectedClient = clientdrp.getSelectionModel().getSelectedItem().toString();

        int clientId = Integer.parseInt(selectedClient.substring(selectedClient.indexOf("(") + 1, selectedClient.indexOf(")")));

        Client client = Database.getInstanceDB().getExactClient(clientId);

        fnameLabel.setText("First name:   " + client.getFirstname());
        lnameLabel.setText("Last name:   " + client.getLastname());
        mailLabel.setText("Email:   " + client.getEmail());
    }
}