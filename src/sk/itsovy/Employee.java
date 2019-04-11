package sk.itsovy;

public class Employee {

    private String firstName;
    private String lastName;
    private String position;
    //private int id;

    public Employee(String firstName, String lastName, String position)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        //this.id = id;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    /*
    public int getId() {
        return id;
    }
    */
}
