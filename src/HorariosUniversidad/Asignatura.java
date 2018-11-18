/*
 * Abstraccion simple de una asignatura-se asigna la materia un profesor
 */
package HorariosUniversidad;

/**
 *
 * @author Samuel Vinas
 */
public class Asignatura {
    private final int idAsignatura;
    private final String claveAsignatura;
    private final String asignatura;
    private final int profesorIds[];
    
    
    public Asignatura(int idAsignatura, String claveAsignatura, String asignatura, int  profesorIds[]){
        this.idAsignatura = idAsignatura;
        this.claveAsignatura = claveAsignatura;
        this.asignatura = asignatura;
        this.profesorIds = profesorIds;
    }

    public int getIdAsignatura() {
        return this.idAsignatura;
    }

    public String getClaveAsignatura() {
        return this.claveAsignatura;
    }

    public String getAsignatura() {
        return this.asignatura;
    }

    public int[] getProfesorIds() {
        return this.profesorIds;
    }
    
    public int getRandomProfesorId(){
        int profesorId = profesorIds[(int)(profesorIds.length * Math.random())];
        return profesorId;
    }
    
    public static void main(String[] args){
        
        Asignatura x = new Asignatura(1,"x","esp",new int[]{1,2,3});
        String cad="";
        for(int i=0; i<10;i++){
            cad= cad+ x.getRandomProfesorId();
            
        }
        System.out.println(cad);
    }
    
}
