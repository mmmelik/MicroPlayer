package app.microplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.io.File;

import app.microplayer.fragment.DirFragment;
import app.microplayer.fragment.FileFragment;

public class MainActivity extends AppCompatActivity {
    private FrameLayout container;
    private DirFragment dirFragment;
    private boolean canGoBack=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        container=findViewById(R.id.fragment_container);
        dirFragment=new DirFragment();
        switchPage(dirFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            switchPage(dirFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchPage(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment).commit();

        canGoBack=fragment instanceof FileFragment;
        getSupportActionBar().setDisplayHomeAsUpEnabled(fragment instanceof FileFragment);
    }

    @Override
    public void onBackPressed() {
        if (canGoBack){
            switchPage(dirFragment);
        }else {
            super.onBackPressed();
        }
    }

    public void changeTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}