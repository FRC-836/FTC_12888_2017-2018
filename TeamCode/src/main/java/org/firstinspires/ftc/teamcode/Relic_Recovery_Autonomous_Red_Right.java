package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Right", group="Competition")
public class Relic_Recovery_Autonomous_Red_Right extends Autonomous_Parent {

    @Override
    public void runAutonomous() {
        grab();
        sleep(1000);

        telemetry.addData("Status","Started");
        telemetry.update();

        // Read the pictograph
        cryptoboxKey = getPictographKey();

        hitJewel(true);

        lift(0.8);
        sleep(300);
        lift(0.2);

        switch(cryptoboxKey)
        {
            case LEFT:
                telemetry.addLine("Left Column");
                telemetry.update();
                moveStraightEncoder(1.95, 4.0);
                holdCube();
                sleep(1000);
                compassTurn(-45.0);
                break;
            case CENTER:
                telemetry.addLine("Center Column");
                telemetry.update();
                moveStraightEncoder(1.95, 4.0);
                holdCube();
                sleep(1000);
                compassTurn(-35.0);
                break;
            default:
                telemetry.addLine("Saw nothing");
            case RIGHT:
                telemetry.addLine("Right Column");
                telemetry.update();
                moveStraightEncoder(1.95, 4.0);
                holdCube();
                sleep(1000);
                oneWheelCompassTurn(-25.0, true);
                break;
        }
        sleep(1000);
        //compassTurn(120);

        //CODE BELOW THIS POINT WAS DELETED AND RE-ADDED

        lift(-0.1);
        moveStraightTime(0.3,1000);
        lift(0.0);
        sleep(1000);

        drop();
        sleep(1000);
        moveStraightTime(-0.3, 500);
        stopIntake();
        sleep(1000);
        pushCube();
    }
}
