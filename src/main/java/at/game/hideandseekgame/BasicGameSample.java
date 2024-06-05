/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 */

package at.game.hideandseekgame;

import com.almasb.fxgl.achievement.Achievement;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicGameSample extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setFullScreenAllowed(true);
        settings.setTitle("Hide & Seek Game");
        settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
        settings.getCredits().addAll(Arrays.asList(
                "Karo - Programmer",
                "André - Programmer"
        ));

        settings.getAchievements().add(new Achievement("Survivalist", "Survive for 10 Seconds", "", 0));
        settings.getAchievements().add(new Achievement("Stand back up", "Die for the first time", "", 1));
    }

    @Override
    protected void initGame() {
        FXGL.entityBuilder()
                .at(300, 300)
                .view(new Rectangle(100, 100, Color.BLUE))
                .view(new Text("Example"))
                .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}