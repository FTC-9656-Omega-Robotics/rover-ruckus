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

    OmegaBot robot = new OmegaBot(DcMotor.RunMode.RUN_USING_ENCODER);

    private int initialPos, finalPos;

    private ElapsedTime     runtime = new ElapsedTime();




    @Override
    public void runOpMode() {


        robot.init(hardwareMap);
        telemetry.addData("Initialization", "Complete");


        initialPos = robot.leftDrive.getCurrentPosition();


        waitForStart();

        telemetry.addData("Initial position", initialPos);
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 0.25) {
            robot.leftDrive.setPower(1);
        }

        robot.stopRobot();

        finalPos = robot.leftDrive.getCurrentPosition();
        telemetry.addData("Final position", finalPos);

        telemetry.addData("Change in position", finalPos - initialPos);


    }
}
