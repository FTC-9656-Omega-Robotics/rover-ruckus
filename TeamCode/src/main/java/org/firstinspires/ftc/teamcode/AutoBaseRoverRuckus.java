package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Coding paradigm is that movement methods that involve loops go here, generic methods otherwise go in OmegaBot
 */
public abstract class AutoBaseRoverRuckus extends LinearOpMode {
    private GoldAlignDetector detector;
    private ElapsedTime runtime = new ElapsedTime();
    public double robotSpeed = 0.8;
    int liftMaxHeight = 3500;
    OmegaBot robot;

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
        robot.leftFlip.setPosition(0.65);
        robot.rightFlip.setPosition(0.35);
        runtime.reset();
        int x = 0;
        while (opModeIsActive() && runtime.seconds() < 2) {
            x = (int) detector.getXPosition();
        }
        detector.disable();
        robot.leftFlip.setPosition(0.15);
        robot.rightFlip.setPosition(0.85);
        runtime.reset();
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

    public void move(double inches, double velocity) {
        DcMotor.RunMode originalMode = robot.frontLeft.getMode(); //Assume that all wheels have the same runmode
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.drivetrain.setTargetPosition(robot.getTicksPerInch() * inches);
        robot.drivetrain.setVelocity(velocity);
        int count = 0;
        while (opModeIsActive() && (count < 600 && (robot.frontLeft.isBusy() || robot.frontRight.isBusy() || robot.backLeft.isBusy() || robot.backRight.isBusy()))) {
            telemetry.addData("Drivetrain is positioning. Count:", count);
            telemetry.update();
            count++;
        }

        robot.drivetrain.setVelocity(0);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(originalMode);
    }

    public void move(OmegaBot robot, double inches, double velocity) {
        DcMotor.RunMode originalMode = robot.frontLeft.getMode(); //Assume that all wheels have the same runmode
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.drivetrain.setTargetPosition(robot.getTicksPerInch() * inches);
        robot.drivetrain.setVelocity(velocity);
        int count = 0;
        while (opModeIsActive() && (count < 600 && (robot.frontLeft.isBusy() || robot.frontRight.isBusy() || robot.backLeft.isBusy() || robot.backRight.isBusy()))) {
            telemetry.addData("Drivetrain is positioning. Count:", count);
            telemetry.update();
            count++;
        }
        robot.drivetrain.setVelocity(0);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(originalMode);
    }

    /**
     * This method makes the robot turn counterclockwise based on gyro values and PID
     * Velocity is always positive. Set neg degrees for clockwise turn
     * pwr in setPower(pwr) is a fraction [-1.0, 1.0] of 12V
     *
     * @param degrees  desired angle in deg
     * @param velocity max velocity
     */
    public void turnUsingPIDVoltage(double degrees, double velocity) {
        DcMotor.RunMode original = robot.frontLeft.getMode(); //assume all drive motors r the same runmode
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double max = 12.0 * velocity;
        double targetHeading = robot.getAngle() + degrees;
        int count = 0;
        while (opModeIsActive() && Math.abs(targetHeading - robot.getAngle()) > robot.getErrorTolerance()) {
            velocity = (robot.pid.calculatePower(robot.getAngle(), targetHeading, -max, max) / 12.0); //pid.calculatePower() used here will return a voltage
            telemetry.addData("Count", count);
            telemetry.addData("Calculated velocity [-1.0, 1/0]", robot.pid.getDiagnosticCalculatedPower() / 12.0);
            telemetry.addData("PID power [-1.0, 1.0]", velocity);
            telemetry.update();
            robot.frontLeft.setPower(-velocity);
            robot.backLeft.setPower(-velocity);
            robot.frontRight.setPower(velocity);
            robot.backRight.setPower(velocity);
            count++;
        }
        robot.drivetrain.setVelocity(0);
        robot.setDrivetrainToMode(original);
    }
    /**
     * This method makes the robot turn counterclockwise based on gyro values and PID
     * Velocity is always positive. Set neg degrees for clockwise turn
     * pwr in setPower(pwr) is a fraction [-1.0, 1.0] of 12V
     *
     * @param robot - the OmegaBot that will perform these actions
     * @param degrees  desired angle in deg
     * @param velocity max velocity
     */
    public void turnUsingPIDVoltage(OmegaBot robot, double degrees, double velocity) {
        DcMotor.RunMode original = robot.frontLeft.getMode(); //assume all drive motors r the same runmode
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double max = 12.0 * velocity;
        double targetHeading = robot.getAngle() + degrees;
        int count = 0;
        while (opModeIsActive() && Math.abs(targetHeading - robot.getAngle()) > robot.getErrorTolerance()) {
            velocity = (robot.pid.calculatePower(robot.getAngle(), targetHeading, -max, max) / 12.0); //pid.calculatePower() used here will return a voltage
            telemetry.addData("Count", count);
            telemetry.addData("Calculated velocity [-1.0, 1/0]", robot.pid.getDiagnosticCalculatedPower() / 12.0);
            telemetry.addData("PID power [-1.0, 1.0]", velocity);
            telemetry.update();
            robot.frontLeft.setPower(-velocity);
            robot.backLeft.setPower(-velocity);
            robot.frontRight.setPower(velocity);
            robot.backRight.setPower(velocity);
            count++;
        }
        robot.drivetrain.setVelocity(0);
        robot.setDrivetrainToMode(original);
    }

    abstract public void goldLeft();

    abstract public void goldCenter();

    abstract public void goldRight();

}
