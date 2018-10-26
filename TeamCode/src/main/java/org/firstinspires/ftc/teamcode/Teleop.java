package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import edu.spa.ftclib.internal.state.ToggleBoolean;
import edu.spa.ftclib.sample.TestClass;

/**
 *
 */

@TeleOp(name = "Teleop", group = "prototype")

public class Teleop extends OpMode {

    private OmegaBot robot;

    private TankDrivetrainFourWheels drivetrain;
    private ToggleBoolean driveMode;

    @Override
    public void init() {
        robot = new OmegaBot(telemetry, hardwareMap);

        driveMode = new ToggleBoolean();
    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        if(gamepad1.a) {
            robot.arm1.setPower(0.1);
        } else {
            robot.arm1.setPower(0);
        }
        if(gamepad1.b) {
            robot.arm1.setPower(-0.1);
        } else {
            robot.arm1.setPower(0);
        }
        if(gamepad2.a) {
            robot.moveForward100();
        }
        telemetry.addData("Arm position", robot.arm1.getCurrentPosition());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }
}

