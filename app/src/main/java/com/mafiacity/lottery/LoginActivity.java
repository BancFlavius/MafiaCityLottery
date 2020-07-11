package com.mafiacity.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private Button btnLogin;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initializeViews();
    }

    private void initializeViews(){
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this::onClick);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUi(currentUser);
    }

    private void updateUi(FirebaseUser user) {
        if(!user.getDisplayName().isEmpty()){
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
        }

    }

    private void signOut() {
        mAuth.signOut();
        updateUi(null);
    }

    private void signIn() {

        TextInputLayout emailView = findViewById(R.id.et_email_layout);
        TextInputLayout passwordView = findViewById(R.id.et_password_layout);

        String[] credentials = Validator.validateLogin(emailView, passwordView);
        if( credentials != null ) {

            mAuth.signInWithEmailAndPassword(credentials[0], credentials[1])
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("INFO", "successful sign in");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUi(user);
                        } else {
                            Log.d("INFO", "Unsuccessful sign in");
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUi(null);
                        }
                    });
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_existing_account:
                Log.d("INFO", "Finishing LoginActivity");
                finish();
                break;
            case R.id.btn_login:
                Log.d("INFO", "User log in");
                signIn();
                break;
        }
    }

}
