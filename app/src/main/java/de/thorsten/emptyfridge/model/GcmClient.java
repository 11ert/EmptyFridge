package de.thorsten.emptyfridge.model;

import java.io.Serializable;

/**
 * Created by Thorsten on 29.11.2015.
 */
public class GcmClient implements Serializable {

    private long id;
    private String regId;

    public long getId() {
        return id;
    }

    public void ShoppingItem(long id, String regId) {
        this.id = id;
        this.regId = regId;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    @Override
    public String toString() {
        return "Gcm Client {" +
                "id=" + id +
                ", regId='" + regId+ '\'' +
                '}';
    }
}
