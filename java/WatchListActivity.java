package com.example.filmfinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WatchListActivity extends AppCompatActivity {

    TextView watchMovie;
    ArrayList<String> arrayList;
    Dialog dialog;
    Button watch;
    ImageView back, watchMoviePoster;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersDb, moviesDb;
    private String currentUserid;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserid = firebaseAuth.getCurrentUser().getUid();
        usersDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(currentUserid).child("matches").child("yeps");
        moviesDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Movies");
        watchMovie = findViewById(R.id.watchListMovieTitle);
        watch = findViewById(R.id.watchListButton);
        back = findViewById(R.id.watchListBack);
        watchMoviePoster = findViewById(R.id.watchListMoviePoster);
        arrayList = new ArrayList<>();

        // Get movies from Database and add them to the arrayList
        getMovies();

        // Set dialog and add selected movie on database
        watchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize dialog
                dialog = new Dialog(WatchListActivity.this);
                // Set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                // Set custom height and width
                dialog.getWindow().setLayout(650, 800);
                // Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.dialogSearch);
                ListView listView = dialog.findViewById(R.id.dialogList);

                // Initialize array adapter
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(WatchListActivity.this, android.R.layout.simple_list_item_1, arrayList);

                // Set adapter
                listView.setAdapter(arrayAdapter);

                // Set edit text
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // Filter array list
                        arrayAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // When item selected from list set selected item on text view
                        String name = arrayAdapter.getItem(i);
                        watchMovie.setText(name);
                        dialog.dismiss();

                        storageReference = FirebaseStorage.getInstance().getReference("movieImages/" + name + ".jpg");
                        try {
                            File localFile = File.createTempFile("tempfile", ".jpg");
                            storageReference.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            watchMoviePoster.setImageBitmap(bitmap);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    watchMoviePoster.setImageResource(R.drawable.logo);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        watchMoviePoster.setImageResource(R.drawable.logo);
                    }
                });
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movie = watchMovie.getText().toString();
                movie = movie.replace(' ', '+');
                Uri uri = Uri.parse("https://tenies-online.best/?story="+movie+"&sfSbm=Search&titleonly=3&do=search&subaction=search"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(WatchListActivity.this, SearchActivity.class);
                startActivity(backIntent);
                finish();
            }
        });
    }

    public void getMovies(){
        usersDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String movieId = ds.getKey();
                    moviesDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Movies").child(movieId).child("name");
                    moviesDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.getValue().toString();
                            arrayList.add(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}