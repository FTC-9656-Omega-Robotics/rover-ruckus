package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by tvt on 9/29/17.
 */

public class OmegaBot {

    public DcMotor leftDrive = null;
    private DcMotor.RunMode initialMode = null;

    HardwareMap map = null;


    public OmegaBot(DcMotor.RunMode enteredMode) {
        initialMode = enteredMode;
    }

    public void init(HardwareMap aMap) {

        map = aMap;

        leftDrive = map.dcMotor.get("motor");

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        stopRobot();
    }

    public void stopRobot() {
        leftDrive.setPower(0);
    }
}
