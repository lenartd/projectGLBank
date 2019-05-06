package sk.itsovy.UserInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sk.itsovy.*;
import sk.itsovy.Database.Database;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public ComboBox accountNum3;
    public ComboBox cardId;
    public Label cardIDLabel;
    public Label pinLabel;
    public Label activeLabel;
    public Label expireLabel;
    public Label cardNumberLabel;
    public CheckBox blockCardBox;
    public TextField amountW;
    public TextField amountD;
    public Label ibUname;
    public CheckBox blockCheck;


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
        accountNum3.getItems().clear();
        fillUpAccountDropdown(accountNum);
        fillUpAccountDropdown(accountNum2);
        fillUpAccountDropdown(accountNum3);

        String uname = dbase.getLoginClientName(getCurrentClientId());
        ibUname.setText("Username:   " + uname);

        setIBBlock();
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

    public void updateAccountInfo(ActionEvent actionEvent)
    {
        updateAccInfo();
    }

    public void updateAccInfo()
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

    public void updateAccCardInfo(ActionEvent actionEvent)
    {
        updateAccCardInfo();
    }

    public void updateAccCardInfo()
    {
        cardId.getItems().clear();
        if(!accountNum2.getSelectionModel().isEmpty())
        {
            String selectedAccount = accountNum2.getSelectionModel().getSelectedItem().toString();

            ArrayList <Card> card = dbase.getCardInfo(getCurrentClientId());

            ArrayList <Account> account = dbase.getAccountInfo(getCurrentClientId());

            ObservableList <String> cardList = FXCollections.observableArrayList();

            for(int i=0; i<account.size(); i++)
            {
                for(int j=0; j<card.size(); j++)
                {
                    if(card.get(j).getIda() == account.get(i).getId() && account.get(i).getAccNum().equals(selectedAccount))
                    {
                        cardList.add(Integer.toString(card.get(j).getId()));
                    }
                }
            }

            cardId.setItems(cardList);
            if(cardList.size() == 0)
            {
                cardIDLabel.setText("Card ID:");
                cardNumberLabel.setText("Card number:");
                pinLabel.setText("PIN:");
                activeLabel.setText("Active:");
                expireLabel.setText("Expiration date:");
            }
            else{cardId.getSelectionModel().selectFirst();}
        }
        else
        {
            cardIDLabel.setText("Card ID:");
            cardNumberLabel.setText("Card number:");
            pinLabel.setText("PIN:");
            activeLabel.setText("Active:");
            expireLabel.setText("Expiration date:");
        }
    }

    public void updateCardValues(ActionEvent actionEvent)
    {
        blockCardBox.setSelected(false);
        if(!cardId.getSelectionModel().isEmpty())
        {
            ArrayList <Card> card = dbase.getCardInfo(getCurrentClientId());
            int selectedCard = Integer.parseInt(cardId.getSelectionModel().getSelectedItem().toString());
            for(int i =0 ; i<card.size(); i++)
            {
                if(card.get(i).getId() == selectedCard)
                {
                    cardIDLabel.setText("Card ID:   " + card.get(i).getId());
                    cardNumberLabel.setText("Card number:   " + card.get(i).getCardNumber());
                    pinLabel.setText("PIN:   " + card.get(i).getPIN());
                    activeLabel.setText("Active:   " + card.get(i).isActive());
                    expireLabel.setText("Expiration date:   " + card.get(i).getExpireM() + "/" + card.get(i).getExpireY());

                    if(!card.get(i).isActive())
                    {
                        blockCardBox.setSelected(true);
                    }
                    break;
                }
            }
        }
    }

    public void createNewCard(ActionEvent actionEvent)
    {
        int ida = 0;
        String cardNumber = "";
        String PIN = "";
        int isActive = 1;
        int expireM;
        int expireY;

        Random r = new Random();
        int counter = 0;
        for (int i = 0; i < 16; i++)
        {
            if(counter == 4)
            {
                cardNumber += " ";
                counter = 0;
            }
            cardNumber += Integer.toString(r.nextInt(10));
            counter++;
        }

        for (int i = 0; i < 4; i++)
        {
            PIN += Integer.toString(r.nextInt(10));
        }

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        expireM = localDate.getMonthValue();
        expireY = localDate.getYear()+3;

        String selectedAccount = accountNum2.getSelectionModel().getSelectedItem().toString();

        ArrayList <Account> account = dbase.getAccountInfo(getCurrentClientId());

        for(int i=0; i<account.size(); i++)
        {
            if(account.get(0).getAccNum().equals(selectedAccount))
            {
                ida = account.get(0).getId();
                break;
            }
        }

        if(dbase.insertNewCard(ida, PIN, expireM, expireY, isActive, cardNumber))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Card creation");
            alert.setHeaderText("Card created successfully");
            //alert.setContentText("Account number:  " + accNumber);
            alert.showAndWait();
        }
        else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Card creation");
                alert.setHeaderText("Failed to create card");
                alert.showAndWait();
            }
        updateAccCardInfo();
    }

    public void blockUnblockCard(ActionEvent actionEvent)
    {
        if(!cardId.getSelectionModel().isEmpty())
        {
            ArrayList <Card> card = dbase.getCardInfo(getCurrentClientId());
            int selectedCard = Integer.parseInt(cardId.getSelectionModel().getSelectedItem().toString());
            for(int i =0 ; i<card.size(); i++)
            {
                if(card.get(i).getId() == selectedCard)
                {
                    if(card.get(i).isActive())
                    {
                        dbase.setCardBlock(card.get(i).getId(), 0);
                        activeLabel.setText("Active:   false");
                    }
                    else
                        {
                            dbase.setCardBlock(card.get(i).getId(), 1);
                            activeLabel.setText("Active:   true");
                        }
                    break;
                }
            }
        }
    }

    public void changeCardPin(ActionEvent actionEvent)
    {
        if(!cardId.getSelectionModel().isEmpty())
        {
            ArrayList <Card> card = dbase.getCardInfo(getCurrentClientId());
            int selectedCard = Integer.parseInt(cardId.getSelectionModel().getSelectedItem().toString());
            for(int i =0 ; i<card.size(); i++)
            {
                if(card.get(i).getId() == selectedCard)
                {
                    String newPIN = "";

                    Random r = new Random();
                    for (int j = 0; j < 4; j++)
                    {
                        newPIN += Integer.toString(r.nextInt(10));
                    }

                    dbase.changePIN(newPIN, card.get(0).getId());
                    pinLabel.setText("PIN:   " + newPIN);
                    break;
                }
            }
        }
    }

    public void withdrawMoney(ActionEvent actionEvent)
    {
        double numberW = Double.parseDouble(amountW.getText());
        String selectedAcc = accountNum3.getSelectionModel().getSelectedItem().toString();

        ArrayList <Account> account = dbase.getAccountInfo(getCurrentClientId());

        for(int i = 0; i<account.size(); i++)
        {
            if(account.get(i).getAccNum().equals(selectedAcc))
            {
                if((account.get(i).getAmount() - numberW) < 0)
                {
                    System.out.println("You don't have enough money.");
                }
                else
                {
                    System.out.println("There is enough money");
                    if(dbase.getMoney(selectedAcc, numberW) && dbase.transactionLog(account.get(i).getId(), dbase.getEmployee().getId(), selectedAcc, numberW))
                    {
                        System.out.println("success");
                        updateAccInfo();
                    }
                    else
                        {
                            System.out.println("somthing went wrong");
                        }
                }
                break;
            }
        }
    }

    public void depositMoney(ActionEvent actionEvent)
    {
        double numberD = Double.parseDouble(amountD.getText());
        String selectedAcc = accountNum3.getSelectionModel().getSelectedItem().toString();

        ArrayList <Account> account = dbase.getAccountInfo(getCurrentClientId());

        for(int i = 0; i<account.size(); i++)
        {
            if(account.get(i).getAccNum().equals(selectedAcc))
            {
                if(dbase.addMoney(selectedAcc, numberD) && dbase.transactionLog(account.get(i).getId(), dbase.getEmployee().getId(), selectedAcc, numberD))
                {
                    System.out.println("success");
                    updateAccInfo();
                }
                else
                {
                    System.out.println("somthing went wrong");
                }
                break;
            }
        }
    }

    public String generatePass()
    {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String pass = "";
        Random random = new Random();
        for(int i = 0; i<8; i++)
        {
            pass += alphabet.charAt(random.nextInt(62));
        }
        return pass;
    }

    public void resetIBPass(ActionEvent actionEvent)
    {
        if(dbase.resetIBPass(generatePass(), getCurrentClientId()))
        {
            System.out.println("Password reseted successfully");
        }
        else
            {
                System.out.println("Something went wrong");
            }
    }

    public void blockClientIB(ActionEvent actionEvent)
    {
        if(!blockCheck.isSelected())
        {
            dbase.unblockClient(getCurrentClientId());
            System.out.println("client unblocked");
        }
        else
            {
                dbase.blockClient(getCurrentClientId());
                System.out.println("client blocked");
            }
    }

    public void setIBBlock()
    {
        List <LoginHistory> records = dbase.getThreeLoginRecords(getCurrentClientId());
        int blocks = 0;
        for(int i =0; i<records.size(); i++)
        {
            if ( i == 0 && records.get(i).isSuccess() == null)
            {
                blockCheck.setSelected(true);
            }
            else if ( i == 0 && records.get(i).isSuccess().equals("1"))
            {
                blockCheck.setSelected(false);
            }
            else if (records.get(i).isSuccess() == null) {}
            else if (records.get(i).isSuccess().equals("1")) {}
            else {
                    blocks++;
                 }
        }

        if (blocks == 3)
        {
            blockCheck.setSelected(true);
        }
        else
            {
                blockCheck.setSelected(false);
            }

        if (dbase.isIBBlocked(getCurrentClientId()))
        {
            blockCheck.setSelected(false);
        }
        else
            {
                blockCheck.setSelected(true);
            }
    }
}