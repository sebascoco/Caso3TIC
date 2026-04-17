public class Clasificador extends Thread {

    private int id;
    private int ns; // número de servidores
    private BuzonLimitado   buzonClasificacion;
    private BuzonLimitado[] buzonesConsolidacion; // un buzón por servidor

    private static int     terminados = 0;
    private static int     totalClasificadores = 0;
    private static Object  lockTerminados = new Object(); // monitor para el contador

    public Clasificador(int id, int ns,
        BuzonLimitado buzonClasificacion,
        BuzonLimitado[] buzonesConsolidacion,
        int totalClasificadores) {
        this.id = id;
        this.ns = ns;
        this.buzonClasificacion = buzonClasificacion;
        this.buzonesConsolidacion = buzonesConsolidacion;
        Clasificador.totalClasificadores = totalClasificadores;
    }

    @Override
    public void run() {
        try {
            while (true) {

                // Espera pasiva si el buzón de clasificación está vacío
                Evento evento = buzonClasificacion.retirar();

                // Si es evento de fin el clasificador termina
                if (evento.esEventoFin()) {
                    System.out.println("Clasificador " + id + " recibió FIN.");
                    verificarUltimoYTerminar();
                    break;
                }

                // Enruta al buzón del servidor correspondiente (tipo va de 1 a ns)
                int indiceServidor = evento.getTipo() - 1; 
                buzonesConsolidacion[indiceServidor].depositar(evento);
                System.out.println("Clasificador " + id +
                                   " enrutó " + evento +  " va a servidor " + evento.getTipo());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //erifica si es el último en terminar
    private void verificarUltimoYTerminar() throws InterruptedException {
        synchronized (lockTerminados) {
            terminados++;
            if (terminados == totalClasificadores) {
                System.out.println("Clasificador " + id +
                                   " es el último. Enviando FIN a servidores...");
                for (BuzonLimitado buzon : buzonesConsolidacion) {
                    buzon.depositar(new Evento());
                }
            }
        }
    }
}