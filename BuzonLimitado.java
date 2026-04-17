import java.util.LinkedList;
import java.util.Queue;

public class BuzonLimitado {

    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad; 

    public BuzonLimitado(int capacidad) {
        this.capacidad = capacidad;
    }

    public synchronized void depositar(Evento evento) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait();
        }
        cola.add(evento);
        notifyAll();
    }

    public synchronized Evento retirar() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); 
        }
        Evento e = cola.poll();
        notifyAll(); // despierta a productores que esperaban
        return e;
    }

    public synchronized int size() {
        return cola.size();
    }
}