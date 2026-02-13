import java.math.BigInteger;
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
                //TODO Hier Game und System controls einfügen

                case "stop":
                    exitGame();
                    break;

                case "help":
                    help();
                    break;

                case "cash":
                    printGameCash();
                    break;

                case "machines":
                    printMachines();
                    break;

                case "upgrade":
                    if(game.global_machines.isEmpty()) {
                        break;
                    }
                    this.upgrade(args);
                    break;

                case "buy":
                    this.buy(args);
                    break;

                default:
                    System.out.println("Unbekannter Befehl. Der Ingenieur schüttelt den Kopf.");
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

    public synchronized void upgrade(String[] args)
    {
        if (args.length<3) {
            System.out.println("Bitte Art und Name der Maschine eingeben!");
        }else if (args[1].contentEquals("sockmachine")) {
            this.upgradeSockmachine(args[2]);

        } else if (args[1].contentEquals("schmarnmachine")) {

        } else{
            System.out.println("Schreibfehler!");
        }
    }

    public synchronized void buy(String[] args)
    {
        //TODO geldwert einbauen
        if (args.length<3) {
            System.out.println("Bitte Art und Name der Maschine eingeben!");
        }else if (args[1].contentEquals("sockmachine")) {
            this.buySockmachine(args[2]);
        }else{
            System.out.println("Schreibfehler!");
        }
    }

    public void upgradeSockmachine (String name)
    {
        List<Machine> sockmachines = this.game.sockMachineFactory.getMachines();
        SockMachine target = (SockMachine) sockmachines.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (target != null) {
            target.upgrade();
            System.out.println("Sockmachine " + target.getName() + " upgraded!");
        }else {
            System.out.println("Fehler!");
        }
    }

    public void buySockmachine (String name)
    {
       Machine machine = this.game.sockMachineFactory.createMachine(name);
       game.global_machines.add(machine);
        System.out.println("Sockmachine " + machine.getName() + " bought!");
    }



}