package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AutoDepotExpNoCrater", group = "Testers")
//@Disabled

public class AutoDepotExpNoCrater extends AutoBaseRoverRuckus {

    @Override
    public void runOpMode() {
        super.runOpMode();
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values
    public void goldLeft() {
        turnUsingPIDVoltage(35, robotSpeed);
        movePID(28, robotSpeed);
        turnUsingPIDVoltage(-58, robotSpeed);
        movePID(27, robotSpeed);
        depositMarker();
        turnUsingPIDVoltage(-27, robotSpeed);
        turnUsingPIDVoltage(58, robotSpeed);
        movePID(-28, robotSpeed);

    }

    public void goldCenter() {
        movePID(39, robotSpeed);
        robot.leftFlip.setPosition(0.6);
        robot.rightFlip.setPosition(0.4);
        sleep(1000);
        robot.teamMarker.setPosition(1);
        sleep(1000);
        robot.teamMarker.setPosition(0);

        robot.leftFlip.setPosition(0);
        robot.rightFlip.setPosition(1);
        movePID(-39, robotSpeed);
    }


    public void goldRight() {
        turnUsingPIDVoltage(-35, robotSpeed);
        movePID(28, robotSpeed);
        turnUsingPIDVoltage(63, robotSpeed);
        movePID(27, robotSpeed);
        robot.leftFlip.setPosition(0.6);
        robot.rightFlip.setPosition(0.4);
        sleep(100);
        robot.teamMarker.setPosition(1);
        sleep(1000);
        robot.teamMarker.setPosition(0);

        robot.leftFlip.setPosition(0);
        robot.rightFlip.setPosition(1);
        movePID(-27, robotSpeed);
        turnUsingPIDVoltage(-63, robotSpeed);
        movePID(-28, robotSpeed);

        //TODO: Perhaps incorrect args codeblock below
//        turnUsingPIDVoltage(-35, robotSpeed);
//        movePID(24, robotSpeed);
//        turnUsingPIDVoltage(63, robotSpeed);
//        movePID(29, robotSpeed);
//        robot.leftFlip.setPosition(0.6);
//        robot.rightFlip.setPosition(0.4);
//        sleep(100);
//        robot.teamMarker.setPosition(1);
//        sleep(1000);
//        robot.teamMarker.setPosition(0);
//
//        robot.leftFlip.setPosition(0);
//        robot.rightFlip.setPosition(1);
//        movePID(-29, robotSpeed);
//        turnUsingPIDVoltage(-63, robotSpeed);
//        movePID(-24, robotSpeed);
    }

    public void finishPath() {
        movePID(102, robotSpeed);
    }
}
