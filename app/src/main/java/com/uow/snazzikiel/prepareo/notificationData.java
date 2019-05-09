package com.uow.snazzikiel.prepareo;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

class notificationData {
    String name;
    String intervals;
    String frequency;
    String startDate;
    String endDate;
    String subject;
    String personalMsg;

    public notificationData(String Name, String Intervals, String Frequency, String StartDate,
                            String EndDate, String Subject, String PersonalMsg){
        this.name = Name;
        this.intervals = Intervals;
        this.frequency = Frequency;
        this.startDate = StartDate;
        this.endDate = EndDate;
        this.subject = Subject;
        this.personalMsg = PersonalMsg;
    }

    public String getName(){ return name; }
    public String getIntervals(){ return intervals; }
    public String getFrequency(){ return frequency; }
    public String getStartDate(){ return startDate; }
    public String getEndDate() { return endDate; }
    public String getSubject(){ return subject; }
    public String getPersonalMsg(){ return personalMsg; }

    public void setName(String name) { this.name = name; }
    public void setIntervals(String intervals) { this.intervals = intervals; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setPersonalMsg(String personalMsg) { this.personalMsg = personalMsg; }


};


