package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutoCrater", group = "Testers")
//@Disabled

public class AutoCrater extends LinearOpMode {


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
        robot.move(0.8 * Math.sqrt(72), robotSpeed);
        //Choose corresponding path
        //radius of 30 around the central values
        if (Math.abs(x-240) < 30) {
            goldCenter();
        } else if (Math.abs(x-440) > 30) {
            goldRight();
        } else {
            goldLeft();
        }
        finishPath();
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values {null--none, 100, 315}
    public void goldLeft() {
        robot.turn(49.684, robotSpeed);
        robot.move(Math.sqrt(1548) + 3, robotSpeed);
        robot.move(- (Math.sqrt(1548) + 3), robotSpeed);
        robot.turn(22.620, robotSpeed);
        robot.move(Math.sqrt(1332) + 3, robotSpeed);
    }

    public void goldCenter() {
        robot.move(3 * Math.sqrt(72) + 3, robotSpeed);
        robot.move(-(3  * Math.sqrt(72) + 3), robotSpeed);
        robot.turn(49.684 + 22.620, robotSpeed);
        robot.move(Math.sqrt(1332) + 3, robotSpeed);
    }

    public void goldRight() {
        robot.turn(-49.684, robotSpeed);
        robot.move(Math.sqrt(1548) + 3, robotSpeed);
        robot.move(-(Math.sqrt(1548) + 3), robotSpeed);
        robot.turn(2 * 49.684 + 22.620, robotSpeed);
    }

    public void finishPath() {
        robot.move(Math.sqrt(3636) + 3, robotSpeed);
        robot.teamMarker.setPosition(0); // 0 is extended, 0.9 is withdrawn
        sleep(1000);
        robot.teamMarker.setPosition(0.9);
        robot.turn(-174.289, robotSpeed);
        robot.move(90 + 3, robotSpeed);
    }
}
