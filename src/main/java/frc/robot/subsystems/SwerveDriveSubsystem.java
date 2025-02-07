package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.swervedrivespecialties.swervelib.Mk4SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDriveSubsystem extends SubsystemBase {

    public static boolean FIELD_CENTRIC = true;

    private SwerveModule frontRightModule;
    private SwerveModule frontLeftModule;
    private SwerveModule rearRightModule;
    private SwerveModule rearLeftModule;

    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            new Translation2d(0.262, 0.325), //13.5 to meter 
            new Translation2d(0.262, -0.325),
            new Translation2d(-0.262, 0.325),
            new Translation2d(-0.262, -0.325)
    );

    private AHRS gyro;

    public SwerveDriveSubsystem() {
        frontRightModule = Mk4SwerveModuleHelper.createNeo(Mk4SwerveModuleHelper.GearRatio.L2, 1, 2, 11, 0);
        frontLeftModule = Mk4SwerveModuleHelper.createNeo(Mk4SwerveModuleHelper.GearRatio.L2, 5, 6, 12, 0);
        rearRightModule = Mk4SwerveModuleHelper.createNeo(Mk4SwerveModuleHelper.GearRatio.L2, 4, 3, 13, 0);
        rearLeftModule = Mk4SwerveModuleHelper.createNeo(Mk4SwerveModuleHelper.GearRatio.L2, 8, 7, 14, 0);
        
        gyro = new AHRS(SPI.Port.kMXP);
        // Calibrates, zeros yaw
        gyro.reset();
    }

    public void drive(double forward, double sideways, double angular) {
        // sideways *= -1;
        ChassisSpeeds chassis;
        if (FIELD_CENTRIC) {
            chassis = ChassisSpeeds.fromFieldRelativeSpeeds(
                    forward * 6,
                    sideways * 6,
                    angular * 7,
                    Rotation2d.fromDegrees(gyro.getYaw()*-1)
            );
        } else {
            chassis = new ChassisSpeeds(forward * 4, sideways * 4, angular * 8);
        }
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(chassis);

        frontLeftModule.set(states[0].speedMetersPerSecond, states[0].angle.getRadians() + Math.toRadians(213));
        frontRightModule.set(states[1].speedMetersPerSecond, states[1].angle.getRadians());
        rearLeftModule.set(states[2].speedMetersPerSecond, states[2].angle.getRadians() + Math.toRadians(39));
        rearRightModule.set(states[3].speedMetersPerSecond, states[3].angle.getRadians() + Math.toRadians(141));
    }

    public void reset() {
        gyro.reset();
    }

    public AHRS getGyro() {
        return gyro;
    }

    enum DistanceUnit {
        CENTIMETRES(-1),
        INCHES(-1);

        private double conversionFactor;

        private DistanceUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double toUnit(double value) {
            return value * conversionFactor;
        }

    }

}
