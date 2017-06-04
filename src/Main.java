public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser(args[0]);
        Board board = parser.createBoard();
        board.printBoard();

        Solver solver = new Solver(board);
        solver.solve();
    }
}
