package com.example.filmfinity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards> {
    Context context;
    StorageReference storageReference;

    public arrayAdapter(Context context, int resourceId, List<Cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView movieName = (TextView) convertView.findViewById(R.id.movieName);
        ImageView moviePoster = (ImageView) convertView.findViewById(R.id.moviePoster);
        TextView movieDescription = (TextView) convertView.findViewById(R.id.movieDescription);

        movieName.setText(card_item.getName());
        movieDescription.setText(card_item.getDescription());

        storageReference = FirebaseStorage.getInstance().getReference("movieImages/" + card_item.getName() + ".jpg");
        try {
            File localFile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            moviePoster.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            moviePoster.setImageResource(R.drawable.logo);
                        }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        moviePoster.setImageResource(R.drawable.logo);
        return convertView;
    }
}
