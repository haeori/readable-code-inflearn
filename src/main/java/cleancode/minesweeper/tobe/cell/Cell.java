package cleancode.minesweeper.tobe.cell;

public abstract class Cell {

    protected static final String FLAG_SIGN = "⚑"; // 여러 하위클래스에서 사용하는 경우 protected 키워드를 사용하여 하위에는 열어줌
    protected static final String UNCHECKED_SIGN = "□";

    protected boolean isFlagged; // 하위에서도 사용 가능하도록 private에서 protected로 변경
    protected boolean isOpened;

    // Cell이 가진 속성 : 근처 지뢰 숫자, 지뢰 여부
    // Cell의 상태 : 깃발 유무, 열림/닫힘, 사용자가 확인함

    // 추상클래스는 인스턴스를 생성할 수 없으므로 삭제

    // 특정 Cell에서만 유용한 메서드를 추상 메서드로 변경
    // 상위 메서드에서는 추상 메서드를 호출하고, 하위 클래스에서 구현
    public abstract boolean isLandMine();

    public abstract boolean hasLandMineCount();

    public abstract String getSign();

    // 이하 공통 기능
    public void flag() {
        this.isFlagged = true;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public boolean isOpened() {
        return isOpened;
    }
}
