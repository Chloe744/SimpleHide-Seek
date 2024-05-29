package at.game.hideandseekgame.room.factory;

import at.game.hideandseekgame.room.types.RoomType;

public class RoomFactory {


    public void createRoom(RoomType roomType) {
        switch (roomType) {
            case floor:
               // createFloor();
                break;
            case bigRoom:
               // createBigRoom();
                break;
            case smallRoom:
               // createSmallRoom();
                break;
            case normalRoom:
                //createNormalRoom();
                break;
        }
    }
}
