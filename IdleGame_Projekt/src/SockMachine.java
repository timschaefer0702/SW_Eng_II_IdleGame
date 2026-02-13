import java.math.BigInteger;

public class SockMachine implements Machine {
    private boolean running = false;
    private String name;
    private volatile int production_interval;
    private int level;
    private String type = "Sockenmachine";

    private Game game;

    public SockMachine(String name, Game game) {
        this.name = name;
        this.level = 0;
        this.production_interval = Definitions.getSockMachineProductionSpeed(this.level);
        this.game = game;
    }

    @Override
    public void run() {
        this.running = true;
        while (running) {
            try{
                Thread.sleep(production_interval);
                this.game.addToCash(BigInteger.valueOf(1));//ersetzen durch objekte die erzeugt werden
            } catch (Exception e) {
                running = false;
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public void upgrade() {
        //TODO check if upgradable and subtract money
        this.level++;
        this.production_interval = Definitions.getSockMachineProductionSpeed(this.level);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public int getLevel() {
        return this.level;
    }
}
