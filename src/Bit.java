

public class Bit {

    private  boolean value;
    public static  final Bit ONE  = new Bit(true);
    public static  final Bit ZERO = new Bit(false);

    public Bit(boolean value) {
        this.value = value;
    }

    public Bit(int intValue) {
        if (intValue == 0)
            value = false;
        else {
            if (intValue == 1)
                value = true;
            else throw new IllegalArgumentException(value + " is neither 0 nor 1.");
        }
    }

    public String toString() {
        return "" + toInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Bit))
            return false;
        else return value == ((Bit) obj).value;
    }

    public Bit negate() {
        Bit output;
        if (value)
            output = ZERO;
        else output = ONE;
        return output;
    }

    public int toInt() {
        int output;
        if(value)
            output = 1;
        else
            output = 0;
        return output;
    }
    
    //=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 1.1 ================================================
    public static Bit fullAdderSum(Bit A, Bit B, Bit Cin) {
    	boolean bitValue = false;
    	int sum = ((A.toInt()+B.toInt()+Cin.toInt()))%2; // I used modulo 2 because that's how XOR A,B,Cin works. 
    	if(sum==1)
    		bitValue = true;

    	Bit sumBit = new Bit(bitValue);
    	return sumBit;
    }
    public static Bit fullAdderCarry(Bit A, Bit B, Bit Cin) {
    	boolean bitValue = false;
    	int sum = ((A.toInt()+B.toInt()+Cin.toInt())); 
    	if(sum>=2)
    		bitValue = true;

    	Bit sumBit = new Bit(bitValue);
    	return sumBit;
    }

}
