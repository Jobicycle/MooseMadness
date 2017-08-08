package game;

import actors.Actor;
import actors.Obstacles.Motorist;

import java.util.List;

/**
 * TrafficManager class handles when and where to spawn other motorists.
 * Spawn rates increase as the game session goes on.
 */
public class TrafficManager {
    private Stage stage;
    private List<Actor> motorists;
    private int maxMotorists = 0;
    private int[] laneXPos = {194, 298, 402, 506, 610, 714}; //a list of the x coordinates of the middles of the lanes.

    /**
     * TrafficManager constructor. Takes in stage and the motorists list.
     * @param stage
     * @param motorists
     */
    public TrafficManager(Stage stage, List<Actor> motorists) {
        this.stage = stage;
        this.motorists = motorists;
    }

    /**
     * randomMotorist method is used to decide if it is time to create another motorist actor and add them to the list.
     * Number of motorists are set to 1 after 1 second of game play and increase by 1 every 15 seconds. A motorist may
     * either be speeding or normal. Speeding motorists are spawned below the player and normal above.
     * A safeSpawn utility is used in order to prevent motorists from being spawned on top of one another.
     * @param sessionRunTime
     */
    public void randomMotorist(float sessionRunTime) {
        maxMotorists = (int) sessionRunTime / 15 + 1; //limit motorists to 1 then +1 every 15 seconds.

        if (motorists.size() < maxMotorists && sessionRunTime > 1) { //if not at max motorists and past 1 sec game play.
            Motorist motorist = new Motorist(stage);
            int lane;

            if (Utils.randInt(1, 2) == 1) { //create a non speeder
                motorist.setVy(Utils.randFloat(-3, -1));
                motorist.setPosY(0 - motorist.getHeight());
            } else { //create a speeder
                motorist.setVy(Utils.randFloat(-8, -6));
                motorist.setPosY(stage.getHeight());
            }

            //"safely" assign motorist to a lane (they will probably be in an accident very shortly)
            lane = Utils.randInt(0, 5);
            motorist.setPosX(laneXPos[lane] - motorist.getWidth() / 2 + Utils.randInt(-motorist.getWidth() / 2, motorist.getWidth()) / 2);
            if (Utils.safeSpawn(motorist, motorists)) motorists.add(motorist);
        }
    }
}
