package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

import java.util.Arrays;
import java.util.Random;

public class Minesweeper {
    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    private final GameBoard gameBoard = new GameBoard(BOARD_ROW_SIZE, BOARD_COL_SIZE); // BOARD 사용 위치 없어진 것 확인 후 제거. 실무에서는 점진적 리팩토링이 중요
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
    // 더이상 main 함수에 존재하지 않으므로 상수 제외 static 키워드 일괄 제거
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public void run() {
        consoleOutputHandler.showGameStartComments();
        gameBoard.initializeGame();

        while (true) {
            try {
                consoleOutputHandler.showBoard(gameBoard);

                if (doesUserWinTheGame()) { // 추상화 레벨이 맞지 않는 구체적 로직을 메서드로 추출
                    consoleOutputHandler.printGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    consoleOutputHandler.printGameLosingComment();
                    break;
                }

                String cellInput = getCellInputFromUser(); // 사용할 변수가 가깝게 선언되도록 변경하였다가, 객체가 매번 재생성되어 상수로 변경
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput); // 특정 좌표에서 수행할 행동을 분리한 메서드
            } catch (AppException e) { // e.getMessage()를 받아도 되지만, e 자체를 받아도 무방
                consoleOutputHandler.printExceptionMessage(e);
            } catch (Exception e) { // 의도하지 않은 예외
                consoleOutputHandler.printSimpleMessage("프로그램에 문제가 생겼습니다.");
            }
        }
    } // 모든 게임 로직을 본 클래스로 이동

    private void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flag(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        // if문 끝날 때마다 환기를 위해 의미 단락 분리

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (gameBoard.isLandMineCell(selectedRowIndex, selectedColIndex)) {
                gameBoard.open(selectedRowIndex, selectedColIndex);
                changeGameStatusToLose();
                return;
            }

            gameBoard.openSurroundedCells(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        throw new AppException("잘못된 번호를 선택하셨습니다."); // 사용자 입력에 대한 예외처리
    }

    private void changeGameStatusToLose() {
        gameStatus = -1;
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
        consoleOutputHandler.printCommentForUserAction();
        return consoleInputHandler.getUserInput(); // 유저 액션 입력
    }

    private String getCellInputFromUser() {
        consoleOutputHandler.printCommentForSelectingCell();
        return consoleInputHandler.getUserInput();
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        if (gameBoard.isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = 1;
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

}
