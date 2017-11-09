/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Relic_Recovery", group="Iterative Opmode")
public class Relic_Recovery_Teleop extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm = null;
    private Servo leftIntake = null;
    private Servo rightIntake = null;

    private final double JOYSTICK_DEADZONE = 0.05;
    private boolean hasCube = true;

    /*
     * Code to run ONCE when the driver hits INIT
     */
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
        drop();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
        grab();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
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
        if (gamepad1.right_trigger > 0.5f){
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
                lift_power = 0.1;
            else
                lift_power = 0.0;
        }
        lift(lift_power);

        // set intake power
        if (gamepad1.right_bumper){
            grab();
        }
        else if (gamepad1.left_bumper){
            drop();
        }

        telemetry.addData("Forward/Turn", "%.2f - %.2f", forward_power, turn_power);
        telemetry.addData("Arm", "Controller: %.2f | Actual: %.2f", lift_power, arm.getPower());
        telemetry.addData("Intake", "%.2f", leftIntake.getPosition());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
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
        leftIntake.setPosition(0.75);
        rightIntake.setPosition(0.75);
        hasCube = true;
    }
    private void drop(){
        leftIntake.setPosition(0.15);
        rightIntake.setPosition(0.15);
        hasCube = false;
    }
}
