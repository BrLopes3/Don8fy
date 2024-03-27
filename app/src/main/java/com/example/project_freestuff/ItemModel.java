package com.example.project_freestuff;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

//class for the item added
public class ItemModel {

  String name, description, imageUri;


  //constructor
    public ItemModel(String name, String description, String imageUri) {
        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
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
