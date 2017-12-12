package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public abstract class Robot_Parent extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    protected DcMotor backLeftDrive = null;
    protected DcMotor backRightDrive = null;
//    protected DcMotor frontLeftDrive = null;
//    protected DcMotor frontRightDrive = null;
    protected DcMotor arm = null;
    protected Servo leftIntake = null;
    protected Servo rightIntake = null;
    protected boolean hasCube = false;

    protected final double INTAKE_CLOSE_POSITION = 0.65;
    protected final double INTAKE_OPEN_POSITION = 0.10;

    @Override
    public void runOpMode() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        backLeftDrive = hardwareMap.get(DcMotor.class, "BLD");
        backRightDrive = hardwareMap.get(DcMotor.class, "BRD");
//        frontLeftDrive = hardwareMap.get(DcMotor.class, "FLD");
//        frontRightDrive = hardwareMap.get(DcMotor.class, "FRD");
        arm = hardwareMap.get(DcMotor.class, "arm");
        leftIntake = hardwareMap.get(Servo.class, "intakeRight");
        rightIntake = hardwareMap.get(Servo.class, "intakeLeft");

        // Most robots need the motor on one side to be reversed to drive moveStraightTime
        // Reverse the motor that runs backwards when connected directly to the battery
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
//        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
//        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.REVERSE);
        rightIntake.setDirection(Servo.Direction.FORWARD);
        leftIntake.setDirection(Servo.Direction.REVERSE);

        // Set stopping behavior
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRightDrive .setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        initializeRobot();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        runRobot();

        telemetry.addData("Total runtime","%.2f seconds",runtime.seconds());
        telemetry.update();
    }

    public void initializeRobot() {

    }

    public void runRobot() {

    }

    protected void setDrive(double left_power, double right_power) {
        backLeftDrive.setPower(left_power);
        backRightDrive.setPower(right_power);
//        frontLeftDrive.setPower(left_power);
//        frontRightDrive.setPower(right_power);
    }

    protected void lift(double arm_power){
        arm.setPower(arm_power);
    }

    protected void grab(){
        setIntake(INTAKE_CLOSE_POSITION);
        hasCube = true;
    }

    protected void drop(){
        setIntake(INTAKE_OPEN_POSITION);
        hasCube = false;
    }

    protected void setIntake(double intake_position){
        leftIntake.setPosition(intake_position);
        rightIntake.setPosition(intake_position);
    }
}