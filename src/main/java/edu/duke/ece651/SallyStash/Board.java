package edu.duke.ece651.SallyStash;

import java.util.ArrayList;
import java.util.HashMap;

/*Class for the game board*/
public class Board {

  // board infomration
  private char[][] board;
  private ArrayList<Stack> stacks; // stacks in board, for owner board usage
  private HashMap<ArrayList<Integer>, Character> coorStates; // each coordinate's value, for opponent board usage
  private int height;
  private int width;

  // costructor, set up the board of demand size
  public Board(int h, int w) {
    board = new char[h][w];
    stacks = new ArrayList<Stack>();
    coorStates = new HashMap<ArrayList<Integer>, Character>();
    height = h;
    width = w;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        board[i][j] = ' '; // at first, all empty
      }
    }
  }

  /* get private fields */

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public char[][] getBoard() {
    return board;
  }

  /*
   * read the stacks informations, put the stack's 'P','R' into the board 2D
   * array, in order for printing.
   */
  private void drawOwnBoard() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        board[i][j] = ' ';
      }
    }
    for (Stack toDraw : stacks) {
      for (ArrayList<Integer> block : toDraw.getBlocks()) {
        if (block.get(2) == 0) { // if not digged, draw stack type
          board[block.get(0)][block.get(1)] = toDraw.getType();
        } else { // else, put a '*'
          board[block.get(0)][block.get(1)] = '*';
        }
      }
    }
  }

  /*
   * similar to the last function, read the coorstates and draw the opponent board
   */
  private void drawOtherBoard() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        board[i][j] = ' ';
      }
    }
    for (HashMap.Entry<ArrayList<Integer>, Character> coor : coorStates.entrySet()) {
      board[coor.getKey().get(0)][coor.getKey().get(1)] = coor.getValue();
    }
  }

  /*
   * check the board and call each stack's checkWin function, if all stacks are
   * digged, return true(win).
   */
  private boolean checkWin() {
    boolean res = true;
    for (Stack s : stacks) {
      if (!s.checkWin()) {
        res = false;
      }
    }
    return res;
  }

  /* This function is for trying to update the stack's state */
  public char tryUpdate(int row, int col) {
    char res = 'x'; // missed
    for (Stack s : stacks) {
      if (s.tryUpdate(row, col)) { // call the stack's method to find if the index of digging is in the stack and
                                   // haven't been digged
        if (checkWin()) { // if update is made, check win
          res = '~'; // win
        } else {
          res = s.getType(); // digged, type
        }
        drawOwnBoard(); // update the board 2D array
        break;
      }
    }
    return res;
  }

  /*
   * This function if for update the opponent board, get the state to be put into
   * the board, uodate the coorstate and the 2D array
   */
  public void updateOther(char toUpdate, int row, int col) {
    ArrayList<Integer> updateKey = new ArrayList<Integer>();
    updateKey.add(row);
    updateKey.add(col);
    if (coorStates.containsKey(updateKey)) {
      coorStates.replace(updateKey, toUpdate);
    } else {
      coorStates.put(updateKey, toUpdate);
    }
    drawOtherBoard();
  }

  /* This function is for trying to add a stack into the board */
  public boolean checkAddBlock(Stack toAdd, Stack toAvoid) {
    if (toAvoid != null) { // if it is used in moving a stack, the toAvoid is the moved stack itself, it
                           // shoudl not be considered in conflict detection
      for (ArrayList<Integer> block : toAvoid.getBlocks()) {
        board[block.get(0)][block.get(1)] = ' '; // remove it temporarayly, if not placed in the end, it would be drawn
                                                 // on the board again
      }
    }
    for (ArrayList<Integer> block : toAdd.getBlocks()) {
      if (block.get(0) >= 0 && block.get(0) < height && block.get(1) >= 0 && block.get(1) < width) { // check
                                                                                                     // out-of-bound
        if (board[block.get(0)][block.get(1)] != ' ') {// check confilic
          return false;
        }
      } else {
        return false;
      }
    }
    return true;
  }

  /*
   * This function is for adding a block on 2D char array, used only during first
   * place
   */
  public void addBlock(Stack toAdd) {
    // if no error, update the stacks info and board 2D array
    for (ArrayList<Integer> block : toAdd.getBlocks()) {
      board[block.get(0)][block.get(1)] = toAdd.getType();
    }
    stacks.add(toAdd);
  }

  /*
   * Check if the position for moving asked by user actually hits a stack, if so,
   * return it for move
   */
  public Stack checkMoveStack(int row, int col) {
    for (Stack s : stacks) {
      for (ArrayList<Integer> block : s.getBlocks()) {
        if (block.get(0) == row && block.get(1) == col) { // check hit
          return s;
        }
      }
    }
    return null; // not hit, return null
  }

  /*
   * Actually move the stack, rewrite the moved stack's position value with the
   * newly generated stack's position value, but keeps its state(hit/not hit)
   */
  public void moveStack(Stack toMove, Stack moved, int row, int col) {
    assert (toMove.getType() == moved.getType());
    for (int i = 0; i < moved.getBlocks().size(); i++) {
      toMove.getBlocks().get(i).set(0, moved.getBlocks().get(i).get(0));
      toMove.getBlocks().get(i).set(1, moved.getBlocks().get(i).get(1));
    }
    drawOwnBoard();
  }

  /*check if a position is in range of board, support sonar scan*/
  private boolean inRange(int row, int col) {
    return (row >= 0 && row < height && col >= 0 && col < width);
  }

  /*This function is for the sonar scan function*/
  public HashMap<Character, Integer> sonarScan(int row, int col) {
    drawOwnBoard(); //update the board first
    HashMap<Character, Integer> res = new HashMap<Character, Integer>(); //record the reuslt in a Map
    //using for loop to scan the diamond region
    for (int i = -3; i <= 3; i++) {
      int l = Math.abs((Math.abs(i) - 3)) * 2 + 1;
      for (int j = -1 * l / 2; j <= l / 2; j++) {
        if (inRange(row + i, col + j)) { // keep the searching region in board range
          if (board[row + i][col + j] != ' ' && board[row + i][col + j] != '*') { //only acount blocks that has not been hit
            if (res.containsKey(board[row + i][col + j])) {
              res.put(board[row + i][col + j], res.get(board[row + i][col + j]) + 1);
            } else {
              res.put(board[row + i][col + j], 1);
            }
          }
        }
      }
    }
    return res;
  }
}

/*
 * for (int j = 0; j < stacks.size(); j++){ for (ArrayList<Integer> block :
 * stacks.get(j).getBlocks()) { if (block.get(0) == row && block.get(1) == col)
 * { // check break; } } }
 */
