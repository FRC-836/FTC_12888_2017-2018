package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Servo Test", group="Linear Opmode")
public class ServoTest extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    /*
     For every motor on the robot, you MUST have a line that says
     "private DcMotor" followed by the name of your motor, followed
     by " = null"
      */
    private Servo leftDrive = null;
    private Servo rightDrive = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(Servo.class, "left");
        rightDrive = hardwareMap.get(Servo.class, "right");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        // Set stopping behavior

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        leftDrive.setPosition(1.0);
        sleep(1000);
        leftDrive.setPosition(0.0);
        sleep(1000);
        rightDrive.setPosition(1.0);
        sleep(1000);
        rightDrive.setPosition(0.0);
        sleep(1000);

    }
}
