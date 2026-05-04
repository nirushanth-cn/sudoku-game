import java.util.*;

class SudokuService {
    int SIZE;
    int EMPTY;
    int BLANKS;
    int[][] board_filled;
    int[][] board_template;
    int[][] board_game;
    Random rand;

    public SudokuService() {
        SIZE = 9;
        EMPTY = 0;
        BLANKS = 51;
        board_filled = new int[9][9];
        board_template = new int[9][9];
        board_game = new int[9][9];
        rand = new Random();
    }

    public boolean fillBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                if (board_filled[row][col] == EMPTY) {
                    List<Integer> numbers = getShuffledNumbers();

                    for (int num : numbers) {
                        if (isSafe(board_filled, row, col, num)) {
                            board_filled[row][col] = num;

                            if (fillBoard()) {
                                return true;
                            }

                            board_filled[row][col] = EMPTY;
                        }
                    }
                    return false;
                }
            }
        }
        board_template = Arrays.stream(board_filled).map(int[]::clone).toArray(int[][]::new);
        removeCells(BLANKS);
        return true;
    }

    public void removeCells(int cellsToRemove) {
        while (cellsToRemove > 0) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);

            if (board_template[row][col] != EMPTY) {
                board_template[row][col] = EMPTY;
                cellsToRemove--;
            }
        }
        board_game = Arrays.stream(board_template).map(int[]::clone).toArray(int[][]::new);
    }

    public boolean isSafe(int[][] board, int row, int col, int num) {

        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int boxRow = row - row % 3;
        int boxCol = col - col % 3;

        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<Integer> getShuffledNumbers() {
        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) nums.add(i);
        Collections.shuffle(nums);
        return nums;
    }

    public void printBoard(BoardType boardType) {
        int[][] board = new int[9][9];
        switch (boardType) {
            case FILLED -> board = board_filled;
            case TEMPLATE -> board = board_template;
            case GAME -> board = board_game;
        }

        System.out.print("  ");
        for (int col = 1; col <= 9; col++) {
            if (col % 3 == 1 && col != 1) {
                System.out.print("  ");
            }
            System.out.print(col + " ");
        }
        System.out.println();

        for (int i = 0; i < 9; i++) {

            if (i % 3 == 0 && i != 0) {
                System.out.println("  ------+-------+------");
            }

            char rowLabel = (char) ('A' + i);
            System.out.print(rowLabel + " ");

            for (int j = 0; j < 9; j++) {

                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }

                if (board[i][j] == 0) {
                    System.out.print("- ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public int getBoardValue(BoardType boardType, String pos) {
        int[][] board = new int[9][9];
        switch (boardType) {
            case FILLED -> board = board_filled;
            case TEMPLATE -> board = board_template;
            case GAME -> board = board_game;
        }

        int row = pos.charAt(0) - 'A';
        int col = pos.charAt(1) - '1';
        return board[row][col];
    }

    public void setBoardValue(String pos, int value) {
        if (pos == null || !pos.matches("[A-I][1-9]")) {
            System.out.println("Invalid Position specified!");
            return;
        }
        int row = pos.charAt(0) - 'A';
        int col = pos.charAt(1) - '1';
        board_game[row][col] = value;
    }

    public void getHint() {
        List<String> emptyCells = new ArrayList<>();
        Random rand = new Random();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board_game[row][col] == 0) {
                    String pos = "" + (char) ('A' + row) + (col + 1);
                    emptyCells.add(pos);
                }
            }
        }

        if (emptyCells.isEmpty()) {
            System.out.println("Seems all cells occupied!");
            return;
        }
        String pos = emptyCells.get(rand.nextInt(emptyCells.size()));
        System.out.println("Hint: Cell " + pos + " = " + getBoardValue(BoardType.FILLED, pos));
    }

    public void removeElement(String pos) {
        if (0 != getBoardValue(BoardType.TEMPLATE, pos)) {
            System.out.println("cannot modify pre-filled cells!");
        } else if (0 == getBoardValue(BoardType.GAME, pos)) {
            System.out.println("Cell value already empty!");
        } else {
            setBoardValue(pos, 0);
            System.out.println("Move accepted...");
            System.out.println("Current grid : ");
            printBoard(SudokuService.BoardType.GAME);
        }
    }

    public Boolean insertElement(String pos, Integer value) {
        if (0 != getBoardValue(BoardType.GAME, pos)) {
            System.out.println("Cell already have a value clear it before updating!");
            return false;
        }
        if (value < 1 || value > 9) {
            System.out.println("Value Should Be Between 1 to 9");
            return false;
        }
        setBoardValue(pos, value);
        System.out.println("Move accepted...");
        System.out.println("Current grid : ");
        printBoard(SudokuService.BoardType.GAME);
        return true;
    }

    public void validateLastMove(String lastMve) {
        if (lastMve.isBlank()) {
            System.out.println("Please make a move to check for validity!");
        } else if (!isExistsInSubgrid(lastMve) && !isExistsInRow(lastMve) && !isExistsInColumn(lastMve)) {
            System.out.println("No rule violations detected.");
        }
    }

    private boolean isExistsInSubgrid(String pos) {

        int value = getBoardValue(BoardType.GAME, pos);
        int row = Character.toUpperCase(pos.charAt(0)) - 'A';
        int col = pos.charAt(1) - '1';

        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (i == row && j == col) continue;
                if (board_game[i][j] == value) {
                    System.out.println("Number " + value + " already exists in the same " + pos + " subgrid.");
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isExistsInRow(String pos) {
        int value = getBoardValue(BoardType.GAME, pos);
        int row = Character.toUpperCase(pos.charAt(0)) - 'A';
        int col = pos.charAt(1) - '1';

        for (int j = 0; j < 9; j++) {
            if (j == col) continue;
            if (board_game[row][j] == value) {
                System.out.println("Number " + value + " already exists in the same Row " + pos.charAt(0) + ".");
                return true;
            }
        }
        return false;
    }

    public boolean isExistsInColumn(String pos) {
        int value = getBoardValue(BoardType.GAME, pos);
        int row = Character.toUpperCase(pos.charAt(0)) - 'A';
        int col = pos.charAt(1) - '1';

        for (int i = 0; i < 9; i++) {
            if (i == row) continue;
            if (board_game[i][col] == value) {
                System.out.println("Number " + value + " already exists in the same Column " + pos.charAt(1) + ".");
                return true;
            }
        }

        return false;
    }

    public boolean checkCompletion() {
        return Arrays.deepEquals(board_filled, board_game);
    }

    public void startGame() {
        fillBoard();
        boolean isCompleted = false;

        System.out.println("Welcome to Sudoku! enter command quit to exit any time");
        System.out.println("Here is your puzzle:");
        printBoard(SudokuService.BoardType.GAME);

        String lastmve = "";
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (checkCompletion()) {
                System.out.println("You have successfully completed the Sudoku puzzle!");
                System.out.println("Press any key to play again...");
                scanner.nextLine();
                isCompleted = true;
                break;
            }
            System.out.print("Enter command (e.g., A3 4, C5 clear, hint, check, quit):");
            String input = scanner.nextLine().toUpperCase();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Exiting program...");
                break;
            } else if (input.equalsIgnoreCase("check")) {
                validateLastMove(lastmve);
            } else if (input.equalsIgnoreCase("hint")) {
                getHint();
            } else if (input.matches("(?i)^[A-Ia-i][1-9] clear$")) {
                removeElement(input.substring(0, 2));
                lastmve = "";
            } else if (input.matches("(?i)^[A-Ia-i][1-9] [1-9]$")) {
                lastmve = "";
                if (insertElement(input.substring(0, 2), Integer.valueOf(input.substring(3, 4)))) {
                    lastmve = input.substring(0, 2);
                }
            } else {
                System.out.println("Invalid command try again!");
            }
        }
        if (isCompleted) {
            startGame();
        }
        scanner.close();
    }

    public enum BoardType {
        FILLED, TEMPLATE, GAME
    }
}
