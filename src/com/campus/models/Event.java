package com.campus.models;

import java.util.Date;

public class Event {
    private int id;
    private String title;
    private String description;
    private Date eventDate;
    private String location;
    private int capacity;

    public Event(int id, String title, String description, Date eventDate, String location, int capacity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.capacity = capacity;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getEventDate() { return eventDate; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
}
