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

    private double speedDamper = 0.4;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        robot = new OmegaBot(telemetry, hardwareMap);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
            robot.teamMarker.setPosition(0.9); //0.9 is rest
        } else if (gamepad1.dpad_down) {
            robot.teamMarker.setPosition(0); //0 is extended
        }
        if (gamepad1.left_bumper && gamepad1.right_bumper) {
            speedDamper = 1;
        } else {
            speedDamper = 0.55;
        }

        if (gamepad2.a && robot.extension.getCurrentPosition() > 250) {
            robot.rightFlip.setPosition(0.44);
            robot.leftFlip.setPosition(0.56);
        }
        if (gamepad2.a && robot.extension.getCurrentPosition() < 250) {
            robot.rightFlip.setPosition(0.45);
            robot.leftFlip.setPosition(0.55);
        } else if (gamepad2.b) {
            robot.rightFlip.setPosition(0.7);
            robot.leftFlip.setPosition(0.3);
        } else if (gamepad2.y) {
            robot.rightFlip.setPosition(1);
            robot.leftFlip.setPosition(0);
            //robot.rightFlip.setPosition(0.95);
            //robot.leftFlip.setPosition(0.05);
        }
//push button to set lift to maximum height, currently not working
        if (gamepad2.x) {
            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            int liftMaxHeight = 18100;
            robot.lift.setTargetPosition(-liftMaxHeight);
            robot.lift.setPower(-1);
            robot.lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //push buttons for moving arm to set positions
        if (gamepad2.right_bumper) {
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            int armMaxHeight = 500;
            robot.arm.setTargetPosition(armMaxHeight);
            robot.arm.setPower(0.3);
            robot.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (gamepad2.right_trigger > 0.2) {
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            int armMinHeight = 0;
            robot.arm.setTargetPosition(armMinHeight);
            robot.arm.setPower(-0.3);
            robot.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
/**
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
 **/

        if (gamepad2.left_trigger > 0.2) {
            robot.intake.setPower(1);
        } else if (gamepad2.left_bumper) {
            robot.intake.setPower(-1);
        } else {
            robot.intake.setPower(0);
        }

        robot.extension.setPower(-gamepad2.left_stick_y * 0.7);

        //if(robot.extension.getCurrentPosition()>5) robot.extension.setPower(-gamepad2.left_stick_y*0.4);
        //else robot.extension.setPower(0);
        robot.arm.setPower(gamepad2.right_stick_y * -0.4);


        telemetry.addData("arm pos", robot.arm.getCurrentPosition());
        telemetry.addData("front_left pos", robot.frontLeft.getCurrentPosition());
        telemetry.addData("front_right pos", robot.frontRight.getCurrentPosition());
        telemetry.addData("back_left pos", robot.backLeft.getCurrentPosition());
        telemetry.addData("back_right pos", robot.backRight.getCurrentPosition());
        telemetry.addData("left_flip pos", robot.leftFlip.getPosition());
        telemetry.addData("right_flip pos", robot.rightFlip.getPosition());
        telemetry.addData("team_marker", robot.teamMarker.getPosition());
        telemetry.addData("lift", robot.lift.getCurrentPosition());
        telemetry.addData("extension", robot.extension.getCurrentPosition());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }
}

