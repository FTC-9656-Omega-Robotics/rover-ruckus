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
    public double robotSpeed = 0.45;
    int liftMaxHeight = 9600;
    OmegaBot robot;
    OmegaCamera camLight;
    OmegaPID drivePID;

    public void runOpMode() {
        robot = new OmegaBot(telemetry, hardwareMap);
        drivePID = robot.drivePID;
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //GYRO SETUP
        runtime.reset();
        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !robot.imu.isGyroCalibrated() && runtime.seconds() < 2) {
            telemetry.addLine("Gyro is calibrating");
            telemetry.update();
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", robot.imu.getCalibrationStatus().toString());
        telemetry.update();

        waitForStart();
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

        robot.leftFlip.setPosition(0.85);
        robot.rightFlip.setPosition(0.15);
        int x = 0;
        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 3.5) {
            x = (int) detector.getXPosition();
        }
        detector.disable();
        robot.leftFlip.setPosition(0.15);
        robot.rightFlip.setPosition(0.85);
        runtime.reset();
        robot.lift.setTargetPosition(-liftMaxHeight);
        robot.lift.setPower(-1);
        runtime.reset();
        while (opModeIsActive() && robot.lift.isBusy() && robot.lift.getCurrentPosition() < liftMaxHeight && runtime.seconds() < 5) {
            telemetry.addData("lift", robot.lift.getCurrentPosition());
            telemetry.update();
        }
        robot.lift.setPower(0);
        movePID(0.6 * Math.sqrt(72), robotSpeed);
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
        robot.drivetrain.setRunMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        robot.drivetrain.setRunMode((DcMotor.RunMode.RUN_TO_POSITION));
        robot.drivetrain.setTargetPosition((robot.getTicksPerInch() * inches));
        robot.drivetrain.setVelocity(velocity);
        int count = 0;
        while (opModeIsActive() && (count < 600 && (robot.frontLeft.isBusy() || robot.frontRight.isBusy() || robot.backLeft.isBusy() || robot.backRight.isBusy()))) {
            telemetry.addData("Drivetrain is positioning. Count:", count);
            telemetry.update();
            count++;
        }

        robot.drivetrain.setVelocity(0);
        robot.drivetrain.setRunMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
        robot.drivetrain.setRunMode((originalMode));
    }

    public void movePID(double inches, double velocity) {
        double target = robot.ticksPerInch * inches + robot.drivetrain.getAvgEncoderValueOfFrontWheels();
        DcMotor.RunMode originalMode = robot.frontLeft.getMode(); //Assume that all wheels have the same runmode
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int count = 0;
        ElapsedTime runtime = new ElapsedTime();
        while (opModeIsActive() && runtime.seconds() < robot.driveTimeLimitPer1Foot * inches / 12.0) {
            robot.drivetrain.setVelocity(robot.drivePID.calculatePower(robot.drivetrain.getAvgEncoderValueOfFrontWheels(), target, -velocity, velocity));
            telemetry.addData("Count", count);
            telemetry.update();
        }
        robot.drivetrain.setVelocity(0);
        robot.drivetrain.setRunMode(originalMode);
    }

    public void spline(double angle, double distance, double endHeading, double velocity) {
        double robotRadius = 8.125;
        turnUsingPIDVoltage(endHeading - 2 * angle, velocity);
        double radius = distance / (2 * Math.sin(angle));
        spline(angle * Math.PI * (radius - robotRadius) / 90, angle * Math.PI * (radius + robotRadius) / 90, velocity);
    }

    public void spline(double leftDistance, double rightDistance, double velocity) {
        double leftTarget = robot.ticksPerInch * leftDistance + robot.drivetrain.getAvgEncoderValueOfLeftWheels();
        double rightTarget = robot.ticksPerInch * rightDistance + robot.drivetrain.getAvgEncoderValueOfRightWheels();
        DcMotor.RunMode originalMode = robot.frontLeft.getMode(); //Assume that all wheels have the same runmode
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int count = 0;
        ElapsedTime runtime = new ElapsedTime();
        while (opModeIsActive() && runtime.seconds() < robot.turnTimeLimit) {
            robot.frontLeft.setPower(drivePID.calculatePower(robot.drivetrain.getAvgEncoderValueOfLeftWheels(), leftTarget, -velocity, velocity));
            robot.backLeft.setPower(drivePID.calculatePower(robot.drivetrain.getAvgEncoderValueOfLeftWheels(), leftTarget, -velocity, velocity));
            robot.frontRight.setPower(drivePID.calculatePower(robot.drivetrain.getAvgEncoderValueOfRightWheels(), rightTarget, -velocity, velocity));
            robot.backRight.setPower(drivePID.calculatePower(robot.drivetrain.getAvgEncoderValueOfRightWheels(), rightTarget, -velocity, velocity));
            telemetry.addData("Count", count);
            telemetry.update();
        }
        robot.drivetrain.setRunMode(originalMode);

    }


    public void move(OmegaBot robot, double inches, double velocity) {
        DcMotor.RunMode originalMode = robot.frontLeft.getMode(); //Assume that all wheels have the same runmode
        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.drivetrain.setTargetPosition(robot.getTicksPerInch() * inches);
        robot.drivetrain.setVelocity(velocity);
        int count = 0;
        while (opModeIsActive() && (count < 600 && (robot.frontLeft.isBusy() || robot.frontRight.isBusy() || robot.backLeft.isBusy() || robot.backRight.isBusy()))) {
            telemetry.addData("Drivetrain is positioning. Count:", count);
            telemetry.update();
            count++;
        }
        robot.drivetrain.setVelocity(0);
        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.drivetrain.setRunMode(originalMode);
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
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double max = 12.0 * velocity;
        double targetHeading = robot.getAngle() + degrees;
        int count = 0;
        ElapsedTime runtime = new ElapsedTime();
        while (opModeIsActive() && runtime.seconds() < robot.turnTimeLimit) {
            velocity = (robot.turnPID.calculatePower(robot.getAngle(), targetHeading, -max, max) / 12.0); //turnPID.calculatePower() used here will return a voltage
            telemetry.addData("Count", count);
            telemetry.addData("Calculated velocity [-1.0, 1/0]", robot.turnPID.getDiagnosticCalculatedPower() / 12.0);
            telemetry.addData("PID power [-1.0, 1.0]", velocity);
            telemetry.update();
            robot.frontLeft.setPower(-velocity);
            robot.backLeft.setPower(-velocity);
            robot.frontRight.setPower(velocity);
            robot.backRight.setPower(velocity);
            count++;
        }
        robot.drivetrain.setVelocity(0);
        robot.drivetrain.setRunMode(original);
    }

    /**
     * This method makes the robot turn counterclockwise based on gyro values and PID
     * Velocity is always positive. Set neg degrees for clockwise turn
     * pwr in setPower(pwr) is a fraction [-1.0, 1.0] of 12V
     *
     * @param robot    - the OmegaBot that will perform these actions
     * @param degrees  desired angle in deg
     * @param velocity max velocity
     */
    public void turnUsingPIDVoltage(OmegaBot robot, double degrees, double velocity) {
        DcMotor.RunMode original = robot.frontLeft.getMode(); //assume all drive motors r the same runmode
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double max = 12.0 * velocity;
        double targetHeading = robot.getAngle() + degrees;
        int count = 0;
        ElapsedTime runtime = new ElapsedTime();
        while (opModeIsActive() && runtime.seconds() < robot.turnTimeLimit) {
            velocity = (robot.turnPID.calculatePower(robot.getAngle(), targetHeading, -max, max) / 12.0); //turnPID.calculatePower() used here will return a voltage
            telemetry.addData("Count", count);
            telemetry.addData("Calculated velocity [-1.0, 1/0]", robot.turnPID.getDiagnosticCalculatedPower() / 12.0);
            telemetry.addData("PID power [-1.0, 1.0]", velocity);
            telemetry.update();
            robot.frontLeft.setPower(-velocity);
            robot.backLeft.setPower(-velocity);
            robot.frontRight.setPower(velocity);
            robot.backRight.setPower(velocity);
            count++;
        }
        robot.drivetrain.setVelocity(0);
        robot.drivetrain.setRunMode(original);
    }

    abstract public void goldLeft();

    abstract public void goldCenter();

    abstract public void goldRight();

}
