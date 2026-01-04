import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Main {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/PPD";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "Delia.08";

    private static final int NUM_TABLES = 10;
    private static final String TABLE_PREFIX = "proiect";
    private static final String RESULT_FILE = "rezultate.txt";
    private static final int QUEUE_CAPACITY = 100;

    static class Node {
        String id;
        int nota;
        Node next;
        ReentrantLock lock = new ReentrantLock();

        public Node(String id, int nota) {
            this.id = id;
            this.nota = nota;
            this.next = null;
        }
    }

    /**
     * Lista Thread-Safe cu Fine-Grained Locking.
     */
    static class FineGrainedList {
        private final Node head;
        private final Node tail;

        private final Set<String> cheaters = Collections.synchronizedSet(new HashSet<>());

        public FineGrainedList() {
            head = new Node(null, 0);
            tail = new Node(null, 0);
            head.next = tail;
        }

        public Set<String> getCheaters() {
            return cheaters;
        }

        public void addAllCheaters(Set<String> otherCheaters) {
            this.cheaters.addAll(otherCheaters);
        }

        //Insert, Update sau Delete (pt nota -1)
        public void processStudent(String id, int nota) {
            //ignoram cel care a copiat :))
            if (cheaters.contains(id)) return;

            boolean isCheater = (nota == -1);
            if (isCheater) {
                cheaters.add(id);
            }

            head.lock.lock();
            Node pred = head;
            try {
                Node curr = pred.next;
                curr.lock.lock();
                try {
                    while (curr != tail) {
                        if (curr.id.equals(id)) {
                            if (isCheater) {
                                //nota -1 Il stergem din lista
                                pred.next = curr.next;
                            } else {
                                curr.nota += nota;
                            }
                            return;
                        }
                        //Hand-over-hand locking
                        Node oldPred = pred;
                        pred = curr;
                        oldPred.lock.unlock();
                        curr = curr.next;
                        curr.lock.lock();
                    }

                    //Nu l-am gasit. Daca nu e cheater, il adaugam.
                    if (!isCheater) {
                        Node newNode = new Node(id, nota);
                        newNode.next = tail;
                        pred.next = newNode;
                    }
                } finally {
                    curr.lock.unlock();
                }
            } finally {
                pred.lock.unlock();
            }
        }

        //Extrage primul element (pentru mutarea in lista sortata - Faza 2)
        public Node extractFirst() {
            head.lock.lock();
            Node pred = head;
            try {
                Node curr = pred.next;
                curr.lock.lock();
                try {
                    if (curr == tail) return null;

                    pred.next = curr.next;
                    curr.next = null;
                    return curr;
                } finally {
                    curr.lock.unlock();
                }
            } finally {
                pred.lock.unlock();
            }
        }

        // Inserare sortata Descrescator(Cerinta 1 & 6)
        public void insertSorted(Node nodeToInsert) {
            head.lock.lock();
            Node pred = head;
            try {
                Node curr = pred.next;
                curr.lock.lock();
                try {
                    while (curr != tail) {
                        if (curr.nota < nodeToInsert.nota) {
                            break;
                        }
                        Node oldPred = pred;
                        pred = curr;
                        oldPred.lock.unlock();
                        curr = curr.next;
                        curr.lock.lock();
                    }
                    nodeToInsert.next = curr;
                    pred.next = nodeToInsert;
                } finally {
                    curr.lock.unlock();
                }
            } finally {
                pred.lock.unlock();
            }
        }

        public void writeResults(String filename) throws IOException {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                bw.write("--- LISTA STUDENTI COPIATI (NOTA -1) ---\n");
                List<String> sortedCheaters = new ArrayList<>(cheaters);
                Collections.sort(sortedCheaters); // Sortam alfabetic doar pentru afisare frumoasa
                for (String s : sortedCheaters) {
                    bw.write(s + "\n");
                }

                bw.write("\n--- REZULTATE FINALE (DESCRESCATOR) ---\n");
                Node current = head.next;
                while (current != tail) {
                    bw.write(current.id + "," + current.nota);
                    bw.newLine();
                    current = current.next;
                }
            }
        }
    }

    /**
     * Coada Marginita cu Variabile Conditionale (Cerintele 3 & 4)
     */
    static class BoundedQueue {
        private final LinkedList<Node> queue = new LinkedList<>();
        private final int capacity;
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();
        private final Condition notEmpty = lock.newCondition();
        private boolean producersFinished = false;

        public BoundedQueue(int capacity) {
            this.capacity = capacity;
        }

        public void push(Node node) {
            lock.lock();
            try {
                while (queue.size() == capacity) {
                    try { notFull.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
                queue.addLast(node);
                notEmpty.signal();
            } finally { lock.unlock(); }
        }

        public Node pop() {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    if (producersFinished) return null;
                    try { notEmpty.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return null; }
                }
                Node node = queue.removeFirst();
                notFull.signal();
                return node;
            } finally { lock.unlock(); }
        }

        public void setProducersFinished() {
            lock.lock();
            try {
                producersFinished = true;
                notEmpty.signalAll();
            } finally { lock.unlock(); }
        }
    }

    //Runnable
    static class DatabaseReaderTask implements Runnable {
        private final String tableName;
        private final BoundedQueue queue;

        public DatabaseReaderTask(String tableName, BoundedQueue queue) {
            this.tableName = tableName;
            this.queue = queue;
        }

        @Override
        public void run() {
            String sql = "SELECT student_id, nota FROM " + tableName;
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    String id = rs.getString("student_id").trim();
                    int nota = rs.getInt("nota");
                    queue.push(new Node(id, nota));
                }
            } catch (SQLException e) {
                System.err.println("Eroare citire tabel " + tableName);
                e.printStackTrace();
            }
        }
    }

    // Worker Faza 1: Agregare (Adunare note / Detectie copiat)
    static class WorkerPhase1 implements Runnable {
        private final BoundedQueue queue;
        private final FineGrainedList resultList;

        public WorkerPhase1(BoundedQueue queue, FineGrainedList resultList) {
            this.queue = queue;
            this.resultList = resultList;
        }

        @Override
        public void run() {
            while (true) {
                Node node = queue.pop();
                if (node == null) break;
                resultList.processStudent(node.id, node.nota);
            }
        }
    }

    // Worker Faza 2: Sortare (Mutare noduri in lista finala)
    static class WorkerPhase2 implements Runnable {
        private final FineGrainedList sourceList;
        private final FineGrainedList sortedList;

        public WorkerPhase2(FineGrainedList sourceList, FineGrainedList sortedList) {
            this.sourceList = sourceList;
            this.sortedList = sortedList;
        }

        @Override
        public void run() {
            while (true) {
                Node node = sourceList.extractFirst();
                if (node == null) break;
                node.next = null;
                sortedList.insertSorted(node);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Utilizare: java -cp ... Main paralel <p> <p_r>");
            System.exit(1);
        }

        int p = Integer.parseInt(args[1]);
        int p_r = Integer.parseInt(args[2]);
        int p_w = p - p_r;

        if (p_w <= 0) {
            System.err.println("Eroare: p_w trebuie sa fie > 0");
            System.exit(1);
        }

        System.out.println("Start Lab 5 [Paralel+DB+Bonus]... Readers=" + p_r + ", Workers=" + p_w);
        long start = System.nanoTime();

        FineGrainedList aggregatedList = new FineGrainedList();
        FineGrainedList sortedList = new FineGrainedList();
        BoundedQueue queue = new BoundedQueue(QUEUE_CAPACITY);

        ExecutorService readerExecutor = Executors.newFixedThreadPool(p_r);
        for (int i = 1; i <= NUM_TABLES; i++) {
            readerExecutor.submit(new DatabaseReaderTask(TABLE_PREFIX + i, queue));
        }

        Thread[] workers = new Thread[p_w];
        for (int i = 0; i < p_w; i++) {
            workers[i] = new Thread(new WorkerPhase1(queue, aggregatedList));
            workers[i].start();
        }

        readerExecutor.shutdown();
        try { readerExecutor.awaitTermination(10, TimeUnit.MINUTES); } catch (InterruptedException e) {}

        queue.setProducersFinished();

        for (Thread t : workers) { try { t.join(); } catch (InterruptedException e) {} }

        Thread[] sorters = new Thread[p];
        for (int i = 0; i < p; i++) {
            sorters[i] = new Thread(new WorkerPhase2(aggregatedList, sortedList));
            sorters[i].start();
        }
        for (Thread t : sorters) { try { t.join(); } catch (InterruptedException e) {} }

        sortedList.addAllCheaters(aggregatedList.getCheaters());

        long end = System.nanoTime();
        System.out.printf("Executie completa: %.2f ms\n", (end - start) / 1_000_000.0);

        try {
            sortedList.writeResults(RESULT_FILE);
            System.out.println("Rezultate salvate in " + RESULT_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}