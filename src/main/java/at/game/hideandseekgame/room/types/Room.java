package at.game.hideandseekgame.room.types;

import at.game.hideandseekgame.room.types.RoomType;

public interface Room {
    public RoomType roomType = null;
    int roomSizeX = 0;
    int roomSizeY = 0;
    boolean hasObstacles = false;

    void createEntity(int x, int y);


}
