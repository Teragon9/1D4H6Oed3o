/*
 * Abstraccion simple de un grupo de estudiantes
 */
package HorariosUniversidad;

/**
 *
 * @author Samuel Vinas
 */
public class Grupo {
    private final int idGrupo;
    private final int tamGrupo;
    private final int idAsignaturas[];
    private final int idAula;
    
    public Grupo(int idGrupo, int tamGrupo, int idAsignaturas[], int idAula){
        this.idGrupo =idGrupo;
        this.tamGrupo = tamGrupo;
        this.idAsignaturas = idAsignaturas;
        this.idAula = idAula;
    }

    public int getIdGrupo() {
        return this.idGrupo;
    }

    public int getTamGrupo() {
        return this.tamGrupo;
    }

    public int[] getIdAsignaturas() {
        return this.idAsignaturas;
    }
    
    public int idAula(){
        return this.idAula;
    }
    
}
