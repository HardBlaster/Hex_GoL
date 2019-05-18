package state;

public class Cell {

    private int status;

    public Cell() {
        this.status = -1;
    }

    public Cell(int chanceToLife, boolean isCell) {
        if(isCell){
            if (randomNumber() < chanceToLife)
                this.status = 1;
            else
                this.status = 0;
        } else
            this.status =1;
    }

    private int randomNumber() {
        return (int) (Math.random() * 100) + 1;
    }

    public boolean isCell() {
        return this.status != -1;
    }

    public boolean isAlive() {
        return this.status == 1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
