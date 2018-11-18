package HorariosUniversidad;
/**
 *
 * @author Samuel Viñas
 */
public class Aula {
    private final int idAula;
    private final String nombreAula;
    private final int capacidad;
    
    public Aula(int idAula, String nombreAula, int capacidad){
        this.idAula = idAula;
        this.nombreAula = nombreAula;
        this.capacidad = capacidad;
    }

    public int getIdAula() {
        return this.idAula;
    }

    public String getNombreAula() {
        return this.nombreAula;
    }

    public int getCapacidad() {
        return this.capacidad;
    }
    
    
    
}
