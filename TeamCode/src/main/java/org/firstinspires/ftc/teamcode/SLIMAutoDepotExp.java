package org.firstinspires.ftc.teamcode;

/**
 * Created by tvt on 9/21/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "SLIMAutoDepotExp", group = "Testers")
//@Disabled

public class SLIMAutoDepotExp extends AutoBaseRoverRuckus {

    @Override
    public void runOpMode() {
        super.runOpMode();
    }

    //preset paths based on where the gold cube is located (left, center, right) based on approximate x values
    public void goldLeft() {
        turnUsingPIDVoltage(33, robotSpeed);
        move(Math.sqrt(1548) - 8, robotSpeed);
        move(-(Math.sqrt(1548) - 8), robotSpeed);
        turnUsingPIDVoltage(-33, robotSpeed);
    }

    public void goldCenter() {
        move(3 * Math.sqrt(72) + 15, robotSpeed);
        move(-(3 * Math.sqrt(72) + 15), robotSpeed);
    }


    public void goldRight() {
        turnUsingPIDVoltage(-33, robotSpeed);
        move(Math.sqrt(1548) - 8, robotSpeed);
        move(-(Math.sqrt(1548) - 8), robotSpeed);
        turnUsingPIDVoltage(33, robotSpeed);
    }

    public void finishPath() {
        move(102, robotSpeed);
    }
}
