package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
Created by Chun on 1/26/19 for 10023.
*/

@TeleOp

public class MainTeleOp extends BaseRobot {

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
        // keep marker_servo up
// set_marker_servo(ConstantVariables.K_MARKER_SERVO_UP);

        //drive train
        tankanum_drive(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x);

            }
}
