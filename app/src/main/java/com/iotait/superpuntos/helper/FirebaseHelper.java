package com.iotait.superpuntos.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * FirebaseHelper is our database reference class '
 * from this class we get all firebase database reference
 */
public class FirebaseHelper {
    /*Declaration of local variables*/
    private static FirebaseHelper instance;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private static final String USERS = "users";
    /**Constructor for the helper*/
    private FirebaseHelper() {
        /*saving database reference to local variable*/
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }
    /**Instantiating the helper*/
    public static FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    /**Getting auth*/
    public FirebaseAuth getAuth() {
        return auth;
    }

    /**Getting current user*/
    public FirebaseUser getCurrentUser() {
        return currentUser = auth.getCurrentUser();
    }

    /**Getting user reference*/
    public DatabaseReference getUsersReference() {
        return databaseReference.child(USERS);
    }

    /**Getting current user reference*/
    public DatabaseReference getCurrentUserReference() {
        return getUsersReference().child(getCurrentUser().getUid());
    }

}
