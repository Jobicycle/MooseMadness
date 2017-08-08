package game;

import actors.Actor;
import actors.Obstacles.Motorist;

import java.util.List;

public class TrafficManager {
    private Stage stage;
    private List<Actor> motorists;
    private int maxMotorists = 0;
    private int[] laneXPos = {194, 298, 402, 506, 610, 714};

    public TrafficManager(Stage stage, List<Actor> motorists) {
        this.stage = stage;
        this.motorists = motorists;
    }

    public void randomMotorist(float sessionRunTime) {
        maxMotorists = (int) sessionRunTime / 15 + 1;

        if (motorists.size() < maxMotorists && sessionRunTime > 1) {
            Motorist motorist = new Motorist(stage);
            int lane;

            if (Utils.randInt(1, 2) == 1) {
                motorist.setVy(Utils.randFloat(-3, -1));
                motorist.setPosY(0 - motorist.getHeight());
            } else { //create a speeder
                motorist.setVy(Utils.randFloat(-8, -6));
                motorist.setPosY(stage.getHeight());
            }

            lane = Utils.randInt(0, 5);
            motorist.setPosX(laneXPos[lane] - motorist.getWidth() / 2 + Utils.randInt(-motorist.getWidth() / 2, motorist.getWidth()) / 2);
            if (Utils.safeSpawn(motorist, motorists)) motorists.add(motorist);
        }
    }
}
