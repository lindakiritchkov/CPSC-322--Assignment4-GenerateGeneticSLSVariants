
import java.util.TreeMap;

public class Individual {
    public TreeMap<String, Integer> variableAssignments;
    public int fitness;
    public float selectionChance;

    public Individual(int A, int B, int C, int D, int E, int F, int G, int H) {
        variableAssignments = new TreeMap<>();
        variableAssignments.put("A", A);
        variableAssignments.put("B", B);
        variableAssignments.put("C", C);
        variableAssignments.put("D", D);
        variableAssignments.put("E", E);
        variableAssignments.put("F", F);
        variableAssignments.put("G", G);
        variableAssignments.put("H", H);
        fitness = determineFitness();
    }

    public Individual(TreeMap<String, Integer> variables) {
        variableAssignments = new TreeMap<>();
        variableAssignments.putAll(variables);
        fitness = determineFitness();
    }

    public void setSelectionChance(int overallFitness) {
        this.selectionChance = (float)fitness / (float)overallFitness;
    }

    public void printVariableAssignments() {
        for (String variableLetter : variableAssignments.keySet()) {
            int assignedValue = variableAssignments.get(variableLetter);
            System.out.print(assignedValue + " ");
        }
    }

    public void printFitnessFunction(int overallFitness) {
        setSelectionChance(overallFitness);
        String formattedSelectionChance = String.format("%.2f", (selectionChance * 100));

        System.out.print("fitness = " +  fitness + " (" + formattedSelectionChance + "%)");
    }

    public int determineFitness() {

        // each constraint is represented by one of the following ints
        // int names relate to which 2 variables are involved in the constraint
        int ag = isConstAGSatisfied();
        int ah = isConstAHSatisfied();
        int fb = isConstFBSatisfied();
        int gh = isConstGHSatisfied();
        int gc = isConstGCSatisfied();
        int hc = isConstHCSatisfied();
        int hd = isConstHDSatisfied();
        int dg = isConstDGSatisfied();
        int dc = isConstDCSatisfied();
        int ec = isConstECSatisfied();
        int ed = isConstEDSatisfied();
        int eh = isConstEHSatisfied();
        int gf = isConstGFSatisfied();
        int hf = isConstHFSatisfied();
        int cf = isConstCFSatisfied();
        int df = isConstDFSatisfied();
        int ef = isConstEFSatisfied();

        return  ag +
                ah +
                fb +
                gh +
                gc +
                hc +
                hd +
                dg +
                dc +
                ec +
                ed +
                eh +
                gf +
                hf +
                cf +
                df +
                ef;
    }

    /*

    ***************** CONSTRAINT HELPER FUNCTIONS ******************

    - Each isConstXYSatisfied helper function below first checks
      if both variables have been assigned.

    - If either has not yet been assigned, then 0 will be returned,
      indicating that the constraint is not satisfied

    - If both variables have been assigned, the function will check if
      the constraint is satisfied.

    */

    // A > G
    private int isConstAGSatisfied() {
        Integer A = variableAssignments.get("A");
        Integer G = variableAssignments.get("G");
        if (A == null || G == null) {
            return 0;
        }

        return A > G ? 1 : 0;
    }

    // A <= H
    private int isConstAHSatisfied() {
        Integer A = variableAssignments.get("A");
        Integer H = variableAssignments.get("H");
        if (A == null || H == null) {
            return 0;
        }

        return A <= H ? 1 : 0;
    }

    // |F-B| = 1
    private int isConstFBSatisfied() {
        Integer F = variableAssignments.get("F");
        Integer B = variableAssignments.get("B");
        if (F == null || B == null) {
            return 0;
        }

        return Math.abs(F - B) == 1 ? 1 : 0;
    }

    // G < H
    private int isConstGHSatisfied() {
        Integer H = variableAssignments.get("H");
        Integer G = variableAssignments.get("G");
        if (H == null || G == null) {
            return 0;
        }

        return G < H ? 1 : 0;
    }

    // |G - C| = 1
    private int isConstGCSatisfied() {
        Integer C = variableAssignments.get("C");
        Integer G = variableAssignments.get("G");
        if (C == null || G == null) {
            return 0;
        }

        return Math.abs(G - C) == 1 ? 1 : 0;
    }

    // |H -C| is even
    private int isConstHCSatisfied() {
        Integer H = variableAssignments.get("H");
        Integer C = variableAssignments.get("C");
        if (H == null || C == null) {
            return 0;
        }
        return Math.abs(H - C) % 2 == 0 ? 1 : 0;
    }

    // H != D
    private int isConstHDSatisfied() {
        Integer H = variableAssignments.get("H");
        Integer D = variableAssignments.get("D");
        if (H == null || D == null) {
            return 0;
        }

        return H != D ? 1 : 0;
    }

    // D ≥ G
    private int isConstDGSatisfied() {
        Integer D = variableAssignments.get("D");
        Integer G = variableAssignments.get("G");
        if (D == null || G == null) {
            return 0;
        }
        return D >= G ? 1 : 0;
    }


    // D != C
    private int isConstDCSatisfied() {
        Integer D = variableAssignments.get("D");
        Integer C = variableAssignments.get("C");
        if (D == null || C == null) {
            return 0;
        }
        return D != C ? 1 : 0;
    }

    // E != C
    private int isConstECSatisfied() {
        Integer E = variableAssignments.get("E");
        Integer C = variableAssignments.get("C");
        if (E == null || C == null) {
            return 0;
        }
        return E != C ? 1 : 0;
    }

    // E < D - 1
    private int isConstEDSatisfied() {
        Integer D = variableAssignments.get("D");
        Integer E = variableAssignments.get("E");
        if (E == null || D == null) {
            return 0;
        }
        return E < D - 1 ? 1 : 0;
    }

    // E != H - 2
    private int isConstEHSatisfied() {
        Integer E = variableAssignments.get("E");
        Integer H = variableAssignments.get("H");
        if (E == null || H == null) {
            return 0;
        }
        return E != H - 2 ? 1 : 0;
    }

    // G != F
    private int isConstGFSatisfied() {
        Integer F = variableAssignments.get("F");
        Integer G = variableAssignments.get("G");
        if (G == null || F == null) {
            return 0;
        }
        return G != F ? 1 : 0;
    }

    // H != F
    private int isConstHFSatisfied() {
        Integer F = variableAssignments.get("F");
        Integer H = variableAssignments.get("H");
        if (H == null || F == null) {
            return 0;
        }
        return H != F ? 1 : 0;
    }

    // C != F
    private int isConstCFSatisfied() {
        Integer C = variableAssignments.get("C");
        Integer F = variableAssignments.get("F");
        if (C == null || F == null) {
            return 0;
        }
        return C != F ? 1 : 0;
    }

    // D != F - 1
    private int isConstDFSatisfied() {
        Integer F = variableAssignments.get("F");
        Integer D = variableAssignments.get("D");
        if (D == null || F == null) {
            return 0;
        }
        return D != F - 1 ? 1 : 0;
    }

    // |E-F| is odd
    private int isConstEFSatisfied() {
        Integer E = variableAssignments.get("E");
        Integer F = variableAssignments.get("F");
        if (E == null || F == null) {
            return 0;
        }
        return Math.abs(E - F) % 2 == 1 ? 1 : 0;
    }
}
