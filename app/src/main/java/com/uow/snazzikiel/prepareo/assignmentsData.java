package com.uow.snazzikiel.prepareo;

class assignmentsData {

    String assignmentName;
    String assignmentWeight;
    String assignmentTotalMark;
    String assignmentDueDate;


    public assignmentsData(String assignmentName, String assignmentWeight, String assignmentTotalMark, String assignmentDueDate){
        this.assignmentName = assignmentName;
        this.assignmentWeight = assignmentWeight;
        this.assignmentTotalMark = assignmentTotalMark;
        this.assignmentDueDate = assignmentDueDate;

    }

    public String getAssignmentName(){ return assignmentName; }
    public String getAssignmentWeight(){ return assignmentWeight; }
    public String getAssignmentTotalMark(){ return assignmentTotalMark; }
    public String getAssignmentDueDate(){ return assignmentDueDate; }

    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }
    public void setAssignmentWeight(String assignmentWeight) { this.assignmentWeight = assignmentWeight; }
    public void setAssignmentTotalMark(String assignmentTotalMark) { this.assignmentWeight = assignmentTotalMark; }
    public void setAssignmentDueDate(String assignmentDueDate) { this.assignmentDueDate = assignmentDueDate; }


}

