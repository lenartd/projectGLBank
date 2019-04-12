package sk.itsovy;

public class Client {
    private String firstName;
    private String lastName;
    private String email;
    private int id;

    public Client(String firstName, String lastName, String email, int id)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = id;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getId() { return id; }

}
