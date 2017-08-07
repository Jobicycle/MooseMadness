package game;

import actors.Actor;
import actors.Obstacles.Moose;

import java.util.List;

public class ObstacleManager {
    private Stage stage;
    private List<Actor> obstacles;
    private int maxObstacles = 0;

    public ObstacleManager(Stage stage, List<Actor> obstacles) {
        this.stage = stage;
        this.obstacles = obstacles;
    }

    public void randomMoose(float sessionRunTime) {
        maxObstacles = (int) sessionRunTime / 5 + 1;

        if (obstacles.size() < maxObstacles) {
            Actor moose = new Moose(stage);
            moose.setPosX(Utils.randInt(0 - moose.getWidth(), stage.getWidth()) + moose.getWidth());
            moose.setPosY(-moose.getHeight());

            if (moose.getPosX() + moose.getWidth() / 2 > stage.getWidth() / 2) {
                moose.setVx(Utils.randFloat(-2f, -0f));
            } else {
                moose.setVx(Utils.randFloat(0f, 2f));
            }

            moose.setVy(Utils.randFloat(-1f, 1f));
            obstacles.add(moose);
        }
    }
}
