package aidooo.spydo.com.project1.Database;

public class userDatabaseHelperClassForAgency {

    String fullname,username,email,pass,comPass,phoneNum,currentUserID;

    public userDatabaseHelperClassForAgency(){

    }

    public userDatabaseHelperClassForAgency(String currentUserID, String fullname, String username, String email, String pass, String comPass, String phoneNum) {
        this.currentUserID = currentUserID;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.comPass = comPass;
        this.phoneNum = phoneNum;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getComPass() {
        return comPass;
    }

    public void setComPass(String comPass) {
        this.comPass = comPass;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
