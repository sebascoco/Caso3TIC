import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.load(new FileReader("config.txt"));

        int ni   = Integer.parseInt(props.getProperty("ni"));
        int base = Integer.parseInt(props.getProperty("base"));
        int nc   = Integer.parseInt(props.getProperty("nc"));
        int ns   = Integer.parseInt(props.getProperty("ns"));
        int tam1 = Integer.parseInt(props.getProperty("tam1"));
        int tam2 = Integer.parseInt(props.getProperty("tam2"));

        int totalEventos = 0;
        for (int i = 1; i <= ni; i++) totalEventos += base * i;

        BuzonIlimitado buzonEntrada       = new BuzonIlimitado();
        BuzonIlimitado buzonAlertas       = new BuzonIlimitado();
        BuzonLimitado  buzonClasificacion = new BuzonLimitado(tam1);

        BuzonLimitado[] buzonesConsolidacion = new BuzonLimitado[ns];
        for (int i = 0; i < ns; i++) {
            buzonesConsolidacion[i] = new BuzonLimitado(tam2);
        }

        Sensor[] sensores = new Sensor[ni];
        for (int i = 0; i < ni; i++) {
            sensores[i] = new Sensor(i + 1, base, ns, buzonEntrada);
        }

        Broker broker = new Broker(totalEventos, buzonEntrada,
                                   buzonAlertas, buzonClasificacion);

        Administrador admin = new Administrador(nc, buzonAlertas,
                                                buzonClasificacion);

        Clasificador[] clasificadores = new Clasificador[nc];
        for (int i = 0; i < nc; i++) {
            clasificadores[i] = new Clasificador(i + 1, ns,
                                                 buzonClasificacion, buzonesConsolidacion, nc);
        }

        ServidorDespliegue[] servidores = new ServidorDespliegue[ns];
        for (int i = 0; i < ns; i++) {
            servidores[i] = new ServidorDespliegue(i + 1, buzonesConsolidacion[i]);
        }

        //Iniciar threads
        broker.start();
        admin.start();
        for (Clasificador c : clasificadores) c.start();
        for (ServidorDespliegue s : servidores) s.start();
        for (Sensor s : sensores)              s.start();

        // Esperar a quqe terminen
        for (Sensor s : sensores)              s.join();
        broker.join();
        admin.join();
        for (Clasificador c : clasificadores)  c.join();
        for (ServidorDespliegue s : servidores) s.join();

        System.out.println("\n Sistema IoT terminó correctamente.");
    }
}