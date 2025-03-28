package cleancode.minesweeper.tobe.io;

public interface InputHandler {
    // Minesweeper 입장에서는 ConsoleInputHandler, ConsoleOutputHandler가 너무 저수준 모듈임
    // 게임 실행 환경이 콘솔에서 웹으로 변경되면 게임 규칙과 도메인, 요구사항이 변하지 않지만 Minesweeper의 코드가 바뀌어야 함
    // Minesweeper 코드에 ConsoleInputHandler, ConsoleOutputHandler가 직접적으로 박혀있기 때문
    // 따라서  인터페이스를 만들어 ConsoleInputHandler, ConsoleOutputHandler를 추상화하여 의존성 역전의 법칙을 적용해야 함

    // 의존성 역전으로 인해 Minesweeper가 더 이상 ConsoleInputHandler, ConsoleOutputHandler를 신경쓰지 않아도 됨

    String getUserInput();

}
