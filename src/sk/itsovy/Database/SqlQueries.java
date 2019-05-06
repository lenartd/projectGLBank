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

    private String cardBlock = "update card set Active = ? where id = ?;";

    private String changePin = "update card set pin = ? where id = ?;";

    private String newTransaction = "insert into transaction(idAcc, RecAccount, idEmployee, TransAmount) values(?,?,?,?);";

    private String sendMoney = "update account set amount = amount - ? where accnum = ?;";

    private String addMoney = "update account set amount = amount + ? where accnum = ?;";

    private String resetLoginPassword = "update loginclient set password = ? where idc = ?;";

    private String clientLoginInfo = "select * from loginclient where idc = ?;";

    private String lastLoginRecord = "select * from loginhistory where idl = (select id from loginclient where idc = ?)order by UNIX_TIMESTAMP(logDate) desc limit 1;";

    private String lastThreeLoginRecord = "select * from loginhistory where idl = (select id from loginclient where idc = ?)order by UNIX_TIMESTAMP(logDate) desc limit 3;";

    private String blockClient = "insert into loginhistory(idl) select id from loginclient where idc = ?;";

    private String unblockClient = "insert into loginhistory(idl,success) values((select id from loginclient where idc = ?),1);";

    private String insertClientLogin = "insert into loginclient(idc, login, password) values(?,?,?);";

    public String getEmployeeInfo() { return employeeInfo; }

    public String getLoginInfo() { return loginInfo; }

    public String getClients() { return clients; }

    public String getNewClient() { return newClient; }

    public String getExactClient() { return exactClient; }

    public String getAccountInfo() { return accountInfo; }

    public String getInsertAccount() { return insertAccount; }

    public String getCardInfo() { return cardInfo; }

    public String getInsertCard() { return insertCard; }

    public String getCardBlock() { return cardBlock; }

    public String getChangePin() { return changePin; }

    public String getNewTransaction() { return newTransaction; }

    public String getSendMoney() { return sendMoney; }

    public String getAddMoney() { return addMoney; }

    public String getResetLoginPassword() { return resetLoginPassword; }

    public String getClientLoginInfo() { return clientLoginInfo; }

    public String getLastLoginRecord() { return lastLoginRecord; }

    public String getLastThreeLoginRecord() { return lastThreeLoginRecord; }

    public String getBlockClient() { return blockClient; }

    public String getUnblockClient() { return unblockClient; }

    public String getInsertClientLogin() { return insertClientLogin; }
}
