package game;

import actors.Actor;
import actors.Obstacles.Moose;

import java.util.List;

public class ObstacleManager {
    private Stage stage;

    public ObstacleManager(Stage stage) {
        this.stage = stage;
    }

    public void spawnMoose(List<Actor> obstacles) {
        if (Utils.randInt(0, 1000) >= 900) {
            Actor moose = new Moose(stage);
            moose.setPosX(Utils.randInt(0, stage.getWidth()));
            moose.setPosY(-60);
            moose.setVx(Utils.randInt(-5, 5));
            moose.setVy(Utils.randInt(-1, 1));
            obstacles.add(moose);
        }
    }
}
