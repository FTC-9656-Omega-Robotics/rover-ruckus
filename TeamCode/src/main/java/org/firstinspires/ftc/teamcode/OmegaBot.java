package org.firstinspires.ftc.teamcode;

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
    public DcMotor arm;
    public DcMotor lift;

    public CRServo intake;
    public Servo leftFlip;
    public Servo rightFlip;
    public Servo teamMarker;

    DcMotor.RunMode myRunMode = DcMotor.RunMode.RUN_TO_POSITION;
    public ServoActivator leftFlipActivator;
    public ServoActivator rightFlipActivator;
    public TankDrivetrainFourWheels drivetrain;

    //4 inch wheels, 2 wheel rotations per 1 motor rotation; all Andymark NeveRest 40 motors for wheels (1120 ticks per rev for 1:1)
    private final double ticksPerInch = (1120 / 2) / (2 * Math.PI * 2);
    private final double ticksPerDegree = 24.7 * 1120 / (8 * 360);//22*pi is the approximated turning circumference, multiplying that by ticks/inch , dividing by 360 degrees
    private final double errorTolerance = 3; //3 degrees error tolerance
    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;

    private double MOVE_CORRECTION_ADDENDUM = 0.1;
    private double AUTO_GOLD_RADIUS = 99;

    OmegaBot(Telemetry telemetry, HardwareMap hardwareMap) {
        super(telemetry, hardwareMap);

        frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");
        extension = hardwareMap.get(DcMotor.class, "extension");
        arm = hardwareMap.get(DcMotor.class, "arm");

        lift = hardwareMap.get(DcMotor.class, "lift");
        intake = hardwareMap.get(CRServo.class, "intake");
        leftFlip = hardwareMap.get(Servo.class, "left_flip");
        rightFlip = hardwareMap.get(Servo.class, "right_flip");
        teamMarker = hardwareMap.get(Servo.class, "team_marker");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        frontRight.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.FORWARD);
        extension.setDirection(DcMotorSimple.Direction.FORWARD);

        teamMarker.setPosition(0.9);
        leftFlip.setPosition(0);
        rightFlip.setPosition(1);

        intake.setPower(0);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        lift.setPower(0);

        drivetrain = new TankDrivetrainFourWheels(frontLeft, frontRight, backLeft, backRight);
        setDrivetrainToMode(myRunMode);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(myRunMode);

    }

    /**
     * Move the drivetrain forward by 100 units.
     */
    public void moveForward100() {
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivetrain.setTargetPosition(100);
        drivetrain.setVelocity(0.5);
        while (drivetrain.isPositioning()) {
        }
        drivetrain.setVelocity(0);
        setDrivetrainToMode(originalMode); //revert to original mode once done
    }

    /**
     * Move the drivetrain forward by 100 units.
     */
    public void moveBackward100() {
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivetrain.setTargetPosition(-100);
        drivetrain.setVelocity(-0.5);
        while (drivetrain.isPositioning()) {
        }
        drivetrain.setVelocity(0);
        setDrivetrainToMode(originalMode); //revert to original mode once done
    }

    public void move(double inches, double velocity) {
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivetrain.setTargetPosition(ticksPerInch * inches);
        drivetrain.setVelocity(velocity);
        while (drivetrain.isPositioning()) {
            telemetry.update();
        }
        drivetrain.setVelocity(0);
        setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDrivetrainToMode(originalMode);
    }

    //This method makes the robot turn counterclockwise
    public void turn(double degrees, double velocity) {
        degrees = -degrees;                             //quickfix to make Nidhir's method turn robot counterclockwise with positive arg passed to degrees
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode

        //Resets encoder values
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Sets target position; left motor moves forward while right motor moves backward
        frontLeft.setTargetPosition((int) (ticksPerDegree * degrees));
        backLeft.setTargetPosition((int) (ticksPerDegree * degrees));
        frontRight.setTargetPosition(-1 * (int) (ticksPerDegree * degrees));
        backRight.setTargetPosition(-1 * (int) (ticksPerDegree * degrees));

        //Run to position
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(velocity);
        backLeft.setPower(velocity);
        frontRight.setPower(velocity);
        backRight.setPower(velocity);

        //While the motors are still running, no other code will run
        while (frontRight.isBusy() && frontLeft.isBusy() && backRight.isBusy() && backLeft.isBusy()) {
            telemetry.update();
        }

        //Stops the turn
        //frontLeft.setPower(0);
        //backLeft.setPower(0);
        //frontRight.setPower(0);
        //backRight.setPower(0);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(originalMode);
        backLeft.setMode(originalMode);
        frontRight.setMode(originalMode);
        backRight.setMode(originalMode);
    }
//
//    /*
//     * This method makes the robot turn counterclockwise based on gyro values
//     * Velocity is always positive. Set neg degrees for clockwise turn
//     */
//    public void turnUsingGyro(double degrees, double velocity) {
//        double errorTolerance = 3;
//        double targetHeading = getAngle() + degrees;
//        if (degrees > 0) {
//            while (targetHeading - getAngle() > errorTolerance) {
//                frontLeft.setPower(-velocity);
//                backLeft.setPower(-velocity);
//                frontRight.setPower(velocity);
//                backRight.setPower(velocity);
//            }
//        } else {
//            while (getAngle() - targetHeading > errorTolerance) {
//                frontLeft.setPower(velocity);
//                backLeft.setPower(velocity);
//                frontRight.setPower(-velocity);
//                backRight.setPower(-velocity);
//            }
//        }
//        drivetrain.setVelocity(0);
//    }

//    /**
//     * This method makes the robot turn counterclockwise based on gyro values and PID
//     * Velocity is always positive. Set neg degrees for clockwise turn
//     *
//     * @param degrees  desired angle in deg
//     * @param velocity max velocity
//     */
//    public void turnUsingPID(double degrees, double velocity) {
//        double max = velocity;
//        double targetHeading = getAngle() + degrees;
//        int count = 0;
//        frontLeft.setPower(-velocity);
//        backLeft.setPower(-velocity);
//        frontRight.setPower(velocity);
//        backRight.setPower(velocity);
//        while (Math.abs(targetHeading - getAngle()) > errorTolerance) {
//            telemetry.addData("Run", count );
//            velocity = pid.calculatePower(getAngle(), targetHeading, -max, max);
//            telemetry.addData("Calculated PID power", velocity);
//            frontLeft.setPower(-velocity);
//            backLeft.setPower(-velocity);
//            frontRight.setPower(velocity);
//            backRight.setPower(velocity);
//            count++;
//        }
//        drivetrain.setVelocity(0);
//    }


    /**
     * Set all motors to a runmode
     *
     * @param - the run mode
     */
    public void setDrivetrainToMode(DcMotor.RunMode runMode) {
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backLeft.setMode(runMode);
        backRight.setMode(runMode);
    }

//    /**
//     * Resets the cumulative angle tracking to zero.
//     */
//    public void resetAngle() {
//        lastAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//
//        globalAngle = 0;
//    }

//    /**
//     * Get current cumulative angle rotation from last reset.
//     * @return Angle in degrees. + = left, - = right.
//     */
//    public double getAngle()
//    {
//        // We experimentally determined the Z axis is the axis we want to use for heading angle.
//        // We have to process the angle because the imu works in euler angles so the Z axis is
//        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
//        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.
//
//        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//
//        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
//
//        if (deltaAngle < -180)
//            deltaAngle += 360;
//        else if (deltaAngle > 180)
//            deltaAngle -= 360;
//
//        globalAngle += deltaAngle;
//
//        lastAngles = angles;
//
//        return globalAngle;
//    }

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
}
