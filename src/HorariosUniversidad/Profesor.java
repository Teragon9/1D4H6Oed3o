/*
 * Abstraccion simple de un profesor.
 */
package HorariosUniversidad;

/**
 *
 * @author Samuel Vinas
 */
public class Profesor {
    
    private final int idProfesor;
    private final String nomProfesor;
    private final int[] idTimeSlotProfesor;
    
    
    
   /*public Profesor(int idProfesor, String nomProfesor){
        this.idProfesor = idProfesor;
        this.nomProfesor = nomProfesor;
    }*/
    
    public Profesor(int idProfesor, String nomProfesor, int [] idTimeSlotProfesor){
        this.idProfesor = idProfesor;
        this.nomProfesor = nomProfesor;
        this.idTimeSlotProfesor = idTimeSlotProfesor;
    }
    
    

    public int getIdProfesor() {
        return this.idProfesor;
    }

    public String getNomPorfesor() {
        return this.nomProfesor;
    }

    public int[] getidTimeSlotProfesor() {
        return idTimeSlotProfesor;
    }
    
    public int getRandomTimeSlotProfesor(){
        int timeProfesorId = idTimeSlotProfesor[(int)(idTimeSlotProfesor.length * Math.random())];
        return timeProfesorId;
    }
        
    public static void main(String[] args){
        
        Profesor x = new Profesor(1,"esp",new int[]{1,2,3,4,5,6,7,8});
        String cad="";
        for(int i=0; i<10;i++){
            cad= cad+ x.getRandomTimeSlotProfesor();
            
        }
        System.out.println(cad);
    }
}
