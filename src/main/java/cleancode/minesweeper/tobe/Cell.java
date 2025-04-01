package cleancode.minesweeper.tobe;

// 모두 CELL에 대한 것이므로(동일 관심사이므로) 객체로 생성
public class Cell {

    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□"; // Cell의 상태를 나타내는 명확한 표현으로 리네이밍(열림/닫힘 -> 확인/미확인)
    private static final String EMPTY_SIGN = "■";

    private int nearbyLandMineCount; // 주변 셀의 지뢰 수
    private boolean isLandMine; // 지뢰 여부
    private boolean isFlagged; // 깃발 여부
    private boolean isOpened;

    // sign 필드 삭제
    public Cell(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        this.nearbyLandMineCount = nearbyLandMineCount;
        this.isLandMine = isLandMine;
        this.isFlagged = isFlagged;
        this.isOpened = isOpened;
    }

    // Cell이 가진 속성 : 근처 지뢰 숫자, 지뢰 여부
    // Cell의 상태 : 깃발 유무, 열림/닫힘, 사용자가 확인함
    // 도메인 지식을 얻음
    // BOARD는 Cell을 갈아끼우는 게 아니라, Cell 내부의 상황을 변화시키는 방향으로 가야 함

    public static Cell of(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) { // new Cell 대신 정적 팩토리 메서드로 생성.
        return new Cell(nearbyLandMineCount, isLandMine, isFlagged, isOpened); // 필수는 아니며, 이름을 따로 줄 수 있어 검증 등을 별도로 할 수 있다는 이점이 있음
    }

    // 정적 팩토리 메서드 모두 제거
    public static Cell create() {
        return of(0, false, false, false); // 아무것도 없는 빈 cell을 생성해 BOARD에 할당
    }

    public boolean isClosed() {
        return !isOpened;
    }

    public void turnOnLandMine() {
        this.isLandMine = true;
    }

    public void updateNearbyLandMineCount(int count) {
        this.nearbyLandMineCount = count;
    }

    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public boolean isLandMine() {
        return isLandMine;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean hasLandMineCount() {
        return nearbyLandMineCount != 0;
    }

    // getter, setter 처음부터 만들지 않음
    public String getSign() {
        if (isOpened) {
            if (isLandMine) {
                return LAND_MINE_SIGN;
            }
            if (hasLandMineCount()) {
                return String.valueOf(nearbyLandMineCount);
            }
            return EMPTY_SIGN;
        }

        if (isFlagged) {
            return FLAG_SIGN;
        }
        return UNCHECKED_SIGN;
    }

}
