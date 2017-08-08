package game;

import actors.Actor;
import actors.Obstacles.Moose;
import actors.Obstacles.Pothole;

import java.util.List;

public class ObstacleManager {
    private Stage stage;
    private List<Actor> obstacles;
    private int maxObstacles;

    public ObstacleManager(Stage stage, List<Actor> obstacles) {
        this.stage = stage;
        this.obstacles = obstacles;
    }

    public void randomMoose(float sessionRunTime) {
        maxObstacles = (int) sessionRunTime / 15 + 1;
        int randObstacle = Utils.randInt(1, 3);

        if (obstacles.size() < maxObstacles && sessionRunTime > 1) {
            if (randObstacle == 1) {
                Actor moose = new Moose(stage);
                moose.setPosX(Utils.randInt(0, stage.getWidth()) - moose.getWidth());
                moose.setPosY(-moose.getHeight());

                if (moose.getPosX() + moose.getWidth() / 2 >= stage.getWidth() / 2) {
                    moose.setVx(Utils.randFloat(-2f, -1f));
                } else {
                    moose.setVx(Utils.randFloat(1f, 2f));
                }

                moose.setVy(Utils.randFloat(0f, 2f));
                obstacles.add(moose);

            } else if (randObstacle > 1) {
                Actor pothole = new Pothole(stage);
                pothole.setPosX(Utils.randInt(142 + pothole.getWidth(), 768 - pothole.getWidth()));
                pothole.setPosY(-pothole.getHeight());
                obstacles.add(pothole);
            }
        }
    }
}
