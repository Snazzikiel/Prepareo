package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Alec
 ***********************************************/

import java.util.ArrayList;

/**
    Class:   calendarData
    ---------------------------------------
    Class to load Calendar Objects for calendar Data

*/
public class calendarData {
    public String date="";
    public String name="";
    public String subject="";
    public String description="";

    public static ArrayList<calendarData> calData;

    public calendarData(String date, String name, String subject, String description){
        this.date=date;
        this.name=name;
        this.subject=subject;
        this.description= description;
    }

    public String getDate(){
        return date;
    }

    public String getName(){
        return name;
    }

    public String getSubject(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public void setDescription(String description){
        this.description = description;
    }




}
