package com.uow.snazzikiel.prepareo;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

class notificationData {
    private String name;
    private String frequency;
    private String startDate;
    private String endDate;
    private String personalMsg;

    public notificationData(String Name, String Frequency, String StartDate,
                            String EndDate, String PersonalMsg){
        this.name = Name;
        this.frequency = Frequency;
        this.startDate = StartDate;
        this.endDate = EndDate;
        this.personalMsg = PersonalMsg;
    }

    public String getName(){ return name; }
    public String getFrequency(){ return frequency; }
    public String getStartDate(){ return startDate; }
    public String getEndDate() { return endDate; }
    public String getPersonalMsg(){ return personalMsg; }

    public void setName(String name) { this.name = name; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setPersonalMsg(String personalMsg) { this.personalMsg = personalMsg; }


}


