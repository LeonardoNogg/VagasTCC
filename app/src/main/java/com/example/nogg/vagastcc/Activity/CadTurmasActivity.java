package com.example.nogg.vagastcc.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Cursos;
import com.example.nogg.vagastcc.Entidades.Turmas;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadTurmasActivity extends AppCompatActivity {

    private Button btnGravar;
    private EditText edtNumero;
    private Spinner spnCursos;
    private Turmas turmas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_turmas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        turmas = new Turmas();

        edtNumero = (EditText) findViewById(R.id.edtNumero);
        spnCursos = findViewById(R.id.spnCursos);
        btnGravar = (Button) findViewById(R.id.btnGravar);

        firebase = ConfiguracaoFirebase.getFirebase().child("cursos");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> cursos = new ArrayList<String>();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Cursos cursoNovo = dados.getValue(Cursos.class);
                    cursos.add(cursoNovo.getNome());
                }
                Spinner cursoSpinner = findViewById(R.id.spnCursos);
                ArrayAdapter<String> cursosAdapter = new ArrayAdapter<String>(CadTurmasActivity.this, R.layout.spinner_item, cursos);
                cursosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cursoSpinner.setAdapter(cursosAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtNumero.getText() != null) {
                    turmas.setNumero(edtNumero.getText().toString());
                    turmas.setCurso(spnCursos.getSelectedItem().toString());
                }else{
                    Toast.makeText(CadTurmasActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }

                salvarTurma(turmas);

            }
        });

    }

    private boolean salvarTurma(Turmas turmas) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("turmas");
            firebase.child(turmas.getNumero()).setValue(turmas);
            Toast.makeText(CadTurmasActivity.this, "Turma cadastrada com sucesso", Toast.LENGTH_LONG).show();
            edtNumero.setText("");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
