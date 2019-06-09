package com.uow.snazzikiel.prepareo;

import com.multilevelview.models.RecyclerViewItem;

/*
    Class:   profileData
    ---------------------------------------
    NOT USED - Not used because Profile dropdown list is not used
*/
public class profileData extends RecyclerViewItem {

    String text="";

    /*String secondText = "";

    public String getSecondText() {
        return secondText;
    }
    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }*/


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    protected profileData(int level) {
        super(level);
    }
}
