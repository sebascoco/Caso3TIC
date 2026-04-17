public class Evento {

    // ─── Atributos ───────────────────────────────────────────────

    private String id;     // Identificador único: "sensor1-3" = sensor 1, evento 3
    private int tipo;      // Número entre 1 y ns → indica el servidor destino
    private boolean esFin; // true = señal de terminación, no es un evento real

    // ─── Constructor para eventos REALES (generados por sensores) ─

    public Evento(String id, int tipo) {
        this.id    = id;
        this.tipo  = tipo;
        this.esFin = false; // un evento real nunca es fin por defecto
    }

    // ─── Constructor para eventos de FIN ─────────────────────────
    // El Broker, Administrador y Clasificadores los usan para
    // avisar a los siguientes actores que deben terminar.

    public Evento() {
        this.id    = "FIN";
        this.tipo  = -1;    // -1 indica que no aplica tipo
        this.esFin = true;
    }

    // ─── Getters ──────────────────────────────────────────────────

    public String getId()      { return id; }
    public int    getTipo()    { return tipo; }
    public boolean esEventoFin() { return esFin; }

    // ─── toString (útil para imprimir trazas en consola) ──────────

    @Override
    public String toString() {
        if (esFin) return "[Evento FIN]";
        return "[Evento id=" + id + " tipo=" + tipo + "]";
    }
}