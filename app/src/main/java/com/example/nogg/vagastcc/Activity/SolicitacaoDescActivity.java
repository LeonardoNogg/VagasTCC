package com.example.nogg.vagastcc.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Solicitacoes;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SolicitacaoDescActivity extends AppCompatActivity {

    private DatabaseReference firebaseS, firebaseA, firebaseV, firebase;
    private ValueEventListener valueEventListenerS, valueEventListenerA, valueEventListenerV;
    private TextView tvEmpresa, tvTipo, tvSetor, tvLocal, tvHorario, tvRequisitos, tvBeneficios, tvAtividades, tvSalario, tvNome, tvMatricula, tvEmail, tvTelefone, tvSexo, tvDataNasc, tvCursoV, tvCurso, tvTurma;
    private String id, idAluno, idVaga;
    private Button btnExcluir;
    private Solicitacoes soliExcluir;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitacao_desc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvEmpresa = findViewById(R.id.tvEmpresa);
        tvTipo = findViewById(R.id.tvTipo);
        tvSetor = findViewById(R.id.tvSetor);
        tvLocal = findViewById(R.id.tvLocal);
        tvHorario = findViewById(R.id.tvHorario);
        tvRequisitos = findViewById(R.id.tvRequisitos);
        tvBeneficios = findViewById(R.id.tvBeneficios);
        tvAtividades = findViewById(R.id.tvAtividades);
        tvSalario= findViewById(R.id.tvSalario);
        tvNome = findViewById(R.id.tvNome);
        tvMatricula = findViewById(R.id.tvMatricula);
        tvTelefone = findViewById(R.id.tvTelefone);
        tvSexo = findViewById(R.id.tvSexo);
        tvEmail = findViewById(R.id.tvEmail);
        tvDataNasc = findViewById(R.id.tvDataNasc);
        tvCurso = findViewById(R.id.tvCurso);
        tvTurma = findViewById(R.id.tvTurma);
        tvCursoV = findViewById(R.id.tvCursoV);
        btnExcluir = findViewById(R.id.btnExcluir);

        Intent intent4 = getIntent();
        Bundle bundle = intent4.getExtras();
        id = bundle.getString("id");

        firebaseS = ConfiguracaoFirebase.getFirebase().child("solicitacoes");
        valueEventListenerS = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Solicitacoes soliNova = dados.getValue(Solicitacoes.class);
                    if (soliNova.getId().equals(id)){
                        idAluno = soliNova.getId_aluno();
                        idVaga = soliNova.getId_vaga();
                        firebaseA.addValueEventListener(valueEventListenerA);
                        firebaseV.addValueEventListener(valueEventListenerV);
                        soliExcluir = soliNova;
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
                    Usuarios alunoNovo = dados.getValue(Usuarios.class);
                    if (idAluno.equals(alunoNovo.getId())){
                        tvNome.setText(alunoNovo.getNome());
                        tvMatricula.setText(alunoNovo.getMatricula());
                        tvSexo.setText(alunoNovo.getSexo());
                        tvTelefone.setText(alunoNovo.getTelefone());
                        tvEmail.setText(alunoNovo.getEmail());
                        tvDataNasc.setText(alunoNovo.getData_nasc());
                        tvCurso.setText(alunoNovo.getCurso());
                        tvTurma.setText(alunoNovo.getTurma());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebaseV = ConfiguracaoFirebase.getFirebase().child("vagas");
        valueEventListenerV = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Vagas vagaNova = dados.getValue(Vagas.class);
                    if (idVaga.equals(vagaNova.getId())){
                        tvEmpresa.setText(vagaNova.getEmpresa());
                        tvTipo.setText(vagaNova.getTipo());
                        tvSetor.setText(vagaNova.getSetor());
                        tvLocal.setText(vagaNova.getLocal());
                        tvHorario.setText(vagaNova.getHorario());
                        tvRequisitos.setText(vagaNova.getRequisitos());
                        tvBeneficios.setText(vagaNova.getBeneficios());
                        tvAtividades.setText(vagaNova.getAtividades());
                        tvSalario.setText(vagaNova.getSalario());
                        tvCursoV.setText(vagaNova.getCurso());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SolicitacaoDescActivity.this);
                builder.setTitle("Confirmar exclusão da vaga?");
                builder.setMessage("Você deseja excluir a solicitação de vaga na empresa " + soliExcluir.getEmpresa_vaga() + ", do aluno " + soliExcluir.getNome_aluno() + "?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        firebase = ConfiguracaoFirebase.getFirebase().child("solicitacoes");
                        firebase.child(soliExcluir.getId()).removeValue();
                        Toast.makeText(SolicitacaoDescActivity.this, "Exclusão efetuada!", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alerta = builder.create();
                alerta.show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseS.removeEventListener(valueEventListenerS);
        firebaseA.removeEventListener(valueEventListenerA);
        firebaseV.removeEventListener(valueEventListenerV);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseS.addValueEventListener(valueEventListenerS);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
