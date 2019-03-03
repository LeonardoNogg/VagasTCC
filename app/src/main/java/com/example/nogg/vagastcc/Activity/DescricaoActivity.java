package com.example.nogg.vagastcc.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Solicitacoes;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DescricaoActivity extends AppCompatActivity {

    private DatabaseReference firebaseV, firebaseA, firebaseS;
    private ValueEventListener valueEventListenerV, valueEventListenerA;
    private TextView tvEmpresa, tvTipo, tvSetor, tvLocal, tvHorario, tvRequisitos, tvBeneficios, tvAtividades, tvSalario;
    private Button btnEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String id, id_aluno, nome_aluno, email_aluno;
    private Solicitacoes solicitacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth =  FirebaseAuth.getInstance();
        user  = firebaseAuth.getCurrentUser();

        tvEmpresa = findViewById(R.id.tvEmpresa);
        tvTipo = findViewById(R.id.tvTipo);
        tvSetor = findViewById(R.id.tvSetor);
        tvLocal = findViewById(R.id.tvLocal);
        tvHorario = findViewById(R.id.tvHorario);
        tvRequisitos = findViewById(R.id.tvRequisitos);
        tvBeneficios = findViewById(R.id.tvBeneficios);
        tvAtividades = findViewById(R.id.tvAtividades);
        tvSalario= findViewById(R.id.tvSalario);
        btnEmail = findViewById(R.id.btnEmail);

        Intent intent2 = getIntent();
        Bundle bundle = intent2.getExtras();
        id = bundle.getString("id");

        firebaseV = ConfiguracaoFirebase.getFirebase().child("vagas");
        valueEventListenerV = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Vagas vagaNova = dados.getValue(Vagas.class);
                    if (vagaNova.getId().equals(id)){
                        tvEmpresa.setText(vagaNova.getEmpresa());
                        tvTipo.setText(vagaNova.getTipo());
                        tvSetor.setText(vagaNova.getSetor());
                        tvLocal.setText(vagaNova.getLocal());
                        tvHorario.setText(vagaNova.getHorario());
                        tvRequisitos.setText(vagaNova.getRequisitos());
                        tvBeneficios.setText(vagaNova.getBeneficios());
                        tvAtividades.setText(vagaNova.getAtividades());
                        tvSalario.setText(vagaNova.getSalario());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebaseA = ConfiguracaoFirebase.getFirebase().child("usuarios");
        valueEventListenerA = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuarios usuarioNovo = dados.getValue(Usuarios.class);
                    if (usuarioNovo.getEmail().equals(user.getEmail())){
                        id_aluno = usuarioNovo.getId();
                        nome_aluno = usuarioNovo.getNome();
                        email_aluno = usuarioNovo.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarSolicitacao();
            }
        });

    }

    private void enviarSolicitacao(){

        solicitacao = new Solicitacoes();
        solicitacao.setId_aluno(id_aluno);
        solicitacao.setNome_aluno(nome_aluno);
        solicitacao.setEmail_aluno(email_aluno);
        solicitacao.setId_vaga(id);
        solicitacao.setEmpresa_vaga(tvEmpresa.getText().toString());
        solicitacao.setTipo_vaga(tvTipo.getText().toString());
        solicitacao.setId(solicitacao.getId_aluno() + solicitacao.getId_vaga());
        salvarSolicitacao(solicitacao);

    }

    private boolean salvarSolicitacao(Solicitacoes solicitacao) {
        try {
            firebaseS = ConfiguracaoFirebase.getFirebase().child("solicitacoes");
            firebaseS.child(solicitacao.getId()).setValue(solicitacao);
            Toast.makeText(DescricaoActivity.this, "Solicitação de vaga enviada com sucesso", Toast.LENGTH_LONG).show();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseV.removeEventListener(valueEventListenerV);
        firebaseA.removeEventListener(valueEventListenerA);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseV.addValueEventListener(valueEventListenerV);
        firebaseA.addValueEventListener(valueEventListenerA);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
