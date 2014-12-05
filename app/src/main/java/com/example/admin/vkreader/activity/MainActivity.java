package com.example.admin.vkreader.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.admin.vkreader.R;
import com.example.admin.vkreader.fragments.MyListFragment;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements MyListFragment.onSomeEventListener {
    public final int ACTION_EDIT = 101;
    private final int IDM_ARROW = 100;
    public final String IDE_EXTRA = "param";
    public final String IDE_ARR = "arr";
    private Fragment fragment1;
    private Fragment fragment2;
    private Intent intent;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplication();
        Toast toast = Toast.makeText(context, "Нет соединения с интернетом!", Toast.LENGTH_LONG);
        if (!isOnline()) {
            toast.show();
            MainActivity.this.finish();
        }
        frameLayout = (FrameLayout) findViewById(R.id.frm);
        fragment1 = new MyListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frm, fragment1).commit();
        fragment2 = getSupportFragmentManager().findFragmentById(R.id.details_frag);
    }

    @Override
    public void someEvent(Integer position, ArrayList arr) {
        if (fragment2 == null) {
            intent = new Intent();
            intent.putExtra(IDE_EXTRA, position);
            intent.putExtra(IDE_ARR, arr);
            intent.setClass(this, TextActivity.class);
            startActivityForResult(intent, ACTION_EDIT);
        }
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo().isAvailable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (fragment2 != null) {
            menu.add(Menu.NONE, IDM_ARROW, Menu.NONE, "Back").setIcon(R.drawable.arrow).
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case IDM_ARROW:
                frameLayout.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment2 != null) {
            int visibility = frameLayout.getVisibility();
            outState.putInt("visibility", visibility);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (fragment2 != null) {
            int visibility = savedInstanceState.getInt("visibility");
            if (visibility == View.VISIBLE) frameLayout.setVisibility(View.VISIBLE);
            if (visibility == View.GONE) frameLayout.setVisibility(View.GONE);
        }
    }
}
