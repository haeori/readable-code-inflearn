package cleancode.minesweeper.tobe;

// 모두 CELL에 대한 것이므로(동일 관심사이므로) 객체로 생성
// getter, setter 초기 생성하지 않음
public class Cell {

    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String CLOSED_CELL_SIGN = "□";
    private static final String OPENED_CELL_SIGN = "■";

    private final String sign; // 불변 데이터는 final로 최대한 명시해주는 것이 좋음

    public Cell(String sign) {
        this.sign = sign;
    }

    public static Cell of(String sign) { // new Cell 대신 정적 팩토리 메서드로 생성.
        return new Cell(sign); // 정적 팩토리 메서드로 생성하는 것이 필수는 아니나, 이름을 따로 줄 수 있어 검증 등을 별도로 할 수 있다는 이점이 있음
    }

    public static Cell ofFlag() {
        return of(FLAG_SIGN);
    }

    public static Cell ofLandMine() {
        return of(LAND_MINE_SIGN);
    }

    public static Cell ofClosed() {
        return of(CLOSED_CELL_SIGN);
    }

    public static Cell ofOpened() {
        return of(OPENED_CELL_SIGN);
    }

    public static Cell ofNearbyLandMineCount(Integer integer) {
        return of(String.valueOf(integer));
    }

    public String getSign() {
        return sign;
    }

    public boolean isClosed() {
        return CLOSED_CELL_SIGN.equals(this.sign);
    }

    public boolean doesNotClosed() {
        return !isClosed();
    }
}
