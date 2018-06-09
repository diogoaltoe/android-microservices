package com.diogoaltoe.model;

public class Product {

    private int id;
    private String name;
    private String description;
    private Double cost;
    private String href;

    public Product() { }

    public Product(String name, String description, Double cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public Product(int id, String name, String description, Double cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String toString() {
        return "Id: " + getId()
                + ", Name: " + getName()
                + ", Description: " + getDescription()
                + ", Cost: " + getCost();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
