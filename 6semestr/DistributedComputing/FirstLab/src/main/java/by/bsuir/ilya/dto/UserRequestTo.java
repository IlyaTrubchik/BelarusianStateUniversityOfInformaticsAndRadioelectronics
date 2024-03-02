package by.bsuir.ilya.dto;

public class UserRequestTo {
    //private long id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
    public UserRequestTo(){

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

  //  public void setId(long id) {
  //      this.id = id;
  //  }

   // public long getId() {
   //    return id;
   // }



    @Override
    public String toString() {
        return "UserRequestTo{" +
            //    "id=" + id +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                '}';
    }
}
