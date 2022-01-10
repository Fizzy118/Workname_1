package com.Piotrk_Kielak.Workname_1.Model;
import org.bson.Document;
public class Rodzina {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Rodzina(Document document) {
        super();
        this.id = (String)document.get("_id");
        this.name = (String)document.get("name");
        }
}
