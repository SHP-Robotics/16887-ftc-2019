package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//created by jonathon for 13981

@TeleOp
public class BaseTeleOp extends Base {

    @Override
    public void init(){

        super.init();

        /*leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        climber.setMode(DcMotor.RunMode.RUN_USING_ENCODER);*/
    }

    @Override
    public void start(){

        super.start();
    }

    @Override
    public void loop(){
        super.loop();

        //climber

        if(gamepad1.dpad_up) {
            climb(1);
        } else if (gamepad1.dpad_down) {
            climb(-1);
        } else {
            climb(0);
        }

        //drive train

        left = this.gamepad1.left_stick_y;
        right = -this.gamepad1.right_stick_y;

        leftBack.setPower(left);
        rightBack.setPower(right);
        leftFront.setPower(left);
        rightFront.setPower(right);



        //tank drive sensitivity
/*
        if (gamepad1.left_stick_y > 0 && gamepad1.left_stick_y < 0.5) {

            leftBack.setPower(0.3);
            leftFront.setPower(0.3);

        }
        else if (gamepad1.left_stick_y > 0.5 && gamepad1.left_stick_y < 0.9) {

            leftBack.setPower(0.5);
            leftFront.setPower(0.5);

        }
        else if (gamepad1.left_stick_y > 0.9) {

            leftBack.setPower(0.6);
            leftFront.setPower(0.6);

        }
        else if (gamepad1.right_stick_y > 0 && gamepad1.right_stick_y < 0.5) {

            rightBack.setPower(-0.3);
            rightFront.setPower(-0.3);

        }
        else if (gamepad1.right_stick_y > 0.5 && gamepad1.right_stick_y < 0.9) {

            rightBack.setPower(-0.5);
            rightFront.setPower(-0.5);

        }
        else if (gamepad1.right_stick_y > 0.9) {

            rightBack.setPower(-0.6);
            rightFront.setPower(-0.6);

        }
*/

        if (gamepad1.right_trigger > 0.7 && gamepad1.right_trigger < 0.9) {

            intakeMove.setPower(0.8);

        }
        else if (gamepad1.left_trigger > 0.7 && gamepad1.left_trigger < 0.9) {

            intakeMove.setPower(-0.8);

        }
        else if(gamepad1.right_trigger > 0.3 && gamepad1.right_trigger < 0.7){

            intakeMove.setPower(0.4);

        }
        else if(gamepad1.left_trigger > 0.3 && gamepad1.left_trigger < 0.7){

            intakeMove.setPower(-0.4);

        }
        else if(gamepad1.right_trigger > 0.1 && gamepad1.right_trigger < 0.3){

            intakeMove.setPower(0.2);

        }
        else if(gamepad1.left_trigger > 0.1 && gamepad1.left_trigger < 0.3){

            intakeMove.setPower(-0.2);

        }
        else {
            intakeMove.setPower(0);
        }

    }

}
