package com.uow.snazzikiel.prepareo;

/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 ***********************************************/

/**
    Class:   enrolmentData
    ---------------------------------------
    ***** NOT USED *****
 *
    Used to store thed ata of subjects enrolled in
*/
public class enrolmentData {

    private String bachelor;
    private String major;


    public enrolmentData(String bachelor, String major){
        this.bachelor = bachelor;
        this.major = major;
    }

    public String getBachelor(){ return bachelor; }
    public String getMajor(){ return major; }

    public void setBachelor(String bachelor) { this.bachelor = bachelor; }
    public void setMajor(String major) { this.major = major; }

}




