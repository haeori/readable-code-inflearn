package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.AppException;
import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;

public interface OutputHandler {
    // 기존 클래스의 인터페이스를 만들 때, 선언부만 남기고 구현체를 삭제하고 private 메서드를 제거
    // 접근제어자도 삭제
    // 인터페이스는 상수, 추상 메서드만 가질 수 있음
    // 이후 구현체에서는 인터페이스의 메서드를 구현하여 Override 함. private 메서드는 구현체 내부에서 사용

    void showGameStartComments(GameLevel gameLevel);

    void showBoard(GameBoard board);
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

    void printGameWinningComment();

    void printGameLosingComment();

    void printCommentForSelectingCell();

    void printCommentForUserAction();

    void printExceptionMessage(AppException e);

    void printSimpleMessage(String message);
}
