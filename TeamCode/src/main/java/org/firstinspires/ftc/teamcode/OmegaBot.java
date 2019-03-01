package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import edu.spa.ftclib.internal.Robot;
import edu.spa.ftclib.internal.activator.ServoActivator;

/**
 * Hardware mapping for Rover Ruckus 2018
 */

public class OmegaBot extends Robot {
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor extension;
    public DcMotor lift;
    public DcMotor intake;
    public DcMotor escalator;
    public Servo outtake;

    public Servo leftFlip;
    public Servo rightFlip;
    public Servo teamMarker;

    DcMotor.RunMode myRunMode = DcMotor.RunMode.RUN_TO_POSITION;
    public ServoActivator leftFlipActivator;
    public ServoActivator rightFlipActivator;
    public OmegaDriveTrain drivetrain;

    //3.77953-inch diameter wheels, 2 wheel rotations per 1 motor rotation; all Andymark NeveRest 40 motors for wheels (1120 ticks per rev for 1:1); 27 inch turning diameter
    private final double ticksPerInch = (1120 / 2.0) / (3.77953 * Math.PI);
    private final double ticksPerDegree = ticksPerInch * 27 * Math.PI / 360.0 * (2.0 / 3); //2.0 / 3 is random scale factor
    private final double turnTolerance = 2; //2 degrees error tolerance
    private final double driveTolerance = 8;
    Orientation lastAngles = new Orientation();
    BNO055IMU imu;
    OmegaPID turnPID;
    OmegaPID drivePID;
    double globalAngle, power = .30, correction;

    private double MOVE_CORRECTION_ADDENDUM = 0;
    private double AUTO_GOLD_RADIUS = 110;

    OmegaBot(Telemetry telemetry, HardwareMap hardwareMap) {
        super(telemetry, hardwareMap);

        frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");
        extension = hardwareMap.get(DcMotor.class, "extension");
        intake = hardwareMap.get(DcMotor.class, "intake");
        escalator = hardwareMap.get(DcMotor.class, "escalator");
        outtake = hardwareMap.get(Servo.class, "outtake");

        lift = hardwareMap.get(DcMotor.class, "lift");
        leftFlip = hardwareMap.get(Servo.class, "left_flip");
        rightFlip = hardwareMap.get(Servo.class, "right_flip");
        teamMarker = hardwareMap.get(Servo.class, "team_marker");
        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu1".
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu = hardwareMap.get(BNO055IMU.class, "imu1");

        imu.initialize(parameters);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        escalator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        frontRight.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.FORWARD);
        extension.setDirection(DcMotorSimple.Direction.FORWARD);

        teamMarker.setPosition(0); //0 is retracted; 1 is extended
        leftFlip.setPosition(0);
        rightFlip.setPosition(1);
        outtake.setPosition(0.95);

        intake.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        lift.setPower(0);

        drivetrain = new OmegaDriveTrain(frontLeft, frontRight, backLeft, backRight);
        drivetrain.setRunMode(myRunMode);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(myRunMode);
        turnPID = new OmegaPID(0.25, 0.00008, 0.5, turnTolerance); //0.015, 0.00008, 0.05 work for robotSpeed = 0.6. now tuning for 1.0
        drivePID = new OmegaPID(0.2, 0.0001, 0.4, driveTolerance);//.25, .0001, .08 has some jitters
    }//.25,.00008,.5


    public void move(double inches, double velocity) {
        double target = ticksPerInch * inches;
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (Math.abs(drivetrain.getAvgEncoderValueOfFrontWheels() - target) > 50) {
            drivetrain.setVelocity(velocity * inches/(Math.abs(inches)));
        }
        drivetrain.setVelocity(0);
        drivetrain.setRunMode(originalMode);
    }

    public void movePID(double inches, double velocity) {
        double target = ticksPerInch * inches + drivetrain.getAvgEncoderValueOfFrontWheels();
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int count = 0;
        while (Math.abs(drivetrain.getAvgEncoderValueOfFrontWheels() - target) > driveTolerance) {
            drivetrain.setVelocity(drivePID.calculatePower(drivetrain.getAvgEncoderValueOfFrontWheels(), target, -velocity, velocity));
            telemetry.addData("Count", count);
            telemetry.update();
        }
        extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extension.setTargetPosition(0);
        drivetrain.setVelocity(0);
        drivetrain.setRunMode(originalMode);
    }

    //This method makes the robot turn counterclockwise
    public void turn(double degrees, double velocity) {
        degrees = -degrees;                             //quickfix to make Nidhir's method turn robot counterclockwise with positive arg passed to degrees
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode

        //Resets encoder values
        drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Sets target position; left motor moves forward while right motor moves backward
        frontLeft.setTargetPosition((int) (ticksPerDegree * degrees));
        backLeft.setTargetPosition((int) (ticksPerDegree * degrees));
        frontRight.setTargetPosition(-1 * (int) (ticksPerDegree * degrees));
        backRight.setTargetPosition(-1 * (int) (ticksPerDegree * degrees));

        //Run to position
        drivetrain.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        drivetrain.setVelocity(velocity);

        //While the motors are still running, no other code will run
        while (drivetrain.isPositioning()) {
            telemetry.update();
        }

        drivetrain.setVelocity(0);

        drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        drivetrain.setRunMode(originalMode);
    }

    /*
     * This method makes the robot turn counterclockwise based on gyro values
     * Velocity is always positive. Set neg degrees for clockwise turn
     */
    public void turnUsingGyro(double degrees, double velocity) {
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double targetHeading = getAngle() + degrees;
        if (degrees > 0) {
            while (targetHeading - getAngle() > turnTolerance) {
                frontLeft.setPower(-velocity);
                backLeft.setPower(-velocity);
                frontRight.setPower(velocity);
                backRight.setPower(velocity);
            }
        } else {
            while (getAngle() - targetHeading > turnTolerance) {
                frontLeft.setPower(velocity);
                backLeft.setPower(velocity);
                frontRight.setPower(-velocity);
                backRight.setPower(-velocity);
            }
        }
        drivetrain.setVelocity(0);
        drivetrain.setRunMode(originalMode);
    }

    /**
     * This method makes the robot turn counterclockwise based on gyro values and PID
     * Velocity is always positive. Set neg degrees for clockwise turn
     *
     * @param degrees  desired angle in deg
     * @param velocity max velocity
     */
    public void turnUsingPID(double degrees, double velocity) {
        DcMotor.RunMode original = frontLeft.getMode(); //assume all drive motors r the same runmode
        drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double max = velocity;
        double targetHeading = getAngle() + degrees;
        int count = 0;
        while (Math.abs(targetHeading - getAngle()) > turnTolerance) {
            velocity = turnPID.calculatePower(getAngle(), targetHeading, -max, max);
            telemetry.addData("Count", count);
            telemetry.addData("Calculated power", turnPID.getDiagnosticCalculatedPower());
            telemetry.addData("PID power", velocity);
            telemetry.update();
            frontLeft.setPower(-velocity);
            backLeft.setPower(-velocity);
            frontRight.setPower(velocity);
            backRight.setPower(velocity);
            count++;
        }
        drivetrain.setVelocity(0);
        drivetrain.setRunMode(original);
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
        DcMotor.RunMode original = frontLeft.getMode(); //assume all drive motors r the same runmode
        drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        double max = 12.0 * velocity;
        double targetHeading = getAngle() + degrees;
        int count = 0;
        while (Math.abs(targetHeading - getAngle()) > turnTolerance) {
            velocity = (turnPID.calculatePower(getAngle(), targetHeading, -max, max) / 12.0); //turnPID.calculatePower() used here will return a voltage
            telemetry.addData("Count", count);
            telemetry.addData("Calculated velocity [-1.0, 1/0]", turnPID.getDiagnosticCalculatedPower() / 12.0);
            telemetry.addData("PID power [-1.0, 1.0]", velocity);
            telemetry.update();
            frontLeft.setPower(-velocity);
            backLeft.setPower(-velocity);
            frontRight.setPower(velocity);
            backRight.setPower(velocity);
            count++;
        }
        drivetrain.setVelocity(0);
        drivetrain.setRunMode(original);
    }


    /**
     * Resets the cumulative angle tracking to zero.
     */
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }

    /**
     * Get current cumulative angle rotation from last reset.
     *
     * @return Angle in degrees. + = left, - = right.
     */
    public double getAngle() {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }

//    /**
//     * Get the real heading {0, 360}
//     *
//     * @return the heading of the robot {0, 360}
//     */
//    public double getAngleReadable() {
//        double a = getAngle() % 360;
//        if (a < 0) {
//            a = 360 + a;
//        }
//        return a;
//    }
//
//    /**
//     * See if we are moving in a straight line and if not return a power correction value.
//     *
//     * @return Power adjustment, + is adjust left - is adjust right.
//     */
//    public double checkDirection() {
//        // The gain value determines how sensitive the correction is to direction changes.
//        // You will have to experiment with your robot to get small smooth direction changes
//        // to stay on a straight line.
//        double correction, angle, gain = .10;
//
//        angle = getAngle();
//
//        if (angle == 0)
//            correction = 0;             // no adjustment.
//        else
//            correction = -angle;        // reverse sign of angle for correction.
//
//        correction = correction * gain;
//
//        return correction;
//    }

    public double getMOVE_CORRECTION_ADDENDUM() {
        return MOVE_CORRECTION_ADDENDUM;
    }

    public double getAUTO_GOLD_RADIUS() {
        return AUTO_GOLD_RADIUS;
    }

    public double getTicksPerInch() {
        return ticksPerInch;
    }

    public double getTurnTolerance() {
        return turnTolerance;
    }
}
