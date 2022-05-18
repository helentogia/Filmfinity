package com.example.filmfinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private Button bRegister;
    private EditText etEmail, etPassword, etName, etSurname, etConfirmPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(RegistrationActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        bRegister = (Button) findViewById(R.id.registrationButton);
        etEmail = (EditText) findViewById(R.id.registrationEmail);
        etPassword = (EditText) findViewById(R.id.registrationPassword);
        etName = (EditText) findViewById(R.id.registrationName);
        etSurname = (EditText) findViewById(R.id.registrationSurname);
        etConfirmPassword = (EditText) findViewById(R.id.registrationConfirmPassword);

        bRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String name = etName.getText().toString();
                final String surname = etSurname.getText().toString();
                final String confirmPassword = etConfirmPassword.getText().toString();

                if (password.equals(confirmPassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "An error occurred during the registration.", Toast.LENGTH_SHORT).show();
                            } else{
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(userId).child("name");
                                currentUserDb.setValue(name);
                                currentUserDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(userId).child("surname");
                                currentUserDb.setValue(surname);
                                currentUserDb = FirebaseDatabase.getInstance("https://filmfinity-97a07-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(userId).child("property");
                                currentUserDb.setValue("user");
                            }
                        }
                    });
                }
                else
                    Toast.makeText(RegistrationActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}