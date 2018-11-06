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

    private ToggleBoolean toggleBoolean = new ToggleBoolean();
    private ToggleBoolean driveMode = new ToggleBoolean();
    private ElapsedTime time = new ElapsedTime();
    double speedDamper = 1;

    @Override
    public void init() {
        robot = new OmegaBot(telemetry, hardwareMap);
    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.frontLeft.setPower(speedDamper * gamepad1.left_stick_y);
        robot.backLeft.setPower(speedDamper * gamepad1.left_stick_y);

        robot.frontRight.setPower(speedDamper * gamepad1.right_stick_y);
        robot.backRight.setPower(speedDamper * gamepad1.right_stick_y);

        if(gamepad1.left_bumper && gamepad1.right_bumper) {
            speedDamper = 0.4;
        } else {
            speedDamper = 1;
        }

        if(gamepad1.a) {
            robot.moveForward100();
        }
        if(gamepad1.b) {
            robot.moveBackward100();
        }

       // telemetry.addData("arm pos", robot.arm1.getCurrentPosition());
        telemetry.addData("front_left pos", robot.frontLeft.getCurrentPosition());
        telemetry.addData("front_right pos", robot.frontRight.getCurrentPosition());
        telemetry.addData("back_left pos", robot.backLeft.getCurrentPosition());
        telemetry.addData("back_right pos", robot.backRight.getCurrentPosition());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }
}

