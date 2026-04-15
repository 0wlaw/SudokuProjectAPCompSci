import java.util.ArrayList;
import java.util.Scanner;

public class SudokuGenerator {
    public static final int DIM = 9;
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to sudoku. Generating board.....");
        int[][] board = generateBoard();
        System.out.print("Board generated.\nPlease select a number (recommended 30-50) of cells to be removed, where more removals correlates with increased difficulty (recommended to start with 30)");
        int n = input.nextInt();
        removeNums(board, n);
        boolean[][] locked = lockMask(board);        
        while (!isFull(board)) {
            printBoard(board);
            
            int r =-1;
            int c =-1;
            int num = -1;
            boolean evil = false;
            while (!(r>=0&&r<9&&c>=0&&c<9&&(num>0&&num<10)||evil)){
                evil = false;
                System.out.print("Row (1-9): ");
                r = input.nextInt() - 1;
                System.out.print("Column (1-9): ");
                c = input.nextInt() - 1;
                System.out.print("Enter cell value (1-9): ");
                num = input.nextInt();
                if (locked[r][c]) {
                    evil = true;
                    System.out.println("You can't change existing values");
                }
            }
                        
            int prev = board[r][c];
            board[r][c] = 0;
            
            if (!isValidCell(num, r, c, board)) {
                System.out.println("\nruh roh.... invalid move\ntry again\n");
                board[r][c] = prev;
                continue;
            }
            
            board[r][c] = num;
            System.out.println("\nvalid move :)");
        }
        
        if (isFull(board)) {
            printBoard(board);
            System.out.println("Congratulations! You solved the puzzle!");
        } else {
            System.out.println("Game terminated.");
        }
        
        input.close();
    }     

    public static void testSpeed() {
        long start = System.currentTimeMillis();
        long num = 100000;
        for (int i=0; i<num; i++) {
            generateBoard();
        }
        long end = System.currentTimeMillis();
        double diff = (double) (end - start);
        double avgTime = (diff)/num;
        System.out.println("With a sample size of " + num+ ", the average time to generate a board was "+ (avgTime) + " milliseconds");

    }

    public static int[][] generateBoard() {
        ArrayList<Integer>[][] map = new ArrayList[DIM][DIM];
        int[][] board = new int[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j<DIM; j++) {
                map[i][j] = new ArrayList<Integer>(); 
                for (int k = 0; k<DIM;k++) {
                    map[i][j].add(k+1);
                }
            }
        }
        genRec(board, map);
        return board;
    }

    private static boolean genRec(int[][] arr, ArrayList<Integer>[][] map) {        
        if (isFull(arr)) {return true;}

        int min = DIM+1, mini = 0, minj = 0;
        for (int i = 0; i<DIM; i++) {
            for (int j = 0; j<DIM; j++) {
                if (arr[i][j] == 0 && map[i][j].size()<min) {
                    mini=i;minj=j;
                    min = map[i][j].size();
                }
            }
        }
        if (min == 0) return false;
        ArrayList<Integer> possibilities = new ArrayList<Integer>(map[mini][minj]);
        for (int i = possibilities.size(); i>0; i--) {
            int randIdx = (int) (possibilities.size() * Math.random());
            int num = possibilities.remove(randIdx);
            
            if (isValidCell(num, mini, minj, arr)) {
                arr[mini][minj] = num;
                if (genRec(arr, map)) {
                   return true; 
                }
            }
        }
        arr[mini][minj] = 0;
        return false;
    }

    public static int[][] solveBoard(int[][] board) {
        solveRec(board);
        return board;
    }

    private static boolean solveRec(int[][] arr) {        
        if (isFull(arr)) {return true;}

        int min = DIM+1, mini = 0, minj = 0;
        ArrayList<Integer> possibilities = new ArrayList<Integer>();

        for (int i = 0; i<DIM; i++) {
            for (int j = 0; j<DIM; j++) {
                if (arr[i][j] == 0) {
                    ArrayList<Integer> tempPossibilities = new ArrayList<Integer>();
                    for (int k = 1; k<=9; k++) { 
                        if (isValidCell(k, i, j, arr)) {
                            tempPossibilities.add(k);
                        }
                    }
                    if (tempPossibilities.size()==0) {return false;}

                    if (tempPossibilities.size()<min) {
                        mini=i;minj=j;
                        min=tempPossibilities.size();
                        possibilities=tempPossibilities;
                    }
                    if (possibilities.size()==1) {break;}                    
                }
            }
        }
        
        for (int i = possibilities.size(); i>0; i--) {
            int randIdx = (int) (possibilities.size() * Math.random());
            int num = possibilities.remove(randIdx);
            
            arr[mini][minj] = num;
            if (solveRec(arr)) {
                return true; 
            }
        }
        arr[mini][minj] = 0;
        return false;
    }

    public static void printBoard(int[][] arr) {
        System.out.print("  -------+-------+-------\n | ");
        for (int i = 0; i<DIM; i++) {
            for (int j = 0; j<DIM; j++) {
                System.out.print(arr[i][j] + " ");
                if (j%3==2) {
                    System.out.print("| ");
                    if (j==8) {
                        if (i%3==2) {System.out.print("\n  -------+-------+-------");}
                        if (i!=8) {System.out.print("\n | ");} else {System.out.println("\n\n");}
                    }
                }
            }
        }
    }

    private static boolean isValidCell(int num, int r, int c, int[][] arr) {
        int rb = r/3*3;
        int cb = c/3*3;
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<3; j++) {
                if (arr[rb+i][cb+j]==num) return false;
            }
        }
        for (int i = 0; i<DIM; i++) {
            if (arr[i][c] == num || arr[r][i] == num) return false;
        }
        return true;
    }

    private static boolean isFull(int[][] arr) {
        for (int[] r : arr) {
            for (int e : r) {
                if (e == 0) return false;
            }
        }
        return true;
    }

    public static void removeNums(int[][] board, int n) {
        for (int done = 0; done < n;) {
            int a = (int) (Math.random() * DIM);
            int b = (int) (Math.random() * DIM);
            if (board[a][b]!=0) {
                board[a][b] = 0;
                done++;
            }
        }
    }
    public static boolean[][] lockMask(int[][] board) {
        boolean[][] done = new boolean[DIM][DIM];
        
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (board[i][j] != 0) {
                    done[i][j] = true;
                }
            }
        }
        return done;
    }


}