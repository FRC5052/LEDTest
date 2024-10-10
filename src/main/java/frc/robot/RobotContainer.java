// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.AddressableLEDSubsystem.AddressableLEDSlice;
import frc.robot.subsystems.AddressableLEDSubsystem;
import frc.robot.subsystems.DumbLEDSubsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  
  private final AddressableLEDSubsystem m_ledSubsystem = new AddressableLEDSubsystem(0, 300);
  // private final DumbLEDSubsystem m_ledSubsystem = new DumbLEDSubsystem();
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.

    Command controllerCommand = new Command() {
      private AddressableLEDSlice firstHalf = m_ledSubsystem.createSlice(0, 150);
      private AddressableLEDSlice secondHalf = m_ledSubsystem.createSlice(150, 150);
      private Timer timer = new Timer();

      @Override
      public void initialize() {
        timer.start();
      }

      @Override
      public void execute() {
        int partition = (int)(((Math.sin(timer.get()*Math.PI*0.25) * 0.25) + 0.5) * (double)m_ledSubsystem.length());
        firstHalf.setLength(partition);
        secondHalf.setLength(m_ledSubsystem.length()-partition);
        secondHalf.setOffset(partition);
        firstHalf.setDoubleRGBSine(timer, 5, 10, Color.fromHSV(4, 255, 255), Color.fromHSV(60, 255, 255));
        secondHalf.setRainbow(timer);
        m_ledSubsystem.display();
        
        // m_ledSubsystem.setDoubleRGBSine(5, 5, Color.fromHSV(4, 255, 255),  Color.fromHSV(60, 255, 255));
        // m_ledSubsystem.setAllLEDs(0,(int)(m_driverController.getRightTriggerAxis()*100), (int)(m_driverController.getLeftTriggerAxis()*100));
      };
    };
    controllerCommand.addRequirements(m_ledSubsystem);
    m_ledSubsystem.setDefaultCommand(controllerCommand);
    // m_driverController.a().onTrue(new InstantCommand(() -> m_ledSubsystem.setAllLEDs(255,0,0)));
    // m_driverController.b().onTrue(new InstantCommand(() -> m_ledSubsystem.setAllLEDs(0, 255, 0)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
