public class Sensor extends Thread {

    private int id;               // identificador del sensor (empieza en 1)
    private int cantidadEventos;  // base * id
    private int ns;               // número de servidores (para el tipo del evento)
    private BuzonIlimitado buzonEntrada;

    public Sensor(int id, int base, int ns, BuzonIlimitado buzonEntrada) {
        this.id             = id;
        this.cantidadEventos = base * id; // enunciado: base × idSensor
        this.ns             = ns;
        this.buzonEntrada   = buzonEntrada;
    }

    @Override
    public void run() {
        for (int i = 1; i <= cantidadEventos; i++) {

            // Identificador único: "idSensor-secuencial"
            String idEvento = id + "-" + i;

            // Tipo aleatorio entre 1 y ns (indica el servidor destino)
            int tipo = (int)(Math.random() * ns) + 1;

            Evento evento = new Evento(idEvento, tipo);

            // El buzón es ilimitado → nunca se bloquea por capacidad
            // pero depositar() es synchronized para evitar condición de carrera
            buzonEntrada.depositar(evento);

            System.out.println("Sensor " + id + " generó: " + evento);
        }
        System.out.println("Sensor " + id + " terminó.");
    }
}