/*
 * 
 */
package HorariosUniversidad;
import java.util.HashMap;

/**
 *
 * @author Samuel Vinas
 */
public class Horario {
    private final HashMap <Integer, Aula> aulas;
    private final HashMap <Integer, Profesor> profesores;
    private final HashMap <Integer, Asignatura> asignaturas;
    private final HashMap <Integer, Grupo> grupos;
    private final HashMap <Integer, Tiempo> tiempos;
    private Clase clases[];
    
    private int numClases = 0;
    
    public Horario(){
        this.aulas       =   new HashMap <Integer, Aula>();
        this.asignaturas =   new HashMap <Integer, Asignatura>();
        this.grupos      =   new HashMap <Integer, Grupo>();
        this.tiempos     =   new HashMap <Integer, Tiempo>();
        this.profesores  =   new HashMap <Integer, Profesor>();
    }
    
    public Horario(Horario clon){
        this.aulas       = clon.getAulas();
        this.asignaturas = clon.getAsignaturas();
        this.profesores  = clon.getProfesores();
        this.grupos      = clon.getGrupos();
        this.tiempos     = clon.getTiempos();
    }

    public HashMap <Integer, Aula> getAulas() {
        return aulas;
    }

    public HashMap <Integer, Profesor> getProfesores() {
        return profesores;
    }

    public HashMap <Integer, Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public HashMap <Integer, Grupo> getGrupos() {
        return grupos;
    }

    public HashMap <Integer, Tiempo> getTiempos() {
        return tiempos;
    }
    
    
    //////////////////////////////////////////////////////////////////////////////////////////
    
    public Aula getAula(int idAula){
        if(!this.aulas.containsKey(idAula)){
            System.out.println("No existe el Aula" + idAula);
        }
        return (Aula) this.aulas.get(idAula);
    }
    public Profesor getProfesor(int idProfesor){
        return (Profesor) this.profesores.get(idProfesor);
    }
    
    public Asignatura getAsignatura(int idAsignatura){
        return (Asignatura) this.asignaturas.get(idAsignatura);
    }
    
    public int[] getGrupoAsignaturas (int idGrupo){
        Grupo grupo = (Grupo) this.grupos.get(idGrupo);
        return grupo.getIdAsignaturas();
    }
    
    public Grupo getGrupo(int idGrupo){
        return (Grupo) this.grupos.get((idGrupo));
    }
    
    
    public Grupo[] getGruposArray() {
        return (Grupo[]) this.grupos.values().toArray(new Grupo[this.grupos.size()]);
    }
    
    public Tiempo getTiempos(int idTiempo){
        return(Tiempo) this.tiempos.get(idTiempo);
    }
    
    public Tiempo getRandomTiempos(){
        Object[] tiemposArray = this.tiempos.values().toArray();
        Tiempo tiempos = (Tiempo) tiemposArray[(int) (tiemposArray.length * Math.random())];
        return tiempos;
    }
    
    /*public Aula getRandomAula(){
        Object[] aulaArreglos = this.aulas.values().toArray();
        Aula aula = (Aula) aulaArreglos[(int) (aulaArreglos.length * Math.random())];
        return aula;
    }*/
    public Profesor getRandomSlotTimesProf(){
        Object[] tiemposProf = this.profesores.values().toArray();
        Profesor timesProf = (Profesor) tiemposProf[(int) (tiemposProf.length * Math.random())];
        return timesProf;
    }
    
    
    public Clase[] getClases(){
        return this.clases;
    }
    
    public int getNumClases(){
        if(this.numClases > 0){
            return this.numClases;
        }
        
        int numClases = 0;
        Grupo grupos[] = (Grupo[]) this.grupos.values().toArray(new Grupo[this.grupos.size()]);
        
        for(Grupo grupo : grupos){
            numClases = numClases + grupo.getIdAsignaturas().length;
        }
        this.numClases = numClases;
   
        return this.numClases;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addAula(int idAula, String nomAula, int capAula){
        this.aulas.put(idAula, new Aula(idAula,nomAula,capAula));
    }
    
    public void addProfessor (int idProfesor, String nomProfesor, int[] timeSlotProf){
        this.profesores.put(idProfesor, new Profesor(idProfesor, nomProfesor, timeSlotProf));
    }
    
    public void addAsignatura(int idAsigantura, String claveAsignatura, String asignatura, int profesorsId[]){
        this.asignaturas.put(idAsigantura, new Asignatura(idAsigantura, claveAsignatura,asignatura,profesorsId));
    }
    
    public void addGrupo(int idGrupo, int tamGrupo, int idModulos[], int idAula){
        this.grupos.put(idGrupo, new Grupo(idGrupo,tamGrupo,idModulos, idAula));
        this.numClases = 0;
    }
    
    
    public void addTimpoEspacio(int idTiempo, String espacioTiempo, int tipoTiempo){
        this.tiempos.put(idTiempo, new Tiempo(idTiempo,espacioTiempo, tipoTiempo));
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public void creaClases(Individuo individuo){
        Clase clases[] = new Clase[this.getNumClases()];
         
        
        int chromosome[] = individuo.getChromosome();
        int chromosomaPos =0;
        int indClase = 0;
        
        for(Grupo grupo: this.getGruposArray()){
            int idAsiganturas[] = grupo.getIdAsignaturas();
            
            for(int idAsigantura : idAsiganturas){
                clases[indClase] = new Clase(indClase, grupo.getIdGrupo(),idAsigantura, grupo.idAula());
                
                //add un espacio de tiempo
                clases[indClase].addTiempo(chromosome[chromosomaPos]);
                chromosomaPos++;
                
                clases[indClase].addProfesor(chromosome[chromosomaPos]);
                chromosomaPos++;
                
                clases[indClase].addTimeSlotProfesor(chromosome[chromosomaPos]);
                chromosomaPos++;
                
                
                indClase++;
                }
        }
        this.clases = clases;
    }
        
    public int calcClases(){
        int empalmes=0;
        
        //verifica la capacidad del aula
        for(Clase claseA : this.clases){
            int capAula = this.getAula(claseA.getIdAula()).getCapacidad();
            int tamGrupo = this.getGrupo(claseA.getIdGrupo()).getTamGrupo();
            if(capAula < tamGrupo){
                empalmes++;
            }
            
            //Verifica si el salon esta libre
            for(Clase claseB: this.clases){
                if(claseA.getIdAula() == claseB.getIdAula() && claseA.getIdTiempo() == claseB.getIdTiempo() && claseA.getIdClase() != claseB.getIdClase())
                {
                    empalmes++;
                    break;
                }
            }
            
            //Verifica que el profesor este disponible
            for(Clase claseB : this.clases){
                if(claseA.getIdProfesor() == claseB.getIdProfesor() && claseA.getIdTiempo() == claseB.getIdTiempo() && claseA.getIdClase() != claseB.getIdClase())
                {
                    empalmes++;
                    break;
                }
            }
            
            /*for(Clase claseC: this.clases){
                int tipoTime = this.getTiempos(claseC.getIdTiempo()).getTipoTiemmpo();
                if(tipoTime == 1){
                    clases++;
                    break;
                }else
                {
                    clases--;
                    break;
                }
            }*/
        }
        return empalmes;
    }
}
