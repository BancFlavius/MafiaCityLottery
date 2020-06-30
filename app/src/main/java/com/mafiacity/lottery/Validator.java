package com.mafiacity.lottery;


import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Validator {

    private static final byte PASSWORD_LENGTH = 6;
    private static final byte NAME_MAX_LENGTH = 16;
    private static boolean emailFound = false;


    /**
     * Every TextInputLayout must have an EditText child
     *
     * @param etEmail           TextInputLayout
     * @param etPassword        TextInputLayout
     * @param etConfirmPassword TextInputLayout
     * @return a string[] containing email and password, or null if not valid
     */
    public static String[] validateRegistration(TextInputLayout etEmail, TextInputLayout etPassword, TextInputLayout etConfirmPassword) {
        Context context = etEmail.getContext();

        String email = etEmail.getEditText().getText().toString();
        String password = etPassword.getEditText().getText().toString();
        String confirmPass = etConfirmPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(context.getString(R.string.error_email_empty));
            return null;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(context.getString(R.string.error_pass_empty));
            return null;
        }

        if (TextUtils.isEmpty(confirmPass)) {
            etConfirmPassword.setError(context.getString(R.string.error_pass_empty));
            return null;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(context.getString(R.string.error_email_invalid));
            return null;
        }

        if (password.length() < PASSWORD_LENGTH) {
            etPassword.setError(context.getString(R.string.error_password_short));
            return null;
        }

        if (!TextUtils.equals(password, confirmPass)) {
            etConfirmPassword.setError(context.getString(R.string.error_password_mismatch));
            return null;
        }

        etConfirmPassword.setErrorEnabled(false);
        etEmail.setErrorEnabled(false);
        etPassword.setErrorEnabled(false);
        return new String[]{email, password};
    }

    public static void checkEmailExists(String email, MyCallback myCallback) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(FireBaseContract.USERS_CHILD)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                User user = data.getValue(User.class);
                                if (user != null && user.getEmail().equals(email)) {
                                    myCallback.onCallback(true);
                                    break;
                                }else{
                                    myCallback.onCallback(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println(error.getMessage() + ". " + error.getDetails());
                    }
                });
    }

    /**
     * Every TextInputLayout must have an EditText child
     *
     * @param etEmail    TextInputLayout
     * @param etPassword TextInputLayout
     * @return a string[] containing email and password, or null if not valid
     */
    public static String[] validateLogin(TextInputLayout etEmail, TextInputLayout etPassword) {
        Context context = etEmail.getContext();

        String email = etEmail.getEditText().getText().toString();
        String password = etPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(context.getString(R.string.error_email_empty));
            return null;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(context.getString(R.string.error_email_invalid));
            return null;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(context.getString(R.string.error_pass_empty));
            return null;
        }

        if (password.length() < PASSWORD_LENGTH) {
            etPassword.setError(context.getString(R.string.error_password_short));
            return null;
        }

        etEmail.setErrorEnabled(false);
        etPassword.setErrorEnabled(false);
        return new String[]{email, password};
    }

    public static String[] validateDisplayName(TextInputLayout firstnameView, TextInputLayout lastnameView) {
        firstnameView.setError(null);
        lastnameView.setError(null);

        Context context = firstnameView.getContext();

        String firstname = firstnameView.getEditText().getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            firstnameView.setError(context.getString(R.string.error_firstname_empty));
            return null;
        }
        if (firstname.length() > NAME_MAX_LENGTH) {
            firstnameView.setError(context.getString(R.string.error_name_long));
            return null;
        }
        String lastname = lastnameView.getEditText().getText().toString();
        if (TextUtils.isEmpty(lastname)) {
            lastnameView.setError(context.getString(R.string.error_lastname_empty));
            return null;
        }
        if (lastname.length() > NAME_MAX_LENGTH) {
            lastnameView.setError(context.getString(R.string.error_name_long));
            return null;
        }

        firstnameView.setErrorEnabled(false);
        lastnameView.setErrorEnabled(false);
        return new String[]{firstname, lastname};
    }

    public static String[] validateNewPassword(TextInputLayout oldPasswordView, TextInputLayout newPasswordView, TextInputLayout confirmPasswordView) {

        oldPasswordView.setError(null);
        newPasswordView.setError(null);
        confirmPasswordView.setError(null);

        Context context = oldPasswordView.getContext();

        String oldPassword = oldPasswordView.getEditText().getText().toString();
        if (TextUtils.isEmpty(oldPassword)) {
            oldPasswordView.setError(context.getString(R.string.error_pass_empty));
            return null;
        }

        if (oldPassword.length() < PASSWORD_LENGTH) {
            oldPasswordView.setError(context.getString(R.string.error_password_short));
            return null;
        }

        String newPassword = newPasswordView.getEditText().getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            newPasswordView.setError(context.getString(R.string.error_pass_empty));
            return null;
        }

        if (newPassword.length() < PASSWORD_LENGTH) {
            newPasswordView.setError(context.getString(R.string.error_password_short));
            return null;
        }

        String confirmPassword = confirmPasswordView.getEditText().getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordView.setError(context.getString(R.string.error_pass_empty));
            return null;
        }

        if (confirmPassword.length() < PASSWORD_LENGTH) {
            confirmPasswordView.setError(context.getString(R.string.error_password_short));
            return null;
        }

        if (!TextUtils.equals(newPassword, confirmPassword)) {
            confirmPasswordView.setError(context.getString(R.string.error_password_mismatch));
            return null;
        }

        oldPasswordView.setErrorEnabled(false);
        newPasswordView.setErrorEnabled(false);
        confirmPasswordView.setErrorEnabled(false);
        return new String[]{oldPassword, newPassword};
    }
}
