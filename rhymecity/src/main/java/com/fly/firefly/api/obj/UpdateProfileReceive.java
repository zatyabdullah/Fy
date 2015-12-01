package com.fly.firefly.api.obj;

/*
 * Created by Metech USer
 */

 /* Response From API */

public class UpdateProfileReceive {

    private final UpdateProfileReceive userObj;
    private String status;
    private String message;
    private UserInfo userInfo;

    public UpdateProfileReceive(UpdateProfileReceive param_userObj) {
        this.userObj = param_userObj;
    }

    public UpdateProfileReceive getUserObj() {
        return userObj;
    }

    public class UserInfo{

        private String username;
        private String password;
        private String signature;
        private String new_password;
        private String title;
        private String first_name;
        private String last_name;
        private String dob;
        private String address_1;
        private String address_2;
        private String address_3;
        private String country;
        private String city;
        private String state;
        private String postcode;
        private String mobile_phone;
        private String  alternate_phone;
        private String fax;

        public String getNew_password() {
            return new_password;
        }

        public void setNew_password(String new_password) {
            this.new_password = new_password;
        }



        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getAddress_1() {
            return address_1;
        }

        public void setAddress_1(String address_1) {
            this.address_1 = address_1;
        }

        public String getAddress_2() {
            return address_2;
        }

        public void setAddress_2(String address_2) {
            this.address_2 = address_2;
        }

        public String getAddress_3() {
            return address_3;
        }

        public void setAddress_3(String address_3) {
            this.address_3 = address_3;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public String getAlternate_phone() {
            return alternate_phone;
        }

        public void setAlternate_phone(String alternate_phone) {
            this.alternate_phone = alternate_phone;
        }

        public String getFax() {
            return fax;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }



        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
