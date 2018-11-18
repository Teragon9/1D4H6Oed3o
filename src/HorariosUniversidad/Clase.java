/*
 * Abstraccion simple de una clase de ensenanza.
 */
package HorariosUniversidad;

/**
 *
 * @author Samuel Viñas
 */
public class Clase {
    private final int idClase;
    private final int idGrupo;
    private final int idAsignatura;
    private int idProfesor;
    private int  idTiempo;
    private int idAula;
    
    public Clase(int idClase, int idGrupo, int idAsignatura, int idAula){
        this.idClase = idClase;
        this.idGrupo = idGrupo;
        this.idAsignatura = idAsignatura;
        this.idAula = idAula;
    }
    
    public void addProfesor(int idProfesor){
        this.idProfesor = idProfesor;
    }
    
    public void addTiempo(int idTiempo){
        this.idTiempo = idTiempo;
    }
    
    public void addIdAula(int idAula){
        this.idAula = idAula;
    }
    
    public void addTimeSlotProfesor(int idTiempo){
        this.idTiempo= idTiempo;
    }

    public int getIdClase() {
        return idClase;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public int getIdAsignatura() {
        return idAsignatura;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public int getIdTiempo() {
        return idTiempo;
    }

    public int getIdAula() {
        return idAula;
    }
    
    public void setAula(int idAula){
        this.idAula = idAula;
        
    }
    
    
}
