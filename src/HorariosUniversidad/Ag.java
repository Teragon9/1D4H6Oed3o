package HorariosUniversidad;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;


public class Ag {

	private int tamPoblacion;
	private double tasaMutacion;
	private double tasaCruce;
	private int elitismo;
	protected int tamTorneo;

	public Ag(int tamPoblacion, double tasaMutacion, double tasaCruce, int elitismo, int tamTorneo) {

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
         * Calculo por valor Hash
         */
        /*
        private Map<Individuo, Double> fitnessHash = Collections.synchronizedMap(
		new LinkedHashMap<Individuo, Double>(){
			protected boolean removeEldestEntry(Entry<Individuo, Double>eldest){
				return this.size() > 1000;
			}
		});
		
	public double calcFitness (Individuo individuo, Horario horario){
		Double guardarFitness = this.fitnessHash.get(individuo);
		if(guardarFitness != null){
			return guardarFitness;
		}
		
		Horario hiloHorario = new Horario(horario);
		
		hiloHorario.creaClases(individuo);
		
		int clashes = hiloHorario.calcClases();
		
		double fitness = 1 / (double)(clashes + 1) + (0.2/(double)(0+1));
		
		individuo.setFitness(fitness);
		
		this.fitnessHash.put(individuo, fitness);
		return fitness;
	}*/
        
        
        
        
        /**
         * Calculo con AG puro
         */
	public double calcFitness(Individuo individuo, Horario horario) {
		Horario threadTimetable = new Horario(horario);
		threadTimetable.creaClases(individuo);
		int clashes = threadTimetable.calcClases();
		double fitness = (1 / (double) (clashes + 1))+(0.2/ (double)(0+1)) ;
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
        
        
       
        
        /**
         * Procedimientos de Mutacion y Cruce puros
         * @param  poblacion
         * @param horario
         * @return La población mutada
         */
        /*
        public Poblacion mutaPoblacion(Poblacion poblacion, Horario horario) {
            //Inicializar una nueva población
		Poblacion nuevaPoblacion = new Poblacion(this.tamPoblacion);
                    
                //Recorrer la poblacion actual por fitness
		for (int populationIndex = 0; populationIndex < poblacion.size(); populationIndex++) {
			Individuo individuo = poblacion.getFittest(populationIndex);

			// Se crea un nuevo individuo alatorio para intercambiar genes
			Individuo individuoAleatorio = new Individuo(horario);
                        //Recorrer los genes del individuo    
			for (int geneIndex = 0; geneIndex < individuo.getChromosomeLength(); geneIndex++) {
				// Salta la mutacion si hay un individuo de elite
				if (populationIndex > this.elitismo) {
                                    //Si requiere mutación
					if (this.tasaMutacion > Math.random()) {
						// Intercambia por nuevo gen
						individuo.setGene(geneIndex, individuoAleatorio.getGene(geneIndex));
					}
				}
			}

			// se agrega el nuevo individuo a la poblacion
			nuevaPoblacion.setIndividuo(populationIndex, individuo);
		}
		return nuevaPoblacion;
	}
         
        /**
         * 
         * @param poblacion
         * @return La nueva poblacion 
         */
        
	public Poblacion crucePoblacion(Poblacion poblacion) {
            //Crear una nueva población
		Poblacion nuevaPoblacion = new Poblacion(poblacion.size());
   
                //Recorrer la población actual por fitness
		for (int populationIndex = 0; populationIndex < poblacion.size(); populationIndex++) {
                        Individuo padre1 = poblacion.getFittest(populationIndex);
                        //Si el individuo requiere cruce
                        if (this.tasaCruce > Math.random() && populationIndex >= this.elitismo) {
				// Inicia descendencia
				Individuo descendencia = new Individuo(padre1.getChromosomeLength());
				//Encontrar a un segundo padre
				Individuo padre2 = selectPadre(poblacion);
                                //recorrer el genoma
				for (int geneIndex = 0; geneIndex < padre1.getChromosomeLength(); geneIndex++) {
					// Usa mitad de los genes del padre 1 y 2 
					if (0.5 > Math.random()) {
						descendencia.setGene(geneIndex, padre1.getGene(geneIndex));
					} else {
						descendencia.setGene(geneIndex, padre2.getGene(geneIndex));
					}
				}
                                //agregar la nueva descendencia a la población 
				nuevaPoblacion.setIndividuo(populationIndex, descendencia);
			} else {
                            //agregar el nuevo individuo a la poblacion sin aplicar cruce
				nuevaPoblacion.setIndividuo(populationIndex, padre1);
			}
		}
		return nuevaPoblacion;
	}

        /**
         * Tecnica Adaptativa de Mutacion
         * @param poblacion
         * @param horario
         * @return 
         */
        
	public Poblacion mutaPoblacion(Poblacion poblacion, Horario horario){
          Poblacion newPoblacion = new Poblacion(this.tamPoblacion);
          double mejorInd = poblacion.getFittest(0).getFitness();
          
          for(int indPoblacion = 0; indPoblacion<poblacion.size(); indPoblacion++){
              Individuo individuo = poblacion.getFittest(indPoblacion);
              Individuo randomInd = new Individuo(horario);
              double adatativeMutacionTasa = this.tasaMutacion;
              
              if(individuo.getFitness() > poblacion.getAvgFitness()){
                  double fitnesDelta1 = mejorInd - individuo.getFitness();
                  double fitnesDelta2 = mejorInd - poblacion.getAvgFitness();
                  adatativeMutacionTasa = (fitnesDelta1 - fitnesDelta2) *this.tasaMutacion;
                  
              }
              for(int genIndex=0; genIndex <individuo.getChromosomeLength();genIndex++){
                  if(indPoblacion > this.elitismo){
                      if(adatativeMutacionTasa > Math.random()){
                          individuo.setGene(genIndex, randomInd.getGene(genIndex));
                      }
                  }
              }
              newPoblacion.setIndividuo(indPoblacion, individuo);
          }
          return newPoblacion;
      }
        

}

