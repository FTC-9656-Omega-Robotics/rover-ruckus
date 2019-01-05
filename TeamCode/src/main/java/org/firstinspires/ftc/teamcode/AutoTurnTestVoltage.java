package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutoTurnTestVoltage", group = "Testers")
//@Disabled

public class AutoTurnTestVoltage extends LinearOpMode {


    private int initialPos, finalPos;
    private GoldAlignDetector detector;
    private ElapsedTime runtime = new ElapsedTime();
    private OmegaBot robot;
    private double robotSpeed = 0.6;

    @Override
    public void runOpMode() {
        robot = new OmegaBot(telemetry, hardwareMap);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);

        //imu
        // make sure the imu gyro is calibrated before continuing.
        while (!isStopRequested() && !robot.imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Mode", "waiting for start");
        telemetry.addData("imu calib status", robot.imu.getCalibrationStatus().toString());
        telemetry.update();


        waitForStart();
        runtime.reset();
        robot.move(12, robotSpeed);
        sleep(1000);
        robot.move(-12, robotSpeed);
        robot.turnUsingPIDVoltage(360, robotSpeed);
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values {null--none, 100, 315}
    public void goldLeft() {
        robot.turn(-28.561, robotSpeed);
        robot.move(Math.sqrt(1260), robotSpeed);
        robot.turn(70.746, robotSpeed);
        robot.move(Math.sqrt(1440), robotSpeed);
        robot.turn(-(180 - 18.435), robotSpeed);

    }

    public void goldCenter() {
        robot.move(7.5 * Math.sqrt(72), 1);
    }

    public void goldRight() {
        robot.turn(28.561, robotSpeed);
        robot.move(Math.sqrt(1260), robotSpeed);
        robot.turn(-70.746, robotSpeed);
        robot.move(Math.sqrt(1440), robotSpeed);
        robot.turn(-108.435, robotSpeed);
    }

    public void finishPath() {

    }
}
