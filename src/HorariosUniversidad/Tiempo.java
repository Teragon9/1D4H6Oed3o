/*
 * Abstraccion Simple del espacio del tiempo para las clases 
 */
package HorariosUniversidad;

/**
 *
 * @author Samuel Vinas
 */
public class Tiempo {
    
    private final int idTiempo;
    private final String espacioTiempo;
    private final int tipoTiemmpo;
    
    public Tiempo(int idTiempo, String espacioTiempo,int tipoTiemmpo){
        this.idTiempo      = idTiempo;
        this.espacioTiempo = espacioTiempo;
        this.tipoTiemmpo   = tipoTiemmpo;
    }

    public int getiDTiempo() {
        return this.idTiempo;
    }

    public String getEspacioTiempo() {
        return this.espacioTiempo;
    }

    public int getTipoTiemmpo() {
        return tipoTiemmpo;
    }
    
    
}
