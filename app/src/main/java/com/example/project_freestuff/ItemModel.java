package com.example.project_freestuff;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.widget.ImageView;

//class for the item added
public class ItemModel {

  String name, description, itemId, imageData;


  //constructor
  public ItemModel(String n, String d, String id, String img){
      name = n;
      description = d;
      itemId = id;
      imageData = img;
  }
  //getters and setters

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageData() {
        return imageData;
    }

    public String getItemId() {
        return itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desctiption) {
        this.description = desctiption;
    }

    public void setImageData(String imgData) {
        imageData = imgData;
    }
}
