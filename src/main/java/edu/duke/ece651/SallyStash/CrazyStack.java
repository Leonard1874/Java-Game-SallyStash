package edu.duke.ece651.SallyStash;

import java.util.ArrayList;
/*Class for crazystacks, inherit from stack class*/
public class CrazyStack extends Stack {
  /*constructor, get type, dir, staring index and number of blocks*/
  public CrazyStack(char ptype, char pdir, int row, int col, int len) {
    type = ptype;
    dir = pdir;
    blocks = new ArrayList<ArrayList<Integer>>();
    createBlocks(row, col, len);
  }

  /* This function is for creating stakcs according to the stack's information */
  /*adjusted for crazystacks, diffrent method for UD and LR*/
  protected void createBlocks(int r, int c, int l) {
    int row = r;
    int col = c;
    int i = 0;
    while (i < l) {
      // generate blocks position and size and save into blocks
      ArrayList<Integer> newBlock = new ArrayList<Integer>();
      if (dir == 'U' || dir == 'D') {
        if (i == 3) {
          if (dir == 'U') {
            col += 1;
          } else {
            col -= 1;
          }
        } else{
          if (i != 0) {
            row += 1;
          }
        }
        newBlock.add(row);
        newBlock.add(col);
        newBlock.add(0);
        blocks.add(newBlock);
        i += 1;
      } else {
        if (i == 3) {
          if (dir == 'R') {
            row -= 1;
          } else {
            row += 1;
          }
        } else{
          if (i != 0) {
            col += 1;
          }
        }
        newBlock.add(row);
        newBlock.add(col);
        newBlock.add(0);
        blocks.add(newBlock);
        i += 1;
      }
    }
  }
}
