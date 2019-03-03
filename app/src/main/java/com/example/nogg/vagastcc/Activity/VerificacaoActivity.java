package com.example.nogg.vagastcc.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class VerificacaoActivity extends AppCompatActivity {

    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacao);

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuarios usuarioNovo = dados.getValue(Usuarios.class);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user.getEmail().equals(usuarioNovo.getEmail())){
                        if (usuarioNovo.getPrivilegio().equals("aluno")){
                            abrirVagas();
                        }else{
                            abrirVagasAdmin();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    public void abrirVagas() {
        Intent intent = new Intent(VerificacaoActivity.this, VagasActivity.class);
        startActivity(intent);
    }

    public void abrirVagasAdmin() {
        Intent intent = new Intent(VerificacaoActivity.this, VagasAdminActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }
}
