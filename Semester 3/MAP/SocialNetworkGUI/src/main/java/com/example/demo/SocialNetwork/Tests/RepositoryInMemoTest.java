package com.example.demo.SocialNetwork.Tests;

import com.example.demo.SocialNetwork.Exceptions.RepoException;
import com.example.demo.SocialNetwork.Helper.Entity;
import com.example.demo.SocialNetwork.Repos.Memo.RepositoryInMemo;
import com.example.demo.SocialNetwork.Exceptions.ValidationException;
import com.example.demo.SocialNetwork.Validations.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryInMemoTest {

    static class TestEntity extends Entity<Integer> {
        public TestEntity(Integer id) {
            super.setId(id);
        }
    }

    static class TestValidator implements Validator<TestEntity> {
        @Override
        public void validate(TestEntity entity) throws ValidationException {
            if (entity.getId() == null || entity.getId() < 0)
                throw new ValidationException("Invalid ID");
        }
    }

    private RepositoryInMemo<Integer, TestEntity> repo;

    @BeforeEach
    void setUp() {
        //setam noul repo
        repo = new RepositoryInMemo<>(new TestValidator());
    }

    @Test
    void find() {
        // verificam daca gaseste entitatea 1, si 2 care nu exista
        TestEntity entity = new TestEntity(1);
        repo.save(entity);
        Optional<TestEntity> foundEntity = repo.find(1);
        assertTrue(foundEntity.isPresent(), "Entitatea ar trebui să existe după actualizare.");
        assertEquals(entity, foundEntity.get());
        assertEquals(repo.find(2),Optional.empty());
    }

    @Test
    void findAll() {
        // verificam daca le gaseste pe toate salvate
        TestEntity entity1 = new TestEntity(1);
        TestEntity entity2 = new TestEntity(2);
        repo.save(entity1);
        repo.save(entity2);

        List<TestEntity> allEntities = new ArrayList<>();
        repo.findAll().forEach(allEntities::add);

        assertEquals(2, allEntities.size());
        assertTrue(allEntities.contains(entity1));
        assertTrue(allEntities.contains(entity2));
    }

    @Test
    void save() {
        //incercam sa salvam ceva bun apoi ceva null,apoi ceva cu id invalid
        TestEntity entity = new TestEntity(1);
        assertDoesNotThrow(() -> repo.save(entity));
        Optional<TestEntity> foundEntity = repo.find(1);
        assertTrue(foundEntity.isPresent(), "Entitatea ar trebui să existe după actualizare.");
        assertEquals(entity, foundEntity.get());
        try {
            repo.save(null);
            fail("Expected IllegalArgumentException for null entity");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            repo.save(new TestEntity(-1));
            fail("Expected ValidationException for invalid entity");
        } catch (ValidationException e) {
            assertTrue(true);
        }
    }

    @Test
    void delete() {
        //stergem mai intai ceva existent, apoi incercam sa vedem daca arunca exceptiile bune
        TestEntity entity = new TestEntity(1);
        repo.save(entity);

        Optional<TestEntity> deletedEntity = repo.delete(1);
        assertTrue(deletedEntity.isPresent(), "Entitatea ar trebui să existe înainte de a fi ștearsă.");
        assertEquals(entity, deletedEntity.get());
        try{
            repo.delete(2);
            fail("Expected RepoException for invalid entity");
        }
        catch (RepoException e){
            assertTrue(true);
        }

        try {
            repo.delete(null);
            fail("Expected IllegalArgumentException for null ID");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    void update() {
        // modificam mai intai ceva existent apoi verificam pentru cazurile in care se arunca exceptii
        TestEntity entity = new TestEntity(1);
        repo.save(entity);

        TestEntity updatedEntity = new TestEntity(1); // Same ID, different instance
        assertDoesNotThrow(() -> repo.update(updatedEntity));
        Optional<TestEntity> foundEntity = repo.find(1);
        assertTrue(foundEntity.isPresent(), "Entitatea ar trebui să existe după actualizare.");
        assertEquals(updatedEntity, foundEntity.get());

        try {
            repo.update(null);
            fail("Expected IllegalArgumentException for null entity");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        TestEntity invalidEntity = new TestEntity(-1); // Invalid entity ID
        try {
            repo.update(invalidEntity);
            fail("Expected ValidationException for invalid entity");
        } catch (ValidationException e) {
            assertTrue(true);
        }
        try {
            TestEntity nonExistingEntity = new TestEntity(2);
            repo.update(nonExistingEntity);
            fail("Expected RepoException for invalid entity");
        }
        catch (RepoException e){
            assertTrue(true);
        }
    }
}
