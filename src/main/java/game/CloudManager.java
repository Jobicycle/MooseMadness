package game;

import actors.Actor;
import actors.Environment.Cloud;

import java.util.List;

/**
 * CloudManager classs handles when and where to spawn clouds. Chances of cloud spawn increase as sessionRunTime increases.
 */
public class CloudManager {
    private Stage stage;
    private List<Actor> clouds;

    /**
     * CloudManager constructor. Takes in stage and current cloud list
     * @param stage
     * @param clouds
     */
    public CloudManager(Stage stage, List<Actor> clouds) {
        this.stage = stage;
        this.clouds = clouds;
    }

    /**
     * randomCloud method. Decides if a cloud should be created at random positons and speed then added to the cloud list.
     * @param sessionRunTime
     */
    public void randomCloud(float sessionRunTime) {
        int cloudSpawnUpper = 200 - (int) sessionRunTime;
        if (cloudSpawnUpper < 20) cloudSpawnUpper = 20;

        if (Utils.randInt(1, cloudSpawnUpper) == 1) {
            Actor cloud = new Cloud(stage);
            cloud.setPosX(0 - cloud.getWidth());
            cloud.setPosY((int) Utils.randFloat(-stage.getHeight(), stage.getHeight()) / 3);
            cloud.setVx(Utils.randFloat(2f, 6f));
            cloud.setVy(Utils.randFloat(-4f, -0f));
            clouds.add(cloud);
        }
    }
}

