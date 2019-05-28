package com.uow.snazzikiel.prepareo;

class assignmentsData {

    String assignmentName;
    String assignmentWeight;


    public assignmentsData(String assignmentName, String assignmentWeight ){
        this.assignmentName = assignmentName;
        this.assignmentWeight = assignmentWeight;

    }

    public String getAssignmentName(){ return assignmentName; }
    public String getAssignmentWeight(){ return assignmentWeight; }

    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }
    public void setAssignmentWeight(String assignmentWeight) { this.assignmentWeight = assignmentWeight; }


}

