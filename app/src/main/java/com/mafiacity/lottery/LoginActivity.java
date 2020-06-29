package com.mafiacity.lottery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailView, passwordView;

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
        emailView = findViewById(R.id.et_email);
        passwordView = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this::onClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        updateUi(currentUser);
    }

    private void updateUi(FirebaseUser user) {

    }

    private void signOut() {
        mAuth.signOut();
        updateUi(null);
    }

    private void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        System.out.println("successful sign in");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUi(user);
                    } else {
                        System.out.println("unsuccessful sign in");
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUi(null);
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailView.setError("Required.");
            valid = false;
        } else {
            emailView.setError(null);
        }

        String password = passwordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordView.setError("Required.");
            valid = false;
        } else {
            passwordView.setError(null);
        }

        return valid;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_existing_account:
                System.out.println("Finishing LoginActivity");
                finish();
                break;
            case R.id.btn_signup:
                System.out.println("User log in");
                signIn(emailView.getText().toString(), passwordView.getText().toString());
                break;
        }
    }
}
