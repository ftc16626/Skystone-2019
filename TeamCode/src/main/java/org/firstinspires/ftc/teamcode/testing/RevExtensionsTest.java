package org.firstinspires.ftc.teamcode.testing;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

@TeleOp(name = "Rev Extensions Test", group = "Testing")
public class RevExtensionsTest extends OpMode {

  RevBulkData bulkData;
  ExpansionHubEx expansionHub;
  ExpansionHubMotor motor0, motor1, motor2, motor3;

  int lastMotor3 = 0;

  @Override
  public void init() {
    expansionHub = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 9");
    motor0 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motorFrontLeft");
    motor1 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motorFrontRight");
    motor2 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motorBackLeft");
    motor3 = (ExpansionHubMotor) hardwareMap.dcMotor.get("motorBackRight");
  }

  @Override
  public void loop() {
    bulkData = expansionHub.getBulkInputData();

    telemetry.addLine("------------");
    telemetry.addData("M0 Encoder", bulkData.getMotorCurrentPosition(motor0));
    telemetry.addData("M1 Encoder", bulkData.getMotorCurrentPosition(motor1));
    telemetry.addData("M2 Encoder", bulkData.getMotorCurrentPosition(motor2));
    telemetry.addData("M3 Encoder", bulkData.getMotorCurrentPosition(motor3));
    telemetry.addData("M0 Vel", bulkData.getMotorVelocity(motor0));
    telemetry.addData("M1 Vel", bulkData.getMotorVelocity(motor1));
    telemetry.addData("M2 Vel", bulkData.getMotorVelocity(motor2));
    telemetry.addData("M3 Vel", bulkData.getMotorVelocity(motor3));

    telemetry.addData("Manual vel", bulkData.getMotorCurrentPosition(motor3) - lastMotor3);
    lastMotor3 = bulkData.getMotorCurrentPosition(motor3);
  }
}
