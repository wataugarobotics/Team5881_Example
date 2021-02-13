package org.firstinspires.ftc.teamcode;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import org.firstinspires.ftc.robotcore.external.ClassFactory;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
//import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
//import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
//import java.util.List;

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
        //robot.initCompuVis();
        //if (robot.tfod != null) {
        //    robot.tfod.activate();} //TODO:delete?
    }


    @Override
    public void loop() { //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP


        /* Speed mod toggle switch
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

    }

    @Override
    public void stop() { //Code to run ONCE after the driver hits STOP
    }



}




