package Game.Managers;

import Game.Stage;
import Game.Utils;
import actors.Actor;
import actors.PowerUps.AirHorn;
import actors.PowerUps.Wrench;

import java.util.List;

/**
 * PowreUpManager class for handling when and where to create powerups during the Game loop.
 */
public class PowerUpManager {
    private Stage stage;
    private List<Actor> powerUps;

    /**
     * PowerUpManager constructor. Takes stage and powerup list.
     * @param stage
     * @param powerUps
     */
    public PowerUpManager(Stage stage, List<Actor> powerUps) {
        this.stage = stage;
        this.powerUps = powerUps;
    }

    /**
     * randomWrench method for deciding when and where to spawn a wrench powerup.
     * Wrenches will have a chance to be created after 20 seconds of Game play.
     * @param sessionRunTime
     */
    public void randomWrench(float sessionRunTime) {
        if (Utils.randInt(1, 750) == 1 && sessionRunTime >= 20) {
            Actor wrench = new Wrench(stage);
            wrench.setPosX(Utils.randInt(142 + wrench.getWidth(), 768 - wrench.getWidth()));
            wrench.setPosY(-wrench.getHeight());
            powerUps.add(wrench);
        }
    }

    /**
     * randomAirHorn method for deciding when and where to spawn an airhorn powerup.
     * AirHorns will have a chance to be created after 30 seconds of gameplay.
     * @param sessionRunTime
     */
    public void randomAirHorn(float sessionRunTime) {
        if (Utils.randInt(1, 1500) == 1 && sessionRunTime >= 30) {
            Actor airHorn = new AirHorn(stage);
            airHorn.setPosX(Utils.randInt(142 + airHorn.getWidth(), 768 - airHorn.getWidth()));
            airHorn.setPosY(-airHorn.getHeight());
            powerUps.add(airHorn);
        }
    }
}
