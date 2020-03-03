package edu.duke.ece651.SallyStash;

import java.util.HashMap;
/*This is a factory class for stacks*/
public class StackFactory {
  // it contains the stacks' building information
  private HashMap<Character, Integer> TypeInfo = new HashMap<Character, Integer>() {
    {
      put('R', 4);
      put('B', 6);
      put('G', 5);
      put('P', 3);
}};
  
  // use getSTACK method to get object of type stack, it can only be called by using R/G/P/B, which will be checked in Game class when the input comes
  public Stack getStack(char stackType, char dir, int row, int col) {
    if (stackType == 'R') {
      return new SuperStack(stackType, dir, row, col, TypeInfo.get(stackType));
    } else if (stackType == 'G') {
      return new NormalStack(stackType, dir, row, col, TypeInfo.get(stackType));
    } else if (stackType == 'P') {
      return new NormalStack(stackType, dir, row, col, TypeInfo.get(stackType));
    } else{
      return new CrazyStack(stackType, dir, row, col, TypeInfo.get(stackType));
    }
  }
}
