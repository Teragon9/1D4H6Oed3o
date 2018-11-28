package HorariosUniversidad;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Poblacion {
    private Individuo poblacion[];
    private double poblacionFitness = -1;
    private int tamPoblacion;
    private double tasaMutacion;
    private int elitismo;

    public Poblacion(int tamPoblacion) {
        this.poblacion = new Individuo[tamPoblacion];
    }

    public Poblacion(int tamPoblacion, Horario horario) {
        this.poblacion = new Individuo[tamPoblacion];

        for (int individualCount = 0; individualCount < tamPoblacion; individualCount++) {
            Individuo individuo = new Individuo(horario);
            this.poblacion[individualCount] = individuo;
        }
    }

    public Poblacion(int tamPoblacion, int chromosomeLength) {
        this.poblacion = new Individuo[tamPoblacion];

        for (int individualCount = 0; individualCount < tamPoblacion; individualCount++) {
            Individuo individuo = new Individuo(chromosomeLength);
            this.poblacion[individualCount] = individuo;
        }
    }


    public Individuo[] getIndividuos() {
        return this.poblacion;
    }

    public Individuo getFittest(int offset) {
        //se ordenan los individuos
        Arrays.sort(this.poblacion, new Comparator<Individuo>() {
            @Override
            public int compare(Individuo o1, Individuo o2) {
                if (o1.getFitness() > o2.getFitness()) {
                    return -1;
                } else if (o1.getFitness() < o2.getFitness()) {
                    return 1;
                }
                return 0;
            }
        });

        return this.poblacion[offset];
    }

    public void setPoblacionFitness(double fitness) {
        this.poblacionFitness = fitness;
    }

    public double getPoblacionFitness() {
        return this.poblacionFitness;
    }

    public int size() {
        return this.poblacion.length;
    }

    public Individuo setIndividuo(int offset, Individuo individuo) {
        return poblacion[offset] = individuo;
    }

    public Individuo getIndividuo(int offset) {
        return poblacion[offset];
    }

    public void shuffle() {
        Random rnd = new Random();
        for (int i = poblacion.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Individuo a = poblacion[index];
            poblacion[index] = poblacion[i];
            poblacion[i] = a;
        }
    }

    /**
     * Se aplica en Tecnica Adaptativa
     *
     * @return
     */
    public double getAvgFitness() {
        if (this.poblacionFitness == -1) {
            double totalFitness = 0;
            for (Individuo individual : poblacion) {
                totalFitness += individual.getFitness();
            }
            this.poblacionFitness = totalFitness;
        }
        return poblacionFitness / this.size();
    }


}
