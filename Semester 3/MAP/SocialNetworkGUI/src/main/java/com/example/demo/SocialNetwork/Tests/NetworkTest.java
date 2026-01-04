package com.example.demo.SocialNetwork.Tests;

import com.example.demo.SocialNetwork.Helper.Tuple;
import com.example.demo.SocialNetwork.Model.*;
import com.example.demo.SocialNetwork.Model.Friends.Friendship;
import com.example.demo.SocialNetwork.Model.Friends.Network;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NetworkTest {
    private Network network;

    @BeforeEach
    void setUp() {
        // Crearea utilizatorilor
        User user1 = new User("Alice", "Doe", "alice@example.com", "password1");
        User user2 = new User("Bob", "Smith", "bob@example.com", "password2");
        User user3 = new User("Charlie", "Brown", "charlie@example.com", "password3");
        User user4 = new User("David", "Wilson", "david@example.com", "password4");

        // Set de utilizatori
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        // Crearea prietenilor
        Set<Friendship> friendships = new HashSet<>();
        Friendship friendship = new Friendship();
        friendship.setId(new Tuple<>(user1, user2));
        friendship.setDate(LocalDateTime.now());
        friendships.add(friendship);
        friendship = new Friendship();
        friendship.setId(new Tuple<>(user2, user3));
        friendship.setDate(LocalDateTime.now());
        friendships.add(friendship);

        // Crearea rețelei
        network = new Network(users, friendships);
    }

    @Test
    void testNumberOfCommunities() {
        assertEquals(2, network.numberOfCommunities());
    }

    @Test
    void testMostSociableCommunity() {
        Set<User> mostSociable = network.mostSociableCommunity();
        assertEquals(3, mostSociable.size());
    }
}
