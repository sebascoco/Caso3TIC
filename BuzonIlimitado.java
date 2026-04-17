import java.util.LinkedList;
import java.util.Queue;

public class BuzonIlimitado {

    private Queue<Evento> cola = new LinkedList<>();
    public synchronized void depositar(Evento evento) {
        cola.add(evento);
        notify();
    }

    // Si la cola está vacía, el thread se duerme (espera pasiva)hasta que alguien deposite y lo despierte con notify()
    public synchronized Evento retirar() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); 
        }
        return cola.poll();
    }

    public synchronized int size() {
        return cola.size();
    }
}