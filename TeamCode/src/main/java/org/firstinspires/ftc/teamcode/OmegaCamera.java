package org.firstinspires.ftc.teamcode;
import android.hardware.Camera.Parameters;
import android.hardware.Camera;

/**
 * Class to turn on RC phone's light on or off.
 */
public class OmegaCamera {
    Camera camera;
    public OmegaCamera() {

    }

    public void flashOn() {
        camera = Camera.open();
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
    }

    public void flashOff() {
        camera = Camera.open();
        Parameters p = camera.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
    }
}
