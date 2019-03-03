package com.example.nogg.vagastcc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nogg.vagastcc.Entidades.Solicitacoes;
import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;

import java.util.ArrayList;

public class SolicitacoesAdapter extends ArrayAdapter<Solicitacoes> {

    private ArrayList<Solicitacoes> solicitacao;
    private Context context;

    public SolicitacoesAdapter(Context c, ArrayList<Solicitacoes> objects) {
        super(c, 0, objects);

        this.context = c;
        this.solicitacao = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (solicitacao != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_solicitacoes, parent, false);

            TextView tvNomeAluno = view.findViewById(R.id.tvNomeAluno);
            TextView tvTipo = view.findViewById(R.id.tvTipo);
            TextView tvEmpresa = view.findViewById(R.id.tvEmpresa);

            Solicitacoes solicitacoes2 = solicitacao.get(position);
            tvNomeAluno.setText(solicitacoes2.getNome_aluno());
            tvTipo.setText(solicitacoes2.getTipo_vaga());
            tvEmpresa.setText(solicitacoes2.getEmpresa_vaga());
        }

        return view;
    }
}
