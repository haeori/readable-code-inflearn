package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.gamelevel.Beginner;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

// 의존성 역전의 법칙(Dependency Inversion Principle)
// 상위 수준의 모듈은 하위 수준의 모듈에 의존해서는 안 됨. 둘 모두 추상화에 의존해야 함
// 고수준 - 추상화 레벨이 높은 쪽, 저수준 - 추상화 레벨이 낮은 쪽. 고수준 모듈이 저수준 모듈에 의존하면 안 됨
// 둘 다 추상화에 의존해야 함. 추상화에 의존하면 구체적인 구현에 의존하지 않아 유연성이 높아짐

// 의존성의 순방향: 고수준 모듈이 저수준 모듈을 참조하는 것
// 의존성의 역방향: 고수준, 저수준 모듈이 모두 추상화에 의존하는 것
// 고수준 모듈은 인터페이스를 통해 추상화된 스펙(메서드 선언부)만 참고하고, 고수준 모듈의 이 요구사항을 구현하는 것은 저수준 모듈의 책임
// 따라서 저수준 모듈이 자유롭게 변경되어도 고수준 모듈에 영향을 주지 않음

// (의존성 순방향) 카페 - sell() -> 커피 인 경우
// (의존성 역방향) 카페 - sell() -> Interface Beverage <- 커피, <- 차, <- 병음료


public class GameApplication { // 확장성을 위해 클래스명도 변경
    public static void main(String[] args) { // main 메서드는 게임을 실행하는 책임만 지도록 수정
        GameLevel gameLevel = new Beginner();
        InputHandler inputHandler = new ConsoleInputHandler();
        OutputHandler outputHandler = new ConsoleOutputHandler();

        Minesweeper minesweeper = new Minesweeper(gameLevel, inputHandler, outputHandler);
        minesweeper.initialize();
        minesweeper.run();
    }
}
