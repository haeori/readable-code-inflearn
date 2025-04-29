package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.cell.Cell;
import cleancode.minesweeper.tobe.cell.EmptyCell;
import cleancode.minesweeper.tobe.cell.LandMineCell;
import cleancode.minesweeper.tobe.cell.NumberCell;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;

import java.util.Arrays;
import java.util.Random;

public class GameBoard { // BOARD가 Minesweeper 내부에 존재하기에는 많은 책임을 가지고 있으므로 별도 분리

    private final Cell[][] board;
    private final int landMineCount;

    public GameBoard(GameLevel gameLevel)
    { // 외부에서는 형태를 알 수 없이 rowSize, colSize만 전달하여 생성하는 방식으로 변경(캡슐화)
        int rowSize = gameLevel.getRowSize();
        int colSize = gameLevel.getColSize();
        board = new Cell[rowSize][colSize];

        landMineCount = gameLevel.getLandMineCount();
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
                board[row][col] = new EmptyCell(); // 신규 생성되는 cell은 EmptyCell
            }
        }

        for (int i = 0; i < landMineCount; i++) {
            int landMineCol = new Random().nextInt(colSize);
            int landMineRow = new Random().nextInt(rowSize);
            LandMineCell landMineCell = new LandMineCell();
//            landMineCell.turnOnLandMine(); // 지뢰인 경우에만 LandMineCell이므로 지뢰 여부값 불필요
            board[landMineRow][landMineCol] = landMineCell;
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                if (isLandMineCell(row, col)) {
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                if(count == 0) {
                    continue;  // count가 0인 경우 update 방지(해당 로직이 없으면 게임판 내 count가 0인 경우의 cell이 모두 열림)
                }

                NumberCell numberCell = new NumberCell(count);
//                numberCell.updateNearbyLandMineCount(count); // update 불필요
                board[row][col] = numberCell;
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

    // 예시
    //    public void temp(Cell cell) { // cell이 어떻게 동작하는지 알지 못하고 불필요한 타입체크를 하여 리스코프 치환 원칙 위반. 상속 구조에서는 타입체크가 불필요
    //        if (cell instanceof NumberCell) {
    //            NumberCell numberCell = (NumberCell) cell;
    //            numberCell.updateNearbyLandMineCount(0);
    //        }
    //    }

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
