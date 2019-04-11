package sk.itsovy.Database;

import sk.itsovy.Employee;

public class SqlQueries {

    public static final String getEmployeeInfo = "select employee.fname, employee.lname, positions.name as position from employee inner join positions on employee.position = positions.id where employee.id = \""+Database.getInstanceDB().getEmpoyeeId()+"\";";
    public static final String getLoginInfo = "select * from loginEmp";

}
