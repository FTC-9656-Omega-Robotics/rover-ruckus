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

//        //SIMPLIFIED (comment this block or the one below out Domains for flip positions when extensions are within a certain range
//        if (gamepad2.a && robot.extension.getCurrentPosition() > 250) {
//            robot.leftFlip.setPosition(0.63);
//            robot.rightFlip.setPosition(0.37);
//        }
//        else if (gamepad2.a && robot.extension.getCurrentPosition() < 250) {
//            robot.leftFlip.setPosition(0.655);
//            robot.rightFlip.setPosition(0.345);
//        } else if (gamepad2.b) {
//            robot.rightFlip.setPosition(0.7);
//            robot.leftFlip.setPosition(0.3);
//        } else if (gamepad2.y) {
//            robot.rightFlip.setPosition(0.9);
//            robot.leftFlip.setPosition(0.1);


        //Domains for flip positions when extensions are within a certain range. Domains have length 570. (0.63 - 0.655) / 3100 * 570 = -0.00460
        if (gamepad2.a && (robot.extension.getCurrentPosition() >= 2530 && robot.extension.getCurrentPosition() < 4000)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.63);
            robot.rightFlip.setPosition(0.37);
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 1960 && robot.extension.getCurrentPosition() < 2530)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.6366);
            robot.rightFlip.setPosition(1 - 0.6366);
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 1390 && robot.extension.getCurrentPosition() < 1960)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.6412);
            robot.rightFlip.setPosition(1 - 0.6412);
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 820 && robot.extension.getCurrentPosition() < 1390)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.6458);
            robot.rightFlip.setPosition(1 - 0.6458);
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 250 && robot.extension.getCurrentPosition() < 820)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.6504);
            robot.rightFlip.setPosition(1 - 0.6504);
        } else if (gamepad2.a && robot.extension.getCurrentPosition() < 250) {
            robot.leftFlip.setPosition(0.655);
            robot.rightFlip.setPosition(0.345);
        } else if (gamepad2.b) {
            robot.rightFlip.setPosition(0.7);
            robot.leftFlip.setPosition(0.3);
        } else if (gamepad2.y) {
            robot.rightFlip.setPosition(0.9);
            robot.leftFlip.setPosition(0.1);
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
            int armMaxHeight = 600;
            robot.arm.setTargetPosition(armMaxHeight);
            robot.arm.setPower(0.5);
        }

        if (gamepad2.right_trigger > 0.2) {
            robot.arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            int armMinHeight = 0;
            robot.arm.setTargetPosition(armMinHeight);
            robot.arm.setPower(-0.5);
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
        //robot.arm.setPower(gamepad2.right_stick_y * -0.4);


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

