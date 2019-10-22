package org.firstinspires.ftc.teamcode;

/**
 * Created by Chun on Jan 26, 2019 for 10023.
 */

public class ConstantVariables {
    public static final double K_MARKER_SERVO_UP = 0.72;
    public static final double K_MARKER_SERVO_DOWN = 0.05;

    //public static final double K_INTAKE_SERVO_OUT = 0;
    //public static final double K_INTAKE_SERVO_IN = 0.65;

    public static final int K_PPR_DRIVE = 1120;
    public static final double K_DRIVE_WHEEL_DIA = 4;
    public static final double K_DRIVE_DIA = 16.5;

    public static final double K_DRIVE_WHEEL_CIRCUMFERENCE = K_DRIVE_WHEEL_DIA * Math.PI; //12.56637
    public static final double K_PPIN_DRIVE = K_PPR_DRIVE / K_DRIVE_WHEEL_CIRCUMFERENCE; //89.1267725

    public static final double K_TURN_CIRCUMFERENCE = K_DRIVE_DIA * Math.PI;
    public static final double K_PPTURN_DRIVE = K_PPIN_DRIVE * K_TURN_CIRCUMFERENCE;
    public static final double K_PPDEG_DRIVE = K_PPTURN_DRIVE / 360;

    public static final double K_MAX_CLIBER = 1465;

    public static final double K_DRIVE_ERROR_P = 250; // higher = less sensitive
}
