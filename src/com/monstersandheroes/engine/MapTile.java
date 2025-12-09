package com.monstersandheroes.engine;

import java.util.Objects;

/**
 * Represents a single tile on the world grid.
 */
public class MapTile {
    private final int row;
    private final int column;
    private final TileType tileType;

    public MapTile(int row, int column, TileType tileType) {
        this.row = row;
        this.column = column;
        this.tileType = Objects.requireNonNull(tileType);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public TileType getTileType() {
        return tileType;
    }

    public boolean isAccessible() {
        return tileType != TileType.INACCESSIBLE;
    }
}
