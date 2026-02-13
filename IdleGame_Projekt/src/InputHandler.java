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

                case "upgrade":
                    if(game.global_machines.isEmpty()) {
                        break;
                    }
                    this.upgrade(args);

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
            List<Machine> sockmachines = this.game.sockMachineFactory.getMachines();
            SockMachine target = (SockMachine) sockmachines.stream()
                    .filter(m -> m.getName().equalsIgnoreCase(args[2]))
                    .findFirst()
                    .orElse(null);
            if (target != null) {
                target.upgrade();
                System.out.println("Machine " + target.getName() + " upgraded!");
            }else {
                System.out.println("Fehler!");
            }
        } else if (args[1].contentEquals("schmarnmachine")) {
            
        } else{
            System.out.println("Schreibfehler!");
        }
    }


}