package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// Created for 16887.
@TeleOp(name="Main TeleOp", group="TeleOp")
//@Disabled
public class MainTeleOp extends BaseRobot {
    @Override
    public void init() { super.init(); }
    @Override
    public void start() { super.start(); }
    @Override
    public void loop() {
        super.loop();
        tankanum_drive(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // lift motor
    /*    if (gamepad1.left_bumper)
            set_lift1_target_pos((int)(-ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV_PER_STEP));
        else if (gamepad1.right_bumper)
            set_lift1_target_pos((int)(ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV_PER_STEP));
        else
            lift1.setPower(0);
      */  if (gamepad1.left_bumper)
            lift1.setPower(-1.0);
        else if (gamepad1.right_bumper)
            lift1.setPower(1.0);
        else
            lift1.setPower(0.0);

        /*// lift2 motor
        if (gamepad1.left_trigger>0.1)
            set_lift2_target_pos((int)(-ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV_PER_STEP));
        else if (gamepad1.right_trigger>0.1)
            set_lift2_target_pos((int)(ConstantVariables.K_LIFT_ONE_REV  * ConstantVariables.K_LIFT_NUM_REV_PER_STEP));
        else
            lift2.setPower(0);
        /*if (gamepad1.left_trigger>0.1)
            lift2.setPower(-gamepad1.left_trigger);
        else if (gamepad1.right_trigger>0.1)
            lift2.setPower(gamepad1.left_trigger);
        else
            lift2.setPower(0);
*/
        //open servo (UP)
        if (gamepad1.a) open_servos(); //find double through trial and error; set in constant variables
        //close servo (DOWN)
        if (gamepad1.b)  close_servos(); //find double through trial and error; set in constant variables

        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
    }
}