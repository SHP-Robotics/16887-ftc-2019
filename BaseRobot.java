package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import static com.qualcomm.robotcore.hardware.DistanceSensor.distanceOutOfRange;

//created by for 16887
public class BaseRobot extends OpMode {
    public DcMotor leftBack, rightBack, leftFront, rightFront, lift1; //lift2;
    public Servo left_servo, right_servo;
    public ColorSensor front_sensor;
    public DistanceSensor distance_sensor;
    public ElapsedTime timer = new ElapsedTime();
    private double left_servo_close, right_servo_close;  // the initial positions for close position
    private double left_servo_open, right_servo_open;
    public boolean DEBUG=false;                     // Debug flag
//    public Boolean autonmous = false;

    @Override
    public void init() {
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack   = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack  = hardwareMap.get(DcMotor.class, "rightBack");
        lift1      = hardwareMap.get(DcMotor.class, "lift1");
        left_servo   = hardwareMap.get(Servo.class, "left_servo");
        right_servo  = hardwareMap.get(Servo.class, "right_servo");
        front_sensor = hardwareMap.get(ColorSensor.class, "front_sensor");
        distance_sensor = hardwareMap.get(DistanceSensor.class, "front_sensor");

        // ZeroPowerBehavior of the motors
        telemetry.addData("INI Front ZeroP behavior:", "Left=%s, Right=%s", leftFront.getZeroPowerBehavior(), rightFront.getZeroPowerBehavior());
        telemetry.addData("INI Back ZeroP behavior: ", "Left=%s, Right=%s", leftBack.getZeroPowerBehavior(), rightBack.getZeroPowerBehavior());
        telemetry.addData("INI LIFT1 position", lift1.getCurrentPosition());
        telemetry.addData("INI Sen: ", "%d/ %d/ %d/ %d/ %d", front_sensor.alpha(), front_sensor.red(), front_sensor.green(), front_sensor.blue(), front_sensor.argb());
        telemetry.addData("INI Distance (cm)", distance_sensor.getDistance(DistanceUnit.CM));
        telemetry.addData("INI Servo dir: ", "LEFT=%s, RIGHT=%s", left_servo.getDirection(), right_servo.getDirection());
        telemetry.addData("INI Servo pos: ", "LEFT=%.2f, RIGHT=%.2f", left_servo.getPosition(), right_servo.getPosition());


        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift1.setDirection(DcMotorSimple.Direction.REVERSE);        // Because of the way the motor is mounted, this will avoid using negative numbers
        // Positive is UP and negative in down.  It resets to zero at start().
// Default direction is FORWARD. Front and Back rotates oppositely
//left_servo.resetDeviceConfigurationForOpMode();
//right_servo.resetDeviceConfigurationForOpMode();
        left_servo.setDirection(Servo.Direction.REVERSE);
        right_servo.setDirection(Servo.Direction.FORWARD);
        left_servo_close  = ConstantVariables.K_LEFT_SERVO_CLOSE;
        right_servo_close = ConstantVariables.K_RIGHT_SERVO_CLOSE;
        left_servo_open  = ConstantVariables.K_LEFT_SERVO_OPEN;
        right_servo_open = ConstantVariables.K_RIGHT_SERVO_OPEN;
    }
    @Override
    public void start() {
        timer.reset();
        reset_drive_encoders();         // reset all four motors: set encoders to zero and set modes
        reset_lift1_encoder();          // reset lift motor: set encoders to zero and set modes
        open_servos();                  // open the left and right servos
        front_sensor.enableLed(true);   // turn on the sensor LED
        telemetry.addData("START Front ZeroP behavior:", "Left=%s, Right=%s", leftFront.getZeroPowerBehavior(), rightFront.getZeroPowerBehavior());
        telemetry.addData("START Back ZeroP behavior: ", "Left=%s, Right=%s", leftBack.getZeroPowerBehavior(), rightBack.getZeroPowerBehavior());
        telemetry.addData("START LIFT1 position", lift1.getCurrentPosition());
        telemetry.addData("START Sen: ", "%d/ %d/ %d/ %d/ %d", front_sensor.alpha(), front_sensor.red(), front_sensor.green(), front_sensor.blue(), front_sensor.argb());
        telemetry.addData("START Distance (cm)", distance_sensor.getDistance(DistanceUnit.CM));
        telemetry.addData("START Servo dir: ", "LEFT=%s, RIGHT=%s", left_servo.getDirection(), right_servo.getDirection());
        telemetry.addData("START Servo pos: ", "LEFT=%.2f, RIGHT=%.2f", left_servo.getPosition(), right_servo.getPosition());
    }
    @Override
    public void loop() {
        if (DEBUG) {
            String detected_color = "";
            if (is_black(front_sensor.red(), front_sensor.blue())) detected_color = detected_color + " Black ";
            if (is_yellow(front_sensor.red(), front_sensor.green(), front_sensor.blue())) detected_color = detected_color + " Yellow ";

            telemetry.addData("Timer ", "%.1f", timer.seconds());
            telemetry.addData("Front power: ", "Left=%.2f, Right=%.2f", leftFront.getPower(), rightFront.getPower());
            telemetry.addData("Back  power: ", "Left=%.2f, Right=%.2f", leftBack.getPower(), rightBack.getPower());
            telemetry.addData("Front curr pos:", "Left=%d, Right=%d", get_leftFront_motor_enc(), get_rightFront_motor_enc());
            telemetry.addData("Back  curr pos:", "Left=%d, Right=%d", get_leftBack_motor_enc(), get_rightBack_motor_enc());
            telemetry.addData("LIFT1 motor current pos: ", "%d, Color = %s", get_lift1_motor_enc(), detected_color);
            telemetry.addData("Servo pos: ", "Left=%.2f, Right=%.2f", left_servo.getPosition(), right_servo.getPosition());
            telemetry.addData("Sen: ", " %d/ %d/ %d/ %d/ %d", front_sensor.alpha(), front_sensor.red(), front_sensor.green(), front_sensor.blue(), front_sensor.argb());
            telemetry.addData("Red：", front_sensor.red());
            telemetry.addData("Green：", front_sensor.green());
            telemetry.addData("Blue：", front_sensor.blue());
            double detected_dist = distance_sensor.getDistance(DistanceUnit.CM);
            if (detected_dist == distanceOutOfRange) {
                telemetry.addData("Distance: out of range", distance_sensor.getDistance(DistanceUnit.CM));
            } else {
                telemetry.addData("Distance: ", "%.2fcm", distance_sensor.getDistance(DistanceUnit.CM));
            }
        }
    }
    /* @param power:   the speed to turn at. Negative for reverse
     * @param dist_inches:  move "inches" inches.  "inches" positive for forward
     * @return Whether the target "inches" has been reached.
     */
    public boolean auto_drive(double power, double dist_inch) {
        int TARGET_ENC = (int) (ConstantVariables.K_PPIN_DRIVE * dist_inch);
        double left_speed = -power;                 // Left motors are running in reverse direction
        double right_speed = power;
//        double error = -get_leftFront_motor_enc() - get_rightFront_motor_enc();
//        error /= ConstantVariables.K_DRIVE_ERROR_P;
//        left_speed += error; right_speed -= error;

        left_speed = Range.clip(left_speed, -1, 1);
        right_speed = Range.clip(right_speed, -1, 1);
        leftFront.setPower(left_speed);
        leftBack.setPower(left_speed);
        rightFront.setPower(right_speed);
        rightBack.setPower(right_speed);

        if (DEBUG) telemetry.addData("Auto_D - Target_enc: ", "%d/%d", get_rightFront_motor_enc(), TARGET_ENC);
        if (Math.abs(get_rightFront_motor_enc()) >= TARGET_ENC) { // Arrived at target destination
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            return true;
        }
        return false;
    }
    /* @param power:   the speed to turn at. Negative for left.
     * @param degrees: the number of degrees to turn.
     * @return Whether the target angle has been reached.
     */
    public boolean auto_turn(double power, double degrees) {
        double TARGET_ENC = Math.abs(ConstantVariables.K_PPDEG_DRIVE * degrees);  // degrees to turns
        double speed = Range.clip(power, -1, 1);
        leftFront.setPower(-speed);
        leftBack.setPower(-speed);
        rightFront.setPower(-speed);
        rightBack.setPower(-speed);

        if (DEBUG) telemetry.addData("AUTO_T - TURNING TO ENC: ", TARGET_ENC);
        if (Math.abs(get_rightFront_motor_enc()) >= TARGET_ENC) {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            return true;
        } else {
            return false;
        }
    }
    // Positive for right, negative for left
    // Convert from inches to number of ticks per revolution
    public boolean auto_mecanum(double power, double inches) {
        double TARGET_ENC = ConstantVariables.K_PPIN_DRIVE * inches;
        double leftFrontPower = Range.clip(0 - power, -1.0, 1.0);
        double leftBackPower = Range.clip(0 + power, -1.0, 1.0);
        double rightFrontPower = Range.clip(0 - power, -1.0, 1.0);
        double rightBackPower = Range.clip(0 + power, -1.0, 1.0);

        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);
        if (DEBUG) telemetry.addData("MEC - Target_enc: ", "%.2f", TARGET_ENC);

        if (Math.abs(get_rightFront_motor_enc()) >= TARGET_ENC) {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            return true;
        } else {
            return false;
        }
    }
    public void tankanum_original(double rightPwr, double leftPwr, double lateralpwr) {
        rightPwr *= -1;

        double leftFrontPower = Range.clip(leftPwr - lateralpwr, -1.0, 1.0);
        double leftBackPower = Range.clip(leftPwr + lateralpwr, -1.0, 1.0);
        double rightFrontPower = Range.clip(rightPwr - lateralpwr, -1.0, 1.0);
        double rightBackPower = Range.clip(rightPwr + lateralpwr, -1.0, 1.0);

        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);
    }
    // lateralpwr: pos for right, neg for left
    // When strafing, rightPwr and leftPwr is assumed to be zero
    // With the change, there is no diagonal movement
    public void tankanum_drive(double rightPwr, double leftPwr, double lateralpwr) {
        rightPwr *= -1;                                 // rightPwr is in reverse
        double leftFrontPower, leftBackPower, rightFrontPower, rightBackPower;
        // When lateralpwr is very small, the robot moves purely forward or backward
        if (lateralpwr > -0.05 && lateralpwr < 0.05) {  // Purely forward or backward
            leftFrontPower  = leftPwr;
            leftBackPower   = leftPwr;
            rightFrontPower = rightPwr;                 // left = right's opposite
            rightBackPower  = rightPwr;
        } else {                                        // Strafe
            leftFrontPower  = -lateralpwr;              // leftFront = rightBack's opposite
            leftBackPower   = lateralpwr;               // leftBack  = rightFront's opposite
            rightFrontPower = -lateralpwr;
            rightBackPower  = lateralpwr;
        }
// to adjust the power among the motors so that they have almost equal ACTUAL PHYSICAL powers
leftFrontPower = Range.clip(leftFrontPower * ConstantVariables.K_LF_ADJUST, -1.0, 1.0);
leftBackPower = Range.clip(leftBackPower * ConstantVariables.K_LB_ADJUST, -1.0, 1.0);
rightFrontPower = Range.clip(rightFrontPower * ConstantVariables.K_RF_ADJUST, -1.0, 1.0);
rightBackPower = Range.clip(rightBackPower * ConstantVariables.K_RB_ADJUST, -1.0, 1.0);

        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);
        if (DEBUG) telemetry.addData("TANM- Lateral: ", lateralpwr);
    }
    public void tank_drive(double leftPwr, double rightPwr) {
        rightPwr *= -1;                     // rightPwr is in reverse
        double leftPower = Range.clip(leftPwr, -1.0, 1.0);
        double rightPower = Range.clip(rightPwr, -1.0, 1.0);

        leftFront.setPower(leftPower);
        leftBack.setPower(leftPower);
        rightFront.setPower(rightPower);    // right is opposite to left
        rightBack.setPower(rightPower);     // right is opposite to left
        if (DEBUG) telemetry.addData("TAND- Power: ", "Left=%.2f, Right=%.2f", leftPower, rightPower);
    }
    // SERVOS functions: open, close and reset
    public boolean open_servos() {
        if (!set_left_servo(left_servo_open) && DEBUG)
            telemetry.addData("LEFT_SERVO NOT OPEN YET: ", left_servo.getPosition());
        if (!set_right_servo(right_servo_open) && DEBUG)
            telemetry.addData("BACK_SERVO NOT OPEN YET: ", right_servo.getPosition());
        return((left_servo.getPosition() == left_servo_open) && (right_servo.getPosition() == right_servo_open));
    }
    public boolean close_servos() {
        if (!set_left_servo(left_servo_close) && DEBUG)
            telemetry.addData("LEFT SERVO NOT CLOSE YET: ", left_servo.getPosition());
        if (!set_right_servo(right_servo_close) && DEBUG)
            telemetry.addData("RIGHT SERVO NOT CLOSE YET: ", right_servo.getPosition());
        return((left_servo.getPosition() == left_servo_close) && (right_servo.getPosition() == right_servo_close));
    }
//    public void reset_servos() {
//        back_servo.resetDeviceConfigurationForOpMode();
//        front_servo.resetDeviceConfigurationForOpMode();
//    }
    // Overloading methods
    public boolean open_servos(double front_pos, double back_pos) {
        if (front_pos !=0)
            set_left_servo(front_pos);
        else
            set_left_servo(left_servo_close);
        if (back_pos !=0)
            set_right_servo(back_pos);
        else
            set_right_servo(right_servo_close);
        return((left_servo.getPosition() == front_pos) && (right_servo.getPosition() == back_pos));
    }
    public boolean close_servos(double front_pos, double back_pos) {
        if (front_pos !=0.0)
            set_left_servo(front_pos);
        else
            set_left_servo(left_servo_close);
        if (back_pos != 0.0)
            set_right_servo(back_pos);
        else
            set_right_servo(right_servo_close);
        return((left_servo.getPosition() == front_pos) && (right_servo.getPosition() == back_pos));
    }
    public boolean is_black(int red, int blue) { return blue > red*(3.0/4.0); }
    public boolean is_yellow(int red, int green, int blue) { return ((red > 2*blue) && (green > 2*blue)); }

    public boolean set_lift1_target_pos(int target_pos) {
        if (get_lift1_motor_enc() == target_pos) return true;

        lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift1.setTargetPosition(target_pos);
        lift1.setPower(ConstantVariables.K_LIFT_MAX_PWR);
        return(lift1.getTargetPosition() == target_pos);
    }
    // get lift encoder
    public int get_lift1_motor_enc() {      // RUN_TO_POSITION is more accurate
//        if (lift1.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
//            lift1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        }
        return lift1.getCurrentPosition();
    }
    private boolean set_left_servo(double target_pos) {
        double position = Range.clip(target_pos, 0, 1.0);
        left_servo.setPosition(position);
        return left_servo.getPosition() == position;
    }
    private boolean set_right_servo(double target_pos) {
        double position = Range.clip(target_pos, 0, 1.0);
        right_servo.setPosition(position);
        return right_servo.getPosition() == position;
    }
    public void reset_drive_encoders() {
        // The motor is to set the current encoder position to zero.
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // The motor is to do its best to run at targeted velocity.
//        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
// The motor is simply to run at whatever velocity is achieved by apply a particular power level to the motor.
leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void reset_lift1_encoder() {
        // The motor is to set the current encoder position to zero.
        lift1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // The motor is to attempt to rotate in whatever direction is necessary to cause
        // the encoder reading to advance or retreat from its current setting to the setting
        // which has been provided through the setTargetPosition() method.
        lift1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    //get leftBack encoder
    public int get_leftBack_motor_enc() {
//        if (leftBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
//            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
        return leftBack.getCurrentPosition();
    }
    //get leftFront encoder
    public int get_leftFront_motor_enc() {
//        if (leftFront.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
//            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
        return leftFront.getCurrentPosition();
    }
    //get rightBack encoder
    public int get_rightBack_motor_enc() {
//        if (rightBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
//            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
        return rightBack.getCurrentPosition();
    }
    //get rightFront encoder
    public int get_rightFront_motor_enc() {
//       if (rightFront.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
//            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        }
        return rightFront.getCurrentPosition();
    }
}
