package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Drive and Drop", group="Competition")
public class Relic_Recovery_Autonomous_Drive_and_Drop extends Autonomous_Parent {

    @Override
    public void runAutonomous() {
        grab();
        sleep(1000);
        holdCube();

        lift(0.8);
        sleep(300);
        lift(0.2);

        sleep(1000);
        moveStraightTime(0.5,1500);
        sleep(1000);

        lift(-0.1);
        moveStraightTime(0.3,1000);
        lift(0.0);

        drop();
        moveStraightTime(-0.3, 500);
        stopIntake();
        sleep(1000);
        pushCube();
    }
}
