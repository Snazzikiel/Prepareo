package com.uow.snazzikiel.prepareo;

class goalsData {

    String goalTitle;
    String goalDueDate;


    public goalsData(String goalTitle, String goalDueDate ){
        this.goalTitle = goalTitle;
        this.goalDueDate = goalDueDate;

    }

    public String getGoalTitle(){ return goalTitle; }
    public String getGoalDueDate(){ return goalDueDate; }

    public void setGoalTitle(String goalTitle) { this.goalTitle = goalTitle; }
    public void setGoalDueDate(String goalDueDate) { this.goalDueDate = goalDueDate; }


}

