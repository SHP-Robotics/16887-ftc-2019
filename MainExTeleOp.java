package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// Created for 16887.
@TeleOp(name="MainEx TeleOp", group="_ExTeleOp")
//@Disabled
public class MainExTeleOp extends BaseRobotEx {
    @Override
    public void init() { super.init(); }
    @Override
    public void start() { super.start(); }
    @Override
    public void loop() {
        tankanum_drive(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // lift motor
        if (gamepad1.left_bumper)
            set_lift_target_pos((int)(-ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV));
        else if (gamepad1.right_bumper)
            set_lift_target_pos((int)(ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV));
        else
            lift.setPower(0);
        //open servo (UP)
        if (gamepad1.a) open_servos(); //find double through trial and error; set in constant variables
        //close servo (DOWN)
        if (gamepad1.b)  close_servos(); //find double through trial and error; set in constant variables
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
        super.loop();
    }
}