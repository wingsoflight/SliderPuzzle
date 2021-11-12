import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[] tiles;
    private int zeroPos;
    private final int size;
    private int hamming;
    private int manhattan;
    private final int N;
    public Board(int[][] tiles){
        size = tiles.length;
        N = size * size;
        this.tiles = new int[N];
        int k = 0;
        for(int i = 0; i < size; ++i){
            for(int j = 0; j < size; ++j){
                if(tiles[i][j] == 0)
                    zeroPos = k;
                this.tiles[k++] = tiles[i][j];
            }
        }
        calculateDistances();
    }
    private Board(int[] tiles, int zeroPos, int size, int N){
        this.tiles = tiles;
        this.zeroPos = zeroPos;
        this.size = size;
        this.N = N;
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
//        return "";
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
    public Iterable<Board> neighbors(){
        List<Board> neighbors = new ArrayList<>();
        int newZeroPos;
        int[] changes = {-size, size, -1, 1};
        for(int change: changes){
            if(zeroPos % size == 0 && change == -1 || zeroPos % size == size - 1 && change == 1)
                continue;
            newZeroPos = zeroPos + change;
            if(newZeroPos >= N || newZeroPos < 0)
                continue;
            int[] _tiles = new int[N];
            System.arraycopy(tiles, 0, _tiles, 0, N);
            swap(_tiles, zeroPos, newZeroPos);
            Board board = new Board(_tiles, newZeroPos, size, N);
            neighbors.add(board);
        }
        return neighbors;
    }
    public boolean isSolvable(){
        int n = this.size();
        long numInversions = numInversions();
        if(n % 2 == 1)
            return numInversions % 2 == 0;
        return (numInversions + zeroPos / size) % 2 == 1;
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
            if(tiles[i] == 0){
                continue;
            }
            manhattan += Math.abs(i / size - (tiles[i] - 1) / size);
            manhattan += Math.abs(i % size - (tiles[i] - 1) % size);
            hamming += (tiles[i] == i + 1 ? 0 : 1);
        }
    }


    private void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void main(String[] args) {
        int[][] tiles = {{8,1,3},{4,0,2},{7,6,5}};
        Board board = new Board(tiles);
        System.out.println(board.manhattan);
        System.out.println(board.hamming);
    }
}