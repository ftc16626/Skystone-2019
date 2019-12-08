package org.firstinspires.ftc.teamcode.tuning;

import android.util.Log;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.MovingStatistics;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.DriveConstants;

@Autonomous(name="Tuning - Track Width Tuner", group="tuning")
public class TrackWidthTuner extends LinearOpMode {

  public static double ANGLE = 180; // deg
  public static int NUM_TRIALS = 5;
  public static int DELAY = 1000; // ms

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    telemetry.addLine("Press play to begin the track width tuner routine");
    telemetry.addLine("Make sure your robot has enough clearance to turn smoothly");
    telemetry.update();

    waitForStart();

    if (isStopRequested()) return;

    telemetry.clearAll();
    telemetry.addLine("Running...");
    telemetry.update();

    MovingStatistics trackWidthStats = new MovingStatistics(NUM_TRIALS);
    for (int i = 0; i < NUM_TRIALS; i++) {
      drive.setPoseEstimate(new Pose2d());

      // it is important to handle heading wraparounds
      double headingAccumulator = 0;
      double lastHeading = 0;

      drive.turn(Math.toRadians(ANGLE));

      while (!isStopRequested() && drive.isBusy()) {
        double heading = drive.getPoseEstimate().getHeading();
        headingAccumulator += Angle.norm(heading - lastHeading);
        lastHeading = heading;

        Log.i("HEADING", Double.toString(Math.toDegrees(heading)));

        drive.update();
      }

      double trackWidth = DriveConstants.TRACK_WIDTH * Math.toRadians(ANGLE) / headingAccumulator;
      trackWidthStats.add(trackWidth);

      sleep(DELAY);
    }

    telemetry.clearAll();
    telemetry.addLine("Tuning complete");
    telemetry.addLine(Misc.formatInvariant("Effective track width = %.2f (SE = %.3f)",
        trackWidthStats.getMean(),
        trackWidthStats.getStandardDeviation() / Math.sqrt(NUM_TRIALS)));
    telemetry.update();

    while (!isStopRequested()) {
      idle();
    }
  }
}
