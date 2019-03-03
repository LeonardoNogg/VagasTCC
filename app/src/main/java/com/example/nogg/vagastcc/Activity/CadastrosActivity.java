package com.example.nogg.vagastcc.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.nogg.vagastcc.R;

public class CadastrosActivity extends AppCompatActivity {

    Button btnVagas, btnTurmas, btnCursos, btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastros);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnVagas = findViewById(R.id.btnVagas);
        btnCursos = findViewById(R.id.btnCursos);
        btnTurmas = findViewById(R.id.btnTurmas);
        btnAdmin = findViewById(R.id.btnAdmin);

        btnVagas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastrosActivity.this, CadVagasActivity.class);
                startActivity(intent);
            }
        });

        btnCursos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastrosActivity.this, CadCursosActivity.class);
                startActivity(intent);
            }
        });

        btnTurmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastrosActivity.this, CadTurmasActivity.class);
                startActivity(intent);
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastrosActivity.this, CadAdminActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
