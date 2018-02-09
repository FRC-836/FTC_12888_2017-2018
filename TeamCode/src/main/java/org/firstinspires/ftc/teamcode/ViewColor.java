package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by user on 2/8/2018.
 */
@Autonomous(name = "View Color", group = "sensor")
public class ViewColor extends Autonomous_Parent{
    @Override
    public void runAutonomous() {
        while (true)
        {
            telemetry.addData("Color","%d",getHue(colorSensor.red(),colorSensor.green(),colorSensor.blue()));
            if (getJewelColor() == Color.BLUE)
                telemetry.addLine("Blue");
            else
                telemetry.addLine("Red");
            telemetry.update();
        }
    }
}
