import java.time.LocalDateTime;

public class Lobe extends ProducedObject {
    public static String type = "lobe";
    private long id;
    private String machineName;
    private final LocalDateTime timeOfCreation;

    public Lobe(String machineName){
        this.id = Game.getLobeID();
        this.timeOfCreation = LocalDateTime.now();
        this.machineName = machineName;
    }

    public long getId() {
        return id;
    }

    public String toString()
    {
        return "LobeNr: " + id + " produced by " + machineName + " at: " + timeOfCreation.getHour() +":"+ timeOfCreation.getMinute() +":"+ timeOfCreation.getSecond();
    }
}
