package com.FTC3486.FTCRC_Extensions;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Matthew on 2/20/2016.
 */
public class GyroscopeAutoDriver extends AutoDriver {
    private GyroSensor gyroSensor;

    public GyroscopeAutoDriver(LinearOpMode linearOpMode, DriveTrain driveTrain, String gyroSensor,
                               HardwareMap hardwaremap) {
        super(linearOpMode, driveTrain);
        this.gyroSensor = hardwaremap.gyroSensor.get(gyroSensor);
    }

    private int get_gyro_corrected_heading(int degrees) {
        return degrees % 360;
    }

    private boolean gyro_is_between(int lowerBound, int upperBound) {
        lowerBound = get_gyro_corrected_heading(lowerBound);
        upperBound = get_gyro_corrected_heading(upperBound);
        int heading = gyroSensor.getHeading();

        if (lowerBound <= upperBound) {
            return heading >= lowerBound && heading <= upperBound;
        } else {
            return (heading >= lowerBound && heading <= 359) ||
                    (heading >= 0 && heading <= upperBound);
        }
    }

    @Override
    public AutoDriver drive_forward(int encoderCounts) {
        driveTrain.resetMotorEncoders();
        gyroSensor.resetZAxisIntegrator();

        while (driveTrain.getLeftEncoderCount() < encoderCounts &&
               driveTrain.getRightEncoderCount() < encoderCounts &&
               opMode.opModeIsActive()) {
            //opMode.telemetry.addData("Gyro Heading", gyroSensor.getHeading());
            if (gyro_is_between(180, 357)) {
                driveTrain.setPowers(power, power - (0.75 * power));
            } else if (gyro_is_between(2, 179)) {
                driveTrain.setPowers(power - (0.75 * power), power);
            } else {
                driveTrain.setPowers(power, power);
            }

        }
        driveTrain.haltDrive();

        return this;
    }

    @Override
    public AutoDriver drive_backward(int encoderCounts) {
        driveTrain.resetMotorEncoders();
        gyroSensor.resetZAxisIntegrator();

        while (driveTrain.getLeftEncoderCount() > encoderCounts &&
                driveTrain.getRightEncoderCount() > encoderCounts &&
                opMode.opModeIsActive()) {
            //opMode.telemetry.addData("Gyro Heading", gyroSensor.getHeading());
            if (gyro_is_between(180, 357)) {
                driveTrain.setPowers(-power + (0.75 * power), -power );
            } else if (gyro_is_between(2, 179)) {
                driveTrain.setPowers(-power, -power + (0.75 * power));
            } else {
                driveTrain.setPowers(-power, -power);
            }
        }
        driveTrain.haltDrive();

        return null;
    }

    @Override
    public AutoDriver turn_clockwise(int degrees) {
        gyroSensor.resetZAxisIntegrator();

        driveTrain.setPowers(power, -power);
        while(gyro_is_between(-10, degrees) && opMode.opModeIsActive()) {
            //opMode.telemetry.addData("Gyro Heading", gyroSensor.getHeading());
        }

        driveTrain.setPowers(-power/2, power/2);
        while(gyro_is_between(degrees+5, degrees-10) && opMode.opModeIsActive()) {
            //opMode.telemetry.addData("Gyro Heading", gyroSensor.getHeading());
        }
        driveTrain.haltDrive();

        return this;
    }

    @Override
    public AutoDriver turn_counterclockwise(int degrees) {
        gyroSensor.resetZAxisIntegrator();

        driveTrain.setPowers(-power, power);
        while(gyro_is_between(10, degrees) && opMode.opModeIsActive()) {
            //opMode.telemetry.addData("Gyro Heading", gyroSensor.getHeading());
        }

        driveTrain.setPowers(power/2, -power/2);
        while(gyro_is_between(degrees-5, degrees+10) && opMode.opModeIsActive()) {
            //opMode.telemetry.addData("Gyro Heading", gyroSensor.getHeading());
        }
        driveTrain.haltDrive();

        return this;
    }
}

