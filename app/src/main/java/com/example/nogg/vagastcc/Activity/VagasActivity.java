package com.example.nogg.vagastcc.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.nogg.vagastcc.Fragments.PerfilFragment;
import com.example.nogg.vagastcc.Fragments.VagasFragment;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;

public class VagasActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private VagasFragment vagasFragment;
    private PerfilFragment perfilFragment;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vagas);

        mainNav = findViewById(R.id.main_nav);
        vagasFragment = new VagasFragment();
        perfilFragment = new PerfilFragment();

        setFragment(vagasFragment);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_vagas:
                        setFragment(vagasFragment);
                        return true;

                    case R.id.nav_perfil:
                        setFragment(perfilFragment);
                        return true;

                    default:
                        return false;
                }
            }

        });

    }

    private void setFragment(android.support.v4.app.Fragment fragment) {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }
}
