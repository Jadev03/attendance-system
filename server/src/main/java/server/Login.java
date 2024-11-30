package server;

public class Login {
    String username="user";
    String password="Password&123";
    public   boolean validate(String username,String password){
        return username.equals(this.username) && password.equals(this.password);

    }
}
