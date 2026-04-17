public class ServidorDespliegue extends Thread {

    private int id;
    private BuzonLimitado buzonConsolidacion;

    public ServidorDespliegue(int id, BuzonLimitado buzonConsolidacion) {
        this.id                = id;
        this.buzonConsolidacion = buzonConsolidacion;
    }

    @Override
    public void run() {
        try {
            while (true) {

                // Espera pasiva si su buzón está vacío
                Evento evento = buzonConsolidacion.retirar();

                // Si es evento de fin → termina
                if (evento.esEventoFin()) {
                    System.out.println("Servidor " + id + " recibió FIN. Terminando.");
                    break;
                }

                // Simula procesamiento entre 100ms y 1000ms
                int tiempoProcesamiento = 100 + (int)(Math.random() * 901);
                System.out.println("Servidor " + id +
                                   " procesando " + evento +
                                   " (" + tiempoProcesamiento + "ms)");
                Thread.sleep(tiempoProcesamiento);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}