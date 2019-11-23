package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// Created  for 16887.
@Autonomous(name="BlueFoundation", group="AUTO")
//@Disabled
public class BlueFoundation extends BaseRobot {
    private int stage = 0;
    @Override
    public void init() { super.init(); }
    @Override
    public void start() { super.start(); }
    @Override
    public void loop() {
        // Assumptions:
        // 1. the servos and arms are open
        // 2. the rack is up
        switch (stage) {
            case 0:          // Forward 20 inches
                if (auto_drive(1.0, 20.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 1:         // Lower racks to touch the foundation
                if (set_lift1_target_pos(ConstantVariables.K_LIFT_INIT_DROP_FOUND)) {
                    reset_lift1_encoder(); // reset_lift2_encoder();
                    stage++;
                }
                break;
            case 3:         // Back 10 inches
                if (auto_drive(1.0, -10.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 4:         // Lift racks to release the foundation
                if (set_lift1_target_pos(-ConstantVariables.K_LIFT_INIT_DROP_FOUND)) {
                    reset_lift1_encoder();  //reset_lift2_encoder();
                    stage++;
                }
                break;
            case 5:         // Move RIGHT 30 inches (or turn right and go straight)
                 if (auto_mecanum(1.0, 30.0)) {    // Strafe RIGHT
                    reset_drive_encoders();
                    stage++;
                 }
                 break;
             default:
                 break;
        }
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
        if (DEBUG) telemetry.addData("Blue Foundation: ", stage);
        super.loop();
    }
}