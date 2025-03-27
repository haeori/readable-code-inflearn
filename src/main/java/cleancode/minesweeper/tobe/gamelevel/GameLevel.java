package cleancode.minesweeper.tobe.gamelevel;

public interface GameLevel { // 게임 난이도를 인터페이스로 분리하여 난이도 변경시 해당 인터페이스의 구현체가 달라질 수 있도록 함

    // 선언부 지정
    int getRowSize(); // 행 크기 반환
    int getColSize(); // 열 크기 반환
    int getLandMineCount(); // 지뢰 개수 반환

    String getLevelName(); // 난이도 이름 반환
}
