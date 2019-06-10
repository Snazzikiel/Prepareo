package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 * Assisted:		Adam
 ***********************************************/
/**
    Class:   assignmentsData
    ---------------------------------------
    Data stored locally of all assignments
*/
class assignmentsData {

    String assignmentName;
    String assignmentWeight;
    String assignmentMark;
    String assignmentDueDate;


    public assignmentsData(String assignmentName, String assignmentWeight, String assignmentMark, String assignmentDueDate ){
        this.assignmentName = assignmentName;
        this.assignmentWeight = assignmentWeight;
        this.assignmentMark = assignmentMark;
        this.assignmentDueDate = assignmentDueDate;
    }

    public String getAssignmentName(){ return assignmentName; }
    public String getAssignmentWeight(){ return assignmentWeight; }
    public String getAssignmentMark(){ return assignmentMark; }

    public String getAssignmentDueDate() {
        return assignmentDueDate;
    }

    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }
    public void setAssignmentWeight(String assignmentWeight) { this.assignmentWeight = assignmentWeight; }
    public void setAssignmentMark(String assignmentMark) { this.assignmentMark = assignmentMark;  }

    public void setAssignmentDueDate(String assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }
}

