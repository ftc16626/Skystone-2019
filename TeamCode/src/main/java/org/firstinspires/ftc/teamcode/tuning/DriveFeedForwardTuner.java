package org.firstinspires.ftc.teamcode.tuning;

import static org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants.rpmToVelocity;

import android.util.Log;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.tuning.AccelRegression;
import com.acmerobotics.roadrunner.tuning.RampRegression;
import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.util.LoggingUtil;

@Config
@Autonomous(name="Tuning - Feed Forward", group = "tuning")
public class DriveFeedForwardTuner extends LinearOpMode {
  public static final double MAX_POWER = 0.7;
  public static final double DISTANCE = 120;

  @Override
  public void runOpMode() throws InterruptedException {
//    if (DriveConstants.RUN_USING_ENCODER) {
//      RobotLog.setGlobalErrorMsg("Feedforward constants usually don't need to be tuned " +
//          "when using the built-in drive motor velocity PID.");
//    }

//    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    NanoClock clock = NanoClock.system();

    telemetry.addLine("Press play to begin the feedforward tuning routine");
    telemetry.update();

    Log.i("TEST", "1");

    waitForStart();

    Log.i("TEST", "2");

    if (isStopRequested()) return;

    Log.i("TEST", "3");

    telemetry.clearAll();
    Log.i("TEST", "4");
    telemetry.addLine("Would you like to fit kStatic?");
    telemetry.addLine("Press (A) for yes, (B) for no");
    telemetry.update();
    Log.i("TEST", "5");

    boolean fitIntercept = false;
    while (!isStopRequested()) {
      if (gamepad1.a) {
        fitIntercept = true;
        while (!isStopRequested() && gamepad1.a) {
          idle();
        }
        break;
      } else if (gamepad1.b) {
        while (!isStopRequested() && gamepad1.b) {
          idle();
        }
        break;
      }
      idle();
    }

    telemetry.clearAll();
    telemetry.addLine(Misc.formatInvariant(
        "Place your robot on the field with at least %.2f in of room in front", DISTANCE));
    telemetry.addLine("Press (A) to begin");
    telemetry.update();

    while (!isStopRequested() && !gamepad1.a) {
      idle();
    }
    while (!isStopRequested() && gamepad1.a) {
      idle();
    }

    telemetry.clearAll();
    telemetry.addLine("Running...");
    telemetry.update();

    double maxVel = rpmToVelocity(DriveConstants.getMaxRpm());
    double finalVel = MAX_POWER * maxVel;
    double accel = (finalVel * finalVel) / (2.0 * DISTANCE);
    double rampTime = Math.sqrt(2.0 * DISTANCE / accel);

    double startTime = clock.seconds();
    RampRegression rampRegression = new RampRegression();

    drive.setPoseEstimate(new Pose2d());
    while (!isStopRequested()) {
      double elapsedTime = clock.seconds() - startTime;
      if (elapsedTime > rampTime) {
        break;
      }
      double vel = accel * elapsedTime;
      double power = vel / maxVel;

      rampRegression.add(elapsedTime, drive.getPoseEstimate().getX(), power);

      drive.setDrivePower(new Pose2d(power, 0.0, 0.0));
      drive.updatePoseEstimate();
    }
    drive.setDrivePower(new Pose2d(0.0, 0.0, 0.0));

    RampRegression.RampResult rampResult = rampRegression.fit(fitIntercept);

    rampRegression.save(LoggingUtil.getLogFile(Misc.formatInvariant(
        "DriveRampRegression-%d.csv", System.currentTimeMillis())));

    telemetry.clearAll();
    telemetry.addLine("Quasi-static ramp up test complete");
    if (fitIntercept) {
      telemetry.addLine(Misc.formatInvariant("kV = %.5f, kStatic = %.5f (R^2 = %.2f)",
          rampResult.kV, rampResult.kStatic, rampResult.rSquare));
    } else {
      telemetry.addLine(Misc.formatInvariant("kV = %.5f (R^2 = %.2f)",
          rampResult.kStatic, rampResult.rSquare));
    }
    telemetry.addLine("Would you like to fit kA?");
    telemetry.addLine("Press (A) for yes, (B) for no");
    telemetry.update();

    boolean fitAccelFF = false;
    while (!isStopRequested()) {
      if (gamepad1.a) {
        fitAccelFF = true;
        while (!isStopRequested() && gamepad1.a) {
          idle();
        }
        break;
      } else if (gamepad1.b) {
        while (!isStopRequested() && gamepad1.b) {
          idle();
        }
        break;
      }
      idle();
    }

    if (fitAccelFF) {
      telemetry.clearAll();
      telemetry.addLine("Place the robot back in its starting position");
      telemetry.addLine("Press (A) to continue");
      telemetry.update();

      while (!isStopRequested() && !gamepad1.a) {
        idle();
      }
      while (!isStopRequested() && gamepad1.a) {
        idle();
      }

      telemetry.clearAll();
      telemetry.addLine("Running...");
      telemetry.update();

      double maxPowerTime = DISTANCE / maxVel;

      startTime = clock.seconds();
      AccelRegression accelRegression = new AccelRegression();

      drive.setPoseEstimate(new Pose2d());
      drive.setDrivePower(new Pose2d(MAX_POWER, 0.0, 0.0));
      while (!isStopRequested()) {
        double elapsedTime = clock.seconds() - startTime;
        if (elapsedTime > maxPowerTime) {
          break;
        }

        accelRegression.add(elapsedTime, drive.getPoseEstimate().getX(), MAX_POWER);

        drive.updatePoseEstimate();
      }
      drive.setDrivePower(new Pose2d(0.0, 0.0, 0.0));

      AccelRegression.AccelResult accelResult = accelRegression.fit(
          rampResult.kV, rampResult.kStatic);

      accelRegression.save(LoggingUtil.getLogFile(Misc.formatInvariant(
          "DriveAccelRegression-%d.csv", System.currentTimeMillis())));

      telemetry.clearAll();
      telemetry.addLine("Constant power test complete");
      telemetry.addLine(Misc.formatInvariant("kA = %.5f (R^2 = %.2f)",
          accelResult.kA, accelResult.rSquare));
      telemetry.update();
    }

    while (!isStopRequested()) {
      idle();
    }
  }
}