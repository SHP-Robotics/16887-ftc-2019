package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// Created  for 16887.
@Autonomous(name="BlueSkyStone", group="AUTO")
//@Disabled
public class BlueSkyStone extends BaseRobot {
    private int stage = 0;
    private double distance_mec1 = 0.0;    // distance moved to find the 1st black skystone
    private double distance_mec2 = 0.0;    // distance moved to find the 2nd black skystone
    private boolean found_stone1 = false;  // Found 1st black skystone
    private boolean found_stone2 = false;  // Found 2nd black skystone
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
        // assumptions:
        // 1. the servos and arms are open
        // 2. the rack is up
        switch (stage) {
            case 0:          // Forward 45 inches
//                if (auto_drive(1.0, 45.0) || (is_black(front_sensor.red(),front_sensor.blue()))) {
                if (auto_drive(1.0, 45.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 1:          // Strafe RIGHT up to 6 inches until a black skystone is found
                found_stone1 = is_black(front_sensor.red(),front_sensor.blue());
                if (auto_mecanum(1.0, 6.0) || found_stone1) {
                    if (found_stone1) {             // Found the 1st black skystone
                    } else {                        // Did not find a black skystone; Just pick up any skystone
                                                    // Detect the yellow skystone at the end
                    }
                    distance_mec1 = get_rightFront_motor_enc() / ConstantVariables.K_PPR_DRIVE; // Remember the distance moved to find the 1st skystone and c// onvert ticks to inches
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 2:         // Lower racks
                if (set_lift1_target_pos(ConstantVariables.K_LIFT_INIT_DROP_SKY)) {
                    reset_lift1_encoder();
                    //reset_lift2_encoder();
                    stage++;
                }
                break;
            case 3:         // Close Servos: push back the two stones on the two sides and pick up the middle black stone
                if (close_servos()) stage++;
                break;
            case 4:         // Raise rack: the opposite of drop
                if (set_lift1_target_pos(-ConstantVariables.K_LIFT_INIT_DROP_SKY)) {
                    reset_lift1_encoder();
                    //reset_lift2_encoder();
                    stage++;
                }
                break;
            case 5:         // Back 20 inches
                if (auto_drive(1.0, -20.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 6:         // Move LEFT 30 inches + distance moved to find the black skystone (or turn right and go straight)
                if (auto_mecanum(1.0, -(30.0 + distance_mec1))) {    // Strafe right
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 7:         // Open Servos to drop the skystone
                if (open_servos()) stage++;
                break;
            case 8:         // Strafe RIGHT back to find the 2nd black stone
                if (found_stone1) {  // If there was a black stone, go find the next one
                    if (auto_mecanum(1.0, 30.0 + distance_mec1)) {
                        reset_drive_encoders();
                        stage++;
                    }
                } else {             // If there was not a black stone, pick up any stone
                    if (auto_mecanum(1.0, 30.0)) {
                        reset_drive_encoders();
                        stage++;
                    }
                }
                break;
            case 10:         // Forward 20 inches to the skystones
                if (auto_drive(1.0, 20.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 11:         // Strafe RIGHT up to 2 inches to find another skystone
                if (found_stone1) {   // If found_stone1 then find the 2nd one
                    found_stone2 = is_black(front_sensor.red(),front_sensor.blue());
                    if (auto_mecanum(1.0, 2.0) || found_stone2) {
                        distance_mec2 = get_rightFront_motor_enc() / ConstantVariables.K_PPR_DRIVE; // Remember the distance moved to find the 2nd skystone and c// onvert ticks to inches
                        reset_drive_encoders();
                        stage++;
                    }
                } else {              // If (!found_stone1) then pick up a yellow skystone
                    found_stone2 = is_yellow(front_sensor.red(), front_sensor.green(), front_sensor.blue());
                    if (auto_mecanum(1.0, 2.0) || found_stone2) {
                        distance_mec2 = get_rightFront_motor_enc() / ConstantVariables.K_PPR_DRIVE; // Remember the distance moved to find the 2nd skystone and c// onvert ticks to inches
                        reset_drive_encoders();
                        stage++;
                    }
                }
                break;
            case 12:
// Move back 30 + distance_mec1 + distance_mec2 depending of found_stone1 and found_stone2
                if (auto_mecanum(1.0, -(30.0 + distance_mec2))) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 13:         // Open servos to drop the skystone
                if (open_servos())
                    stage++;
                break;
            case 14:         // Move LEFT 10 inches and stop on the line
                if (auto_mecanum(1.0, -10.0)) {    // Strafe LEFT
                    reset_drive_encoders();
                    stage++;
                }
                break;
            default:
                break;
        }
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
        super.loop();
        if (DEBUG) telemetry.addData("Blue Skystone: ", stage);
    }
}
