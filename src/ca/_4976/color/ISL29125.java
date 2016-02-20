package ca._4976.color;

import edu.wpi.first.wpilibj.I2C;

import java.util.Arrays;

public class ISL29125 {

    // The I2C Sensor
    private I2C sensor;

    // The buffers for data storage
    private byte[] buffer1, buffer2;

    // The buffers for color storage
    private int[] color;

    // Constructor - Creates sensor object
    public ISL29125(I2C.Port port) {
        // Creates sensor object onboard roboRIO and sets I2C address
        sensor = new I2C(port, ISL_I2C_ADDR);

        // Initialize the byte buffers for 8-bit and 16-bit data storage
        buffer1 = new byte[1];
        buffer2 = new byte[1];

        // Initialize the color buffer for color storage
        color = new int[3];
    }

    // Initialize - returns true if successful
    // Verifies sensor is there by checking its device ID
    // Resets all registers/configurations to factory default
    // Sets configuration registers for the common use case
    public boolean init() {
        boolean ret;

        // Check device ID
        ret = (read8(DEVICE_ID) == 0x7D);

        // Reset registers
        ret &= reset();

        //Set to RGB mode, 375 lux, and high IR compensation
        ret &= config(CFG1_MODE_RGB | CFG1_375LUX, CFG2_IR_ADJUST_HIGH, CFG_DEFAULT);

        return ret;
    }

    //Reset all registers - returns true if successful
    public boolean reset() {
        int ret = 0x00;

        // Reset registers
        write8(DEVICE_ID, 0x46);

        // Check reset
        ret = read8(CONFIG_1);
        ret |= read8(CONFIG_2);
        ret |= read8(CONFIG_3);
        ret |= read8(STATUS);

        return (ret == 0x00);
    }

    // Setup configuration registers (three registers) - returns true if successful
    // Use constant variables from end of file for each parameter
    // Use CFG_DEFAULT for default configuration for that register
    public boolean config(int config1, int config2, int config3) {
        boolean ret = true;

        // Set 1st configuration register
        write8(CONFIG_1, config1);

        // Set 2nd configuration register
        write8(CONFIG_2, config2);

        // Set 3rd configuration register
        write8(CONFIG_3, config3);

        //Check if configurations were set correctly
        ret = (read8(CONFIG_1) == config1);
        ret &= (read8(CONFIG_2) == config2);
        ret &= (read8(CONFIG_3) == config3);

        return ret;
    }

    // Read the latest Sensor ADC for RGB color - color[0] = Red, color[1] = GREEN, color[2] = BLUE
    public int[] readColor() {
        color[0] = read16(RED_L);
        color[1] = read16(GREEN_L);
        color[2] = read16(BLUE_L);

        return Arrays.copyOf(color, color.length);
    }

    // Generic I2C read register (single byte)
    private int read8(int registerAddress) {
        //Read register
        sensor.read(registerAddress, 1, buffer1);

        //Un-sign the buffer
        return (buffer1[0] & 0xff);
    }

    //Generic I2C write data to register (single byte)
    private void write8(int registerAddress, int value) {
        sensor.write(registerAddress, value);
    }

    //Generic I2C read registers (two bytes)
    private int read16(int registerAddress) {
        //Read registers
        sensor.read(registerAddress, 1, buffer1);
        sensor.read(registerAddress + 1, 1, buffer2);

        //Un-sign the buffers
        int buffer1Unsigned = buffer1[0] & 0xff;
        int buffer2Unsigned = buffer2[0] & 0xff;

        //Add the high value
        return (buffer1Unsigned | (buffer2Unsigned << 8));
    }

    // BEGIN STATIC CONSTANTS //

    // ISL29125 I2C Address
    public static final int ISL_I2C_ADDR = 0x44;

    // ISL29125 Registers
    public static final int DEVICE_ID = 0x00;
    public static final int CONFIG_1 = 0x01;
    public static final int CONFIG_2 = 0x02;
    public static final int CONFIG_3 = 0x03;
    public static final int THRESHOLD_LL = 0x04;
    public static final int THRESHOLD_LH = 0x05;
    public static final int THRESHOLD_HL = 0x06;
    public static final int THRESHOLD_HH = 0x07;
    public static final int STATUS = 0x08;
    public static final int GREEN_L = 0x09;
    public static final int GREEN_H = 0x0A;
    public static final int RED_L = 0x0B;
    public static final int RED_H = 0x0C;
    public static final int BLUE_L = 0x0D;
    public static final int BLUE_H = 0x0E;

    // Configuration Settings
    public static final int CFG_DEFAULT = 0x00;

    // CONFIG1
    // Pick a mode, determines what color[s] the sensor samples, if any
    public static final int CFG1_MODE_POWERDOWN = 0x00;
    public static final int CFG1_MODE_G = 0x01;
    public static final int CFG1_MODE_R = 0x02;
    public static final int CFG1_MODE_B = 0x03;
    public static final int CFG1_MODE_STANDBY = 0x04;
    public static final int CFG1_MODE_RGB = 0x05;
    public static final int CFG1_MODE_RG = 0x06;
    public static final int CFG1_MODE_GB = 0x07;

    // Light intensity range
    // In a dark environment 375Lux is best, otherwise 10KLux is likely the best option
    public static final int CFG1_375LUX = 0x00;
    public static final int CFG1_10KLUX = 0x08;

    // Change this to 12 bit if you want less accuracy, but faster sensor reads
    // At default 16 bit, each sensor sample for a given color is about ~100ms
    public static final int CFG1_16BIT = 0x00;
    public static final int CFG1_12BIT = 0x10;

    // Unless you want the interrupt pin to be an input that triggers sensor sampling, leave this on normal
    public static final int CFG1_ADC_SYNC_NORMAL = 0x00;
    public static final int CFG1_ADC_SYNC_TO_INT = 0x20;

    // CONFIG2
    // Selects upper or lower range of IR filtering
    public static final int CFG2_IR_OFFSET_OFF = 0x00;
    public static final int CFG2_IR_OFFSET_ON = 0x80;

    // Sets amount of IR filtering, can use these presets or any value between = 0x00 and = 0x3F
    // Consult datasheet for detailed IR filtering calibration
    public static final int CFG2_IR_ADJUST_LOW = 0x00;
    public static final int CFG2_IR_ADJUST_MID = 0x20;
    public static final int CFG2_IR_ADJUST_HIGH = 0x3F;

    // CONFIG3
    // No interrupts, or interrupts based on a selected color
    public static final int CFG3_NO_INT = 0x00;
    public static final int CFG3_G_INT = 0x01;
    public static final int CFG3_R_INT = 0x02;
    public static final int CFG3_B_INT = 0x03;

    // How many times a sensor sample must hit a threshold before triggering an interrupt
    // More consecutive samples means more times between interrupts, but less triggers from short transients
    public static final int CFG3_INT_PRST1 = 0x00;
    public static final int CFG3_INT_PRST2 = 0x04;
    public static final int CFG3_INT_PRST4 = 0x08;
    public static final int CFG3_INT_PRST8 = 0x0C;

    // If you would rather have interrupts trigger when a sensor sampling is complete, enable this
    // If this is disabled, interrupts are based on comparing sensor data to threshold settings
    public static final int CFG3_RGB_CONV_TO_INT_DISABLE = 0x00;
    public static final int CFG3_RGB_CONV_TO_INT_ENABLE = 0x10;

    // STATUS FLAG MASKS
    public static final int FLAG_INT = 0x01;
    public static final int FLAG_CONV_DONE = 0x02;
    public static final int FLAG_BROWNOUT = 0x04;
    public static final int FLAG_CONV_G = 0x10;
    public static final int FLAG_CONV_R = 0x20;
    public static final int FLAG_CONV_B = 0x30;

}