package bowo.skripsi.mobileshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import bowo.skripsi.mobileshop.fragmen.BookmarkFrag;
import bowo.skripsi.mobileshop.fragmen.HistoryFrag;
import bowo.skripsi.mobileshop.fragmen.HomeFrag;
import bowo.skripsi.mobileshop.fragmen.ProfileFrag;

public class HomeAct extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadFragment(new HomeFrag());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.beranda:
                fragment = new HomeFrag();
                break;
            case R.id.bookmark:
                fragment = new BookmarkFrag();
                break;
            case R.id.historyPembelian:
                fragment = new HistoryFrag();
                break;
            case R.id.profil:
                SharedPreferences sharedpreferences;
                Boolean session = false;
                sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                session = sharedpreferences.getBoolean(session_status, false);
                if (session) {

                    fragment = new ProfileFrag();
                }else {
                    Intent intent = new Intent(HomeAct.this, LoginAct.class);
                    startActivity(intent);
                }

                break;
        }
        return loadFragment(fragment);
    }
}
