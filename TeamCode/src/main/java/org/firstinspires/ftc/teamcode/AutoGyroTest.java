//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//@Autonomous(name = "AutoGyro Test", group = "Testers")
////@Disabled
//public class AutoGyroTest extends LinearOpMode {
//    private OmegaBot robot;
//    private double robotSpeed = 0.3;
//
//    @Override
//    public void runOpMode() {
//        robot = new OmegaBot(telemetry, hardwareMap);
//        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        robot.setDrivetrainToMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.setDrivetrainToMode(DcMotor.RunMode.RUN_TO_POSITION);
//        waitForStart();
//        robot.move(6, 0.3);
//        robot.drivetrain.setTargetHeading(Math.PI);
//        robot.drivetrain.rotate();
//        telemetry.addLine("Done rotating");
//        robot.move(12, 0.3);
//        robot.drivetrain.setTargetHeading(-Math.PI);
//        robot.move(12, 0.3);
//    }
//}
