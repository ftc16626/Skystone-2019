package org.firstinspires.ftc.teamcode.tuning;

import android.support.annotation.NonNull;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Arrays;
import java.util.List;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

@Config
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
  public static double TICKS_PER_REV = 720;
  public static double WHEEL_RADIUS = 1.5; // in
  public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

  public static double LATERAL_DISTANCE = 10; // in; distance between the left and right wheels
  public static double FORWARD_OFFSET = 4; // in; offset of the lateral wheel

  private DcMotor leftEncoder, rightEncoder, frontEncoder;

  private ExpansionHubEx hub;

  public StandardTrackingWheelLocalizer(HardwareMap hardwareMap) {
    super(Arrays.asList(
        new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
        new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
        new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
    ));

    hub = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 2");

    leftEncoder = hardwareMap.dcMotor.get("motorIntakeLeftAndEncoderLeft");
    rightEncoder = hardwareMap.dcMotor.get("motorIntakeRightAndEncoderRight");
    frontEncoder = hardwareMap.dcMotor.get("motorLiftTopAndEncoderMiddle");
  }

  public static double encoderTicksToInches(int ticks) {
    return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
  }

  @NonNull
  @Override
  public List<Double> getWheelPositions() {
    RevBulkData bulkData = hub.getBulkInputData();

    return Arrays.asList(
        encoderTicksToInches(bulkData.getMotorCurrentPosition(leftEncoder)),
        encoderTicksToInches(bulkData.getMotorCurrentPosition(rightEncoder)),
        encoderTicksToInches(bulkData.getMotorCurrentPosition(frontEncoder))
    );
  }
}