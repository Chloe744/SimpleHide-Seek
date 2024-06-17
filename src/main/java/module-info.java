module at.game.hideandseekgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires annotations;

    opens at.game.hideandseekgame to javafx.fxml;
    exports at.game.hideandseekgame;
}