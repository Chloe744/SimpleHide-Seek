module at.game.hideandseekgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires annotations;

    exports at.game.hideandseekgame.GameOne;
    opens at.game.hideandseekgame.GameOne to javafx.fxml;
    exports at.game.hideandseekgame.GameTwo;
    opens at.game.hideandseekgame.GameTwo to javafx.fxml;
}