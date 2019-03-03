package com.example.nogg.vagastcc.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nogg.vagastcc.Activity.CadAdminActivity;
import com.example.nogg.vagastcc.Activity.LoginActivity;
import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.example.nogg.vagastcc.Entidades.Usuarios;
import com.example.nogg.vagastcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilAdminFragment extends Fragment {

    private Button btnLogout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuarios;
    private TextView tvNome, tvEmail;

    public PerfilAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil_admin, container, false);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogarUsuario();
            }
        });

        tvNome = view.findViewById(R.id.tvNome);
        tvEmail = view.findViewById(R.id.tvEmail);

        firebase = ConfiguracaoFirebase.getFirebase().child("usuarios");
        valueEventListenerUsuarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Usuarios usuarioNovo = dados.getValue(Usuarios.class);
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user.getEmail().equals(usuarioNovo.getEmail())){
                        tvNome.setText(usuarioNovo.getNome());
                        tvEmail.setText(usuarioNovo.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return view;

    }

    public void abrirCadAdmin() {
        Intent intent = new Intent(getContext(), CadAdminActivity.class);
        startActivity(intent);
    }

    private void deslogarUsuario() {
        firebaseAuth.signOut();
        Intent intent1 = new Intent(getContext(), LoginActivity.class);
        startActivity(intent1);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerUsuarios);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerUsuarios);
    }

}
