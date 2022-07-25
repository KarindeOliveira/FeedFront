package com.ciandt.feedfront.daos;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.excecoes.ArquivoException;
import com.ciandt.feedfront.excecoes.EmailInvalidoException;
import com.ciandt.feedfront.excecoes.EntidadeNaoEncontradaException;
import com.ciandt.feedfront.models.Employee;
import com.ciandt.feedfront.excecoes.EntidadeNaoSerializavelException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployeeDAO implements DAO<Employee> {
    private static String repositorioPath = "src/main/resources/data/employee/"; //TODO: alterar de acordo com a sua implementação;

    public static String getRepositorioPath() {
        return repositorioPath;
    }

    @Override
    public boolean tipoImplementaSerializable() {
        throw new UnsupportedOperationException();
    }



   public static ObjectOutputStream getOutputStream(String arquivo) throws IOException {
        return new ObjectOutputStream(new FileOutputStream(arquivo));
    }

    public static ObjectInputStream getInputStream(String arquivo) throws IOException {
        return new ObjectInputStream(new FileInputStream(arquivo));
    }
    @Override
    public Employee salvar(Employee employee) throws IOException {

        ObjectOutputStream outputStream = null;

            outputStream = getOutputStream(employee.getArquivo());
            outputStream.writeObject(employee);

            outputStream.close();

        return employee;
    }


    @Override
    public List<Employee> listar() throws IOException, EntidadeNaoSerializavelException {

        List<Employee> employees = new ArrayList<>();


            Stream<Path> paths = Files.walk(Paths.get(repositorioPath));

            List<String> files = paths
                    .map(p -> p.getFileName().toString())
                    .filter(p -> p.endsWith(".byte"))
                    .map(p -> p.replace(".byte", ""))
                    .collect(Collectors.toList());

            for (String file: files) {

                employees.add(buscar(file));
            }

            paths.close();

        return employees;
    }

    @Override

    public Employee buscar(String id) throws EntidadeNaoSerializavelException, IOException {

        if(!tipoImplementaSerializable()) {
            throw new EntidadeNaoSerializavelException("Erro de serialização");
        }

        Employee employee;

        ObjectInputStream inputStream;

        inputStream = getInputStream(EmployeeDAO.getRepositorioPath() + id + ".byte");

        try {

            employee = (Employee) inputStream.readObject();

        } catch (ClassNotFoundException e) {

            throw new IOException(e);

        }
        inputStream.close();
        return employee;
    }


            @Override
            public boolean apagar(String id) throws IOException, EntidadeNaoSerializavelException {
                buscar(id);

                new File(String.format("%s%s.byte", repositorioPath, id)).delete();
                return false;
            }

    }

