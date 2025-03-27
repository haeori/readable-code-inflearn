package cleancode.minesweeper.tobe.gamelevel;

public class Middle implements GameLevel {

    private static final int ROW_SIZE = 14;
    private static final int COL_SIZE = 18;
    private static final int LAND_MINE_COUNT = 40;
    private static final String LEVEL_NAME = "중급";

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
