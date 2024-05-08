package com.example.trokyy.models;

public class Type {
    private int id;
    private String type;

    public Type() {}

    public Type(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
