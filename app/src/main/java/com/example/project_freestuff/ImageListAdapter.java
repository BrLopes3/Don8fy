package com.example.project_freestuff;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemModel> arrayList;

    AdapterView.OnItemClickListener onItemClickListener;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public ImageListAdapter(Context context, ArrayList<ItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewProductName);
            imageView = itemView.findViewById(R.id.imageViewProduct);

        }
    }

    @NonNull
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = arrayList.get(position);
        holder.title.setText(item.getName());
        Log.d("ImageAdapter", "Image URI to download: " + item.getImageUri());
        // Check if the image is already loaded
        if (item.getImageUri() != null && !item.getImageUri().isEmpty()) {
            Glide.with(context).load(item.getImageUri()).into(holder.imageView);
        } else {
            // Load the image from storage if it's not already loaded
            loadImageFromStorage(item, holder.imageView);
            // Use a placeholder image while loading
            Glide.with(context).load(R.drawable.androidicon).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void loadImageFromStorage(ItemModel item, ImageView imageView) {

        // Initialize Firebase
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images/");

        // Get a reference to the image in Firebase Storage
        StorageReference imageRef = storageReference.child((item.getImageUri()));

        // Download the image from Firebase Storage
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Load the image into the ImageView using Glide
                Glide.with(context).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                Toast.makeText(context, "Failed to load image for item: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getItems() {

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("items");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemModel item = snapshot.getValue(ItemModel.class);
                    arrayList.add(item);

                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(ItemModel item);
    }

}
