package com.abidjalionel.massguest;

public class Upload {
    private String eventname;
    private String photouri;
    private String eventowner;
    private String eventlocation;
    private String eventdates;
    private String eventseat;
    private String key;

    public Upload(String eventname, String photouri, String eventowner, String eventlocation, String eventdates, String eventseat, String key) {
        if (eventname.trim().equals(""))
        {
            eventname="No name";

        }

        this.eventname = eventname;
        this.photouri = photouri;
        this.eventowner = eventowner;
        this.eventlocation = eventlocation;
        this.eventdates = eventdates;
        this.eventseat = eventseat;
        this.key = key;
    }

    public Upload() {
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getPhotouri() {
        return photouri;
    }

    public void setPhotouri(String photouri) {
        this.photouri = photouri;
    }

    public String getEventowner() {
        return eventowner;
    }

    public void setEventowner(String eventowner) {
        this.eventowner = eventowner;
    }

    public String getEventlocation() {
        return eventlocation;
    }

    public void setEventlocation(String eventlocation) {
        this.eventlocation = eventlocation;
    }

    public String getEventdates() {
        return eventdates;
    }

    public void setEventdates(String eventdates) {
        this.eventdates = eventdates;
    }

    public String getEventseat() {
        return eventseat;
    }

    public void setEventseat(String eventseat) {
        this.eventseat = eventseat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}