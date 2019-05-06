package sk.itsovy.Database;

import sk.itsovy.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database db = new Database();
    private Database() {}
    public static Database getInstanceDB(){
        return db;
    }

    private int employeeId;

    Connection conn = getConnection();

    private Connection getConnection()
    {
        Connection connection;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(Globals.host, Globals.userName, Globals.password);
            System.out.println("Connecting");
            return connection;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }

    public void closeConnection()
    {
        if(conn!=null)
        {
            try
            {
                conn.close();
                System.out.println("Disconnected");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean matchUser(String username, String password)
    {
        try
        {
            if (conn.isClosed())
            {
                conn = getConnection();
            }
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getLoginInfo());
            ResultSet rs = st.executeQuery();

            while(rs.next())
            {
                String uname = rs.getString("login");
                String pass = rs.getString("password");
                if(uname.equals(username) && pass.equals(password))
                {
                    employeeId = rs.getInt("ide");
                    return true;
                }
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public Employee getEmployee()
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getEmployeeInfo());
            st.setInt(1, employeeId);
            ResultSet rs = st.executeQuery();
            rs.next();

            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            String position = rs.getString("position");
            int id = rs.getInt("id");

            Employee emp = new Employee(fname, lname, position, id);

            return emp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Client> getClients()
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getClients());
            ResultSet rs = st.executeQuery();

            ArrayList <Client> clientList = new ArrayList <>();
            Client client;

            while(rs.next())
            {
                client = new Client(rs.getString("fname"), rs.getString("lname"), rs.getString("email"), rs.getInt("id"));
                clientList.add(client);
            }
            return clientList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public int addClient(String fname, String lname, String email)
    {
        int index = -1;
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getNewClient(), Statement.RETURN_GENERATED_KEYS);
            st.setString(1, fname);
            st.setString(2, lname);
            st.setString(3, email);
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.next())
            {
                index = rs.getInt(1);
                return index;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return index;
    }

    public Client getExactClient(int id)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getExactClient());
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            rs.next();

            String fname = rs.getString("fname");
            String lname = rs.getString("lname");
            String email = rs.getString("email");

            Client client = new Client(fname, lname, email, id);

            return client;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList <Account> getAccountInfo(int id)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getAccountInfo());
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            ArrayList <Account> accountList = new ArrayList <>();
            Account account;

            while(rs.next())
            {
                account = new Account(rs.getInt("idc"), rs.getString("accNum"), rs.getDouble("amount"), rs.getInt("id"));
                accountList.add(account);
            }
            return accountList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertAccount(int idc, String accnum)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getInsertAccount());
            st.setString(1, Integer.toString(idc));
            st.setString(2, accnum);
            st.setString(3, "0");
            st.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList <Card> getCardInfo(int id)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getCardInfo());
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            ArrayList <Card> cardList = new ArrayList <>();
            Card card;

            while(rs.next())
            {
                int idd = rs.getInt("id");
                int ida = rs.getInt("ida");
                String PIN = rs.getString("PIN");
                int expireM = rs.getInt("expireM");
                int expireY = rs.getInt("expireY");
                boolean active =  rs.getBoolean("Active");
                String cardNum = rs.getString("CardNumber");
                card = new Card(idd, ida, PIN, Integer.toString(expireM), Integer.toString(expireY), active, cardNum);
                cardList.add(card);
            }
            return cardList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertNewCard(int ida, String PIN, int ExpireM, int ExpireY, int isActive, String cardNumber)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getInsertCard());
            st.setInt(1, ida);
            st.setString(2, PIN);
            st.setInt(3, ExpireM);
            st.setInt(4, ExpireY);
            st.setInt(5, isActive);
            st.setString(6, cardNumber);
            st.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void setCardBlock(int id, int isblocked)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getCardBlock());
            statement.setInt(1, isblocked);
            statement.setInt(2, id);
            statement.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changePIN(String pin, int id)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getChangePin());
            statement.setString(1, pin);
            statement.setInt(2, id);
            statement.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean transactionLog(int ida, int ide, String recAcc, double amount)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getNewTransaction());
            statement.setInt(1, ida);
            statement.setString(2, recAcc);
            statement.setInt(3, ide);
            statement.setDouble(4, amount);
            statement.execute();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getMoney(String accNum, double amount)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getSendMoney());
            statement.setDouble(1, amount);
            statement.setString(2, accNum);
            statement.execute();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addMoney(String accNum, double amount)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getAddMoney());
            statement.setDouble(1, amount);
            statement.setString(2, accNum);
            statement.execute();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetIBPass(String password, int idc)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getResetLoginPassword());
            statement.setString(1, password);
            statement.setInt(2, idc);
            statement.executeUpdate();
            return true;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public String getLoginClientName(int idc)
    {
        String pw = "";
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getClientLoginInfo());
            statement.setInt(1, idc);
            ResultSet rs = statement.executeQuery();

            while(rs.next())
            {
                pw = rs.getString("login");
            }
            return pw;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pw;
    }

    public List <LoginHistory> getThreeLoginRecords(int idc)
    {
        List <LoginHistory> loginHistory = new ArrayList<>();
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getLastThreeLoginRecord());
            statement.setInt(1,idc);
            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("id");
                int idl = rs.getInt("idl");
                String success = rs.getString("success");
                Date dateLog = rs.getDate("LogDate");
                LoginHistory record = new LoginHistory(id, idl,success,dateLog);

                loginHistory.add(record);
            }
            return loginHistory;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return loginHistory;
    }

    public boolean isIBBlocked(int idc)
    {
        String success;
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getLastLoginRecord());
            statement.setInt(1, idc);
            ResultSet rs = statement.executeQuery();
            while(rs.next())
            {
                success = rs.getString("success");
                if (success == null)
                {
                    return false;
                }
                else
                    {
                        return true;
                    }
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public void blockClient(int idc)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getBlockClient());
            statement.setInt(1, idc);
            statement.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void unblockClient(int idc)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getUnblockClient());
            statement.setInt(1, idc);
            statement.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void newClientLogin(int idc, String uname, String password)
    {
        try
        {
            PreparedStatement statement = conn.prepareStatement(new SqlQueries().getInsertClientLogin());
            statement.setInt(1, idc);
            statement.setString(2, uname);
            statement.setString(3, password);
            statement.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
