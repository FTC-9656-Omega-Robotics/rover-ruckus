package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "AutoDepot", group = "Testers")
//@Disabled

public class AutoDepot extends LinearOpMode {


    private int initialPos, finalPos;
    private GoldAlignDetector detector;
    private ElapsedTime runtime = new ElapsedTime();
    private OmegaBot robot;
    private double robotSpeed = 0.5;

    @Override
    public void runOpMode() {
        robot = new OmegaBot(telemetry, hardwareMap);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);


//        //GYRO SETUP
//
//
//        // make sure the imu gyro is calibrated before continuing.
//        while (!isStopRequested() && !robot.imu.isGyroCalibrated())
//        {
//            sleep(50);
//            idle();
//        }
//
//        telemetry.addData("Mode", "waiting for start");
//        telemetry.addData("imu calib status", robot.imu.getCalibrationStatus().toString());
//        telemetry.update();

        // Set up detector
        detector = new GoldAlignDetector(); // Create detector
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance()); // Initialize it with the app context and camera
        detector.useDefaults(); // Set detector to use default settings

        // Optional tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005; //

        detector.ratioScorer.weight = 5; //
        detector.ratioScorer.perfectRatio = 1.0; // Ratio adjustment

        detector.enable(); // Start the detector!

        telemetry.addData("IsAligned", detector.getAligned()); // Is the bot aligned with the gold mineral?
        telemetry.addData("X Pos", detector.getXPosition()); // Gold X position.
        telemetry.addData("Initialization", "Complete");
        //Determine location of gold cube (threshold radius of 20) and
        waitForStart();
        int x = (int) detector.getXPosition();
        detector.disable();
        runtime.reset();

        int liftMaxHeight = 18100;
        robot.lift.setTargetPosition(-liftMaxHeight);
        robot.lift.setPower(-1);
        while (opModeIsActive() && robot.lift.isBusy() && robot.lift.getCurrentPosition() < liftMaxHeight) {
            telemetry.addData("lift", robot.lift.getCurrentPosition());
            telemetry.update();
        }
        robot.lift.setPower(0);
        robot.move(0.7 * Math.sqrt(72), robotSpeed);
        robot.lift.setTargetPosition(-1);

        //Choose corresponding path
        //radius of 40 around the central values
        if (Math.abs(x-240) < robot.getAUTO_GOLD_RADIUS()) {
            goldCenter();
            telemetry.addLine("goldCenter() selected.");
        } else if (Math.abs(x-440) < robot.getAUTO_GOLD_RADIUS()) {
            goldRight();
            telemetry.addLine("goldRight() selected.");
        } else {
            goldLeft();
            telemetry.addLine("goldLeft() selected");
        }
        //finishPath();
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values {null--none, 100, 315}
    public void goldLeft() {
        robot.turn(20.392, 0.3); // turn() method takes in a pos arg NOW to turn left
        robot.teamMarker.setPosition(0.9);
        robot.move(Math.sqrt(1140), .3);
        //then head to depot
        robot.turn(-52.061, robotSpeed);
        robot.move(Math.sqrt(1400) + robot.getMOVE_CORRECTION_ADDENDUM(), .3); //add addendum to compensate for real-life unexpected gap
        robot.teamMarker.setPosition(0); // 0 is extended, 0.9 is withdrawn
        sleep(1000);
        robot.move(-4,.3);
    }

    public void goldCenter() {
        robot.move(7 * Math.sqrt(72) + robot.getMOVE_CORRECTION_ADDENDUM()-7, robotSpeed);
        robot.teamMarker.setPosition(0); // 0 is extended, 0.9 is withdrawn
        sleep(1000);
        robot.teamMarker.setPosition(0.9);
        robot.move(-4,.3);
    }


    public void goldRight() {
        robot.turn(-40, .3);
        robot.move(Math.sqrt(1220)-10, 0.3);
        //then head to depot
        robot.turn(45.392, robotSpeed);
        robot.move(Math.sqrt(1440) + robot.getMOVE_CORRECTION_ADDENDUM(), 0.3);
        robot.teamMarker.setPosition(0); // 0 is extended, 0.9 is withdrawn
        robot.move(-4,.3);
        sleep(1000);
        //robot.turn(70.801, robotSpeed);
    }

    public void finishPath() {
        robot.move(102, robotSpeed);
    }
}
