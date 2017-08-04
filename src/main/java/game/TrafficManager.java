package game;

import actors.Actor;
import actors.Obstacles.Motorist;

import java.util.List;

public class TrafficManager {
    private Stage stage;
    private int[] laneXs = {115, 270, 430, 585};

    public TrafficManager(Stage stage) {
        this.stage = stage;
    }

    public void randomMotorist(List<Actor> motorists) {
        if (Utils.randInt(1, 1000) >= 990) {
            Actor motorist = new Motorist(stage);
            int randLane;

            if (Utils.randInt(0, 1) == 1) { //if speeding
                randLane = Utils.randInt(0, 1);
                motorist.setPosY(stage.getHeight() * 2);
                motorist.setVy(Utils.randInt(-15, -11));
            } else { //not speeding
                randLane = Utils.randInt(1, 2);
                motorist.setPosY(stage.getHeight() * -2);
                motorist.setVy(Utils.randInt(-9, -7));
            }

            motorist.setPosX((laneXs[randLane] + laneXs[randLane + 1]) / 2 - motorist.getWidth() / 2);
            motorists.add(motorist);
        }

    }
}
