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

@Autonomous(name = "MotorAuto", group = "Testers")
//@Disabled

public class MotorAuto extends LinearOpMode {

    OmegaBot robot = new OmegaBot(telemetry, hardwareMap);

    private int initialPos, finalPos;

    private ElapsedTime     runtime = new ElapsedTime();




    @Override
    public void runOpMode() {


        telemetry.addData("Initialization", "Complete");




        waitForStart();

        telemetry.addData("Initial position", initialPos);
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 0.25) {
            robot.drivetrain.setVelocity(1);
        }
        robot.drivetrain.setVelocity(0);
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 0.5) {
            robot.arm1.setPower(1);
        }
        robot.arm1.setPower(0);



    }
}
