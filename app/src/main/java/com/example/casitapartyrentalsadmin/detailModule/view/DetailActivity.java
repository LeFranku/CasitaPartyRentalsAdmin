package com.example.casitapartyrentalsadmin.detailModule.view;

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
import com.example.casitapartyrentalsadmin.addModule.view.AddMuebleActivity;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.common.utils.CommonUtils;
import com.example.casitapartyrentalsadmin.detailModule.DetailMueblePresenter;
import com.example.casitapartyrentalsadmin.detailModule.DetailMueblePresenterClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements DetailMuebleView{

    private DetailMueblePresenter mPresenter;

    private static final int RC_GALLERY = 21;
    private static final String IMAGE_DIRECTORY = "/Images";
    private static final String IMAGE_MUEBLES = "/muebles";
    private static String PHOTO = "/photo";
    private StorageReference mStorage;

    Mueble mMueble;
    Bundle extra;
    String URL = null;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etName)
    TextInputEditText etName;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.imgFoto)
    AppCompatImageView imgFoto;
    @BindView(R.id.imgDeleteFoto)
    AppCompatImageView imgDeleteFoto;
    @BindView(R.id.imgFromGallery)
    AppCompatImageView imgFromGallery;
    @BindView(R.id.imgFromUrl)
    AppCompatImageView imgFromUrl;
    @BindView(R.id.etPrice)
    TextInputEditText etPrice;
    @BindView(R.id.tilPrice)
    TextInputLayout tilPrice;
    @BindView(R.id.etQuantity)
    TextInputEditText etQuantity;
    @BindView(R.id.tilQuantity)
    TextInputLayout tilQuantity;
    @BindView(R.id.etDescription)
    TextInputEditText etDescription;
    @BindView(R.id.tilDescription)
    TextInputLayout tilDescription;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.pbContent)
    ProgressBar pbContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        extra = getIntent().getExtras();
        if (extra != null) {
            mMueble = new Mueble();
            configMueble(extra);
            configActionBar();
            configValues();
        }
        mPresenter= new DetailMueblePresenterClass(this);
        mPresenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                super.onBackPressed();
                break;
            case R.id.action_save:
                saveOrEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                            uploadImageStorage(DetailActivity.this,mPhotoSelectedUri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
    public void uploadImageStorage(Context context, Uri mPhotoSelectedUri) {
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
                                configPhoto(URL);
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


    @OnClick(R.id.fab)
    public void saveOrEdit() {
        if (CommonUtils.validateMueble(this,etName,etPrice,etQuantity,etDescription,URL
                ,tilName,tilPrice,tilQuantity,tilDescription)) {
            mMueble.setName(etName.getText().toString().trim());
            mMueble.setPrice(Float.parseFloat(etPrice.getText().toString().trim()));
            mMueble.setQuantity(Integer.parseInt(etQuantity.getText().toString().trim()));
            mMueble.setDescription(etDescription.getText().toString().trim());
            mMueble.setPhotoURL(URL);
            mPresenter.updateMueble(mMueble);
        }
    }

    private void configValues() {
        configPhoto(mMueble.getPhotoURL());
        etName.setText(mMueble.getName());
        etPrice.setText(String.valueOf(mMueble.getPrice()));
        etQuantity.setText(String.valueOf(mMueble.getQuantity()));
        etDescription.setText(mMueble.getDescription());
    }

    private void configActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mMueble.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configMueble(Bundle extra) {
        mMueble.setId(extra.getString(Mueble.ID));
        mMueble.setName(extra.getString(Mueble.NAME));
        mMueble.setPhotoURL(extra.getString(Mueble.PHOTO_URL));
        mMueble.setQuantity(extra.getInt(Mueble.QUANTITY));
        mMueble.setPrice(extra.getFloat(Mueble.PRICE));
        mMueble.setDescription(extra.getString(Mueble.DESCRIPTION));
        URL = mMueble.getPhotoURL();
    }

    @OnClick({R.id.imgDeleteFoto, R.id.imgFromGallery, R.id.imgFromUrl})
    public void imageEvents(View view) {
        switch (view.getId()) {
            case R.id.imgDeleteFoto:
                if (URL != null) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.detalle_dialogDelete_title)
                            .setMessage(R.string.detalle_dialogDelete_message)
                            .setPositiveButton(R.string.label_dialog_delete, (dialogInterface, i) -> configPhoto(null))
                            .setNegativeButton(R.string.label_dialog_cancel, null);
                    builder.show();
                }
                break;
            case R.id.imgFromGallery:
                if (!etName.getText().toString().trim().isEmpty()) {
                    fromGallery();
                } else {
                    Toast.makeText(DetailActivity.this, R.string.addMueble_name_requied, Toast.LENGTH_SHORT).show();
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
        etFotoUrl.setText(URL);
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.addArtist_dialogUrl_title)
                .setPositiveButton(R.string.label_dialog_add, (dialogInterface, i) -> {
                    URL = etFotoUrl.getText().toString().trim();
                    configPhoto(URL);
                })
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.setView(etFotoUrl);
        builder.show();
    }

    private void configPhoto(String photoURL) {
        if (photoURL != null) {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop();
            Glide.with(this)
                    .load(photoURL)
                    .apply(options)
                    .into(imgFoto);
            Toast.makeText(this, "" + photoURL, Toast.LENGTH_SHORT).show();
        } else {
            imgFoto.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_photo_size_select_actual));
        }
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
    public void enableUIElements() {
        final boolean bo=true;
        etName.setEnabled(bo);
        etDescription.setEnabled(bo);
        etQuantity.setEnabled(bo);
        etPrice.setEnabled(bo);
        fab.setEnabled(bo);
        imgFoto.setEnabled(bo);
        imgDeleteFoto.setEnabled(bo);
        imgFromGallery.setEnabled(bo);
        imgFromUrl.setEnabled(bo);
    }

    @Override
    public void disableUIElements() {
        final boolean bo=false;
        etName.setEnabled(bo);
        etDescription.setEnabled(bo);
        etQuantity.setEnabled(bo);
        etPrice.setEnabled(bo);
        fab.setEnabled(bo);
        imgFoto.setEnabled(bo);
        imgDeleteFoto.setEnabled(bo);
        imgFromGallery.setEnabled(bo);
        imgFromUrl.setEnabled(bo);
    }

    @Override
    public void updateSuccess() {
        Toast.makeText(this, R.string.detailMueble_update_successfully, Toast.LENGTH_SHORT).show();
        finishAfterTransition();
    }

    @Override
    public void updateError() {
        Toast.makeText(this, R.string.detailMueble_update_error, Toast.LENGTH_SHORT).show();
    }
}
