package edu.duke.ece651.SallyStash;

import java.util.ArrayList;
/*This class is for the rectangle(normal) stacks*/
public class NormalStack extends Stack {
  /*This function is for creating stakcs according to the stack's information*/
  protected void createBlocks(int r, int c, int l) {
    int row = r;
    int col = c;
    for (int i = 0; i < l; i++) {
      //generate blocks position and size and save into blocks 
      ArrayList<Integer> newBlock = new ArrayList<Integer>();
      newBlock.add(row);
      newBlock.add(col);
      newBlock.add(0);
      blocks.add(newBlock);
      if (dir == 'H') {
        col += 1;
      } else {
        row += 1;
      }
    }
  }

  /*Constrictor, get the type, size, direction*/
  public NormalStack(char ptype, char pdir, int row, int col, int len) {
    type = ptype;
    dir = pdir;
    blocks = new ArrayList<ArrayList<Integer>>();
    createBlocks(row, col, len);
  }
}
