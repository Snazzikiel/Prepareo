package com.uow.snazzikiel.prepareo;

class goalsData {

    String goalTitle;
    String goalDescription;
    String goalDueDate;


    public goalsData(String goalTitle, String goalDueDate, String dueDate ){
        this.goalTitle = goalTitle;
        this.goalDescription = goalDescription;
        this.goalDueDate = goalDueDate;


    }

    public String getGoalTitle(){ return goalTitle; }
    public String getGoalDueDate(){ return goalDueDate; }
    public String getGoalDescription() { return goalDueDate; }

    public void setGoalTitle(String goalTitle) { this.goalTitle = goalTitle; }
    public void setGoalDueDate(String goalDueDate) { this.goalDueDate = goalDueDate; }
    public void setGoalDescription(String goalDescription) { this.goalDescription = goalDescription; }


}

