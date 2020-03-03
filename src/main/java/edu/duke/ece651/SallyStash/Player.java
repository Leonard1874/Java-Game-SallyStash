package edu.duke.ece651.SallyStash;

import java.util.HashMap;

/*This class is for the player's states and movements*/
public class Player {
  // player's information
  // each player has two boards, keep track of his/her board and the opponent's
  // board
  private Board myBoard;
  private Board otherBoard;
  private char Name;
  private StackFactory myFactory; // use the stack factory to create the stack according to user's demand
  private Printer myPrinter; // print two boards
  private HashMap<Character, Integer> actionTimes; // keep track of how many special actions player has left

  /* Constructor, get the username, board's size from Game */
  public Player(char pname, int h, int w, int limit) {
    Name = pname;
    myBoard = new Board(h, w);
    otherBoard = new Board(h, w);
    myFactory = new StackFactory();
    myPrinter = new Printer();
    actionTimes = new HashMap<Character, Integer>();
    actionTimes.put('M', limit); // limit is the maximum time for action, set by Game
    actionTimes.put('S', limit);
  }

  /* call printer to print boards */
  public void printBoard() {
    myPrinter.printBoard(myBoard, otherBoard);
  }

  /* get the stack's information and call board's method to put stack */
  public boolean place(char type, char dir, int row, int col) {
    Stack toPlace = myFactory.getStack(type, dir, row, col);
    if (!myBoard.checkAddBlock(toPlace, null)) {
      return false; // conflict or out of bound
    } else {
      myBoard.addBlock(toPlace); // no conflict, call board to move the stack by changing stack's block position
                                 // value and re-draw the board
      return true;
    }
  }

  /* This function is used for checking if a specail action can be acted. */
  public boolean canAct(char c) {
    if (actionTimes.get(c) > 0) {
      actionTimes.put(c, actionTimes.get(c) - 1);
      return true;
    } else {
      return false;
    }
  }

  /* Call the board's check move, check if the move position is on a stack */
  public Stack checkMove(int row, int col) {
    return myBoard.checkMoveStack(row, col);
  }

  /*This function is for moving operation, if the moving position hit a stack, ask for user's place input, then call this function*/
  public boolean Move(Stack toMove, char dir, int row, int col) {
    Stack tempStack = myFactory.getStack(toMove.getType(), dir, row, col); // generate a new stack
    if (!myBoard.checkAddBlock(tempStack, toMove)) { // check if it can be placed
      return false; // conflict or out of bound
    } else {
      myBoard.moveStack(toMove, tempStack, row, col); // if it can, call moveStack to replace the original block's info with the new one's
      return true;
    }
  }

  /* get the digged index, check digging result */
  public char check(int row, int col) {
    return myBoard.tryUpdate(row, col);
  }

  /* update the opponent stack */
  public void update(char toUpdate, int row, int col) {
    otherBoard.updateOther(toUpdate, row, col);
  }

  /*Call sonar scan function of board and return scan result*/
  public HashMap<Character, Integer> sonarScan(int row, int col) {
    return myBoard.sonarScan(row, col);
  }

  /*check the actionTimes map for how many actions remain*/
  public int checkActionTime(char action){
    return actionTimes.get(action);
  }

}
