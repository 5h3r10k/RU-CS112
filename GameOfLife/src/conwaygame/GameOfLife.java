package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        int rows = StdIn.readInt();
        int cols = StdIn.readInt();
        grid = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = StdIn.readBoolean();
                if (grid[i][j]) {
                    totalAliveCells++;
                }
            }
        }

    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        //return true; // update this line, provided so that code compiles
        return grid[row][col];
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        //return false; // update this line, provided so that code compiles
        for(boolean[] row : grid){
            for(boolean cell : row){
                if(cell == ALIVE){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        // WRITE YOUR CODE HERE
        //return 0; // update this line, provided so that code compiles
        int aliveNeighbors = 0;
        
        //checks the 9x9 grid around the cell, including the cell itself
        for(int i = row - 1; i <= row + 1; i++){
            int r = i;
            if(r<0) r+=grid.length; //if the row is out of bounds, wrap around
            if(r>=grid.length) r-=grid.length; //if the row is out of bounds, wrap around
            for(int j = col - 1; j <= col + 1; j++){
                int c = j;
                if(c<0) c+=grid[0].length; //if the column is out of bounds, wrap around
                if(c>=grid[0].length) c-=grid[0].length; //if the column is out of bounds, wrap around
                if(grid[r][c] == ALIVE){
                    aliveNeighbors++;
                }
            }
        }

        //if the cell itself is alive, subtract 1 from the total
        //since we counted it in the loop and it is not a neighbor to itself
        if(grid[row][col] == ALIVE){
            aliveNeighbors--;
        }

        return aliveNeighbors;
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE
        //return new boolean[1][1];// update this line, provided so that code compiles
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];

        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                int aliveNeighbors = numOfAliveNeighbors(i, j);
                if(grid[i][j] == ALIVE){
                    if(aliveNeighbors < 2 || aliveNeighbors > 3){
                        newGrid[i][j] = DEAD;
                    }else{
                        newGrid[i][j] = ALIVE;
                    }
                }else{
                    if(aliveNeighbors == 3){
                        newGrid[i][j] = ALIVE;
                    }else{
                        newGrid[i][j] = DEAD;
                    }
                }
            }
        }

        return newGrid;
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
    
        // WRITE YOUR CODE HERE
        grid = computeNewGrid();
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for(int i = 0; i < n; i++){
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        //return 0; // update this line, provided so that code compiles

        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(grid.length , grid[0].length);

        for(int row=0;row<grid.length;row++){

            for(int col=0;col<grid[row].length;col++){
                if(grid[row][col]==DEAD) continue;
                for(int i = row - 1; i <= row + 1; i++){
                    int r = i;
                    if(r<0) r+=grid.length; //if the row is out of bounds, wrap around
                    if(r>=grid.length) r-=grid.length; //if the row is out of bounds, wrap around
                    for(int j = col - 1; j <= col + 1; j++){
                        int c = j;
                        if(c<0) c+=grid[0].length; //if the column is out of bounds, wrap around
                        if(c>=grid[0].length) c-=grid[0].length; //if the column is out of bounds, wrap around
                        if(grid[r][c] == ALIVE){
                            uf.union(row, col, r, c);
                        }
                    }
                }

            }

        }

        ArrayList<Integer> finds = new ArrayList<Integer>();

        // for(int row=0;row<grid.length;row++){

        //     for(int col=0;col<grid[row].length;col++){

        //         for(int i = row - 1; i <= row + 1; i++){
        //             int r = i;
        //             if(r<0) r+=grid.length; //if the row is out of bounds, wrap around
        //             if(r>=grid.length) r-=grid.length; //if the row is out of bounds, wrap around
        //             for(int j = col - 1; j <= col + 1; j++){
        //                 int c = j;
        //                 if(c<0) c+=grid[0].length; //if the column is out of bounds, wrap around
        //                 if(c>=grid[0].length) c-=grid[0].length; //if the column is out of bounds, wrap around
        //                 if(grid[r][c] == ALIVE){
        //                     System.out.println(uf.find(r,c));
        //                     if(finds.indexOf(uf.find(r, c))==-1) finds.add(uf.find(r,c));
        //                 }
        //             }
        //         }

        //     }

        // }

        for(int row=0;row<grid.length;row++){
            for(int col=0;col<grid[row].length;col++){
                if(grid[row][col]==ALIVE){
                    if(finds.indexOf(uf.find(row, col))==-1) finds.add(uf.find(row,col));
                }
            }
        }

        return finds.size();

    }
}
