package com.example.nogg.vagastcc.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Cursos;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.database.DatabaseReference;

public class CadCursosActivity extends AppCompatActivity {

    private Button btnGravar;
    private EditText edtNome;
    private Cursos cursos;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_cursos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cursos = new Cursos();

        edtNome = (EditText) findViewById(R.id.edtCurso);
        btnGravar = (Button) findViewById(R.id.btnGravar);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtNome.getText() != null) {
                    cursos.setNome(edtNome.getText().toString());
                }else{
                    Toast.makeText(CadCursosActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }

                salvarCurso(cursos);

            }
        });

    }


    private boolean salvarCurso(Cursos cursos) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("cursos");
            firebase.child(cursos.getNome()).setValue(cursos);
            Toast.makeText(CadCursosActivity.this, "Curso cadastrado com sucesso", Toast.LENGTH_LONG).show();
            edtNome.setText("");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
