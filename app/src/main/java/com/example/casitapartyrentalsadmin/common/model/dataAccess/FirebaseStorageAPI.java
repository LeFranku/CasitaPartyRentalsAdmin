package com.example.casitapartyrentalsadmin.common.model.dataAccess;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageAPI {

    private StorageReference mStorage;
    private static FirebaseStorageAPI INSTANCE=null;
    private static final String IMAGE_DIRECTORY = "/Images";
    private static final String IMAGE_MUEBLES = "/muebles";
    private static final String PHOTO = "/photo";
    private int num=0;
    public String downloadPath;
    private FirebaseStorageAPI() {
        mStorage = FirebaseStorage.getInstance().getReference();
    }
    public static FirebaseStorageAPI getInstance(){
        if (INSTANCE==null){
            INSTANCE=new FirebaseStorageAPI();
        }
        return INSTANCE;
    }
    public StorageReference getmReference(){
        return mStorage;
    }

    /**
     * Upload the image of storage.
     * @param context Activity to send a result.
     */
    public void uploadImageStorage(Context context,Uri mPhotoSelectedUri) {
        StorageReference imageReference= mStorage.child(IMAGE_DIRECTORY).child(IMAGE_MUEBLES);
        StorageReference imgP= imageReference.child(PHOTO);
        imgP.putFile(mPhotoSelectedUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //la imagen se ha subido con exito.

                        imgP.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                 setDownloadPath(uri.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error al subir la foto", Toast.LENGTH_LONG).show();
            }
        });
    }

    private  void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }
}