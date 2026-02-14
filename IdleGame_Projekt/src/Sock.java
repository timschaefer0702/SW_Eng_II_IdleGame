import java.time.LocalDateTime;

public class Sock extends ProducedObject {
    public static String type = "sock";
    private long id;
    private String machineName;
    private final LocalDateTime timeOfCreation;
    public Sock(String machineName){
        this.id = Game.getSockID();
        this.timeOfCreation = LocalDateTime.now();
        this.machineName = machineName;
    }

    public long getId() {
        return id;
    }

    public String toString()
    {
        return "SockNr: " + id + " produced by " + machineName + " at: " + timeOfCreation.getHour() +":"+ timeOfCreation.getMinute() +":"+ timeOfCreation.getSecond();
    }
}
