package com.example.nogg.vagastcc.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.nogg.vagastcc.Fragments.PerfilAdminFragment;
import com.example.nogg.vagastcc.Fragments.PerfilFragment;
import com.example.nogg.vagastcc.Fragments.SolicitacoesFragment;
import com.example.nogg.vagastcc.Fragments.VagasAdminFragment;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;

public class VagasAdminActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private VagasAdminFragment vagaFragment;
    private PerfilAdminFragment perfilFragment;
    private SolicitacoesFragment solicitacoesFragment;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vagas_admin);

        mainNav = findViewById(R.id.main_nav);
        vagaFragment = new VagasAdminFragment();
        perfilFragment = new PerfilAdminFragment();
        solicitacoesFragment = new SolicitacoesFragment();

        setFragment(vagaFragment);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_vagas:
                        setFragment(vagaFragment);
                        return true;

                    case R.id.nav_perfil:
                        setFragment(perfilFragment);
                        return true;

                    case R.id.nav_solicitacoes:
                        setFragment(solicitacoesFragment);
                        return true;

                    default:
                        return false;
                }
            }

        });

    }

    public void setFragment(android.support.v4.app.Fragment fragment) {

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }
}
