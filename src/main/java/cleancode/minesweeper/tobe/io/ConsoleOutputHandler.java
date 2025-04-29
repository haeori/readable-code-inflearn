package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.AppException;
import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;

import java.util.List;
import java.util.stream.IntStream;

public class ConsoleOutputHandler implements OutputHandler {
    @Override
    public void showGameStartComments(GameLevel gameLevel) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println("난이도: " + gameLevel.getLevelName());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void showBoard(GameBoard board) { // Cell[][] 대신 GameBoard 클래스의 board를 사용하도록 변경
        String alphabets = generateColAlphabets(board);

        System.out.println("    " + alphabets);
        for (int row = 0; row < board.getRowSize(); row++) {
            System.out.printf("%2d  ", row + 1);
            for (int col = 0; col < board.getColSize(); col++) {
                System.out.print(board.getSign(row, col) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // 상속 구조에서, 부모 클래스의 인스턴스를 자식클래스의 인스턴스로 치환할 수 있어야 함
    // 자식 클래스는 부모 클래스의 책임을 준수하며, 부모 클래스 행동을 변경하지 않아야 함
    // LSP 위반시 불필요한 타입 체크가 발생할 수 있다.
    // 부모-자식. 슈퍼-서브. 상위-하위. Base-Derived 등으로 표현
    /* LSP 예시
        class Parent {
         public Result doSomething() { ... }
       }
        class Child extends Parent {}
        Result r = new Parent().doSomething(); // 가능
        Result r = new Child().doSomething(); // 가능
     */

    private String generateColAlphabets(GameBoard board) {
        List<String> alphabets = IntStream.range(0, board.getColSize())
                .mapToObj(index -> (char) ('a' + index))
                .map(Object::toString)
                .toList();
        return String.join(" ", alphabets);
    }

    @Override
    public void showGameWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }

    @Override
    public void showGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }

    @Override
    public void showCommentForSelectingCell() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
    }

    @Override
    public void showCommentForUserAction() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }

    @Override
    public void showExceptionMessage(AppException e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void showSimpleMessage(String message) {
        System.out.println(message);
    }
}
