import java.util.Arrays;
import java.util.List;

import GameLogic.BoardLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BoardLogicParametrizedTest {
   private BoardLogic board;
   private final Integer firstValue;
   private final Integer secondValue;
   
   public BoardLogicParametrizedTest(Integer firstValue, Integer secondValue) {
      this.firstValue = firstValue;
      this.secondValue = secondValue;
   }

   @Parameters
   public static List<Integer[]> params() {
       return Arrays.asList(
               //4 values out of bounds for the 5x5 board
               new Integer[] {-1, 0},
               new Integer[] {0, -1},
               new Integer[] {5, 0},
               new Integer[] {0,5}
           );
   }
   @Before
   public void setUp() {
      board = new BoardLogic(false);
   }
   
   /**
    * Test of isEmpty method, of class BoardLogic.
    */
   @Test(expected = IllegalArgumentException.class)
   public void testIsEmptyShallThrowIllegalArgumentExceptionIfParameterOutOfBoardRange1() {
      System.out.println("testIsEmptyShallThrowIllegalArgumentExceptionIfParameterOutOfBoardRange1");
      board.isEmpty(firstValue, secondValue);
   }
   
   /**
    * Test of isEmpty method, of class BoardLogic.
    */
   @Test
   public void testIsPlayableShallReturnFalseIfOutOfBounds() {
      System.out.println("testIsPlayableShallReturnFalseIfOutOfBounds");
      assertFalse(board.isPlayablePosition(firstValue, secondValue));
   }
   
}
