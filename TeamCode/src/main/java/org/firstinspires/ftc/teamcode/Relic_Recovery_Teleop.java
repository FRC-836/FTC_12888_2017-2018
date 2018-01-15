package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Relic_Recovery_Teleop", group="Competition")
public class Relic_Recovery_Teleop extends Teleop_Parent
{
    @Override
    public void cycleTeleop() {

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

        if (gamepad1.b && bButtonEnabled)
        {
            slowMode = !slowMode;
            bButtonEnabled = false;
        }
        if(!gamepad1.b)
            bButtonEnabled = true;

        if(slowMode)
        {
            forward_power *= SLOWMODE_VALUE;
            turn_power *= SLOWMODE_VALUE;
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
                lift_power = 0.8;
            else
                lift_power = 0.6;
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
        else if(gamepad1.a){
            release();
            setDrive(-0.2,-0.2);
            lift(0.2);
            while (gamepad1.a);
        }
        else
        {
            stopIntake();
        }

        if (!IN_COMPETITION) {
            telemetry.addData("Forward/Turn", "%.2f - %.2f", forward_power, turn_power);
            telemetry.addData("Arm", "Controller: %.2f | Actual: %.2f", lift_power, arm.getPower());
            //telemetry.addData("Intake", "%.2f", leftIntake.getPosition());
        }
        if (slowMode) {
            telemetry.addData("Mode","Slow");
        }
        else {
            telemetry.addData("Mode", "Fast");
        }
        telemetry.update();
    }
}
