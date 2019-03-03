package com.example.nogg.vagastcc.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Cursos;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.Helper.Base64Custom;
import com.example.nogg.vagastcc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CadVagasActivity extends AppCompatActivity {

    private Button btnGravar;
    private EditText edtEmpresa, edtSetor, edtLocal, edtHorario, edtRequisitos, edtSalario, edtBeneficios, edtAtividades;
    private RadioButton rbEmprego;
    private String id;
    private Spinner spnCursos;
    private Vagas vagas;
    private DatabaseReference firebase;
    private DatabaseReference firebaseC;
    private ValueEventListener valueEventListenerCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_vagas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rbEmprego = (RadioButton) findViewById(R.id.rbEmprego);
        edtEmpresa = (EditText) findViewById(R.id.edtEmpresa);
        edtSetor = (EditText) findViewById(R.id.edtSetor);
        edtLocal = (EditText) findViewById(R.id.edtLocal);
        edtHorario = (EditText) findViewById(R.id.edtHorario);
        edtRequisitos = (EditText) findViewById(R.id.edtRequisitos);
        edtSalario = (EditText) findViewById(R.id.edtSalario);
        edtBeneficios = (EditText) findViewById(R.id.edtBeneficios);
        edtAtividades = (EditText) findViewById(R.id.edtAtividades);
        btnGravar = (Button) findViewById(R.id.btnGravar);
        spnCursos = findViewById(R.id.spnCursos);

        firebaseC = ConfiguracaoFirebase.getFirebase().child("cursos");
        valueEventListenerCursos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> cursos = new ArrayList<String>();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Cursos cursoNovo = dados.getValue(Cursos.class);
                    cursos.add(cursoNovo.getNome());
                }
                Spinner cursoSpinner = findViewById(R.id.spnCursos);
                ArrayAdapter<String> cursosAdapter = new ArrayAdapter<String>(CadVagasActivity.this, R.layout.spinner_item, cursos);
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

                if (edtEmpresa.getText() != null || edtSetor.getText() != null || edtLocal.getText() != null || edtHorario.getText() != null || edtRequisitos.getText() != null ||
                        edtAtividades.getText() != null || edtBeneficios.getText() != null || edtSalario.getText() != null) {
                    vagas = new Vagas();
                    if (rbEmprego.isChecked()) {
                        vagas.setTipo("Emprego");
                    } else {
                        vagas.setTipo("Estágio");
                    }
                    id = vagas.getTipo() + edtEmpresa.getText().toString() + edtSetor.getText().toString() + getRandomInt(1, 10000);
                    String identificador = Base64Custom.codificarBase64(id);
                    vagas.setId(identificador);
                    vagas.setEmpresa(edtEmpresa.getText().toString());
                    vagas.setSetor(edtSetor.getText().toString());
                    vagas.setLocal(edtLocal.getText().toString());
                    vagas.setHorario(edtHorario.getText().toString());
                    vagas.setRequisitos(edtRequisitos.getText().toString());
                    vagas.setSalario(edtSalario.getText().toString());
                    vagas.setBeneficios(edtBeneficios.getText().toString());
                    vagas.setAtividades(edtAtividades.getText().toString());
                    vagas.setCurso(spnCursos.getSelectedItem().toString());

                    salvarVaga(vagas);
                }else{
                    Toast.makeText(CadVagasActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private boolean salvarVaga(Vagas vagas) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("vagas");
            firebase.child(vagas.getId()).setValue(vagas);
            Toast.makeText(CadVagasActivity.this, "Vaga cadastrada com sucesso", Toast.LENGTH_LONG).show();
            edtEmpresa.setText("");
            edtSetor.setText("");
            edtLocal.setText("");
            edtHorario.setText("");
            edtRequisitos.setText("");
            edtSalario.setText("");
            edtBeneficios.setText("");
            edtAtividades.setText("");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static int getRandomInt(int min, int max) {
        Random random = new Random();

        return random.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseC.removeEventListener(valueEventListenerCursos);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseC.addValueEventListener(valueEventListenerCursos);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
