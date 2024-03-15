package com.example.project_freestuff;

import android.content.ClipData;
import android.widget.ImageView;

//class for the item added
public class ItemModel {

  String name, desctiption;

  ImageView picture;

  //constructor
  public ItemModel(String n, String d, ImageView img){
      name = n;
      desctiption = d;
      picture = img;
  }
  //getters and setters

    public String getName() {
        return name;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public ImageView getPicture() {
        return picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public void setPicture(ImageView picture) {
        this.picture = picture;
    }

}
