package com.Piotrk_Kielak.Workname_1.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;


public class User extends RealmObject {
    @PrimaryKey @RealmField("_id")
    private String id;
    private RealmList<Opieka> memberOf;
    private String nick;
    private String name;
    private String _partition;
    private Boolean type;

    public RealmList<Opieka> getPolaczenie() {
        return memberOf;
    }

    public void setPolaczenie(RealmList<Opieka> polaczenie) {
        this.memberOf = polaczenie;
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

    public final boolean getType(){return this.type;}
    public final void setType(Boolean t){
        this.type=t;
    }

    public final String getNumer(){return this.name;}
    public final void setNumer(String nr){
        this.name=nr;
    }

//    public User(String id, String _partition,  RealmList memberOf,  String name, String nick, Boolean type) {
//        super();
//        this.id = id;
//        this._partition = _partition;
//        this.memberOf = memberOf;
//        this.numer = name;
//        this.nick = nick;
//        this.type = type;
//    }

}
