import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class Game implements Runnable {

    private final long startTime;
    private final long durationMillis;
    private boolean running = false;

    //Das ist die Variable, in der das Geld des Spiels gespeichert wird jeder Thread
    public BigInteger global_cash;

    //Dies ist die Liste in der sich alle gekauften Maschinen befinden
    public List<Machine> global_machines = new ArrayList<>();
    public List<String> typeList = List.of(SockMachine.type);
    public List<String> productList = List.of(Sock.type);

    public SockMachineFactory sockMachineFactory = new SockMachineFactory(this);
    private static final AtomicLong sockCounter = new AtomicLong(0);
    public static long getSockID () { return sockCounter.incrementAndGet();}
    public long seeSockID() { return sockCounter.get();}

    private final Warehouse warehouse = new Warehouse(this);





    public Game(long startTime, int minutes) {
        this.startTime = startTime;
        if (minutes <= 0) { minutes = Integer.MAX_VALUE; }
        this.durationMillis = (long) minutes * 60 * 1000;
        global_cash = BigInteger.ZERO;

        System.out.println("Projekt gestartet. Deadline in " + minutes + " Minuten.");
    }

    @Override
    public void run() {

        this.running = true;
        long endTime = startTime + durationMillis;
        if(init()!=null)
        {
            System.out.println("Error occured while initializing game.");
        }

        while (running) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) {
                System.out.println("Deadline erreicht! Der Ingenieur lässt den Stift fallen.");
                running = false;
                break;
            }

            try {
                // 24 ticks / s
                Thread.sleep(42);
            } catch (InterruptedException e) {
                System.err.println("Thread wurde unerwartet unterbrochen!");
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    public Exception init()
    {
        //Konsole wird gestartet und Startobjekte initialisiert
        try {
            SockMachine startMachine = (SockMachine) sockMachineFactory.createMachine("startMachine");
            global_machines.add(startMachine);
            InputHandler konsole = new InputHandler(this);
            Thread konsolenThread = new Thread(konsole);
            konsolenThread.setDaemon(true);
            konsolenThread.start();
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }


    public void stopGame() {
        this.running = false;
    }

    public synchronized BigInteger getCash() {
        return global_cash;
    }

    public synchronized void addToCash(BigInteger cash) {
        global_cash = global_cash.add(cash);
    }

    public synchronized void payWithCash(BigInteger cash) {
        global_cash = global_cash.subtract(cash);
    }
}
