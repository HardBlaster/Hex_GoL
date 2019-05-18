package state;

public class State {
    private Cell[][] map;
    private int[] stableIf;
    private int[] bornIf;

    public State(int chanceToLife, int rows, int columns) {
        columns *= 2;
        map = new Cell[rows][columns];

        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                if(isCell(i, j))
                    map[i][j] = new Cell(chanceToLife, true);
    }

    private int countNeighbours(int row, int column) {
        int amount = 0;

        try {
            if(this.map[row][column].isAlive())
                amount++;
        } catch (Exception ignored) {}

        return amount;
    }

    private int nextStatusForAlive(int aliveNeighbours) {

    }

    private int nextStatusForDead(int aliveNeighbours) {

    }

    private int nextStatus(int row, int column) {
        int aliveNeighbours = countNeighbours(row, column);

        if(map[row][column].isAlive())
            return nextStatusForAlive(aliveNeighbours);
        else
            return nextStatusForDead(aliveNeighbours);
    }

    private Cell[][] nextGen(Cell[][] current) {
        for(int i = 0; i < current.length; i++)
            for(int j = 0; j < current[0].length; j++)
                if(current[i][j].isCell())
                    current[i][j].setStatus(nextStatus(i, j));

        return current;
    }

    public void refresh() {
        Cell[][] tmpMap = this.map;

        this.map = nextGen(tmpMap);
    }

    private boolean isCell(int x, int y) {
        return (x % 2 == 0 && y % 2 != 0) || (x % 2 != 0 && y % 2 == 0);
    }

    public int[] getStableIf() {
        return stableIf;
    }

    public void setStableIf(int[] stableIf) {
        this.stableIf = stableIf;
    }

    public int[] getBornIf() {
        return bornIf;
    }

    public void setBornIf(int[] bornIf) {
        this.bornIf = bornIf;
    }
}
