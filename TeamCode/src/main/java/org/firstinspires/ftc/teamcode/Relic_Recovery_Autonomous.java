package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Relic_Recovery", group="Competition")
public class Relic_Recovery_Autonomous extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;

    private final double DRIVE_EN_COUNT_PER_FT = 1920.0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive = hardwareMap.get(DcMotor.class, "leftdrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightdrive");
        arm = hardwareMap.get(DcMotor.class, "arm");
        leftIntake = hardwareMap.get(Servo.class, "intakeRight");
        rightIntake = hardwareMap.get(Servo.class, "intakeLeft");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.REVERSE);
        rightIntake.setDirection(Servo.Direction.FORWARD);
        leftIntake.setDirection(Servo.Direction.REVERSE);

        // Set stopping behavior
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        drop();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        setDrive(1.0,0.0);
        sleep(1000);
        setDrive(0.0,1.0);
        sleep(1000);
        setDrive(0.0,0.0);
        //moveStraightEncoder(8.0);

        telemetry.addData("Total runtime","%.2f seconds",runtime.seconds());
        telemetry.update();
        while(true);
    }

    private void forward(double power, long time) {
        setDrive(power, power);
        sleep(time);
    }

    private void moveStraightEncoder(double dist_feet){
        int end_pos = leftDrive.getCurrentPosition() + (int)(dist_feet * DRIVE_EN_COUNT_PER_FT);
        setDrive(1.0, 1.0);
        while(leftDrive.getCurrentPosition()<end_pos){

        }
        setDrive(0.0, 0.0);
    }

    private void setDrive(double left_power, double right_power) {
        leftDrive.setPower(left_power);
        rightDrive.setPower(right_power);
    }

    private void lift(double arm_power){
        arm.setPower(arm_power);
    }

    private void grab(){
        leftIntake.setPosition(0.75);
        rightIntake.setPosition(0.75);
    }

    private void drop(){
        leftIntake.setPosition(0.15);
        rightIntake.setPosition(0.15);
    }
}
