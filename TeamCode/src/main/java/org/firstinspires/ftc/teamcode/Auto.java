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

    OmegaBotTwoWheels robot = new OmegaBotTwoWheels(telemetry, hardwareMap);

    private int initialPos, finalPos;

    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        telemetry.addData("Initialization", "Complete");

        waitForStart();
        runtime.reset();
        robot.rack.setPower(1);
        sleep(2000);
        robot.rack.setPower(0);
        robot.drivetrain.setTargetPosition(-3000);
        robot.drivetrain.setVelocity(-1);
        while (opModeIsActive() && robot.drivetrain.isPositioning()) {

        }
        robot.rack.setPower(-1);
        sleep(2000);
        robot.rack.setPower(0);

    }
}
