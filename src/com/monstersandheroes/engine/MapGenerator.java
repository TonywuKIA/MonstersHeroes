package com.monstersandheroes.engine;

import com.monstersandheroes.config.GameConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Builds world maps respecting configured tile ratios and chooses a starting party position.
 */
public class MapGenerator {
    private final Random random = new Random();

    public WorldMap generate(int size) {
        WorldMap map = new WorldMap(size);
        List<Coordinate> coordinates = allCoordinates(size);
        Collections.shuffle(coordinates, random);

        int totalTiles = size * size;
        int inaccessibleCount = Math.max(1, (int) Math.round(totalTiles * GameConfig.DEFAULT_INACCESSIBLE_RATIO));
        int marketCount = Math.max(1, (int) Math.round(totalTiles * GameConfig.DEFAULT_MARKET_RATIO));

        List<MapTile> tiles = new ArrayList<>(totalTiles);
        int index = 0;

        for (; index < inaccessibleCount && index < coordinates.size(); index++) {
            Coordinate c = coordinates.get(index);
            tiles.add(new MapTile(c.row, c.column, TileType.INACCESSIBLE));
        }
        for (; index < inaccessibleCount + marketCount && index < coordinates.size(); index++) {
            Coordinate c = coordinates.get(index);
            tiles.add(new MapTile(c.row, c.column, TileType.MARKET));
        }
        for (; index < coordinates.size(); index++) {
            Coordinate c = coordinates.get(index);
            tiles.add(new MapTile(c.row, c.column, TileType.COMMON));
        }

        map.populate(tiles);
        Coordinate start = pickStart(coordinates, map);
        map.placeParty(start.row, start.column);
        return map;
    }

    private Coordinate pickStart(List<Coordinate> coordinates, WorldMap map) {
        for (Coordinate c : coordinates) {
            if (map.getTile(c.row, c.column).isAccessible()) {
                return c;
            }
        }
        return new Coordinate(0, 0);
    }

    private List<Coordinate> allCoordinates(int size) {
        List<Coordinate> coords = new ArrayList<>(size * size);
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                coords.add(new Coordinate(row, column));
            }
        }
        return coords;
    }

    private static final class Coordinate {
        private final int row;
        private final int column;

        private Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
