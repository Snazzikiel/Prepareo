package com.uow.snazzikiel.prepareo;

class accountData {
    private String fullName;
    private String email;
    private String address;
    private String birthday;
    private String password;

    public accountData(String fullName, String email, String address,
                            String birthday, String password){
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.birthday = birthday;
        this.password = password;
    }

    public String getName(){ return fullName; }
    public String getEmail(){ return email; }
    public String getAddress(){ return address; }
    public String getBirthday() { return birthday; }
    public String getPassword(){ return password; }

    public void setName(String fullName) { this.fullName = fullName; }
    public void setFrequency(String email) { this.email = email; }
    public void setStartDate(String address) { this.address = address; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setPassword(String password) { this.password = password; }


};


