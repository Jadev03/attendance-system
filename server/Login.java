package server;

public class Login {
    String Username= "user";
    String Password="Password@7";

    public boolean loginValidate(String Username,String Password){

        if(Username==this.Username && Password==this.Password){
            return true;
        }
        return false;

    }


    
}
