package com.ciandt.feedfront.controller;

import com.ciandt.feedfront.contracts.Service;
import com.ciandt.feedfront.excecoes.ArquivoException;
import com.ciandt.feedfront.excecoes.BusinessException;
import com.ciandt.feedfront.models.Employee;
import com.ciandt.feedfront.models.FeedBack;

import java.util.List;

public class FeedBackController {


    private Service<FeedBack> service;

    public FeedBackController() {
        throw new UnsupportedOperationException();
    }

    public List<FeedBack> listar() throws ArquivoException {

        return  service.listar();
    }

    public FeedBack buscar(String id) throws BusinessException, ArquivoException {
        return service.buscar(id);
    }

    public FeedBack salvar(FeedBack feedBack) throws BusinessException, ArquivoException {
        return service.salvar(feedBack);
    }

    public FeedBack atualizar(FeedBack feedBack) throws BusinessException, ArquivoException {
        return service.atualizar(feedBack);
    }

    public void apagar(String id) throws BusinessException, ArquivoException {
        service.apagar(id);
    }

    public void setService(Service<FeedBack> service) {}
}
