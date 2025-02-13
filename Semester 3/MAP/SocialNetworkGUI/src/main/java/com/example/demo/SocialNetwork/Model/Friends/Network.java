package com.example.demo.SocialNetwork.Model.Friends;

import com.example.demo.SocialNetwork.Model.User;

import java.util.*;

public class Network {
    Set<User> users;
    Set<Friendship> friendships;

    /*
    * users- este o multime de tip User
    * friendships- este o multime cu relatiile de prietenie
    * Constructorul pentru o relatie de networking
    * */
    public Network(Set<User> users, Set<Friendship> friendships) {
        this.users = users;
        this.friendships = friendships;
    }


    private Map<User, List<User>> createGraph() {
        Map<User, List<User>> graph = new HashMap<>();
        for (User user : users) {
            graph.put(user, new ArrayList<>());
        }
        for (Friendship friendship : friendships) {
            User u1 = friendship.getId().getE1();
            User u2 = friendship.getId().getE2();
            //graful va fi neorientat
            graph.get(u1).add(u2);
            graph.get(u2).add(u1);
        }
        return graph;
    }

    private void dfs(User user, Set<User> visited, Map<User, List<User>> graph) {
        visited.add(user);
        graph.getOrDefault(user, List.of()).forEach(neighbor -> {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, graph);
            }
        });
    }

    //DFS pentru a umple o comunitate
    private void dfs(User user, Set<User> visited, Map<User, List<User>> graph, Set<User> community) {
        visited.add(user);
        community.add(user);
        graph.getOrDefault(user, List.of()).forEach(neighbor -> {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, graph, community);
            }
        });
    }

    public int numberOfCommunities() {
        Map<User, List<User>> graph = createGraph();
        Set<User> visited = new HashSet<>();
        int[] numberOfComponents = {0};
        users.forEach(user -> {
            if (!visited.contains(user)) {
                // Am găsit o componentă conexă
                numberOfComponents[0]++;
                dfs(user, visited, graph);
            }
        });
        return numberOfComponents[0];
    }

    // Funcția pentru a calcula cel mai lung drum dintr-o componentă
    private int longestPathFrom(User user, Map<User, List<User>> graph) {
        Queue<User> queue = new LinkedList<>();
        Map<User, Integer> distances = new HashMap<>();
        queue.add(user);
        distances.put(user, 0);

        int[] maxDistance = {0};

        while (!queue.isEmpty()) {
            User current = queue.poll();
            int currentDistance = distances.get(current);
            graph.get(current).forEach(neighbor->
            {
                if(!distances.containsKey(neighbor)) {
                    queue.add(neighbor);
                    distances.put(neighbor, currentDistance + 1);
                    maxDistance[0]=Math.max(maxDistance[0],currentDistance+1);
                }
            });
        }
        return maxDistance[0];
    }

    public Set<User> mostSociableCommunity() {
        Map<User, List<User>> graph = createGraph();
        Set<User> visited = new HashSet<>();
        Set<User> largestCommunity = new HashSet<>();
        int maxLongestPath = 0;
        users.forEach(user -> {});
        for (User user : users) {
            if (!visited.contains(user)) {
                Set<User> currentCommunity = new HashSet<>();
                dfs(user, visited, graph, currentCommunity); // DFS care adaugă utilizatorii în comunitate
                int longestPath = 0;

                // Calculăm cel mai lung drum din orice nod din această comunitate
                for (User member : currentCommunity) {
                    longestPath = Math.max(longestPath, longestPathFrom(member, graph));
                }

                if (longestPath > maxLongestPath) {
                    maxLongestPath = longestPath;
                    largestCommunity = currentCommunity;
                }
            }
        }
        System.out.println(largestCommunity.size());
        return largestCommunity;
    }

}
