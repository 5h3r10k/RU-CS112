package avengers;

// import java.util.Arrays;

/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

        // read file names from command line
        String locateTitanInputFile = args[0];
        String locateTitanOutputFile = args[1];

        // Set the input and output files.
        StdIn.setFile(locateTitanInputFile);
        StdOut.setFile(locateTitanOutputFile);


        // READ INPUT FILE DATA

        // Step 1: read the number of generators
        int numGen = StdIn.readInt();

        // Step 2: read the functionality values of each generator
        double[] genFunc = new double[numGen];
        for (int i = 0; i < numGen; i++) {
            genFunc[StdIn.readInt()] = StdIn.readDouble();
        }
        // System.out.println(Arrays.toString(genFunc));

        // Step 3: read the adjacency matrix
        int[][] adjMatrix = new int[numGen][numGen];
        for (int i = 0; i < numGen; i++) {
            for (int j = 0; j < numGen; j++) {
                adjMatrix[i][j] = StdIn.readInt();
            }
        }
        // System.out.println(Arrays.deepToString(adjMatrix));


        // UPDATE THE ADJACENCY MATRIX

        // change EVERY edge weight by dividing it by the functionality of BOTH vertices that the edge points to
        for (int i = 0; i < numGen; i++) {
            for (int j = 0; j < numGen; j++) {
                adjMatrix[i][j] = (int) (adjMatrix[i][j] / (genFunc[i] * genFunc[j]));
            }
        }
        // System.out.println(Arrays.deepToString(adjMatrix));


        // FIND THE MINIMUM COST TO TRAVEL FROM EARTH TO TITAN
        // use Dijkstra's Algorithm to find the path of minimum cost between Earth (vertex 0) and Titan (vertex numGen-1)

        // init minCost and visited arrays
        int[] minCost = new int[numGen];
        boolean[] visited = new boolean[numGen];

        // initialize minCost array with Integer.MAX_VALUE apart from vertex 0
        minCost[0] = 0;
        for (int i = 1; i < numGen; i++) minCost[i] = Integer.MAX_VALUE;

        // initialize visited array with false
        for (int i = 0; i < numGen; i++) visited[i] = false;

        // loop through all vertices
        for (int i = 0; i < numGen; i++) {

            // find the vertex with the minimum cost that has not been visited
            int min = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < numGen; j++) {
                if (!visited[j] && minCost[j] < min) {
                    min = minCost[j];
                    minIndex = j;
                }
            }

            // mark the vertex with the minimum cost as visited
            visited[minIndex] = true;

            // update the minCost array
            for (int j = 0; j < numGen; j++) {
                if (!visited[j] && adjMatrix[minIndex][j] != 0 && minCost[minIndex] != Integer.MAX_VALUE && minCost[minIndex] + adjMatrix[minIndex][j] < minCost[j]) {
                    minCost[j] = minCost[minIndex] + adjMatrix[minIndex][j];
                }
            }
        }

        // print the minimum cost to travel from Earth to Titan
        StdOut.print(minCost[numGen-1]);

    }
}
