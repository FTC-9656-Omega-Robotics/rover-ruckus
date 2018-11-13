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
        OmegaBot robot = new OmegaBot(telemetry, hardwareMap);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setDirection(DcMotor.Direction.REVERSE);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Initialization", "Complete");
        telemetry.addData("front_left", robot.frontLeft.getCurrentPosition());
        telemetry.addData("front_right", robot.frontRight.getCurrentPosition());
        telemetry.addData("back_left", robot.backLeft.getCurrentPosition());
        telemetry.addData("back_right", robot.backRight.getCurrentPosition());
        telemetry.update();
        waitForStart();
        runtime.reset();
        robot.move(50, 0.3);
        while(opModeIsActive()) {
            telemetry.update();
        }
    }
}
