package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "PIDTesting", group = "Testers")
//@Disabled
/**
 * Note that methods called that are not prefixed by "robot." involve loops and thus, on the backorder of things (abstraction), check for opModeIsActive()
 */

public class PIDTesting extends AutoBaseRoverRuckus {
    public void runOpMode(){
        robot = new OmegaBot(telemetry, hardwareMap);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.turnUsingPIDVoltage(-90, robotSpeed);
    }

    public void goldLeft(){
        robot.turn(-28.561,robotSpeed);
        robot.move(Math.sqrt(1260),robotSpeed);
        robot.turn(70.746,robotSpeed);
        robot.move(Math.sqrt(1440),robotSpeed);
        robot.turn(-(180-18.435),robotSpeed);

    }

    public void goldCenter(){
        robot.move(7.5*Math.sqrt(72),1);
    }

    public void goldRight(){
        robot.turn(28.561,robotSpeed);
        robot.move(Math.sqrt(1260),robotSpeed);
        robot.turn(-70.746,robotSpeed);
        robot.move(Math.sqrt(1440),robotSpeed);
        robot.turn(-108.435,robotSpeed);
    }

    public void finishPath(){

    }

}
