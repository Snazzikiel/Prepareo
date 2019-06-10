package com.uow.snazzikiel.prepareo;

/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		Adam
 * Assisted:		Connor
 ***********************************************/

/**
    Class:   notificationData
    ---------------------------------------
    Used to store notification objects
*/
class notificationData {
    private String name;
    private String time;
    private String startDate;
    private String endDate;
    private String personalMsg;

    public notificationData(String Name, String time, String StartDate,
                            String EndDate, String PersonalMsg){
        this.name = Name;
        this.time = time;
        this.startDate = StartDate;
        this.endDate = EndDate;
        this.personalMsg = PersonalMsg;
    }

    public String getName(){ return name; }
    public String getTime(){ return time; }
    public String getStartDate(){ return startDate; }
    public String getEndDate() { return endDate; }
    public String getPersonalMsg(){ return personalMsg; }

    public void setName(String name) { this.name = name; }
    public void setTime(String time) { this.time = time; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setPersonalMsg(String personalMsg) { this.personalMsg = personalMsg; }


}


