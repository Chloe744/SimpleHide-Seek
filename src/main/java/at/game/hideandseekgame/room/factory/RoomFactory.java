package at.game.hideandseekgame.room.factory;

import at.game.hideandseekgame.room.Room;
import at.game.hideandseekgame.room.types.*;

public class RoomFactory {

    public Room createRoom(RoomType roomType) {
        return switch (roomType) {
            case floor -> new Floor();
            case bigRoom -> new BigRoom();
            case smallRoom -> new SmallRoom();
            case normalRoom -> new NormalRoom();
        };
    }
}
