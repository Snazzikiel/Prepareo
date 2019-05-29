package com.uow.snazzikiel.prepareo;

class goalsData {

    String goalTitle;
    String goalDueDate;
    String goalDesc;
    int goalProgres;
    boolean goalCompleted = false;



    public goalsData(String goalTitle, String goalDueDate ){
        this.goalTitle = goalTitle;
        this.goalDueDate = goalDueDate;

    }

    public goalsData(String goalTitle, String goalDueDate, String goalDesc ){
        this.goalTitle = goalTitle;
        this.goalDueDate = goalDueDate;
        this.goalDesc = goalDesc;

    }

    public String getGoalTitle(){ return goalTitle; }
    public String getGoalDueDate(){ return goalDueDate; }

    public String getGoalDesc() {
        return goalDesc;
    }

    public void setGoalTitle(String goalTitle) { this.goalTitle = goalTitle; }
    public void setGoalDueDate(String goalDueDate) { this.goalDueDate = goalDueDate; }

    public void setGoalDesc(String goalDesc) {
        this.goalDesc = goalDesc;
    }
}

