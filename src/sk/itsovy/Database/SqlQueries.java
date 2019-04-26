package sk.itsovy.Database;

public class SqlQueries
{

    private String employeeInfo = "select employee.id, employee.fname, employee.lname, positions.name as position from employee inner join positions on employee.position = positions.id where employee.id = ?;";

    private String loginInfo = "select * from loginemp";

    private String clients = "select * from client";

    private String newClient = "insert into client(fname, lname, email) values(?, ?, ?);";

    private String exactClient = "select * from client where id = ?";

    private String accountInfo = "select * from account where idc = ?;";

    private String insertAccount = "insert into account(idc, AccNum, amount) values(?, ?, ?);";

    private String cardInfo = "select * from card inner join account on card.ida = account.id where account.idc like ?;";

    private String insertCard = "insert into card(ida, PIN, ExpireM, ExpireY, Active, CardNumber) values(?, ?, ?, ?, ?, ?);";

    public String getEmployeeInfo() { return employeeInfo; }

    public String getLoginInfo() { return loginInfo; }

    public String getClients() { return clients; }

    public String getNewClient() { return newClient; }

    public String getExactClient() { return exactClient; }

    public String getAccountInfo() { return accountInfo; }

    public String getInsertAccount() { return insertAccount; }

    public String getCardInfo() { return cardInfo; }

    public String getInsertCard() { return insertCard; }
}
