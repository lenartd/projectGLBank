package sk.itsovy.Database;

import sk.itsovy.Globals;

import java.sql.*;

public class Database {
    private static Database db = new Database();

    private Database() {}

    public static Database getInstanceDB(){
        return db;
    }

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
            Connection conn = getConnection();

            PreparedStatement st = conn.prepareStatement("select * from loginEmp");
            ResultSet rs = st.executeQuery();

            while(rs.next())
            {
                String uname = rs.getString("login");
                String pass = rs.getString("password");
                if(uname.equals(username) && pass.equals(password))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
