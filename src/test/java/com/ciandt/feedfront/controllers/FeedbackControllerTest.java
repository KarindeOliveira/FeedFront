package com.ciandt.feedfront.controllers;


import com.ciandt.feedfront.contracts.Service;
import com.ciandt.feedfront.controller.FeedBackController;
import com.ciandt.feedfront.excecoes.ArquivoException;
import com.ciandt.feedfront.excecoes.BusinessException;
import com.ciandt.feedfront.excecoes.ComprimentoInvalidoException;
import com.ciandt.feedfront.models.Employee;

import com.ciandt.feedfront.models.FeedBack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FeedbackControllerTest {

    private FeedBack feedback;

    private Employee autor;

    private Employee proprietario;

    private FeedBackController controller;
    private Service<FeedBack> service;

    @BeforeEach
    public void initEach() throws IOException, BusinessException {
        Files.walk(Paths.get("src/main/resources/data/feedback/"))
                .filter(p -> p.toString().endsWith(".byte"))
                .forEach(p -> {
                    new File(p.toString()).delete();
                });

        controller = new FeedBackController();
        service = (Service<FeedBack>) Mockito.mock(Service.class);
        autor = new Employee("João", "Silveira", "j.silveira@email.com");

        proprietario = new Employee("Mateus", "Santos", "m.santos@email.com");


        feedback = new FeedBack( "1", LocalDate.now(), autor, proprietario,"Agradeco muito pelo apoio feito pelo colega!", "file");//construtor 1

        controller.salvar(feedback);
    }
    @Test
    public void listar() {
        Collection<FeedBack> listaFeedback = assertDoesNotThrow(controller::listar);

        assertNotNull(listaFeedback);
    }

    @Test
    public void salvar() throws BusinessException, ArquivoException {
        when(service.salvar(feedback)).thenReturn(feedback);

        FeedBack salvo = assertDoesNotThrow(() -> controller.salvar(feedback));

        assertEquals(feedback, salvo);

    }

    @Test
    public void buscar() throws BusinessException, ArquivoException {
        String uuid = feedback.getId();
        when(service.buscar(uuid)).thenReturn(feedback);

        FeedBack salvo = assertDoesNotThrow(() -> controller.buscar(uuid));

        assertEquals(feedback, salvo);

    }

}
