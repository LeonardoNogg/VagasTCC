package com.example.nogg.vagastcc.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nogg.vagastcc.Activity.CadastrosActivity;
import com.example.nogg.vagastcc.Activity.DescricaoAdminActivity;
import com.example.nogg.vagastcc.Adapter.VagasAdapter;
import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VagasAdminFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Vagas> adapter;
    private ArrayList<Vagas> vagas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerVagas;
    private Vagas vagaClick;

    public VagasAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vagas_admin, container, false);

        vagas = new ArrayList<>();
        listView = view.findViewById(R.id.lvVagas);
        adapter = new VagasAdapter(getContext(), vagas);
        listView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abrirCadastros();
            }
        });

        firebase = ConfiguracaoFirebase.getFirebase().child("vagas");

        valueEventListenerVagas = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vagas.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Vagas vagasNova = dados.getValue(Vagas.class);

                    vagas.add(vagasNova);
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
                Intent intent2 = new Intent(getContext(), DescricaoAdminActivity.class);
                Bundle bundle = new Bundle();
                String id = vagaClick.getId();
                bundle.putString("id", id);
                intent2.putExtras(bundle);
                startActivity(intent2);
            }
        });

        return view;
    }

    public void abrirCadastros() {
        Intent intent = new Intent(getContext(), CadastrosActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerVagas);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerVagas);
    }

}
