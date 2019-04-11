package sk.itsovy.Database;

import sk.itsovy.Employee;
import sk.itsovy.Globals;

import java.sql.*;

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
            //Connection conn = getConnection();

            PreparedStatement st = conn.prepareStatement(SqlQueries.getLoginInfo);
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
            //Connection conn = getConnection();

            PreparedStatement st = conn.prepareStatement(SqlQueries.getEmployeeInfo);
            ResultSet rs = st.executeQuery();
            rs.next();

            String fname = rs.getString("fname");
            String lanme = rs.getString("lname");
            String position = rs.getString("position");

            Employee emp = new Employee(fname, lanme, position);
            return emp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
