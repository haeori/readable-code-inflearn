package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard { // BOARD가 Minesweeeper 내부에 존재하기에는 많은 책임을 가지고 있으므로 별도 분리

    private final Cell[][] board;
    public static final int LAND_MINE_COUNT = 10;

    public GameBoard(int rowSize, int colSize) { // 외부에서는 형태를 알 수 없이 rowSize, colSize만 전달하여 생성하는 방식으로 변경(캡슐화)
        board = new Cell[rowSize][colSize];
    }

    public boolean isAllCellChecked() { // 중첩 반복문 메서드로 분리 및 stream 활용하여 3중 depth 해소
        return Arrays.stream(board) // Stream<String[]>
                .flatMap(Arrays::stream) // Stream<String>
                .noneMatch(Cell::isChecked);
    }

    public void initializeGame() {
        int rowSize = board.length;
        int colSize = board[0].length;

        for (int row = 0; row < rowSize; row++) { // i, j를 명확한 명칭인 row, col로 리네이밍
            for (int col = 0; col < colSize; col++) {
                board[row][col] = Cell.create(); // cell을 할당하는 로직이라 findCell 로 대체 불가
            }
        }
        // 반복문 종료시마다 작업이 하나 끝난 것이므로 환기를 위해 단락 분리

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            Cell landMineCell = findCell(landMineRow, landMineCol);
            landMineCell.turnOnLandMine(); // LAND_MINES 제거
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (isLandMineCell(row, col)) {
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                Cell cell = findCell(row, col);
                cell.updateNearbyLandMineCount(count);
            }
        }
    }

    public void flag(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.flag();
    }

    public void open(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        cell.open();
    }

    public void openSurroundedCells(int row, int col) { // 재귀함수
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) { // board라는 인덱스를 벗어나 판을 벗어난 경우
            return;
        }
        if (isOpenedCell(row, col)) { // Cell이 열려있으면 return. 닫혀있지 않다 -> 열려있다 동일 의미
            return;
        }
        if (isLandMineCell(row, col)) { // 이제 해당 로직이 gameBoard에 존재, 동일 클래스이므로 메서드명만 기술
            return;
        }

        open(row, col); // board[row][col].open(); 대체

        if (doesCellHaveLandMineCount(row, col)) { // 주변에 지뢰가 있는 경우
            return;
        }
        openSurroundedCells(row - 1, col - 1); // 해당 셀의 주변 8개 셀을 모두 탐색
        openSurroundedCells(row - 1, col);
        openSurroundedCells(row - 1, col + 1);
        openSurroundedCells(row, col - 1);
        openSurroundedCells(row, col + 1);
        openSurroundedCells(row + 1, col - 1);
        openSurroundedCells(row + 1, col);
        openSurroundedCells(row + 1, col + 1);
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return findCell(row, col).hasLandMineCount();
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }

    public boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        Cell cell = findCell(selectedRowIndex, selectedColIndex); // board[row][col] 변경 후 findCell(row, col) 적용 -> 변수 추출
        return cell.isLandMine();
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSign();
    }

    private Cell findCell(int rowIndex, int colIndex) { // board[row][col] 로직이 많으므로 메서드로 추출
        return board[rowIndex][colIndex];
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    // initializeGame을 우선 GameBoard 클래스로 가져왔다가, initializeGame 내부에서 사용하는 메서드들을 차례로 이동함
    public int countNearbyLandMines(int row, int col) {
        int rowSize = getRowSize();
        int colSize = getColSize();
        int count = 0; // 사용할 위치와 가깝게 선언
        
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < colSize && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < colSize && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < rowSize && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < rowSize && col + 1 < colSize && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }
}
