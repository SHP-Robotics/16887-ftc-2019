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
        //keep marker_servo up
        set_marker_servo(ConstantVariables.K_MARKER_SERVO_UP);

        //drive train
        tankanum_drive(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x);

        //climber
        if(gamepad1.dpad_up) {
            climb(-1);
        } else if (gamepad1.dpad_down) {
            climb(1);
        } else {
            climb(0);
        }

        //flip
        if(gamepad1.a || gamepad1.dpad_right) {
            flip(-1);
        } else if (gamepad1.b) {
            flip(1);
        } else {
            flip(0);
        }

        //intake servo
        if(gamepad1.left_bumper) {
            set_intake_servo(ConstantVariables.K_INTAKE_SERVO_IN);
        } else if (gamepad1.right_bumper){
            set_intake_servo(ConstantVariables.K_INTAKE_SERVO_OUT);
        }

        //lift
        if(gamepad1.dpad_right) {
            lift(-1);
        } else if (gamepad1.dpad_left) {
            lift(1);
        } else {
            lift(0);
        }

        //bucket
        if(gamepad1.x) {
            bucket(-1);
        } else if (gamepad1.y) {
            bucket(1);
        } else {
            bucket(0);
        }
    }
}