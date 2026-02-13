import java.util.ArrayList;
import java.util.List;

public class SockMachineFactory implements MachineFactory{

    private List<Machine> sockMachines = new ArrayList<Machine>();
    private Game game;

    public SockMachineFactory(Game game) {
        this.game = game;
    }

    @Override
    public synchronized List<Machine> getMachines() {
        return sockMachines;
    }

    @Override
    public synchronized void addToMachines(Machine machine) {
        sockMachines.add(machine);
    }

    @Override
    public synchronized void removeFromMachines(Machine machine) {
        sockMachines.remove(machine);
    }

    @Override
    public Machine createMachine(String machineName) {
        SockMachine machine = new SockMachine(machineName, game);
        Thread thread = new Thread(machine);
        thread.setDaemon(true);
        thread.start();
        sockMachines.add(machine);
        return machine;
    }

}
