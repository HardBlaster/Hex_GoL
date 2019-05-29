package state;

public class State {

    private Cell[][] map;
    private int[] stableIf;
    private int[] bornIf;
    private int[] deadIf;
    private int generation;
    private int TOTAL;


    public State(int chanceToLife, int rows, int columns) {
        TOTAL = columns*rows;
        columns *= 2;
        map = new Cell[rows][columns];
        generation = 0;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (isCell(i, j))
                    map[i][j] = new Cell(chanceToLife);
                else
                    map[i][j] = new Cell();
    }

    public int getLivingCells() {
        int amount = 0;

        for(Cell[] row : map)
            for(Cell cell : row)
                if(cell.isAlive())
                    amount++;

        return amount;
    }

    public int getDeadCells() {
        int amount = 0;

        for(Cell[] row : map)
            for(Cell cell : row)
                if(cell.isCell() && !cell.isAlive())
                    amount++;

        return amount;
    }

    private int countNeighbours(Cell[][] current, int row, int column) {
        int amount = 0;

        for (int a = -1; a <= 1; a++)
            for (int b = -2; b <= 2; b++)
                try {
                    if (!(a == 0 && b == 0))
                        if (isCell(row + a, column + b))
                            if (current[row + a][column + b].isAlive())
                                amount++;
                } catch (Exception ignored) {
                }

        try {
            if (current[row - 2][column].isAlive())
                amount++;
        } catch (Exception ignored) {
        }
        try {
            if (current[row + 2][column].isAlive())
                amount++;
        } catch (Exception ignored) {
        }

        return amount;
    }

    private boolean isInArray(int[] array, int number) {
        for (int a : array)
            if (a == number)
                return true;
        return false;
    }

    private boolean isCell(int x, int y) {
        return (x % 2 == 0 && y % 2 != 0) || (x % 2 != 0 && y % 2 == 0);
    }

    private int[][] getNeighboursMatrix() {
        int[][] neighboursMatrix = new int[map.length][map[0].length];

        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (isCell(i, j))
                    neighboursMatrix[i][j] = countNeighbours(map, i, j);
                else
                    neighboursMatrix[i][j] = -1;

        return neighboursMatrix;
    }

    private int getNextStatusForAlive(int livingNeighbours) {
        return isInArray(deadIf, livingNeighbours) ? 0 : 1;
    }

    private int getNextStatusForDead(int livingNeighbours) {
        return isInArray(bornIf, livingNeighbours) ? 1 : 0;
    }

    private int getNextStatus(int livingNeighbours, boolean alive) {
        if (alive)
            return getNextStatusForAlive(livingNeighbours);
        else
            return getNextStatusForDead(livingNeighbours);
    }

    private void nextGen(int[][] neighboursMatrix) {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[0].length; j++)
                if (isCell(i, j))
                    if (!isInArray(stableIf, neighboursMatrix[i][j]))
                        map[i][j].setStatus(getNextStatus(neighboursMatrix[i][j], map[i][j].isAlive()));

        generation++;
    }

    public void refresh() {
        int[][] neighboursMatrix = getNeighboursMatrix();

        nextGen(neighboursMatrix);
    }

    public void refresh(int steps) {
        for(int i = 0; i < steps; i++) {
            int[][] neighboursMatrix = getNeighboursMatrix();
            nextGen(neighboursMatrix);
        }
    }

    public void reverseCellStatus(int row, int column) {
        if(map[row][column].isCell())
            if(map[row][column].isAlive())
                map[row][column].setStatus(0);
            else
                map[row][column].setStatus(1);
    }

    public void printMap() {
        for (Cell[] row : map) {
            for (Cell cell : row) {
                System.out.print(cell.isCell() ? cell.getStatus() : "X");
            }
            System.out.print("\n");
        }

        System.out.print("\n\n");
    }

    public void setStableIf(int[] stableIf) {
        this.stableIf = stableIf;
    }

    public void setBornIf(int[] bornIf) {
        this.bornIf = bornIf;
    }

    public void setDeadIf(int[] deadIf) {
        this.deadIf = deadIf;
    }

    public Cell[][] getMap() {
        return map;
    }

    public int getGeneration() {
        return generation;
    }

    public int getTOTAL() {
        return TOTAL;
    }
}
