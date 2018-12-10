//package org.firstinspires.ftc.teamcode;
//
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import edu.spa.ftclib.internal.state.ToggleBoolean;
//
///**
// *
// */
//
//@TeleOp(name = "TeleopGyroTest", group = "prototype")
//
//public class TeleopGyroTest extends OpMode {
//
//    private OmegaBot robot;
//
//    private ToggleBoolean toggleBoolean = new ToggleBoolean();
//    private ToggleBoolean driveMode = new ToggleBoolean();
//    private Toggle gamepad2LeftBumper = new Toggle();
//    private Toggle gamepad2RightBumper = new Toggle();
//    private Toggle gamepad2LeftTrigger = new Toggle();
//    private Toggle gamepad2RightTrigger = new Toggle();
//    PIDController           pidRotate, pidDrive;
//
//
//    private ElapsedTime time = new ElapsedTime();
//    double speedDamper = 0.4;
//    private ElapsedTime runtime = new ElapsedTime();
//    private double power = 0.4;
//
//    @Override
//    public void init() {
//        robot = new OmegaBot(telemetry, hardwareMap);
//        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        runtime.reset();
//        // Set PID proportional value to start reducing power at about 50 degrees of rotation.
//        pidRotate = new PIDController(.01, 0, 0);
//
//        // Set PID proportional value to produce non-zero correction value when robot veers off
//        // straight line. P value controls how sensitive the correction is.
//        pidDrive = new PIDController(.05, 0, 0);
//        // Set up parameters for driving in a straight line.
//        pidDrive.setSetpoint(0);
//        pidDrive.setOutputRange(0, power);
//        pidDrive.setInputRange(-90, 90);
//        pidDrive.enable();
//
//    }
//
//    /**
//     * In teleop mode, the robot is continually checking for input from the user.
//     */
//    @Override
//    public void loop() {
//        if(gamepad1.a) {
//            rotate(60, 0.5);
//        } else if (gamepad1.b) {
//            rotate(-60, 0.5);
//        } else if (gamepad1.x) {
//            robot.move(12, 0.5);
//        } else if (gamepad1.y) {
//            robot.move(-12, 0.5);
//        }
//
//        robot.frontLeft.setPower(-1 * speedDamper * gamepad1.left_stick_y);
//        robot.backLeft.setPower(-1 * speedDamper * gamepad1.left_stick_y);
//
//        robot.frontRight.setPower(-1 * speedDamper * gamepad1.right_stick_y);
//        robot.backRight.setPower(-1 * speedDamper * gamepad1.right_stick_y);
//
//        // telemetry.addData("arm pos", robot.arm1.getCurrentPosition());
//        telemetry.addData("front_left pos", robot.frontLeft.getCurrentPosition());
//        telemetry.addData("front_right pos", robot.frontRight.getCurrentPosition());
//        telemetry.addData("back_left pos", robot.backLeft.getCurrentPosition());
//        telemetry.addData("back_right pos", robot.backRight.getCurrentPosition());
//        telemetry.addData("lift", robot.lift.getCurrentPosition());
//        telemetry.addData("imu", robot.imu.getAngularOrientation());
//        telemetry.addData("getAngle()", robot.getAngle());
//    }
//
//    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
//        return (Math.abs(a) > Math.abs(b)) ? a : b;
//    }
//
//    /**
//     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
//     * @param degrees Degrees to turn, + is left - is right
//     */
//    private void rotate(int degrees, double power)
//    {
//        // restart imu angle tracking.
//        robot.resetAngle();
//
//        // start pid controller. PID controller will monitor the turn angle with respect to the
//        // target angle and reduce power as we approach the target angle with a minimum of 20%.
//        // This is to prevent the robots momentum from overshooting the turn after we turn off the
//        // power. The PID controller reports onTarget() = true when the difference between turn
//        // angle and target angle is within 2% of target (tolerance). This helps prevent overshoot.
//        // The minimum power is determined by testing and must enough to prevent motor stall and
//        // complete the turn. Note: if the gap between the starting power and the stall (minimum)
//        // power is small, overshoot may still occur. Overshoot is dependant on the motor and
//        // gearing configuration, starting power, weight of the robot and the on target tolerance.
//
//        pidRotate.reset();
//        pidRotate.setSetpoint(degrees);
//        pidRotate.setInputRange(0, 90);
//        pidRotate.setOutputRange(.2, power);
//        pidRotate.setTolerance(2);
//        pidRotate.enable();
//
//        // getAngle() returns + when rotating counter clockwise (left) and - when rotating
//        // clockwise (right).
//
//        // rotate until turn is completed.
//
//        if (degrees < 0)
//        {
//            // On right turn we have to get off zero first.
//            while (robot.getAngle() == 0)
//            {
//                robot.frontLeft.setPower(power);
//                robot.backLeft.setPower(power);
//                robot.frontRight.setPower(-power);
//                robot.backRight.setPower(-power);
//            }
//
//            do
//            {
//                power = pidRotate.performPID(robot.getAngle()); // power will be - on right turn.
//                robot.frontLeft.setPower(-power);
//                robot.backLeft.setPower(-power);
//                robot.frontRight.setPower(power);
//                robot.backRight.setPower(power);
//
//            } while (!pidRotate.onTarget());
//        }
//        else    // left turn.
//            do
//            {
//                power = pidRotate.performPID(robot.getAngle()); // power will be + on left turn.
//                robot.frontLeft.setPower(-power);
//                robot.backLeft.setPower(-power);
//                robot.frontRight.setPower(power);
//                robot.backRight.setPower(power);
//            } while (!pidRotate.onTarget());
//
//        // turn the motors off.
//        robot.drivetrain.setVelocity(0);
//
//
//
//        // reset angle tracking on new heading.
//        robot.resetAngle();
//    }
//}
//
