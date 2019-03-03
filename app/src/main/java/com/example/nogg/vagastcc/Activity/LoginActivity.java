package com.example.nogg.vagastcc.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.Fragments.PerfilFragment;
import com.example.nogg.vagastcc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private TextView tvCadastrar;
    private Button btnLogar;
    private FirebaseAuth autenticacao;
    private Usuarios usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        tvCadastrar = (TextView) findViewById(R.id.tvCadastrar);
        btnLogar = (Button) findViewById(R.id.btnLogar);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {

                    usuario = new Usuarios();
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());

                    validarLogin();

                } else {
                    Toast.makeText(LoginActivity.this, "Preencha os campos de e-mail e senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroUsuario();
            }
        });

    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                    abrirVerificacao();
                } else {
                    Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirCadastroUsuario() {
        Intent intent = new Intent(LoginActivity.this, CadAlunosActivity.class);
        startActivity(intent);
    }

    public void abrirVerificacao() {
        Intent intent = new Intent(LoginActivity.this, VerificacaoActivity.class);
        startActivity(intent);
    }
}
