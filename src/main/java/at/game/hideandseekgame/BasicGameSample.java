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
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.EnumSet;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getPhysicsWorld;

public class BasicGameSample extends GameApplication {
    public static Entity player;
    public static Entity enemy;
    public static Entity vision;
    private boolean playerInVision = false;

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
        FXGL.onKey(KeyCode.W, "up", () -> {
            if (player.getY() > 0) {
                player.translateY(-8);
            }
        });
        FXGL.onKey(KeyCode.S, "down", () -> {
            if (player.getY() < FXGL.getAppHeight()-25) {
                player.translateY(8);
            }
        });
        FXGL.onKey(KeyCode.A, "left", () -> {
            if (player.getX() > 0) {
                player.translateX(-8);
            }
        });
        FXGL.onKey(KeyCode.D, "right", () -> {
            if (player.getX()+player.getWidth() < FXGL.getAppWidth()-25) {
                player.translateX(8);
            }
        });
    }

    @Override
    protected void initGame() {
        // Erstellung des Spielers
        player = entityBuilder()
                .view(new Rectangle(25, 25, Color.BLUE))
                .with(new CollidableComponent(true))
                .at(300, 300)
                .buildAndAttach();

        // Erstellung des Gegners
        enemy = entityBuilder()
                .view(new Rectangle(25, 25, Color.RED))
                .with(new CollidableComponent(true))
                .at(100, 100)
                .buildAndAttach();

        // Erstellung des Sichtfeld-Dreiecks
        Polygon visionTriangle = new Polygon();
        visionTriangle.getPoints().addAll(new Double[]{
                0.0, 0.0,  // Basispunkt in der Mitte der Enemy-Box
                -100.0, -200.0,  // Rechter Punkt
                100.0, -200.0   // Linker Punkt
        });

        vision = entityBuilder()
                .view(visionTriangle)
                .with(new CollidableComponent(true))
                .at(enemy.getX(), enemy.getY())
                .zIndex(-1)
                .buildAndAttach();

        run(() -> {
            if(enemy != null && vision != null && player != null) {
                Point2D enemyCenter = enemy.getCenter();
                Point2D playerCenter = player.getCenter();
                Point2D direction = playerCenter.subtract(enemyCenter);
                double angle = Math.atan2(direction.getY(), direction.getX()) * 180 / Math.PI + 90;
                vision.setRotation(angle); // rotate the vision
                vision.setPosition(enemyCenter.add(12.5, 12.5));
            }
        }, Duration.seconds(0.017)); // 60 FPS

    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.VISION) {


            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                playerInVision = true;
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if (playerInVision) {
            Point2D directionToPlayer = player.getPosition().subtract(enemy.getPosition()).normalize().multiply(200 * tpf);
            enemy.translate(directionToPlayer);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
