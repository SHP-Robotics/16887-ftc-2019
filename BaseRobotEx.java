package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotorEx;

// created by for 16887
// BaseRobotEx uses DcMotorEx to control the motors.  Velocity is used instead of Power
// Velocity is measured in ticks per second
// void setVelocity(double angularRate) - angularRate: the desired ticks per second
// double getVelocity() - Returns the current velocity of the motor, in ticks per second

public class BaseRobotEx extends OpMode {
    public DcMotorEx leftBack, rightBack, leftFront, rightFront, lift;
    public Servo back_servo, front_servo;
    public ElapsedTime timer = new ElapsedTime();
    public boolean DEBUG=false;                     // Debug flag

    @Override
    public void init() {
        leftBack   = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack  = hardwareMap.get(DcMotorEx.class, "rightBack");
        leftFront  = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        lift       = hardwareMap.get(DcMotorEx.class, "lift");
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
    public boolean auto_drive(double velocity, double inches) {
        double TARGET_ENC = ConstantVariables.K_PPIN_DRIVE * inches;
        double left_speed = -velocity;
        double right_speed = velocity;
        double error = -get_leftFront_motor_enc() - get_rightFront_motor_enc();
        error /= ConstantVariables.K_DRIVE_ERROR_P;
        left_speed += error;
        right_speed -= error;
// Angular Velocity Adjustment
        left_speed = left_speed * ConstantVariables.K_Ang_Rate_ADJUST;
        right_speed = right_speed * ConstantVariables.K_Ang_Rate_ADJUST;
        leftFront.setVelocity(left_speed);
        leftBack.setVelocity(left_speed);
        rightFront.setVelocity(right_speed);
        rightBack.setVelocity(right_speed);

        if (DEBUG) {
            double left_speed_REV_PER_S = left_speed / ConstantVariables.K_PPR_DRIVE;
            double right_speed_REV_PER_S = right_speed / ConstantVariables.K_PPR_DRIVE;
            telemetry.addData("Auto_D - Target_enc: ", TARGET_ENC);
            telemetry.addData("AUTO_D- Velocity:", "Left=%0.2f t/s, Right=%0.2f t/s", left_speed, right_speed);
            telemetry.addData("AUTO_D- Velocity:", "Left=%0.2f r/s, Right=%0.2f r/s", left_speed_REV_PER_S, right_speed_REV_PER_S);
        }
        if (Math.abs(get_rightFront_motor_enc()) >= TARGET_ENC) {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            return true;
        }
        return false;
    }
    /*
     * @param power:   the speed to turn at. Negative for left.
     * @param velocity: the desired angular Rate, in units per second.  It is converted to ticks per second by K_Ang_Rate_Adjust
     * @param degrees: the number of degrees to turn.
     * @return Whether the target angle has been reached.
     */
    public boolean auto_turn(double velocity, double degrees) {
        double TARGET_ENC = Math.abs(ConstantVariables.K_PPDEG_DRIVE * degrees);  // degrees to turns
        double speed = velocity * ConstantVariables.K_Ang_Rate_ADJUST;
        leftFront.setVelocity(-speed);
        leftBack.setVelocity(-speed);
        rightFront.setVelocity(-speed);
        rightBack.setVelocity(-speed);

        if (DEBUG) {
            double speed_REV_PER_S = speed / ConstantVariables.K_PPR_DRIVE;
            telemetry.addData("AUTO_T- TURNING TO ENC: ", TARGET_ENC);
            telemetry.addData("AUTO_T- Velocity:", "%0.2f tick/s", speed);
            telemetry.addData("AUTO_T- Velocity:", "%0.2f tick/s", speed_REV_PER_S);
        }
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
    //positive for right, negative for left
    public boolean auto_mecanum(double velocity, double inches) {
        double TARGET_ENC = ConstantVariables.K_PPIN_DRIVE * inches;

        double leftFrontVelo = Range.clip(0 - velocity, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        double leftBackVelo = Range.clip(0 + velocity, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        double rightFrontVelo = Range.clip(0 - velocity, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        double rightBackVelo = Range.clip(0 + velocity, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;

        leftFront.setVelocity(leftFrontVelo);
        leftBack.setVelocity(leftBackVelo);
        rightFront.setVelocity(rightFrontVelo);
        rightBack.setVelocity(rightBackVelo);

        if (DEBUG) {
            telemetry.addData("Mec- Target_enc: ", TARGET_ENC);
            telemetry.addData("Mec- Front Vel:", "Left=%0.2f, Right=%0.2f", leftFront.getVelocity(), rightFront.getVelocity());
            telemetry.addData("Mec- Back Vel: ", "Left=%0.2f, Right=%0.2f", leftBack.getVelocity(), rightBack.getVelocity());
        }
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
    // When moving sideway, rightVelo and leftVelo is assumed to be zero
    public void tankanum_drive(double rightVelo, double leftVelo, double lateralVelo) {
        rightVelo *= -1;    // rightPwr is in reverse
        double leftFrontVelo = Range.clip(leftVelo - lateralVelo, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        double leftBackVelo = Range.clip(leftVelo + lateralVelo, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        double rightFrontVelo = Range.clip(rightVelo - lateralVelo, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        double rightBackVelo = Range.clip(rightVelo + lateralVelo, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        // When lateralpwr is very small, the robot move purely forward or backward
//        if (lateralpwr > -0.05 && lateralpwr < 0.05) {                  // purely forward or backward
 //           leftFrontPower = Range.clip(leftPwr, -1.0, 1.0);
//            leftBackPower = Range.clip(leftPwr, -1.0, 1.0);
//            rightFrontPower = Range.clip(rightPwr, -1.0, 1.0);
//            rightBackPower = Range.clip(rightPwr, -1.0, 1.0);
//        } else {                                                        // Strafing
//            leftFrontPower = Range.clip(-lateralpwr, -1.0, 1.0);
//            leftBackPower = Range.clip(lateralpwr, -1.0, 1.0);
//            rightFrontPower = Range.clip(-lateralpwr, -1.0, 1.0);
//            rightBackPower = Range.clip(lateralpwr, -1.0, 1.0);
//        }

        leftFront.setVelocity(leftFrontVelo);
        leftBack.setVelocity(leftBackVelo);
        rightFront.setVelocity(rightFrontVelo);
        rightBack.setVelocity(rightBackVelo);

        if (DEBUG) {
            telemetry.addData("TAN Front:", " Left=%.2ft/s, Right%.2ft/s", leftFront.getVelocity(), rightFront.getVelocity());
            telemetry.addData("TAN Back :", " Left=%.2ft/s, Right%.2ft/s", leftBack.getVelocity(),  rightBack.getVelocity());
        }
    }
    public void tank_drive(double leftVelo, double rightVelo) {
        rightVelo *= -1;    // rightPwr is in reverse
        double leftVelocity = Range.clip(leftVelo, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;
        double rightVelocity = Range.clip(rightVelo, -1.0, 1.0) * ConstantVariables.K_Ang_Rate_ADJUST;

        leftFront.setVelocity(leftVelocity);
        leftBack.setVelocity(leftVelocity);
        rightFront.setVelocity(rightVelocity);   // right is opposite to left
        rightBack.setVelocity(rightVelocity);    // right is opposite to left
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
    public boolean set_front_servo(double target_pos) {
        double position = Range.clip(target_pos, 0, 1.0);
        front_servo.setPosition(position);
        return front_servo.getPosition() == position;
    }
    public boolean set_back_servo(double target_pos) {
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
    //get leftBack encoders
    public int get_leftBack_motor_enc() {
        if (leftBack.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
            leftBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        return leftBack.getCurrentPosition();
    }
    //get leftFront encoders
    public int get_leftFront_motor_enc() {
        if (leftFront.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
            leftFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        return leftFront.getCurrentPosition();
    }
    //get rightBack encoders
    public int get_rightBack_motor_enc() {
        if (rightBack.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
            rightBack.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        return rightBack.getCurrentPosition();
    }
    //get rightFront encoders
    public int get_rightFront_motor_enc() {
        if (rightFront.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
            rightFront.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
        return rightFront.getCurrentPosition();
    }
    // get lift encoder
    public int get_lift_motor_enc() {
        if (lift.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
            lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        return lift.getCurrentPosition();
    }
}