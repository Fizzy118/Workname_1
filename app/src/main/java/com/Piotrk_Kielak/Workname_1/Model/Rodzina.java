package com.Piotrk_Kielak.Workname_1.Model;
import org.bson.Document;
public class Rodzina {
    private String name;

    public Rodzina(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

