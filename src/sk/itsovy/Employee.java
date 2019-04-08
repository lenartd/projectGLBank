package sk.itsovy;

public class Employee {

    private String firstName;
    private String lastName;
    private int position;
    private int id;

    public Employee(String firstName, String lastName, int position, int id)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.id = id;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public int getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }
}
