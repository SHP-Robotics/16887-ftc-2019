package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Created for 16887.
@TeleOp(name="TestEx TeleOp", group="_ExTeleOp")
//@Disabled
public class TestExTeleOp extends BaseRobotEx {
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
        if (gamepad1.left_bumper) {     // Using the power adjustment factors to balance the motors
            if (gamepad1.x) leftBack.setPower(ConstantVariables.K_LB_ADJUST * ConstantVariables.K_Ang_Rate_ADJUST);
            else leftBack.setPower(0);
            if (gamepad1.y) rightBack.setPower(ConstantVariables.K_RB_ADJUST * ConstantVariables.K_Ang_Rate_ADJUST);
            else rightBack.setPower(0);
            if (gamepad1.a) leftFront.setPower(ConstantVariables.K_LF_ADJUST * ConstantVariables.K_Ang_Rate_ADJUST);
            else leftFront.setPower(0);
            if (gamepad1.b) rightFront.setPower(ConstantVariables.K_RF_ADJUST * ConstantVariables.K_Ang_Rate_ADJUST);
            else rightFront.setPower(0);
        } else {
            if (gamepad1.x) leftBack.setPower(ConstantVariables.K_Ang_Rate_ADJUST);
            else leftBack.setPower(0);
            if (gamepad1.y) rightBack.setPower(ConstantVariables.K_Ang_Rate_ADJUST);
            else rightBack.setPower(0);
            if (gamepad1.a) leftFront.setPower(ConstantVariables.K_Ang_Rate_ADJUST);
            else leftFront.setPower(0);
            if (gamepad1.b) rightFront.setPower(ConstantVariables.K_Ang_Rate_ADJUST);
            else rightFront.setPower(0);
        }
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
        super.loop();
    }
}