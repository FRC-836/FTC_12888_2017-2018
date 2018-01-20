package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Blue Left", group="Competition")
public class Relic_Recovery_Autonomous_Blue_Left extends Autonomous_Parent {

    @Override
    public void runAutonomous() {
        grab();
        sleep(1000);
        holdCube();

        telemetry.addData("Status","Started");
        telemetry.update();

        lift(0.8);
        sleep(300);
        lift(0.2);

        // Read the pictograph
        cryptoboxKey = getPictographKey();

        moveStraightEncoder(-2.0, 4.0);
        sleep(1000);
        compassTurn(-55.0);
        sleep(1000);
        moveStraightEncoder(1.5, 2.0);
        sleep(1000);
        switch(cryptoboxKey)
        {
            case LEFT:
                telemetry.addLine("Left Column");
                telemetry.update();
                compassTurn(-170.0);
                moveStraightEncoder(1.15, 3.0);
                break;
            case CENTER:
                telemetry.addLine("Center Column");
                telemetry.update();
                compassTurn(-145.0);
                moveStraightEncoder(0.75, 3.0);
                break;
            default:
                telemetry.addLine("Saw nothing");
            case RIGHT:
                telemetry.addLine("Right Column");
                telemetry.update();
                compassTurn(-120.0);
                moveStraightEncoder(0.50, 3.0);
                break;
        }
        sleep(1000);
        //compassTurn(120);

        //CODE BELOW THIS POINT WAS DELETED AND RE-ADDED

        lift(-0.1);
        moveStraightTime(0.3,500);
        lift(0.0);

        drop();
        moveStraightTime(-0.3, 500);
        stopIntake();
        sleep(1000);
        pushCube();
    }
}
