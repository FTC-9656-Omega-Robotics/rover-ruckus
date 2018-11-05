package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Auto", group = "Testers")
//@Disabled

public class Auto extends LinearOpMode {


    private int initialPos, finalPos;

    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        OmegaBotTwoWheels robot = new OmegaBotTwoWheels(telemetry, hardwareMap);
        robot.rack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rack.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Initialization", "Complete");

        waitForStart();
        runtime.reset();
        robot.rack.setPower(1);
        sleep(2000);
        robot.rack.setPower(0);
        robot.drivetrain.setTargetPosition(-4000);
        robot.backLeft.setPower(-0.35);
        robot.backRight.setPower(-0.95);
        while (robot.drivetrain.isPositioning()) {

        }
        robot.rack.setPower(-1);
        sleep(2000);
        robot.rack.setPower(0);
        robot.drivetrain.setRotation(-Math.PI / 2);
        sleep(3000);
//        robot.drivetrain.setTargetPosition(10000);
//       robot.backLeft.setPower(-0.35);
//       robot.backRight.setPower(-1);
//        sleep(1000);
        robot.drivetrain.setVelocity(0);
    }
}
