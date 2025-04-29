package cleancode.minesweeper.tobe.gamelevel;

public class Advanced implements GameLevel {

    private static final int ROW_SIZE = 20;
    private static final int COL_SIZE = 24;
    private static final int LAND_MINE_COUNT = 99;
    private static final String LEVEL_NAME = "고급";

    @Override
    public int getRowSize() {
        return ROW_SIZE;
    }

    @Override
    public int getColSize() {
        return COL_SIZE;
    }

    @Override
    public int getLandMineCount() {
        return LAND_MINE_COUNT;
    }

    @Override
    public String getLevelName() {
        return LEVEL_NAME;
    }
}
