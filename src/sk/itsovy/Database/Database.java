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

    private int empoyeeId;
    public int getEmpoyeeId() { return empoyeeId; }

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
                    empoyeeId = rs.getInt("ide");
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
            PreparedStatement st = conn.prepareStatement(new SqlQueries().getGetClients());
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
}
