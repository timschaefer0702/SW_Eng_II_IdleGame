import java.math.BigInteger;

public class LobeMachine implements Machine{
    private boolean running = false;
    private String name;
    private volatile int production_interval;
    private int level;
    public static String type = "lobemachine";

    private Game game;

    public LobeMachine(String name, Game game) {
        this.name = name;
        this.game = game;
        this.level = 0;
        this.production_interval = 10000;
    }

    @Override
    public void run() {
        this.running = true;
        while(running)
        {
            try {
                Thread.sleep(production_interval);
                Lobe lobe = new Lobe(this.name);
                this.game.getWarehouse().pushLobe(lobe);
            } catch (InterruptedException e) {
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
    public synchronized boolean upgrade() {
        if (isUpgradable(this.level))
        {
            this.game.payWithCash(Definitions.getLobeMachineUpgradeCost(this.level));
            this.level++;
            this.production_interval = (int) (this.production_interval * 0.95);
            return true;
        }else {
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
    public void sell() {
        //TODO
    }

    @Override
    public boolean isUpgradable(int currLevel) {
        BigInteger upgradecosts = Definitions.getLobeMachineUpgradeCost(currLevel);
        return (Definitions.getLobeMachineMaxLevel()> this.level && this.game.getCash().compareTo(upgradecosts) > 0);
    }
}
