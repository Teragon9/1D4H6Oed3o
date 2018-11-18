package HorariosUniversidad;



public class AGA {

	private int tamPoblacion;
	private double tasaMutacion;
	private double tasaCruce;
	private int elitismo;
	protected int tamTorneo;

	public AGA(int tamPoblacion, double tasaMutacion, double tasaCruce, int elitismo, int tamTorneo) {

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
         * 
         * @param individuo
         * @param horario
         * @return 
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

