package com.Piotrk_Kielak.Workname_1.Model;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;


public class User extends RealmObject {
    @PrimaryKey @RealmField("_id")
    //private ObjectId id;
    //@BsonProperty(value = "_id")
    private int _id;
    @BsonProperty(value = "_partition")
    private int partition;

//    public ObjectId getId() {
//        return id;
//    }
//
//    public void setId(ObjectId id) {
//        this.id = id;
//    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public boolean isTyp() {
        return typ;
    }

    public void setTyp(boolean typ) {
        this.typ = typ;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    private String name;
    private String nick;
    private String tasks;
    private boolean typ;
    private String map;























//    @PrimaryKey @RealmField("_id")
//    private int id;
//
//    public RealmList getMembers() {
//        return members;
//    }
//
//    public void setMembers(RealmList members) {
//        this.members = members;
//    }
//
//    RealmList<User> members;
//    private String nick;
//    private String name;
//    private String _partition;
//    private Boolean type;
//
//
//    public final int getId(){return this.id;}
//    public final void setId(int id){
//        this.id = id;
//    }
//
//    public final String get_partition(){return this._partition;}
//    public final void set_partition(String part){
//        this._partition=part;
//    }
//
//    public final String getNick() {return this. nick;}
//    public final void setNick(String nc){
//        this.nick=nc;
//    }
//
//    public final boolean getType(){return this.type;}
//    public final void setType(Boolean t){
//        this.type=t;
//    }
//
//    public final String getNumer(){return this.name;}
//    public final void setNumer(String nr){
//        this.name=nr;
//    }
//
//


}
