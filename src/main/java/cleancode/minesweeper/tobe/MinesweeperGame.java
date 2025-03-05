package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static String[][] board = new String[8][10];
    private static Integer[][] landMineCounts = new Integer[8][10];
    private static boolean[][] landMines = new boolean[8][10];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        Scanner scanner = new Scanner(System.in);
        initializeGame();
        while (true) {
            showBoard();
            if (gameStatus == 1) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (gameStatus == -1) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }
            System.out.println("선택할 좌표를 입력하세요. (예: a1)");
            String cellInput = scanner.nextLine(); // 셀 위치 입력이라는 변수 의미를 명확히 나타내도록 리네이밍
            System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
            String userActionInput = scanner.nextLine(); // 유저 액션 입력
            char cellInputCol = cellInput.charAt(0);
            char cellInputRow = cellInput.charAt(1);
            int selectedColIndex = convertColFrom(cellInputCol); // 전치사로 자연스럽게 의미 추측되도록 메서드 레벨 추상화
            int selectedRowIndex = convertRowFrom(cellInputRow);
            if (userActionInput.equals("2")) { // 사용자가 선택한 행위가 깃발 꽂기(2)인 경우
                board[selectedRowIndex][selectedColIndex] = "⚑";
                checkIfGameIsOver();
            } else if (userActionInput.equals("1")) { // 사용자가 선택한 행위가 셀을 열기(1)인 경우
                if (landMines[selectedRowIndex][selectedColIndex]) { // 지뢰를 밟았다면 게임 종료
                    board[selectedRowIndex][selectedColIndex] = "☼";
                    gameStatus = -1;
                    continue;
                } else { // 정상 셀이면 오픈
                    open(selectedRowIndex, selectedColIndex);
                }
                checkIfGameIsOver();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) { // game status를 변경하는 로직을 메서드로 추출
            gameStatus = 1;
        }
    }

    private static boolean isAllCellOpened() {
        boolean isAllOpened = true; // 두 가지 이상의 일을 하는 메서드를 단일 주제로 분리
        for (int row = 0; row < 8; row++) { // isAllOpened를 결정하는 로직을 메서드로 추출
            for (int col = 0; col < 10; col++) {
                if (board[row][col].equals("□")) {
                    isAllOpened = false;
                }
            }
        }
        return isAllOpened;
    }

    private static int convertRowFrom(char cellInputRow) {
        int selectedRowIndex = Character.getNumericValue(cellInputRow) - 1;
        return selectedRowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) { // 불필요한 변수 삭제하고 바로 return하도록 수정
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                return -1;
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < 8; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < 10; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < 8; row++) { // i, j를 명확한 명칭인 row, col로 리네이밍
            for (int col = 0; col < 10; col++) {
                board[row][col] = "□";
            }
        }
        for (int i = 0; i < 10; i++) {
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            landMines[row][col] = true;
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                int count = 0;
                if (!landMines[row][col]) { // 지뢰가 아닌 칸에서만 주변 지뢰 카운트
                    if (row - 1 >= 0 && col - 1 >= 0 && landMines[row - 1][col - 1]) {
                        count++;
                    }
                    if (row - 1 >= 0 && landMines[row - 1][col]) {
                        count++;
                    }
                    if (row - 1 >= 0 && col + 1 < 10 && landMines[row - 1][col + 1]) {
                        count++;
                    }
                    if (col - 1 >= 0 && landMines[row][col - 1]) {
                        count++;
                    }
                    if (col + 1 < 10 && landMines[row][col + 1]) {
                        count++;
                    }
                    if (row + 1 < 8 && col - 1 >= 0 && landMines[row + 1][col - 1]) {
                        count++;
                    }
                    if (row + 1 < 8 && landMines[row + 1][col]) {
                        count++;
                    }
                    if (row + 1 < 8 && col + 1 < 10 && landMines[row + 1][col + 1]) {
                        count++;
                    }
                    landMineCounts[row][col] = count;
                    continue;
                }
                landMineCounts[row][col] = 0;
            }
        }
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) { // 재귀함수
        if (row < 0 || row >= 8 || col < 0 || col >= 10) { // board라는 인덱스를 벗어나 판을 벗어난 경우
            return;
        }
        if (!board[row][col].equals("□")) {
            return;
        }
        if (landMines[row][col]) {
            return;
        }
        if (landMineCounts[row][col] != 0) {
            board[row][col] = String.valueOf(landMineCounts[row][col]);
            return;
        } else {
            board[row][col] = "■";
        }
        open(row - 1, col - 1); // 해당 셀의 주변 8개 셀을 모두 탐색
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
