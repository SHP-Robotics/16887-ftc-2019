package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoCrater extends Base {
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

        detector.enable();
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

                if(Math.abs(get_climb_enc())> 4000){
                    climb(0);
                    timer.reset();
                    stage++;
                }else{
                    climb(1);
                }

                break;

            case 2:

                if(auto_drive(0.3, -2)){
                    reset_drive_encoders();
                    detector.enable();
                    stage++;
                }

                break;

            case 3:

                if(detector.getAligned()){
                    stage +=3;
                }
                else{
                    stage++;
                }

                break;

            case 4:

                if(detector.getXPosition() > 280){
                    if(detector.getAligned()){
                        stage+=2;
                    }
                    else if(auto_turn(0.4, 5)){
                        reset_drive_encoders();
                    }
                }
                else{
                    stage++;
                }

                break;

            case 5:

                if(detector.getXPosition() < 280){
                    if(detector.getAligned()){
                        stage++;
                    }
                    else if(auto_turn(0.4, -5)){
                        reset_drive_encoders();
                    }
                }
                else{
                    stage++;
                }

                break;

            case 6:

                if(auto_drive(0.7, 35)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 7:

                if(auto_drive(0.4, -5)){
                    reset_drive_encoders();
                    stage++;
                }

            case 8:

                if(auto_turn(0.4, -90)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 9:

                if(auto_drive(0.8, 48)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 10:

                if(auto_turn(0.4, -45)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 11:

                if(auto_drive(0.6, 50)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 12:

                if(auto_turn(0.4, 90)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 13:

                marker_servo.setPosition(drop_position);
                stage++;
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
