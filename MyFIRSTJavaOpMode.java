// Enabling Auto Import
// The auto import feature of Android Studio is a convenient function that helps save time
// as you write your op mode. If you would like to enable this feature, select the 
// Editor->General->Auto Import item from the Android Studio Settings screen. 
// This will display the editor's auto import settings.  Check the "Add unambiguous imports
// on the fly" so that Android Studio will automatically add the required import statements 
// for classes that you would like to use in your op mode.

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//At the start of the op mode there is an annotation that occurs before the class definition. 
//This annotation states that this is a tele-operated (i.e., driver controlled) op mode:
@TeleOp(name = "Sensor: First Tutor", group = "_Sensor")
//@Disabled

// If you wanted to change this op mode to an autonomous op mode, you would replace the “@TeleOp”
// with an “@Autonomous” annotation instead.  You can see from the sample code that an op mode
// is defined as a Java class. In this example, the op mode name is called “MyFIRSTJavaOpMode”
// and it inherits characteristics from the LinearOpMode class.

public class MyFIRSTJavaOpMode extends LinearOpMode {
//    private Gyroscope imu;
//    private DcMotor motorTest;
//    private DigitalChannel digitalTouch;
    private DistanceSensor sensorColorRange;
//    private Servo servoTest;

// Next, there is an overridden method called runOpMode.
// Every op mode of type LinearOpMode must implement this method.
// This method gets called when a user selects and runs the op mode.
    @Override
    public void runOpMode() {
// At the start of the runOpMode method, the op mode uses an object named hardwareMap to get
// references to the hardware devices that are listed in the Robot Controller’s configuration file:
//        imu = hardwareMap.get(Gyroscope.class, "imu");
//        motorTest = hardwareMap.get(DcMotor.class, "motorTest");
//        digitalTouch = hardwareMap.get(DigitalChannel.class, "digitalTouch");
//        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
        sensorColorRange = hardwareMap.get(DistanceSensor.class, "sensorColorRange");
//        servoTest = hardwareMap.get(Servo.class, "servoTest");

// The hardwareMap object is available to use in the runOpMode method. It is an object of type
// HardwareMap class.  Note that when you attempt to retrieve a reference to a specific device
// in your op mode, the name that you specify as the second argument of the HardwareMap.get method
// must match the name used to define the device in your configuration file.
// For example, if you created a configuration file that had a DC motor named “motorTest”,
// then you must use this same name (it is case sensitive) to retrieve this motor from
// the hardwareMap object. If the names do not match, the op mode will throw an exception
// indicating that it cannot find the device.

// In the next few statements of the example, the op mode prompts the user to push the start button
// to continue. It uses another object that is available in the runOpMode method. This object is
// called telemetry and the op mode uses the addData method to add a message to be sent to the
// Driver Station. The op mode then calls the update method to send the message to
// the Driver Station. Then it calls the waitForStart method, to wait until the user pushes the
// start button on the driver station to begin the op mode run.

        telemetry.addData("Status:", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

// Note that all linear op modes should have a waitForStart statement to ensure that the robot
// will not begin executing the op mode until the driver pushes the start button.
// After a start command has been received, the op mode enters a while loop and keeps iterating
// in this loop until the op mode is no longer active (i.e., until the user pushes the stop button
// on the Driver Station):
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
// As the op mode iterates in the while loop, it will continue to send telemetry messages with
// the index of “Status” and the message of “Running” to be displayed on the Driver Station.

//            telemetry.addData("Servo Position", servoTest.getPosition());
//            telemetry.addData("Target Power", tgtPower);
//            telemetry.addData("Motor Power", motorTest.getPower());

// Note that if the distance reads “NaN” (short for “Not a Number”) it probably means that your
// sensor is too far from the target (zero reflection). Also note that the sensor saturates
// at around 5 cm.
            telemetry.addData("Distance (cm):", sensorColorRange.getDistance(DistanceUnit.CM));
            telemetry.addData("Status       :", "Running");
            telemetry.update();

        }
    }
}