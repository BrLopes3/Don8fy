package com.example.project_freestuff;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.widget.ImageView;

//class for the item added
public class ItemModel {

  String name, desctiption, itemId;

//    byte[] picture;

  //constructor
  public ItemModel(String n, String d, String id){
      name = n;
      desctiption = d;
      itemId = id;
  }
  //getters and setters

    public String getName() {
        return name;
    }

    public String getDesctiption() {
        return desctiption;
    }

//    public byte[] getPicture() {
//        return picture;
//    }

    public String getItemId() {
        return itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

//    public void setPicture(byte[] picture) {
//        this.picture = picture;
//    }
//

}
