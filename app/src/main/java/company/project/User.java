package company.project;

public class User {

    public String role;
    public String hubicacion;
    public String userName;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String role, String hubicacion, String userName) {
        this.role = role;
        this.hubicacion = hubicacion;
        this.userName = userName;
    }

}