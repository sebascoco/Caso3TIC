import java.util.LinkedList;
import java.util.Queue;

public class BuzonLimitado {

    private Queue<Evento> cola = new LinkedList<>();
    private int capacidad; // tam1 o tam2 según el buzón

    public BuzonLimitado(int capacidad) {
        this.capacidad = capacidad;
    }

    // ─── DEPOSITAR (usan: Broker, Administrador, Clasificadores) ─
    // Si está lleno, el productor espera hasta que alguien retire
    public synchronized void depositar(Evento evento) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait(); // buzón lleno → espera PASIVA del productor
        }
        cola.add(evento);
        notifyAll(); // despierta a consumidores que esperaban
    }

    // ─── RETIRAR (usan: Clasificadores, Servidores) ───────────────
    // Si está vacío, el consumidor espera hasta que alguien deposite
    public synchronized Evento retirar() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); // buzón vacío → espera PASIVA del consumidor
        }
        Evento e = cola.poll();
        notifyAll(); // despierta a productores que esperaban
        return e;
    }

    public synchronized int size() {
        return cola.size();
    }
}