package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

// Created  for 16887.
@Autonomous(name="RedSkyStone", group="AUTO")
//@Disabled
public class RedSkyStone extends BaseRobot {
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
                if (auto_drive(1.0, 15)) {     //drive forward
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 1:
                if (auto_mecanum(1, 20)) {    // Strafe
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 2:
                if (open_servos())
                    stage++;
                break;
            case 3:
                if (close_servos())
                    stage++;
                break;
            default:
                break;
        }
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
    }
}
