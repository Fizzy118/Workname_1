package com.Piotrk_Kielak.Workname_1;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Model extends RealmObject {
    @PrimaryKey
    private int pp_id;
    private String pp_nick;
    private int pp_number;
    private Boolean pp_opiekun;

    public int getPp_id(){
        return pp_id;
    }
    public void setPp_id(int pp_id){
        this.pp_id=pp_id;
    }

    public String getPp_nick() {
        return pp_nick;
    }

    public void setPp_nick(String pp_nick) {
        this.pp_nick = pp_nick;
    }

    public int getPp_number() {
        return pp_number;
    }

    public void setPp_number(int pp_number) {
        this.pp_number = pp_number;
    }

    public Boolean getPp_opiekun() {
        return pp_opiekun;
    }

    public void setPp_opiekun(Boolean pp_opiekun) {
        this.pp_opiekun = pp_opiekun;
    }
}
