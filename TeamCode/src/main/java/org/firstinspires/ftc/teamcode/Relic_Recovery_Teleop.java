package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Relic_Recovery_Teleop", group="Competition")
public class Relic_Recovery_Teleop extends OpMode
{
    // Declare OpMode members.
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;

    private final double JOYSTICK_DEADZONE = 0.05;
    private final double INTAKE_CLOSE_POSITION = 0.65;
    private final double INTAKE_OPEN_POSITION = 0.10;
    private final double INTAKE_OPEN_SLIGHTLY_POSITION = 0.50;
    private boolean hasCube = true;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

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

        // set the zero power behavior of the motors
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        grab();
    }

    @Override
    public void loop() {

        // Set drive motors
        double forward_power = -gamepad1.left_stick_y;
        double turn_power = gamepad1.right_stick_x;
        if (Math.abs(forward_power) < JOYSTICK_DEADZONE)
        {
            forward_power = 0.0;
        }
        if (Math.abs(turn_power) < JOYSTICK_DEADZONE)
        {
            turn_power = 0.0;
        }

        //telemetry.addData("Joystick Values","Forward: %.2f, Turn: %.2f", forward_power, turn_power);

        double left_power = forward_power + turn_power;
        double right_power = forward_power - turn_power;
        //telemetry.addData("Motor Values","Left: %.2f, Right: %.2f", left_power, right_power);

        setDrive(left_power, right_power);

        // Set lift motor(s)
        double lift_power;
        if (gamepad1.left_bumper){
            // Lift Arm
            if (hasCube)
                lift_power = 0.9;
            else
                lift_power = 0.7;
        }
        else if(gamepad1.left_trigger > 0.5f){
            // Lower Arm
            if (hasCube)
                lift_power = -0.2;
            else
                lift_power = -0.4;
        }
        else {
            // Keep Arm Still
            if (hasCube)
                lift_power = 0.2;
            else
                lift_power = 0.1;
        }
        lift(lift_power);

        // set intake power
        if (gamepad1.right_bumper){
            grab();
        }
        else if (gamepad1.right_trigger > 0.5f){
            drop();
        }

        if(gamepad1.a){
            release();
            setDrive(-0.2,-0.2);
            lift(0.2);
            while (gamepad1.a);
        }

        telemetry.addData("Forward/Turn", "%.2f - %.2f", forward_power, turn_power);
        telemetry.addData("Arm", "Controller: %.2f | Actual: %.2f", lift_power, arm.getPower());
        telemetry.addData("Intake", "%.2f", leftIntake.getPosition());
        telemetry.update();
    }

    @Override
    public void stop() {
    }

    private void setDrive(double left_power, double right_power) {
        leftDrive.setPower(left_power);
        rightDrive.setPower(right_power);
    }

    private void lift(double arm_power){
        arm.setPower(arm_power);
    }

    private void grab(){
        setIntake(INTAKE_CLOSE_POSITION);
        hasCube = true;
    }

    private void drop(){
        setIntake(INTAKE_OPEN_POSITION);
        hasCube = false;
    }

    private void release(){
        setIntake(INTAKE_OPEN_SLIGHTLY_POSITION);
        hasCube = false;
    }

    private void setIntake(double intake_position){
        leftIntake.setPosition(intake_position);
        rightIntake.setPosition(intake_position);
    }
}
