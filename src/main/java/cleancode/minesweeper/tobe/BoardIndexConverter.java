package cleancode.minesweeper.tobe;

public class BoardIndexConverter {

    public static final char BASE_CHAR_FOR_COL = 'a';

    public int getSelectedColIndex(String cellInput, int colSize) { // 클래스 내부에서만 사용하는 메서드는 private, 동일 패키지여도 클래스 외부에서 사용하는 메서드는 public
        char cellInputCol = cellInput.charAt(0);
        // 전치사로 자연스럽게 의미 추측되도록 메서드 레벨 추상화
        return convertColFrom(cellInputCol, colSize);
    }

    public int getSelectedRowIndex(String cellInput, int rowSize) {
        String cellInputRow = cellInput.substring(1); // 두 자리 이상 수에 대한 고려
        return convertRowFrom(cellInputRow, rowSize); // 변수 인라인화(이전 강의 해당)
    }

    private int convertColFrom(char cellInputCol, int colSize) {
        int colIndex = cellInputCol - BASE_CHAR_FOR_COL; // 소문자만 고려, 상수 처리

        if (colIndex < 0 || colIndex >= colSize) {
            throw new AppException("잘못된 입력입니다."); // 존재하지 않는 위치에 대한 접근으로 인해 발생하는 exception에 대한 예외처리
        }
        return colIndex;
    }

    private int convertRowFrom(String cellInputRow, int rowSize) {
        int rowIndex = Integer.parseInt(cellInputRow) - 1; // String 타입으로 변경
        if (rowIndex < 0 || rowIndex >= rowSize) {
            throw new AppException("잘못된 입력입니다.");
        }

        return rowIndex;
    }

}
