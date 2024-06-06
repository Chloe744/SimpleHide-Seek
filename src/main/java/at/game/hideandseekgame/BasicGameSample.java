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
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.EnumSet;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BasicGameSample extends GameApplication {
    public static Entity player;
    public static Entity enemy;
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
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
    protected void initInput() {
        FXGL.onKey(KeyCode.W, "up", () -> player.translateY(-5));
        FXGL.onKey(KeyCode.S, "down", () -> player.translateY(5));
        FXGL.onKey(KeyCode.A, "left", () -> player.translateX(-5));
        FXGL.onKey(KeyCode.D, "right", () -> player.translateX(5));



    }

    @Override
    protected void initGame() {
        player = FXGL.entityBuilder()
                .at(300,300)
                .view(new Rectangle(25, 25, Color.BLUE))
                .buildAndAttach();


        enemy = FXGL.entityBuilder()
                .at(100,100)
                .view(new Rectangle(25, 25, Color.RED))
                .buildAndAttach();



    }


    @Override
    protected void onUpdate(double tpf) {
        Point2D directionToPlayer = player.getPosition().subtract(enemy.getPosition()).normalize().multiply(200 * tpf);
        enemy.translate(directionToPlayer);
    }
    public static void main(String[] args) {
        launch(args);
    }
}