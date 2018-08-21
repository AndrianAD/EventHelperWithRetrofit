package com.example.event_retrofit.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("event_id")
    @Expose
    private String event_id;


    public Event(String name, String description, String time, String event_id) {
        this.name = name;
        this.description = description;
        this.time = time;
        this.event_id = event_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}


