public class Evento {

//Atributos
    private String id;     // Identificador unico del sensor 1, evento 3
    private int tipo;      // Número entre 1 y ns que indica el servidor destino
    private boolean esFin; // true = señal de terminación

    //Constructor
    public Evento(String id, int tipo) {
        this.id    = id;
        this.tipo  = tipo;
        this.esFin = false; 
    }
    //Constructor de eventos de fin
    // El Broker, Administrador y Clasificadores los usan para avisar a los siguientes actores que deben terminar.

    public Evento() {
        this.id    = "FIN";
        this.tipo  = -1;
        this.esFin = true;
    }

    public String getId()      { return id; }
    public int    getTipo()    { return tipo; }
    public boolean esEventoFin() { return esFin; }

    @Override
    public String toString() {
        if (esFin) return "[Evento FIN]";
        return "[Evento id=" + id + " tipo=" + tipo + "]";
    }
}