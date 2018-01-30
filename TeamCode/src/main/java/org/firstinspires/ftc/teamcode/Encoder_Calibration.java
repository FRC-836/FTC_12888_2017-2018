package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Encoder Calibration", group = "Sensor Setup")
public class Encoder_Calibration extends Autonomous_Parent {

    @Override
    public void runAutonomous() {
        int start = backLeftDrive.getCurrentPosition();
        moveStraightTime(ENCODER_DRIVE_POWER, 3000);
        sleep(1000);
        telemetry.addData("End Displacement","%d",backLeftDrive.getCurrentPosition() - start);
        telemetry.update();
        sleep(10000);
    }
}
