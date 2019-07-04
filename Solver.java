import java.util.Stack;

public class Solver {
    Stack<Board> solution;
    int numberOfMoves = -1;
    boolean solvable = false;
    Board initial;

    private class SearchNode implements Comparable {
        private Board board;
        private int numberOfMoves;
        private SearchNode previus;

        SearchNode(Board board, int numberOfMoves, SearchNode previus) {
            this.board = board;
            this.numberOfMoves = numberOfMoves;
            this.previus = previus;
        }

        @Override
        public int compareTo(Object o) {
            SearchNode bufferNode = (SearchNode) o;
            return (board.manhattan() + numberOfMoves) - (bufferNode.board.manhattan() + bufferNode.numberOfMoves);
        }
    }

    // знайти рішення для дошки initial
    public Solver(Board initial) {
        solution = new Stack();
        this.initial = initial;
        if (isSolvable()) {
            SearchNode bufferNode;
            MinPQ nodes = new MinPQ();
            nodes.insert(new SearchNode(initial, 0, null));
            Stopwatch watch = new Stopwatch();
            while (!nodes.isEmpty()) {
                bufferNode = (SearchNode) nodes.delMin();
                solution.add(bufferNode.board);
                if (bufferNode.board.isGoal()) {
                    solvable = true;
                    numberOfMoves = bufferNode.numberOfMoves;
                    break;
                }
                for (Board buffer : bufferNode.board.neighbors()) {
                    if (bufferNode.previus == null || !bufferNode.previus.board.equals(buffer))
                        nodes.insert(new SearchNode(buffer, bufferNode.numberOfMoves + 1, bufferNode));
                }
                if (watch.elapsedTime() > 10) {
                    break;
                }
            }
        }
    }

    // чи має початкова дошка розв’язок
    public boolean isSolvable() {
        int noTargetPossCounter;
        noTargetPossCounter = initial.manhattan();
        System.out.println(noTargetPossCounter);
        if (((noTargetPossCounter + 1) % 2)> 0) return false;
        else return true;
    }

    // мінімальна кількість кроків для вирішення дошки, -1 якщо немає рішення
    public int moves() {
        return numberOfMoves;
    }

    // послідовність дошок в найкоротшому рішенні; null якщо немає рішення
    public Iterable<Board> solution() {
        return solution;
    }

    // вирішити
    public static void main(String[] args) {
        In in = new In("D:\\puzzle3x3-unsolvable.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);
        // надрукувати рішення
        if (!solver.isSolvable())
            StdOut.println("Дошка не має розв’язку");
        else {
            StdOut.println("Мінімальна кількість кроків = " + solver.moves());

            for (Board buf : solver.solution) {
                System.out.println(buf.toString());
            }
        }
    }
}
