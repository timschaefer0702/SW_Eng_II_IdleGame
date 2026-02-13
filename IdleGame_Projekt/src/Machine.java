public interface Machine extends Runnable{
     void run();
     void stop();
     boolean upgrade();
     String getName();
     String getType();
     int getLevel();
     void sell();
     boolean isUpgradable (int currLevel);


}
