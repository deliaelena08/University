package com.example.demo.SocialNetwork.Tests;

import com.example.demo.SocialNetwork.Helper.Tuple;
import com.example.demo.SocialNetwork.Model.*;
import com.example.demo.SocialNetwork.Model.Friends.Friendship;
import com.example.demo.SocialNetwork.Repos.Repository;
import com.example.demo.SocialNetwork.Repos.Memo.RepositoryFriendship;
import com.example.demo.SocialNetwork.Repos.Memo.RepositoryUser;
import com.example.demo.SocialNetwork.Validations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryFriendshipTest {
    RepositoryFriendship repositoryFriendship;
    Repository<Long, User> repositoryUser;
    Validator<Friendship> friendshipValidator;
    Validator<User> userValidator;

    @BeforeEach
    void setUp() {
        clearFile();
        userValidator = (Validator<User>) new UserValidator();
        friendshipValidator = (Validator<Friendship>) new FriendshipValidator();
        repositoryUser=new RepositoryUser(userValidator,"test_users.txt");
        repositoryFriendship = new RepositoryFriendship(friendshipValidator, "test_friendships.txt", repositoryUser);

        User u1 = new User("Ovidiu","Mihai","om@yahoo.com","1231114");
        User u2 = new User("Ana","Maria","am@yahoo.com","1111111");
        User u3 = new User("Cristian","Aurelian","ca@yahoo.com","222222");
        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);
        repositoryUser.save(u1);
        repositoryUser.save(u2);
        repositoryUser.save(u3);
    }

    void clearFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("test_friendships.txt"))) {
            writer.flush();
        }
         catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("test_users.txt"))) {
            writer.flush();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveFriendship() {
        Optional<User> u1=repositoryUser.find(1L);
        Optional<User> u2=repositoryUser.find(2L);
        Optional<User> u3=repositoryUser.find(3L);

        Friendship friendship = new Friendship();
        Tuple<User,User> tuple=new Tuple<>(u1.get(),u2.get());
        Tuple<User,User> tuple2=new Tuple<>(u2.get(),u3.get());
        friendship.setId(tuple);
        friendship.setDate(LocalDateTime.now());
        assertNotNull(repositoryFriendship.save(friendship));
        friendship.setId(tuple2);
        friendship.setDate(LocalDateTime.now());
        assertNotNull(repositoryFriendship.save(friendship));
        Optional<Friendship> friendshipsaved=repositoryFriendship.find(friendship.getId());
        assertNotNull(friendshipsaved);
    }

    @Test
    void testFindFriendship() {
        Optional<User> u1 = repositoryUser.find(1L);
        Optional<User> u2 = repositoryUser.find(2L);
        Tuple<User,User> tuple=new Tuple<>(u1.get(),u2.get());
        Friendship friendship = new Friendship();
        friendship.setId(tuple);
        friendship.setDate(LocalDateTime.now());
        repositoryFriendship.save(friendship);

        Optional<Friendship> foundFriendship = repositoryFriendship.find(new Tuple<>(u1.get(), u2.get()));
        assertNotNull(foundFriendship);
        //Verificam daca prietenia e corecta
        assertEquals(foundFriendship.get().getId(), friendship.getId());
    }

    @Test
    void testDeleteFriendship() {
        Optional<User> u3 = repositoryUser.find(3L);
        Optional<User> u2 = repositoryUser.find(2L);

        // Verificăm dacă ambele opțiuni sunt prezente
        if (u3.isPresent() && u2.isPresent()) {
            // Creăm un Tuple din utilizatori
            Tuple<User, User> userTuple = new Tuple<>(u3.get(), u2.get());

            // Creăm prietenia
            Friendship friendship = new Friendship();
            friendship.setId(userTuple);
            friendship.setDate(LocalDateTime.now());
            repositoryFriendship.save(friendship);

            // Ștergem prietenia
            Optional<Friendship> deletedFriendship = repositoryFriendship.delete(userTuple);

            // Verificăm dacă prietenia a fost ștearsă cu succes
            assertNotNull(deletedFriendship); // Prietenia ar trebui să fie ștearsă cu succes
            assertEquals(repositoryFriendship.find(userTuple),Optional.empty()); // Nu ar trebui să mai fie în repo după ștergere
        } else {
            // Gestionează cazul în care cel puțin un utilizator nu a fost găsit
            fail("Unul dintre utilizatori nu a fost găsit.");
        }
    }

    @Test
    void testUpdateFriendship() {
        Optional<User> u1 = repositoryUser.find(1L);
        Optional<User> u3 = repositoryUser.find(3L);

        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(u1.get(), u3.get()));
        friendship.setDate(LocalDateTime.now());
        repositoryFriendship.save(friendship);

        // Actualizăm data prieteniei
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);
        friendship.setDate(newDate);
        Optional<Friendship> updatedFriendship = repositoryFriendship.update(friendship);
        // Verificăm dacă prietenia a fost actualizată corect
        assertEquals(newDate, updatedFriendship.get().getDate());
    }
}