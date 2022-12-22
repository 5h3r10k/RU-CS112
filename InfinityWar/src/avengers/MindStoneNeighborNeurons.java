package avengers;

import java.util.ArrayList;

/**
 * Given a Set of Edges representing Vision's Neural Network, identify all of the 
 * vertices that connect to the Mind Stone. 
 * List the names of these neurons in the output file.
 * 
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * MindStoneNeighborNeuronsInputFile name is passed through the command line as args[0]
 * Read from the MindStoneNeighborNeuronsInputFile with the format:
 *    1. v (int): number of neurons (vertices in the graph)
 *    2. v lines, each with a String referring to a neuron's name (vertex name)
 *    3. e (int): number of synapses (edges in the graph)
 *    4. e lines, each line refers to an edge, each line has 2 (two) Strings: from to
 * 
 * Step 2:
 * MindStoneNeighborNeuronsOutputFile name is passed through the command line as args[1]
 * Identify the vertices that connect to the Mind Stone vertex. 
 * Output these vertices, one per line, to the output file.
 * 
 * Note 1: The Mind Stone vertex has out degree 0 (zero), meaning that there are 
 * no edges leaving the vertex.
 * 
 * Note 2: If a vertex v connects to the Mind Stone vertex m then the graph has
 * an edge v -> m
 * 
 * Note 3: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut:
 *     StdOut.setFile(outputfilename);
 *     //Call StdOut.print() for EVERY neuron (vertex) neighboring the Mind Stone neuron (vertex)
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/MindStoneNeighborNeurons mindstoneneighborneurons.in mindstoneneighborneurons.out
 *
 * @author Yashas Ravi
 * 
 */


public class MindStoneNeighborNeurons {
    
    public static void main (String [] args) {
        
    	if ( args.length < 2 ) {
            StdOut.println("Execute: java MindStoneNeighborNeurons <INput file> <OUTput file>");
            return;
        }

        // Set I/O files
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        // Read number of vertices
        int v = StdIn.readInt();

        // array for storing vertex pairs and # of outgoing edges
        String[][] vertexNames = new String[v][2];

        // Read vertex names
        for (int i = 0; i < v; i++) {
            vertexNames[i][0] = StdIn.readString();
        }

        // System.out.println(Arrays.deepToString(vertexNames));

        // Read number of edges
        int e = StdIn.readInt();

        

        // Read edges
        for (int i = 0; i < e; i++) {
            String from = StdIn.readString();
            String to = StdIn.readString();

            // System.out.println(from + " " + to);

            // find index of from and to in vertexNames
            int fromIndex = -1;
            for (int j = 0; j < v; j++) {
                if (vertexNames[j][0].equals(from)) {
                    fromIndex = j;
                    break;
                }
            }

            // store to in vertexNames
            vertexNames[fromIndex][1] = to;
        }


        // String to store mind stone name
        String mindStone = "";

        // find mind stone name
        for (int i = 0; i < v; i++) {
            if (vertexNames[i][1] == null) {
                mindStone = vertexNames[i][0];
                break;
            }
        }
        

        // System.out.println(mindStone);

        // ArrayList to store vertices that point to mind stone
        ArrayList<String> mindStoneNeighbors = new ArrayList<String>();

        // find vertices that point to mind stone
        for (int i = 0; i < v; i++) {
            if (vertexNames[i][1] != null && vertexNames[i][1].equals(mindStone)) {
                mindStoneNeighbors.add(vertexNames[i][0]);
            }
        }

        // System.out.println(mindStoneNeighbors);

        // print vertices that point to mind stone
        for (int i = 0; i < mindStoneNeighbors.size(); i++) {
            StdOut.println(mindStoneNeighbors.get(i));
        }
        
    }
}
