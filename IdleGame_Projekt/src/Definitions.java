import java.math.BigInteger;
import java.util.List;

public class Definitions {

    private static final BigInteger sockMachinePrice = BigInteger.valueOf(3);
    private static final List<Integer> sockMachineProdcutionSpeeds = List.of(
            10000, 9900, 9750, 9550, 9300, 9000, 8650, 8250, 7800, 7300, 6600, 5700, 5000, 2500, 1250, 625, 312, 155, 77, 37, 18, 10, 5);
    private static final List<Integer> sockMachineUpgradeCosts = List.of(
            5, 10, 25, 50, 100, 200, 500, 1000, 2000, 5000, 7500, 10000, 15000, 20000, 25000, 40000, 50000, 75000, 100000, 150000, 200000);
    private static final List<Integer> sockMachineSellingPrices = List.of(
            2, 3, 7, 14, 33, 72, 145, 295, 690, 1480, 6970, 12460, 17450, 24900, 34850, 44444, 64444, 89999, 125000, 175000, 250000, 350000, 500000);

    public static int getSockMachineProductionSpeed(int index)
    {
        return sockMachineProdcutionSpeeds.get(index);
    }

    public static int getSockMachineProductionFinalIndex() {
        return sockMachineProdcutionSpeeds.size() - 1;
    }

    public static int getSockMachineUpgradeCost(int index) {
        return sockMachineUpgradeCosts.get(index);
    }

    public static int getSockMachineUpgradeCostFinalIndex() {
        return sockMachineUpgradeCosts.size() - 1;
    }

    public static BigInteger getSockMachinePrice() {
        return sockMachinePrice;
    }

    public static int getSockMachineSellingPrice(int index) {
        return sockMachineSellingPrices.get(index);
    }

    public static final BigInteger lobeMachinePrice = BigInteger.valueOf(5);

    public static int getLobeMachineMaxLevel() {
        return 30;
    }

    public static BigInteger getLobeMachineUpgradeCost(int currLevel) {
        return BigInteger.valueOf((long) currLevel * currLevel + 2);
    }

    public static BigInteger getLobeMachineSellingPrice(int currLevel) {
        return Definitions.getLobeMachineUpgradeCost(currLevel - 1);
    }

    public static BigInteger getLobeMachinePrice() {
        return lobeMachinePrice;
    }


    private static final BigInteger salesAgentHirePrice = BigInteger.valueOf(10);

    private static final List<Integer> salesAgentSpeeds = List.of(
            5000, 4500, 4000, 3500, 3000, 2500, 2000, 1500, 1000, 750, 500, 250, 100, 50);

    private static final List<BigInteger> salesAgentSalaries = List.of(
            BigInteger.valueOf(2), BigInteger.valueOf(4), BigInteger.valueOf(7),
            BigInteger.valueOf(11), BigInteger.valueOf(16), BigInteger.valueOf(22),
            BigInteger.valueOf(30), BigInteger.valueOf(45), BigInteger.valueOf(65),
            BigInteger.valueOf(90), BigInteger.valueOf(130), BigInteger.valueOf(200),
            BigInteger.valueOf(350), BigInteger.valueOf(600));

    private static final List<BigInteger> salesAgentPromotionCosts = List.of(
            BigInteger.valueOf(20), BigInteger.valueOf(50), BigInteger.valueOf(100),
            BigInteger.valueOf(200), BigInteger.valueOf(400), BigInteger.valueOf(800),
            BigInteger.valueOf(1500), BigInteger.valueOf(3000), BigInteger.valueOf(6000),
            BigInteger.valueOf(12000), BigInteger.valueOf(25000), BigInteger.valueOf(50000),
            BigInteger.valueOf(100000));

    public static BigInteger getSalesAgentHirePrice() {
        return salesAgentHirePrice;
    }

    public static int getSalesAgentSpeed(int level) {
        return salesAgentSpeeds.get(Math.min(level, salesAgentSpeeds.size() - 1));
    }

    public static BigInteger getSalesAgentSalary(int level) {
        return salesAgentSalaries.get(Math.min(level, salesAgentSalaries.size() - 1));
    }

    public static BigInteger getSalesAgentPromotionCost(int level) {
        return salesAgentPromotionCosts.get(Math.min(level, salesAgentPromotionCosts.size() - 1));
    }

    public static int getSalesAgentMaxLevel() {
        return salesAgentSpeeds.size() - 1;
    }

    public static BigInteger getSockPrice() {
        return BigInteger.valueOf(2);
    }

    public static BigInteger getLobePrice() {
        return BigInteger.valueOf(5);
    }

    public static BigInteger getProductPrice(String type) {
        if (Sock.type.equals(type)) return getSockPrice();
        if (Lobe.type.equals(type)) return getLobePrice();
        return BigInteger.ZERO;
    }

    public static BigInteger getWarehouseUpgradeCost(int level) {
        return BigInteger.valueOf(30+ 30L * level);
    }
}
