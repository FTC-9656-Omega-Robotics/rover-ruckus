package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.CameraDevice;

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
        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.escalator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        if (gamepad2.right_bumper) {
            robot.outtake.setPosition(0.55);
        } else if (gamepad1.right_bumper) {
            robot.outtake.setPosition(0.90);
        }

        robot.frontLeft.setPower(-1 * speedDamper * gamepad1.left_stick_y);
        robot.backLeft.setPower(-1 * speedDamper * gamepad1.left_stick_y);

        robot.frontRight.setPower(-1 * speedDamper * gamepad1.right_stick_y);
        robot.backRight.setPower(-1 * speedDamper * gamepad1.right_stick_y);

        if (gamepad1.dpad_up) {
            robot.teamMarker.setPosition(0.9); //0.9 is rest
        } else if (gamepad1.dpad_down) {
            robot.teamMarker.setPosition(0); //0 is extended
        }
        if (gamepad1.right_bumper) {
            speedDamper = 1;
        } else {
            speedDamper = 0.55;
        }

        if (gamepad1.a) {
            robot.escalator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.escalator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        if (gamepad1.x) {
            robot.escalator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.escalator.setPower(-1);
            robot.outtake.setPosition(0.80);
        } else if (gamepad1.right_trigger > 0.2) {
            //put B
            robot.leftFlip.getController().pwmEnable();
            robot.leftFlip.setPosition(0.25);
            robot.rightFlip.setPosition(0.75);
            robot.escalator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            robot.escalator.setPower(1);
            robot.outtake.setPosition(0.80);
        } else if (gamepad1.left_trigger > 0.2) {
            robot.escalator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.escalator.setTargetPosition(0);
            robot.escalator.setPower(-1);
        } else {
            robot.escalator.setPower(0);
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


        //Domains for flip positions when extensions are within a certain range. Domains have length 570. (0.83 - 0.855) / 3100 * 570 = -0.00460
        if (gamepad2.a && (robot.extension.getCurrentPosition() >= 2530 && robot.extension.getCurrentPosition() < 4000)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.8);//.63
            robot.rightFlip.setPosition(0.2);//.37
            waitFor(0.5);
            robot.leftFlip.getController().pwmDisable();
            robot.rightFlip.getController().pwmDisable();
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 1960 && robot.extension.getCurrentPosition() < 2530)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.8);
            robot.rightFlip.setPosition(0.2);
            waitFor(0.5);
            robot.leftFlip.getController().pwmDisable();
            robot.rightFlip.getController().pwmDisable();
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 1390 && robot.extension.getCurrentPosition() < 1960)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.8);
            robot.rightFlip.setPosition(0.2);
            waitFor(0.5);
            robot.leftFlip.getController().pwmDisable();
            robot.rightFlip.getController().pwmDisable();
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 820 && robot.extension.getCurrentPosition() < 1390)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.8);
            robot.rightFlip.setPosition(0.2);
            waitFor(0.5);
            robot.leftFlip.getController().pwmDisable();
            robot.rightFlip.getController().pwmDisable();
        } else if (gamepad2.a && (robot.extension.getCurrentPosition() >= 250 && robot.extension.getCurrentPosition() < 820)) { //upper bound of 3100 is just pushed to 4000 as safeguard
            robot.leftFlip.setPosition(0.8);
            robot.rightFlip.setPosition(0.2);
            waitFor(0.5);
            robot.leftFlip.getController().pwmDisable();
            robot.rightFlip.getController().pwmDisable();
        } else if (gamepad2.a && robot.extension.getCurrentPosition() < 250) {
            robot.leftFlip.setPosition(0.8);
            robot.rightFlip.setPosition(0.2);
            waitFor(0.5);
            robot.leftFlip.getController().pwmDisable();
            robot.rightFlip.getController().pwmDisable();
        } else if (gamepad2.b) {
            robot.leftFlip.getController().pwmEnable();
            robot.leftFlip.setPosition(0.25);
            robot.rightFlip.setPosition(0.75);
            //Don't let intake completely retract unless 1. extension is fairly in and 2. arm is fairly retracted
        } else if (gamepad2.y /*&& robot.extension.getCurrentPosition() < 200*/) {
            robot.leftFlip.getController().pwmEnable();
            robot.leftFlip.setPosition(0);
            robot.rightFlip.setPosition(1);
        }


        //gamepad2 will have all the special mechanisms
        if (gamepad2.dpad_up) {
            robot.lift.setPower(-1);
        } else if (gamepad2.dpad_down) {
            robot.lift.setPower(1);
        } else {
            robot.lift.setPower(0);
        }

        if (gamepad2.left_trigger > 0.2) {
            robot.intake.setPower(1);
        } else if (gamepad2.left_bumper || gamepad2.y) {
            robot.intake.setPower(-1);
        } else {
            robot.intake.setPower(0);
        }

        if (gamepad2.x) {
            robot.extension.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.extension.setTargetPosition(0);
            robot.extension.setPower(-1);
        } else {
            robot.extension.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.extension.setPower(-gamepad2.left_stick_y);
        }

        telemetry.addData("escalator pos", robot.escalator.getCurrentPosition());
        telemetry.addData("front_left pos", robot.frontLeft.getCurrentPosition());
        telemetry.addData("front_right pos", robot.frontRight.getCurrentPosition());
        telemetry.addData("back_left pos", robot.backLeft.getCurrentPosition());
        telemetry.addData("back_right pos", robot.backRight.getCurrentPosition());
        telemetry.addData("left_flip pos", robot.leftFlip.getPosition());
        telemetry.addData("right_flip pos", robot.rightFlip.getPosition());
        telemetry.addData("team_marker", robot.teamMarker.getPosition());
        telemetry.addData("lift", robot.lift.getCurrentPosition());
        telemetry.addData("extension", robot.extension.getCurrentPosition());
        telemetry.addData("heading", robot.getAngle());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }

    /**
     * Tells the thread to wait for a number of seconds. Exception-protected
     *
     * @param sec number of sec to wait for
     */
    private void waitFor(double sec) {
        try {
            Thread.sleep((long) (sec * 1000));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

