package com.example.casitapartyrentalsadmin.common.pojo;



import com.google.firebase.database.Exclude;

import java.util.Objects;

public class Mueble {
    public static final String ID= "id";
    public static final String NAME= "name";
    public static final String QUANTITY= "quantity";
    public static final String PRICE= "price";
    public static final String DESCRIPTION= "description";
    public static final String PHOTO_URL= "photoURL";

    @Exclude
    private String id;
    private String name;
    private int quantity;
    private float price;
    private String description;
    private String photoURL;

    public Mueble() {
    }

    public Mueble(String id, String name, int quantity, float price, String description, String photoURL) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.photoURL = photoURL;
    }

    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mueble mueble = (Mueble) o;
        return Objects.equals(id, mueble.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
