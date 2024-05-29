module at.game.hideandseekgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens at.game.hideandseekgame to javafx.fxml;
    exports at.game.hideandseekgame;
    exports at.game.hideandseekgame.room;
    opens at.game.hideandseekgame.room to javafx.fxml;
    exports at.game.hideandseekgame.room.types;
    opens at.game.hideandseekgame.room.types to javafx.fxml;
    exports at.game.hideandseekgame.room.factory;
    opens at.game.hideandseekgame.room.factory to javafx.fxml;
}