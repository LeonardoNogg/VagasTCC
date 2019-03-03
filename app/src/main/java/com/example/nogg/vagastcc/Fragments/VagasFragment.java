package com.example.nogg.vagastcc.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nogg.vagastcc.Activity.DescricaoActivity;
import com.example.nogg.vagastcc.Adapter.VagasAdapter;
import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VagasFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Vagas> adapter;
    private ArrayList<Vagas> vagas;
    private DatabaseReference firebaseV, firebaseA;
    private ValueEventListener valueEventListenerV, valueEventListenerA;
    private Vagas vagaClick;
    private String curso;

    public VagasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vagas, container, false);

        vagas = new ArrayList<>();
        listView = view.findViewById(R.id.lvVagas);
        adapter = new VagasAdapter(getContext(), vagas);
        listView.setAdapter(adapter);

        firebaseA = ConfiguracaoFirebase.getFirebase().child("usuarios");
        valueEventListenerA = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuarios alunoNovo = dados.getValue(Usuarios.class);
                    if(alunoNovo.getEmail().equals(user.getEmail())){
                        curso = alunoNovo.getCurso();
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebaseV = ConfiguracaoFirebase.getFirebase().child("vagas");
        valueEventListenerV = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vagas.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Vagas vagasNova = dados.getValue(Vagas.class);

                    if (curso.equals(vagasNova.getCurso())) {
                        vagas.add(vagasNova);
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vagaClick = adapter.getItem(i);
                Intent intent2 = new Intent(getContext(), DescricaoActivity.class);
                Bundle bundle = new Bundle();
                String id = vagaClick.getId();
                bundle.putString("id", id);
                intent2.putExtras(bundle);
                startActivity(intent2);
            }
        });

        return view;
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

}
