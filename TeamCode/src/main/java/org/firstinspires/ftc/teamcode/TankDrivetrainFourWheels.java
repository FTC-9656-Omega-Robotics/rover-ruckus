package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.spa.ftclib.internal.drivetrain.Drivetrain;
import edu.spa.ftclib.internal.drivetrain.Positionable;
import edu.spa.ftclib.internal.drivetrain.Rotatable;

/**
 * A simple drivetrain with four wheels.
 */

public class TankDrivetrainFourWheels extends Drivetrain implements Rotatable, Positionable {
    private double rotation = 0;

    private double targetPosition = 0;

    private DcMotor.RunMode[] runModes = new DcMotor.RunMode[2]; //We have two possible run modes.

    private double[] wheelTargetPositions = new double[2]; //Target encoder values for two front wheels

    public DcMotor frontLeft, frontRight, backLeft, backRight;

    /**
     * The constructor for the class.
     *
     * @param motors the array of motors that you give the constructor so that it can find the hardware
     */
    public TankDrivetrainFourWheels(DcMotor[] motors) { //motors are numbered clockwise starting with front left
        super(motors);
        frontLeft = motors[0];
        frontRight = motors[1];
        backRight = motors[2];
        backLeft = motors[3];
    }

    public TankDrivetrainFourWheels(DcMotor motor1, DcMotor motor2, DcMotor motor3, DcMotor motor4) {
        this(new DcMotor[]{motor1, motor2, motor3, motor4});
    }

    /**
     * @param rotation the velocity that you want to rotate the robot by.
     *                 Counterclockwise is positive and clockwise is negative.
     *                 Zero is if you don't want to rotate the robot.
     */
    @Override
    public void setRotation(double rotation) {
        this.rotation = rotation;
        updateMotorPowers();
    }

    /**
     * @return the rotation velocity the robot was given.
     */
    @Override
    public double getRotation() {
        return rotation;
    }

    /**
     * Calculates the motor powers. It combines the velocity you gave it and the rotation that you gave it
     *
     * @return the motorPowers array, which will then be sent to the motors to tell them how fast to move
     */
    protected double[] calculateMotorPowers() {
        double[] motorPowers = new double[4]; //create a new array to hold the motor powers
        motorPowers[0] = getVelocity() - rotation; //calculate the motor power for the left wheel
        motorPowers[1] = getVelocity() + rotation; //calculate the motor power for the right wheel
        motorPowers[2] = motorPowers[1] + rotation;
        motorPowers[3] = motorPowers[0] - rotation;
        return motorPowers;
    }

    /**
     * @param position the position that you want the robot to move to
     */
    @Override
    public void setTargetPosition(double position) {
        for (int i = 0; i < runModes.length; i++) {
            runModes[i] = motors[i].getMode();  //Save the RunModes so we can restore them later
        }
        this.targetPosition = position;
        for (DcMotor motor : motors) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        for (DcMotor motor : motors) motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        for (int i = 0; i < motors.length; i++) {   //Calculate how far each wheel has to go to get the drivetrain to a specific position
            wheelTargetPositions[0] = targetPosition * (1 - rotation);
            wheelTargetPositions[1] = targetPosition * (1 + rotation);

            switch (i) {
                case 2:
                    motors[2].setTargetPosition((int) (wheelTargetPositions[1] + 0.5));//Round to the nearest int because setTargetPosition only accepts ints
                    break;
                case 3:
                    motors[3].setTargetPosition((int)(wheelTargetPositions[0] + 0.5));
                    break;
                default:
                    motors[i].setTargetPosition((int)(wheelTargetPositions[i] + 0.5));
            }
        }
        updateMotorPowers();
    }

    /**
     * @return the current position of the robot
     */
    @Override
    public double getCurrentPosition() {
        return ( motors[2].getCurrentPosition() + motors[3].getCurrentPosition() ) / 2; //takes an avg of the encoder values for the two front wheels
    }

    /**
     * @return the position the robot is supposed to move to (the Target Position)
     */
    @Override
    public double getTargetPosition() {
        return targetPosition;
    }

    /**
     * Recalculate motor powers to maintain or move towards the target position
     */
    @Override
    public void updatePosition() {
    }

    /**
     * Use this as a loop condition (with {@link #updatePosition in the loop body) if you want to move to a specific position and then move on to other code.
     *
     * @return Whether or not the drivetrain is still moving towards the target position
     */
    @Override
    public boolean isPositioning() {
        if (motors[0].isBusy() || motors[1].isBusy() || motors[2].isBusy() || motors[3].isBusy()) return true;
        else return false;
    }

    /**
     * If there are any settings (motor {@link DcMotor.RunMode RunModes}, etc.) you changed to position, change them back in this method.
     */
    @Override
    public void finishPositioning() {
        for (DcMotor motor : motors) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        for (int i = 0; i < motors.length; i++) motors[i].setMode(runModes[i]);
    }

    @Override
    public void position() {
        while (isPositioning()) updatePosition();
        finishPositioning();
    }

    /**
     * Depending on the encoder type, the number of ticks per rotation can vary. This method uses motor configuration data to calculate that number.
     *
     * @return the number of encoder ticks per rotation
     */
    @Override
    public double getTicksPerUnit() {  //The motors better have the same number of ticks per rotation (perhaps a future version can make this unnecessary), but get the average anyway
        double ticksPerUnit = 0;
        for (DcMotor motor : motors) ticksPerUnit += motor.getMotorType().getTicksPerRev();
        ticksPerUnit /= motors.length;
        return ticksPerUnit;
    }
}