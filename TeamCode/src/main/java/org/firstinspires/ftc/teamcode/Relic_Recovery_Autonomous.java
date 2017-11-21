package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Relic_Recovery_Auto", group="Competition")
public class Relic_Recovery_Autonomous extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;

    private pictograph cryptoboxKey = null;

    private final double DRIVE_EN_COUNT_PER_FT = 1920.0;
    private final double INTAKE_CLOSE_POSITION = 0.65;
    private final double INTAKE_OPEN_POSITION = 0.10;

    BNO055IMU imu;
    Orientation angles;

    enum pictograph {
        LEFT,
        CENTER,
        RIGHT
    }

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
        imu = hardwareMap.get(BNO055IMU.class, "imu");

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

        setIntake(0.0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        grab();

        compassTurn(90.0);

        // Read the pictograph
        cryptoboxKey = getPictographKey();

        // Drive straight to cryptobox area
        // 3 +- 0.625 ft
        switch(cryptoboxKey)
        {
            case LEFT:
                moveStraightEncoder(3.625);
                break;
            case CENTER:
                moveStraightEncoder(3.0);
                break;
            case RIGHT:
                moveStraightEncoder(2.375);
                break;
            default:
                telemetry.addData("ERROR","cryptobox key is not LEFT, CENTER, or RIGHT.");
                telemetry.update();
                while(true);
        }

        compassTurn(90);
        
        forward(0.3,1000);

        drop();

        forward(-0.3, 1000);

        compassTurn(180);


        telemetry.addData("Total runtime","%.2f seconds",runtime.seconds());
        telemetry.update();
        while(true);
    }

    private void forward(double power, long time) {
        setDrive(power, power);
        sleep(time);
        setDrive(0.0, 0.0);
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
        setIntake(INTAKE_CLOSE_POSITION);
    }

    private void drop(){
        setIntake(INTAKE_OPEN_POSITION);
    }

    private void setIntake(double intake_position){
        leftIntake.setPosition(intake_position);
        rightIntake.setPosition(intake_position);
    }

    private pictograph getPictographKey(){
        return null;
    }

    private void compassTurn(double degrees) {
        float goalAngle = getCurrentDegrees() + ((float) degrees);
        double drivePower = 1.0;
        if (degrees < 0.0)
        {
            // Turning left
            setDrive(-drivePower, drivePower);
            while (getCurrentDegrees() > goalAngle)
            {
                telemetry.addData("Angle","%.2f",getCurrentDegrees());
                telemetry.update();
            }
        }
        else
        {
            // Turning right
            setDrive(drivePower, -drivePower);
            while (getCurrentDegrees() < goalAngle)
            {
                telemetry.addData("Angle","%.2f",getCurrentDegrees());
                telemetry.update();
            }
        }
        setDrive(0.0, 0.0);
    }

    private float getCurrentDegrees()
    {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle));
    }
}
