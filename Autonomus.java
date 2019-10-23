package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

/*
 * Created by Chun on 1/26/19 for 10023.
 */

@Autonomous
@Disabled

public class TestAutonomous extends BaseRobot {
    private int stage = 0;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void loop() {
        super.loop();
        switch (stage) {
            case 0:
                if (auto_mecanum(1, 20)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            /*case 1:
                if (auto_turn(0.6, 360)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;*/
            default:
                break;
        }
    }
}
