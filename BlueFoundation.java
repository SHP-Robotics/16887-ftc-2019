package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

// Created  for 16887.
@Autonomous(name="BlueFoundation", group="AUTO")
//@Disabled
public class BlueFoundation extends BaseRobot {
    private int stage = 0;
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
        switch (stage) {
            case 0:
                if (auto_drive(1.0, 45)) {     //drive forward
                    if (get_leftFront_motor_enc() !=
                        get_leftBack_motor_enc() !=
                        get_rightFront_motor_enc() !=
                        get_rightBack_motor_enc() !=
                    )
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 1:
                if (set_lift_target_pos((int) (-ConstantVariables.K_LIFT_ONE_REV * ConstantVariables.K_LIFT_NUM_REV))) {
                reset_lift_encoder();
                reset_lift2_encoder();
                stage++;
        }
        break;

        case 2:
            if (auto_drive(-1,25)) {
                reset_drive_encoders();
                stage++;
            }
            break;

            case 3:
                if (auto_mecanum(1, 20)) {    // Strafe
                    reset_drive_encoders();
                    stage++;
                }
                break;
            default:
                break;
        }
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
    }
}
