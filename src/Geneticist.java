import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class Geneticist {

    public String[] alphabetPositions;
    public int generation;

    public Geneticist() {
        alphabetPositions = new String[8];
        alphabetPositions[0] = "A";
        alphabetPositions[1] = "B";
        alphabetPositions[2] = "C";
        alphabetPositions[3] = "D";
        alphabetPositions[4] = "E";
        alphabetPositions[5] = "F";
        alphabetPositions[6] = "G";
        alphabetPositions[7] = "H";

        generation = 0;

        // Starting population
        Individual A = new Individual(1,1,1,1,1,1,1,1);
        Individual B = new Individual(2,2,2,2,2,2,2,2);
        Individual C = new Individual(3,3,3,3,3,3,3,3);
        Individual D = new Individual(4,4,4,4,4,4,4,4);
        Individual E = new Individual(1,2,3,4,1,2,3,4);
        Individual F = new Individual(4,3,2,1,4,3,2,1);
        Individual G = new Individual(1,2,1,2,1,2,1,2);
        Individual H = new Individual(3,4,3,4,3,4,3,4);


        ArrayList<Individual> population = new ArrayList<>();
        population.add(A);
        population.add(B);
        population.add(C);
        population.add(D);
        population.add(E);
        population.add(F);
        population.add(G);
        population.add(H);

        generateNextPopulation(population);
    }

    public void generateNextPopulation(ArrayList<Individual> currPop) {
        // to stop the program after 5 generations
        if (generation < 5) {
            printPopulation(currPop);

            System.out.println("Performing sexual selection... ");
            // ========================  SELECTION  ===========================

            // set the selection chance for each individual in the population
            int overallFitness = determineOverallFitness(currPop);
            for (Individual ind : currPop) {
                ind.setSelectionChance(overallFitness);
            }

            // select parent pairs
            ArrayList<Individual> parentSet1 = getParentSet(currPop);
            ArrayList<Individual> parentSet2 = getParentSet(currPop);
            ArrayList<Individual> parentSet3 = getParentSet(currPop);
            ArrayList<Individual> parentSet4 = getParentSet(currPop);

            // =======================  CROSSOVER  ==============================
            System.out.println("");
            System.out.println("SELECTED PARENTS AND THEIR RESULTING OFFSPRING: ");

            System.out.print("Parent set 1:  ");
            printTwoIndividuals(parentSet1);
            ArrayList<Individual> children1 = crossOver(parentSet1);
            System.out.println("");

            System.out.print("Parent set 2:  ");
            printTwoIndividuals(parentSet2);
            ArrayList<Individual> children2 = crossOver(parentSet2);
            System.out.println("");


            System.out.print("Parent set 3:  ");
            printTwoIndividuals(parentSet3);
            ArrayList<Individual> children3 = crossOver(parentSet3);
            System.out.println("");

            System.out.print("Parent set 4:  ");
            printTwoIndividuals(parentSet4);
            ArrayList<Individual> children4 = crossOver(parentSet4);
            System.out.println("");

            ArrayList<Individual> nextGen = new ArrayList<>();
            nextGen.addAll(children1);
            nextGen.addAll(children2);
            nextGen.addAll(children3);
            nextGen.addAll(children4);

            // =======================  MUTATION  ==============================
            System.out.println("");
            System.out.println("Now mutating children...");
            mutate(nextGen);

            generation++;
            generateNextPopulation(nextGen);
        } else {
            // print final generation
            printPopulation(currPop);
        }
    }

    public ArrayList<Individual> getParentSet (ArrayList<Individual> currPop) {

        // Each individual is placed on a numberline using their selection chance
        // This is used for randomly selecting the parents of the next population
        float[] numberlinePosition = new float[currPop.size()];
        float currNumberlineValue = 0;
        for (int i = 0; i < currPop.size(); i++) {
            currNumberlineValue += currPop.get(i).selectionChance;
            numberlinePosition[i] = currNumberlineValue;
        }


        ArrayList<Individual> parentSet = new ArrayList<>();
        int indexOfFirstParent = -1;
        double randomNum = Math.random();

        for (int i = 0; i < currPop.size(); i++) {
            if (randomNum <= numberlinePosition[i]) {
                parentSet.add(currPop.get(i));
                indexOfFirstParent = i;
                break;
            }
        }

        while (parentSet.size() != 2) {
            randomNum = Math.random();
            for (int i = 0; i < currPop.size(); i++) {
                if (randomNum <= numberlinePosition[i]) {
                    if (i != indexOfFirstParent) {
                        // this ensures that the same individual is not selected for both parents
                        parentSet.add(currPop.get(i));
                        break;
                    }
                }
            }
        }

        return parentSet;
    }

    public ArrayList<Individual> crossOver(ArrayList<Individual> parents) {


        int crossoverPoint = (int) (Math.random() * 7);

        // get the front and back half of the variable assignments for each parent in regard to the crossover point
        SortedMap<String, Integer> frontP1 = parents.get(0).variableAssignments.headMap(alphabetPositions[crossoverPoint + 1]);
        SortedMap<String, Integer> backP1 = parents.get(0).variableAssignments.tailMap(alphabetPositions[crossoverPoint + 1]);
        SortedMap<String, Integer> frontP2 = parents.get(1).variableAssignments.headMap(alphabetPositions[crossoverPoint + 1]);
        SortedMap<String, Integer> backP2 = parents.get(1).variableAssignments.tailMap(alphabetPositions[crossoverPoint + 1]);

        TreeMap<String, Integer> child1Var = new TreeMap<>();
        child1Var.putAll(frontP1);
        child1Var.putAll(backP2);

        TreeMap<String, Integer> child2Var = new TreeMap<>();
        child2Var.putAll(frontP2);
        child2Var.putAll(backP1);

        // create children with the new variable assignments
        Individual child1 = new Individual(child1Var);
        Individual child2 = new Individual(child2Var);

        ArrayList<Individual> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        printCrossOver(children, alphabetPositions[crossoverPoint]);

        return children;
    }

    public void mutate(ArrayList<Individual> pop) {
        for (Individual ind : pop) {

            ind.printVariableAssignments();

            // 30% chance of mutation
            double random = Math.random();
            if (random >= 0.7) {
                int indexToMutate = (int) (Math.random() * 8);
                String letterToMutate = alphabetPositions[indexToMutate];

                int oldValue = ind.variableAssignments.get(letterToMutate);
                int newValue = 0;

                while (newValue == 0 || newValue == oldValue) {
                    newValue = (int) (Math.random() * 4) + 1;
                }

                ind.variableAssignments.put(letterToMutate, newValue);
                System.out.print("was mutated at position " + letterToMutate + ". New genetic code: ");
                ind.printVariableAssignments();
                System.out.println("");
            } else {
                System.out.println("was not mutated");
            }
        }
        System.out.println("");
    }

    public int determineOverallFitness(ArrayList<Individual> pop) {
        int overallFitness = 0;

        for (Individual ind : pop) {
            overallFitness = overallFitness + ind.fitness;
        }

        return overallFitness;
    }
    public void printPopulation(ArrayList<Individual> pop) {
        System.out.println("=============== GENERATION: " + generation + " ===============");

        int overallFitness = determineOverallFitness(pop);

        for (int i = 0; i < pop.size(); i++ ) {
            Individual ind = pop.get(i);
            ind.printVariableAssignments();
            System.out.print("  <=  ");
            ind.printFitnessFunction(overallFitness);
            System.out.println("");
        }
        System.out.println("");
    }

    public void printTwoIndividuals(ArrayList<Individual> pop) {
        Individual p1 = pop.get(0);
        Individual p2 = pop.get(1);

        p1.printVariableAssignments();
        System.out.print(" and  ");
        p2.printVariableAssignments();
    }

    public void printCrossOver(ArrayList<Individual> children, String crossoverPoint) {
        System.out.print(" with a crossover at position " + crossoverPoint );
        System.out.print(" resulting in these children: ");
        printTwoIndividuals(children);
    }
}
