/*
    This file is part of Team5881_Example.

    Team5881_Example is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Team5881_Example is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;

@TeleOp(name="1p TeleOP", group="Team 5881 Example Code")
public class OnePersonTeleOP extends OpMode {
    /* Create HardwareAndMethods instance called robot */
    private HardwareAndMethods robot = new HardwareAndMethods();

    /* Declare Variables for toggle switches */
    private boolean stickPressed = false;
    // add more for each new toggle
    /* ************************************* */
    @Override
    public void init() {
        /* Initialize the hardware variables */
        robot.init(hardwareMap); //Passes the hardware map that you set in the app to robot

        /*Tell the driver that initialization is complete. */
        telemetry.addData("Status:", "Initialized");
    }


    @Override
    public void init_loop() { //Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    }
    @Override
    public void start(){ //Code to run ONCE when the driver hits PLAY
    }


    @Override
    public void loop() { //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP


        /*
         * Speed mod toggle switch
         * See wiki page on toggle switches //TODO:toggleWikiPage
         */
        if(gamepad1.right_stick_button && !stickPressed){
            stickPressed = true;
            if(robot.speedMod == 1f) {
                robot.speedMod = 2f;
            }else{
                robot.speedMod = 1f;
            }
        }else if(!gamepad1.right_stick_button){
            stickPressed = false;
        }

        /* Mecanum uses the left stick to drive in the x,y directions, and the right stick to rotate */
        robot.mecanum(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);
        if(gamepad1.dpad_left) robot.spinOneWheel(0);
        if(gamepad1.dpad_right) robot.spinOneWheel(1);
        if(gamepad1.dpad_up) robot.spinOneWheel(2);
        if(gamepad1.dpad_down) robot.spinOneWheel(3);
        telemetry.addData("Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Right Stick Y", gamepad1.right_stick_y);
        telemetry.addData("Right Stick X", gamepad1.right_stick_x);
        telemetry.update();
    }

    @Override
    public void stop() { //Code to run ONCE after the driver hits STOP
    }



}




