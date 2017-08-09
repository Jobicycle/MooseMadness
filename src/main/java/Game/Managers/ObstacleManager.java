package Game.Managers;

import Game.Stage;
import Game.Utils;
import actors.Actor;
import actors.Obstacles.Moose;
import actors.Obstacles.Pothole;

import java.util.List;

/**
 * ObstacleManager classs handles when and where to spawn obacles such as moose and potholes.
 * Spawn rates increase as sessionRunTime increases.
 */
public class ObstacleManager {
    private Stage stage;
    private List<Actor> obstacles;
    private int maxObstacles;

    /**
     * ObstacleManager constructor. Takes in the stage and obstacle list.
     * @param stage
     * @param obstacles
     */
    public ObstacleManager(Stage stage, List<Actor> obstacles) {
        this.stage = stage;
        this.obstacles = obstacles;
    }

    /**
     * randomObstacle function decides if a moose or pothole should be created. Obstacles can spawn 2 seconds after
     * start and beyond. Number of obstacles is increased every 15 seconds starting with 1 and going onward until the
     * Game ends. Obstacle ratio is typically 1:3 of moose:potholes.
     *
     * @param sessionRunTime
     */
    public void randomObstacle(float sessionRunTime) {
        maxObstacles = (int) sessionRunTime / 10 + 1; //limit max obstacles to how long Game session has been underway.
        int randObstacle = Utils.randInt(1, 3); //choose a random number to decide on which obstacle to spawn.

        if (obstacles.size() < maxObstacles && sessionRunTime > 2) { //if under obstacle limit and 2 seconds have passed since start of Game.
            if (randObstacle == 1) { //if rand 1 (33%), create a moose with random positioning and speeds.
                Moose moose = new Moose(stage);
                moose.setPosX(Utils.randInt(0, stage.getWidth()) - moose.getWidth());
                moose.setPosY(-moose.getHeight());

                if (moose.getPosX() + moose.getWidth() / 2 >= stage.getWidth() / 2) {
                    moose.setVx(Utils.randFloat(-2f, -1f));
                    moose.setDirectionRight(false);
                } else {
                    moose.setVx(Utils.randFloat(1f, 2f));
                    moose.setDirectionRight(true);
                }

                moose.setVy(Utils.randFloat(0f, 2f));
                obstacles.add(moose);

            } else { //if not 1 (66%) create a pothole with random positioning within bounds of road.
                Actor pothole = new Pothole(stage);
                pothole.setPosX(Utils.randInt(142 + pothole.getWidth(), 768 - pothole.getWidth()));
                pothole.setPosY(-pothole.getHeight());
                obstacles.add(pothole);
            }
        }
    }
}
