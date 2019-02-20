package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutoDepotExp", group = "Testers")
//@Disabled

public class AutoDepotExp extends AutoBaseRoverRuckus {

    @Override
    public void runOpMode() {
        super.runOpMode();
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values
    public void goldLeft() {
        turnUsingPIDVoltage(33, robotSpeed);
        robot.movePID(Math.sqrt(1548) - 8, robotSpeed);
        turnUsingPIDVoltage(-55, robotSpeed);
        robot.movePID(Math.sqrt(1548) - 18, robotSpeed);
        robot.leftFlip.setPosition(0.6);
        robot.rightFlip.setPosition(0.4);
        sleep(100);
        robot.teamMarker.setPosition(1);
        sleep(100);
        robot.movePID(-(Math.sqrt(1548) - 18), robotSpeed);
        turnUsingPIDVoltage(55, robotSpeed);
        robot.movePID(-(Math.sqrt(1548) - 8), robotSpeed);
        turnUsingPIDVoltage(-33, robotSpeed);
    }

    public void goldCenter() {
        robot.movePID(3 * Math.sqrt(72) + 15, robotSpeed);
        robot.leftFlip.setPosition(0.6);
        robot.rightFlip.setPosition(0.4);
        sleep(1000);
        robot.teamMarker.setPosition(1);
        sleep(1000);
        robot.movePID(-(3 * Math.sqrt(72) + 15), robotSpeed);
    }


    public void goldRight() {
        turnUsingPIDVoltage(-33, robotSpeed);
        robot.movePID(Math.sqrt(1548) - 8, robotSpeed);
        turnUsingPIDVoltage(55, robotSpeed);
        robot.movePID(Math.sqrt(1548) - 18, robotSpeed);
        robot.leftFlip.setPosition(0.6);
        robot.rightFlip.setPosition(0.4);
        sleep(100);
        robot.teamMarker.setPosition(1);
        sleep(100);
        robot.movePID(-(Math.sqrt(1548) - 18), robotSpeed);
        turnUsingPIDVoltage(-55, robotSpeed);
        robot.movePID(-(Math.sqrt(1548) - 8), robotSpeed);
        turnUsingPIDVoltage(33, robotSpeed);
    }

    public void finishPath() {
        move(102, robotSpeed);
    }
}
