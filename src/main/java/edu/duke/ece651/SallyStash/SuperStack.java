package edu.duke.ece651.SallyStash;

import java.util.ArrayList;

public class SuperStack extends Stack {
  /* This function is for creating stakcs according to the stack's information */
  protected void createBlocks(int r, int c, int l) {
    int row = r;
    int col = c;
    int i = 0;
    while (i < l) {
      // generate blocks position and size and save into blocks
      //adjusted only for super blocks, diffrent generating method for different directions
      ArrayList<Integer> newBlock = new ArrayList<Integer>();
      if (dir == 'U') {
        if (i == 0) {
          row += 0;
          col += 0;
        } else if (i == 1) {
          row += 1;
          col -= 1;
        } else {
          col += 1;
        }
        newBlock.add(row);
        newBlock.add(col);
        newBlock.add(0);
        blocks.add(newBlock);
        i += 1;
      } else if (dir == 'R' || dir == 'L') {
        if (i < l - 1) {
          if (i == 0) {
            row += 0;
            col += 0;
          } else {
            row += 1;
          }
        } else {
          if (dir == 'R') {
            row -= 1;
            col += 1;
          } else {
            row -= 1;
            col -= 1;
          }
        }
        newBlock.add(row);
        newBlock.add(col);
        newBlock.add(0);
        blocks.add(newBlock);
        i += 1;
      } else {
        if (i < l - 1) {
          if (i == 0) {
            row += 0;
            col += 0;
          } else {
            col += 1;
          }
        } else {
          row += 1;
          col -= 1;
        }
        newBlock.add(row);
        newBlock.add(col);
        newBlock.add(0);
        blocks.add(newBlock);
        i += 1;
      }
    }
  }

  /* Constructor, get the type, size, direction, number of blocks */
  public SuperStack(char ptype, char pdir, int row, int col, int len) {
    type = ptype;
    dir = pdir;
    blocks = new ArrayList<ArrayList<Integer>>();
    createBlocks(row, col, len);
  }

}
