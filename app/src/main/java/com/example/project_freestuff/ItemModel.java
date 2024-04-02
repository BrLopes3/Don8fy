package com.example.project_freestuff;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

//class for the item added
public class ItemModel {

  String itemId, name, description, imageUri;


  //constructor

    public ItemModel() {
    }

    public ItemModel(String itemId, String name, String description, String imageUri) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
    }

    //getters and setters

    public String getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
