import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private int[] tiles;
    private int zeroPos;
    private int hamming, manhattan, size, N;
    public Board(int[][] tiles){
        size = tiles.length;
        N = size * size;
        this.tiles = new int[N];
        int k = 0;
        for(int i = 0; i < size; ++i){
            for(int j = 0; j < size; ++j){
                this.tiles[k++] = tiles[i][j];
                if(tiles[i][j] == 0)
                    zeroPos = k;
            }
        }
        calculateDistances();
    }
    private Board(int[] tiles, int zeroPos){
        this.tiles = tiles;
        this.zeroPos = zeroPos;
        calculateDistances();
    }
    public String toString(){
        StringBuilder boardRepr = new StringBuilder();
        for(int i = 0; i < size; ++i){
            StringBuilder rowRepr = new StringBuilder();
            for(int j = 0; j < size; ++j){
                rowRepr.append(tileAt(i, j)).append(" ");
            }
            boardRepr.append(rowRepr).append(System.lineSeparator());
        }
        return boardRepr.toString();
    }
    public int tileAt(int row, int col){
        return tiles[row * size + col];
    }
    public int size(){
        return size;
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
        return Arrays.equals(this.tiles, that.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.tiles);
    }

    public Iterable<Board> neighbors(){
        Stack<Board> neighbors = new Stack<>();
        int[] changes = {size, -size, -1, 1};
        for(int change: changes){
            int newZeroPos = zeroPos + change;
            if(newZeroPos < 0 || newZeroPos >= N)
                continue;
            int[] _tiles = tiles.clone();
            swap(_tiles, zeroPos, newZeroPos);
            Board board = new Board(_tiles, newZeroPos);
            neighbors.push(board);
        }
        return neighbors;
    }
    public boolean isSolvable(){
        long numInversions = numInversions();
        if(size % 2 == 1)
            return numInversions % 2 == 0;
        return (numInversions + N / zeroPos) % 2 == 1;
    }

    private long numInversions(){
        long numInversions = 0;
        BITree biTree = new BITree(N);
        for (int i = 0; i < N; ++i) {
            if (tiles[i] == 0)
                    continue;
            numInversions += biTree.getSum(N) - biTree.getSum(tiles[i]);
            biTree.update(tiles[i], 1);
        }
        return numInversions;
    }

    private void calculateDistances(){
        for(int i = 0; i < N; ++i){
            int Y = (tiles[i][j] - 1) / size, X = (tiles[i][j] - 1) % size, ind = i * size + j + 1;
            manhattan += Math.abs(X - j) + Math.abs(Y - i);
            hamming += (tiles[i][j] == ind ? 0 : 1);
        }
    }

    private void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int[][] tiles = {{1, 2, 3, 4}, {5, 6, 0, 8}, {9, 10, 7, 11}, {13, 14, 15, 12}};
        Board board = new Board(tiles);
        System.out.print(board.isSolvable());
    }
}
