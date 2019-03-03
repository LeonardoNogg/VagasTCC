package com.example.nogg.vagastcc.Entidades;

import com.example.nogg.vagastcc.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuarios {

    private String id;
    private String matricula;
    private String nome;
    private String senha;
    private String email;
    private String telefone;
    private String data_nasc;
    private String sexo;
    private String privilegio;
    private String curso;
    private String turma;

    public Usuarios() {
    }

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child(String.valueOf(getId())).setValue(this);
    }

    @Exclude

    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("matricula", getMatricula());
        hashMapUsuario.put("nome", getNome());
        hashMapUsuario.put("senha", getSenha());
        hashMapUsuario.put("email", getEmail());
        hashMapUsuario.put("telefone", getTelefone());
        hashMapUsuario.put("data_nasc", getData_nasc());
        hashMapUsuario.put("sexo", getSexo());
        hashMapUsuario.put("privilegio", getPrivilegio());
        hashMapUsuario.put("curso", getCurso());
        hashMapUsuario.put("turma", getTurma());

        return hashMapUsuario;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(String privilegio) {
        this.privilegio = privilegio;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurma() {
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }
}
