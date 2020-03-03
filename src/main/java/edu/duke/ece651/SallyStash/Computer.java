package edu.duke.ece651.SallyStash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*This class is for the computer, which generates the computer player's commands.*/
public class Computer {
  private ArrayList<String> actions; // actions for computer
  private HashMap<Character, ArrayList<Character>> dirChoice; // each stack type's dir choices
  private int height;
  private int width;
  private Random rand; // using random generation

  public Computer(int h, int w, ArrayList<String> act, HashMap<Character, ArrayList<Character>> dir) {
    height = h;
    width = w;
    actions = act;
    dirChoice = dir;
    rand = new Random();
  }

  /*
   * This function is fot generating the position for dig/move/sonar scan, it
   * genrates a string, like "M4"
   */
  public String generatePos() {
    // gerate the row and column of the position randomly
    int rand_row = rand.nextInt(height);
    int ran_col = rand.nextInt(width);
    String row = Character.toString('A' + rand_row);
    String col = Integer.toString(ran_col);
    return row + col;
  }

  /*
   * This function is for generating the place for stacks, returns string like
   * "M4H"
   */
  public String generatePlace(char color) {
    String res = generatePos(); //call generatePos to get the row and colunm
    int rand_dir = rand.nextInt(100); //generate the dir randomly
    String dir = "";
    if (color == 'G' || color == 'P') {
      if (rand_dir % 2 == 0) { // 50% for both H/V
        dir = "H";
      } else {
        dir = "V";
      }
    } else {
      // 25% for U/D/L/R
      if (rand_dir < 25) {
        dir = "U";
      } else if (rand_dir < 50) {
        dir = "D";
      } else if (rand_dir < 75) {
        dir = "L";
      } else {
        dir = "R";
      }
    }
    res += dir;
    return res;
  }

  /*This function is for generating the computer's choice of actions, returns "D", "M" or "S"*/
  public String generateAction() {
    // 80% of D, 10% for both M and S
    int rand_int = rand.nextInt(100);
    if (rand_int < 80) {
      return actions.get(0);
    } else if (rand_int < 90) {
      return actions.get(1);
    } else {
      return actions.get(2);
    }
  }

}
