package sk.itsovy;

public class Card {

    private int id;
    private int ida;
    private String PIN;
    private String expireM;
    private String expireY;
    private boolean active;

    public Card(int id, int ida, String PIN, String expireM, String expireY,  boolean active)
    {
        this.id = id;
        this.ida = ida;
        this.PIN = PIN;
        this.expireM = expireM;
        this.expireY = expireY;
        this.active = active;
    }

    public int getId() { return id; }

    public int getIda() { return ida; }

    public String getPIN() { return PIN; }

    public boolean isActive() { return active; }

    public String getExpireM() { return expireM; }

    public String getExpireY() { return expireY; }
}
