package com.example.casitapartyrentalsadmin.detailModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.example.casitapartyrentalsadmin.common.BasicEventCallBack;
import com.example.casitapartyrentalsadmin.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class RealtimeDatabase {
    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        mDatabaseAPI= FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void updateMueble(Mueble mueble,final BasicEventCallBack callBack){
        mDatabaseAPI.getMueblesReference().child(mueble.getId()).setValue(mueble)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callBack.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.onError();
                    }
                });
    }
}
