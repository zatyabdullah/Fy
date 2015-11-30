package com.fly.firefly.api.obj;

/*
 * Created by ImalPasha on 11/6/2015.
 */

 /* Response From API */

public class LoginReceive {

    private final LoginReceive userObj;
    private String status;
    private String message;
    private user_info user_info;

    public LoginReceive(LoginReceive param_userObj) {
        this.userObj = param_userObj;
    }

    public LoginReceive getUserObj() {
        return userObj;
    }

   public class user_info{

        private String username;
        private String password;
       private String first_name;

       public String getLast_name() {
           return last_name;
       }

       public void setLast_name(String last_name) {
           this.last_name = last_name;
       }

       private String last_name;

       public String getFirst_name() {
           return first_name;
       }

       public void setFirst_name(String first_name) {
           this.first_name = first_name;
       }



       public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

    }

    public user_info getUser_info() {
        return user_info;
    }

    public void setUser_info(user_info user_info) {
        this.user_info = user_info;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
