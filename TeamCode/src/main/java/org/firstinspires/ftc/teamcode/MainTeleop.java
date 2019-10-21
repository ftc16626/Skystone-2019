package org.firstinspires.ftc.teamcode;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;

@TeleOp(name = "Main TeleOp", group = "Mecanum")
public class MainTeleop extends OpMode implements GamepadListener {

  private MainHardware robot;
  private DriverInterface driverInterface;

  private MissionControl missionControl;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    robot = new MainHardware(hardwareMap);
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);
    driverInterface.driver.setProfile(missionControl.getPilotProfileHandler().getCurrentProfile());

    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    driverInterface.update();

    double magnitude = driverInterface.driver.getStrafeStickMagnitude();
    double angle = driverInterface.driver.getStrafeStickAngle();
    double turn = driverInterface.driver.getTurnStickX();

    robot.drive.setAngle(angle);
    robot.drive.setPower(magnitude);
    robot.drive.setTurn(turn);

    robot.update();

    telemetry.addData("Current Profile", driverInterface.driver.getProfile());
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.DRIVER) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case START:
            missionControl.getPilotProfileHandler().incremementPosition();
            driverInterface.driver
                .setProfile(missionControl.getPilotProfileHandler().getCurrentProfile());
            break;
        }
      }
    }
  }
}
