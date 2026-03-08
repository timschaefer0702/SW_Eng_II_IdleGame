import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Game implements Runnable {

    private long startTime;
    private long durationMillis;
    private boolean running = false;
    public boolean getRunning() {
        return running;
    }

    //Das ist die Variable, in der das Geld des Spiels gespeichert wird jeder Thread
    public BigInteger global_cash;

    //Dies ist die Liste in der sich alle gekauften Maschinen befinden
    public List<Machine> global_machines = new ArrayList<>();
    public List<String> typeList = List.of(SockMachine.type,LobeMachine.type);
    public List<String> productList = List.of(Sock.type,Lobe.type);

    public SockMachineFactory sockMachineFactory = new SockMachineFactory(this);
    public LobeMachineFactory lobeMachineFactory = new LobeMachineFactory(this);
    private static final AtomicLong sockCounter = new AtomicLong(0);
    private static final AtomicLong lobeCounter = new AtomicLong(0);
    public static long getSockID () { return sockCounter.incrementAndGet();}
    public static long getLobeID () { return lobeCounter.incrementAndGet();}
    public long seeSockID() { return sockCounter.get();}
    public long seeLobeID() { return lobeCounter.get();}

    private final Warehouse warehouse = new Warehouse(this);

    public void setTime(int minutes)
    {
        this.startTime = Instant.now().toEpochMilli();
        if (minutes <= 0) { minutes = Integer.MAX_VALUE; }
        this.durationMillis = (long) minutes * 60 * 1000;
        endTime = startTime + durationMillis;
    }

    private long endTime;
    public InputHandler inputHandler;
    public GUIManager guiManager;





    public Game() {
        global_cash = BigInteger.ZERO;
    }

    @Override
    public void run() {

        this.running = true;

        if(this.init()!=null)
        {
            System.out.println("Error occured while initializing game.");
        }

        while (running) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime >= endTime) && this.guiManager.getState() == GUIManager.GUIState.DEFAULT) {
                endGame();
            }

            try {
                // 24 ticks / s
                Thread.sleep(40);
                this.guiManager.renderUI();
                this.guiManager.handleInput();
            } catch (InterruptedException e) {
                System.err.println("Thread wurde unerwartet unterbrochen!");
                running = false;
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public Exception init()
    {
        try {

            inputHandler = new InputHandler(this);
            guiManager = new GUIManager(this);
        } catch (Exception e) {
            return e;
        }
        return null;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }


    public void stopGame() {
        guiManager.closeUI();
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

    public boolean isNameUnique (String name)
    {
        if (name == null) return false;
        for (Machine machine : global_machines) {
            if(name.equalsIgnoreCase(machine.getName())){return false;}
        }
        return true;
    }

    public String getRemainingTime()
    {
        long currentTime = System.currentTimeMillis();
        long diff = endTime - currentTime;
        long totalSeconds = diff / 1000;
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }

    public long getEndTime() {
        return endTime;
    }

    public void endGame() {
        this.guiManager.setState(GUIManager.GUIState.ENDSCREEN);
    }

    public void startGame(String[] args)
    {
        SockMachine startMachine = (SockMachine) sockMachineFactory.createMachine("startMachine");
        global_machines.add(startMachine);
        int minutes = 10;
        if (args.length > 1) {
            try {
                minutes = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
            }
        }
        this.setTime(minutes);

    }




}
