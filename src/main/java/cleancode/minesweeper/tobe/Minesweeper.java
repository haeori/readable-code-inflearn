package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE]; // 실무에서 점진적 리팩토링이 중요
    public static final int LAND_MINE_COUNT = 10;

    // 더이상 main 함수에 존재하지 않으므로 상수 제외 static 키워드 일괄 제거
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public void run() {
        showGameStartComments();
        initializeGame();
        // 이니셜라이즈 종료되므로 의미 단락 분리

        while (true) {
            try {
                showBoard();
                // showBoard 후 게임 진행시키므로 의미 단락 분리

                if (doesUserWinTheGame()) { // 추상화 레벨이 맞지 않는 구체적 로직을 메서드로 추출
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser(); // 사용할 변수가 가깝게 선언되도록 변경하였다가, 객체가 매번 재생성되어 상수로 변경
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput); // 특정 좌표에서 수행할 행동을 분리한 메서드
            } catch (AppException e) { // 의도한 예외
                System.out.println(e.getMessage());
            } catch (Exception e) { // 의도하지 않은 예외
                System.out.println("프로그램에 문제가 생겼습니다.");
                e.printStackTrace(); // printStackTrace는 실무에서는 안티패턴이며 로그로 남겨야 함
            }
        }
    } // 모든 게임 로직을 본 클래스로 이동

    private void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) { // if-else 무분별한 사용 대신 if로 조건 기술 및 early return 적용
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }
        // if문 끝날 때마다 환기를 위해 의미 단락 분리

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
//                BOARD[selectedRowIndex][selectedColIndex] = Cell.ofLandMine(); // 이제 Flag sign으로 Cell을 갈아끼우는 것이 아니라, Cell 객체 내에 깃발 꽂았다는 플래그 불필요
                BOARD[selectedRowIndex][selectedColIndex].open(); // 그러나 셀을 열었으므로 열림 처리 필요
                changeGameStatusToLose();
                return;
            }
            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new AppException("잘못된 번호를 선택하셨습니다."); // 사용자 입력에 대한 예외처리
    }

    private void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
    }

    private boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        int selectedRowIndex = convertRowFrom(cellInputRow);
        return selectedRowIndex;
    }

    private int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        int selectedColIndex = convertColFrom(cellInputCol); // 전치사로 자연스럽게 의미 추측되도록 메서드 레벨 추상화
        return selectedColIndex;
    }

    private String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine(); // 유저 액션 입력
    }

    private String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) { // game status를 변경하는 로직을 메서드로 추출
            changeGameStatusToWin(); // 세부 구현부에 대한 내용 추론이 되도록 수정
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private boolean isAllCellChecked() { // 중첩 반복문 메서드로 분리 및 stream 활용하여 3중 depth 해소
        return Arrays.stream(BOARD) // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String>
                .noneMatch(Cell::isChecked);
    }

    private int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if (rowIndex < 0 || rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }

        return rowIndex;
    }

    private int convertColFrom(char cellInputCol) {
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
                throw new AppException("잘못된 입력입니다."); // 존재하지 않는 위치에 대한 접근으로 인해 발생하는 exception에 대한 예외처리
        }
    }

    private void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col].getSign() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) { // i, j를 명확한 명칭인 row, col로 리네이밍
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = Cell.create();
            }
        }
        // 반복문 종료시마다 작업이 하나 끝난 것이므로 환기를 위해 단락 분리

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            BOARD[row][col].turnOnLandMine(); // LAND_MINES 제거
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) { // 부정연산자를 사용하지 않아도 되는 상황이므로 제거 후 로직 변경
//                    NEARBY_LAND_MINE_COUNTS[row][col] = 0; // create 시에 이미 0을 세팅하여 삭제
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                BOARD[row][col].updateNearbyLandMineCount(count);
            }
        }
    }

    private int countNearbyLandMines(int row, int col) {
        int count = 0; // 사용할 위치와 가깝게 선언
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private void open(int row, int col) { // 재귀함수
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) { // board라는 인덱스를 벗어나 판을 벗어난 경우
            return;
        }
        if (BOARD[row][col].isOpened()) { // Cell이 열려있으면 return. 닫혀있지 않다 -> 열려있다 동일 의미
            return;
        }
        if (isLandMineCell(row, col)) { // 지뢰 Cell이면 리턴
            return;
        }

        BOARD[row][col].open(); // if-else 어느 쪽이든 open이 필요해져 else문 삭제

        if (BOARD[row][col].hasLandMineCount()) { // 주변에 지뢰가 있는 경우
//            BOARD[row][col] = Cell.ofNearbyLandMineCount(NEARBY_LAND_MINE_COUNTS[row][col]); // Cell 객체 내에 근처 지뢰 개수를 저장하므로 불필요
            return;
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
