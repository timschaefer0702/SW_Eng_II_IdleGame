import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputHandler implements Runnable {
    private final Game game;
    private boolean running = false;

    public InputHandler(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        running = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Konsole bereit.Spiel Startet!");

        while (running) {
            String input = scanner.nextLine().toLowerCase();

            String[] args = input.split("\\s+");
            String command = args[0];
            switch (command) {

                case "stop":
                    exitGame();
                    break;

                case "help":
                    //TODO richtige erklärung für Befehle
                    help();
                    break;

                case "cash":
                    printGameCash();
                    break;

                case "machines":
                    printMachines();
                    break;

                case "count":
                    countProducts(args);
                    break;

                case "upgrade":
                    if(game.global_machines.isEmpty()) {
                        System.out.println("Keine Maschinen zum Upgraden verfügbar!");
                        break;
                    }
                    if(this.anfragen("Willst du wirklich diese Maschine upgraden")){this.upgrade(args);};
                    break;

                case "buy":
                    if(this.anfragen("Willst du wirklich diese Maschine kaufen")){this.buy(args);};
                    break;

                case "sell":
                    if(game.global_machines.isEmpty()) {
                        System.out.println("Keine Maschinen zum Verkaufen verfügbar!");
                        break;
                    }
                    if(this.anfragen("Willst du wirklich diese Maschine verkaufen")){this.sell(args);};
                    break;

                default:
                    System.out.println("Unbekannter Befehl. Brauchst du hilfe?");
                    if(this.anfragen("")){this.help();};
                    break;
            }
        }
        scanner.close();
    }

    public void printGameCash(){
        System.out.println(this.game.getCash());
    }

    public void printMachines(){
        List<Machine> list = this.game.global_machines;
        for (Machine machine : list) {
            System.out.println("Maschine vom Typ: "+ machine.getType() + " mit Namen " + machine.getName() + " hat Level: " + machine.getLevel());
        }
    }

    public void exitGame()
    {
        System.out.println("Herunterfahren wird eingeleitet...");
        this.running = false;
        game.stopGame();

    }

    public void help()
    {
        System.out.println("Verfügbare Befehle: status, stop");
    }

    public void countProducts(String[] args)
    {
        if(this.game.productList.contains(args[1]))
        {
            if(Sock.type.equals(args[1])){System.out.println(this.game.seeSockID());}
        } else {
            System.out.println("Produkt nicht gefunden 😢");

            }
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
        }else if (args[1].contentEquals("sockmachine")) {
            this.buySockmachine(args[2]);
        }else{
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

    public boolean anfragen (String botschaft)
    {
        System.out.println(botschaft + "([y]/[n])");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toLowerCase();
        return input.equalsIgnoreCase("y")||input.equalsIgnoreCase("yes")||input.equalsIgnoreCase("ja");
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



}