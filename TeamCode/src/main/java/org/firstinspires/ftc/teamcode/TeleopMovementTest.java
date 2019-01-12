package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.spa.ftclib.internal.state.ToggleBoolean;

/**
 *
 */

@TeleOp(name = "TeleopMovementTest", group = "prototype")

public class TeleopMovementTest extends OpMode {

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
        if(gamepad1.a) {
            robot.teamMarker.setPosition(1);
        } else if (gamepad1.b) {
            robot.teamMarker.setPosition(0);
        }

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
        telemetry.addData("orientation", robot.getAngle());
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }
}

