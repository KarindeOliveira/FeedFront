package com.ciandt.feedfront.services;

import com.ciandt.feedfront.contracts.DAO;
import com.ciandt.feedfront.contracts.Service;
import com.ciandt.feedfront.excecoes.EmailInvalidoException;
import com.ciandt.feedfront.excecoes.EntidadeNaoEncontradaException;
import com.ciandt.feedfront.models.Employee;
import com.ciandt.feedfront.excecoes.ArquivoException;
import com.ciandt.feedfront.excecoes.BusinessException;

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

public class EmployeeService implements Service<Employee> {
    private DAO<Employee> dao;

    private static final String repositorioPath = "src/main/resources/data/employee/"; //TODO: alterar de acordo com a sua implementação

    public EmployeeService() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> listar() throws ArquivoException {
        List<Employee> employees = new ArrayList<>();

        try {
            Stream<Path> paths = Files.walk(Paths.get(repositorioPath));

            List<String> files = paths
                    .map(p -> p.getFileName().toString())
                    .filter(p -> p.endsWith(".byte"))
                    .map(p -> p.replace(".byte", ""))
                    .collect(Collectors.toList());

            for (String file: files) {
                try {
                    employees.add(buscar(file));
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
            }

            paths.close();
        } catch (IOException e) {
            throw new ArquivoException("");
        }

        return employees;
    }

    @Override
    public Employee buscar(String id) throws ArquivoException, BusinessException {
        Employee employee;
        ObjectInputStream inputStream;

        try {
            inputStream = getInputStream(repositorioPath + id + ".byte");
            employee = (Employee) inputStream.readObject();

            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            if (e.getClass().getSimpleName().equals("FileNotFoundException")) {
                throw new EntidadeNaoEncontradaException("Employee não encontrado");
            }

            throw new ArquivoException("");
        }

        return employee;
    }

    @Override
    public Employee salvar(Employee employee) throws ArquivoException, BusinessException {
          try {
              List<Employee> employees = null;
              try {
                  employees = dao.listar();
              } catch (ClassNotFoundException e) {
                  throw new RuntimeException(e);
              }

              boolean emailExistente = false;
            for (Employee employeeSalvo: employees) {
                if (!employeeSalvo.getId().equals(employee.getId()) && employeeSalvo.getEmail().equals(employee.getEmail())) {
                    emailExistente = true;
                    break;
                }
            }

            if (emailExistente) {
                throw new EmailInvalidoException("E-mail ja cadastrado no repositorio");
            }

              try {
                  dao.salvar(employee);
              } catch (ClassNotFoundException e) {
                  throw new RuntimeException(e);
              }

          } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ArquivoException("");
        }
        return employee;
    }

    @Override
    public Employee atualizar(Employee employee) throws ArquivoException, BusinessException {
        buscar(employee.getId());

        return salvar(employee);
    }

    @Override
    public void apagar(String id) throws ArquivoException, BusinessException {
        buscar(id);

        new File(String.format("%s%s.byte", repositorioPath, id)).delete();
    }

    @Override
    public void setDAO(DAO<Employee> dao) {

    }
}
