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

/*
 * DIP (Dependency Inversion Principle) : 의존성 역전의 법칙
 * Spring Framework의 IoC/DI와 밀접한 관련이 있음(3대원칙 IoC/DI, PSA, AOP)
 *
 * DI (Dependency Injection): 의존성 주입 - "3"
 * 필요한 의존성을 직접 생성하는 게 아니라 외부에서 주입받겠다는 의미
 * 객체와 또다른 객체가 있을 때 스프링의 경우 생성자를 통해 주입하는 행위를 해줄 수 없음. 제3자가 해줘야 함
 * 제3자가 두 객체 간 의존성을 맺어주고 주입시켜줌(런타임 시점)
 *
 * IoC (Inversion of Control): 제어의 역전
 * 프로그램의 흐름을 개발자가 아닌 프레임워크가 담당하도록 하는 것
 * 프로그램이라는 것은 개발자가 만드는 것이므로 개발자가 주도하고 제어하는 것이 순방향임
 * 개발자가 제어하는 것이 아니라 프레임워크가 제어하도록 하는 것. 개발자의 코드는 프레임워크의 일부가 되어 동작함
 * 프레임워크가 제어의 주도권을 가지고 있음.
 * 스프링이 관리하는 객체들: 빈
 * 빈들을 생성하거나 빈들끼리 의존성을 주입하고 객체의 생명주기를 관리하는 일을 IoC 컨테이너가 함
 * 스프링에서만 사용되는 개념은 아님.
 *
 * IoC(원칙) → DI(구현 패턴) → IoC 컨테이너(구현 프레임워크) → 스프링 컨텍스트(구현체)
 * 스프링의 IoC 컨테이너: BeanFactory, 스프링 컨텍스트: ApplicationContext
 */

/*
* 섹션4 키워드 정리
* 객체, 협력과 책임, 관심사 분리, 높은 응집도와 낮은 결합도
* getter/setter 자제하기, 객체에 메시지 보내기(무례하게 탈취하지 말고, 캡슐화된 데이터를 꺼내지 않는다)
* SOLID: SRP, OCP, LSP, ISP, DIP
    * SRP: 단일 책임 원칙: 클래스는 단 하나의 책임을 가져야 함. 간단하지만 어렵다. 책임을 보는 눈 기르기
    * OCP: 개방 폐쇄 원칙: 확장에는 열려있고, 수정에는 닫혀 있어야 함. 인터페이스를 통해 확장 가능하도록 함
    * LSP: 리스코프 치환 원칙: 자식 클래스는 부모 클래스의 역할을 대체할 수 있어야 함.
        * 자식 클래스는 상속 관계에서 부모 클래스의 책임을 준수하며, 부모 클래스 행동을 변경하지 않아야 함
    * ISP: 인터페이스 분리 원칙: 인터페이스를 기능 단위로 분리하여 클라이언트가 필요한 메서드만 사용할 수 있도록 함
    * DIP: 의존성 역전의 법칙: 고수준 모듈은 저수준 모듈에 의존해서는 안 됨. 둘 모두 추상화에 의존해야 함.
        * 각각 추상화를 의존하므로 저수준 모듈이 변경되어도 고수준 모듈이 타격받지 않음
* DI/IoC
 */