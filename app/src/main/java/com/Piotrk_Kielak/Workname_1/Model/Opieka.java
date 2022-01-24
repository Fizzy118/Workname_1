package com.Piotrk_Kielak.Workname_1.Model;

import io.realm.RealmObject;

public class Opieka extends RealmObject {
    private String name;
    private String partition;


    public String getImie() {
        return name;
    }

    public void setImie(String name) {
        this.name = name;
    }



    public String getPartycja() {
        return partition;
    }

    public void setPartycja(String partycja) {
        this.partition = partycja;
    }


}
