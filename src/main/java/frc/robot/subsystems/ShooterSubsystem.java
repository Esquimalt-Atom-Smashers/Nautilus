package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {

    private static final double LOW_SHOOT_SPEED = 0.4;
    private static final double MEDIUM_SHOOT_SPEED = 0.6;
    private static final double HIGH_SHOOT_SPEED = 0.8;

    private final CANSparkMax frontShooter = new CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax rearShooter = new CANSparkMax(10, CANSparkMaxLowLevel.MotorType.kBrushless);

    public ShooterSubsystem() {

    }

    public void shoot(double power) {
        frontShooter.set(power);
        rearShooter.set(-power);
    }

    public CommandBase shootStop() {
        return this.runOnce(() -> {
            frontShooter.set(0);
            rearShooter.set(0);
        });
    }

    public CommandBase lowShoot() {
        return this.runOnce(() -> {
            frontShooter.set(LOW_SHOOT_SPEED);
            rearShooter.set(-LOW_SHOOT_SPEED);
        });
    }
    public CommandBase mediumShoot() {
        return this.runOnce(() -> {
            frontShooter.set(MEDIUM_SHOOT_SPEED);
            rearShooter.set(-MEDIUM_SHOOT_SPEED);
        });
    }
    public CommandBase highShoot() {
        return this.runOnce(() -> {
            frontShooter.set(HIGH_SHOOT_SPEED);
            rearShooter.set(-HIGH_SHOOT_SPEED);
        });
    }

}