public class Administrador extends Thread {

    private int nc; // num de clasificadores
    private BuzonIlimitado buzonAlertas;
    private BuzonLimitado  buzonClasificacion;

    public Administrador(int nc,
                         BuzonIlimitado buzonAlertas,
                         BuzonLimitado  buzonClasificacion) {
        this.nc                 = nc;
        this.buzonAlertas       = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
    }

    @Override
    public void run() {
        try {
            while (true) {

                // Espera pasiva: se duerme si el buzón de alertas está vacío
                Evento evento = buzonAlertas.retirar();

                // Si es evento de fin se termina
                if (evento.esEventoFin()) {
                    System.out.println("Administrador recibió FIN. Avisando a clasificadores...");

                    // Deposita nc eventos de fin, uno por cada clasificador
                    for (int i = 0; i < nc; i++) {
                        buzonClasificacion.depositar(new Evento());
                    }
                    System.out.println("Administrador terminó.");
                    break;
                }

                // Analiza si el evento es realmente malicioso
                int random = (int)(Math.random() * 21);

                if (random % 4 == 0) {
                    // Inofensivo
                    buzonClasificacion.depositar(evento);
                    System.out.println("Administrador rescató: " + evento);
                } else {
                    // Malicioso
                    System.out.println("Administrador descartó: " + evento);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}