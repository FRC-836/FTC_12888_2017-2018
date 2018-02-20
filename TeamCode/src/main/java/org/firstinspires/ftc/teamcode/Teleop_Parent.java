package org.firstinspires.ftc.teamcode;

public class Teleop_Parent extends Robot_Parent{

    protected final double JOYSTICK_DEADZONE = 0.05;
    protected final double INTAKE_OPEN_SLIGHTLY_POSITION = 0.50;
    protected final double SLOWMODE_VALUE = 0.25;
    protected boolean slowMode = false;
    protected boolean bButtonEnabled = true;
    protected final boolean IN_COMPETITION = false;

    @Override
    public void initializeRobot(){

    }

    @Override
    public void runRobot(){
        raiseJewelArm();
        while (opModeIsActive())
        {
            cycleTeleop();
        }
    }

    public void cycleTeleop(){

    }

    protected void release(){
        if (INTAKE_OPERATES_BY_POWER)
            setIntake(INTAKE_RELEASE_POWER);
        else
            setIntake(INTAKE_OPEN_SLIGHTLY_POSITION);
        hasCube = false;
    }
}
