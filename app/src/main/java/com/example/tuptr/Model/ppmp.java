package com.example.tuptr.Model;

import java.util.ArrayList;

public class ppmp {

    private String title, Id, PId;
    private int icon;

    private ArrayList<ppmp> childItemList = new ArrayList<ppmp>();


    public ppmp(String title, String Id) {
        this.title = title;
        this.Id = Id;
    }

    public ppmp(String title, String Id, String PId) {
        this.title = title;
        this.Id = Id;
        this.PId = PId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getId() {
        return this.Id;
    }

    public String getPId() {
        return this.PId;
    }


    public int getIcon() {
        return this.icon;
    }

    public ArrayList<ppmp> getChildItem() {
        System.out.println("CHILD_ITEM = "+childItemList);
        return childItemList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public void setPId(String PId) {
        this.PId = PId;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setChildItem(ArrayList<ppmp> childItemList) {
        this.childItemList = childItemList;
    }

}