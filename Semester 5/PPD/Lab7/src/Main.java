import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

class BankAcc{
    private int funds;
    private final int id;

    static private int globalCounter = 0;
    static private ReentrantLock globalLock = new ReentrantLock();

    //un lock simplu s-ar bloca daca ar fi sa intre intr-un thread inca odata
    // asta intre de nspe mii de ori, are un count ce verifica
    private ReentrantLock lock;

    public BankAcc(int funds){
        this.funds = funds;
        lock = new ReentrantLock();
        globalLock.lock();
        this.id = globalCounter++;
        globalLock.unlock();
    }

    public int getFunds(){
        lock.lock();
        int copy = funds;
        lock.unlock();
        return copy;
    }
    //ce o sa retuneze?(intrebare de examen care probabil are mai multe raspunsuri :) )
     boolean  addFunds(int amount){
        lock.lock();
        funds += amount;
        lock.unlock();
        return true;
     }

     boolean removeFunds(int amount){
        lock.lock();
        if(funds - amount < 0){
            lock.unlock();
            return false;
        }
        funds -= amount;
        lock.unlock();
        return true;
     }

     boolean transferFunds(int amount, BankAcc ot) {
         //V1
//        this.lock.lock();
//        //deadlock, in c++ le rezolva separat acele biblioteci
//         // aici trebuie intr o ordine prestabilita trebuie blocate
//         if(funds - amount < 0){
//            lock.unlock();
//            return false;
//        }
//        ot.lock.lock();
//
//        this.removeFunds(amount);
//        ot.addFunds(amount);
//
//        ot.lock.unlock();
//        lock.unlock();
//
//        return true;
         ReentrantLock lock1 = this.lock;
         ReentrantLock lock2 = ot.lock;

         if (ot.id == this.id) {
             return true;
             // return funds>=amount;
         }

         if (ot.id < this.id) {
             lock1 = ot.lock;
             lock2 = this.lock;
         }

         try {
             lock1.lock();
             Thread.sleep(200);
             lock2.lock();
             if (funds < amount) {
                 return false;
             }
             this.removeFunds(amount);
             ot.addFunds(amount);
             return true;
         } catch (InterruptedException e) {
             e.printStackTrace();
         } finally {
             lock1.unlock();
             lock2.unlock();
         }
         return false;
     }
}

class BankAcc2{
    private int funds;
    private final int id;
    static private int globalCounter = 0;

    public BankAcc2(int funds){
        this.funds = funds;
        synchronized (BankAcc2.class) {
            id = globalCounter++;
        }
    }

    synchronized int getFunds(){
        return funds;
    }

    synchronized boolean addFunds(int amount){
        funds += amount;
        return true;
    }

    synchronized boolean removeFunds(int amount){
        if(funds - amount < 0){
            return false;
        }
        funds -= amount;
        return true;
    }

    boolean transferFunds(int amount, BankAcc2 ot) {
        Object l1 =this;
        Object l2 =ot;
        if(ot.id == this.id) {
             return true;
        }
        if(ot.id < this.id) {
            l1 = ot;
            l2 = this;
        }
        synchronized (l1) {
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            synchronized (l2) {
                if(funds<amount){
                    return false;
                }
                this.removeFunds(amount);
                ot.addFunds(amount);
                return true;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BankAcc bank1 = new BankAcc(10);
        BankAcc bank2 = new BankAcc(10);
        CyclicBarrier barrier = new CyclicBarrier(3);
        ArrayList<Thread> threads = new ArrayList<>();
        threads.add(new Thread(()-> {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            bank1.addFunds(15);
        }));
        threads.add(new Thread(()-> {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            bank1.transferFunds(20,bank2);
        }));
        threads.add(new Thread(()-> {
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            bank2.transferFunds(5,bank1);
        }));
        threads.forEach(Thread::start);
        threads.forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Bank 1 account balance is " + bank1.getFunds());
        System.out.println("Bank 2 account balance is " + bank2.getFunds());

        BankAcc2 bank12 = new BankAcc2(10);
        BankAcc2 bank22 = new BankAcc2(10);
        CyclicBarrier barrier2 = new CyclicBarrier(3);
        ArrayList<Thread> threads2 = new ArrayList<>();
        threads2.add(new Thread(()-> {
            try {
                barrier2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            bank12.addFunds(15);
        }));
        threads2.add(new Thread(()-> {
            try {
                barrier2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            bank12.transferFunds(20,bank22);
        }));
        threads2.add(new Thread(()-> {
            try {
                barrier2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            bank22.transferFunds(5,bank12);
        }));
        threads2.forEach(Thread::start);
        threads2.forEach(t->{
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("Bank 1 account balance is " + bank12.getFunds());
        System.out.println("Bank 2 account balance is " + bank22.getFunds());
    }
}