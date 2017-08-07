package game;

import actors.Actor;
import actors.Obstacles.Motorist;

import java.util.List;

public class TrafficManager {
    private Stage stage;
    private List<Actor> motorists;
    private int maxMotorists = 0;
    private int[] laneXs = {190, 350, 510};

    public TrafficManager(Stage stage, List<Actor> motorists) {
        this.stage = stage;
        this.motorists = motorists;
    }

    public void randomMotorist(float sessionRunTime) {
        maxMotorists = (int) sessionRunTime  + 1;

        if (motorists.size() < maxMotorists) {
            Motorist motorist = new Motorist(stage);
            int lane = 0;

            if (Utils.randInt(1, 10) >= 4) { //create a non-speeder (60% chance)
                lane = Utils.randInt(1, 2);
                motorist.setMinSpeed(8);
                motorist.setMaxSpeed(9);
                motorist.setVy(Utils.randFloat(-9, -8));
                motorist.setPosY(stage.getHeight() * -2);
            } else { //create a speeder
                lane = Utils.randInt(0, 1);
                motorist.setMinSpeed(11);
                motorist.setMaxSpeed(12);
                motorist.setVy(Utils.randFloat(-12, -11));
                motorist.setPosY(stage.getHeight() * 2);
            }

            motorist.setPosX(laneXs[lane] - motorist.getWidth() / 2 + Utils.randInt(-motorist.getWidth() / 2, motorist.getWidth() / 2));
            if (Utils.safeSpawn(motorist, motorists) == true) motorists.add(motorist);
        }
    }
}
