package sk.itsovy;

public class Account {
    private int idc;
    private String accNum;
    private double amount;
    private int id;

    public Account(int idc, String accNum, double amount, int id)
    {
        this.idc = idc;
        this.accNum = accNum;
        this.amount = amount;
        this.id = id;
    }

    public int getIdc() { return idc; }

    public String getAccNum() { return accNum; }

    public double getAmount() { return amount; }

    public int getId() { return id; }
}
