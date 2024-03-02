package by.bsuir.ilya.dto;

public class UserResponseTo {
    private long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
    public UserResponseTo(){

    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    @Override
    public String toString() {
        return "UserResponseTo{" +
                 "id=" + id +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                '}';
    }
}
