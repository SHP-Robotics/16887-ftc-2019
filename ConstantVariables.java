package org.firstinspires.ftc.teamcode;

/**
 * Created by andrew on Nov 14, 2017 as part of ftc_app in org.firstinspires.ftc.teamcode.
 * and edited by matthew for 13981
 */

public class ConstantVariables {
    public static final int K_FLIP_MIN = 0;
    public static final int K_FLIP_MAX = 10000;
    public static final int K_FLIP_ERROR_P = 500;

    public static final int K_INTAKE_EXTENSION_MIN = 0;
    public static final int K_INTAKE_EXTENSION_MAX = 10000;

    public static final int K_CLIMB_MIN = 0;
    public static final int K_CLIMB_MAX = 10000;

    public static final double K_MARKER_SERVO_UP = 1;
    public static final double K_MARKER_SERVO_DOWN = 0.5;

    public static final double K_WEDGE_SERVO_UP = 0.2;
    public static final double K_WEDGE_SERVO_DOWN = 1;

    public static final int K_PPR_DRIVE = 1120; //280
    public static final double K_DRIVE_WHEEL_DIA = 4;
    public static final double K_DRIVE_DIA = 16.5;

    public static final double K_DRIVE_WHEEL_CIRCUMFERENCE = K_DRIVE_WHEEL_DIA * Math.PI;
    public static final double K_PPIN_DRIVE = K_PPR_DRIVE / K_DRIVE_WHEEL_CIRCUMFERENCE;

    public static final double K_TURN_CIRCUMFERENCE = K_DRIVE_DIA * Math.PI;
    public static final double K_PPTURN_DRIVE = K_PPIN_DRIVE * K_TURN_CIRCUMFERENCE;
    public static final double K_PPDEG_DRIVE = K_PPTURN_DRIVE / 360;

    public static final double K_DRIVE_ERROR_P = 250; // higher = less sensitive
}