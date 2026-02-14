import java.util.ArrayList;
import java.util.List;

public class LobeMachineFactory implements MachineFactory{

    private List<Machine> lobeMachines = new ArrayList<Machine>();
    private Game game;

    public LobeMachineFactory(Game game) {
        this.game = game;
    }
    @Override
    public List<Machine> getMachines() {
        return this.lobeMachines;
    }

    @Override
    public void addToMachines(Machine machine) {
        this.lobeMachines.add(machine);
    }

    @Override
    public void removeFromMachines(Machine machine) {
        this.lobeMachines.remove(machine);
    }

    @Override
    public Machine createMachine(String machineName) {
        LobeMachine machine = new LobeMachine(machineName, game);
        Thread thread = new Thread(machine);
        thread.setDaemon(true);
        thread.start();
        this.addToMachines(machine);
        return machine;
    }
}
