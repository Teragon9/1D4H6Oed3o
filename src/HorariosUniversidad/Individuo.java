package HorariosUniversidad;

import java.util.Arrays;

public class Individuo {

    private int[] chromosome;
    private double fitness = -1;

    public Individuo(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public Individuo(int chromosomeLength) {
        int[] individual;
        individual = new int[chromosomeLength];

        for (int gene = 0; gene < chromosomeLength; gene++) {
            individual[gene] = gene;
        }
        this.chromosome = individual;
    }

    public Individuo(Horario horario) {
        int numClases = horario.getNumClases();

        int chromosomeLength = numClases * 3;

        int newChromosome[] = new int[chromosomeLength];
        int chromosomeIndex = 0;

        for (Grupo grupo : horario.getGruposArray()) {
            for (int idAsigantura : grupo.getIdAsignaturas()) {

                int idTiempos = horario.getRandomTiempos().getiDTiempo();
                newChromosome[chromosomeIndex] = idTiempos;
                chromosomeIndex++;

                Asignatura asignatura = horario.getAsignatura(idAsigantura);
                int profId = asignatura.getRandomProfesorId();
                newChromosome[chromosomeIndex] = profId;
                chromosomeIndex++;

                int profTime = horario.getProfesor(profId).getRandomTimeSlotProfesor();
                newChromosome[chromosomeIndex] = profTime;
                chromosomeIndex++;
            }
        }

        this.chromosome = newChromosome;
    }


    public int[] getChromosome() {
        return this.chromosome;
    }

    public int getChromosomeLength() {
        return this.chromosome.length;
    }

    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }

    public int getGene(int offset) {
        return this.chromosome[offset];
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return this.fitness;
    }

    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene] + ",";
        }
        return output;
    }

    public boolean containsGene(int gene) {
        for (int i = 0; i < this.chromosome.length; i++) {
            if (this.chromosome[i] == gene) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hash = Arrays.hashCode(this.chromosome);
        return hash;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        Individuo individual = (Individuo) obj;
        return Arrays.equals(this.chromosome, individual.chromosome);
    }
}
