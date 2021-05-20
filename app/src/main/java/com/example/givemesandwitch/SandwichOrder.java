package com.example.givemesandwitch;

import java.lang.reflect.Constructor;
import java.util.UUID;

public class SandwichOrder {

    private final String id;
    private String customerName;
    private int pickles;
    private boolean hummus;
    private boolean tahini;
    private String comment;
    private String status;

    public SandwichOrder(String customerName, int pickles, boolean hummus, boolean tahini, String comment)
    {
        this.id = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
        this.comment = comment;
        this.status = "waiting";
    }

    public boolean isHummus() {
        return hummus;
    }

    public boolean isTahini() {
        return tahini;
    }

    public int getPickles() {
        return pickles;
    }

    public String getComment() {
        return comment;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public String getId() {
        return id;
    }

    public void setOrderInProgress()
    {
        this.status = "in-progress";
    }

    public void setOrderReady()
    {
        this.status = "ready";
    }

    public void setOrderDone()
    {
        this.status = "done";
    }

    public void putHummus()
    {
        this.hummus = true;
    }

    public void removeHummus()
    {
        this.hummus = false;
    }

    public void putTahini()
    {
        this.tahini = true;
    }

    public void removeTahini()
    {
        this.tahini = false;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPickles(int pickles) {
        this.pickles = pickles;
    }

}
