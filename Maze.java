import java.util.*;
import java.io.*;
public class Maze{

  private char[][] maze;
  private boolean animate;//false by default
  private int startR;
  private int startC;
  private int[][] moves = {{-1,0},{0,1},{1,0},{0,-1}};


  /*Constructor loads a maze text file, and sets animate to false by default.

  1. The file contains a rectangular ascii maze, made with the following 4 characters:
  '#' - Walls - locations that cannot be moved onto
  ' ' - Empty Space - locations that can be moved onto
  'E' - the location of the goal (exactly 1 per file)
  'S' - the location of the start(exactly 1 per file)

  2. The maze has a border of '#' around the edges. So you don't have to check for out of bounds!

  3. When the file is not found OR the file is invalid (not exactly 1 E and 1 S) then:
  throw a FileNotFoundException or IllegalStateException
  */
  public Maze(String filename) throws FileNotFoundException{
    int rows = 1;
    animate = false;
    int hasStart = 0;
    int hasEnd = 0;

    File text = new File(filename);
    Scanner in = new Scanner(text);
    String n = in.nextLine();
    int cols = n.length();
    while(in.hasNextLine()){
      rows++;
      in.nextLine();
    }

    in = new Scanner(text);
    maze = new char[rows][cols];
    for(int r = 0; r < maze.length; r++){
      if(hasStart>1||hasEnd>1) throw new IllegalStateException();
      String line = in.nextLine();
      for(int c = 0; c < line.length(); c++){
        if(line.charAt(c)=='S') {
          startR = r;
          startC = c;
          hasStart++;
        }
        if(line.charAt(c)=='E') hasEnd++;
        maze[r][c]=line.charAt(c);
      }
    }
    if(hasStart!=1 || hasStart!=1) throw new IllegalStateException();

  }

  private void wait(int millis){
    try {
      Thread.sleep(millis);
    }
    catch (InterruptedException e) {
    }
  }

  public void setAnimate(boolean b){
    animate = b;
  }

  public void clearTerminal(){
    //erase terminal, go to top left of screen.
    System.out.println("\033[2J\033[1;1H");
  }


  /*Wrapper Solve Function returns the helper function
  Note the helper function has the same name, but different parameters.
  Since the constructor exits when the file is not found or is missing an E or S, we can assume it exists.
  */
  public int solve(){
    maze[startR][startC] = '@';
    return solve(startR, startC);
  }

  /*
  Recursive Solve function:

  A solved maze has a path marked with '@' from S to E.

  Returns the number of @ symbols from S to E when the maze is solved,
  Returns -1 when the maze has no solution.

  Postcondition:
  The S is replaced with '@' but the 'E' is not.
  All visited spots that were not part of the solution are changed to '.'
  All visited spots that are part of the solution are changed to '@'
  */
  private int solve(int row, int col){ //you can add more parameters since this is private

    //automatic animation! You are welcome.
    if(animate){
      clearTerminal();
      System.out.println(this);
      wait(50);
    }
    //loop through moves; order: up,right,down,left
    for(int[] move:moves){
      int nextR = row+move[0];
      int nextC = col+move[1];
      if(maze[nextR][nextC] =='E') return 1;
      if(maze[nextR][nextC] ==' ') {
        maze[nextR][nextC] = '@';
        int ans = solve(nextR, nextC);
        if (ans != -1) return 1+ans;
        else{
          maze[nextR][nextC] = '.';
        }
      }

    }
    return -1; //no solution
  }

  public String toString(){
    String ans = "";
    for(int r = 0; r < maze.length; r++){
      for(int c = 0; c < maze[r].length; c++){
        ans+=maze[r][c];
      }
      ans+="\n";
    }
    return ans;
  }

  public static void main(String args[]){
    try{
      Maze m = new Maze(args[0]);
      m.setAnimate(true);
      System.out.println(m.solve());
      System.out.println(m);
    }
    catch(FileNotFoundException e){
      System.out.println("File not Found");
    }
  }

}
