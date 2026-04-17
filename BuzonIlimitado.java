import java.util.LinkedList;
import java.util.Queue;

public class BuzonIlimitado {

    // Cola interna que guarda los eventos en orden FIFO
    private Queue<Evento> cola = new LinkedList<>();

    // ─── DEPOSITAR (usan: Sensores, Broker) ──────────────────────
    // synchronized garantiza que solo un thread a la vez modifica la cola
    public synchronized void depositar(Evento evento) {
        cola.add(evento);
        // Avisa a quien esté esperando que ya hay algo disponible
        notify();
    }

    // ─── RETIRAR (usan: Broker, Administrador) ───────────────────
    // Si la cola está vacía, el thread se duerme (espera pasiva)
    // hasta que alguien deposite y lo despierte con notify()
    public synchronized Evento retirar() throws InterruptedException {
        while (cola.isEmpty()) {
            wait(); // libera el lock y espera → espera PASIVA
        }
        return cola.poll();
    }

    public synchronized int size() {
        return cola.size();
    }
}