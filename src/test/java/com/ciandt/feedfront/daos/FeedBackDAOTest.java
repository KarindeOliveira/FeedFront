package com.ciandt.feedfront.daos;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.contracts.Service;
import com.ciandt.feedfront.controller.FeedBackController;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FeedBackDAOTest {

    private FeedBack feedBack;
    private DAO<FeedBack> dao;
    private FeedBack feedback;

    private Employee autor;

    private Employee proprietario;

    private FeedBackController controller;
    private Service<FeedBack> service;

    @BeforeEach
    public void initEach() throws IOException, ComprimentoInvalidoException, ClassNotFoundException {
        // Este trecho de código serve somente para limpar o repositório
        Files.walk(Paths.get("src/main/resources/data/employee/"))
                .filter(p -> p.toString().endsWith(".byte"))
                .forEach(p -> {
                    new File(p.toString()).delete();
                });

        controller = new FeedBackController();
        service = (Service<FeedBack>) Mockito.mock(Service.class);
        autor = new Employee("João", "Silveira", "j.silveira@email.com");

        proprietario = new Employee("Mateus", "Santos", "m.santos@email.com");

        dao = new FeedBackDao();
        feedBack = new FeedBack( "1", LocalDate.now(), autor, proprietario,"Agradeco muito pelo apoio feito pelo colega!", "file");

        dao.salvar(feedBack);
    }

    @Test
    public void listar() throws IOException, ClassNotFoundException {
        List<FeedBack> result = dao.listar();

        assertFalse(result.isEmpty());
    }

    @Test
    public void buscar() {
        String idValido = feedBack.getId();
        String idInvalido = UUID.randomUUID().toString();

        assertThrows(IOException.class, () -> dao.buscar(idInvalido));
        FeedBack salvo = assertDoesNotThrow(() -> dao.buscar(idValido));

        assertEquals(salvo, feedBack);
    }

    @Test
    public void salvar() throws IOException, ComprimentoInvalidoException, ClassNotFoundException {
        String id = feedBack.getId();
        FeedBack salvo = dao.buscar(id);
        FeedBack naoSalvo = new FeedBack( "1", LocalDate.now(), autor, proprietario,"Agradeco muito pelo apoio feito pelo colega!", "file");

        assertEquals(feedBack, salvo);
        assertDoesNotThrow(() -> dao.salvar(naoSalvo));
    }

    @Test
    public void atualizarDados() throws IOException, ComprimentoInvalidoException, ClassNotFoundException {
        feedBack.setId("1");
        feedBack.setDescricao("b.silveira@email.com");

        FeedBack salvo = dao.buscar(feedBack.getId());

        assertNotEquals(salvo.getId(), feedBack.getId());
        assertNotEquals(salvo.getDescricao(), feedBack.getDescricao());

        FeedBack atualizado = dao.salvar(feedBack);

        assertEquals(atualizado, feedBack);
    }

    @Test
    public void apagar() {
        boolean apagou = assertDoesNotThrow(() -> dao.apagar(feedBack.getId()));

        assertTrue(apagou);
        assertThrows(IOException.class, () -> dao.buscar(feedBack.getId()));
    }
}


}
