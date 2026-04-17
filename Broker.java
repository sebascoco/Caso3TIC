public class Broker extends Thread {

    private int totalEventos;          // suma de todos los eventos que generarán los sensores
    private BuzonIlimitado buzonEntrada;
    private BuzonIlimitado buzonAlertas;
    private BuzonLimitado  buzonClasificacion;

    public Broker(int totalEventos,
                  BuzonIlimitado buzonEntrada,
                  BuzonIlimitado buzonAlertas,
                  BuzonLimitado  buzonClasificacion) {
        this.totalEventos       = totalEventos;
        this.buzonEntrada       = buzonEntrada;
        this.buzonAlertas       = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < totalEventos; i++) {

                // Retira evento del buzón de entrada (espera pasiva si está vacío)
                Evento evento = buzonEntrada.retirar();

                // Genera número entre 0 y 200 para decidir si es anómalo
                int random = (int)(Math.random() * 201);

                if (random % 8 == 0) {
                    // Evento anómalo → va al buzón de alertas
                    buzonAlertas.depositar(evento);
                    System.out.println("Broker → ALERTA: " + evento);
                } else {
                    // Evento normal → va al buzón de clasificación
                    buzonClasificacion.depositar(evento);
                    System.out.println("Broker → CLASIFICACIÓN: " + evento);
                }
            }

            // Ya procesó todo → manda evento de fin al Administrador
            buzonAlertas.depositar(new Evento());
            System.out.println("Broker terminó. Envió evento de FIN al Administrador.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}