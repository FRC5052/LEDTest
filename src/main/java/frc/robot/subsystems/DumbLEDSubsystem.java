// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DumbLEDSubsystem extends SubsystemBase {
  PWM red;
  PWM green;
  PWM blue;
  /** Creates a new ExampleSubsystem. */
  public DumbLEDSubsystem() {
    red = new PWM(0);
    green = new PWM(1);
    blue = new PWM(2);
    red.setPeriodMultiplier(PeriodMultiplier.k1X);
    green.setPeriodMultiplier(PeriodMultiplier.k1X);
    blue.setPeriodMultiplier(PeriodMultiplier.k1X);
  }

  public void setAllLEDs(double r, double g, double b) {
    
    // if (b < 0.18) {
    //   blue.setAlwaysHighMode();
    // } else {
    //   blue.setPulseTimeMicroseconds((int) MathUtil.applyDeadband(b, 0.18));
    // }
    // green.setPulseTimeMicroseconds((int) MathUtil.applyDeadband(g, 0.18));
    red.setPulseTimeMicroseconds((int) ((1.0-r) * 4095));
    int gv = (int) ((1.0-g) * 1.212 * 4095.0);
    if (gv >= 4046) gv = 0xFFFF;
    green.setPulseTimeMicroseconds(gv);
    // green.setPulseTimeMicroseconds(0xFFFF);
    blue.setPulseTimeMicroseconds((int) ((1.0-b) * 4095));
    System.out.println(red.getPulseTimeMicroseconds());
    //System.out.println(green.getPulseTimeMicroseconds());
  } 
  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
