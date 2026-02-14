import java.math.BigInteger;

public class SockMachine implements Machine {
    private boolean running = false;
    private String name;
    private volatile int production_interval;
    private int level;
    public static String type = "sockmachine";


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
                Sock sock = new Sock(this.name);
                this.game.getWarehouse().pushSock(sock);
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
    public boolean upgrade() {
        if(this.isUpgradable(this.level))
        {
            game.payWithCash(BigInteger.valueOf(Definitions.getSockMachineUpgradeCost(this.level)));
            this.level++;
            this.production_interval = Definitions.getSockMachineProductionSpeed(this.level);
            return true;
        }
        else {
            System.out.println("Zu wenig Cash! Du benötoigst " + Definitions.getSockMachineUpgradeCost(this.level) + " Du hast " + this.game.getCash() + " Cash");
            return false;
        }

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getType() {
        return type;
    }


    @Override
    public int getLevel() {
        return this.level;
    }


    @Override
    public synchronized void sell() {
        this.game.sockMachineFactory.removeFromMachines(this);
        this.game.global_machines.remove(this);
        this.game.addToCash(BigInteger.valueOf(Definitions.getSockMachineSellingPrice(this.level)));
    }

    @Override
    public boolean isUpgradable(int currLevel) {
        BigInteger upgradecosts = (BigInteger.valueOf(Definitions.getSockMachineUpgradeCost(currLevel)));
        return (this.level < Definitions.getSockMachineProductionFinalIndex()) && (this.game.getCash().compareTo(upgradecosts) > 0);
    }


}
