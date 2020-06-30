package com.mafiacity.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignUpActivity extends AppCompatActivity {


    private Button btnStartActivity;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        initializeViews();
    }

    private void signUp() {
        TextInputLayout firstname = findViewById(R.id.et_firstname_layout);
        TextInputLayout lastname = findViewById(R.id.et_lastname_layout);

        String[] displayName = Validator.validateDisplayName(firstname, lastname);
        if (displayName != null) {
            TextInputLayout email = findViewById(R.id.et_email_layout);
            TextInputLayout password = findViewById(R.id.et_password_layout);
            TextInputLayout pasConfirm = findViewById(R.id.et_password_confirm_layout);

            Validator.checkEmailExists(email.getEditText().getText().toString(), exists -> {
                if(exists){
                    email.setError(getString(R.string.error_email_exists));
                }else{
                    email.setErrorEnabled(false);
                    String[] emailPassword = Validator.validateRegistration(email, password, pasConfirm);

                    if (emailPassword != null) {
                        mAuth.createUserWithEmailAndPassword(emailPassword[0], emailPassword[1]).addOnCompleteListener(
                                task -> {
                                    if (task.isSuccessful()) {
                                        AuthResult result = task.getResult();
                                        if (result != null) {
                                            FirebaseUser user = result.getUser();
                                            postNewUser(user.getUid(), user.getEmail(), displayName[0], displayName[1]);
                                        }
                                    }
                                }
                        );
                    }
                }
            });

        }

    }

    private void postNewUser(String userKey, String email, String firstName, String lastName) {
        User user = new User();

        user.email = email;
        user.firstname = firstName;
        user.lastname = lastName;

        FirebaseDatabase.getInstance()
                .getReference()
                .child(FireBaseContract.USERS_CHILD)
                .child(userKey)
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
                    firebaseMessaging.subscribeToTopic(FireBaseContract.NEW_LOTO_EVENT);
                });
    }

    private void initializeViews() {
        btnStartActivity = findViewById(R.id.btn_signup);
        btnStartActivity.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_existing_account:
                Log.d("INFO", "starting login activity");
                SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                break;
            case R.id.btn_signup:
                Log.d("INFO", "user sign up");
                signUp();
                break;
        }
    }

}
