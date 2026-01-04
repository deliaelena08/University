package Tests;

import Exceptions.RepoException;
import Model.User;
import Repos.RepositoryUser;
import Validations.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.descriptor.TestInstanceLifecycleUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryUserTest {
    private RepositoryUser repo;

    @BeforeEach
    void setUp() {
        clearFile();
        repo = new RepositoryUser(new UserValidator(), "test_users.txt");
    }

    void clearFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("test_users.txt"))) {
            writer.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveUser(){
        User u1 = new User("Ovidiu","Mihai","om@yahoo.com","1231114");
        User u2 = new User("Ana","Maria","am@yahoo.com","1111111");
        User u3 = new User("Cristian","Aurelian","ca@yahoo.com","222222");
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);
        repo.save(u1);
        repo.save(u2);
        repo.save(u3);
        int i=0;
        for(User u: repo.findAll()){
            if(i==0) {
                assert u.getId() == 1L;
            }
            else if(i==1) {
                assert u.getId() == 2L;
            }
            else if(i==2) {
                assert u.getId() == 3L;
            }
            i++;
        }
    }

    @Test
    void findUser(){
        User u1 = new User("Ovidiu","Mihai","om@yahoo.com","1231114");
        User u2 = new User("Ana","Maria","am@yahoo.com","1111111");
        User u3 = new User("Cristian","Aurelian","ca@yahoo.com","222222");
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);
        repo.save(u1);
        repo.save(u2);
        repo.save(u3);
        Optional<User> u=repo.find(u1.getId());
        assert u.get().getId() == 1L;
        assertEquals(repo.find(4L),Optional.empty());
        assert repo.find(u2.getId()).get().getId() == 2L;
    }

    @Test
    void deleteUser(){
        User u1 = new User("Ovidiu","Mihai","om@yahoo.com","1231114");
        User u2 = new User("Ana","Maria","am@yahoo.com","1111111");
        u1.setId(1L);
        u2.setId(2L);
        repo.save(u1);
        repo.save(u2);
        assertEquals(u1.getId() , repo.delete(u1.getId()).get().getId());
        assertEquals(u2.getId() , repo.delete(u2.getId()).get().getId());
        try{
            repo.delete(u1.getId());
            fail("Excepted RepoException");
        }
        catch (RepoException e){
            assertTrue(true);
        }
    }

    @Test
    void updateUser(){
        User u1 = new User("Ovidiu","Mihai","om@yahoo.com","1231114");
        User u2 = new User("Ana","Maria","am@yahoo.com","1111111");
        u1.setId(1L);
        u2.setId(1L);
        repo.save(u1);
        repo.update(u2);
        assertEquals(repo.find(u1.getId()).get().getFirstName() , "Ana");
        assertEquals(repo.find(u1.getId()).get().getLastName() , "Maria");
        assertEquals(repo.find(u1.getId()).get().getPassword() , "1111111");
    }
}