package com.monstersandheroes.engine;

import java.util.List;

/**
 * Represents the overworld grid that the party traverses.
 */
public class WorldMap {
    private final int size;
    private final MapTile[][] tiles;
    private int partyRow;
    private int partyColumn;

    public WorldMap(int size) {
        this.size = size;
        this.tiles = new MapTile[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                tiles[row][column] = new MapTile(row, column, TileType.COMMON);
            }
        }
    }

    public void populate(List<MapTile> generatedTiles) {
        for (MapTile tile : generatedTiles) {
            tiles[tile.getRow()][tile.getColumn()] = tile;
        }
    }

    public void placeParty(int row, int column) {
        partyRow = row;
        partyColumn = column;
    }

    public boolean canMove(int deltaRow, int deltaColumn) {
        int newRow = partyRow + deltaRow;
        int newColumn = partyColumn + deltaColumn;
        return isInside(newRow, newColumn) && tiles[newRow][newColumn].isAccessible();
    }

    public void move(int deltaRow, int deltaColumn) {
        if (canMove(deltaRow, deltaColumn)) {
            partyRow += deltaRow;
            partyColumn += deltaColumn;
        }
    }

    public MapTile currentTile() {
        return tiles[partyRow][partyColumn];
    }

    public MapTile getTile(int row, int column) {
        return tiles[row][column];
    }

    public boolean isPartyAt(int row, int column) {
        return partyRow == row && partyColumn == column;
    }

    public int getPartyRow() {
        return partyRow;
    }

    public int getPartyColumn() {
        return partyColumn;
    }

    private boolean isInside(int row, int column) {
        return row >= 0 && column >= 0 && row < size && column < size;
    }

    public int getSize() {
        return size;
    }
}
