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

@TeleOp(name = "TeleopTwoWheels", group = "prototype")

public class TeleopTwoWheels extends OpMode {

    private OmegaBotTwoWheels robot;

    private ToggleBoolean toggleBoolean = new ToggleBoolean();
    private ToggleBoolean driveMode = new ToggleBoolean();
    private ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
       robot = new OmegaBotTwoWheels(telemetry, hardwareMap);
       robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        robot.arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rack.setPower(0);
        if(!robot.arm1.isBusy()){ robot.arm1.setPower(0);}

        if(gamepad1.x) {
            robot.arm1.setTargetPosition(robot.arm1.getCurrentPosition() + 500);
            robot.arm1.setPower(0.5);
        }
        if(gamepad1.y) {
            robot.arm1.setTargetPosition(robot.arm1.getCurrentPosition() - 500);
            robot.arm1.setPower(-0.5);
        }
        while(gamepad1.dpad_up) {
            robot.rack.setPower(1);
        }
        while(gamepad1.dpad_down) {
            robot.rack.setPower(-1);
        }
        if(gamepad1.a) {
            time.reset();
            while(time.seconds() < 2) {
                robot.rack.setPower(1);
            }
            robot.rack.setPower(0);
        }
        if(gamepad1.b) {
            time.reset();
            while(time.seconds() < 2) {
                robot.rack.setPower(-1);
            }
            robot.rack.setPower(0);
        }
        robot.backLeft.setPower(-0.35 * gamepad1.left_stick_y); //pushing a gamepad joystick forward on d-mode gives a negative value, so we must invert it
        robot.backRight.setPower(-0.95 * gamepad1.right_stick_y);

        telemetry.addData("arm pos", robot.arm1.getCurrentPosition());
        telemetry.addData("back_left pos", robot.backLeft.getCurrentPosition());
        telemetry.addData("back_right pos", robot.backRight.getCurrentPosition());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }
}

