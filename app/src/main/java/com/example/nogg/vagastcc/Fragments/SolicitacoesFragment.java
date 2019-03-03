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

import com.example.nogg.vagastcc.Activity.SolicitacaoDescActivity;
import com.example.nogg.vagastcc.Adapter.SolicitacoesAdapter;
import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Solicitacoes;
import com.example.nogg.vagastcc.Entidades.Usuarios;
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
public class SolicitacoesFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<Solicitacoes> adapter;
    private ArrayList<Solicitacoes> solicitacoes;
    private DatabaseReference firebaseS, firebaseA, firebaseV;
    private ValueEventListener valueEventListenerS, valueEventListenerA, valueEventListenerV;
    private Solicitacoes solicitacoesClick;
    private String nome_aluno, empresa_vaga, tipo_vaga;

    public SolicitacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_solicitacoes, container, false);

        solicitacoes = new ArrayList<>();
        listView = view.findViewById(R.id.lvSolicitacoes);
        adapter = new SolicitacoesAdapter(getContext(), solicitacoes);
        listView.setAdapter(adapter);

        firebaseS = ConfiguracaoFirebase.getFirebase().child("solicitacoes");
        valueEventListenerS = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                solicitacoes.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Solicitacoes solicitacaoNova = dados.getValue(Solicitacoes.class);

                    solicitacoes.add(solicitacaoNova);
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
                solicitacoesClick = adapter.getItem(i);
                Intent intent4 = new Intent(getContext(), SolicitacaoDescActivity.class);
                Bundle bundle = new Bundle();
                String id = solicitacoesClick.getId();
                bundle.putString("id", id);
                intent4.putExtras(bundle);
                startActivity(intent4);
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseS.removeEventListener(valueEventListenerS);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseS.addValueEventListener(valueEventListenerS);
    }

}
