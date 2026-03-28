import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class InputHandler{
    private final Game game;
    private boolean isStartable = true;
    public InputHandler(Game game) {
        this.game = game;
    }

    public void handleInput(String input) {
        if (input == null || input.trim().isEmpty()) return;
        //input command in string array für einfachere verwendung
        String[] args = input.trim().toLowerCase().split("\\s+");
        String command = args[0];

            switch (command) {
                // spiel kann nur aus dem startscreen oder help gestartet werden und kann nur einmal gestartet werdeb
                case "start":
                    if((this.game.guiManager.getState()==GUIManager.GUIState.STARTSCREEN || this.game.guiManager.getState()==GUIManager.GUIState.HELP)&&this.isStartable)
                    {
                        this.game.guiManager.setState(GUIManager.GUIState.DEFAULT);
                        this.game.startGame(args);
                        this.isStartable = false;
                    }else{
                        this.game.guiManager.setCommandReturn("Spiel kann von hier aus nicht gestartet werden.");
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
                //sichten umschalten
                case "help":
                    this.game.guiManager.setState(GUIManager.GUIState.HELP);
                    break;

                case "machines":
                    this.game.guiManager.setState(GUIManager.GUIState.MACHINES);
                    break;

                case "dashboard":
                    this.game.guiManager.setState(GUIManager.GUIState.DEFAULT);
                    break;

                case "sales":
                    this.game.guiManager.setState(GUIManager.GUIState.SALES);
                    break;
                //aktionen im spiel
                case "upgrade":
                    if(game.getMachines().isEmpty()) {
                        this.game.guiManager.setCommandReturn("keine Maschinen zum Upgraden vorhanden!");
                        break;
                    }
                    this.upgrade(args);
                    break;

                case "buy":
                    this.buy(args);
                    break;

                case "sell":
                    if(game.getMachines().isEmpty()) {
                        this.game.guiManager.setCommandReturn("Keine Maschinen zum Verkaufen verfügbar!");
                        break;
                    }
                    this.sell(args);
                    break;

                case "hire":
                    this.hireNewSalesAgent(args);
                    break;

                case "promote":
                    this.promoteSalesAgent(args);
                    break;

                case "fire":
                    this.fireSalesAgent(args);
                    break;

                case "finish":
                    this.game.endGame();
                    break;

                case "expand":
                    this.upgradeWarehouse();
                    break;

                default:
                    this.game.guiManager.setCommandReturn("Kein gültiger Command");
                    break;
            }

    }

    //General

    public void exitGame()
    {
        game.stopGame();
    }

    //Machines

    public synchronized void upgrade(String[] args)
    {
        if (args.length<3) {
            this.game.guiManager.setCommandReturn("Nutze upgrade <type> <name>");
        }else if (!args[1].isEmpty() && this.game.typeList.contains(args[1])) {
            Machine target = this.findMachineWithName(args[2]);
            if (target != null && target.upgrade()) {
                this.game.guiManager.setCommandReturn(target.getType() + " "+ target.getName() + " upgraded!");
            }
        } else{
            this.game.guiManager.setCommandReturn("Schreibfehler in Maschinentyp oder Name");
        }
    }

    public synchronized void buy(String[] args)
    {
        if (args.length<3) {
            this.game.guiManager.setCommandReturn("Nutze buy <type> <name>");
        }else if(!this.game.isMachineNameUnique(args[2])) {
            this.game.guiManager.setCommandReturn("Maschine: " + args[2] + "bereits gekauft!");
        }else if(args[2].equalsIgnoreCase("all")){
            this.game.guiManager.setCommandReturn("Bitte die Maschine nicht \"all\" nennen!");
        } else if (args[1].equalsIgnoreCase("sockmachine")) {
            this.buySockmachine(args[2]);
        } else if (args[1].equalsIgnoreCase("lobemachine")) {
            this.buyLobemachine(args[2]);
        } else{
            this.game.guiManager.setCommandReturn("Schreibfehler in Maschinentyp oder Name");
        }
    }

    public void buySockmachine (String name)
    {
        if(this.game.getCash().compareTo(Definitions.getSockMachinePrice()) > 0)
        {
            this.game.payWithCash(Definitions.getSockMachinePrice());
            Machine machine = this.game.sockMachineFactory.createMachine(name);
            game.getMachines().add(machine);
            this.game.guiManager.setCommandReturn(machine.getType() + " " + machine.getName() + " bought!");
        }else {
            this.game.guiManager.setCommandReturn("Zu wenig Cash! Du benötoigst " + Definitions.getSockMachinePrice() + " Du hast " + this.game.getCash() + " Cash");
        }
    }

    public void buyLobemachine (String name)
    {
        if(this.game.getCash().compareTo(Definitions.getLobeMachinePrice()) > 0)
        {
            this.game.payWithCash(Definitions.getLobeMachinePrice());
            Machine machine = this.game.lobeMachineFactory.createMachine(name);
            game.getMachines().add(machine);
            this.game.guiManager.setCommandReturn(machine.getType() + " " + machine.getName() + " bought!");
        }else{
            this.game.guiManager.setCommandReturn("Zu wenig Cash! Du benötoigst " + Definitions.getLobeMachinePrice() + " Du hast " + this.game.getCash() + " Cash");
        }
    }

    public synchronized void sell (String[] args)
    {
        if (args.length<2) {
            this.game.guiManager.setCommandReturn("Nutze sell <name>/all");
        }
        else if (args[1].contentEquals("all")) {
            if(this.game.getMachines().isEmpty()) {
                this.game.guiManager.setCommandReturn("Keine Maschine zum verkaufen vorhanden");
            }else{
                List<Machine> list = new ArrayList<>();
                for (Machine machine : this.game.getMachines()) {
                    list.add(machine);
                    machine.stop();
                }
                for (Machine machine : list) {
                    machine.sell();
                    this.game.guiManager.setCommandReturn("Alle Maschinen verkauft!");
                }
            }
        }
        else{
            Machine target = this.findMachineWithName(args[1]);
            if (target != null) {
                target.sell();
                this.game.guiManager.setCommandReturn("Maschine " + target.getType() + " " + target.getName() + " verkauft!");
            }
        }
    }

    public Machine findMachineWithName (String name)
    {
        for (Machine m : this.game.getMachines()) {
            if (m.getName() != null && m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    //Sales Agents

    public synchronized void hireNewSalesAgent (String[] args){
        if (args.length < 3) {
            this.game.guiManager.setCommandReturn("Nutze: hire <name> <"+Sock.type+"|"+Lobe.type+">");
            return;
        }
        String name = args[1];
        String fokus = args[2].toLowerCase();
        if (!this.game.isAgentNameUnique(name)) {
            this.game.guiManager.setCommandReturn("Der Mitarbeiter " + name + " ist bereits eingestellt!");
            return;
        }
        if (!this.game.productList.contains(fokus)) {
            this.game.guiManager.setCommandReturn("Ungültiger Fokus! Verfügbar: " + this.game.productList);
            return;
        }
        BigInteger hirePrice = Definitions.getSalesAgentHirePrice();
        if (this.game.getCash().compareTo(hirePrice) >= 0) {
            this.game.payWithCash(hirePrice);
            this.game.hireSalesAgent(name, fokus);
            this.game.guiManager.setCommandReturn("Mitarbeiter " + name + " spezialisiert auf " + fokus + " eingestellt!");
        } else {
            this.game.guiManager.setCommandReturn("Zu wenig Cash! Benötigt: " + hirePrice + "€");
        }
    }

    public synchronized void promoteSalesAgent (String[] args)
    {
        if (args.length < 2) {
            this.game.guiManager.setCommandReturn("Nutze: promote <name>");
            return;
        }
        String name = args[1];
        SalesAgent agent = this.game.findAgentWithName(name);

        if (agent == null) {
            this.game.guiManager.setCommandReturn("Verkäufer '" + name + "' wurde nicht gefunden.");
            return;
        }
        if (agent.getLevel() >= Definitions.getSalesAgentMaxLevel()) {
            this.game.guiManager.setCommandReturn(name + " hat bereits das höchste Level!");
            return;
        }
        BigInteger promoCost = Definitions.getSalesAgentPromotionCost(agent.getLevel());
        if (this.game.getCash().compareTo(promoCost) >= 0) {
            this.game.payWithCash(promoCost);
            agent.promote();
            this.game.guiManager.setCommandReturn(name + " wurde auf Stufe " + agent.getLevel() + " befördert!");
        } else {
            this.game.guiManager.setCommandReturn("Beförderung zu teuer! Kosten: " + promoCost + "€");
        }
    }

    public synchronized void fireSalesAgent(String[] args)
    {
        if (args.length<2) {
            this.game.guiManager.setCommandReturn("Nutze fire <name>/all");
        }
        else if (args[1].contentEquals("all")) {
            if(this.game.getSalesAgents().isEmpty()) {
                this.game.guiManager.setCommandReturn("Kein Mitarbeiter zum feuern vorhanden");
            }else{

                for (SalesAgent agent : this.game.getSalesAgents()) {
                    agent.stop();
                }
                this.game.getSalesAgents().clear();
                this.game.guiManager.setCommandReturn("Alle Mitarbeiter wurden gefeuert!");
            }
        }
        else{
            SalesAgent target = this.game.findAgentWithName(args[1]);
            if (target != null) {
                target.fire();
                this.game.guiManager.setCommandReturn("Mitarbeiter " + target.getName() + " wurde gefeuert!");
            }
        }
    }

    public void upgradeWarehouse()
    {
        BigInteger upgradeCost = Definitions.getWarehouseUpgradeCost(this.game.getWarehouse().getLevel());

        if (this.game.getCash().compareTo(upgradeCost) >= 0) {
            this.game.payWithCash(upgradeCost);

            this.game.getWarehouse().expand();

            this.game.guiManager.setCommandReturn("Lager erweitert! Neue Kapazität: " +
                    this.game.getWarehouse().getCapacity());
        } else {
            this.game.guiManager.setCommandReturn("Zu wenig Cash! Upgrade kostet " + upgradeCost + "€");
        }

    }
}