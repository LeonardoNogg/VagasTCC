package com.example.nogg.vagastcc.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.example.nogg.vagastcc.Entidades.Turmas;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.Helper.Base64Custom;
import com.example.nogg.vagastcc.Helper.PreferenciasAndroid;
import com.example.nogg.vagastcc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadAlunosActivity extends AppCompatActivity {

    private EditText edtEmail, edtNome, edtMatricula, edtSenha, edtConfirmaSenha, edtDataNasc, edtTelefone;
    private Spinner spnCursos, spnTurmas;
    private RadioButton rbFeminino;
    private Button btnGravar;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseC, firebaseT;
    private ValueEventListener valueEventListenerCursos, valueEventListenerTurmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_alunos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtMatricula = (EditText) findViewById(R.id.edtMatricula);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        edtConfirmaSenha = (EditText) findViewById(R.id.edtConfirmarSenha);
        edtDataNasc = (EditText) findViewById(R.id.edtDataNasc);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        rbFeminino = (RadioButton) findViewById(R.id.rbFeminino);
        btnGravar = (Button) findViewById(R.id.btnGravar);
        spnCursos = findViewById(R.id.spnCursos);
        spnTurmas = findViewById(R.id.spnTurmas);

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
                ArrayAdapter<String> cursosAdapter = new ArrayAdapter<String>(CadAlunosActivity.this, R.layout.spinner_item, cursos);
                cursosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cursoSpinner.setAdapter(cursosAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebaseT = ConfiguracaoFirebase.getFirebase().child("turmas");
        valueEventListenerTurmas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> turmas = new ArrayList<String>();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Turmas turmaNova = dados.getValue(Turmas.class);
                    turmas.add(turmaNova.getNumero());
                }
                Spinner turmaSpinner = findViewById(R.id.spnTurmas);
                ArrayAdapter<String> turmasAdapter = new ArrayAdapter<String>(CadAlunosActivity.this, R.layout.spinner_item, turmas);
                turmasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                turmaSpinner.setAdapter(turmasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText() != null || edtNome.getText() != null || edtDataNasc.getText() != null || edtMatricula.getText() != null || edtConfirmaSenha.getText() != null || edtSenha.getText() != null || edtTelefone.getText() != null) {
                    if (edtSenha.getText().toString().equals(edtConfirmaSenha.getText().toString())) {
                        usuarios = new Usuarios();
                        usuarios.setNome(edtNome.getText().toString());
                        usuarios.setEmail(edtEmail.getText().toString());
                        usuarios.setSenha(edtSenha.getText().toString());
                        usuarios.setMatricula(edtMatricula.getText().toString());
                        usuarios.setData_nasc(edtDataNasc.getText().toString());
                        usuarios.setTelefone(edtTelefone.getText().toString());
                        usuarios.setCurso(spnCursos.getSelectedItem().toString());
                        usuarios.setTurma(spnTurmas.getSelectedItem().toString());
                        usuarios.setPrivilegio("aluno");
                        if (rbFeminino.isChecked()) {
                            usuarios.setSexo("Feminino");
                        } else {
                            usuarios.setSexo("Masculino");
                        }

                        cadastrarUsuario();

                    } else {
                        Toast.makeText(CadAlunosActivity.this, "As senhas não são correspondentes", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(CadAlunosActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void cadastrarUsuario() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(CadAlunosActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadAlunosActivity.this, "Aluno cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                    String idenficadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    usuarios.setId(idenficadorUsuario);
                    usuarios.salvar();

                    PreferenciasAndroid preferenciasAndroid = new PreferenciasAndroid(CadAlunosActivity.this);
                    preferenciasAndroid.salvarUsuarioPrefencias(idenficadorUsuario, usuarios.getNome());

                    abrirLoginUsuario();
                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = " Digite uma senha mais forte, contendo no mínimo 8 caracteres de letras e números";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = " O e-mail digitado é inválido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastrado no sistema";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadAlunosActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirLoginUsuario() {
        Intent intent = new Intent(CadAlunosActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseC.removeEventListener(valueEventListenerCursos);
        firebaseT.removeEventListener(valueEventListenerTurmas);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseC.addValueEventListener(valueEventListenerCursos);
        firebaseT.addValueEventListener(valueEventListenerTurmas);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
