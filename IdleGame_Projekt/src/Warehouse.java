import java.util.concurrent.ConcurrentLinkedDeque;

public class Warehouse {
    private Game game;
    private int capacity;

    public int getLevel() {
        return level;
    }

    private int level;

    public Warehouse(Game game, int capacity) {
        this.game = game;
        this.capacity = capacity;
        this.level = 0;

    }
    private final ConcurrentLinkedDeque<Sock> sockWarehouse = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedDeque<Lobe> lobeWarehouse = new ConcurrentLinkedDeque<>();

    public synchronized boolean hasSockWarehouseSpace() {return sockWarehouse.size() < capacity;}
    public synchronized boolean hasLobeWarehouseSpace() {return lobeWarehouse.size() < capacity;}


    //generelle push und pop funktionen für alle Warehouses egal welches Objekt in welche Liste gepackt wird

    public <T> void push(ConcurrentLinkedDeque<T> stack, T item)
    {
        if(stack != null&& item != null) {stack.push(item);}
    }

    public  <T> T pop(ConcurrentLinkedDeque<T> stack)
    {
        if(stack == null || stack.isEmpty()) {return null;}
        return stack.pop();
    }

    public synchronized void pushSock(Sock sock)
    {
        if(this.hasSockWarehouseSpace())
        {
            this.push(sockWarehouse, sock);
        }else{
            this.game.guiManager.setCommandReturn("LAGER VOLL: "+ Sock.type +"wurde weggeworfen!");
        }

    }
    public synchronized Sock popSock() {return this.pop(sockWarehouse);}

    public synchronized void pushLobe(Lobe lobe)
    {
        if(this.hasLobeWarehouseSpace())
        {
            this.push(lobeWarehouse, lobe);
        }else{
            this.game.guiManager.setCommandReturn("LAGER VOLL: "+ Lobe.type +"wurde weggeworfen!");
        }

    }
    public synchronized Lobe popLobe() {return this.pop(lobeWarehouse);}

    public synchronized ProducedObject popByType(String type) {
        if (Sock.type.equals(type)) {
            return popSock();
        } else if (Lobe.type.equals(type)) {
            return popLobe();
        }
        return null;
    }

    public int getSockStock()
    {
        return sockWarehouse.size();
    }

    public int getLobeStock()
    {
        return lobeWarehouse.size();
    }

    public int getCapacity(){
        return capacity;
    }

    public int getSockUtilization() {
        if (capacity <= 0) return 0;
        return (int) ((double) sockWarehouse.size() / capacity * 100);
    }

    public int getLobeUtilization() {
        if (capacity <= 0) return 0;
        return (int) ((double) lobeWarehouse.size() / capacity * 100);
    }

    public synchronized void expand() {
        this.capacity += 3;
        this.level++;
    }

}
