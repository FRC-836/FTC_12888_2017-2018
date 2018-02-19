package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

@Autonomous(name="Blue Left", group="Competition")
public class Relic_Recovery_Autonomous_Blue_Left extends Autonomous_Parent {

    @Override
    public void runAutonomous() {
        grab();
        sleep(1000);
        holdCube();

        telemetry.addData("Status","Started");

        // Read the pictograph
        cryptoboxKey = getPictographKey();

        hitJewel(false);

        telemetry.update();

        lift(0.8);
        sleep(300);
        lift(0.2);

        moveStraightEncoder(-2.00, 4.0);
        sleep(1000);
        compassTurn(-60.0);
        sleep(1000);
        moveStraightEncoder(1.95, 4.0);
        sleep(1000);
        switch(cryptoboxKey)
        {
            case LEFT:
                telemetry.addLine("Left Column");
                telemetry.update();
                compassTurn(-120.0);
                sleep(1000);
                oneWheelCompassTurn(-57, false);
                sleep(1000);
                moveStraightEncoder(1.50, 3.0);
                break;
            case CENTER:
                telemetry.addLine("Center Column");
                telemetry.update();
                compassTurn(-120.0);
                sleep(1000);
                oneWheelCompassTurn(-49, false);
                sleep(1000);
                moveStraightEncoder(1.15, 3.0);
                break;
            default:
                telemetry.addLine("Saw nothing");
            case RIGHT:
                telemetry.addLine("Right Column");
                telemetry.update();
                compassTurn(-120.0);
                sleep(1000);
                oneWheelCompassTurn(-39, false);
                sleep(1000);
                moveStraightEncoder(0.50, 3.0);
                break;
        }
        sleep(1000);
        //compassTurn(120);

        //CODE BELOW THIS POINT WAS DELETED AND RE-ADDED

        lift(-0.1);
        moveStraightTime(0.4,500);
        lift(0.0);

        drop();
        sleep(1000);
        oneWheelCompassTurn(-20, false);
        moveStraightEncoder(-0.3,3.0);
        stopIntake();
        sleep(1000);
        if (cryptoboxKey != RelicRecoveryVuMark.LEFT)
        {
            pushCube();
        }

        if (cryptoboxKey== RelicRecoveryVuMark.RIGHT) {
            oneWheelCompassTurn(-50.0, false);
            moveStraightTime(0.3, 1000);
        }


    }
}
