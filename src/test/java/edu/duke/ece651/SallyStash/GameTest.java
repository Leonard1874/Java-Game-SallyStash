package edu.duke.ece651.SallyStash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class GameTest {
  @Test
  public void test_() throws FileNotFoundException{
    File fileH = new File("inputV2.txt");
    FileInputStream streamH = new FileInputStream(fileH);
    System.setIn(streamH);
    Game g = new Game();
    g.Play();
    File fileR = new File("inputV2R.txt");
    FileInputStream streamR = new FileInputStream(fileR);
    System.setIn(streamR);
    Game g1 = new Game();
    g1.Play();
  }
}
