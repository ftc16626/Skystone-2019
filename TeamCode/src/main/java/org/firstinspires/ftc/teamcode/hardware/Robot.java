package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.RevBulkData;

public class Robot extends Subsystem {
  ExpansionHubEx expansionHubMother;
  ExpansionHubEx expansionHubDaughter;

  RevBulkData bulkDataMother = null;
  RevBulkData bulkDataDaughter = null;

  private boolean bulkDataUpdatedThisCycleMother = false;
  private boolean bulkDataUpdatedThisCycleDaughter = false;

  public HardwareMap hwMap;

  public SubsystemDriveTrainMecanum subsystemDriveTrainMecanum;
  public SubsystemIntake subsystemIntake;
  public SubsystemFoundationGrabber subsystemFoundationGrabber;
  public SubsystemIMU subsystemIMU;

  public Robot(HardwareMap hwMap, RadicalOpMode opMode) {
    this.hwMap = hwMap;

    expansionHubMother = hwMap.get(ExpansionHubEx.class, "Expansion Hub 9");
    expansionHubDaughter = hwMap.get(ExpansionHubEx.class, "Expansion Hub 2");

    subsystemDriveTrainMecanum = new SubsystemDriveTrainMecanum(this, opMode);
    subsystemIntake = new SubsystemIntake(this, opMode);
    subsystemFoundationGrabber = new SubsystemFoundationGrabber(this, opMode);
    subsystemIMU = new SubsystemIMU(this, opMode);

    // Keep the IMU off by default to avoid 'problem with imu' errors
    subsystemIMU.turnOff();

    getSubsystemHandler().add(subsystemDriveTrainMecanum);
    getSubsystemHandler().add(subsystemIntake);
    getSubsystemHandler().add(subsystemFoundationGrabber);
    getSubsystemHandler().add(subsystemIMU);
  }

  @Override
  public void update() {

  }

  public RevBulkData getBulkDataMother() {
    if(!bulkDataUpdatedThisCycleMother) {
      bulkDataMother = expansionHubMother.getBulkInputData();
      bulkDataUpdatedThisCycleMother = true;
    }

    return bulkDataMother;
  }

  public RevBulkData getBulkDataDaughter() {
    if(!bulkDataUpdatedThisCycleDaughter) {
      bulkDataDaughter = expansionHubDaughter.getBulkInputData();
      bulkDataUpdatedThisCycleDaughter = true;
    }

    return bulkDataDaughter;
  }
}
