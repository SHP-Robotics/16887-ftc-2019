package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//created by jonathon for 13981

public class BaseRobot extends OpMode {
    public DcMotor leftBack, rightBack, leftFront, rightFront, lower, upper;
    public Servo left, right;


    //public Enc leftBack_enc, rightBack_enc, leftFront_enc, rightFront_enc;

    public ElapsedTime timer = new ElapsedTime();

//    double left = 0;
//    double right = 0;

    double slowPower = 0.4;
    double normalPower = 0.7;
    double fastPower = 1.0;

    public double slowSpeed = 0.4;
    public double normalSpeed = 0.8;
    public double fastSpeed = 1.0;

    @Override
    public void init() {
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        //climbMotor = hardwareMap.get(DcMotor.class, "climbMotor");

       // flipMotor = hardwareMap.get(DcMotor.class, "flipMotor");
        //liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");
        //bucketMotor = hardwareMap.get(DcMotor.class, "bucketMotor");

        //marker_servo = hardwareMap.get(Servo.class, "marker_servo");
        //intake_servo = hardwareMap.get(Servo.class, "intake_servo");

        //set_marker_servo(ConstantVariables.K_MARKER_SERVO_UP);
        //set_intake_servo(ConstantVariables.K_INTAKE_SERVO_IN);
    }

    @Override
    public void start() {
        timer.reset();
        reset_drive_encoders();
        //reset_climb_encoders();
        //reset_intake_outtake_encoders();
    }

    @Override
    public void loop() {
        telemetry.addData("Timer: ", timer.seconds());

        telemetry.addData("leftBack encoder: ", get_leftBack_enc());
        telemetry.addData("rightBack encoder: ", get_rightBack_enc());
        telemetry.addData("leftFront encoder: ", get_leftFront_enc());
        telemetry.addData("rightFront encoder: ", get_rightFront_enc());

        reset encoders;
    }

    //public void climb(double power) {
        //double speed = Range.clip(power, -1, 1);
        //climbMotor.setPower(speed);
   // }

    //public void flip(double power) {
        //double speed = Range.clip(power, -1, 1);
        //flipMotor.setPower(speed);
    //}

    //public void lift(double power) {
        //double speed = Range.clip(power, -1, 1);
        //liftMotor.setPower(speed);
    //}

    //public void bucket(double power) {
        //double speed = Range.clip(power, -1, 1);
        //bucketMotor.setPower(speed);
    //}

    public boolean auto_drive(double power, double inches) {
        double TARGET_ENC = ConstantVariables.K_PPIN_DRIVE * inches;
        telemetry.addData("Target_enc: ", TARGET_ENC);
        double left_speed = -power;
        double right_speed = power;
        double error = -get_leftFront_enc() - get_rightFront_enc();
        error /= ConstantVariables.K_DRIVE_ERROR_P;
        left_speed += error;
        right_speed -= error;

        left_speed = Range.clip(left_speed, -1, 1);
        right_speed = Range.clip(right_speed, -1, 1);
        leftFront.setPower(leftBack_speed);
        leftBack.setPower(leftFront_speed);
        rightFront.setPower(rightBack_speed);
        rightBack.setPower(rightFront_speed);

        if (Math.abs(get_rightFront_enc()) >= TARGET_ENC) {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            return true;
        }
        return false;
    }

    /**
     * @param power:   the speed to turn at. Negative for left.
     * @param degrees: the number of degrees to turn.
     * @return Whether the target angle has been reached.
     */
    public boolean auto_turn(double power, double degrees) {
        double TARGET_ENC = Math.abs(ConstantVariables.K_PPDEG_DRIVE * degrees);
        telemetry.addData("D99 TURNING TO ENC: ", TARGET_ENC);

        double speed = Range.clip(power, -1, 1);
        leftFront.setPower(-speed);
        leftBack.setPower(-speed);
        rightFront.setPower(-speed);
        rightBack.setPower(-speed);

        if (Math.abs(get_rightFront_enc()) >= TARGET_ENC) {
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
    public boolean auto_mecanum(double power, double inches) {
        double TARGET_ENC = ConstantVariables.K_PPIN_DRIVE * inches;
        telemetry.addData("Target_enc: ", TARGET_ENC);

        double leftFrontPower = Range.clip(0 - power, -1.0, 1.0);
        double leftBackPower = Range.clip(0 + power, -1.0, 1.0);
        double rightFrontPower = Range.clip(0 - power, -1.0, 1.0);
        double rightBackPower = Range.clip(0 + power, -1.0, 1.0);

        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);

        if (Math.abs(get_rightFront_enc()) >= TARGET_ENC) {
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            return true;
        } else {
            return false;
        }
    }

    public void tankanum_drive(double rightPwr, double leftPwr, double lateralpwr) {
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

    //public void set_marker_servo(double pos) {
        //double position = Range.clip(pos, 0, 1.0);
        //marker_servo.setPosition(position);
    //}

    //public void set_intake_servo(double pos) {
        //double position = Range.clip(pos, 0, 1.0);
        //intake_servo.setPosition(position);
    //}

    public void reset_drive_encoders() {
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
}
