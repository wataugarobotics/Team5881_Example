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

@TeleOp(name="1p TeleOP", group="Summer Development 2020")
//@Disabled
public class OnePersonTeleOP extends OpMode {
    //Create HardwareAndMethods instance called robot
    private HardwareAndMethods robot = new HardwareAndMethods();
    //private ConceptTensorFlowObjectDetection tflow = new ConceptTensorFlowObjectDetection();

    // Declares variables
    private boolean stickPressed = false;

    @Override
    public void init() {
        // Initialize the hardware variables
        robot.init(hardwareMap);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }


    @Override
    public void init_loop() { //Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    }
    @Override
    public void start(){ //Code to run ONCE when the driver hits PLAY
        //robot.initVuforia();
        //robot.initTfod();
        //if (robot.tfod != null) {
        //    robot.tfod.activate();}
    }


    @Override
    public void loop() { //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP


        //Speed mod toggle switch
        if(gamepad1.right_stick_button && !stickPressed){
            stickPressed = true; //prevents code from toggling on/off many times every time you press a button, as this is run in a very fast loop
            if(robot.speedMod == 1f) {
                robot.speedMod = 2f;
            }else{
                robot.speedMod = 1f;
            }
        }else if(!gamepad1.right_stick_button){
            stickPressed = false;
        }

        // Mecanum uses the left stick to drive in the x,y directions, and the right stick to turn
        robot.mecanum(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x);

    }

    @Override
    public void stop() { //Code to run ONCE after the driver hits STOP
    }



}




