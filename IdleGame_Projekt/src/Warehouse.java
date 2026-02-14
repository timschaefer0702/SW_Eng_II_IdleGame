import java.util.concurrent.ConcurrentLinkedDeque;

public class Warehouse {
    private Game game;

    public Warehouse(Game game) {
        this.game = game;
    }
    private final ConcurrentLinkedDeque<Sock> sockWarehouse = new ConcurrentLinkedDeque<>();



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
        this.push(sockWarehouse, sock);
    }
    public synchronized Sock popSock()
    {
        return this.pop(sockWarehouse);
    }
}
