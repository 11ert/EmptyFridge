package de.thorsten.emptyfridge.model;

import java.io.Serializable;

/**
 * Created by Thorsten on 29.11.2015.
 */
public class ShoppingItem implements Serializable {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void ShoppingItem(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
