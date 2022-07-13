package com.Piotrk_Kielak.Workname_1.Model;

import io.realm.RealmObject;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmField;

public class Opieka extends RealmObject {
    @SuppressWarnings("unused")
    private RealmList<User> userList;
    public RealmList<User> getUserList(){
        return userList;
    }
}
