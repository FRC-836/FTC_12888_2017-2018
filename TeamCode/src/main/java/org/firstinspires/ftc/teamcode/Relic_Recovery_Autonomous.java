package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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

@Autonomous(name="Relic_Recovery_Auto", group="Competition")
public class Relic_Recovery_Autonomous extends LinearOpMode {

    //Vuforia variables
    public static final String TAG = "Vuforia VuMark Sample";
    OpenGLMatrix lastLocation = null;
    int cameraMonitorViewId;
    VuforiaLocalizer.Parameters vParameters;
    VuforiaTrackables relicTrackables;
    private RelicRecoveryVuMark cryptoboxKey = null;
    private VuforiaTrackable relicTemplate = null;
    VuforiaLocalizer vuforia;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;

    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    private final double DRIVE_EN_COUNT_PER_FT = 341.1;
    private final double INTAKE_CLOSE_POSITION = 0.65;
    private final double INTAKE_OPEN_POSITION = 0.10;
    private final double INTAKE_OPEN_FULLY = 0.0;
    private final double ENCODER_DRIVE_POWER = 0.3;
    // COMPASS_PAUSE_TIME - When using compassTurn, it waits COMPASS_PAUSE_TIME milliseconds before
    // using the compass to ensure the robot has begun moving.
    private final long COMPASS_PAUSE_TIME = 200;

    BNO055IMU imu;
    Orientation angles;

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

        setIntake(INTAKE_OPEN_FULLY);

        setupVuMarkData();
        setupIMU();

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        relicTrackables.activate();
        runtime.reset();
        grab();

        telemetry.addData("Status","Started");
        telemetry.update();

        // Read the pictograph
        cryptoboxKey = getPictographKey();

        // Drive straight to cryptobox area
        // 3 +- 0.625 ft
        switch(cryptoboxKey)
        {
            case LEFT:
                telemetry.addLine("Left Column");
                telemetry.update();
                moveStraightEncoder(3.625);
                break;
            case CENTER:
                telemetry.addLine("Center Column");
                telemetry.update();
                moveStraightEncoder(3.0);
                break;
            default:
                telemetry.addLine("Saw nothing");
            case RIGHT:
                telemetry.addLine("Right Column");
                telemetry.update();
                moveStraightEncoder(2.375);
                break;
        }

        compassTurn(90);
        
        forward(0.3,1000);

        drop();

        forward(-0.3, 1000);

        compassTurn(180);


        telemetry.addData("Total runtime","%.2f seconds",runtime.seconds());
        telemetry.update();
        while(opModeIsActive());
    }

    //TO TEST:
    //moveStraightEncoder
    //compassTurn

    private void forward(double power, long time) {
        setDrive(power, power);
        sleep(time);
        setDrive(0.0, 0.0);
    }

    /*private void moveStraightEncoder(double dist_feet){
        int left_end_pos = leftDrive.getCurrentPosition() + (int)(dist_feet * DRIVE_EN_COUNT_PER_FT);
        int right_end_pos = rightDrive.getCurrentPosition() + (int)(dist_feet * DRIVE_EN_COUNT_PER_FT);

        leftDrive.setTargetPosition(left_end_pos);
        rightDrive.setTargetPosition(right_end_pos);
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setDrive(ENCODER_DRIVE_POWER, ENCODER_DRIVE_POWER);
        while (opModeIsActive() && leftDrive.isBusy() && rightDrive.isBusy())
        {
            telemetry.addData("Left has moved","%.2f",((double)(left_end_pos - leftDrive.getCurrentPosition()))/DRIVE_EN_COUNT_PER_FT);
            telemetry.addData("Right has moved","%.2f",((double)(right_end_pos - rightDrive.getCurrentPosition()))/DRIVE_EN_COUNT_PER_FT);
            telemetry.update();
        }
        setDrive(0.0, 0.0);
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }*/


    private void moveStraightEncoder(double dist_feet){
        int end_pos = leftDrive.getCurrentPosition() + (int)(dist_feet * DRIVE_EN_COUNT_PER_FT);
        if (dist_feet > 0.0) {
            setDrive(ENCODER_DRIVE_POWER, ENCODER_DRIVE_POWER);
            while (leftDrive.getCurrentPosition() < end_pos && opModeIsActive());
        }
        else
        {
            setDrive(-ENCODER_DRIVE_POWER, -ENCODER_DRIVE_POWER);
            while (leftDrive.getCurrentPosition() > end_pos && opModeIsActive());
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

    private RelicRecoveryVuMark getPictographKey(){
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        telemetry.addData("Status","Searching for Pictograph");
        telemetry.update();
        while (vuMark == RelicRecoveryVuMark.UNKNOWN && opModeIsActive())
        {

            vuMark = RelicRecoveryVuMark.from(relicTemplate);
        }
        telemetry.addData("Status","Pictograph Found");
        telemetry.update();
        return vuMark;
    }

    private void compassTurn(double degrees) {
        float startPos = getCurrentDegrees();
        float goalAngle = startPos - ((float) degrees);
        double drivePower = 0.5;
        if (degrees < 0.0)
        {
            // Turning left
            setDrive(-drivePower, drivePower);
            if (goalAngle > 175.0) {
                goalAngle -= 360.0;
                sleep(COMPASS_PAUSE_TIME);
                while (getCurrentDegrees() >= startPos && opModeIsActive())
                {
                    telemetry.addData("Angle1","%.2f",getCurrentDegrees());
                    telemetry.update();
                }
            }
            while (getCurrentDegrees() < goalAngle && opModeIsActive())
            {
                telemetry.addData("Angle1","%.2f",getCurrentDegrees());
                telemetry.update();
            }
        }
        else
        {
            // Turning right
            setDrive(drivePower, -drivePower);
            if (goalAngle < -175.0) {
                goalAngle += 360.0;
                sleep(COMPASS_PAUSE_TIME);
                while (getCurrentDegrees() <= startPos && opModeIsActive())
                {
                    telemetry.addData("Angle1","%.2f",getCurrentDegrees());
                    telemetry.update();
                }
            }
            while (getCurrentDegrees() > goalAngle && opModeIsActive())
            {
                telemetry.addData("Angle1","%.2f",getCurrentDegrees());
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

    private void setupVuMarkData()
    {
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        vParameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        vParameters.vuforiaLicenseKey = "ATR3/Cb/////AAAAGTVIvIjz0kb9u+DtnsWzGj0JigqZYbgQE+XMNBbu/++4Xnjd7/uUpFGFKVr/yZ7lnRorZBA+mpukXprPG9dDy22DQIPjId5gCDNTGs1faBtAwVnoDm8qXxeCgIoRXh7aXbQBCVdy9xusOMwgnJwn2lsINNC7dHUF4Z+azbhfjIjoZoNUsLqUBfnXoO7+Emfu62Nlnl6DQhsKLRcjCE551beyEi2Co6RLn2+so7oCY3Favuwpm4H5+f1TPMBW2fhBJH9g4nEKziL90BTu+jLjA/Pt8LIOa3OQaLy7A8gmf8GLnNFvpYQSSOuE+JCMi55Ebv8POx1MmH20HkklMkpWIdmfM/gKfnDKShnG3bJ7oOg+";

        vParameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(vParameters);
        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
    }

    private void setupIMU()
    {
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
    }
}
