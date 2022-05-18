package com.example.filmfinity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemoveMovieActivity extends AppCompatActivity {
    TextView removeMovie;
    ArrayList<String> arrayList;
    Dialog dialog;
    Button removeButton;
    ImageView back;
    private DatabaseReference moviesDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_movie);

        moviesDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Movies");
        removeMovie = findViewById(R.id.removeMovieTitle);
        removeButton = findViewById(R.id.removeMovieButton);
        back = findViewById(R.id.removeMovieBack);
        arrayList = new ArrayList<>();

        // Get movies from Database and add them to the arrayList
        getMovies();

        // Set dialog and add selected movie on database
        removeMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize dialog
                dialog = new Dialog(RemoveMovieActivity.this);
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
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RemoveMovieActivity.this, android.R.layout.simple_list_item_1, arrayList);

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
                        removeMovie.setText(arrayAdapter.getItem(i));
                        dialog.dismiss();
                        // Add favorite movie to user database
                    }
                });
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = removeMovie.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Movies");
                Query movieQuery = ref.orderByChild("name").equalTo(title);

                movieQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(RemoveMovieActivity.this, AdminActivity.class);
                startActivity(backIntent);
                finish();
            }
        });
    }

    public void getMovies(){
        moviesDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String name = ds.child("name").getValue().toString();
                    arrayList.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}