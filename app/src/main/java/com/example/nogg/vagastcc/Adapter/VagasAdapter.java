package com.example.nogg.vagastcc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nogg.vagastcc.Entidades.Vagas;
import com.example.nogg.vagastcc.R;

import java.util.ArrayList;

public class VagasAdapter extends ArrayAdapter<Vagas> {

    private ArrayList<Vagas> vaga;
    private Context context;

    public VagasAdapter(Context c, ArrayList<Vagas> objects) {
        super(c, 0, objects);

        this.context = c;
        this.vaga = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (vaga != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_vagas, parent, false);

            TextView tvEmpresa = view.findViewById(R.id.tvEmpresa);
            TextView tvTipo = view.findViewById(R.id.tvTipo);
            TextView tvSetor = view.findViewById(R.id.tvSetor);
            TextView tvLocal = view.findViewById(R.id.tvLocal);

            Vagas vagas2 = vaga.get(position);
            tvEmpresa.setText(vagas2.getEmpresa());
            tvTipo.setText(vagas2.getTipo());
            tvSetor.setText(vagas2.getSetor());
            tvLocal.setText(vagas2.getLocal());

        }

        return view;
    }
}
