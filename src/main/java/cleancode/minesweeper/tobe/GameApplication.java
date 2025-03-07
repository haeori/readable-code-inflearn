package cleancode.minesweeper.tobe;

public class GameApplication { // 확장성을 위해 클래스명도 변경
    public static void main(String[] args) { // main 메서드는 게임을 실행하는 책임만 지도록 수정
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.run();
    }
}
