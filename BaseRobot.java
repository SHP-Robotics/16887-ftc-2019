package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
//created by for 16887
public class BaseRobot extends OpMode {
    public DcMotor leftBack, rightBack, leftFront, rightFront, lift, lift2;
    public Servo back_servo, front_servo;
    public ElapsedTime timer = new ElapsedTime();
    public boolean DEBUG=false;                     // Debug flag

    @Override
    public void init() {
        leftFront  = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack   = hardwareMap.get(DcMotor.class, "leftBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack  = hardwareMap.get(DcMotor.class, "rightBack");
        lift       = hardwareMap.get(DcMotor.class, "lift");
        front_servo = hardwareMap.get(Servo.class, "front_servo");
        back_servo  = hardwareMap.get(Servo.class, "back_servo");
        // ZeroPowerBehavior of the motors
        telemetry.addData("LF ZeroP behavior ", leftFront.getZeroPowerBehavior());
        telemetry.addData("LB ZeroP behavior ", leftBack.getZeroPowerBehavior());
        telemetry.addData("RF ZeroP behavior ", rightFront.getZeroPowerBehavior());
        telemetry.addData("RB ZeroP behavior ", rightBack.getZeroPowerBehavior());
        telemetry.addData("LIFT ZeroP behavior ", lift.getZeroPowerBehavior());

        lift.setDirection(DcMotorSimple.Direction.FORWARD);
// Default direction is FORWARD. Front and Back rotates oppositely
// front_servo.setDirection(Servo.Direction.REVERSE);
        front_servo.setDirection(Servo.Direction.REVERSE);
        back_servo.setDirection(Servo.Direction.FORWARD);
        telemetry.addData("Servo ports:", "Front=%d, Back=%d", front_servo.getPortNumber(), back_servo.getPortNumber());
        telemetry.addData("Servo dir: ", "Front=%s, Back=%s", front_servo.getDirection(), back_servo.getDirection());
        open_servos();
    }
    @Override
    public void start() {
        timer.reset();
        reset_drive_encoders();
        reset_lift_encoder();
    }
    @Override
    public void loop() {
        if (DEBUG) {
            telemetry.addData("Timer ", "%.2f", timer.seconds());
            telemetry.addData("Front power: ", "Left=%.2f, Right=%.2f", leftFront.getPower(), rightFront.getPower());
            telemetry.addData("Back  power: ", "Left=%.2f, Right=%.2f", leftBack.getPower(), rightBack.getPower());
            telemetry.addData("Front curr pos:", "Left=%d, Right=%d", get_leftFront_motor_enc(), get_rightFront_motor_enc());
            telemetry.addData("Back  curr pos:", "Left=%d, Right=%d", get_leftBack_motor_enc(), get_rightBack_motor_enc());
            telemetry.addData("LIFT motor current pos: ", "%d", get_lift_motor_enc());
            telemetry.addData("Servo pos: ", "Left=%.2f, Right=%.2f", back_servo.getPosition(), front_servo.getPosition());
        }
    }
    /* @param power:   the speed to turn at. Negative for reverse
     * @param inches:  move "inches" inches.  "inches" is positive
     * @return Whether the target "inches" has been reached.
     */
    public boolean auto_drive(double power, double inches) {
        double TARGET_ENC = ConstantVariables.K_PPIN_DRIVE * inches;
        double left_speed = -power;
        double right_speed = power;
        double error = -get_leftFront_motor_enc() - get_rightFront_motor_enc();
        error /= ConstantVariables.K_DRIVE_ERROR_P;
        left_speed += error;
        right_speed -= error;

        left_speed = Range.clip(left_speed, -1, 1);
        right_speed = Range.clip(right_speed, -1, 1);
        leftFront.setPower(left_speed);
        leftBack.setPower(left_speed);
        rightFront.setPower(right_speed);
        rightBack.setPower(right_speed);

        if (DEBUG) telemetry.addData("Auto_D - Target_enc: ", TARGET_ENC);
        if (Math.abs(get_rightFront_motor_enc()) >= TARGET_ENC) {
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

        if (DEBUG) telemetry.addData("AUTO_T TURNING TO ENC: ", TARGET_ENC);
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

        if (DEBUG) telemetry.addData("MEC - Target_enc: ", TARGET_ENC);
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);

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
    // When strafing, rightPwr and leftPwr is assumed to be zero
    // With the change, there is no diagonal movement
    public void tankanum_drive(double rightPwr, double leftPwr, double lateralpwr) {
        rightPwr *= -1;    // rightPwr is in reverse
        double leftFrontPower, leftBackPower, rightFrontPower, rightBackPower;
        // When lateralpwr is very small, the robot purely forward or backward
        if (lateralpwr > -0.05 && lateralpwr < 0.05) {                  // Purely forward or backward
            leftFrontPower = Range.clip(leftPwr, -1.0, 1.0);
            leftBackPower = Range.clip(leftPwr, -1.0, 1.0);
            rightFrontPower = Range.clip(rightPwr, -1.0, 1.0);
            rightBackPower = Range.clip(rightPwr, -1.0, 1.0);
        } else {                                                        // Strafe
            leftFrontPower = Range.clip(-lateralpwr, -1.0, 1.0);
            leftBackPower = Range.clip(lateralpwr, -1.0, 1.0);
            rightFrontPower = Range.clip(-lateralpwr, -1.0, 1.0);
            rightBackPower = Range.clip(lateralpwr, -1.0, 1.0);
        }
// to adjust the power among the motors so that they have almost equal ACTUAL PHYSICAL powers
leftFrontPower = Range.clip(leftFrontPower * ConstantVariables.K_LF_ADJUST, -1.0, 1.0);
leftBackPower = Range.clip(leftBackPower * ConstantVariables.K_LF_ADJUST, -1.0, 1.0);
rightFrontPower = Range.clip(rightFrontPower * ConstantVariables.K_LF_ADJUST, -1.0, 1.0);
rightBackPower = Range.clip(rightBackPower * ConstantVariables.K_LF_ADJUST, -1.0, 1.0);

        if (DEBUG) telemetry.addData("TAN- Lateral: ", lateralpwr);
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);
    }
    public void tank_drive(double leftPwr, double rightPwr) {
        rightPwr *= -1;    // rightPwr is in reverse
        double leftPower = Range.clip(leftPwr, -1.0, 1.0);
        double rightPower = Range.clip(rightPwr, -1.0, 1.0);

        if (DEBUG) telemetry.addData("TAN- Power: ", "Left=%0.2f, Right=%0.2f", leftPower, rightPower);
        leftFront.setPower(leftPower);
        leftBack.setPower(leftPower);
        rightFront.setPower(rightPower);   // right is opposite to left
        rightBack.setPower(rightPower);    // right is opposite to left
    }
    // SERVOS functions: open, close and reset
    public boolean open_servos() {
        if (DEBUG && !set_front_servo(ConstantVariables.K_FRONT_SERVO_UP))
            telemetry.addData("FRONT_SERVO NOT OPEN YET: ", front_servo.getPosition());
        if (DEBUG && !set_back_servo(ConstantVariables.K_BACK_SERVO_UP))
            telemetry.addData("BACK_SERVO NOT OPEN YET: ", back_servo.getPosition());
        return((front_servo.getPosition() == ConstantVariables.K_FRONT_SERVO_UP) && (back_servo.getPosition() == ConstantVariables.K_BACK_SERVO_UP));
    }
    public boolean close_servos() {
        if (DEBUG && !set_front_servo(ConstantVariables.K_FRONT_SERVO_DOWN))
            telemetry.addData("FRONT SERVO NOT CLOSE YET: ", front_servo.getPosition());
        if (DEBUG && !set_back_servo(ConstantVariables.K_BACK_SERVO_DOWN))
            telemetry.addData("BACK SERVO NOT CLOSE YET: ", back_servo.getPosition());
        return((front_servo.getPosition() == ConstantVariables.K_FRONT_SERVO_UP) && (back_servo.getPosition() == ConstantVariables.K_BACK_SERVO_UP));
    }
    public void reset_servos() {
        back_servo.resetDeviceConfigurationForOpMode();
        front_servo.resetDeviceConfigurationForOpMode();
    }
    // Overloading methods
    public boolean open_servos(double front_pos, double back_pos) {
        if (front_pos !=0)
            set_front_servo(front_pos);
        else
            set_front_servo(ConstantVariables.K_FRONT_SERVO_UP);
        if (back_pos !=0)
            set_back_servo(back_pos);
        else
            set_back_servo(ConstantVariables.K_BACK_SERVO_UP);
        return((front_servo.getPosition() == front_pos) && (back_servo.getPosition() == back_pos));
    }
    public boolean close_servos(double front_pos, double back_pos) {
        if (front_pos !=0.0)
            set_front_servo(front_pos);
        else
            set_front_servo(ConstantVariables.K_FRONT_SERVO_DOWN);
        if (back_pos != 0.0)
            set_back_servo(back_pos);
        else
            set_back_servo(ConstantVariables.K_BACK_SERVO_DOWN);
        return((front_servo.getPosition() == front_pos) && (back_servo.getPosition() == back_pos));
    }
    public boolean set_lift_target_pos(int target_pos) {
        lift.setTargetPosition(target_pos);
        lift.setPower(ConstantVariables.K_LIFT_MAX_PWR);
        return(lift.getTargetPosition() == target_pos);
    }
    public boolean set_lift2_target_pos(int target_pos) {
        lift2.setTargetPosition(target_pos);
        lift2.setPower(ConstantVariables.K_LIFT2_MAX_PWR);
        return(lift2.getTargetPosition() == target_pos);
    }
    private boolean set_front_servo(double target_pos) {
        double position = Range.clip(target_pos, 0, 1.0);
        front_servo.setPosition(position);
        return front_servo.getPosition() == position;
    }
    private boolean set_back_servo(double target_pos) {
        double position = Range.clip(target_pos, 0, 1.0);
        back_servo.setPosition(position);
        return back_servo.getPosition() == position;
    }
    public void reset_drive_encoders() {
        // The motor is to set the current encoder position to zero.
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // The motor is to do its best to run at targeted velocity.
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }
    public void reset_lift_encoder() {
        // The motor is to set the current encoder position to zero.
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // The motor is to attempt to rotate in whatever direction is necessary to cause
        // the encoder reading to advance or retreat from its current setting to the setting
        // which has been provided through the setTargetPosition() method.
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void reset_lift2_encoder() {
        // The motor is to set the current encoder position to zero.
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // The motor is to attempt to rotate in whatever direction is necessary to cause
        // the encoder reading to advance or retreat from its current setting to the setting
        // which has been provided through the setTargetPosition() method.
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //get leftBack encoders
    public int get_leftBack_motor_enc() {
        if (leftBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return leftBack.getCurrentPosition();
    }
    //get leftFront encoders
    public int get_leftFront_motor_enc() {
        if (leftFront.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return leftFront.getCurrentPosition();
    }
    //get rightBack encoders
    public int get_rightBack_motor_enc() {
        if (rightBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return rightBack.getCurrentPosition();
    }
    //get rightFront encoders
    public int get_rightFront_motor_enc() {
        if (rightFront.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return rightFront.getCurrentPosition();
    }
    // get lift encoder
    public int get_lift_motor_enc() {      // RUN_TO_POSITION is more accurate
        if (lift.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        return lift.getCurrentPosition();
    }
}
