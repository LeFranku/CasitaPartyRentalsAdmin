package com.example.casitapartyrentalsadmin.mainModule.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.casitapartyrentalsadmin.R;
import com.example.casitapartyrentalsadmin.addModule.view.AddMuebleActivity;
import com.example.casitapartyrentalsadmin.common.pojo.Mueble;
import com.example.casitapartyrentalsadmin.detailModule.view.DetailActivity;
import com.example.casitapartyrentalsadmin.mainModule.MainPresenter;
import com.example.casitapartyrentalsadmin.mainModule.MainPresenterClass;
import com.example.casitapartyrentalsadmin.mainModule.view.adapters.MuebleAdapter;
import com.example.casitapartyrentalsadmin.mainModule.view.adapters.OnItemClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, MainView {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.pbContent)
    ProgressBar pbContent;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.contentMain)
    CoordinatorLayout contentMain;

    private MainPresenter mPresenter;
    private MuebleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mueble);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        configToolbar();
        configAdapter();
        configRecyclerView();
        mPresenter = new MainPresenterClass(this);
        mPresenter.onCreate();
    }

    private void configToolbar() {
        setSupportActionBar(toolbar);
    }

    private void configAdapter() {
        mAdapter = new MuebleAdapter(new ArrayList<Mueble>(), this);
    }

    private void configRecyclerView() {
        /*
         * Para hacer un grid.
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,
         getResources().getInteger(R.integer.main_columns));
        recyclerView.setLayoutManager(linearLayoutManager);*/
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onAddClicked() {
        Intent intent = new Intent(MainActivity.this, AddMuebleActivity.class);
        startActivityForResult(intent, 1,
                ActivityOptions.makeSceneTransitionAnimation(
                       this ).toBundle());
    }

    /**
     * MainView
     */

    @Override
    public void showProgress() {
        pbContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbContent.setVisibility(View.GONE);
    }

    @Override
    public void add(Mueble mueble) {
    mAdapter.add(mueble);
    }

    @Override
    public void update(Mueble mueble) {
    mAdapter.update(mueble);
    }

    @Override
    public void remove(Mueble mueble) {
    mAdapter.remove(mueble);
    }

    @Override
    public void removeFail() {
        Snackbar.make(contentMain,R.string.main_error_remove,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void OnShowError(int resMsg) {
        Snackbar.make(contentMain,resMsg,Snackbar.LENGTH_LONG).show();
    }

    /**
     * OnItemClickListener
     */

    @Override
    public void OnItemClick(Mueble mueble) {
        Intent intent= new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Mueble.ID,mueble.getId());
        intent.putExtra(Mueble.NAME,mueble.getName());
        intent.putExtra(Mueble.DESCRIPTION,mueble.getDescription());
        intent.putExtra(Mueble.PHOTO_URL,mueble.getPhotoURL());
        intent.putExtra(Mueble.PRICE,mueble.getPrice());
        intent.putExtra(Mueble.QUANTITY,mueble.getQuantity());
        startActivityForResult(intent,1,
                ActivityOptions.makeSceneTransitionAnimation(
                        this ).toBundle());
    }

    @Override
    public void onLongItemClick(final Mueble mueble) {
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator!=null){
            vibrator.vibrate(60);
        }
        MaterialAlertDialogBuilder builder =new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.main_dialog_remove_title)
                .setMessage(R.string.main_dialog_remove_message)
                .setPositiveButton(R.string.main_dialog_ok, (dialogInterface, i) -> mPresenter.remove(mueble))
                .setNegativeButton(R.string.common_dialog_cancel, null);
        builder.show();
    }
}
