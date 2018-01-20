package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Right", group="Competition")
public class Relic_Recovery_Autonomous_Red_Right extends Autonomous_Parent {

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

        switch(cryptoboxKey)
        {
            case LEFT:
                telemetry.addLine("Left Column");
                telemetry.update();
                moveStraightEncoder(2.00, 4.0);
                sleep(1000);
                lift(0.8);
                sleep(600);
                lift(0.2);
                sleep(1000);
                moveStraightEncoder(0.15, 4.0);
                sleep(1000);
                oneWheelCompassTurn(-45, false);
                break;
            case CENTER:
                telemetry.addLine("Center Column");
                telemetry.update();
                lift(0.8);
                sleep(600);
                lift(0.2);
                sleep(1000);
                moveStraightEncoder(2.15, 4.0);
                sleep(1000);
                oneWheelCompassTurn(-30, false);
                break;
            default:
                telemetry.addLine("Saw nothing");
            case RIGHT:
                telemetry.addLine("Right Column");
                telemetry.update();
                moveStraightEncoder(2.00, 4.0);
                sleep(1000);
                oneWheelCompassTurn(-15, true);
                break;
        }
        sleep(1000);
        //compassTurn(120);

        //CODE BELOW THIS POINT WAS DELETED AND RE-ADDED

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
