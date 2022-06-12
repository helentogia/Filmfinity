package com.example.filmfinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewMovieActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    Dialog dialog;
    TextView reviewMovie;
    Button review;
    ImageView back;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersDb, moviesDb;
    private String currentUserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_movie);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserid = firebaseAuth.getCurrentUser().getUid();
        usersDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(currentUserid).child("matches").child("yeps");
        moviesDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Movies");
        reviewMovie = findViewById(R.id.reviewMovieTitle);
        review = findViewById(R.id.reviewButton);
        back = findViewById(R.id.reviewMovieBack);
        arrayList = new ArrayList<>();

        // Get movies from Database and add them to the arrayList
        getMovies();

        reviewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize dialog
                dialog = new Dialog(ReviewMovieActivity.this);
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ReviewMovieActivity.this, android.R.layout.simple_list_item_1, arrayList);

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
                        reviewMovie.setText(arrayAdapter.getItem(i));
                        dialog.dismiss();



                        // Add favorite movie to user database
                    }
                });
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = reviewMovie.getText().toString();
                Toast.makeText(ReviewMovieActivity.this, "Thank you for reviewing " + name, Toast.LENGTH_LONG).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(ReviewMovieActivity.this, SearchActivity.class);
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