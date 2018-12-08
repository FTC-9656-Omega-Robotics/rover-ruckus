package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.spa.ftclib.internal.state.ToggleBoolean;
import edu.spa.ftclib.sample.TestClass;

/**
 *
 */

@TeleOp(name = "Teleop", group = "prototype")

public class Teleop extends OpMode {

    private OmegaBot robot;

    private ToggleBoolean gamepad2LeftBumper = new ToggleBoolean();
    private ToggleBoolean gamepad2RightBumper = new ToggleBoolean();
    private ToggleBoolean gamepad2LeftTrigger = new ToggleBoolean();
    private ToggleBoolean gamepad2RightTrigger = new ToggleBoolean();


    private ElapsedTime time = new ElapsedTime();
    double speedDamper = 0.4;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        robot = new OmegaBot(telemetry, hardwareMap);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        runtime.reset();

    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        gamepad2LeftBumper.input(gamepad2.left_bumper);
        gamepad2RightBumper.input(gamepad2.right_bumper);
        gamepad2LeftTrigger.input(gamepad2.left_trigger > 0.2);
        gamepad2RightTrigger.input(gamepad2.right_trigger > 0.2);

        robot.frontLeft.setPower(-1 * speedDamper * gamepad1.left_stick_y);
        robot.backLeft.setPower(-1 * speedDamper * gamepad1.left_stick_y);

        robot.frontRight.setPower(-1 * speedDamper * gamepad1.right_stick_y);
        robot.backRight.setPower(-1 * speedDamper * gamepad1.right_stick_y);

        if (gamepad1.dpad_up) {
            robot.move(12, 0.5);
        }
        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            speedDamper = 0.8;
        } else {
            speedDamper = 0.4;
        }

        if (gamepad1.a) {
            robot.rightFlip.setPosition(0);
        } else if (gamepad1.b) {
            robot.rightFlip.setPosition(0.5);
        }
        if (gamepad1.x) {
            robot.leftFlip.setPosition(1);
        } else if (gamepad1.y) {
            robot.leftFlip.setPosition(0.5);
        }


        //gamepad2 will have all the special mechanisms

        if (gamepad2.dpad_up) {
            robot.lift.setPower(-1);
        } else if (gamepad2.dpad_down) {
            robot.lift.setPower(1);
        } else {
            robot.lift.setPower(0);
        }


        //INTAKES
//        if (gamepad2LeftBumper.output()) {
//            robot.leftIntake.setPower(0.9); //outtake
//        } else if (gamepad2LeftTrigger.output()) {
//            robot.leftIntake.setPower(-0.9);
//        } else {
//            robot.leftIntake.setPower(0);
//        }
//
//        if (gamepad2RightBumper.output()) {
//            robot.rightIntake.setPower(0.9); //outtake
//        } else if (gamepad2RightTrigger.output()) {
//            robot.rightIntake.setPower(-0.9); //intake
//        } else {
//            robot.rightIntake.setPower(0);
//        }

        if(gamepad2.left_bumper) {
            robot.leftIntake.setPower(0.9); //outtake
        } else if (gamepad2.left_trigger > 0.2) {
            robot.leftIntake.setPower(-0.9); //intake
        } else {
            robot.leftIntake.setPower(0);
        }

        if(gamepad2.right_bumper) {
            robot.rightIntake.setPower(0.9); //outtake
        } else if (gamepad2.right_trigger > 0.2) {
            robot.rightIntake.setPower(-0.9); //intake
        } else {
            robot.rightIntake.setPower(0);
        }



        robot.pivot.setPower(-gamepad2.left_stick_y);
        robot.arm.setPower(gamepad2.right_stick_y);


        // telemetry.addData("arm pos", robot.arm1.getCurrentPosition());
        telemetry.addData("front_left pos", robot.frontLeft.getCurrentPosition());
        telemetry.addData("front_right pos", robot.frontRight.getCurrentPosition());
        telemetry.addData("back_left pos", robot.backLeft.getCurrentPosition());
        telemetry.addData("back_right pos", robot.backRight.getCurrentPosition());
        telemetry.addData("left_flip pos", robot.leftFlip.getPosition());
        telemetry.addData("right_flip pos", robot.rightFlip.getPosition());
        telemetry.addData("lift", robot.lift.getCurrentPosition());
        telemetry.addData("imu", robot.imu.getAngularOrientation());
        telemetry.addData("getAngle()", robot.getAngle());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }
}

