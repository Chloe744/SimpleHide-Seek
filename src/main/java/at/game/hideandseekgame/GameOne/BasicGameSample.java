package at.game.hideandseekgame.GameOne;

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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;


public class BasicGameSample extends GameApplication {
    private Entity player;
    private Entity enemy;
    private Entity VISIONCONE;
    private Point2D target;
    private Random rand = new Random();
    private double rotationTimer = 0;
    private double additionalRotation = 0;
    private int rotationDirection = 1;

    //Initialise settings
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
    protected void initGame() {
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
                .viewWithBBox(new Rectangle(25, 25, Color.BLUE))
                .with(new CollidableComponent(true))
                .collidable()
                .buildAndAttach();

        enemy = FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(100, 100)
                .view(new Circle(10, Color.RED))
                .with(new CollidableComponent(false))
                .with(new PhysicsComponent())
                .collidable()
                .buildAndAttach();

        Polygon visionShape = new Polygon(0.0, 0.0, 100.0, 50.0, 100.0, -50.0);
        visionShape.setFill(Color.rgb(255, 0, 0, 0.3));

        VISIONCONE = FXGL.entityBuilder()
                .type(EntityType.VISIONCONE)
                .at(enemy.getX(), enemy.getY())
                .viewWithBBox(visionShape)
                .with(new CollidableComponent(true))
                .collidable()
                .buildAndAttach();




        pickNewTarget();
    }
    //initialises the input
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

    private void pickNewTarget() {
        target = new Point2D(rand.nextInt(800), rand.nextInt(600));
    }


    @Override
    protected void onUpdate(double tpf) {
        if (target != null && enemy.getPosition().distance(target) < 5) {
            pickNewTarget();
        }

        Point2D directionToTarget = target.subtract(enemy.getPosition()).normalize().multiply(100 * tpf);
        enemy.translate(directionToTarget);

        rotationTimer += tpf;
        if (rotationTimer >= 0.001) {
            additionalRotation += rotationDirection * 30 * tpf;  // Halbiert die Drehgeschwindigkeit
            rotationTimer = 0;
            if (additionalRotation >= 360 || additionalRotation <= -360) {
                additionalRotation %= 360;
            }
        }

        double coneBaseX = enemy.getX() + enemy.getWidth() / 2 - 0;
        double coneBaseY = enemy.getY() + enemy.getHeight() / 2;
        VISIONCONE.setPosition(coneBaseX, coneBaseY);

        VISIONCONE.setRotation(additionalRotation);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.VISIONCONE) {
            @Override
            protected void onCollisionBegin(Entity player, Entity visionCone) {
                FXGL.getDialogService().showMessageBox("Entdeckt! Das Spiel ist vorbei.", FXGL.getGameController()::exit);
            }
        });
    }

    //launches the application
    public static void main(String[] args) {
        launch(args);
    }
}
