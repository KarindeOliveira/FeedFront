package com.ciandt.feedfront.daos;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.excecoes.EntidadeNaoSerializavelException;
import com.ciandt.feedfront.models.Employee;
import com.ciandt.feedfront.models.FeedBack;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FeedBackDao implements DAO<FeedBack> {
    private static String repositorioPath = "src/main/resources/data/employee/";

    public static String getRepositorioPath() {
        return repositorioPath;
    }

    public static ObjectOutputStream getOutputStream(String arquivo) throws IOException {
        return new ObjectOutputStream(new FileOutputStream(arquivo));
    }

    public static ObjectInputStream getInputStream(String arquivo) throws IOException {
        return new ObjectInputStream(new FileInputStream(arquivo));
    }

    @Override
    public boolean tipoImplementaSerializable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FeedBack> listar() throws IOException, EntidadeNaoSerializavelException {
        List<FeedBack> feedBacks = new ArrayList<>();


        Stream<Path> paths = Files.walk(Paths.get(repositorioPath));

        List<String> files = paths
                .map(p -> p.getFileName().toString())
                .filter(p -> p.endsWith(".byte"))
                .map(p -> p.replace(".byte", ""))
                .collect(Collectors.toList());

        for (String file: files) {

            feedBacks.add(buscar(file));
        }

        paths.close();

        return feedBacks;
    }

    @Override
    public FeedBack buscar(String id) throws IOException, EntidadeNaoSerializavelException {
        if(!tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Erro de serialização");
        }

        FeedBack feedBack;

        ObjectInputStream inputStream;

        inputStream = getInputStream(EmployeeDAO.getRepositorioPath() + id + ".byte");

        try {

            feedBack = (FeedBack) inputStream.readObject();

        } catch (ClassNotFoundException e) {

            throw new IOException(e);

        }
        inputStream.close();
        return feedBack;
    }

    @Override
    public FeedBack salvar(FeedBack feedBack) throws IOException, EntidadeNaoSerializavelException {
        ObjectOutputStream outputStream = null;

        outputStream = getOutputStream(feedBack.getArquivo());
        outputStream.writeObject(feedBack);

        outputStream.close();

        return feedBack;
    }

    @Override
    public boolean apagar(String id) throws IOException, EntidadeNaoSerializavelException {
        buscar(id);

        new File(String.format("%s%s.byte", repositorioPath, id)).delete();
        return false;
    }
}





