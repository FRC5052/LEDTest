// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.function.DoubleFunction;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AddressableLEDSubsystem extends SubsystemBase {
  private final AddressableLED led;
  private final AddressableLEDBuffer ledBuffer;
  private final Color colorCorrection = new Color(1.0, 1.0, 1.0);

  public static class AddressableLEDSlice {
    private final AddressableLEDSubsystem parent;
    private int offset;
    private int length;

    private AddressableLEDSlice(AddressableLEDSubsystem parent, int offset, int length) {
      this.parent = parent;
      this.offset = offset;
      this.length = length;
    }

    public int getOffset() {
      return this.offset;
    }

    public void setOffset(int offset) {
      // if (length+offset > parent.length()) {
      //   throw new IndexOutOfBoundsException();
      // }
      this.offset = offset;
    }

    public int getLength() {
      return this.length;
    }

    public void setLength(int length) {
      // if (length+offset > parent.length()) {
      //   throw new IndexOutOfBoundsException();
      // }
      this.length = length;
    }

    public void fill(Color color) {
      for (int i = offset; i < length+offset; i++) {
        parent.set(i, color);
      }
    }
    
    public void setFunc(DoubleFunction<Color> func) {
      for (int i = offset; i < length+offset; i++) {
        double value = (((double)(i-offset))/(double)length);
        parent.set(i, func.apply(value));
      }
    }

    public void setHueSine(Timer timer, int hue) {
      this.setFunc((double value) -> {
        return Color.fromHSV(hue, 255, (int)((Math.sin((value + (timer.get() / 5))*Math.PI*2*5)+1)*127));
      });
    }

    public void setDoubleRGBSine(Timer timer, double speed, double frequency, Color color1, Color color2) {
      this.setFunc((double value) -> {
        double mult = (Math.sin((value + (timer.get() / speed))*Math.PI*2*frequency)+1)*0.5;
        return new Color(
          (color1.red*mult)+(color2.red*(1-mult)), 
          (color1.green*mult)+(color2.green*(1-mult)), 
          (color1.blue*mult)+(color2.blue*(1-mult))
        );
      });
    }

    public void setMeter(double threshold, Color onColor, Color offColor) {
      this.setFunc((double value) -> {
        if (value >= threshold) {
          return offColor;
        } else {
          return onColor;
        }
      });
    }

    public void setMeter(double total, double position, Color onColor, Color offColor) {
      this.setMeter(position/total, onColor, offColor);
    }

    public void setMeter(int total, int position, Color onColor, Color offColor) {
      this.setMeter((double)total, (double)position, onColor, offColor);
    }

    public void setSpooky(Timer timer) {
      this.setHueSine(timer, 4);
    }

    public void setRainbow(Timer timer) {
      this.setFunc((double value) -> {
        return Color.fromHSV((int)(((value + (timer.get() / 5)) % 1) * 180), 255, (int)((Math.sin((value + (timer.get() / 3))*Math.PI*2*5)+1)*127));
      });
    }
    public void setColor(double r, double g, double b) {
      this.setFunc((double value) -> {
        return new Color(r,g,b);      
      });
    }
  }

  public AddressableLEDSubsystem(int port, int length) {
    // PWM Port of LED
    led = new AddressableLED(port);

    // Length is expensive to set, so only set it once, then just update data
    ledBuffer = new AddressableLEDBuffer(length);
    led.setLength(ledBuffer.getLength());

    led.start();
  }

  private void set(int index, Color color) {
    this.ledBuffer.setRGB(
      index, 
      (int)(Math.min(color.red*colorCorrection.red, 1.0) * 255), 
      (int)(Math.min(color.green*colorCorrection.green, 1.0) * 255), 
      (int)(Math.min(color.blue*colorCorrection.blue, 1.0) * 255)
    );
  }

  public AddressableLEDSlice createSlice(int offset, int length) {
    return new AddressableLEDSlice(this, offset, length);
  }

  public AddressableLEDSlice createSlice() {
    return this.createSlice(0, this.length());
  }

  public void display() {
    this.led.setData(this.ledBuffer);
  }

  public int length() {
    return this.ledBuffer.getLength();
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
