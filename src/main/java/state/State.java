package state;

public class State {
    private Cell[][] map;
    private int[] stableIf;
    private int[] bornIf;
    private int[] deadIf;

    public State(int chanceToLife, int rows, int columns) {
        columns *= 2;
        map = new Cell[rows][columns];

        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                if(isCell(i, j))
                    map[i][j] = new Cell(chanceToLife, true);
                else
                    map[i][j] = new Cell();
    }

    private int countNeighbours(int row, int column) {
        int amount = 0;

        try {
            if(this.map[row-2][column].isAlive())
                amount++;
        } catch (Exception ignored) {}
        try {
            if(this.map[row+2][column].isAlive())
                amount++;
        } catch (Exception ignored) {}
        try {
            if(this.map[row-1][column+1].isAlive())
                amount++;
        } catch (Exception ignored) {}
        try {
            if(this.map[row-1][column-1].isAlive())
                amount++;
        } catch (Exception ignored) {}
        try {
            if(this.map[row][column-2].isAlive())
                amount++;
        } catch (Exception ignored) {}
        try {
            if(this.map[row][column+2].isAlive())
                amount++;
        } catch (Exception ignored) {}
        try {
            if(this.map[row+1][column+1].isAlive())
                amount++;
        } catch (Exception ignored) {}
        try {
            if(this.map[row+1][column-1].isAlive())
                amount++;
        } catch (Exception ignored) {}

        return amount;
    }

    private boolean isInArray(int[] array, int number) {
        for(int a : array)
            if(a == number)
                return true;
        return false;
    }

    private int nextStatusForAlive(int aliveNeighbours) {
        return isInArray(deadIf, aliveNeighbours) ? 0 : 1;
    }

    private int nextStatusForDead(int aliveNeighbours) {
        return isInArray(deadIf, aliveNeighbours) ? 1 : 0;
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

    public void printMap() {
        for(Cell[] row : map) {
            for (Cell cell : row) {
                System.out.print(cell.isCell() ? cell.getStatus() : " ");
            }
            System.out.print("\n");
        }

        System.out.print("\n\n");
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

    public int[] getDeadIf() {
        return deadIf;
    }

    public void setDeadIf(int[] deadIf) {
        this.deadIf = deadIf;
    }
}
