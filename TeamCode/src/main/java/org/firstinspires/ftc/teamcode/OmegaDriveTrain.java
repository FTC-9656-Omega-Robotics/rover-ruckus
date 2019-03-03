package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class OmegaDriveTrain {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    public OmegaDriveTrain(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
    }

    /**
     * Set the number of ticks for the drivetrain to go forward
     *
     * @param ticks - number of ticks
     */
    public void setTargetPosition(double ticks) {
        int distance = (int) (ticks + 0.5);
        frontLeft.setTargetPosition(distance);
        frontRight.setTargetPosition(distance);
        backLeft.setTargetPosition(distance);
        backRight.setTargetPosition(distance);
    }

    public int getAvgEncoderValueOfFrontWheels() {
        return (frontLeft.getCurrentPosition() + frontRight.getCurrentPosition()) / 2;
    }

    public int getAvgEncoderValueOfBackWheels() {
        return (backLeft.getCurrentPosition() + backRight.getCurrentPosition()) / 2;
    }

    public int getAvgEncoderValueOfLeftWheels() {
        return (frontLeft.getCurrentPosition() + backLeft.getCurrentPosition()) / 2;
    }

    public int getAvgEncoderValueOfRightWheels() {
        return (frontRight.getCurrentPosition() + backRight.getCurrentPosition()) / 2;
    }

    /**
     * Set all motors to a runmode
     *
     * @param - the run mode
     */
    public void setRunMode(DcMotor.RunMode runMode) {
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backLeft.setMode(runMode);
        backRight.setMode(runMode);
    }

    /**
     * Set the velocity of all the wheels
     *
     * @param velocity - a number between -1.0 and 1.0
     */
    public void setVelocity(double velocity) {
        frontLeft.setPower(velocity);
        frontRight.setPower(velocity);
        backLeft.setPower(velocity);
        backRight.setPower(velocity);
    }

    /**
     * Use as loop condition to wait for drivetrain to finish positioning before moving onto the rest of the code
     */
    public boolean isPositioning() {
        return frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy();
    }
}
