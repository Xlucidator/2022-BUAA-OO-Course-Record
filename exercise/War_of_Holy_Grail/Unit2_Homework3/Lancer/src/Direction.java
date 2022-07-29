public class Direction {
    private final int buildingMove;
    private final int floorMove;

    public Direction(int buildingMove, int floorMove) {
        this.buildingMove = buildingMove;
        this.floorMove = floorMove;
    }

    public int getBuildingMove() {
        return buildingMove;
    }

    public int getFloorMove() {
        return floorMove;
    }

    public boolean notMove() {
        return buildingMove == 0 && floorMove == 0;
    }
}
