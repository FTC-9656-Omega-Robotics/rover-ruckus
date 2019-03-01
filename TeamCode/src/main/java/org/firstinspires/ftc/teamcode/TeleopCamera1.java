package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import android.hardware.Camera.Parameters;
import android.hardware.Camera;

import edu.spa.ftclib.internal.state.ToggleBoolean;

/**
 *
 */

@TeleOp(name = "TeleopCamera1", group = "prototype")

public class TeleopCamera1 extends OpMode {

    private OmegaBot robot;

    private ToggleBoolean gamepad2LeftBumper = new ToggleBoolean();
    private ToggleBoolean gamepad2RightBumper = new ToggleBoolean();
    private ToggleBoolean gamepad2LeftTrigger = new ToggleBoolean();
    private ToggleBoolean gamepad2RightTrigger = new ToggleBoolean();

    private double speedDamper = 0.4;
    private ElapsedTime runtime = new ElapsedTime();
    Camera camera;

    @Override
    public void init() {

    }

    /**
     * In teleop mode, the robot is continually checking for input from the user.
     */
    @Override
    public void loop() {
        if (gamepad1.a) {
            camera = Camera.open();
            Parameters p = camera.getParameters();
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(p);
            camera.startPreview();
        }
        if (gamepad1.b) {
            camera = Camera.open();
            Parameters p = camera.getParameters();
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
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

