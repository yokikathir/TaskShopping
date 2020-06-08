package com.task.taskshopping.cart;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class Cart {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    @NonNull
    @ColumnInfo(name = "special")
    private String special;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    @NonNull
    @ColumnInfo(name = "images")
    private String images;
    @NonNull
    @ColumnInfo(name = "price")
    private String price;

    @NonNull
    @ColumnInfo(name = "count")
    private int count;
    public Cart() {
    }

    public Cart(@NonNull String name, @NonNull String special, int quantity, @NonNull String images, @NonNull String price, int count) {
        this.name = name;
        this.special = special;
        this.quantity = quantity;
        this.images = images;
        this.price = price;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getSpecial() {
        return special;
    }

    public void setSpecial(@NonNull String special) {
        this.special = special;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public String getImages() {
        return images;
    }

    public void setImages(@NonNull String images) {
        this.images = images;
    }

    @NonNull
    public String getPrice() {
        return price;
    }

    public void setPrice(@NonNull String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
