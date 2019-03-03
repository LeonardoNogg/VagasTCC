package com.example.nogg.vagastcc.Activity;

import android.support.annotation.NonNull;
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
import com.example.nogg.vagastcc.Entidades.Usuarios;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadAdminActivity extends AppCompatActivity {

    private EditText edtCadEmail;
    private EditText edtCadNome;
    private EditText edtCadSenha;
    private EditText edtCadConfirmaSenha;
    private Button btnGravar;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtCadEmail = (EditText) findViewById(R.id.edtEmail);
        edtCadNome = (EditText) findViewById(R.id.edtNome);
        edtCadSenha = (EditText) findViewById(R.id.edtSenha);
        edtCadConfirmaSenha = (EditText) findViewById(R.id.edtConfirmarSenha);
        btnGravar = (Button) findViewById(R.id.btnGravar);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCadEmail.getText() != null || edtCadNome.getText() != null || edtCadSenha.getText() != null || edtCadConfirmaSenha.getText() != null) {
                    if (edtCadSenha.getText().toString().equals(edtCadConfirmaSenha.getText().toString())) {
                        usuarios = new Usuarios();
                        usuarios.setNome(edtCadNome.getText().toString());
                        usuarios.setEmail(edtCadEmail.getText().toString());
                        usuarios.setSenha(edtCadSenha.getText().toString());
                        usuarios.setMatricula(null);
                        usuarios.setData_nasc(null);
                        usuarios.setTelefone(null);
                        usuarios.setSexo(null);
                        usuarios.setPrivilegio("admin");

                        cadastrarUsuario();

                    } else {
                        Toast.makeText(CadAdminActivity.this, "As senhas não são correspondentes", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(CadAdminActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void cadastrarUsuario() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(CadAdminActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadAdminActivity.this, "Administrador cadastrado com sucesso", Toast.LENGTH_LONG).show();

                    String idenficadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(idenficadorUsuario);
                    usuarios.salvar();

                    PreferenciasAndroid preferenciasAndroid = new PreferenciasAndroid(CadAdminActivity.this);
                    preferenciasAndroid.salvarUsuarioPrefencias(idenficadorUsuario, usuarios.getNome());

                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastrado no sistema";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadAdminActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
