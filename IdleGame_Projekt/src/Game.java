public class Game implements Runnable {

    private final long startTime;
    private final long durationMillis;
    private boolean running = false;


    public Game(long startTime, int minutes) {
        this.startTime = startTime;
        this.durationMillis = (long) minutes * 60 * 1000;

        System.out.println("Projekt gestartet. Deadline in " + minutes + " Minuten.");
    }

    @Override
    public void run() {
        this.running = true;
        long endTime = startTime + durationMillis;

        while (running) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= endTime) {
                System.out.println("Deadline erreicht! Der Ingenieur lässt den Stift fallen.");
                running = false;
                break;
            }

            try {
                Thread.sleep(42);
            } catch (InterruptedException e) {
                System.err.println("Thread wurde unerwartet unterbrochen!");
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopGame() {
        this.running = false;
    }
}
