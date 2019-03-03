package com.example.nogg.vagastcc.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                verificarUsuario();
            }
        }, 2000);
    }

    private void verificarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent2);
        finish();
        /*if (autenticacao.getCurrentUser() != null) {
            Intent intent = new Intent(SplashActivity.this, VerificacaoActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
    }
}
