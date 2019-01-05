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
    private double robotSpeed = 1;

    @Override
    public void runOpMode() {
        robot = new OmegaBot(telemetry, hardwareMap);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);


        //GYRO SETUP


        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !robot.imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", robot.imu.getCalibrationStatus().toString());
        telemetry.update();

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
        telemetry.update();

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
        robot.move(0.6 * Math.sqrt(72), robotSpeed);
        //Choose corresponding path
        if (Math.abs(x - 178) < robot.getAUTO_GOLD_RADIUS()) {
            telemetry.addLine("goldCenter() selected.");
            telemetry.update();
            goldCenter();
        } else if (Math.abs(x - 402) < robot.getAUTO_GOLD_RADIUS()) {
            telemetry.addLine("goldRight() selected.");
            telemetry.update();
            goldRight();
        } else {
            telemetry.addLine("goldLeft() selected");
            telemetry.update();
            goldLeft();
        }
        //finishPath();
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values {null--none, 100, 315}
    //give up on depositing marker into depot. just parking into crater bc of time constraints.
    public void goldLeft() {
        robot.turnUsingPIDVoltage(37, robotSpeed);
        robot.move(Math.sqrt(1548), robotSpeed);
//        robot.move(-(Math.sqrt(1548)), robotSpeed);
//        robot.turnUsingPIDVoltage(18.620, robotSpeed);
//        robot.move(Math.sqrt(1332), robotSpeed);
    }

    public void goldCenter() {
        robot.move(3 * Math.sqrt(72), robotSpeed);
//        robot.move(-(3 * Math.sqrt(72) - 2), robotSpeed);
//        robot.turnUsingPIDVoltage(52, robotSpeed);
//        robot.move(Math.sqrt(1332) + 5, robotSpeed); // addendum
//        robot.turnUsingPIDVoltage(78, robotSpeed); // a rough guess
    }

    public void goldRight() {
        robot.turnUsingPIDVoltage(-37, robotSpeed);
        robot.move(Math.sqrt(1548), robotSpeed);
//        robot.move(-(Math.sqrt(1548)), robotSpeed);
//        robot.turnUsingPIDVoltage(2 * 49.684 + 22.620, robotSpeed);

    }

    /**
     * Robot should be at leftmost point on the fan and oriented toward the depot before executing
     * this path
     */
    public void finishPath() {
        robot.move(Math.sqrt(3636), robotSpeed);
        robot.teamMarker.setPosition(0); // 0 is retracted, 0.9 is extended
        sleep(1000);
        robot.teamMarker.setPosition(0.9);
        robot.turn(-174.289, robotSpeed);
        robot.move(90, robotSpeed);
    }
}
