package edu.duke.ece651.SallyStash;

import java.util.ArrayList;

/*Parent class for all stacks*/
abstract class Stack {
  protected ArrayList<ArrayList<Integer>> blocks; // this field keep track of all blocks' poistion and state(1:digged,
                                                // 0:not digged) in this stack
  protected ArrayList<ArrayList<Integer>> temp_bloks;
  protected char type;
  protected char dir;

  /* Constrictor, get the type, size, direction */
  public Stack() {
  }

  protected abstract void createBlocks(int row, int col, int len);

  public ArrayList<ArrayList<Integer>> getBlocks() {
    return blocks;
  }

  public char getType() {
    return type;
  }

  /*
   * check each block in current stack, to see if the digged index is in the
   * block, and if the block need to update its state to digged
   */
  public boolean tryUpdate(int row, int col) {
    boolean res = false;
    for (ArrayList<Integer> block : blocks) {
      if (block.get(0) == row && block.get(1) == col && block.get(2) != 1) {
        block.set(2, 1);
        res = true;
        break;
      }
    }
    return res;
  }

  /* check all blocks, to see if this stack has been digged empty */
  public boolean checkWin() {
    boolean res = true;
    for (ArrayList<Integer> block : blocks) {
      if (block.get(2) != 1) {
        res = false;
        break;
      }
    }
    return res;
  }
}
