package org.firstinspires.ftc.teamcode;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.subsystem.system.SubsystemDriverControl;

@TeleOp(name = "Main TeleOp", group = "Mecanum")
public class MainTeleop extends RadicalOpMode {
  @Override
  public void overridenInit() {
    Subsystem driverControl = new SubsystemDriverControl(robot, this);

    subsystemHandler.add(driverControl);
  }
}
