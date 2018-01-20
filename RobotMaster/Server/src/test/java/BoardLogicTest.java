import java.util.ArrayList;

import GameLogic.BoardLogic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class BoardLogicTest {
   private BoardLogic board;
   
   
   public BoardLogicTest() {
   }
   @Before
   public void setUp() {
      board = new BoardLogic(false);
   }

   /**
    * Test the size returned by getBoard method, of class BoardLogic.
    */
   @org.junit.Test
   public void testGetBoardShallReturnAnArrayOfLength5x5() {
      System.out.println("testGetBoardShallReturnAnArrayOfLength5x5");
      int[][] result = board.getBoard();
      assertEquals(5, result.length);
      assertEquals(5, result[0].length);
   }
   
   /**
    * Test the elements returned by getBoard method, of class BoardLogic.
    */
   @org.junit.Test
   public void testGetBoardShallReturnElementsBetweenMinusOneAndFiveIncludes() {
      System.out.println("testGetBoardShallReturnElementsBetweenMinusOneAndFiveIncludes");
      boolean result = true;
      for (int[] lines : board.getBoard()) {
         for (int line : lines) {
            if ( (line < board.getMIN_VALUE() || line > board.getMAX_VALUE()) && line != board.getEMPTY_SQUARE()) {
               System.out.println("valeur : " + line);
               result = false;
            }
         }
      }
      assertTrue(result);
   }
   
   
   /**
    * Test the elements returned by getBoard method in the case we use in a wrong way playAt before, of class BoardLogic.
    */
   @org.junit.Test
   public void testGetBoardShallReturnElementsBetweenMinusOneAndFiveIncludesEvenIfWeUsePlayAtWithIncorrectNumbers() {
      System.out.println("testGetBoardShallReturnElementsBetweenMinusOneAndFiveIncludesEvenIfWeUsePlayAtWithIncorrectNumbers");
      //We try to play at all positions with a wrong number
      int[][] array = board.getBoard();
      for (int i = 0; i < array.length; i++) {
         for (int j = 0; j < array[0].length; j++) {
            board.playAt(i, j, 6);
         }
      }
      
      boolean result = true;
      for (int[] lines : board.getBoard()) {
         for (int line : lines) {
            if ( (line < board.getMIN_VALUE() || line > board.getMAX_VALUE()) && line != board.getEMPTY_SQUARE()) {
               result = false;
            }
         }
      }
      assertTrue(result);
   }

   /**
    * Test of isEmpty method, of class BoardLogic.
    */
   @org.junit.Test
   public void testIsEmptyShallReturnTrueIfTheSquareIsEmptyAndFalseIfItContainsADifferentNumberThanMinusOne() {
      System.out.println("isEmpty");
      int[][] array = board.getBoard();
      
      for (int i = 0; i < array.length; i++) {
         for (int j = 0; j < array[0].length; j++) {
            
            if (array[i][j] == board.getEMPTY_SQUARE()) {
               assertEquals(board.isEmpty(i, j), true);
            } else {
               assertEquals(board.isEmpty(i, j), false);
            }
         }
      }
   }
   
   /**
    * Test of isEmpty method, of class BoardLogic.
    */
   @org.junit.Test(expected = IllegalArgumentException.class)
   public void testIsEmptyShallThrowIllegalArgumentExceptionIfParameterOutOfBoardRange4() {
      System.out.println("isEmpty");
      board.isEmpty(0, 5);
   }

   /**
    * Test of draw method, of class BoardLogic.
    */
   @org.junit.Test
   public void testDrawShallReturnNumbersBetweenZeroAndFiveIncludes() {
      System.out.println("testDrawShallReturnNumbersBetweenZeroAndFiveIncludes");
      int value;
      //6 * 6 because there is 6 differents value and 6 of each value in the starting deck
      //-1 because the first is played at construction
      for (int i = 0; i < 6 * 6 - 1; i++) {
         value = board.draw();
         assertTrue(value <= board.getMAX_VALUE());
         assertTrue(value >= board.getMIN_VALUE());
      }
   }
   
   /**
    * Test of draw method, of class BoardLogic.
    */
   @org.junit.Test
   public void testDrawShallSendAnIllegalStateExceptionIfThereIsNoMoreCards() {
      System.out.println("testDrawShallSendAnIllegalStateExceptionIfThereIsNoMoreCards");
      boolean result = false;
      //6 * 6 because there is 6 differents value and 6 of each value in the starting deck
      for (int i = 0; i < 6 * 6 + 1; i++) {
         try {
            board.draw();
         } catch (IllegalStateException e) {
            result = true;
         }
      }
      //we shall have go through the catch clause
      assertTrue(result);
   }

   /**
    * helpfull methode that return true if an grid contains the specified row
    * @param array the grid
    * @param searchFor the specified row
    * @return true if it contains the specified row, else false
    */
   public boolean contains(Integer[][] array, Integer[] searchFor) {
      boolean result = false;
      for (Integer[] row : array) {
         result = row.length == searchFor.length;
         for (int i = 0; i < row.length && result; i++) {
            if(row[i] != searchFor[i]) {
               result = false;
               break;
            }
         }
         if(result) {
            return result;
         }
      }
      return result;
   }
   
   /**
    * Test of isPlayablePosition method at the initialization of the board, of class BoardLogic.
    * Only {1,2},{2,1},{3,2},{2,3} should be playable at beginning
    */
   @org.junit.Test
   public void testIsPlayablePositionAtStartShallOnlyBePlayable4Square() {
      System.out.println("isPlayablePosition");
      
      Integer[][] expectedSquaresPossibles = {{1,2},{2,1},{3,2},{2,3}};
      
      int count = 0;
      final int expectedResult = 4;
      
      int[][] array = board.getBoard();
      
      for (int i = 0; i < array.length; i++) {
         for (int j = 0; j < array[0].length; j++) {
            Integer[] square = {i, j};
            if (board.isPlayablePosition(i, j) && contains(expectedSquaresPossibles, square)) {
               ++count;
            }
         }
      }
      assertEquals(expectedResult, count);
   }

   /**
    * Test of playAt method, of class BoardLogic.
    */
   @org.junit.Test
   public void testPlayAtShallReturnTrueIfTheMoveIsEffectiveOrFalseIfImpossibleMove() {
      System.out.println("testPlayAtShallReturnTrueIfTheMoveIsEffectiveOrFalseIfImpossibleMove");
      assertFalse(board.playAt(1, 1, 1));
      assertTrue(board.playAt(1, 2, 1));
      assertTrue(board.playAt(1, 1, 1));
   }
   
   /**
    * Test of playAt method, of class BoardLogic.
    */
   @org.junit.Test
   public void testPlayAtShallReturnFalseIfWeTryToPlayAnImpossibleCard() {
      System.out.println("testPlayAtShallReturnFalseIfWeTryToPlayAnImpossibleCard");
      assertFalse(board.playAt(1, 2, 10));
      
      
   }

   /**
    * helpfull method to fullfill the grid and let first row and column empty
    */
   private void fullOfFiveButFirstRowAndColumn() {
      board.playAt(1, 2, 5);
      board.playAt(2, 1, 5);
      board.playAt(3, 2, 5);
      board.playAt(2, 3, 5);
      board.playAt(1, 1, 5);
      board.playAt(1, 3, 5);
      board.playAt(3, 1, 5);
      board.playAt(3, 3, 5);
      board.playAt(1, 4, 5);
      board.playAt(4, 1, 5);
      board.playAt(2, 4, 5);
      board.playAt(4, 2, 5);
      board.playAt(3, 4, 5);
      board.playAt(4, 3, 5);
      board.playAt(4, 4, 5);
   }
   
   /**
    * Test of win method, of class BoardLogic.
    */
   @org.junit.Test
   public void testWinShallReturnGAME_NOT_ENDEDBeforeFullGrid() {
      System.out.println("testWinShallReturnGAME_NOT_ENDEDBeforeFullGrid");
      assertEquals(board.getGAME_NOT_ENDED(), board.win());
      
      fullOfFiveButFirstRowAndColumn();
      board.playAt(0, 1, 5);
      board.playAt(0, 2, 5);
      board.playAt(0, 3, 5);
      board.playAt(0, 4, 5);
      board.playAt(0, 0, 1);
      board.playAt(1, 0, 2);
      board.playAt(2, 0, 5);
      board.playAt(3, 0, 3);
      //only 1 square left
      assertEquals(board.getGAME_NOT_ENDED(), board.win());
      
      //a wrong move shall not increase the number of turns
      board.playAt(3, 0, 3); //same move that previous
      //still 1 square left
      assertEquals(board.getGAME_NOT_ENDED(), board.win());
      
      board.playAt(4, 0, 4);
      //grid is full
      assertNotEquals(board.getGAME_NOT_ENDED(), board.win());
   }

   /**
    * Test of win method, of class BoardLogic.
    */
   @org.junit.Test
   public void testWinShallReturnROWorCOLUMN_WINDependentlyTheWinner() {
      System.out.println("testWinShallReturnROWorCOLUMN_WINDependentlyTheWinner");
      
      //ROW_WIN scenario
      fullOfFiveButFirstRowAndColumn();
      board.playAt(0, 1, 5);
      board.playAt(0, 2, 5);
      board.playAt(0, 3, 5);
      board.playAt(0, 4, 5);
      board.playAt(0, 0, 1);
      board.playAt(1, 0, 2);
      board.playAt(2, 0, 5);
      board.playAt(3, 0, 3);
      board.playAt(4, 0, 4);
      assertEquals(board.getROW_WIN(), board.win());
      
      //COLUMN_WIN scenario
      board = new BoardLogic(false);
      fullOfFiveButFirstRowAndColumn();
      board.playAt(0, 1, 2);
      board.playAt(0, 2, 5);
      board.playAt(0, 3, 3);
      board.playAt(0, 4, 4);
      board.playAt(0, 0, 1);
      board.playAt(1, 0, 5);
      board.playAt(2, 0, 5);
      board.playAt(3, 0, 5);
      board.playAt(4, 0, 5);
      assertEquals(board.getCOLUMN_WIN(), board.win());
      
      //EQUALS_WIN scenario
      board = new BoardLogic(false);
      fullOfFiveButFirstRowAndColumn();
      board.playAt(0, 1, 2);
      board.playAt(0, 2, 5);
      board.playAt(0, 3, 3);
      board.playAt(0, 4, 4);
      board.playAt(0, 0, 1);
      board.playAt(1, 0, 2);
      board.playAt(2, 0, 5);
      board.playAt(3, 0, 3);
      board.playAt(4, 0, 4);
      assertEquals(board.getEQUALS_WIN(), board.win());
   }
}
