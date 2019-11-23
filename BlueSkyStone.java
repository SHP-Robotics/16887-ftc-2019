package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import static com.qualcomm.robotcore.hardware.DistanceSensor.distanceOutOfRange;
// Created  for 16887.
@Autonomous(name="BlueSkyStone", group="AUTO")
//@Disabled
public class BlueSkyStone extends BaseRobot {
    private int stage = 0;
    private double distance_mec1 = 0.0;     // distance moved to find the 1st black skystone
//    private double distance_mec2 = 0.0;     // distance moved to find the 2nd black skystone
    private boolean found_stone1 = false;   // Found 1st black skystone
//    private boolean found_stone2 = false;   // Found 2nd black skystone
    private double detected_dist;           // Distance detected by the sensor in CM
    private boolean touches = false;       // Almost touching the skystone
    @Override
    public void init() {
        super.init();
        DEBUG = true;
        timer.reset();
        reset_drive_encoders();         // reset all four motors: set encoders to zero and set modes
        reset_lift1_encoder();          // reset lift motor: set encoders to zero and set modes
        set_lift1_target_pos(-ConstantVariables.K_LIFT_INIT_DROP_SKY);  // Lower the rack
        open_servos();                  // open the left and right servos
    }
    @Override
    public void start() { super.start(); }
    @Override
    public void loop() {
        // assumptions:
        // 1. the servos and arms are open
        // 2. the rack is down
        switch (stage) {
            case 0:          // SLOWLY Forward 45 inches or touches the skystone
                detected_dist = distance_sensor.getDistance(DistanceUnit.CM);
                if (!(detected_dist == distanceOutOfRange)) {   // Within sensor range
                    touches = detected_dist < 25.0;              // Almost touches the skystone
                }
                if (auto_drive(0.5, 70.0) || touches) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 1:          // SLOWLY Strafe RIGHT up to 60 inches until a black skystone is found
                found_stone1 = is_black(front_sensor.red(),front_sensor.blue());
                if (auto_mecanum(0.5, 60.0) || found_stone1) {
                    if (found_stone1) {             // Found the 1st black skystone
                        auto_mecanum(1.0, 60.0 + 2.0);  // Strafe from the edge to the center of the black skystone
                    } else {                        // Did not find a black skystone; Just pick up any skystone
                                                    // Detect the yellow skystone at the end?
                    }
                    distance_mec1 = get_rightFront_motor_enc() / ConstantVariables.K_PPIN_DRIVE; // Remember the distance moved to find the 1st skystone and c// onvert ticks to inches
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 2:         // CAREFULLY Move in 2.0 inch to start grabbing
                if (auto_drive(0.1, 2.0)) {
                    reset_drive_encoders();
                    stage++;
                }
            case 3:         // Lower racks
// if (set_lift1_target_pos(-ConstantVariables.K_LIFT_INIT_DROP_SKY)) {
//      reset_lift1_encoder();
//      stage++;
// }
                stage++;
                break;
            case 4:         // Close Servos: push back the two stones on the two sides and pick up the middle black stone
                if (close_servos()) stage++;
                break;
            case 5:         // Raise rack: the opposite of drop
                if (set_lift1_target_pos(ConstantVariables.K_LIFT_INIT_DROP_SKY)) {
                    stage++;
                }
                break;
            case 6:         // Back 20 inches
                if (auto_drive(-1.0, 40.0)) {
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 7:         // Move LEFT 30 inches + distance moved to find the black skystone (or turn right and go straight)
                if (auto_mecanum(-1.0, (100.0 + distance_mec1))) {    // Strafe right
                    reset_drive_encoders();
                    stage++;
                }
                break;
            case 8:         // Open Servos to drop the skystone
                if (open_servos())
                    stage++;
                break;
            case 9:         // Move RIGHT 15 inch to Park
                if (auto_mecanum(1.0, 80.0)) {   // Strafe RIGHT
                    reset_drive_encoders();
                    set_lift1_target_pos(120);                  // Set LIFT back to the original position
                    front_sensor.enableLed(false);              // Turn off LED
                    stage++;
                }
                break;
//            case 8:         // Strafe RIGHT back to find the 2nd black stone
//                if (found_stone1) {  // If there was a black stone, go find the next one
//                    if (auto_mecanum(1.0, 30.0 + distance_mec1)) {
//                        reset_drive_encoders();
//                        stage++;
//                    }
//                } else {             // If there was not a black stone, pick up any stone
//                    if (auto_mecanum(1.0, 30.0)) {
//                        reset_drive_encoders();
//                        stage++;
//                    }
//                }
//                break;
//            case 10:         // Forward 20 inches to the skystones
//                if (auto_drive(1.0, 20.0)) {
//                    reset_drive_encoders();
//                    stage++;
//                }
//                break;
//            case 11:         // Strafe RIGHT up to 2 inches to find another skystone
//                if (found_stone1) {   // If found_stone1 then find the 2nd one
//                    found_stone2 = is_black(front_sensor.red(),front_sensor.blue());
//                    if (auto_mecanum(1.0, 2.0) || found_stone2) {
//                        distance_mec2 = get_rightFront_motor_enc() / ConstantVariables.K_PPR_DRIVE; // Remember the distance moved to find the 2nd skystone and c// onvert ticks to inches
//                        reset_drive_encoders();
//                        stage++;
//                    }
//                } else {              // If (!found_stone1) then pick up a yellow skystone
//                    found_stone2 = is_yellow(front_sensor.red(), front_sensor.green(), front_sensor.blue());
//                    if (auto_mecanum(1.0, 2.0) || found_stone2) {
//                        distance_mec2 = get_rightFront_motor_enc() / ConstantVariables.K_PPR_DRIVE; // Remember the distance moved to find the 2nd skystone and c// onvert ticks to inches
//                        reset_drive_encoders();
//                        stage++;
//                    }
//              }
//                break;
//            case 12:
// Move back 30 + distance_mec1 + distance_mec2 depending of found_stone1 and found_stone2
  //              if (auto_mecanum(1.0, -(30.0 + distance_mec2))) {
//                    reset_drive_encoders();
//                    stage++;
//                }
//                break;
//            case 13:         // Open servos to drop the skystone
//                if (open_servos())
//                    stage++;
//                break;
//            case 14:         // Move LEFT 10 inches and stop on the line
//                if (auto_mecanum(1.0, -10.0)) {    // Strafe LEFT
//                    reset_drive_encoders();
//                    stage++;
//                }
//                break;
            default:
                break;
        }
        if (gamepad1.left_stick_button) DEBUG = !DEBUG; // Toggle the debug flag
        if (DEBUG) telemetry.addData("Blue Skystone: ", stage);
        super.loop();
    }
}
