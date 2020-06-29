package com.mafiacity.lottery;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
//import com.google.gson.Gson;


/**
 * Created by doruchidean on 1/16/17.
 *
 */

public class User {

    @PropertyName(FireBaseContract.User.FIRST_NAME_FIELD)
    public String firstname;
    @PropertyName(FireBaseContract.User.LAST_NAME_FIELD)
    public String lastname;
    @PropertyName(FireBaseContract.User.EMAIL_FIELD)
    public String email;

    @Exclude
    public String getFullName() {
        //cap words
        if (!TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname)) {
            return firstname + " " + lastname;
        }

        return null;
    }

    @Exclude
    public void setFullName(String fullName) {
        String[] firstAndLast = fullName.split(" ");
        this.firstname = firstAndLast[0];
        this.lastname = firstAndLast[1];
    }

//    @Override
//    public String toString() {
//        return new Gson().toJson(this);
//    }
}

