import edu.princeton.cs.algs4.MinPQ;

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

        public Board getBoard(){
            return board;
        }

        public SearchNode getParent() {
            return parent;
        }

        public int getMoves() {
            return moves;
        }

        public boolean createsLoop(){
            SearchNode parent = this.getParent();
            while (parent != null){
                if(this.getBoard().equals(parent.getBoard()))
                    return true;
                parent = parent.getParent();
            }
            return false;
        }

        @Override
        public int compareTo(SearchNode that) {
            int a = this.getMoves() + this.getBoard().manhattan();
            int b = that.getMoves() + that.getBoard().manhattan();
            return a - b;
        }
    }
    Board initial;
    int moves;
    MinPQ<SearchNode> minPQ;
    ArrayList<Board> solution;
    public Solver(Board inital){
        if(inital == null)
            throw new IllegalArgumentException("Initial board cannot be NULL");
        if (!inital.isSolvable())
            throw new RuntimeException("Provided initial board is unsolvable");
        this.initial = inital;
        minPQ = new MinPQ<>();
        solution = new ArrayList<>();
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
            if (searchNode.getBoard().isGoal())
                break;
            for(Board board: searchNode.getBoard().neighbors()){
                SearchNode neighborNode = new SearchNode(searchNode, board, searchNode.getMoves() + 1);
                if(searchNode.createsLoop())
                    continue;
                minPQ.insert(neighborNode);
            }
        }
        moves = searchNode.getMoves();
        while (searchNode != null){
            solution.add(0, searchNode.getBoard());
            searchNode = searchNode.getParent();
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
