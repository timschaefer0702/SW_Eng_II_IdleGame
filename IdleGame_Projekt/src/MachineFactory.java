import java.util.List;

public interface MachineFactory{

     List<Machine> getMachines();
     void addToMachines(Machine machine);
     void removeFromMachines(Machine machine);
     Machine createMachine(String machineName);


}
