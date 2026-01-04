import java.io.*;
import java.util.*;

public class Main {

    private static final int NUM_FILES = 10;
    private static final String FILE_PREFIX = "proiect";
    private static final String RESULT_FILE = "rezultate.txt";

    static class Node {
        String id;
        int nota;
        Node next;

        public Node(String id, int nota) {
            this.id = id;
            this.nota = nota;
        }
    }

    static class ResultList {
        private Node head;

        public synchronized void addOrUpdate(String id, int nota) {
            if (head == null) {
                head = new Node(id, nota);
                return;
            }

            Node current = head;
            while (current != null) {
                if (current.id.equals(id)) {
                    current.nota += nota;
                    return;
                }

                if (current.next == null) {
                    break;
                }
                current = current.next;
            }

            current.next = new Node(id, nota);
        }

        public void writeToFile(String filename) throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            Node current = head;
            while (current != null) {
                bw.write(current.id + "," + current.nota);
                bw.newLine();
                current = current.next;
            }
            bw.close();
        }
    }

    static class MyQueue {
        private final LinkedList<Node> queue = new LinkedList<>();
        private int activeProducers;

        public MyQueue(int producers) {
            this.activeProducers = producers;
        }

        public synchronized void push(Node node) {
            queue.addLast(node);
            notifyAll();
        }

        public synchronized Node pop() {
            while (queue.isEmpty()) {
                if (activeProducers == 0) {
                    return null;
                }
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return queue.removeFirst();
        }

        public synchronized void decrementProducers() {
            activeProducers--;
            notifyAll();
        }
    }


    static class ReaderTask implements Runnable {
        private final List<String> filesToRead;
        private final MyQueue queue;

        public ReaderTask(List<String> files, MyQueue queue) {
            this.filesToRead = files;
            this.queue = queue;
        }

        @Override
        public void run() {
            for (String fileName : filesToRead) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 2) {
                            String id = parts[0].trim();
                            int nota = Integer.parseInt(parts[1].trim());
                            queue.push(new Node(id, nota));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            queue.decrementProducers();
        }
    }

    static class WorkerTask implements Runnable {
        private final MyQueue queue;
        private final ResultList resultList;

        public WorkerTask(MyQueue queue, ResultList resultList) {
            this.queue = queue;
            this.resultList = resultList;
        }

        @Override
        public void run() {
            while (true) {
                Node node = queue.pop();
                if (node == null) {
                    break;
                }
                resultList.addOrUpdate(node.id, node.nota);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <secvential|paralel> [p] [p_r]");
            System.exit(1);
        }

        String mode = args[0].toLowerCase();
        double duration = 0;

        try {
            if (mode.equals("secvential")) {
                long start = System.nanoTime();
                runSequential();
                long end = System.nanoTime();

                long durationNs = end - start;
                duration = durationNs / 1_000_000.0;

            } else if (mode.equals("paralel")) {
                if (args.length < 3) {
                    System.err.println("Eroare: Modul paralel necesita p si p_r. Ex: java Main paralel 4 2");
                    System.exit(1);
                }
                int p = Integer.parseInt(args[1]);
                int p_r = Integer.parseInt(args[2]);

                long start = System.nanoTime();
                runParallel(p, p_r);
                long end = System.nanoTime();
                long durationNs = end - start;
                duration = durationNs / 1_000_000.0;

            } else {
                System.err.println("Mod necunoscut. Foloseste 'secvential' sau 'paralel'.");
                System.exit(1);
            }

            System.out.printf(java.util.Locale.US, "%.6f%n", duration);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runSequential() throws IOException {
        ResultList seqList = new ResultList();
        for (int i = 1; i <= NUM_FILES; i++) {
            String fileName = FILE_PREFIX + i + ".txt";
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    seqList.addOrUpdate(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                }
            }
        }
        seqList.writeToFile(RESULT_FILE);
    }

    private static void runParallel(int p, int p_r) throws InterruptedException, IOException {
        int p_w = p - p_r;
        if (p_w <= 0) throw new IllegalArgumentException("Numarul de workeri (p - p_r) trebuie sa fie > 0");

        ResultList resultList = new ResultList();
        MyQueue queue = new MyQueue(p_r);

        List<String> allFiles = new ArrayList<>();
        for (int i = 1; i <= NUM_FILES; i++) allFiles.add(FILE_PREFIX + i + ".txt");

        Thread[] threads = new Thread[p];
        int tIdx = 0;

        //readeri
        int filesPerReader = NUM_FILES / p_r;
        int remainder = NUM_FILES % p_r;
        int fileIdx = 0;

        for (int i = 0; i < p_r; i++) {
            int count = filesPerReader + (i < remainder ? 1 : 0);
            List<String> assignedFiles = new ArrayList<>();
            for (int k = 0; k < count; k++) {
                assignedFiles.add(allFiles.get(fileIdx++));
            }
            threads[tIdx++] = new Thread(new ReaderTask(assignedFiles, queue));
        }

        //workeri
        for (int i = 0; i < p_w; i++) {
            threads[tIdx++] = new Thread(new WorkerTask(queue, resultList));
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        resultList.writeToFile(RESULT_FILE);
    }
}