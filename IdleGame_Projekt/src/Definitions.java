import java.math.BigInteger;
import java.util.List;

public class Definitions {
    private static BigInteger sockMachinePrice = BigInteger.valueOf(3);
    private static final List<Integer> sockMachineProdcutionSpeeds = List.of(
            10000,9900,9750,9550,9300,9000,8650,8250,7800,7300,6600,5700,5000,2500,1250,625,312,155,77,37,18,10,5);
    private static final List<Integer> sockMachineUpgradeCosts = List.of(
            5,10,25,50,100,200,500,1000,2000,5000,7500,10000,15000,20000,25000,40000,50000,75000,100000,150000,200000);
    public static int getSockMachineProductionSpeed(int index){
        //TODO idee bei verschiedenen Maschinen verschiedene arten des Upgradens mal *0.9 oder so
        return sockMachineProdcutionSpeeds.get(index);
    }
    public static int getSockMachineProductionFinalIndex(){
        return sockMachineProdcutionSpeeds.size()-1;
    }
    public static int getSockMachineUpgradeCost(int index){
        return sockMachineUpgradeCosts.get(index);
    }
    public static int getSockMachineUpgradeCostFinalIndex(){
        return sockMachineUpgradeCosts.size()-1;
    }
    public static BigInteger getSockMachinePrice(){
        return sockMachinePrice;
    }
}
