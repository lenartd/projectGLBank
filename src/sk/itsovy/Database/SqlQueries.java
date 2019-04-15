package sk.itsovy.Database;

public class SqlQueries
{

    private String employeeInfo = "select employee.id, employee.fname, employee.lname, positions.name as position from employee inner join positions on employee.position = positions.id where employee.id = ?;";

    private String loginInfo = "select * from loginemp";

    private String clients = "select * from client";

    private String newClient = "insert into client(fname, lname, email) values(?, ?, ?);";

    private String exactClient = "select * from client where id = ?";

    public String getEmployeeInfo() { return employeeInfo; }

    public String getLoginInfo() { return loginInfo; }

    public String getClients() { return clients; }

    public String getNewClient() { return newClient; }

    public String getExactClient() { return exactClient; }
}
