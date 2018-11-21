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
    public DcMotor intake;
    public DcMotor pivot;
    public DcMotor arm;
    public DcMotor lift;
//    public DcMotor pivot;
    //public DcMotor arm2;
    public Servo teamMarker;

    DcMotor.RunMode myRunMode = DcMotor.RunMode.RUN_TO_POSITION;
    public ServoActivator teamMarkerActivator;
    public TankDrivetrainFourWheels drivetrain;

    //4 inch wheels, 2 wheel rotations per 1 motor rotation; all Andymark NeveRest 40 motors for wheels (1120 ticks per rev for 1:1)
    private final double ticksPerInch = (1120 / 2) / (2 * Math.PI * 2);
    private final double ticksPerDegree = 22*1120/(8*360);//22*pi is the approximated turning circumference, multiplying that by ticks/inch , dividing by 360 degrees


    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    OmegaBot(Telemetry telemetry, HardwareMap hardwareMap) {
        super(telemetry, hardwareMap);

        frontLeft  = hardwareMap.get(DcMotor.class,"front_left");
        frontRight = hardwareMap.get(DcMotor.class,"front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");
        intake = hardwareMap.get(DcMotor.class, "intake");
        pivot = hardwareMap.get(DcMotor.class, "pivot");
        arm = hardwareMap.get(DcMotor.class, "arm");

        lift = hardwareMap.get(DcMotor.class, "lift");
        // arm1 = hardwareMap.get(DcMotor.class, "arm1");
        //arm2 = hardwareMap.get(DcMotor.class, "arm2");
        teamMarker = hardwareMap.get(Servo.class, "team_marker");

        frontLeft.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        frontRight.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors

        backLeft.setDirection(DcMotor.Direction.REVERSE); //
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        lift.setPower(0);
//        arm1.setPower(0);
        //arm2.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        setDrivetrainToMode(myRunMode);
        lift.setMode(myRunMode);
//        arm1.setMode(myRunMode);
        //arm2.setMode(myRunMode);

        drivetrain = new TankDrivetrainFourWheels(frontLeft, frontRight, backLeft, backRight);

        teamMarkerActivator = new ServoActivator(teamMarker, 0.9, 0.5);

        teamMarkerActivator.deactivate();
    }

    /**
     * Move the drivetrain forward by 100 units.
     */
    public void moveForward100() {
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivetrain.setTargetPosition(100);
        drivetrain.setVelocity(0.5);
        while(drivetrain.isPositioning()) {
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
        while(drivetrain.isPositioning()) {
        }
        drivetrain.setVelocity(0);
        setDrivetrainToMode(originalMode); //revert to original mode once done
    }

    public void move(int inches, double velocity) {
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode
        setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        drivetrain.setTargetPosition(ticksPerInch * inches);
        drivetrain.setVelocity(velocity);
        while(drivetrain.isPositioning()) {
            telemetry.update();
        }
        drivetrain.setVelocity(0);
        setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDrivetrainToMode(originalMode);
    }

    //This method makes the robot turn right
    public void turn(int degrees, double velocity){
        DcMotor.RunMode originalMode = frontLeft.getMode(); //Assume that all wheels have the same runmode

        //Resets encoder values
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Sets target position; left motor moves forward while right motor moves backward
        frontLeft.setTargetPosition((int)(ticksPerDegree*degrees+.5));
        backLeft.setTargetPosition((int)(ticksPerDegree*degrees+.5));
        frontRight.setTargetPosition(-1*(int)(ticksPerDegree*degrees+.5));
        backRight.setTargetPosition(-1*(int)(ticksPerDegree*degrees+.5));

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
        while(frontRight.isBusy()&&frontLeft.isBusy()&&backRight.isBusy()&&backLeft.isBusy())
        {
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
}
