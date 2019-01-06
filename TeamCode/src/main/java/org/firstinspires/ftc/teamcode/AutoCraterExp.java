package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AutoCraterExp", group = "Testers")
//@Disabled
/**
 * Note that methods called that are not prefixed by "robot." involve loops and thus, on the backorder of things (abstraction), check for opModeIsActive()
 */
public class AutoCraterExp extends AutoBaseRoverRuckus {


    @Override
    public void runOpMode() {
        super.runOpMode();
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values {null--none, 100, 315}
    //give up on depositing marker into depot. just parking into crater bc of time constraints.
    public void goldLeft() {
        turnUsingPIDVoltage(33, robotSpeed);
        move(Math.sqrt(1548) - 3, robotSpeed);
        robot.leftFlip.setPosition(0.5);
        robot.rightFlip.setPosition(0.5);
        sleep(2000);
//        move(-(Math.sqrt(1548)), robotSpeed);
//        turnUsingPIDVoltage(18.620, robotSpeed);
//        move(Math.sqrt(1332), robotSpeed);
    }

    public void goldCenter() {
        move(3 * Math.sqrt(72) - 3, robotSpeed);
        robot.leftFlip.setPosition(0.5);
        robot.rightFlip.setPosition(0.5);
        sleep(2000);
//        move(-(3 * Math.sqrt(72) - 2), robotSpeed);
//        turnUsingPIDVoltage(52, robotSpeed);
//        move(Math.sqrt(1332) + 5, robotSpeed); // addendum
//        turnUsingPIDVoltage(78, robotSpeed); // a rough guess
    }

    public void goldRight() {
        turnUsingPIDVoltage(-33, robotSpeed);
        move(Math.sqrt(1548) - 3, robotSpeed);
        robot.leftFlip.setPosition(0.5);
        robot.rightFlip.setPosition(0.5);
        sleep(2000);
//        move(-(Math.sqrt(1548)), robotSpeed);
//        robot.turnUsingPIDVoltage(2 * 49.684 + 22.620, robotSpeed);

    }

    /**
     * Robot should be at leftmost point on the fan and oriented toward the depot before executing
     * this path
     */
    public void finishPath() {
        move(Math.sqrt(3636), robotSpeed);
        robot.teamMarker.setPosition(0); // 0 is retracted, 0.9 is extended
        sleep(1000);
        robot.teamMarker.setPosition(0.9);
        turnUsingPIDVoltage(-174.289, robotSpeed);
        move(90, robotSpeed);
    }
}
