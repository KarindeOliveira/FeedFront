package com.ciandt.feedfront.models;

import java.io.Serializable;
import java.time.LocalDate;

public class FeedBack implements Serializable {

    private static final long serialVersionID = 1;

    private String id;
    private LocalDate data;
    private Employee autor;
    private Employee proprietario;
    private String descricao;
    private String arquivo;
    private String oqueMelhora;
    private String comoMelhora;

    public FeedBack(String id, LocalDate data, Employee autor, Employee proprietario, String descricao, String arquivo, String oqueMelhora, String comoMelhora) {
        this.id = id;
        this.data = data;
        this.autor = autor;
        this.proprietario = proprietario;
        this.descricao = descricao;
        this.arquivo = arquivo;
        this.oqueMelhora = oqueMelhora;
        this.comoMelhora = comoMelhora;
    }

    public FeedBack(String id, LocalDate data, Employee autor, Employee proprietario, String descricao, String arquivo) {
        this.id = id;
        this.data = data;
        this.autor = autor;
        this.proprietario = proprietario;
        this.descricao = descricao;
        this.arquivo = arquivo;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Employee getAutor() {
        return autor;
    }

    public void setAutor(Employee autor) {
        this.autor = autor;
    }

    public Employee getProprietario() {
        return proprietario;
    }

    public void setProprietario(Employee proprietario) {
        this.proprietario = proprietario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public String getOqueMelhora() {
        return oqueMelhora;
    }

    public void setOqueMelhora(String oqueMelhora) {
        this.oqueMelhora = oqueMelhora;
    }

    public String getComoMelhora() {
        return comoMelhora;
    }

    public void setComoMelhora(String comoMelhora) {
        this.comoMelhora = comoMelhora;
    }
}
