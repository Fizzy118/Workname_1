package com.Piotrk_Kielak.Workname_1.Model;

import io.realm.RealmObject;

public class Opieka extends RealmObject {
    private String imie;
    private String partycja;
    //private int avatar;
//    public int getavatar() {
//        return avatar;
//    }
//
//    public void setavatar(String avatar) {
//        this.avatar = avatar;
//    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }



    public String getPartycja() {
        return partycja;
    }

    public void setPartycja(String partycja) {
        this.partycja = partycja;
    }


}
