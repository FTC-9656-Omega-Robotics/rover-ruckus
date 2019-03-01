package org.firstinspires.ftc.teamcode;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.CameraControl;

import edu.spa.ftclib.internal.state.ToggleBoolean;

/**
 *
 */

@TeleOp(name = "TeleopCamera", group = "prototype")

public class TeleopCamera extends OpMode {

    private OmegaBot robot;

    private ToggleBoolean gamepad2LeftBumper = new ToggleBoolean();
    private ToggleBoolean gamepad2RightBumper = new ToggleBoolean();
    private ToggleBoolean gamepad2LeftTrigger = new ToggleBoolean();
    private ToggleBoolean gamepad2RightTrigger = new ToggleBoolean();

    private double speedDamper = 0.4;
    private ElapsedTime runtime = new ElapsedTime();
    CameraBlinker light;

    @Override
    public void init() {
        light = new CameraBlinker();
    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        if(gamepad1.a) {
            light.on();
        }
        if(gamepad1.b) {
            light.off();
        }
    }

    private double absMax(double a, double b) { //Returns the argument whose absolute value is greater (similar to Math.max() but compares absolute values)
        return (Math.abs(a) > Math.abs(b)) ? a : b;
    }

    /**
     * Tells the thread to wait for a number of seconds. Exception-protected
     *
     * @param sec number of sec to wait for
     */
    private void waitFor(double sec) {
        try {
            Thread.sleep((long) (sec * 1000));
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}

