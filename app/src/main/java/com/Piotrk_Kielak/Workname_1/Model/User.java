package com.Piotrk_Kielak.Workname_1.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;


public class User extends RealmObject {
    @PrimaryKey @RealmField("_id")
    private String id;
    private RealmList<Opieka> polaczenie;
    private String nick;
    private String numer;
    private String _partition;
    private Boolean typ;

    public RealmList<Opieka> getPolaczenie() {
        return polaczenie;
    }

    public void setPolaczenie(RealmList<Opieka> polaczenie) {
        this.polaczenie = polaczenie;
    }
    public final String getId(){return this.id;}
    public final void setId(String id){
        this.id = id;
    }

    public final String get_partition(){return this._partition;}
    public final void set_partition(String part){
        this._partition=part;
    }

    public final String getNick() {return this. nick;}
    public final void setNick(String nc){
        this.nick=nc;
    }

    public final boolean getTyp(){return this.typ;}
    public final void setTyp(Boolean t){
        this.typ=t;
    }

    public final String getNumer(){return this.numer;}
    public final void setNumer(String nr){
        this.numer=nr;
    }



}
