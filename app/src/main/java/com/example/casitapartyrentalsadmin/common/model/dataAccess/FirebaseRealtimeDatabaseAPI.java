package com.example.casitapartyrentalsadmin.common.model.dataAccess;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRealtimeDatabaseAPI {
    private static final String PATH_MUEBLES="muebles";
    private DatabaseReference mReference;

    private static FirebaseRealtimeDatabaseAPI INSTANCE=null;

    private FirebaseRealtimeDatabaseAPI() {
        mReference= FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseRealtimeDatabaseAPI getInstance(){
        if (INSTANCE==null){
            INSTANCE= new FirebaseRealtimeDatabaseAPI();
        }
        return INSTANCE;
    }

    //Referencias
    public DatabaseReference getmReference(){
        return mReference;
    }
    public DatabaseReference getMueblesReference(){
        return getmReference().child(PATH_MUEBLES);
    }

}
