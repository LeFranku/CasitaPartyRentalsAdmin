package com.example.casitapartyrentalsadmin.addModule.model.dataAccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.casitapartyrentalsadmin.R;
import com.example.casitapartyrentalsadmin.addModule.events.AddMuebleEvent;
import com.example.casitapartyrentalsadmin.common.BasicErrorEventCallback;
import com.example.casitapartyrentalsadmin.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class RealTimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealTimeDatabase() {
        mDatabaseAPI=FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void addMueble(Mueble mueble, BasicErrorEventCallback callback){
        mDatabaseAPI.getMueblesReference().push().setValue(mueble, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError==null){
                    callback.onSuccess();
                }else{
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            callback.onError(AddMuebleEvent.ERROR_MAX_VALUE, R.string.addMueble_message_validate_max_quantity);
                            break;
                        default:
                            callback.onError(AddMuebleEvent.ERROR_SERVER,R.string.addMueble_message_added_error);
                    }
                }
            }
        });
    }
}
