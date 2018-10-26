package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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
    public DcMotor rack;
    public DcMotor arm1;
    //public DcMotor arm2;
    public Servo teamMarker;

    DcMotor.RunMode myRunMode = DcMotor.RunMode.RUN_USING_ENCODER;
    public ServoActivator teamMarkerActivator;
    public TankDrivetrainFourWheels drivetrain;

    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    OmegaBot(Telemetry telemetry, HardwareMap hardwareMap) {
        super(telemetry, hardwareMap);

        frontLeft  = hardwareMap.get(DcMotor.class,"front_left");
        frontRight = hardwareMap.get(DcMotor.class,"front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");
        rack = hardwareMap.get(DcMotor.class, "rack");
        arm1 = hardwareMap.get(DcMotor.class, "arm1");
        //arm2 = hardwareMap.get(DcMotor.class, "arm2");
        teamMarker = hardwareMap.get(Servo.class, "team_marker");

        frontLeft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        frontRight.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors

        backLeft.setDirection(DcMotor.Direction.FORWARD); //gobuilda motors are used in the back
        backRight.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        rack.setPower(0);
        arm1.setPower(0);
        //arm2.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeft.setMode(myRunMode);
        frontRight.setMode(myRunMode);
        backLeft.setMode(myRunMode);
        backRight.setMode(myRunMode);
        rack.setMode(myRunMode);
        arm1.setMode(myRunMode);
        //arm2.setMode(myRunMode);

        drivetrain = new TankDrivetrainFourWheels(frontLeft, frontRight, backLeft, backRight);

        teamMarkerActivator = new ServoActivator(teamMarker, 0.9, 0.5);

        teamMarkerActivator.deactivate();
    }

    public void moveForward100() {
        drivetrain.setTargetPosition(100);
    }
}
