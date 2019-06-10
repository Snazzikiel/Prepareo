package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 ***********************************************/

/**
    Class:   accountData
    ---------------------------------------
    Used to store the data of the current account in to local data
*/
class accountData {
    private String firstName;
    private String userName;
    private String email;
    private String lastName;
    private String birthday;
    private String password;

    public accountData(String firstName, String lName, String userName, String email,
                            String birthday, String password){
        this.firstName = firstName;
        this.lastName = lName;
        this.userName = userName;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
    }

    public String getName(){ return firstName; }
    public String getEmail(){ return email; }
    public String getAddress(){ return lastName; }
    public String getBirthday() { return birthday; }
    public String getPassword(){ return password; }
    public String getUserName() { return userName; }

    public void setName(String firstName) { this.firstName = firstName; }
    public void setFrequency(String email) { this.email = email; }
    public void setStartDate(String address) { this.lastName = address; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setPassword(String password) { this.password = password; }
    public void setUserName(String userName) { this.userName = userName; }


};


