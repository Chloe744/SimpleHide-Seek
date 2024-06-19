package at.game.hideandseekgame.GameTwo;

import com.almasb.fxgl.app.CursorInfo;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.profile.DataFile;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class BasicGameTwo extends GameApplication  {
    private Entity playerOne;
    private Entity playerTwo;
    private Random rand = new Random();
    private Map<Entity, Point2D> visionConeMovements = new HashMap<>();
    private Text spawnTimer;
    private Duration nextSpawnTime = Duration.seconds(5);
    private Text winCounterText;
    private Text winnerText;
    private int winCountPlayerOne = 0;
    private int winCountPlayerTwo = 0;



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
        settings.setDefaultCursor(new CursorInfo("glassmain.png", 0, 0));
    }

    //initialises the input
    @Override
    protected void initInput() {
        int i = 4;
        FXGL.onKey(KeyCode.W, "up", () -> {
            if (playerOne.getY() > 0) {
                playerOne.translateY(-i);
            }
        });
        FXGL.onKey(KeyCode.S, "down", () -> {
            if (playerOne.getY() < FXGL.getAppHeight()-25) {
                playerOne.translateY(i);
            }
        });
        FXGL.onKey(KeyCode.A, "left", () -> {
            if (playerOne.getX() > 0) {
                playerOne.translateX(-i);
            }
        });
        FXGL.onKey(KeyCode.D, "right", () -> {
            if (playerOne.getX()+ playerOne.getWidth() < FXGL.getAppWidth()) {
                playerOne.translateX(i);
            }
        });


        // Add controls for playerTwo
        FXGL.onKey(KeyCode.UP, "up2", () -> {
            if (playerTwo.getY() > 0) {
                playerTwo.translateY(-i);
            }
        });
        FXGL.onKey(KeyCode.DOWN, "down2", () -> {
            if (playerTwo.getY() < FXGL.getAppHeight()-25) {
                playerTwo.translateY(i);
            }
        });
        FXGL.onKey(KeyCode.LEFT, "left2", () -> {
            if (playerTwo.getX() > 0) {
                playerTwo.translateX(-i);
            }
        });
        FXGL.onKey(KeyCode.RIGHT, "right2", () -> {
            if (playerTwo.getX()+ playerTwo.getWidth() < FXGL.getAppWidth()) {
                playerTwo.translateX(i);
            }
        });
    }

    @Override
    protected void initUI() {
        spawnTimer = new Text();
        spawnTimer.setTranslateX(10); // Position des Timers links oben
        spawnTimer.setTranslateY(20);
        FXGL.getGameScene().addUINode(spawnTimer); // Füge den Timer zum UI hinzu
        // Position des Gewinnzählers rechts oben

        winCounterText = new Text("Punktestand: Player One: 0, Player Two: 0");
        winCounterText.setTranslateX(FXGL.getAppWidth() - 250); // Abstand zu rechts
        winCounterText.setTranslateY(20);                       // Abstand zu oben
        FXGL.getGameScene().addUINode(winCounterText);         // Füge den Gewinnzähler zur Spiel-Szene hinzu

        winnerText = new Text(); // Text für den Gewinner
        winnerText.setTranslateX(FXGL.getAppWidth() / 2 - 100); // Zentriert in der Breite
        winnerText.setTranslateY(FXGL.getAppHeight() / 2);      // Zentriert in der Höhe
        winnerText.setFill(Color.GOLD);                         // Farbe des Gewinnertextes
        FXGL.getGameScene().addUINode(winnerText);}

    @Override
    protected void initGame() {
        playerOne = FXGL.entityBuilder()
                .type(EntityType.PlayerOne)
                .at(300, 300)
                .viewWithBBox(new Rectangle(25, 25, Color.BLUE))
                .with(new CollidableComponent(true))
                .collidable()
                .buildAndAttach();

        playerTwo = FXGL.entityBuilder()
                .type(EntityType.PlayerTwo)
                .at(FXGL.getAppWidth() - 300, FXGL.getAppHeight() - 300)
                .viewWithBBox(new Rectangle(25, 25, Color.GREEN))
                .with(new CollidableComponent(true))
                .collidable()
                .buildAndAttach();

        FXGL.getGameTimer().runAtInterval(() -> {
            int corner = rand.nextInt(4);  // generate a random number between 0 (inclusive) and 4 (exclusive)

            switch(corner) {
                case 0:
                    // Obere linke Ecke
                    spawnVisionCone(0, 0);
                    break;
                case 1:
                    // Obere rechte Ecke
                    spawnVisionCone(FXGL.getAppWidth(), 0);
                    break;
                case 2:
                    // Untere linke Ecke
                    spawnVisionCone(0, FXGL.getAppHeight());
                    break;
                case 3:
                    // Untere rechte Ecke
                    spawnVisionCone(FXGL.getAppWidth(), FXGL.getAppHeight());
                    break;
            }
        }, Duration.seconds(5));
    }


    @Override
    protected void onUpdate(double tpf) {
        for (Entity visionCone : FXGL.getGameWorld().getEntitiesByType(EntityType.Light)) {
            Point2D movement = visionConeMovements.get(visionCone);
            double newX = Math.max(0, Math.min(visionCone.getX() + movement.getX() * tpf, FXGL.getAppWidth() ));
            double newY = Math.max(0, Math.min(visionCone.getY() + movement.getY() * tpf, FXGL.getAppHeight() ));
            visionCone.setPosition(newX, newY);


            // Prüfungen wie zuvor, jetzt mit Aktualisierung des UI
            double distToPlayerOne = visionCone.getPosition().distance(playerOne.getPosition().add(playerOne.getWidth() / 2, playerOne.getHeight() / 2));
            double distToPlayerTwo = visionCone.getPosition().distance(playerTwo.getPosition().add(playerTwo.getWidth() / 2, playerTwo.getHeight() / 2));

            if (distToPlayerOne <= 50) {
                winCountPlayerTwo++;
                winnerText.setText("Player One verliert, Player Two gewinnt!");
                winCounterText.setText("Punktestand: Player One: " + winCountPlayerOne + ", Player Two: " + winCountPlayerTwo);
                FXGL.getDialogService().showMessageBox(winnerText.getText(), () -> FXGL.getGameController().startNewGame());
            } else if (distToPlayerTwo <= 50) {
                winCountPlayerOne++;
                winnerText.setText("Player Two verliert, Player One gewinnt!");
                winCounterText.setText("Punktestand: Player One: " + winCountPlayerOne + ", Player Two: " + winCountPlayerTwo);
                FXGL.getDialogService().showMessageBox(winnerText.getText(), () -> FXGL.getGameController().startNewGame());
            }
        }

        nextSpawnTime = nextSpawnTime.subtract(Duration.seconds(tpf));
        spawnTimer.setText("Nächster Spawn in: " + Math.max(0, (int) nextSpawnTime.toSeconds()) + " Sekunden");

        if (nextSpawnTime.lessThanOrEqualTo(Duration.ZERO)) {
            nextSpawnTime = Duration.seconds(5); // Reset Timer
        }



    }


    private void spawnVisionCone(double x, double y) {
        Circle visionShape = new Circle(50);
        visionShape.setFill(Color.rgb(255, 0, 0, 0.3));

        var newVisionCone = FXGL.entityBuilder()
                .type(EntityType.Light)
                .at(x, y)
                .viewWithBBox(visionShape)
                .with(new CollidableComponent(true))
                .collidable()
                .buildAndAttach();

        // Schedule the movement update every 10 seconds
        FXGL.getGameTimer().runAtInterval(() -> {
            Point2D newTarget = new Point2D(rand.nextDouble() * FXGL.getAppWidth(), rand.nextDouble() * (FXGL.getAppHeight()));
            Point2D movement = newTarget.subtract(newVisionCone.getPosition()).normalize().multiply(rand.nextInt(50) + 50);
            visionConeMovements.put(newVisionCone, movement);
        }, Duration.seconds(10));

        // Set initial movement
        Point2D initialTarget = new Point2D(rand.nextDouble() * FXGL.getAppWidth(), rand.nextDouble() * (FXGL.getAppHeight()));
        Point2D initialMovement = initialTarget.subtract(new Point2D(x, y)).normalize().multiply(rand.nextInt(50) + 50);
        visionConeMovements.put(newVisionCone, initialMovement);
    }

    public static void main(String[] args) {
        launch(args);
    }
}