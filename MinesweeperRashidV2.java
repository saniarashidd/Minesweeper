import java.util.Scanner;
import java.util.Random;

public class MinesweeperRashidV2 { 

  private static final int GAME_WON = 1;
  private static final int GAME_LOST = -1;
  private static final int GAME_NOTOVER = 0;
  
  // Add other static variables and constants you might need
  private static Cell[][] grid;

  /* 
  * Create the grid and place mines in random locations.
  *
  * @param rows     The number of rows in the Minesweeper grid
  * @param columns  The number of columns in the Minesweeper grid
  *
  * Tip: Create Minesweeper grid with 2 extra rows and 2 extra columns
  *      This will make it easy to go around the grid eliminating
  *      the need for ArrayOutOfBounds checking at the edges.
  */
  public static void initGrid(int width, int length) {
    grid = new Cell[width + 2][length + 2]; //initializes grid
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        grid[i][j] = new Cell();
      }
    }
  }
    
  /*
  * Places mines in random locations in the grid.
  *
  * @param amountMines   The number of mines to be set in the grid.
  */
  public static void disperseMines(int amountMines) {
    int numMines = 0;
    Random random = new Random();
    while (numMines < amountMines) {
      int randomRow = random.nextInt(grid.length - 2); //subtracts border from values
      int randomCol = random.nextInt(grid[0].length - 2);
      if (grid[randomRow + 1][randomCol + 1].isMine() == false) { //if it is not a mine, then set it to a mine
        grid[randomRow + 1][randomCol + 1].setMine();
        numMines++;
      } 
    }
  }

  /*
  * Updates each cell with the number of adjacent cells with mines
  */
  public static void adjacentMines() {
    for (int i = 1; i < (grid.length - 1); i++) {
      for (int j = 1; j < (grid[0].length - 1); j++) {
        int numAdjMines = 0;
        if (grid[i][j].isMine() == false) { // if the position we are checking is not a mine, then check the values in a square around it for mines and set that position to that number
          for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
              if (grid[i + k][j + l].isMine() == true) {
                numAdjMines++;
                grid[i][j].setAdjacentMines(numAdjMines);
              }
            }
          }
        }
      }
    }
  }
 
  /*
  * Method to print Minesweeper grid
  */
  private static void printGrid() {
    for (int i = 1; i < (grid.length - 1); i++) {
      for (int j = 1; j < (grid[0].length - 1); j++) {
        System.out.print(grid[i][j].getVal() + " ");
      }
      System.out.println("");
    }
  }

  /*
  * Method to reveal all the hidden cells. Prints grid after all cells
  * have been revealed.
  */
  public static void revealGrid() {
    for (int i = 1; i < (grid.length - 1); i++) {
      for (int j = 1; j < (grid[0].length - 1); j++) {
        System.out.print(grid[i][j].getTempReveal() + " "); // only temporarily reveals grid, so user can still play
      }
      System.out.println("");
    }
  }

  /* 
  * Reveals the selected cell. So the cell now displays a '.' if no
  * adjacent cells have mines, or, a number representing the number 
  * of adjacent cells with mines.
  *
  * Extra Credit: Reveal surrounding cells until encountering a cell 
  *               with non-zero adjacent mines 
  *
  * @param   row    Row of the user selected cell
  * @param   column Column of the user selected cell
  * @return  an integer indicating if the game is won, lost or not-over
  */
  public static int revealCell(int row, int col) {
    /*
      * Handle user's cell selection specified by row and column 
      * There are three different cases:
      * 1. user chooses already explored cell - do nothing
      * 2. user chooses cell which has a mine - game lost
      * 3. user chooses a mine-free cell - reveal the cell
      * Print Minesweeper grid after handling user input
      *
      */
      if (grid[row][col].isRevealed() == true) { // already explored
        printGrid();
        return GAME_NOTOVER;
      }
      if (grid[row][col].isMine() == true) { // if mine
        grid[row][col].reveal();
        printGrid();
        return GAME_LOST;
      }
      if (grid[row][col].isMine() == false) { // not mine, empty or number
        grid[row][col].reveal();
        printGrid();
        return GAME_NOTOVER;
      }
    return GAME_NOTOVER;
  }

  /*
  * Check if all the mine-free cells are revealed
  * @return  true if game over, false if not
  */
  public static boolean checkGameOver() {
    for (int i = 1; i < grid.length - 1; i++) {
      for (int j = 1; j < grid[0].length - 1; j++) {
        if (grid[i][j].isMine() == false && grid[i][j].isRevealed() == false) { // if mine free and not revealed
          return false;
        }
      }
    }
    return true;
  }

  /* Add other static methods as necessary */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("What do you want the height of the grid to be? ");
    int width = Integer.parseInt(scanner.nextLine());
    System.out.println("What do you want the width of the grid to be? ");
    int length = Integer.parseInt(scanner.nextLine());
    System.out.println("How many mines do you want in the grid? ");
    int amountMines = Integer.parseInt(scanner.nextLine());
    initGrid(width, length);
    System.out.println("");
    printGrid();
    disperseMines(amountMines);
    adjacentMines();
    
    while (checkGameOver() == false) {
      System.out.println("");
      System.out.print("Which row to play? ");
      int row = Integer.parseInt(scanner.nextLine());
      System.out.print("Which column to play? ");
      int col = Integer.parseInt(scanner.nextLine());
      System.out.println("");
      if (grid[row][col].isRevealed() == true) {
        System.out.print("That position is already opened. Pick another");
      } else if (revealCell(row, col) == GAME_LOST) {
        System.out.println("");
        System.out.println("Game over, you hit a mine and lost.");
        revealGrid();
        return;
      }
      System.out.println("");
      System.out.print("Would you like to reveal the grid/solution? Say yes or no: ");
      String answer = scanner.nextLine();
      if (answer.equals("yes")) {
        System.out.println("");
        revealGrid();
        System.out.println("");
      }
    }
    System.out.println("");
    System.out.println("You won! Good job.");
    revealGrid();
  }
}
