import java.math.BigInteger;
import java.time.Instant;

public class SalesAgent implements Runnable {
    private BigInteger generated_cash = BigInteger.ZERO;
    private boolean running = false;
    private String name;
    private volatile int sales_interval;
    private int level;
    public static String type = "verkäufer";
    private String fokus = Sock.type;
    private int sold_products = 0;

    private long hireTime;
    private long lastPaymentTime;
    private static final long PAYMENT_INTERVAL_MS = 30000; // 30 Sekunden

    private Game game;
    private int salary_stress = 0;

    public SalesAgent(String name, Game game, String fokus) {
        this.name = name;
        this.level = 0;
        this.game = game;
        this.sales_interval = Definitions.getSalesAgentSpeed(this.level);
        this.hireTime = System.currentTimeMillis();
        this.lastPaymentTime = this.hireTime;
        this.fokus = fokus;
    }

    @Override
    public void run() {
        this.running = true;
        while (running) {
            try {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPaymentTime >= PAYMENT_INTERVAL_MS) {
                    paySalary();
                    lastPaymentTime = currentTime;
                }
                sellProduct();
                Thread.sleep(sales_interval);

            } catch (Exception e) {
                running = false;
                System.out.println("SalesAgent " + this.name + " hat gekündigt.");
            }
        }
    }

    private void paySalary() {
        BigInteger salary = Definitions.getSalesAgentSalary(this.level);
        if(this.game.getCash().compareTo(salary) >= 0)
        {
            this.game.payWithCash(salary);
            this.game.guiManager.setCommandReturn("Lohn gezahlt: " + this.name + " (-" + salary + "€)");
        }else {
            if(this.salary_stress >=3) {this.stop();this.game.guiManager.setCommandReturn("Verkäufer " + this.name + " hat gekündigt!");}
            else
            {
                this.salary_stress++;
                this.game.guiManager.setCommandReturn("WARNUNG: Konnte Lohn für " + this.name + " nicht zahlen!");
            }
        }
    }

    private void sellProduct() {
        if (attemptSale(this.fokus)) return;

        for (String productType : game.productList) {
            if (productType.equals(this.fokus)) continue;
            if (attemptSale(productType)) return;
        }
    }

    public int getNumberofSoldProducts() {
        return sold_products;
    }

    public void promote()
    {
        this.level++;
        this.sales_interval = Definitions.getSalesAgentSpeed(this.level);
    }

    private boolean attemptSale(String type) {
        ProducedObject item = game.getWarehouse().popByType(type);

        if (item != null) {
            BigInteger price = Definitions.getProductPrice(type);
            game.addToCash(price);
            sold_products++;
            this.generated_cash = generated_cash.add(price);
            return true;
        }
        return false;
    }

    public void stop() { this.running = false; }

    public int getLevel() { return this.level; }
    public String getName() { return this.name; }

    public void fire() {
        this.stop();
        this.game.global_salesAgents.remove(this);
    }

    public BigInteger getGeneratedCash() { return this.generated_cash; }
    public String getFokus() { return this.fokus; }
}