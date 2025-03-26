package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class Minesweeper {
    public static final int BOARD_ROW_SIZE = 14;
    public static final int BOARD_COL_SIZE = 18;

    // 공백 적절히 추가(이전 강의 해당)

    private final GameBoard gameBoard = new GameBoard(BOARD_ROW_SIZE, BOARD_COL_SIZE); // BOARD 사용 위치 없어진 것 확인 후 제거. 실무에서는 점진적 리팩토링이 중요
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();
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
        int selectedColIndex = boardIndexConverter.getSelectedColIndex(cellInput, gameBoard.getColSize());
        int selectedRowIndex = boardIndexConverter.getSelectedRowIndex(cellInput, gameBoard.getRowSize());

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

}
