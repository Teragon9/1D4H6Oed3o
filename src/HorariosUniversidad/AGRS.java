package HorariosUniversidad;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;


public class AGRS {

	private int tamPoblacion;
	private double tasaMutacion;
	private double tasaCruce;
	private int elitismo;
	protected int tamTorneo;
        private double temperature = 1.0;
        private double coolingRate =0.001 ;

	public AGRS(int tamPoblacion, double tasaMutacion, double tasaCruce, int elitismo, int tamTorneo) {

		this.tamPoblacion   = tamPoblacion;
		this.tasaMutacion   = tasaMutacion;
		this.tasaCruce      = tasaCruce;
		this.elitismo       = elitismo;
		this.tamTorneo      = tamTorneo;
	}

	public Poblacion iniciaPoblacion(Horario horario) {
		Poblacion poblacion = new Poblacion(this.tamPoblacion, horario);
		return poblacion;
	}

	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	public boolean isTerminationConditionMet(Poblacion poblacion) {
		return poblacion.getFittest(0).getFitness() >= 1.2;
	}

        
        
        /**
         * Calculo con AG puro
         */
	public double calcFitness(Individuo individuo, Horario horario) {
		Horario threadTimetable = new Horario(horario);
		threadTimetable.creaClases(individuo);
		int clashes = threadTimetable.calcClases();
		double fitness = (1 / (double) (clashes + 1)) + (0.2/ (double)(0+1)) ;
		individuo.setFitness(fitness);
		return fitness;
	}
        
        /**
         * Evaluar Poblacion
         * 
         * @param poblacion
         * @param horario 
         */
	public void evalPoblacion(Poblacion poblacion, Horario horario) {
		double poblacionFitness = 0;

		for (Individuo individuo : poblacion.getIndividuos()){
			poblacionFitness += this.calcFitness(individuo, horario);
		}

		poblacion.setPoblacionFitness(poblacionFitness);
	}
        /*
        	public void evalPoblacion(Poblacion poblacion, Horario horario) {
		IntStream.range(0, poblacion.size()).parallel()
		.forEach(i -> this.calcFitness(poblacion.getIndividuo(i),horario));
				
		double poblacionFitness = 0;
		//Lazo sobre la población que evalúa a individuos y sumando Aptitud de la población
		for (Individuo individuo : poblacion.getIndividuos()){
			poblacionFitness += individuo.getFitness();
		}

		poblacion.setPoblacionFitness(poblacionFitness);
	}*/
        
        
        

	public Individuo selectPadre(Poblacion poblacion) {
		// se crea el torneo
		Poblacion torneo = new Poblacion(this.tamTorneo);

		// se agregan los individuos al torneo
		poblacion.shuffle();
		for (int i = 0; i < this.tamTorneo; i++) {
			Individuo resulIndividuo = poblacion.getIndividuo(i);
			torneo.setIndividuo(i, resulIndividuo);
		}

		// se retorna al mejor
		return torneo.getFittest(0);
	}
        
        
      
	
	public Poblacion mutaPoblacion(Poblacion poblacion, Horario horario){
          //Iniciar una nueva poblacion
		  Poblacion newPoblacion = new Poblacion(this.tamPoblacion);
          //Recorrer la poblacion acual por fitness        
          for(int indPoblacion = 0; indPoblacion<poblacion.size(); indPoblacion++){
			  
              Individuo individuo = poblacion.getFittest(indPoblacion);
			  //Crear un individuo aleatorio para intercambiar genes
              Individuo randomInd = new Individuo(horario);                  
              
			  //Recorrer los genes del individuo
              for(int genIndex=0; genIndex <individuo.getChromosomeLength();genIndex++){
				  //Saltar si es un individuo de elite
                  if(indPoblacion > this.elitismo){
					  //Si el gen requiere de mutacion
                      if(this.tasaMutacion * this.getTemperature() > Math.random()){
						  //intercambiar por el nuevo gen
                          individuo.setGene(genIndex, randomInd.getGene(genIndex));
                      }
                  }
              }
			  //Add el nuevo individuo a la poblacion
              newPoblacion.setIndividuo(indPoblacion, individuo);
          }
		  //Retornar poblacion mutada
          return newPoblacion;
      }
        
        public Poblacion crucePoblacion(Poblacion poblacion) {
		//Crear una nueva poblacion
		Poblacion newPoblacion = new Poblacion(this.tamPoblacion);
		
		//Recorrer la poblacion acual por fitness
		for (int populationIndex = 0; populationIndex < poblacion.size(); populationIndex++) {
			
            Individuo padre1 = poblacion.getFittest(populationIndex);
			
			//Si requiere de cruce
            if (this.tasaCruce * this.getTemperature() > Math.random() && populationIndex >= this.elitismo) {
				// Iniciar descendencia
				Individuo descendencia = new Individuo(padre1.getChromosomeLength());
				
				//Seleccionar a un segundo padre
				Individuo padre2 = selectPadre(poblacion);
				
				//Recorrer el genoma
				for (int geneIndex = 0; geneIndex < padre1.getChromosomeLength(); geneIndex++) {
					// Usa mitad de los genes del padre 1 y 2 
					if (0.5 > Math.random()) {
						descendencia.setGene(geneIndex, padre1.getGene(geneIndex));
					} else {
						descendencia.setGene(geneIndex, padre2.getGene(geneIndex));
					}
				}
				//Add la descendencia a la nueva poblacion
				newPoblacion.setIndividuo(populationIndex, descendencia);
			} else {
				//Si no, Add un nuevo individuo sin aplicar cruce
				newPoblacion.setIndividuo(populationIndex, padre1);
			}
		}
		return newPoblacion;
	}
        
         /**
         * Metodos para Tecnica de Enfriamiento Simulado
         */
        public void coolTemperatura(){
            this.temperature = this.temperature * (1 - this.coolingRate);
        }
        
       public double getTemperature() {
            return temperature;
        }
       
        
       

}

