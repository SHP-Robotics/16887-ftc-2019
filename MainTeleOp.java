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
        tankanum_drive(gamepad1.right_stick_y, gamepad1.left_stick_y, gamepad1.right_stick_x);

        // lift motor
        if (gamepad1.left_bumper)
            lift.setPower(-1);
        else if (gamepad1.right_bumper)
            lift.setPower(1);
        else
            lift.setPower(0);

      /*  // lift2 motor
        if (gamepad1.left_trigger>0.1)
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
        super.loop();
    }
}