package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		Connor
 ***********************************************/
/**
    Class:   subjectsData
    ---------------------------------------
    Used to store subject objects
*/
class subjectsData {

    String courseName;
    String courseCode;


    public subjectsData(String courseName, String courseCode ){
        this.courseName = courseName;
        this.courseCode = courseCode;

    }

    public String getCourseName(){ return courseName; }
    public String getCourseCode(){ return courseCode; }

    public void setCourseName(String courseName) { this.courseName = courseName; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }


}

