package server;

import java.util.ArrayList;
import java.util.Collections;

/**
 * class who contains the board and his logic to play the game
 * It allow a 2v2 or a 1v1
 * @author Jimmy Verdasca
 */
public class BoardLogic {
   private boolean doublePlayers;
   private int[][] arrayBoard;
   private boolean[][] nextToFull;
   private ArrayList<Integer> deck;
   private int nbTurn = 0;
   
   private final Integer EMPTY_SQUARE = -1;

   private final Integer MIN_VALUE = 0;
   private final Integer MAX_VALUE = 5;
   
   private final int GAME_NOT_ENDED = -1;
   private final int ROW_WIN = 0;
   private final int COLUMN_WIN = 1;
   private final int EQUALS_WIN = 2;
   
   
   /**
    * constructor
    * @param doublePlayers specify if its a 2v2 or a 1v1 (true for 2v2)
    */
   public BoardLogic(boolean doublePlayers) {
      this.doublePlayers = doublePlayers;
      arrayBoard = new int[5][5];
      nextToFull = new boolean[5][5];
      deck = new ArrayList<>();
      for (int i = 0; i <= MAX_VALUE; i++) {
         for (int j = 0; j <= MAX_VALUE; j++) {
            deck.add(i);
         }
      }
      
      for (int i = 0; i < 5; i++) {
         for (int j = 0; j < 5; j++) {
            arrayBoard[i][j] = EMPTY_SQUARE;
            nextToFull[i][j] = false;
         }
      }
      Collections.shuffle(deck);
      nextToFull[2][2] = true;
      playAt(2, 2, deck.remove(0));
   }
   
   /**
    * allow to know the state of the board
    * @return the array of square representing the board
    */
   public int[][] getBoard() {
      return arrayBoard;
   }
   
   /**
    * determine if a square at a given location is empty
    * @param positionX 1st dimension of arrayBoard
    * @param positionY 2nd dimension of arrayBoard
    * @return true is the square is empty
    * @throws IllegalArgumentException if the location doesn't exist
    */
   public boolean isEmpty(Integer positionX, Integer positionY) throws IllegalArgumentException {
      if (!validate(positionX, positionY)) {
         throw new IllegalArgumentException("those positions doesn't exists X: " + positionX + " Y : " + positionY);
      } else {
         if (arrayBoard[positionX][positionY] == EMPTY_SQUARE) {
            return true;
         } else {
            return false;
         }
      }
   }
   
   /**
    * allow to draw a card from the deck
    * @return the number (from 0 to 5 include) of the card we drew
    * @throws IllegalStateException if there was no more cards in the deck
    */
   public Integer draw() throws IllegalStateException {
      if (deck.isEmpty()) {
         throw new IllegalStateException("you can't draw when dek is empty");
      }
      return deck.remove(0);
   }
   
   /**
    * Method to check if the position is valid for arrayBoard limits
    * @param positionX 1st dimension of arrayBoard
    * @param positionY 2nd dimension of arrayBoard
    * @return true if the location is in the bound of arrayBoard
    */
   private boolean validate(Integer positionX, Integer positionY) {
      if (positionX < 0 || positionY < 0) {
         return false;
      }
      if (positionX >= arrayBoard.length || positionY >= arrayBoard.length) {
         return false;
      }
      return true;
   }
   
   /**
    * check if the location is next to a full square
    * @param positionX 1st dimension of arrayBoard
    * @param positionY 2nd dimension of arrayBoard
    * @return true if the location is next to a full square
    */
   private boolean isNextToFull(Integer positionX, Integer positionY) {
      return nextToFull[positionX][positionY];
   }
   
   /**
    * Method who check if a position is playable with the currect 
    * disposition of cards. 
    * It means that we can't play in a full square 
    * and only next to a full square
    * @param positionX 1st dimension of arrayBoard
    * @param positionY 2nd dimension of arrayBoard
    * @return true if the position is currently playable
    */
   public boolean isPlayablePosition(Integer positionX, Integer positionY) {
      if (validate(positionX, positionY) && isEmpty(positionX, positionY) && isNextToFull(positionX, positionY)) {
         return true;
      } else {
         return false;
      }
   }
   
   /**
    * check if the number of the card is valid (0 to 5 include)
    * @param card number of the card 
    * @return true if the card's number is valid
    */
   private boolean isValidCard(Integer card) {
      if (card < MIN_VALUE || card > MAX_VALUE) {
         return false;
      }
      return true;
   }
   
   /**
    * play a card at given position and return true if the move was effective
    * @param positionX 1st dimension of arrayBoard
    * @param positionY 2nd dimension of arrayBoard
    * @param card the number of the card we want to play
    * @return true if the move was effective
    */
   public final boolean playAt(Integer positionX, Integer positionY, Integer card) {
      if(isValidCard(card) && isPlayablePosition(positionX, positionY)) {
         arrayBoard[positionX][positionY] = card;
         int[] iterate = {-1, 1};
         for (int i : iterate) {
            try {
                  nextToFull[positionX + i][positionY] = true;
            } catch(ArrayIndexOutOfBoundsException e) {
               
            }
            try {
                  nextToFull[positionX][positionY + i] = true;
            } catch(ArrayIndexOutOfBoundsException e) {
               
            }
         }
         nbTurn++;
         return true;
      }
      return false;
   }
   
   /**
    * easy way to invert the view if necessary
    * @return an inverted diagonaly board
    */
   private int[][] invertBoard() {
      int[][] invertedArray = new int[arrayBoard.length][arrayBoard.length];
      for (int i = 0; i < arrayBoard.length; i++) {
         for (int j = 0; j < arrayBoard.length; j++) {
            invertedArray[j][i] = arrayBoard[i][j];
         }
      }
      return invertedArray;
   }
   
   @Override
   public String toString() {
      String result = "";
      for (int[] line : invertBoard()) {
         for (int i : line) {
            result += " " + i;
         }
         result += System.lineSeparator();
         result += System.lineSeparator();
      }
      return result;
   }
   
   /**
    * allow to know if the game has ended and return the winner
    * @return GAME_NOT_ENDED if the game isn't finish 0 if rows are winners or 1 
    *         if column are winners and 2 if there is equality
    */
   public int win() {
      if(nbTurn < 25) {
         return GAME_NOT_ENDED;
      }
      int minLine = 151;
      int minRow = 151;
      for (int[] line : arrayBoard) {
         int temp = getCount(line);
         if (minLine > temp) {
            minLine = temp;
         }
      }
      int[][] invertedArray = invertBoard();
      for (int[] row : invertedArray) {
         int temp = getCount(row);
         if (minRow > temp) {
            minRow = temp;
         }
      }
      
      if(minLine < minRow) {
         return COLUMN_WIN;
      } else if (minLine > minRow) {
         return ROW_WIN;
      } else {
         return EQUALS_WIN;
      }
   }
   
   /**
    * return the count for a given row or column
    * @param list array of 5 square that we want to get the score
    * @return the score
    * @throws IllegalArgumentException if the array has wrong length or content
    */
   private int getCount(int[] list) throws IllegalArgumentException {
      if (list.length != 5) {
         throw new IllegalArgumentException("The list has wrong length : " + list.length);
      }
      
      //count the number of occurence for each number in the list
      int[] counter = new int[MAX_VALUE - MIN_VALUE + 1];
      for (int i : list) {
         counter[i]++;
      }
      
      int sum = 0;
      for (int i = 0; i < counter.length; i++) {
         switch(counter[i]) {
            case 1:
               sum += i;
               break;
            case 2:
               sum += 10 * i;
               break;
            case 3:
               sum += 100;
               break;
            case 4:
               sum += 100 + i;
               break;
            case 5:
               sum += 100 + 10 * i;
               break;
         }
      }
      return sum;
   }
   
   /**
    * getters for constants
    */
   public Integer getEMPTY_SQUARE() {
      return EMPTY_SQUARE;
   }

   public Integer getMIN_VALUE() {
      return MIN_VALUE;
   }

   public Integer getMAX_VALUE() {
      return MAX_VALUE;
   }

   public int getGAME_NOT_ENDED() {
      return GAME_NOT_ENDED;
   }

   public int getROW_WIN() {
      return ROW_WIN;
   }

   public int getCOLUMN_WIN() {
      return COLUMN_WIN;
   }

   public int getEQUALS_WIN() {
      return EQUALS_WIN;
   }
   /**
    * End Of Getters
    */
   
   /**
    * main who test the methodes of the board logic
    * @param args without args it will test a 1v1 if the is any argument,
    *             it will test a 2v2
    */
   public static void main(String... args) {
      BoardLogic board;
      if(args.length == 0) {
         board = new BoardLogic(false);
      } else {
         board = new BoardLogic(true);
      }
      System.out.println(board);
      board.playAt(0, 0, 0);
      System.out.println(board);
      board.playAt(1, 2, 0);
      System.out.println(board);
      board.playAt(2, 1, board.draw());
      System.out.println(board);
   }
}
