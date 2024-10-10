// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AddressableLEDSubsystem extends SubsystemBase {
  AddressableLED led;
  AddressableLEDBuffer ledBuffer;
  Timer timer;

  public AddressableLEDSubsystem() {
    // PWM Port of LED
    led = new AddressableLED(0);

    // Length is expensive to set, so only set it once, then just update data
    ledBuffer = new AddressableLEDBuffer(300);
    led.setLength(ledBuffer.getLength());

    // Set the data
    led.setData(ledBuffer);
    led.start();

    timer = new Timer();
    timer.start();
  }

  public void setAllLEDs(int r, int g, int b) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setRGB(i, r, g, b);
    }
    led.setData(ledBuffer);
  } 

  public void setEven(int r, int g, int b) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      ledBuffer.setRGB(i, r, g, b);
    }
    led.setData(ledBuffer);
  } 

  public void setHueSine(int hue) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      double value = (((double)i)/(double)ledBuffer.getLength());
      ledBuffer.setHSV(i, hue, 255, (int)((Math.sin((value + (timer.get() / 5))*Math.PI*2*5)+1)*127));
    }
    led.setData(ledBuffer);
  }

  public void setDoubleRGBSine(double speed, double frequency, Color color1, Color color2) {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      double value = (((double)i)/(double)ledBuffer.getLength());
      double mult = (Math.sin((value + (timer.get() / speed))*Math.PI*2*frequency)+1)*0.5;
      ledBuffer.setLED(i, new Color(
        (color1.red*mult)+(color2.red*(1-mult)), 
        (color1.green*mult)+(color2.green*(1-mult)), 
        (color1.blue*mult)+(color2.blue*(1-mult))
      ));
    }
    led.setData(ledBuffer);
  }

  public void setSpooky() {
    setHueSine(4);
  }

  public void setWomensHistoryMonth() {
    setHueSine(175);
  }

  public void setLGBTQ() {
    for (int i = 0; i < ledBuffer.getLength(); i++) {
      double value = (((double)i)/(double)ledBuffer.getLength());
      ledBuffer.setHSV(i, (int)(((value + (timer.get() / 5)) % 1) * 180), 255, (int)((Math.sin((value + (timer.get() / 3))*Math.PI*2*5)+1)*127));
    }
    led.setData(ledBuffer);
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
