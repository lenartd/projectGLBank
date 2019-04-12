package sk.itsovy.Database;

import sk.itsovy.Employee;

public class SqlQueries
{

    private String employeeInfo = "select employee.id, employee.fname, employee.lname, positions.name as position from employee inner join positions on employee.position = positions.id where employee.id = \""+Database.getInstanceDB().getEmpoyeeId()+"\";";

    private String loginInfo = "select * from loginemp";

    private String getClients = "select * from client";

    public String getEmployeeInfo() { return employeeInfo; }

    public String getLoginInfo() { return loginInfo; }

    public String getGetClients() { return getClients; }
}
