package com.example.casitapartyrentalsadmin.addModule.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.casitapartyrentalsadmin.R;
import com.example.casitapartyrentalsadmin.addModule.AddMueblePresenter;
import com.example.casitapartyrentalsadmin.addModule.AddMueblePresenterClass;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.common.utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMuebleActivity extends AppCompatActivity implements AddMuebleView  {


    private AddMueblePresenter mPresenter;
    private StorageReference mStorage;
    private static final String IMAGE_DIRECTORY = "/Images";
    private static final String IMAGE_MUEBLES = "/muebles";
    private static String PHOTO = "/photo";
    private static final int RC_GALLERY = 21;
    private static final int RC_CAMERA = 22;
    String URL=null;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgFoto)
    AppCompatImageView imgFoto;
    @BindView(R.id.imgDeleteFoto)
    AppCompatImageView imgDeleteFoto;
    @BindView(R.id.imgFromGallery)
    AppCompatImageView imgFromGallery;
    @BindView(R.id.imgFromUrl)
    AppCompatImageView imgFromUrl;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.etDescription)
    TextInputEditText etDescription;
    @BindView(R.id.tilDescription)
    TextInputLayout tilDescription;
    @BindView(R.id.etPrice)
    TextInputEditText etPrice;
    @BindView(R.id.tilPrice)
    TextInputLayout tilPrice;
    @BindView(R.id.etQuantity)
    TextInputEditText etQuantity;
    @BindView(R.id.tilQuantity)
    TextInputLayout tilQuantity;
    @BindView(R.id.pbContent)
    ProgressBar pbContent;
    @BindView(R.id.contentMain)
    CoordinatorLayout contentMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mueble);
        ButterKnife.bind(this);
        configActionBar();
        configFocus();
        mPresenter=new AddMueblePresenterClass(this);
        mPresenter.onCreate();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int resOk =RESULT_OK;
        if (resultCode == resOk) {
            switch (requestCode) {
                case RC_GALLERY:
                    if (data != null) {
                        Uri mPhotoSelectedUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                    mPhotoSelectedUri);
                            uploadImageStorage(AddMuebleActivity.this,mPhotoSelectedUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }public void uploadImageStorage(Context context, Uri mPhotoSelectedUri) {
        mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference imageReference= mStorage.child(IMAGE_DIRECTORY).child(IMAGE_MUEBLES);

            StorageReference imgP= imageReference.child(PHOTO+etName.getText().toString());
            imgP.putFile(mPhotoSelectedUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //la imagen se ha subido con exito.

                            imgP.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    URL=uri.toString();
                                    configImageView(URL);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Error al subir la foto", Toast.LENGTH_LONG).show();
                }
            });
            //

    }

    @OnClick({R.id.imgDeleteFoto, R.id.imgFromGallery, R.id.imgFromUrl})
    public void imageEvents(View view) {
        switch (view.getId()) {
            case R.id.imgDeleteFoto:
                if (URL!=null) {
                    MaterialAlertDialogBuilder builder =new MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.detalle_dialogDelete_title)
                            .setMessage(R.string.detalle_dialogDelete_message)
                            .setPositiveButton(R.string.label_dialog_delete, (dialogInterface, i) -> configImageView(null))
                            .setNegativeButton(R.string.label_dialog_cancel, null);
                    builder.show();
                }
                break;
            case R.id.imgFromGallery:
                if (!etName.getText().toString().trim().isEmpty()) {
                    fromGallery();
                }else{
                    Toast.makeText(AddMuebleActivity.this, R.string.addMueble_name_requied, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.imgFromUrl:
                showAddPhotoDialog();
                break;
        }
    }
    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RC_GALLERY);
    }

    private void showAddPhotoDialog() {
        final EditText etFotoUrl = new EditText(this);
        MaterialAlertDialogBuilder builder =new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.addArtist_dialogUrl_title)
                .setPositiveButton(R.string.label_dialog_add, (dialogInterface, i) ->{
                           URL= etFotoUrl.getText().toString().trim();
                            configImageView(URL);})
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.setView(etFotoUrl);
        builder.show();
    }

    private void configImageView(String fotoUrl) {
        if (fotoUrl != null) {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop();

            Glide.with(this)
                    .load(fotoUrl)
                    .apply(options)
                    .into(imgFoto);
            Toast.makeText(this, ""+fotoUrl, Toast.LENGTH_SHORT).show();
        } else {
            imgFoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_photo_size_select_actual));
        }
    }


    private void configFocus() {
        etName.requestFocus();
    }


    private void configActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                break;
            case R.id.action_save:
                Mueble mueble= null;
                if (CommonUtils.validateMueble(this,etName,etPrice,etQuantity,etDescription,URL
                ,tilName,tilPrice,tilQuantity,tilDescription)) {
                    mueble = new Mueble();
                    mueble.setName(etName.getText().toString().trim());
                    mueble.setDescription(etDescription.getText().toString().trim());
                    mueble.setPrice(Float.parseFloat(etPrice.getText().toString().trim()));
                    mueble.setQuantity(Integer.parseInt(etQuantity.getText().toString().trim()));
                    mueble.setPhotoURL(URL);
                    mPresenter.addMueble(mueble);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enableUIElements() {
       final boolean bo=true;
        etName.setEnabled(bo);
        etDescription.setEnabled(bo);
        etPrice.setEnabled(bo);
        etQuantity.setEnabled(bo);
        imgFoto.setEnabled(bo);
        imgDeleteFoto.setEnabled(bo);
        imgFromUrl.setEnabled(bo);
        imgFromGallery.setEnabled(bo);
    }

    @Override
    public void disableUIElements() {
       final boolean bo=false;
        etName.setEnabled(bo);
        etDescription.setEnabled(bo);
        etPrice.setEnabled(bo);
        etQuantity.setEnabled(bo);
        imgFoto.setEnabled(bo);
        imgDeleteFoto.setEnabled(bo);
        imgFromUrl.setEnabled(bo);
        imgFromGallery.setEnabled(bo);
    }

    @Override
    public void showProgress() {
        pbContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbContent.setVisibility(View.GONE);
    }


    @Override
    public void muebleAdded() {
        Toast.makeText(this, R.string.addMueble_message_successfully, Toast.LENGTH_SHORT).show();
        finishAfterTransition();
    }

    @Override
    public void showError(int resMsg) {
        Toast.makeText(this, resMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void maxValueError(int resMsg) {
        Toast.makeText(this, resMsg, Toast.LENGTH_SHORT).show();
    }
}