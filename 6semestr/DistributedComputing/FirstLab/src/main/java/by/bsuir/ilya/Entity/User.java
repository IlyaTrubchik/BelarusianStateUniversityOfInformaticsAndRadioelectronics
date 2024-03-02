package by.bsuir.ilya.Entity;

public class User {
    private long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;

    public User()
    {

    }

    public String getLastname() {
        return lastname;
    }
    public String getLogin()
    {
        return  login;
    }
    public String getPassword()
    {
        return password;
    }
    public String getFirstname()
    {
        return firstname;
    }

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                '}';
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
