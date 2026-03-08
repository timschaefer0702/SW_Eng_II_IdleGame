import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class InputHandler{
    private final Game game;

    public InputHandler(Game game) {
        this.game = game;
    }

    public void handleInput(String input) {
        if (input == null || input.trim().isEmpty()) return;

        String[] args = input.trim().toLowerCase().split("\\s+");
        String command = args[0];

            switch (command) {
                case "start":
                    if(this.game.guiManager.getState()==GUIManager.GUIState.STARTSCREEN)
                    {
                        this.game.guiManager.setState(GUIManager.GUIState.DEFAULT);
                        this.game.startGame(args);
                    }
                    break;
                //for test
                case "cheat":
                    System.out.println("Okay du Frechdachs 🦡 dann Schummel mal los");
                    this.game.addToCash(BigInteger.valueOf(1000));
                    break;

                case "stop":
                    exitGame();
                    break;

                case "help":
                    this.game.guiManager.setState(GUIManager.GUIState.HELP);
                    break;

                case "machines":
                    this.game.guiManager.setState(GUIManager.GUIState.MACHINES);
                    break;

                case "dashboard":
                    this.game.guiManager.setState(GUIManager.GUIState.DEFAULT);
                    break;

                case "upgrade":
                    if(game.global_machines.isEmpty()) {
                        System.out.println("Keine Maschinen zum Upgraden verfügbar!");
                        break;
                    }
                    this.upgrade(args);
                    break;

                case "buy":
                    this.buy(args);
                    break;

                case "sell":
                    if(game.global_machines.isEmpty()) {
                        System.out.println("Keine Maschinen zum Verkaufen verfügbar!");
                        break;
                    }
                    this.sell(args);
                    break;

                default:
                    break;
            }

    }

    public void exitGame()
    {
        game.stopGame();

    }



    public synchronized void upgrade(String[] args)
    {
        if (args.length<3) {
            System.out.println("Bitte Art und Name der Maschine eingeben!");
        }else if (!args[1].isEmpty() && this.game.typeList.contains(args[1])) {
            Machine target = this.findMachineWithName(args[2]);
            if (target != null && target.upgrade()) {
                System.out.println(target.getType() + " "+ target.getName() + " upgraded!");
            }
        }
            else{
            System.out.println("Schreibfehler!");
        }
    }

    public synchronized void buy(String[] args)
    {
        if (args.length<3) {
            System.out.println("Bitte Art und Name der Maschine eingeben!");
        }else if(!this.game.isNameUnique(args[2])){
            System.out.println("Es gibt schon eine Maschine mit dem Namen " + args[2] + " !");
        } else if (args[1].equalsIgnoreCase("sockmachine")) {
            this.buySockmachine(args[2]);
        } else if (args[1].equalsIgnoreCase("lobemachine")) {
            this.buyLobemachine(args[2]);
        } else{
            System.out.println("Schreibfehler!");
        }
    }


    public void buySockmachine (String name)
    {
        if(this.game.getCash().compareTo(Definitions.getSockMachinePrice()) > 0)
        {
            this.game.payWithCash(Definitions.getSockMachinePrice());
            Machine machine = this.game.sockMachineFactory.createMachine(name);
            game.global_machines.add(machine);
            System.out.println(machine.getType() + " " + machine.getName() + " bought!");
        }else {
            System.out.println("Zu wenig Cash! Du benötoigst " + Definitions.getSockMachinePrice() + " Du hast " + this.game.getCash() + " Cash");
        }
    }

    public void buyLobemachine (String name)
    {
        if(this.game.getCash().compareTo(Definitions.getLobeMachinePrice()) > 0)
        {
            this.game.payWithCash(Definitions.getLobeMachinePrice());
            Machine machine = this.game.lobeMachineFactory.createMachine(name);
            game.global_machines.add(machine);
            System.out.println(machine.getType() + " " + machine.getName() + " bought!");
        }else{
            System.out.println("Zu wenig Cash! Du benötoigst " + Definitions.getLobeMachinePrice() + " Du hast " + this.game.getCash() + " Cash");
        }
    }

    public synchronized void sell (String[] args)
    {
        if (args.length<2) {
            System.out.println("Bitte Name der Maschine oder all eingeben!");
        }
        else if (args[1].contentEquals("all")) {
            List<Machine> list = new ArrayList<>();
            for (Machine machine : this.game.global_machines) {
                list.add(machine);
                machine.stop();
            }
            for (Machine machine : list) {
                machine.sell();
            }
        }
        else{
            Machine target = this.findMachineWithName(args[1]);
            if (target != null) {
                target.sell();
            }
        }
    }

    public Machine findMachineWithName (String name)
    {
        for (Machine m : this.game.global_machines) {
            if (m.getName() != null && m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
    // TODO VORLÄUFIGER DEADCODE

    public void countProducts(String[] args)
    {
        if(this.game.productList.contains(args[1]))
        {
            if(Sock.type.equals(args[1])){System.out.println(this.game.seeSockID());}
            else if (Lobe.type.equals(args[1])){System.out.println(this.game.seeLobeID());}
        } else {
            System.out.println("Produkt nicht gefunden 😢");

        }
    }

    public void printGameCash(){
        System.out.println(this.game.getCash());
    }

    public String printMachines() {
        List<Machine> list = this.game.global_machines;

        if (list.isEmpty()) {
            return "Keine Maschinen vorhanden.";
        }

        StringBuilder sb = new StringBuilder();
        for (Machine machine : list) {
            sb.append("- Typ: ").append(machine.getType())
                    .append(" | Name: ").append(machine.getName())
                    .append(" | Level: ").append(machine.getLevel())
                    .append("\n");
        }

        return sb.toString();
    }





}