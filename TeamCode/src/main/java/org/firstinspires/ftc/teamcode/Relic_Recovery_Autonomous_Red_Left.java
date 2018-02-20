package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Left", group="Competition")
public class Relic_Recovery_Autonomous_Red_Left extends Autonomous_Parent {

    @Override
    public void runAutonomous() {
        grab();
        sleep(1000);
        holdCube();

        telemetry.addData("Status","Started");
        telemetry.update();

        // Read the pictograph
        cryptoboxKey = getPictographKey();

        hitJewel(true);

        lift(0.8);
        sleep(300);
        lift(0.2);

        moveStraightEncoder(3.00, 4.0);
        sleep(1000);

        switch(cryptoboxKey)
        {
            case LEFT:
                telemetry.addLine("Left Column");
                telemetry.update();
                //moveStraightEncoder(3.625, 3.0);
                oneWheelCompassTurn(55, true);
                break;
            default:
                telemetry.addLine("Saw nothing");
            case CENTER:
                telemetry.addLine("Center Column");
                telemetry.update();
                //moveStraightEncoder(3.0, 2.5);
                oneWheelCompassTurn(65, true);
                break;
            case RIGHT:
                telemetry.addLine("Right Column");
                telemetry.update();
                //moveStraightEncoder(2.375, 2.0);
                oneWheelCompassTurn(75, true);
                break;
        }
        sleep(1000);
        //compassTurn(120);

        //CODE BELOW THIS POINT WAS DELETED AND RE-ADDED

        lift(-0.1);
        moveStraightTime(0.3,1000);
        lift(0.0);

        drop();
        sleep(1000);
        moveStraightTime(-0.3, 500);
        stopIntake();
        sleep(1000);
        pushCube();
    }
}
