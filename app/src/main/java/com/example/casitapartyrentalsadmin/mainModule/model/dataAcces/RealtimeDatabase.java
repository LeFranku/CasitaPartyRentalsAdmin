package com.example.casitapartyrentalsadmin.mainModule.model.dataAcces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.casitapartyrentalsadmin.R;
import com.example.casitapartyrentalsadmin.common.BasicErrorEventCallback;
import com.example.casitapartyrentalsadmin.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.mainModule.events.MainEvent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class RealtimeDatabase {


    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    private ChildEventListener mMueblesChildEventListener;


    public RealtimeDatabase() {
        mDatabaseAPI= FirebaseRealtimeDatabaseAPI.getInstance();

    }



    public void subscribeToMuebles(MueblesEventListener listener){
        if (mMueblesChildEventListener ==null){
            mMueblesChildEventListener =new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onChildAdded(getMueble(dataSnapshot));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onChildUpdated(getMueble(dataSnapshot));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    listener.onChildRemoved(getMueble(dataSnapshot));
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.min_error_permission_denied);
                            break;
                        default:
                            listener.onError(R.string.main_error_server);
                            break;
                    }
                }
            };
        }
        mDatabaseAPI.getMueblesReference().addChildEventListener(mMueblesChildEventListener);
    }

    private Mueble getMueble(DataSnapshot dataSnapshot) {
        Mueble mueble=dataSnapshot.getValue(Mueble.class);
        if (mueble!=null){
            mueble.setId(dataSnapshot.getKey());
        }
        return mueble;

    }
    public void unsubscribeToMuebles(){
        if(mMueblesChildEventListener!=null){
            mDatabaseAPI.getMueblesReference().removeEventListener(mMueblesChildEventListener);
        }
    }

    public void removeMueble(Mueble mueble, BasicErrorEventCallback callback){
        mDatabaseAPI.getMueblesReference().child(mueble.getId())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError==null){
                            callback.onSuccess();
                        }else{
                            switch (databaseError.getCode()){
                                case DatabaseError.PERMISSION_DENIED:
                                    callback.onError(MainEvent.ERROR_TO_REMOVE,R.string.main_error_remove);
                                    break;
                                default:
                                    callback.onError(MainEvent.ERROR_SERVER,R.string.main_error_server);
                            }
                        }
                    }
                });
    }
}
