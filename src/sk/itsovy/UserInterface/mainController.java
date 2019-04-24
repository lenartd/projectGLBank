package sk.itsovy.UserInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sk.itsovy.Account;
import sk.itsovy.Card;
import sk.itsovy.Database.Database;
import sk.itsovy.Employee;
import sk.itsovy.Client;

import java.util.ArrayList;
import java.util.Random;


public class mainController{
    public Label userlabel;
    public Button logoutb;
    public ComboBox clientdrp;
    public Label fnameLabel;
    public Label lnameLabel;
    public Label mailLabel;
    public ComboBox accountNum;
    public Label accID;
    public Label currentBalance;
    public ComboBox accountNum2;
    public ComboBox cardId;
    public Label cardIDLabel;
    public Label pinLabel;
    public Label activeLabel;
    public Label expireLabel;

    Database dbase = Database.getInstanceDB();

    public void initialize()
    {
        fillUpClientDropdown();
        //Employee myemp = dbase.getEmployee();
        //userlabel.setText("Logged in as: " +  myemp.getFirstname() + " " + myemp.getLastname());
    }


    public void logout(ActionEvent actionEvent)
    {
        Parent root;
        try
        {
            dbase.closeConnection();
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

    public void fillUpClientDropdown()
    {
        ArrayList <Client> clientList = dbase.getClients();
        ObservableList<String> cblist = FXCollections.observableArrayList();

        for(int i=0; i<clientList.size(); i++)
        {
            cblist.add(clientList.get(i).getFirstname() + " " + clientList.get(i).getLastname() + " (" + clientList.get(i).getId() + ")");
        }
        clientdrp.setItems(cblist);
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
        Client client = dbase.getExactClient(getCurrentClientId());

        fnameLabel.setText("First name:   " + client.getFirstname());
        lnameLabel.setText("Last name:   " + client.getLastname());
        mailLabel.setText("Email:   " + client.getEmail());

        accountNum.getItems().clear();
        accountNum2.getItems().clear();
        fillUpAccountDropdown(accountNum);
        fillUpAccountDropdown(accountNum2);
        setDefaultCardInfo();
    }

    public int getCurrentClientId()
    {
        String selectedClient = clientdrp.getSelectionModel().getSelectedItem().toString();
        return Integer.parseInt(selectedClient.substring(selectedClient.indexOf("(") + 1, selectedClient.indexOf(")")));
    }

    public void fillUpAccountDropdown(ComboBox comboId)
    {
        ArrayList <Account> account = dbase.getAccountInfo(getCurrentClientId());

        ObservableList<String> cblist = FXCollections.observableArrayList();

        if(account.size() == 0)
        {
            accID.setText("Account ID:");
            currentBalance.setText("Account balance:");
        }
        else
            {
                for(int i=0; i<account.size(); i++)
                {
                    cblist.add(account.get(i).getAccNum());
                }
                comboId.setItems(cblist);
                if(comboId.equals(accountNum))
                {
                    comboId.getSelectionModel().selectFirst();
                    accID.setText("Account ID:   " + account.get(0).getId());
                    currentBalance.setText("Account balance:   " + account.get(0).getAmount() + "  Eur");
                }
                else
                    {
                        comboId.getSelectionModel().selectFirst();
                    }
            }
    }

    public void setDefaultCardInfo()
    {
        ArrayList <Card> card = dbase.getCardInfo(getCurrentClientId());

        ObservableList<String> cardList = FXCollections.observableArrayList();

        if(card.size() == 0 )
        {
            cardIDLabel.setText("Card ID:");
            pinLabel.setText("PIN:");
            activeLabel.setText("Active:");
            expireLabel.setText("Expiration date:");
        }
        else
            {
                for(int i=0; i<card.size(); i++)
                {
                    cardList.add(Integer.toString(card.get(i).getId()));
                }
                cardId.setItems(cardList);
                cardIDLabel.setText("Card ID:   " + card.get(0).getId());
                pinLabel.setText("PIN:   " + card.get(0).getPIN());
                activeLabel.setText("Active:   " + card.get(0).isActive());
                expireLabel.setText("Expiration date:   " + card.get(0).getExpireM() + "/" + card.get(0).getExpireY());
            }
    }

    public void updateAccountInfo(ActionEvent actionEvent)
    {
        if(!accountNum.getSelectionModel().isEmpty())
        {
            String selectedAccount = accountNum.getSelectionModel().getSelectedItem().toString();

            ArrayList <Account> account = dbase.getAccountInfo(getCurrentClientId());

            for(int i=0; i<account.size(); i++)
            {
                if(account.get(i).getAccNum().equals(selectedAccount))
                {
                    accID.setText("Account ID:   " + account.get(i).getId());
                    currentBalance.setText("Account balance:   " + account.get(i).getAmount() + "  Eur");
                    break;
                }
            }
        }
    }

    public void createNewAccount(ActionEvent actionEvent)
    {
        String accNumber = "";
        Random r = new Random();
        for (int i = 0; i < 10; i++)
        {
            accNumber += Integer.toString(r.nextInt(10));
        }

        if(dbase.insertAccount(getCurrentClientId(), accNumber))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account creation");
            alert.setHeaderText("Account created successfully");
            alert.setContentText("Account number:  " + accNumber);
            alert.showAndWait();

            fillUpAccountDropdown(accountNum);
        }
        else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account creation");
                alert.setHeaderText("Failed to create account");
                alert.showAndWait();
            }
    }
}