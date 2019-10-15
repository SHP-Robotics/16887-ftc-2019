package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//created by jonathon for 13981

public class Base extends OpMode {

    public DcMotor leftBack, rightBack, leftFront, rightFront, intakeMove, climbMotor;
    public Servo marker_servo;
    public ElapsedTime timer = new ElapsedTime();

    double left = 0;
    double right = 0;

    double slowPower = 0.4;
    double normalPower = 0.7;
    double fastPower = 1.0;

    public double slowSpeed = 0.4;
    public double normalSpeed = 0.8;
    public double fastSpeed = 1.0;

    public double drop_position = 1.0;
    public double up_position = 0.3;

    @Override

    public void init() {
        timer.reset();

        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");

        intakeMove = hardwareMap.get(DcMotor.class, "intakeMove");

        climbMotor = hardwareMap.get(DcMotor.class, "climbMotor");

        marker_servo = hardwareMap.get(Servo.class, "marker_servo");

        marker_servo.setPosition(up_position);
    }

    @Override
    public void start(){
        timer.reset();
        reset_drive_encoders();
        reset_climb_encoders();
    }

    public void stop_all(){
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
        climbMotor.setPower(0);
    }

    @Override
    public void loop() {
        telemetry.addData("Timer: ", timer.seconds());

        telemetry.addData("leftBack encoder: ", get_leftBack_motor_enc());
        telemetry.addData("rightBack encoder: ", get_rightBack_motor_enc());
        telemetry.addData("leftFront encoder: ", get_leftFront_motor_enc());
        telemetry.addData("rightFront encoder: ", get_rightFront_motor_enc());
        telemetry.addData("climber encoder: ", get_climb_enc());
    }

    //reset encoders

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

    public void reset_climb_encoders(){

        climbMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        climbMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }


    public void climb(double power) {

        double speed = Range.clip(power, -1, 1);
        climbMotor.setPower(-speed);

    }

    //climber

    public int get_climb_enc(){
        if(climbMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
            climbMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return climbMotor.getCurrentPosition();
    }

    //get leftBack encoders

    public int get_leftBack_motor_enc(){
        if(leftBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return leftBack.getCurrentPosition();
    }

    //get leftFront encoders

    public int get_leftFront_motor_enc(){
        if(leftFront.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return leftFront.getCurrentPosition();
    }

    //get rightBack encoders

    public int get_rightBack_motor_enc(){
        if(rightBack.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return rightBack.getCurrentPosition();
    }

    //get rightFront encoders

    public int get_rightFront_motor_enc(){
        if(rightFront.getMode() != DcMotor.RunMode.RUN_USING_ENCODER){
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        return rightFront.getCurrentPosition();
    }

    //drive in autonomous

    /**
     * @param power: power at which the motors run
     * @param inches: how far the robot will travel
     * @return if distance is reached
     */

    public boolean auto_drive(double power, double inches){
        double target_enc = ConstantVariables.K_PPIN_DRIVE * inches;
        telemetry.addData("Target: ", target_enc);
        double left_speed = -power;
        double right_speed = power;

        left_speed = Range.clip(left_speed, -1, 1);
        right_speed = Range.clip(right_speed, -1, 1);

        leftBack.setPower(left_speed);
        leftFront.setPower(left_speed);
        rightBack.setPower(right_speed);
        rightFront.setPower(right_speed);

        if(Math.abs(get_leftBack_motor_enc()) >= target_enc && Math.abs(get_rightBack_motor_enc()) >= target_enc
                && Math.abs(get_leftFront_motor_enc()) >= target_enc && Math.abs(get_rightFront_motor_enc()) >= target_enc){
            leftBack.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            rightFront.setPower(0);
            return true;

        }
        return false;

    }

    /**
     *
     * @param power
     * @param degrees
     * @return
     */

    //turn in autonomous

    public boolean auto_turn(double power, double degrees){
        double target_enc = Math.abs(ConstantVariables.K_PPDEG_DRIVE * degrees);
        telemetry.addData("Turning to: ", target_enc);

        if(Math.abs(get_leftBack_motor_enc()) >= target_enc && Math.abs(get_rightBack_motor_enc()) >= target_enc
         && Math.abs(get_leftFront_motor_enc()) >= target_enc && Math.abs(get_rightFront_motor_enc()) >= target_enc){
            leftBack.setPower(0);
            leftFront.setPower(0);
            rightBack.setPower(0);
            rightFront.setPower(0);
            return true;
        }

        else{
            leftBack.setPower(-power);
            leftFront.setPower(-power);
            rightBack.setPower(-power);
            rightFront.setPower(-power);
        }
        return false;
    }

    /*

    public void driveForward(double leftPower, double rightPower) {
        leftBack.setPower(-leftPower);
        rightBack.setPower(rightPower);
        leftFront.setPower(-leftPower);
        rightFront.setPower(rightPower);

        Range.clip(leftPower, -1, 1);
        Range.clip(rightPower, -1, 1);
    }

    public void turnRight() {
        leftBack.setPower(-0.2); //0.3
        rightBack.setPower(-0.2); //0.3
        leftFront.setPower(-0.2);
        rightFront.setPower(-0.2);
    }

    public void turnLeft() {
        leftBack.setPower(0.2);
        rightBack.setPower(0.2);
        leftFront.setPower(0.2);
        rightFront.setPower(0.2);
    }

    public void goStraight() {
        leftBack.setPower(-1);
        rightBack.setPower(1);
        leftFront.setPower(-1);
        rightFront.setPower(1);
    }

    public void goBack(){
        leftBack.setPower(1);
        rightBack.setPower(-1);
        leftFront.setPower(1);
        rightFront.setPower(-1);
    }

    public void turn_degrees(){
        leftBack.setPower(0.5);
        rightBack.setPower(0.5);
        leftFront.setPower(0.5);
        rightFront.setPower(0.5);
    }

    */
}
