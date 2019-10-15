package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class AutoNoDetector extends Base {
    private int stage = 0;

    @Override
    public void init(){
        super.init();
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop(){
        super.loop();

        switch(stage){

            case 0:
                if(Math.abs(get_climb_enc())>4000){
                    climb(0);
                    stage++;
                }
                else{
                    climb(1);
                }

                break;

            case 1:
                if(auto_drive(0.8, 57)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 2:
                if(auto_turn(0.4, 90)){
                    reset_drive_encoders();
                    stage++;
                }

                break;

            case 3:

                marker_servo.setPosition(drop_position);
                stage++;
                break;

            default:

                break;

        }

    }

}
