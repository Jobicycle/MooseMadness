package game;

import actors.Actor;
import actors.PowerUps.Wrench;

import java.util.List;

public class PowerUpManager {
    private Stage stage;
    private List<Actor> powerUps;

    public PowerUpManager(Stage stage, List<Actor> powerUps) {
        this.stage = stage;
        this.powerUps = powerUps;
    }

    public void randomWrench(float sessionRunTime) {
        if (Utils.randInt(1, 500) == 1 && sessionRunTime > 20) {
            Actor wrench = new Wrench(stage);
            wrench.setPosX(Utils.randInt(142 + wrench.getWidth(), 768 - wrench.getWidth()));
            wrench.setPosY(-wrench.getHeight());
            powerUps.add(wrench);
        }
    }
}
