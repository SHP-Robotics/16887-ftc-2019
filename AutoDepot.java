package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class 9AutoDepot extends Base {
    private int stage = 0;
    private GoldAlignDetector detector;

    @Override
    public void init(){
        super.init();

        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        //set_marker_servo(ConstantVariables.K_MARKER_SERVO_UP);

        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 50; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        marker_servo.setPosition(up_position);

        //detector.enable();
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop(){
        super.loop();

        telemetry.addData("IsAligned", detector.getAligned());
        telemetry.addData("XPos", detector.getXPosition());

        switch(stage){

            case 0:

                break;

            default:

                break;

        }

    }

    @Override
    public void stop(){
        detector.disable();
    }

}
