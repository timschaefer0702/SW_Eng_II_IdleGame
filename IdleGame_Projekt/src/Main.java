import java.time.Instant;



public class Main {
    public static void main(String[] args) {
        long currentTime = Instant.now().toEpochMilli();

        //TODO User eingabe wie lang gespielt werden will bevor das Spiel startet
        Game game = new Game(currentTime ,10);

        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}
