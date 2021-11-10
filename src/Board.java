import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;

public class Board {
    private int[][] tiles;
    private int[][] goal;
    private int[] pos;
    private int hamming, manhattan;
    public Board(int[][] tiles){
        this.tiles = tiles;
        calculateDistances();
        pos = getEmptyPos();
    }
    public String toString(){
        int n = this.size();
        StringBuilder boardRepr = new StringBuilder();
        for(int i = 0; i < n; ++i){
            StringBuilder rowRepr = new StringBuilder();
            for(int j = 0; j < n; ++j){
                rowRepr.append(tileAt(i, j)).append(" ");
            }
            boardRepr.append(rowRepr).append(System.lineSeparator());
        }
        return boardRepr.toString();
    }
    public int tileAt(int row, int col){
        return tiles[row][col];
    }
    public int size(){
        return tiles.length;
    }
    public int hamming(){
        return hamming;
    }
    public int manhattan(){
        return manhattan;
    }
    public boolean isGoal(){
        return manhattan == 0;
    }
    public boolean equals(Object y){
        if(this == y)
            return true;
        if(y == null)
            return false;
        if(y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.getTiles(), that.getTiles());
    }
    public Iterable<Board> neighbors(){
        ArrayList<Board> neighbors = new ArrayList<>();
        if(pos == null)
            pos = getEmptyPos();
        int n = this.size();
        int[][] changes = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        for(int[] change: changes){
            int[] _pos = {pos[0] + change[0], pos[1] + change[1]};
            if(_pos[0] < 0 || _pos[0] >= n || _pos[1] < 0 || _pos[1] >= n)
                continue;
            int[][] _tiles = new int[n][n];
            for(int i = 0; i < n; ++i)
                _tiles[i] = tiles[i].clone();
            swap(_tiles, _pos[0], _pos[1], pos[0], pos[1]);
            Board board = new Board(_tiles);
            neighbors.add(board);
        }
        return neighbors;
    }
    public boolean isSolvable(){
        int n = this.size();
        long numInversions = numInversions(tiles);
        if(n % 2 == 1)
            return numInversions % 2 == 0;
        if(pos == null)
            pos = getEmptyPos();
        return (numInversions + pos[0]) % 2 == 1;
    }

    private long numInversions(int[][] arr){
        int n = arr.length;
        long numInversions = 0;
        BITree biTree = new BITree(n * n);
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < n; ++j){
                if(arr[i][j] == 0)
                    continue;
                numInversions += biTree.getSum(n * n) - biTree.getSum(arr[i][j]);
                biTree.update(arr[i][j], 1);
            }
        }
        return numInversions;
    }

    private void calculateDistances(){
        int n = this.size();
        if(goal == null)
            goal = generateGoal(n);
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < n; ++j){
                if(tiles[i][j] == 0)
                    continue;
                int Y = (tiles[i][j] - 1) / n, X = (tiles[i][j] - 1) % n;
                manhattan += Math.abs(X - j) + Math.abs(Y - i);
                hamming += (tiles[i][j] == goal[i][j] ? 0 : 1);
            }
        }
    }
    private int[][] generateGoal(int n ){
        int[][] goal = new int[n][n];
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < n; ++j){
                goal[i][j] = i * n + j + 1;
            }
        }
        goal[n - 1][n - 1] = 0;
        return goal;
    }

    private int[][] getTiles(){
        return tiles;
    }

    private int[] getEmptyPos(){
        int n = this.size();
        int[] pos = {-1, -1};
        for(int i = 0; i < n; ++i){
            for(int j = 0; j < n; ++j){
                if(tiles[i][j] == 0){
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

    private void swap(int[][] arr, int i, int j, int _i, int _j){
        int tmp = arr[i][j];
        arr[i][j] = arr[_i][_j];
        arr[_i][_j] = tmp;
    }

    public static void main(String[] args) {
        int[][] tiles = {{1, 2, 3, 4}, {5, 6, 0, 8}, {9, 10, 7, 11}, {13, 14, 15, 12}};
        Board board = new Board(tiles);
        System.out.print(board.isSolvable());
    }
}
