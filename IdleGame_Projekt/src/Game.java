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
    private BigInteger global_cash;

    //Dies ist die Liste in der sich alle gekauften Maschinen befinden
    private List<Machine> global_machines = new ArrayList<>();
    private List<SalesAgent> global_salesAgents = new ArrayList<>();
    public final List<String> typeList = List.of(SockMachine.type,LobeMachine.type);
    public final List<String> productList = List.of(Sock.type,Lobe.type);

    public SockMachineFactory sockMachineFactory = new SockMachineFactory(this);
    public LobeMachineFactory lobeMachineFactory = new LobeMachineFactory(this);
    private static final AtomicLong sockCounter = new AtomicLong(0);
    private static final AtomicLong lobeCounter = new AtomicLong(0);
    public static long getSockID () { return sockCounter.incrementAndGet();}
    public static long getLobeID () { return lobeCounter.incrementAndGet();}
    public long seeSockID() { return sockCounter.get();}
    public long seeLobeID() { return lobeCounter.get();}

    private final Warehouse warehouse = new Warehouse(this, 5);

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
        //einmal initen dann run in loop
        if(this.init()!=null)
        {
            System.out.println("Error occured while initializing game.");
        }

        while (running) {
            //berechnung wann spiel ausläuft
            long currentTime = System.currentTimeMillis();
            if ((currentTime >= endTime) && this.guiManager.getState() == GUIManager.GUIState.DEFAULT) {
                endGame();
            }
            try {
                // 25 ticks / s
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

    public boolean isMachineNameUnique(String name)
    {
        if (name == null) return false;
        for (Machine machine : this.getMachines()) {
            if(name.equalsIgnoreCase(machine.getName())){return false;}
        }
        return true;
    }

    public boolean isAgentNameUnique(String name)
    {
        if (name == null) return false;
        for (SalesAgent salesAgent : this.getSalesAgents()) {
            if(name.equalsIgnoreCase(salesAgent.getName())){return false;}
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

    public void endGame() {
        this.guiManager.setState(GUIManager.GUIState.ENDSCREEN);
        for (Machine machine : this.getMachines()) {
            if (machine != null) {
                machine.stop();
            }
        }
        for (SalesAgent salesAgent : this.getSalesAgents()) {
            if(salesAgent != null) {
                salesAgent.stop();
            }
        }
    }

    public void startGame(String[] args)
    {
        SockMachine startMachine = (SockMachine) sockMachineFactory.createMachine("start");
        this.getMachines().add(startMachine);
        this.hireSalesAgent("HansUmsatz", Sock.type);
        int minutes = 10;
        if (args.length > 1) {
            try {
                minutes = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
            }
        }
        this.setTime(minutes);
    }

    public void hireSalesAgent(String name, String fokus) {
        SalesAgent salesAgent = new SalesAgent(name,this, fokus);

        Thread thread = new Thread(salesAgent);
        thread.setDaemon(true);
        thread.start();

        this.getSalesAgents().add(salesAgent);
    }

    public SalesAgent findAgentWithName(String name) {
        for (SalesAgent agent : this.getSalesAgents()) {
            if (agent.getName().equalsIgnoreCase(name)) {
                return agent;
            }
        }
        return null;
    }

    public synchronized List<Machine> getMachines() {
        return this.global_machines;
    }

    public synchronized List<SalesAgent> getSalesAgents() {
        return this.global_salesAgents;
    }
}
