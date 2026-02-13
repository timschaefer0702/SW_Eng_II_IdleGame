public interface Machine extends Runnable{
     void run();
     void stop();
     void upgrade();
     String getName();
     String getType();
     int getLevel();
     void sell();


}
