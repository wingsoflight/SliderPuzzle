import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;

public class Solver {
    public class SearchNode implements Comparable<SearchNode>{
        private int moves;
        private Board board;
        private SearchNode parent;

        public SearchNode(SearchNode parent, Board board, int moves){
            this.parent = parent;
            this.board = board;
            this.moves = moves;
        }

        public boolean createsLoop(){
            SearchNode parent = this.parent;
            while (parent != null){
                if(this.board.equals(parent.board))
                    return true;
                parent = parent.parent;
            }
            return false;
        }

        @Override
        public int compareTo(SearchNode that) {
            return this.board.manhattan() - that.board.manhattan();
        }
    }
    Board initial;
    int moves;
    MinPQ<SearchNode> minPQ;
    Stack<Board> solution;
    public Solver(Board inital){
        if(inital == null)
            throw new IllegalArgumentException("Initial board cannot be NULL");
        if (!inital.isSolvable())
            throw new RuntimeException("Provided initial board is unsolvable");
        this.initial = inital;
        minPQ = new MinPQ<>();
        solution = new Stack<>();
        solve();
    }
    public int moves(){
        return moves;
    }
    public Iterable<Board> solution(){
        return solution;
    }
    private void solve(){
        SearchNode searchNode = new SearchNode(null, initial, 0);
        minPQ.insert(searchNode);
        while (!minPQ.isEmpty()){
            searchNode = minPQ.delMin();
            if (searchNode.board.isGoal())
                break;
            for(Board board: searchNode.board.neighbors()){
                SearchNode neighborNode = new SearchNode(searchNode, board, searchNode.moves + 1);
                if(searchNode.createsLoop())
                    continue;
                minPQ.insert(neighborNode);
            }
        }
        moves = searchNode.moves;
        while (searchNode != null){
            solution.push(searchNode.board);
            searchNode = searchNode.parent;
        }
    }
    public static void main(String[] args) {
        int[][] arr = {{5, 3, 1, 4}, {10, 2, 8, 7}, {14, 13, 0, 11}, {6,9,15,12}};
        Board board = new Board(arr);
        Solver solver = new Solver(board);
        System.out.println(solver.moves());
        for (Board _b: solver.solution()){
            System.out.println(_b);
        }
    }

}
