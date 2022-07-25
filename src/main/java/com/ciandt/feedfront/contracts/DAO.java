package com.ciandt.feedfront.contracts;

import com.ciandt.feedfront.excecoes.EntidadeNaoSerializavelException;

import java.io.IOException;
import java.util.List;

public interface DAO<E> {
    boolean tipoImplementaSerializable();

    List<E> listar() throws IOException, EntidadeNaoSerializavelException, ClassNotFoundException;

    E buscar(String id) throws IOException, EntidadeNaoSerializavelException, ClassNotFoundException;

    E salvar(E e) throws IOException, EntidadeNaoSerializavelException, ClassNotFoundException;

    boolean apagar(String id) throws IOException, EntidadeNaoSerializavelException, ClassNotFoundException;

}
