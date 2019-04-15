package sk.itsovy.Database;

import sk.itsovy.Client;
import sk.itsovy.Employee;
import sk.itsovy.Globals;

import java.sql.*;
import java.util.ArrayList;

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

    public boolean addClient(String fname, String lname, String email)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getNewClient());
            st.setString(1, fname);
            st.setString(2, lname);
            st.setString(3, email);
            st.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
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
}
