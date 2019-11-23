package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Created for 16887.
@TeleOp(name="Test TeleOp", group="_Other")
//@Disabled
public class TestTeleOp extends BaseRobot {
    @Override
    public void init() {
        super.init();
    }
    @Override
    public void start() {
        super.start();
        DEBUG = true;
    }
    @Override
    public void loop() {
        if (gamepad1.left_bumper) {    // Using the power adjustment factors to balance the motors
            if (gamepad1.x) leftBack.setPower(ConstantVariables.K_LB_ADJUST);
            else leftBack.setPower(0);
            if (gamepad1.y) rightBack.setPower(ConstantVariables.K_RB_ADJUST);
            else rightBack.setPower(0);
            if (gamepad1.a) leftFront.setPower(ConstantVariables.K_LF_ADJUST);
            else leftFront.setPower(0);
            if (gamepad1.b) rightFront.setPower(ConstantVariables.K_RF_ADJUST);
            else rightFront.setPower(0);
        } else {
            if (gamepad1.x) leftBack.setPower(1);
            else leftBack.setPower(0);
            if (gamepad1.y) rightBack.setPower(1);
            else rightBack.setPower(0);
            if (gamepad1.a) leftFront.setPower(1);
            else leftFront.setPower(0);
            if (gamepad1.b) rightFront.setPower(1);
            else rightFront.setPower(0);
        }
        // lift motor
        if (gamepad1.left_bumper)
            set_lift1_target_pos((int)(-ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV_PER_STEP));
        else if (gamepad1.right_bumper)
            set_lift1_target_pos((int)(ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV_PER_STEP));
        else
            lift1.setPower(0);
        /*if (gamepad1.left_bumper)
            lift1.setPower(-1.0);
        else if (gamepad1.right_bumper)
            lift1.setPower(1.0);
        else
            lift1.setPower(0.0);
*/
        //open servo (UP)
        if (gamepad1.a) open_servos(); //find double through trial and error; set in constant variables
        //close servo (DOWN)
        if (gamepad1.b)  close_servos(); //find double through trial and error; set in constant variables
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
        super.loop();
    }
}