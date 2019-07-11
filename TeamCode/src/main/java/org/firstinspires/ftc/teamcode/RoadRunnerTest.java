package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.RamseteFollower;
import com.acmerobotics.roadrunner.followers.TankPIDVAFollower;
import com.acmerobotics.roadrunner.path.Path;
import com.acmerobotics.roadrunner.path.PathBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryGenerator;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

//me trying to implement roadrunner. not really working
public class RoadRunnerTest extends LinearOpMode {
    private OmegaBot robot;
    Path path = new PathBuilder(new Pose2d(0, 0, 0))
            .splineTo(new Pose2d(15, 15, 0))
            .lineTo(new Vector2d(30, 15))
            .build();
    DriveConstraints constraints = new DriveConstraints(20, 40, 80, 1, 2, 4);
    Trajectory traj = TrajectoryGenerator.generateTrajectory(path, constraints);
    PIDCoefficients translationalPid = new PIDCoefficients(5, 0, 0);
    PIDCoefficients headingPid = new PIDCoefficients(2, 0, 0);
    TankPIDVAFollower follower = new TankPIDVAFollower(translationalPid, headingPid);
    follower.followTrajectory(traj);
    public void runOpMode(){
        robot = new OmegaBot(telemetry, hardwareMap);
    }
}
