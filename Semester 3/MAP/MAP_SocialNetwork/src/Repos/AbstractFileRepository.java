package Repos;

import Helper.Entity;
import Validations.Validator;

import java.io.*;
import java.util.Optional;

public abstract class AbstractFileRepository <ID,E extends Entity<ID>> extends RepositoryInMemo<ID,E>{
    private String fileName;
    public AbstractFileRepository(Validator<E> validator,String fileName) {
        super(validator);
        this.fileName = fileName;
    }

    protected abstract E createEntity(String line);
    protected abstract String saveEntity(E entity);

    @Override
    public Optional<E> find(ID id) {
        return super.find(id);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if (e.get() != null)
            writeToFile();
        return e;
    }

    private void writeToFile() {
        //daca nu punem conditia ar fi intrat si daca merge si daca nu
        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            //nu merge for each pentru ca writer trebuie sa fie intr un try catch
            for (E entity: super.findAll()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                E entity=createEntity(line);
                Optional<E> optionalE=super.save(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> e=super.delete(id);
        writeToFile();
        return e;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> ent=super.update(entity);
        if(ent.get()==entity)
            writeToFile();
        return ent;
    }
}
