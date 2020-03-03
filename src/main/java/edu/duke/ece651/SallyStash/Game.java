package edu.duke.ece651.SallyStash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/*This class this a monitor class that controls the game.*/
public class Game {
  // game informations
  private char[] playerNames = { 'A', 'B' };
  private ArrayList<Player> Players; // list of player objects
  private HashMap<Player, Character> playerRole; // keep track of the player's type {'A':'R'}
  private String[] colors = { "Green", "Purple", "Red", "Blue" };
  private char[] colorsAbbrev = { 'G', 'P', 'R', 'B' };
  private HashMap<Character, String> colorToAbbrev;
  private String[] Times = { "first", "second", "third" };
  private int[] stackNums = { 1, 1, 2, 2 }; // control how many stacks can this game have, for testing, less stacks
  private ArrayList<String> actions;
  private ArrayList<Character> dirNormal;
  private ArrayList<Character> dirOther;
  private HashMap<Character, ArrayList<Character>> dirChoice; // for different stacks, there are different dir choices
  private int height = 20;
  private int width = 10;
  private int actionLimit = 3;
  private Computer computer;

  public Game() {
    Players = new ArrayList<Player>();
    for (int i = 0; i < 2; i++) {
      Player newPlayer = new Player(playerNames[i], height, width, actionLimit);
      Players.add(newPlayer);
    }
    colorToAbbrev = new HashMap<Character, String>();
    for (String color : colors) {
      colorToAbbrev.put(color.charAt(0), color);
    }
    actions = new ArrayList<String>(Arrays.asList("D", "M", "S"));
    dirNormal = new ArrayList<Character>(Arrays.asList('H', 'V'));
    dirOther = new ArrayList<Character>(Arrays.asList('U', 'D', 'L', 'R'));
    dirChoice = new HashMap<Character, ArrayList<Character>>();
    dirChoice.put('G', dirNormal); // normal stacks, H/V
    dirChoice.put('P', dirNormal);
    dirChoice.put('R', dirOther); // super/crazy stacks, U/D/L/R
    dirChoice.put('B', dirOther);
    playerRole = new HashMap<Player, Character>();
    computer = new Computer(height, width, actions, dirChoice); // a computer object to generate computer player's
                                                                // action strings
  }

  private void printLine() {
    System.out.println();
    System.out.println("-----------------------------------------------------");
    System.out.println();
  }

  /*
   * This function is used for checking the validaity of the user's input, it must
   * be like "M4"
   */
  private boolean inputValid(String in) {
    // check the digit bit
    if (!Character.isDigit(in.charAt(1))) {
      return false;
      // check the alpha bit
    } else if (!Character.isUpperCase(in.charAt(0)) || (int) (in.charAt(0) - 'A') >= height) {
      return false;
    } else {
      return true;
    }
  }

  /*
   * This function is used to check the user's input during placing stacks, it
   * will check the third bit and then call 'inputValid' to check the first to
   * bits, error info will be provided.
   */
  private String getUserInputPlace(Scanner scan, char color) {
    String place = scan.nextLine();
    if (place.length() != 3) {
      System.out.println("Invalid place input length, please try again.");
      return null;
    } else if ((color == 'G' || color == 'P') && (place.charAt(2) != 'V' && place.charAt(2) != 'H')) {
      // must be V or H
      System.out.println(place.charAt(2));
      System.out.println("Invalid place input (V/H), please try again.");
      return null;
    } else if ((color == 'R' || color == 'B')
        && (place.charAt(2) != 'U' && place.charAt(2) != 'D' && place.charAt(2) != 'L' && place.charAt(2) != 'R')) {
      // must be U, D, L, R
      System.out.println(place.charAt(2));
      System.out.println("Invalid place input (U/D/L/R), please try again.");
      return null;
    } else if (!inputValid(place)) {
      System.out.println("Invalid place input, please try again.");
      return null;
    } else {
      return place;
    }
  }

  /* Let computer player to generate the command for placing stacks */
  private String getComputerInputPlace(char color) {
    return computer.generatePlace(color);
  }

  /* This function will call the player class's method and ask to place a stack */
  private boolean placeStack(Player p, String place, char type) {
    char dir = place.charAt(2);
    int row = (int) (place.charAt(0) - 'A');
    int col = (int) (place.charAt(1) - '0');
    return p.place(type, dir, row, col);
  }

  /*
   * This function is used in the open stage of the game, it will print the
   * openning words and ask users to place stacks, then call placeStack to place.
   */
  private void openPlace(Scanner scan) {
    for (int i = 0; i < 2; i++) {
      boolean isRobot = true;
      if (playerRole.get(Players.get(i)) == 'H') {
        printLine();
        System.out.print("Player " + playerNames[i]
            + ", you are going place Sally’s stash on the board. Make sure Player " + playerNames[(i + 1) % 2]
            + " isn’t looking!  For  each  stack,  type  the  coordinate  of  the  upper  left  side  of  the  stash, followed  by  either  H  (for  horizontal)  or  V  (for  vertical).  For  example,  M4H  would place a stackhorizontally starting at M4 and going to the right.You have \n");
        System.out.println(stackNums[0] + " Greenstacks that are 1x2");
        System.out.println(stackNums[1] + " Purple stacks that are 1x3");
        System.out.println(stackNums[2] + " Red stacks that are Super Stacks");
        System.out.println(stackNums[3] + " Blue stacks that are Crazy Stacks");
        isRobot = false;
      }
      for (int j = 0; j < 4; j++) {
        int k = 0;
        while (k < stackNums[j]) {
          if (!isRobot) {
            printLine();
            Players.get(i).printBoard();
            System.out.println("Player " + playerNames[i] + ", where do you want to place the " + Times[k] + " "
                + colors[j] + "stack?");
          }
          String place = null;
          if (!isRobot) {
            place = getUserInputPlace(scan, colorsAbbrev[j]); // get the user's input, check if it is valid
          } else {
            place = getComputerInputPlace(colors[j].charAt(0));
          }
          if (place != null) {
            // if valid, ask to put stacks
            if (placeStack(Players.get(i), place, colors[j].charAt(0))) {
              k += 1;
            } else {
              if (!isRobot) {
                // if there is conflicts or out of bound, ask the user to place stack again.
                System.out.println("Can't place stack, please try again.");
              }
            }
          }
        }
      }
      if (!isRobot) {
        Players.get(i).printBoard();
      }
    }
  }

  /*
   * This function is used for check the validity of user's inputs during digging
   * part.
   */
  private String getUserInputPos(Scanner scan) {
    String pos = scan.nextLine();
    if (pos.length() != 2) { // check the length first
      System.out.println("Invalid input length, please try again.");
      return null;
    } else if (!inputValid(pos)) { // call inputValid to check the content
      System.out.println("Invalid place input, please try again.");
      return null;
    } else {
      return pos;
    }
  }

  /* let computer player to generate the position dig/move/sonar */
  private String getComputerInputPos() {
    return computer.generatePos();
  }

  /*
   * This function is used for calling the checking methods in class player and
   * get the result of current digging, then check the result, notice the other
   * player and call updating methods. It also tracks the winning state.
   */
  private boolean checkUpdateDig(int playerNum, String dig) {
    boolean isWin = false; // keep track of if one player has won
    // dig position
    int row = (int) (dig.charAt(0) - 'A');
    int col = (int) (dig.charAt(1) - '0');
    // ask for oppent player class for dig result
    char res = Players.get((playerNum + 1) % 2).check(row, col);
    Players.get(playerNum).update(res, row, col);
    if (res == 'x') { // missed
      System.out.println("Player " + playerNames[playerNum] + " Missed!");
    } else if (res == '~') { // current players has won
      isWin = true;
      System.out.println("Player " + playerNames[playerNum] + " Win!"); // notice the player of winning
    } else {
      System.out.println("Player " + playerNames[playerNum] + " Found a Stack!"); // found a stack
    }
    return isWin;
  }

  /*
   * This function is for letting two players to dig in turn, give output and read
   * user input.
   */
  private int digEachOther(Scanner scan, int playerNum) {
    int winner = -1; // keep track of the winner
    Players.get(playerNum).printBoard();
    System.out.println("Player " + playerNames[playerNum] + ", where do you want to dig?");
    int j = 0;
    while (j < 1) {
      String dig = getUserInputPos(scan); // get the user's input, check if it is valid
      if (dig != null) {
        if (checkUpdateDig(playerNum, dig)) {
          winner = playerNum;
        }
        j += 1;
      }
    }
    return winner;
  }

  /* This function is for handling the computer player's digging */
  private int digEachOtherComputer(char playerName, int playerNum) {
    int winner = -1; // keep track of the winner
    int j = 0;
    while (j < 1) {
      String dig = getComputerInputPos(); // get the computer's input, check if it is valid
        if (checkUpdateDig(playerNum, dig)) { // call checkUpdate, if return true, someone has won
          winner = playerNum;
        }
        j += 1;
    }
    return winner;
  }

  /* This function handles the user's move operation. */
  private void moveStack(int playerNum, Scanner scan) {
    int i = 0;
    // if not successful, repeat input
    while (i < 1) {
      System.out.println("Which stack to move?");
      String moveTo = getUserInputPos(scan); // get the user's input, check if it is valid
      if (moveTo != null) {
        int row = (int) (moveTo.charAt(0) - 'A');
        int col = (int) (moveTo.charAt(1) - '0');
        // use the user's input, check if it is in a stack, if so, return it, else,
        // return null
        Stack toMove = Players.get(playerNum).checkMove(row, col);
        if (toMove != null) {
          System.out.println("Move to where?");
          moveTo = getUserInputPlace(scan, toMove.getType()); // get the user's place input, check if it is valid
          if (moveTo != null) {
            row = (int) (moveTo.charAt(0) - 'A');
            col = (int) (moveTo.charAt(1) - '0');
            char dir = moveTo.charAt(2);
            // call player obj's move function, if return false, position conflict, input
            // again
            if (!Players.get(playerNum).Move(toMove, dir, row, col)) {
              System.out.println("Can't place stack, please try again.");
            } else {
              i += 1;
            }
          }
        } else {
          System.out.println("Can't find stack to move, please try again.");
        }
      }
    }
    Players.get(playerNum).printBoard();
  }

  /*
   * This function handles the computer player's move action, similar to user's
   * move action handler, all output removed
   */
  private void moveStackComputer(char playerName, int playerNum) {
    int i = 0;
    while (i < 1) {
      String moveTo = getComputerInputPos(); // get the computer's input, check if it is valid
      int row = (int) (moveTo.charAt(0) - 'A');
      int col = (int) (moveTo.charAt(1) - '0');
      Stack toMove = Players.get(playerNum).checkMove(row, col);
      if (toMove != null) {
        moveTo = getComputerInputPlace(toMove.getType()); // get the computer's input, check if it is valid
        row = (int) (moveTo.charAt(0) - 'A');
        col = (int) (moveTo.charAt(1) - '0');
        char dir = moveTo.charAt(2);
        if (!Players.get(playerNum).Move(toMove, dir, row, col)) {
          i += 0;
        } else {
          i += 1;
        }
      }
    }
    System.out.println("Player " + playerName + " used a special action!");
  }

  /* This function handles the sonar function for user. */
  private void sonarCheck(int playerNum, Scanner scan) {
    int i = 0;
    while (i < 1) {
      System.out.println("Where would you like to sonar scan?");
      String toScan = getUserInputPos(scan); // get sonar scan position and check validity
      if (toScan != null) {
        // if position valid, call sonar functions
        int row = (int) (toScan.charAt(0) - 'A');
        int col = (int) (toScan.charAt(1) - '0');
        HashMap<Character, Integer> stackNum = Players.get(playerNum).sonarScan(row, col); // save the scan result into
                                                                                           // a hashmap {'color':'block
                                                                                           // number'}
        for (Character c : colorToAbbrev.keySet()) {
          // print the scan results
          if (!stackNum.containsKey(c)) {
            System.out.println(colorToAbbrev.get(c) + " stacks occupy 0 squares.");
          } else {
            System.out.println(colorToAbbrev.get(c) + " stacks occupy " + stackNum.get(c) + " squares.");
          }
        }
        i += 1;
      }
    }
  }

  /*
   * This function handles the computer's scan function, since the computer don't
   * handle scan result so far, it only print the action is taken
   */
  private void sonarCheckComputer(char playerName) {
    System.out.println("Player " + playerName + " used a special action!");
  }

  /* This function scans the user's choice for actions. */
  private String getUserInputAction(Scanner scan) {
    String action = scan.nextLine();
    if (action.length() != 1) { // check the length first
      System.out.println("Invalid dig input length, please try again.");
      return null;
    } else if (action.charAt(0) != 'D' && action.charAt(0) != 'M' && action.charAt(0) != 'S') { // check the content,
                                                                                                // must be 'D'/'M'/'S'
      System.out.println("Invalid action input, please try again.");
      return null;
    } else {
      return action; // valid, return the input string
    }
  }

  /*
   * This function let computer to generate its action choice and return it as a
   * string
   */
  private String getComputerInputAction() {
    return computer.generateAction();
  }

  /*
   * This function loops and ask for the user or computer for action choice, check
   * it then call coresponding functions
   */
  private void askAction(Scanner scan) {
    while (true) { // loop, until on player wins
      for (int i = 0; i < 2; i++) { // ask two players for action in turn
        printLine();
        boolean isRobot = true;
        if (playerRole.get(Players.get(i)) == 'H') { // check if the player is computer player
          Players.get(i).printBoard();
          System.out.println("Player " + playerNames[i] + ", what would you want to do?"); // if not, output asking
          System.out.println("'D' for dig, 'M' for move("+Players.get(i).checkActionTime('M') +" times remain), 'S' for sonar("+Players.get(i).checkActionTime('S')+" times remain)");
          isRobot = false;
        }
        int j = 0;
        while (j < 1) { // if not success, continue to try calling the corresponding function for action
                        // choice
          String action = null;
          // call different functions according to robot and human
          if (!isRobot) {
            action = getUserInputAction(scan);
          } else {
            action = getComputerInputAction();
          }
          if (action != null) {
            if (action.charAt(0) == 'D') {
              if (!isRobot) {
                if (digEachOther(scan, i) >= 0) {
                  return;
                }
              } else {
                if (digEachOtherComputer(playerNames[i], i) >= 0) {
                  return;
                }
              }
              j += 1;
            } else if (action.charAt(0) == 'M') {
              if (Players.get(i).canAct('M')) { // if it is a special action, check if there is action left
                if (!isRobot) {
                  moveStack(i, scan);
                } else {
                  moveStackComputer(playerNames[i], i);
                }
                j += 1;
              } else {
                if (!isRobot) {
                  System.out.println("You have used all your Move actions! Please try other actions."); // if not, ask
                                                                                                        // for a new
                                                                                                        // action choice
                }
              }
            } else{ // if it is a special action, check if there is action left
              if (Players.get(i).canAct('S')) {
                if (!isRobot) {
                  sonarCheck(i, scan);
                } else {
                  sonarCheckComputer(playerNames[i]);
                }
                j += 1;
              } else {
                if (!isRobot) {
                  System.out.println("You have used all your Sonar actions! Please try other actions.");
                }
              }
            }
          }
        }
      }
    }
  }

  /*
   * This function ask for the roles of two players, A and B can be human or
   * computer.
   */
  private void askRole(Scanner scan) {
    for (int i = 0; i < 2; i++) {
      System.out.println("What is Player " + playerNames[i] + "'s role? 'H' for human player, 'R' for robot player.");
      int j = 0;
      while (j < 1) {
        String role = scan.nextLine();
        if (role.length() == 1 && (role.charAt(0) == 'H' || role.charAt(0) == 'R')) { // the input must be 'H' or 'R'
          playerRole.put(Players.get(i), role.charAt(0));
          j += 1;
        } else {
          System.out.println("Invalid role input, please input again."); // invalid input, ask for input again
        }
      }
    }
  }

  /* This is the top method of this class, it controls the game process */
  public void Play() {
    Scanner scan = new Scanner(System.in); // use a scanner to get user input
    askRole(scan);
    openPlace(scan);
    askAction(scan);
    scan.close();
  }

  public static void main(String[] args) {
    Game g = new Game();
    g.Play();
  }
}
