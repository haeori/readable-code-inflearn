package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.game.GameRunnable;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

public class Minesweeper implements GameInitializable, GameRunnable {

    private final GameBoard gameBoard;
    private final GameLevel gameLevel;
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    // 런타임 시점에 어떤 구현체가 들어올지 모르나 GameLevel이라는 스펙은 알고 있으므로 게임 수행 가능
    // colSize, rowSize, landMineCount만 알면 게임 수행 가능한 구조
    public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
        gameBoard = new GameBoard(gameLevel);
        this.gameLevel = gameLevel;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    @Override
    public void run() {
        outputHandler.showGameStartComments(gameLevel); // 게임 시작 시 난이도 출력
        gameBoard.initializeGame();

        while (true) {
            try {
                outputHandler.showBoard(gameBoard);

                if (doesUserWinTheGame()) { // 추상화 레벨이 맞지 않는 구체적 로직을 메서드로 추출
                    outputHandler.showGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    outputHandler.showGameLosingComment();
                    break;
                }

                String cellInput = getCellInputFromUser(); // 사용할 변수가 가깝게 선언되도록 변경하였다가, 객체가 매번 재생성되어 상수로 변경
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput); // 특정 좌표에서 수행할 행동을 분리한 메서드
            } catch (AppException e) { // e.getMessage()를 받아도 되지만, e 자체를 받아도 무방
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) { // 의도하지 않은 예외
                outputHandler.showSimpleMessage("프로그램에 문제가 생겼습니다.");
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
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserInput(); // 유저 액션 입력
    }

    private String getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        return inputHandler.getUserInput();
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void checkIfGameIsOver() {
        extracted();
    }

    private void extracted() {
        if (gameBoard.isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private void changeGameStatusToWin() {
        gameStatus = 1;
    }

}
