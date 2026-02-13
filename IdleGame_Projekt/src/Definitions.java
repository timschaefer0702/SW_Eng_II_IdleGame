import java.util.ArrayList;
import java.util.List;

public class Definitions {

    public static int getSockMachineProductionSpeed(int index){
        //TODO idee bei verschiedenen Maschinen verschiedene arten des Upgradens mal *0.9 oder so

        List<Integer> sockMachineProdcutionSpeeds = List.of(
                10000,9900,9750,9550,9300,9000,8650,8250,7800,7300,6600,5700,5000,2500,1250,625,312,155,77,37,18,10,5);

        return sockMachineProdcutionSpeeds.get(index);
    }
}
