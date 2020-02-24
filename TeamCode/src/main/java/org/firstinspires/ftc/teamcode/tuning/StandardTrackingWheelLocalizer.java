package org.firstinspires.ftc.teamcode.tuning;

import android.support.annotation.NonNull;
import android.util.Log;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Arrays;
import java.util.List;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

@Config
public class StandardTrackingWheelLocalizer extends RadicalThreeTrackingWheelLocalizer {

  public static double TICKS_PER_REV = 720;
  public static double WHEEL_RADIUS = 0.75; // in
  public static double GEAR_RATIO = 0.252476537 * 1.011824101657394
      * 0.9782104794947832; // output (wheel) speed / input (encoder) speed

//  public static double LATERAL_DISTANCE = 10 * 1.5864525889819947
//      * 1.0117341199218144 * (0.9872802886455438) * 0.9955892823350541; // in; distance between the left and right wheels
//  public static double FORWARD_OFFSET = 4; // in; offset of the lateral wheel

  public static double LATERAL_DISTANCE = 16 * 0.989908320012592;
  public static double FORWARD_OFFSET = -3.25 * 0.98; // in; offset of the lateral wheel


  public static double MULTIPLIER_X = 1.003821352575286 * 1.00206844852084;
  public static double MULTIPLIER_Y = 0.99447513812154;

  private DcMotor leftEncoder, rightEncoder, frontEncoder;

  private ExpansionHubEx hub;

  public StandardTrackingWheelLocalizer(HardwareMap hardwareMap) {
    super(Arrays.asList(
        new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
        new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
        new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
    ));

    hub = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 4 ");

    leftEncoder = hardwareMap.dcMotor.get("motorIntakeLeftAndEncoderLeft");
    rightEncoder = hardwareMap.dcMotor.get("motorIntakeRightAndEncoderRight");
    frontEncoder = hardwareMap.dcMotor.get("motorLiftTopAndEncoderMiddle");

    frontEncoder.setDirection(Direction.REVERSE);

    leftEncoder.setDirection(Direction.REVERSE);
  }

  public static double encoderTicksToInches(int ticks) {
    return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
  }

  @NonNull
  @Override
  public List<Double> getWheelPositions() {
    RevBulkData bulkData = hub.getBulkInputData();

//    TelemetryPacket packet = new TelemetryPacket();
//    packet.put("left", bulkData.getMotorCurrentPosition(leftEncoder));
//    FtcDashboard.getInstance().sendTelemetryPacket(packet);

    Log.i("leftenc", Double.toString(bulkData.getMotorCurrentPosition(leftEncoder)));
    Log.i("rightenc", Double.toString(bulkData.getMotorCurrentPosition(rightEncoder)));
    Log.i("middleenc", Double.toString(bulkData.getMotorCurrentPosition(frontEncoder)));
    Log.i("enc", "----------------");

    TelemetryPacket packet = new TelemetryPacket();

    packet.put("left", Double.toString(bulkData.getMotorCurrentPosition(leftEncoder)));
    packet.put("right", Double.toString(bulkData.getMotorCurrentPosition(rightEncoder)));
    packet.put("middle", Double.toString(bulkData.getMotorCurrentPosition(frontEncoder)));

//    FtcDashboard.getInstance().sendTelemetryPacket(packet);

    return Arrays.asList(
        encoderTicksToInches(bulkData.getMotorCurrentPosition(leftEncoder)) * MULTIPLIER_X,
        encoderTicksToInches(bulkData.getMotorCurrentPosition(rightEncoder)) * MULTIPLIER_X,
        encoderTicksToInches(bulkData.getMotorCurrentPosition(frontEncoder)) * MULTIPLIER_Y
    );
  }
}