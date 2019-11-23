package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
// Created  for 16887.
@Autonomous(name="RedSkyStone Simple", group="Simple")
//@Disabled
public class RedSkyStoneSimple extends BaseRobot {
    private int stage = 0;
    @Override
    public void init() {
        super.init();
        DEBUG = true;
        timer.reset();
        reset_drive_encoders();         // reset all four motors: set encoders to zero and set modes
//        reset_lift1_encoder();          // reset lift motor: set encoders to zero and set modes
//        set_lift1_target_pos(-ConstantVariables.K_LIFT_INIT_DROP_SKY);  // Lower the rack
//        open_servos();                  // open the left and right servos
    }
    @Override
    public void start() { super.start(); }
    @Override
    public void loop() {
        // assumptions:
        // 1. the servos and arms are open
        // 2. the rack is down
        switch (stage) {
            case 0:          // SLOWLY Forward 5 inches
                if (auto_drive(0.5, 5.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 1:          // SLOWLY Strafe RIGHT up to 12 inches to park
                if (auto_mecanum(0.5, 12.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            default:
                break;
        }
        if (DEBUG) telemetry.addData("Red Skystone Simple: ", stage);
        super.loop();
    }
}
