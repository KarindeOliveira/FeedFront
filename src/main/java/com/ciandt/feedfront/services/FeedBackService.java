package com.ciandt.feedfront.services;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.contracts.Service;
import com.ciandt.feedfront.excecoes.ArquivoException;
import com.ciandt.feedfront.excecoes.BusinessException;
import com.ciandt.feedfront.excecoes.EmailInvalidoException;
import com.ciandt.feedfront.excecoes.EntidadeNaoEncontradaException;
import com.ciandt.feedfront.models.Employee;
import com.ciandt.feedfront.models.FeedBack;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ciandt.feedfront.daos.EmployeeDAO.getInputStream;

public class FeedBackService implements Service<FeedBack> {

    private DAO<FeedBack> dao;

    private static final String repositorioPath = "src/main/resources/data/employee/"; //TODO: alterar de acordo com a sua implementação

    public FeedBackService() {
        throw new UnsupportedOperationException();
    }



    @Override
    public List listar() throws ArquivoException {
        List<FeedBack> feedBacks = new ArrayList<>();

        try {
            Stream<Path> paths = Files.walk(Paths.get(repositorioPath));

            List<String> files = paths
                    .map(p -> p.getFileName().toString())
                    .filter(p -> p.endsWith(".byte"))
                    .map(p -> p.replace(".byte", ""))
                    .collect(Collectors.toList());

            for (String file: files) {
                try {
                    feedBacks.add(buscar(file));
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
            }

            paths.close();
        } catch (IOException e) {
            throw new ArquivoException("");
        }

        return feedBacks;
    }

    @Override
    public FeedBack buscar(String id) throws ArquivoException, BusinessException {
        FeedBack feedBack;
        ObjectInputStream inputStream;

        try {
            inputStream = getInputStream(repositorioPath + id + ".byte");
            feedBack = (FeedBack) inputStream.readObject();

            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            if (e.getClass().getSimpleName().equals("FileNotFoundException")) {
                throw new EntidadeNaoEncontradaException("Employee não encontrado");
            }

            throw new ArquivoException("");
        }

        return feedBack;
    }

    @Override
    public FeedBack salvar(FeedBack feedBack) throws ArquivoException, BusinessException, IllegalArgumentException {
        try {
            List<FeedBack> feedBacks = null;
            try {
                feedBacks= dao.listar();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            boolean emailExistente = false;
            for (FeedBack feedBackSalvo: feedBacks) {
                if (!feedBackSalvo.getId().equals(feedBack.getId())) {
                    emailExistente = true;
                    break;
                }
            }

            if (emailExistente) {
                throw new EmailInvalidoException("E-mail ja cadastrado no repositorio");
            }

            try {
                dao.salvar(feedBack);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ArquivoException("");
        }
        return feedBack;
    }

    @Override
    public FeedBack atualizar(FeedBack feedBack) throws ArquivoException, BusinessException, IllegalArgumentException {
        buscar(feedBack.getId());

        return salvar(feedBack);
    }

    @Override
    public void apagar(String id) throws ArquivoException, BusinessException {
        buscar(id);

        new File(String.format("%s%s.byte", repositorioPath, id)).delete();

    }

    @Override
    public void setDAO(DAO<FeedBack> dao) {

    }


}
