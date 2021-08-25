import java.util.*;

import com.sun.tools.javac.jvm.PoolConstant.Dynamic;

public class main {

    public static void main (String[] args) {
    	BinaryNumber bn9 = new BinaryNumber('8'); // 01001 (9)
    	BinaryNumber bnM9 = bn9.negate(); // 10111 (-9)
    	System.out.println(bnM9.toInt());
    	;




    }

}
