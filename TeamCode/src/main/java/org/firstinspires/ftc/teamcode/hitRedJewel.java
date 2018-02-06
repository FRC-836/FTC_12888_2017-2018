package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by user on 2/5/2018.
 */
@Autonomous(name = "hit red", group = "")
public class hitRedJewel extends Autonomous_Parent{
    @Override
    public void runAutonomous()
    {
        grab();
        sleep(1000);
        holdCube();

        telemetry.addData("Status","Started");
        telemetry.update();

        lift(0.8);
        sleep(300);
        lift(0.2);
        
        hitJewel(false);
    }
}
