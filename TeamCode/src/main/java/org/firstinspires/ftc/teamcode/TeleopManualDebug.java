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

@TeleOp(name = "TeleopManualDebug", group = "prototype")

public class TeleopManualDebug extends OpMode {

    private OmegaBotTwoWheels robot;

    private ToggleBoolean toggleBoolean = new ToggleBoolean();
    private ToggleBoolean driveMode = new ToggleBoolean();
    private ElapsedTime time = new ElapsedTime();

    @Override
    public void init() {
        robot = new OmegaBotTwoWheels(telemetry, hardwareMap);

    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        if(!robot.drivetrain.isPositioning()) {
            robot.drivetrain.setVelocity(0);
        }
        if(gamepad2.left_bumper) {
            robot.drivetrain.setTargetPosition(1000);
            robot.drivetrain.setVelocity(1);
        }
        if(gamepad2.right_bumper) {
            robot.drivetrain.setTargetPosition(-1000);
            robot.drivetrain.setVelocity(-1);
        }
        if(gamepad2.a) {
            time.reset();
            while(time.seconds() < 2) {
                robot.rack.setPower(1);
            }
        }
        if(gamepad2.b) {
            time.reset();
            while(time.seconds() < 2) {
                robot.rack.setPower(-1);
            }
        }
        if(!robot.rack.isBusy()) {
            robot.rack.setPower(0);
        }

        telemetry.addData("arm pos", robot.arm1.getCurrentPosition());
        telemetry.addData("back_left pos", robot.backLeft.getCurrentPosition());
        telemetry.addData("back_right pos", robot.backRight.getCurrentPosition());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }
}

