package edu.duke.ece651.SallyStash;
/*This class is aimed at printing two boards*/
public class Printer {

  //toPrint1: owner board, toPrint2: opponent board
  public void printBoard(Board toPrint1, Board toPrint2) {
    int height = toPrint1.getHeight();
    int width = toPrint1.getWidth();
    //get board 2D array to print 
    char[][] board1 = toPrint1.getBoard();
    char[][] board2 = toPrint2.getBoard();
    for (int i = -1; i < height + 1; i++) {
      //print the first/last line(colunm number)
      if (i == -1 || i == height) {
        for (int k = 0; k < 2; k++) {
          for (int j = 0; j < width; j++) {
            if (j >= 0 && j < width - 1) {
              if (j == 0) {
                System.out.print(" ");
              }
              System.out.print(j);
              System.out.print("|");
            }
            if (j == width - 1) {
              System.out.print(j);
            }
          }
          System.out.print("           "); //to boards has some space in between
        }
        System.out.print("\n");
      } else {
        for (int j = -1; j < width+1; j++) {
          //print the row number
          if (j == -1 || j == width) {
            if (j == width) {
              System.out.print((char) ((int) 'A' + i));
            } else {
              System.out.print((char) ((int) 'A' + i));
            }
          } else {
            //print board content
            if (j >= 0 && j < width - 1) {
              System.out.print(board1[i][j]);
              System.out.print("|");
            }
            if (j == width - 1) {
              System.out.print(board1[i][j]);
            }
          }
        }
        System.out.print("          ");
        for (int j = -1; j < width+1; j++) {
          if (j == -1 || j == width) {
            if (j == width) {
              System.out.print((char) ((int) 'A' + i));
            } else {
              System.out.print((char) ((int) 'A' + i));
            }
          } else {
            if (j >= 0 && j < width - 1) {
              System.out.print(board2[i][j]);
              System.out.print("|");
            }
            if (j == width - 1) {
              System.out.print(board2[i][j]);
            }
          }
        }
        System.out.print("\n");
      }
    }
  }
}
