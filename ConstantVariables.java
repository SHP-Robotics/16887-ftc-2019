package org.firstinspires.ftc.teamcode;
// Created for 16887

public class ConstantVariables {
    public static final int K_PPR_DRIVE = 1120;       // Number of ticks per revolution (AndyMark)
    public static final double K_DRIVE_WHEEL_DIA = 4; // Diameter of the wheel in inches?
    public static final double K_DRIVE_DIA = 16.5;    // Diameter

    public static final double K_DRIVE_WHEEL_CIRCUMFERENCE = K_DRIVE_WHEEL_DIA * Math.PI; //12.56637
    public static final double K_PPIN_DRIVE = K_PPR_DRIVE / K_DRIVE_WHEEL_CIRCUMFERENCE; //89.1267725

    public static final double K_TURN_CIRCUMFERENCE = K_DRIVE_DIA * Math.PI;  // Circumference
    public static final double K_PPTURN_DRIVE = K_PPIN_DRIVE * K_TURN_CIRCUMFERENCE;
    public static final double K_PPDEG_DRIVE = K_PPTURN_DRIVE / 360;     // in degrees

    public static final double K_DRIVE_ERROR_P = 250; // higher = less sensitive

    public static final int    K_LIFT_ONE_REV = K_PPR_DRIVE;  // one rev in encode ticks
    public static final double K_LIFT_NUM_REV = 1.5; // Number of revolution for each step
    public static final double K_LIFT_MAX_PWR = 1.0; // Maximum power (speed) to target position

    public static final int    K_LIFT2_ONE_REV = K_PPR_DRIVE;  // one rev in encode ticks
    public static final double K_LIFT2_NUM_REV = 1.5; // Number of revolution for each step
    public static final double K_LIFT2_MAX_PWR = 1.0; // Maximum power (speed) to target position

    public static final double K_FRONT_SERVO_UP = 0.0;     // Servo starts with UP position
    public static final double K_FRONT_SERVO_DOWN = 0.3;

    public static final double K_BACK_SERVO_UP = 0.0;     // Servo starts with UP position
    public static final double K_BACK_SERVO_DOWN = 0.3;   // Back is set to run in REVERSE?

    // Since the four motors are not identical, an additional factor is used to adjust the speed
    // so that they will run equally.
    // Since the power is below -1.0 and 1.0, these factors should be less than one to avoid
    // clipping
    public static final double K_LF_ADJUST = 1.0;
    public static final double K_LB_ADJUST = 1.0;
    public static final double K_RF_ADJUST = 1.0;
    public static final double K_RB_ADJUST = 1.0;
    // Multiplication factor for Angularity Velocity - ticks per second
    public static final double K_Ang_Rate_ADJUST = K_PPR_DRIVE * 1.5; // One revolution per sec?
}
// Andy Mark Motor Specification
// Ticks Per Revolution or PPR (Pulse Per Revolution): 1120
// Maximum speed: 128 revolution per minute? 2 revolution per second
//    public static final int K_UPPER_MOT_UP = (int) (0.6 * K_PPR_DRIVE);    // Position in ticks
//    public static final int K_UPPER_MOT_DOWN = (int) (-0.6 * K_PPR_DRIVE); // Position in ticks
//
//    public static final int K_LOWER_MOT_UP = (int) (0.7 * K_PPR_DRIVE);    // Position in ticks
//    public static final int K_LOWER_MOT_DOWN = (int) (-0.7 * K_PPR_DRIVE); // Position in ticks
