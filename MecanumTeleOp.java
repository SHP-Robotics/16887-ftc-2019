package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Created for 16887.
@TeleOp(name="Auto Mecanum TeleOp", group="TeleOp")
//@Disabled
public class MecanumTeleOp extends BaseRobot {
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
        auto_mecanum(gamepad1.right_stick_y, gamepad1.left_stick_y);
        // lift motor
        if (gamepad1.left_bumper)
            set_lift_target_pos((int)(-ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV));
        else if (gamepad1.right_bumper)
            set_lift_target_pos((int)(ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV));
        else
            lift.setPower(0);
        //open servo (UP)
        if (gamepad1.a)  open_servos();   // find double through trial and error. Also set min and max
        //close servo (DOWN)
        if (gamepad1.b)   close_servos(); // find double through trial and error. Also set min and max
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
     }
}