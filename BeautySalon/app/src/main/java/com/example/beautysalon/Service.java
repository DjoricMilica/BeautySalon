package com.example.beautysalon;

public class Service {
    String id;
    String name;
    Integer time;
    Float price;

    public Service(){

    }

    public Service(String id,String name, Integer time, Float price) {
        this.id=id;
        this.name = name;
        this.time = time;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
