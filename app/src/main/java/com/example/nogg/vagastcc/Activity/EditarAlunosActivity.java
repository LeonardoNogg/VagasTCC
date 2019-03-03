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
import com.example.nogg.vagastcc.Entidades.Solicitacoes;
import com.example.nogg.vagastcc.Entidades.Turmas;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditarAlunosActivity extends AppCompatActivity {

    private EditText edtNome;
    private EditText edtMatricula, edtDataNasc, edtTelefone;
    private Spinner spnCursos, spnTurmas;
    private RadioButton rbFeminino, rbMasculino;
    private Button btnGravar;
    private Usuarios usuarios;
    private ValueEventListener valueEventListener, valueEventListenerC, valueEventListenerT, valueEventListenerS;
    private DatabaseReference firebase, firebaseC, firebaseT, firebaseS;
    private String id, email, senha, curso, turma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_alunos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtMatricula = (EditText) findViewById(R.id.edtMatricula);
        edtDataNasc = (EditText) findViewById(R.id.edtDataNasc);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        rbFeminino = (RadioButton) findViewById(R.id.rbFeminino);
        rbMasculino = findViewById(R.id.rbMasculino);
        btnGravar = findViewById(R.id.btnGravar);
        spnCursos = findViewById(R.id.spnCursos);
        spnTurmas = findViewById(R.id.spnTurmas);

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuarios usuarioNovo = dados.getValue(Usuarios.class);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user.getEmail().equals(usuarioNovo.getEmail())){
                        edtNome.setText(usuarioNovo.getNome());
                        edtMatricula.setText(usuarioNovo.getMatricula());
                        edtDataNasc.setText(usuarioNovo.getData_nasc());
                        edtTelefone.setText(usuarioNovo.getTelefone());
                        curso = usuarioNovo.getCurso();
                        turma = usuarioNovo.getTurma();
                        if (usuarioNovo.getSexo().equals("Masculino")){
                            rbMasculino.setChecked(true);
                        }else{
                            rbFeminino.setChecked(true);
                        }
                        id = usuarioNovo.getId();
                        email = usuarioNovo.getEmail();
                        senha = usuarioNovo.getSenha();
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
                ArrayAdapter<String> cursosAdapter = new ArrayAdapter<String>(EditarAlunosActivity.this, R.layout.spinner_item, cursos);
                cursosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cursoSpinner.setAdapter(cursosAdapter);
                spnCursos.setSelection(getSpinnerIndex(spnCursos, curso));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebaseT = ConfiguracaoFirebase.getFirebase().child("turmas");
        valueEventListenerT = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> turmas = new ArrayList<String>();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Turmas turmaNova = dados.getValue(Turmas.class);
                    turmas.add(turmaNova.getNumero());
                }
                Spinner turmaSpinner = findViewById(R.id.spnTurmas);
                ArrayAdapter<String> turmasAdapter = new ArrayAdapter<String>(EditarAlunosActivity.this, R.layout.spinner_item, turmas);
                turmasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                turmaSpinner.setAdapter(turmasAdapter);
                spnTurmas.setSelection(getSpinnerIndex(spnTurmas, turma));
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

                    String empresa = soliNova.getEmpresa_vaga();
                    String tipo = soliNova.getTipo_vaga();

                    if (soliNova.getId_aluno().equals(id)){
                        Solicitacoes solicitacoes = new Solicitacoes();
                        solicitacoes.setId(soliNova.getId());
                        solicitacoes.setId_aluno(id);
                        solicitacoes.setNome_aluno(edtNome.getText().toString());
                        solicitacoes.setEmail_aluno(email);
                        solicitacoes.setId_vaga(soliNova.getId_vaga());
                        solicitacoes.setEmpresa_vaga(empresa);
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
            public void onClick(View v) {

                if (edtNome.getText() != null || edtTelefone.getText() != null || edtMatricula.getText() != null || edtDataNasc.getText() != null) {

                    usuarios = new Usuarios();
                    usuarios.setNome(edtNome.getText().toString());
                    usuarios.setMatricula(edtMatricula.getText().toString());
                    usuarios.setData_nasc(edtDataNasc.getText().toString());
                    usuarios.setTelefone(edtTelefone.getText().toString());
                    usuarios.setCurso(spnCursos.getSelectedItem().toString());
                    usuarios.setTurma(spnTurmas.getSelectedItem().toString());
                    if (rbFeminino.isChecked()) {
                        usuarios.setSexo("Feminino");
                    } else {
                        usuarios.setSexo("Masculino");
                    }
                    usuarios.setPrivilegio("aluno");
                    usuarios.setId(id);
                    usuarios.setEmail(email);
                    usuarios.setSenha(senha);

                    cadastrarUsuario();

                }else{
                    Toast.makeText(EditarAlunosActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
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

    private void cadastrarUsuario() {
        usuarios.salvar();
        firebaseS.addValueEventListener(valueEventListenerS);
        Toast.makeText(EditarAlunosActivity.this, "Dados alterados com sucesso", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
        firebaseC.removeEventListener(valueEventListenerC);
        firebaseT.removeEventListener(valueEventListenerT);
        firebaseS.removeEventListener(valueEventListenerS);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
        firebaseC.addValueEventListener(valueEventListenerC);
        firebaseT.addValueEventListener(valueEventListenerT);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
