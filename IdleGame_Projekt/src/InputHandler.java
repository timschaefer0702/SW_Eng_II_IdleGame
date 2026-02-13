import java.math.BigInteger;
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

            switch (input) {
                //TODO Hier Game und System controls einfügen
                case "status":
                    // Hier rufen wir eine Methode im Game-Thread auf
                    System.out.println("Ingenieur-Bericht: Alles im grünen Bereich (vielleicht).");
                    break;

                case "stop":
                    System.out.println("Herunterfahren wird eingeleitet...");
                    game.stopGame(); // Wir steuern den anderen Thread
                    this.running = false;
                    break;

                case "help":
                    System.out.println("Verfügbare Befehle: status, stop");
                    break;

                case "cash":
                    printGameCash();
                    break;

                case "upgrade":

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
}