package avengers;

import java.util.ArrayList;

/**
 * Given an adjacency matrix, use a random() function to remove half of the nodes. 
 * Then, write into the output file a boolean (true or false) indicating if 
 * the graph is still connected.
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * PredictThanosSnapInputFile name is passed through the command line as args[0]
 * Read from PredictThanosSnapInputFile with the format:
 *    1. seed (long): a seed for the random number generator
 *    2. p (int): number of people (vertices in the graph)
 *    2. p lines, each with p edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note: the last p lines of the PredictThanosSnapInputFile is an ajacency matrix for
 * an undirected graph. 
 * 
 * The matrix below has two edges 0-1, 0-2 (each edge appear twice in the matrix, 0-1, 1-0, 0-2, 2-0).
 * 
 * 0 1 1 0
 * 1 0 0 0
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * Delete random vertices from the graph. You can use the following pseudocode.
 * StdRandom.setSeed(seed);
 * for (all vertices, go from vertex 0 to the final vertex) { 
 *     if (StdRandom.uniform() <= 0.5) { 
 *          delete vertex;
 *     }
 * }
 * Answer the following question: is the graph (after deleting random vertices) connected?
 * Output true (connected graph), false (unconnected graph) to the output file.
 * 
 * Note 1: a connected graph is a graph where there is a path between EVERY vertex on the graph.
 * 
 * Note 2: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, isConnected is true if the graph is connected,
 *   false otherwise):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(isConnected);
 * 
 * @author Yashas Ravi
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/PredictThanosSnap predictthanossnap.in predictthanossnap.out
*/

public class PredictThanosSnap {
	 
    public static void main (String[] args) {
 
        if ( args.length < 2 ) {
            StdOut.println("Execute: java PredictThanosSnap <INput file> <OUTput file>");
            return;
        }
        
        // Set I/O files
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        // Read input file

        // read seed
        long seed = StdIn.readLong();

        // read number of people
        int p = StdIn.readInt();

        // read adjacency matrix
        int[][] adjMatrix = new int[p][p];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < p; j++) {
                adjMatrix[i][j] = StdIn.readInt();
            }
        }

        // map adjacency matrix to array of arraylists
        ArrayList<Integer>[] adjList = new ArrayList[p];

        for (int i = 0; i < p; i++) {
            adjList[i] = new ArrayList<Integer>();
            for (int j = 0; j < p; j++) {
                if (adjMatrix[i][j] == 1) {
                    adjList[i].add(j);
                }
            }
        }


        // delete random vertices

        // set seed
        StdRandom.setSeed(seed);

        // delete random vertices
        for (int i = 0; i < p; i++) {
            if (StdRandom.uniform() <= 0.5) {
                adjList[i] = null; // set vertex in adjList to null
                for (int j = 0; j < p; j++) {
                    if (adjList[j] != null) {
                        adjList[j].remove((Integer) i); // remove reference to vertex i from adjList
                    }
                }
            }
        }



        // System.out.println(Arrays.toString(adjList));

        // checking for islands
        for(int i = 0; i < adjList.length; i++) {
            if(adjList[i] != null && adjList[i].size() == 0) {
                // System.out.println("Island found at " + i);
                StdOut.print(false);
                return;
            }
        }

        // checking for connectedness
        boolean[] visited = new boolean[p];
        
        // dfs algorithm
        for (int i = 0; i < adjList.length; i++) {
            if (adjList[i] != null) {
                StdOut.println(dfs(adjList, visited, i));
                break;
            }
        }


    }

    public static boolean dfs(ArrayList<Integer>[] adjList, boolean[] visited, int v) {
        visited[v] = true;
        for (int w : adjList[v]) {
            if (!visited[w]) {
                dfs(adjList, visited, w);
            }
        }

        // check if all vertices are visited
        for (int i = 0; i < visited.length; i++) {
            if (adjList[i] != null && !visited[i]) {
                return false;
            }
        }

        return true;
    }

}
