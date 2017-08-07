package game;

import actors.Actor;
import actors.Obstacles.Motorist;

import java.util.List;

public class TrafficManager {
    private Stage stage;
    private List<Actor> motorists;
    private int[] laneXs = {190, 350, 510};

    public TrafficManager(Stage stage, List<Actor> motorists) {
        this.stage = stage;
        this.motorists = motorists;
    }

    public void randomMotorist() {
        if (Utils.randInt(1, 1000) >= 990) {
            Motorist motorist = new Motorist(stage);

            if (Utils.randInt(1, 10) >= 4) { //create a non-speeder (60% chance)
                safeMotoristSpawn(motorist, false, Utils.randInt(1, 2), 9, 8);
            } else { //create a speeder
                safeMotoristSpawn(motorist, true, Utils.randInt(0, 1), 12, 11);
            }

            motorists.add(motorist);
        }
    }


    private void safeMotoristSpawn(Motorist motorist, Boolean speeder, int lane, int maxSpeed, int minSpeed) {
        int screen;
        int tryPosY = 0;

        if (speeder) screen = 1;
        else screen = -1;

        motorist.setMaxSpeed(maxSpeed);
        motorist.setMinSpeed(minSpeed);
        motorist.setVy(Utils.randFloat(-maxSpeed, -minSpeed));
        motorist.setPosX(laneXs[lane] - motorist.getWidth() / 2 + Utils.randInt(-motorist.getWidth()/2, motorist.getWidth()/2));
        motorist.setPosY(stage.getHeight() * screen + tryPosY);

    }

}
