package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class WaldonHardware {

    public LinearOpMode opMode = null;   // gain access to methods in the calling OpMode.
    public HardwareMap WaldonHMap;

    public VoltageSensor controlHubBatteryVoltage;
    public VoltageSensor expansionHubBatteryVoltage;

    public LynxModule controlHub;
    public LynxModule expansionHub;

    // First, let's define all of the hardware elements that we will interface with.
    // There are two constant rotation servos
    public CRServo intake_servo_1;
    public CRServo intake_servo_2;

    //5 more regular servos
    public Servo wrist;
    public Servo InsidePixel;
    public Servo p6servo;
    public Servo OutsidePixel;
    public Servo drone;

    // 7 total motors
    public DcMotor leftfront_drive;
    public DcMotor leftback_drive;
    public DcMotor rightback_drive;
    public DcMotor rightfront_drive;
    public DcMotor ScissorLeft;
    public DcMotor ScissorRight;
    public DcMotor Intake;

    // Now for all of our sensors: 3 distance, 1 IMU and 1 color
    public BNO055IMU imu;
    public DistanceSensor leftDistanceSensor;  //not used in teleop but defined anyways
    public DistanceSensor centerDistanceSensor; //not used in teleop but defined anyways
    public DistanceSensor rightDistanceSensor; //not used in teleop but defined anyways
    public ColorSensor p6Color; //not used in teleop but defined anyways

    //Servo variables
    double dWristIn = 0.49;
    double dWristDeliver = 0.77;
    double dOutsideIn = 0.43;
    double dOutside1Pixel = 0.65;
    double dOutside2Pixel = 1;
    double dInsideIn = 0;
    double dInsideHold = 0.8;

    double dDronePos = 0.5;

    int iDeliveryState = 0;

    double dInsidePixelServo = dInsideHold;
    double dWristServo = dWristIn;
    double dOutsidePixel = dOutsideIn;
    double dIntakeSpeed = 0;
    double dP6ServoPosition = 0;
    public WaldonHardware(LinearOpMode opmode) {
        this.opMode = opmode;
    }

    public void initialize(){
        opMode.telemetry.addData("Status", "detecting...");

        controlHubBatteryVoltage = WaldonHMap.get(VoltageSensor.class, "Control Hub");
        expansionHubBatteryVoltage = WaldonHMap.get(VoltageSensor.class, "Expansion Hub 2");
        controlHub = WaldonHMap.get(LynxModule.class, "Control Hub");
        expansionHub = WaldonHMap.get(LynxModule.class, "Expansion Hub 2");


        //2 Constant Rotation Servos
        intake_servo_1 = WaldonHMap.get(CRServo.class, "intake1");
        intake_servo_2 = WaldonHMap.get(CRServo.class, "intake2");

        //5 Regular Servos
        wrist = WaldonHMap.get(Servo.class, "wrist");
        InsidePixel = WaldonHMap.get(Servo.class, "InsidePixel");
        p6servo = WaldonHMap.get(Servo.class, "p6servo");
        OutsidePixel = WaldonHMap.get(Servo.class, "OutsidePixel");
        drone = WaldonHMap.get(Servo.class, "drone");

        //7 Motors
        leftfront_drive = WaldonHMap.get(DcMotor.class, "leftfront_drive");
        leftback_drive = WaldonHMap.get(DcMotor.class, "leftback_drive");
        rightback_drive = WaldonHMap.get(DcMotor.class, "rightback_drive");
        rightfront_drive = WaldonHMap.get(DcMotor.class, "rightfront_drive");
        ScissorLeft = WaldonHMap.get(DcMotor.class, "ScissorLeft");
        ScissorRight = WaldonHMap.get(DcMotor.class, "ScissorRight");
        Intake = WaldonHMap.get(DcMotor.class, "Intake");

        //Sensors
        imu = WaldonHMap.get(BNO055IMU.class, "imu");
        leftDistanceSensor = WaldonHMap.get(DistanceSensor.class, "leftDistanceSensor");
        centerDistanceSensor = WaldonHMap.get(DistanceSensor.class, "centerDistanceSensor");
        rightDistanceSensor = WaldonHMap.get(DistanceSensor.class, "rightDistanceSensor");
        p6Color = WaldonHMap.get(ColorSensor.class, "P6Color");

        leftfront_drive.setDirection(DcMotor.Direction.REVERSE);
        leftback_drive.setDirection(DcMotor.Direction.REVERSE);
        rightback_drive.setDirection(DcMotor.Direction.FORWARD);
        rightfront_drive.setDirection(DcMotor.Direction.FORWARD);
        leftback_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftfront_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightback_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightfront_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftfront_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftback_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightfront_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightback_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ScissorLeft.setDirection(DcMotor.Direction.FORWARD);
        ScissorRight.setDirection(DcMotor.Direction.FORWARD);
        ScissorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ScissorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Initalize all of our servo positions.
        p6servo.setPosition(Variables.p6servo);
        OutsidePixel.setPosition(Variables.OutsidePixelHome);
        InsidePixel.setPosition(Variables.InsidePixelHome);
        wrist.setPosition(Variables.wristHome);
    }
}
