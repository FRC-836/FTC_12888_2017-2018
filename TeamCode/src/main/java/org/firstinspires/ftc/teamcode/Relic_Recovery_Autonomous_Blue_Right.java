package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name="Blue Right", group="Competition")
public class Relic_Recovery_Autonomous_Blue_Right extends Autonomous_Parent {

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

        moveStraightEncoder(-2.385, 2.0);
        sleep(1000);

        switch(cryptoboxKey)
        {
            case LEFT:
                telemetry.addLine("Left Column");
                telemetry.update();
                //moveStraightEncoder(3.625, 3.0);
                oneWheelCompassTurn(55,true);
                break;
            case CENTER:
                telemetry.addLine("Center Column");
                telemetry.update();
                //moveStraightEncoder(3.0, 2.5);
                oneWheelCompassTurn(68, true);
                break;
            default:
                telemetry.addLine("Saw nothing");
            case RIGHT:
                telemetry.addLine("Right Column");
                telemetry.update();
                //moveStraightEncoder(2.375, 2.0);
                oneWheelCompassTurn(81,true);
                break;
        }
        sleep(1000);

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
