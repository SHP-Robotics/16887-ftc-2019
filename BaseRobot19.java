+package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//created by jonathon for 13981

public class Base extends OpMode {


    public DcMotor leftBack, rightBack, leftFront, rightFront,
    public HexMotor lower,upper,
    public Serov left, right,

    public ElapsedTime timer = new ElapsedTime();

    @Override
    public void loop() {
        telemetry.addData("Timer: ", timer.seconds());

        telemetry.addData("leftBack encoder: ", get_leftBack_motor_enc());
        telemetry.addData("rightBack encoder: ", get_rightBack_motor_enc());
        telemetry.addData("leftFront encoder: ", get_leftFront_motor_enc());
        telemetry.addData("rightFront encoder: ", get_rightFront_motor_enc());
