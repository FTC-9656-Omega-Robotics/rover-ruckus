package org.firstinspires.ftc.teamcode;

/**
 * Designed to return power to DcMotors to turn accordingly to gyro
 */
public class OmegaPID{
    public double prevError, error, power, derror, pgain, dgain, igain, ierror, threshold;

    /**
     * Initialize PID controller with constants for calibration
     * @param pgain weighted constant for position
     * @param igain weighted constant for integral
     * @param dgain weighted constant for derivative
     * @param threshold tolerance
     */
    public OmegaPID(double pgain, double igain, double dgain, double threshold){
        this.pgain = pgain;
        this.igain = igain;
        this.dgain = dgain;
        this.threshold = threshold;
    }

    /**
     * Return power that is desired for the motors to turn appropriately according to sensed gyro values and PID
     * @param currentAngle current angle in degrees
     * @param desiredAngle desired angle in degrees
     * @param minPower min power robot should turn at
     * @param maxPower max power robot should turn at
     * @return
     */
    public double calculatePower(double currentAngle, double desiredAngle, double minPower, double maxPower){
        if(Math.abs(error)>threshold) {
            error = desiredAngle - currentAngle;
            ierror += error;
            derror = error - prevError;
            prevError = error;
            power = error * pgain - derror * dgain + ierror * igain;
            if (power > maxPower) {
                return maxPower;
            }
            if (power < minPower) {
                return minPower;
            }
            return power;
        }
        return 0;
    }

}