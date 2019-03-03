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
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DescricaoAdminActivity extends AppCompatActivity {

    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private TextView tvEmpresa, tvTipo, tvSetor, tvLocal, tvHorario, tvRequisitos, tvBeneficios, tvAtividades, tvSalario;
    private Button btnEditar, btnExcluir;
    private String id;
    private Vagas vagaExcluir;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descricao_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir);
        tvEmpresa = findViewById(R.id.tvEmpresa);
        tvTipo = findViewById(R.id.tvTipo);
        tvSetor = findViewById(R.id.tvSetor);
        tvLocal = findViewById(R.id.tvLocal);
        tvHorario = findViewById(R.id.tvHorario);
        tvRequisitos = findViewById(R.id.tvRequisitos);
        tvBeneficios = findViewById(R.id.tvBeneficios);
        tvAtividades = findViewById(R.id.tvAtividades);
        tvSalario= findViewById(R.id.tvSalario);

        Intent intent2 = getIntent();
        Bundle bundle = intent2.getExtras();
        id = bundle.getString("id");

        firebase = ConfiguracaoFirebase.getFirebase().child("vagas");
        valueEventListener = new ValueEventListener() {
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
                        vagaExcluir = vagaNova;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(DescricaoAdminActivity.this, EditarVagasActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent3.putExtras(bundle);
                startActivity(intent3);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DescricaoAdminActivity.this);
                builder.setTitle("Confirmar exclusão da vaga?");
                builder.setMessage("Você deseja excluir o " + vagaExcluir.getTipo() + " da empresa " + vagaExcluir.getEmpresa() + "?");

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        firebase = ConfiguracaoFirebase.getFirebase().child("vagas");
                        firebase.child(vagaExcluir.getId()).removeValue();
                        Toast.makeText(DescricaoAdminActivity.this, "Exclusão efetuada!", Toast.LENGTH_LONG).show();
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
