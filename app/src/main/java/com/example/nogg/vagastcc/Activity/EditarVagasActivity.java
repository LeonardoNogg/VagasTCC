package com.example.nogg.vagastcc.Activity;

import android.content.Intent;
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
import com.example.nogg.vagastcc.Entidades.Solicitacoes;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditarVagasActivity extends AppCompatActivity {

    private Button btnGravar;
    private EditText edtEmpresa, edtSetor, edtLocal, edtHorario, edtRequisitos, edtSalario, edtBeneficios, edtAtividades;
    private RadioButton rbEmprego, rbEstagio;
    private String id, curso, tipo;
    private Vagas vagas;
    private Spinner spnCursos;
    private DatabaseReference firebase, firebaseC, firebaseS;
    private ValueEventListener valueEventListener, valueEventListenerC, valueEventListenerS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_vagas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rbEmprego = (RadioButton) findViewById(R.id.rbEmprego);
        rbEstagio = (RadioButton) findViewById(R.id.rbEstagio);
        edtEmpresa = (EditText) findViewById(R.id.edtEmpresa);
        edtSetor = (EditText) findViewById(R.id.edtSetor);
        edtLocal = (EditText) findViewById(R.id.edtLocal);
        edtHorario = (EditText) findViewById(R.id.edtHorario);
        edtRequisitos = (EditText) findViewById(R.id.edtRequisitos);
        edtSalario = (EditText) findViewById(R.id.edtSalario);
        edtBeneficios = (EditText) findViewById(R.id.edtBeneficios);
        edtAtividades = (EditText) findViewById(R.id.edtAtividades);
        btnGravar = (Button) findViewById(R.id.btnEditar);
        spnCursos = findViewById(R.id.spnCursos);

        Intent intent3 = getIntent();
        Bundle bundle = intent3.getExtras();
        id = bundle.getString("id");

        firebase = ConfiguracaoFirebase.getFirebase().child("vagas");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Vagas vagaNova = dados.getValue(Vagas.class);
                    if (vagaNova.getId().equals(id)){
                        edtEmpresa.setText(vagaNova.getEmpresa());
                        edtSetor.setText(vagaNova.getSetor());
                        edtLocal.setText(vagaNova.getLocal());
                        edtHorario.setText(vagaNova.getHorario());
                        edtRequisitos.setText(vagaNova.getRequisitos());
                        edtSalario.setText(vagaNova.getSalario());
                        edtBeneficios.setText(vagaNova.getBeneficios());
                        edtAtividades.setText(vagaNova.getAtividades());
                        curso = vagaNova.getCurso();
                        if (vagaNova.getTipo().equals("Emprego")){
                            rbEmprego.setChecked(true);
                        }else{
                            rbEstagio.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        firebaseC = ConfiguracaoFirebase.getFirebase().child("cursos");
        valueEventListenerC = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> cursos = new ArrayList<String>();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Cursos cursoNovo = dados.getValue(Cursos.class);
                    cursos.add(cursoNovo.getNome());
                }
                Spinner cursoSpinner = findViewById(R.id.spnCursos);
                ArrayAdapter<String> cursosAdapter = new ArrayAdapter<String>(EditarVagasActivity.this, R.layout.spinner_item, cursos);
                cursosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cursoSpinner.setAdapter(cursosAdapter);
                spnCursos.setSelection(getSpinnerIndex(spnCursos, curso));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebaseS = ConfiguracaoFirebase.getFirebase().child("solicitacoes");
        valueEventListenerS = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Solicitacoes soliNova = dados.getValue(Solicitacoes.class);

                    if (soliNova.getId_vaga().equals(id)){
                        Solicitacoes solicitacoes = new Solicitacoes();
                        solicitacoes.setId(soliNova.getId());
                        solicitacoes.setId_aluno(soliNova.getId_aluno());
                        solicitacoes.setNome_aluno(soliNova.getNome_aluno());
                        solicitacoes.setEmail_aluno(soliNova.getEmail_aluno());
                        solicitacoes.setId_vaga(id);
                        solicitacoes.setEmpresa_vaga(edtEmpresa.getText().toString());
                        solicitacoes.setTipo_vaga(tipo);
                        firebaseS.child(soliNova.getId()).setValue(solicitacoes);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtSalario.getText() != null || edtBeneficios.getText() != null || edtAtividades.getText() != null || edtRequisitos.getText() != null ||
                        edtEmpresa.getText() != null || edtHorario.getText() != null || edtLocal.getText() != null || edtSetor.getText() != null) {

                    vagas = new Vagas();
                    if (rbEmprego.isChecked()) {
                        vagas.setTipo("Emprego");
                    } else {
                        vagas.setTipo("Estágio");
                    }
                    tipo = vagas.getTipo();
                    vagas.setId(id);
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
                    Toast.makeText(EditarVagasActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public static int getSpinnerIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equals(myString)){
                index = i;
            }
        }
        return index;
    }

    private boolean salvarVaga(Vagas vagas) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("vagas");
            firebase.child(vagas.getId()).setValue(vagas);
            Toast.makeText(EditarVagasActivity.this, "Vaga editada com sucesso", Toast.LENGTH_LONG).show();
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
        firebaseC.removeEventListener(valueEventListenerC);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
        firebaseC.addValueEventListener(valueEventListenerC);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
