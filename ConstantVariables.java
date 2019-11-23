package org.firstinspires.ftc.teamcode;
// Created for 16887
public class ConstantVariables {
    public static final int    K_PPR_DRIVE = 1120;    // Number of ticks per revolution (AndyMark)
    public static final double K_DRIVE_WHEEL_DIA = 4; // Diameter of the mecanum wheel in inches?
    public static final double K_DRIVE_DIA = 16.5;    // Diameter?

    public static final double K_DRIVE_WHEEL_CIRCUMFERENCE = K_DRIVE_WHEEL_DIA * Math.PI; //12.56637inch
    public static final double K_PPIN_DRIVE = K_PPR_DRIVE / K_DRIVE_WHEEL_CIRCUMFERENCE; //89.1267725inch

    public static final double K_TURN_CIRCUMFERENCE = K_DRIVE_DIA * Math.PI;  // Circumference
    public static final double K_PPTURN_DRIVE = K_PPIN_DRIVE * K_TURN_CIRCUMFERENCE;
    public static final double K_PPDEG_DRIVE = K_PPTURN_DRIVE / 360;     // in degrees

    public static final double K_DRIVE_ERROR_P = 250; // higher = less sensitive

    public static final int    K_LIFT_ONE_REV = K_PPR_DRIVE;  // one rev in encode ticks
    public static final int  K_LIFT_INIT_DROP_FOUND = (int) (K_LIFT_ONE_REV * 2.0); // Initial drop in foundation, in ticks
    public static final int    K_LIFT_INIT_DROP_SKY = (int) (K_LIFT_ONE_REV * 1.0); // Initial drop in skytstone, in ticks
    public static final double K_LIFT_NUM_REV_PER_STEP = 1.5; // Number of revolution for each step
    public static final double K_LIFT_MAX_PWR = 1.0; // Maximum power (speed) to target position

    //public static final int    K_LIFT2_ONE_REV = K_PPR_DRIVE;  // one rev in encode ticks
    //public static final double K_LIFT2_NUM_REV = 1.5; // Number of revolution for each step
    //public static final double K_LIFT2_MAX_PWR = 1.0; // Maximum power (speed) to target position

    public static final double K_LEFT_SERVO_OPEN = 0.8;     // STEP
    public static final double K_LEFT_SERVO_CLOSE = 0.6;

    public static final double K_RIGHT_SERVO_OPEN = 0.63;     // STEP
    public static final double K_RIGHT_SERVO_CLOSE = 0.43;   // Back is set to run in REVERSE?

    // Since the four motors are not identical, an additional factor is used to adjust the speed
    // so that they will run equally.
    // Since the power is below -1.0 and 1.0, these factors should be less than one to avoid
    // clipping
    public static final double K_LF_ADJUST = 1.0;
    public static final double K_LB_ADJUST = 1.0;
    public static final double K_RF_ADJUST = 1.0;
    public static final double K_RB_ADJUST = 1.0;
    // Multiplication factor for Angularity Velocity - ticks per second
    public static final double K_Ang_Rate_ADJUST = K_PPR_DRIVE * 2.0; // One revolution per sec?
}
// Andy Mark Motor Specification
// Ticks Per Revolution or PPR (Pulse Per Revolution): 1120

// Maximum speed: 128 revolution per minute? 2 revolution per second
// PPR = Pulse Per Revolution = 1,120
// PPIN = Pulse Per INch = PPR / Circumference of the wheel = 1,120 (Pi * Diameter)
// PPDEG = Pulse Per DEGree = PPR / 360
// PPTURN = Pulse Per Turn =
