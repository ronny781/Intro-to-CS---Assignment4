

import java.util.Iterator;



public class BinaryNumber implements Comparable<BinaryNumber>{
	private static final BinaryNumber ZERO = new BinaryNumber(0);
	private static final BinaryNumber ONE = new BinaryNumber(1);

	private BitList bits;

	// Copy constructor
	//Do not change this constructor
	public BinaryNumber(BinaryNumber number) {
		bits = new BitList(number.bits);
	}

	//Do not change this constructor
	private BinaryNumber(int i) {
		bits = new BitList();
		bits.addFirst(Bit.ZERO);
		if (i == 1)
			bits.addFirst(Bit.ONE);
		else if (i != 0)
			throw new IllegalArgumentException("This Constructor may only get either zero or one.");
	}
	

	//Do not change this method
	public int length() {
		return bits.size();
	}

	//Do not change this method
	public boolean isLegal() {
		return bits.isNumber() & bits.isReduced();
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.1 ================================================
	public BinaryNumber(char c) {
		int number = c-'0';
		if(number<0 | number >9)//If the input is not a valid number throw exception 
			throw new IllegalArgumentException("Wrong input");
		bits = new BitList();
		
		while(number!=0) {
			bits.addLast(new Bit(number%2));
			number = number/2;
		}	  
		bits.addLast(Bit.ZERO);//The first "0" for positive number indicator
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.2 ================================================
	public String toString() {
		// Do not remove or change the next two lines
		if (!isLegal()) // Do not change this line
			throw new RuntimeException("I am illegal.");// Do not change this line
		return bits.toString().substring(1, this.length()+1); //go to bits.toString and cut the edges "<>"

	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.3 ================================================
	public boolean equals(Object other) {
		boolean isEqual;
		if(other==null)
			throw new IllegalArgumentException("The Input is null");
		if(!(other instanceof BinaryNumber)) //  { //Check if the instance is BinaryNumber and checks if the lengths of the lists matches.if not return false
		isEqual = false;
		else if(((BinaryNumber) other).length() != this.length())
			isEqual = false;
		else {
			Iterator<Bit> thisIterator = bits.iterator();
			Iterator<Bit> otherIterator = ((BinaryNumber) other).bits.iterator();
			isEqual = true;
			while(thisIterator.hasNext() & otherIterator.hasNext() & isEqual) 
				isEqual = thisIterator.next().equals(otherIterator.next());
		}
		return isEqual;	
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.4 ================================================
	public BinaryNumber add(BinaryNumber addMe) {
		if(!(addMe instanceof BinaryNumber))
			throw new IllegalArgumentException("Wrong Input");
		bits.reduce();
		BinaryNumber addMeCopy = new BinaryNumber(addMe);
		addMeCopy.bits.reduce();
		if(this.length()>addMeCopy.length())//If my length is bigger then addMe then "pad" addMe. and the opposite.
			addMeCopy.bits.padding(this.length());
		else if(this.length()<addMeCopy.length())
			bits.padding(addMeCopy.length());
		Bit Cin = new Bit(false);//At first, initialize the carry to zero
		BitList sumList = new BitList(); //This list will store the sum of the the two numbers.
		Iterator<Bit> thisIterator = bits.iterator();
		Iterator<Bit> otherIterator = ((BinaryNumber) addMeCopy).bits.iterator();
		while(thisIterator.hasNext() & otherIterator.hasNext()) {
			Bit A = thisIterator.next();//The bit the corresponding place of me
			Bit B = otherIterator.next();//The bit the corresponding place of addMe
			sumList.addLast(Bit.fullAdderSum(A,B,Cin));
			Cin = Bit.fullAdderCarry(A,B,Cin);//Let Cin be the carry.
		}
		if(this.bits.getLast().equals(addMeCopy.bits.getLast()))//If we adding both negative or positive numbers then we should add the last carry 
			sumList.addLast(Cin);
			sumList.reduce();
		bits = sumList;//Point the new list to field list.
		return this;
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.5 ================================================
	public BinaryNumber negate() {
		bits.reduce();//Make sure the number is reduced.
		BinaryNumber one = new BinaryNumber('1');//Create a list of bits that equals to'01'.
		BinaryNumber compToOne = new BinaryNumber(this);//Create a list of bit that equals the complement to one of this list.
		compToOne.bits = compToOne.bits.complement();
		BinaryNumber negate = one.add(compToOne);//Sum both of the lists.
		return negate;
		}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.6 ================================================
	public BinaryNumber subtract(BinaryNumber subtractMe) {
		if(subtractMe==null)
			throw new IllegalArgumentException("The Input is null");
		BinaryNumber thisCopy = new BinaryNumber(this); //Create a copy of the current list, because I don't want to execute the add operation on this list(because it will change it's value).
		BinaryNumber list = thisCopy.add(subtractMe.negate());// Subtract an object equals to add the negated value of the object
		return list;
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.7 ================================================
	public int signum() {
		int ans=0;
		bits.reduce();
		if(this.length()==1 & this.bits.getFirst().equals(Bit.ZERO))//If the number is "0" then  return 0
			ans = 0;
		else if(this.bits.getLast().equals(Bit.ZERO)) //If the first bit is zero then return 1
			ans = 1;
		else if(this.bits.getLast().equals(Bit.ONE))//If the first bit is one then return -1
			ans = -1;
			
		return ans;	
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.8 ================================================
	public int compareTo(BinaryNumber other) {
		if(other==null)
			throw new IllegalArgumentException("The Input is null");
		if(!other.isLegal())
			throw new IllegalArgumentException("The Input isn't legal");
		BinaryNumber otherCopy = new BinaryNumber(other);//Make sure we don't accidentally change it's value.
		otherCopy.bits.reduce();
		this.bits.reduce();
		int ans = 0;
		if(this.equals(otherCopy)) //If they both are equal return 0
			ans = 0;
		else if(this.signum()==1 & otherCopy.signum()==-1)
			ans = 1;
		else if(this.signum()==-1 & otherCopy.signum()==1)
			ans = -1;
		else if(this.signum()==1 & otherCopy.signum()==1 & this.length()!=otherCopy.length())//If both are reduced positive and have the different 
			if(this.length()>otherCopy.length())//If this length is bigger then other's then it's the bigger number
				ans = 1;
			else//Else this is the smaller one.
				ans = -1;
		else if(this.signum()==-1 & otherCopy.signum()==-1 & this.length()!=otherCopy.length())//If both are reduced negative and have the different 
			if(this.length()>otherCopy.length())//Opposite to the case above
				ans = -1;
			else
				ans = 1;		
			
			else {//If we reached so far both have the same length.
				Iterator<Bit> thisIter = this.bits.descendingIterator();//Creating two iterators that goes from the end of the list to the beginning.
				Iterator<Bit> otherIter = otherCopy.bits.descendingIterator();
				while(thisIter.hasNext() & otherIter.hasNext()) {
					Bit thisBit = thisIter.next();
					Bit otherBit = otherIter.next();
					//When we first encounter an equality in the numbers, we compare them.The bit which equals to '1' is representing the bigger number.
					if(!thisBit.equals(otherBit)) {
						if(thisBit.equals(Bit.ONE))
							ans = 1;
						else
							ans = -1;
					}
				}
			}
		return ans;

	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.9 ================================================
	public int toInt() {
		// Do not remove or change the next two lines
		if (!isLegal()) // Do not change this line
			throw new RuntimeException("I am illegal.");// Do not change this line
		double sum = 0;
		bits.reduce();
		double temp;
		int index = this.length()-2;
		Iterator<Bit> thisIter = this.bits.descendingIterator();//Creating two iterators that goes from the end of the list to the beginning.
		Bit signBit = thisIter.next();
		while( thisIter.hasNext()) { //Calculating the value of all digits except the first one.
			Bit current = thisIter.next();
			if(current.equals(Bit.ONE)) {//If the bit is one then add it's value 
				temp = Math.pow(2, index);
				sum += temp;
				if(sum>Integer.MAX_VALUE)//If the number has exceeded the int value limit throw exception
					throw new RuntimeException();		
			}
			index--;
		}
		
		if(signBit.equals(Bit.ONE)) {//In case the number is negative the calculate the value of the last bit and subtract it from our sum
			temp = Math.pow(2, this.length()-1);
			sum = sum - temp;
			if(sum<Integer.MIN_VALUE)//If the number has exceeded the int value limit throw exception
				throw new RuntimeException();	
		}
		int sum1 = (int) sum;
		return sum1;

	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.10 ================================================
	// Do not change this method
	public BinaryNumber multiply(BinaryNumber multiplyMe) {
		if(multiplyMe==null)
			throw new IllegalArgumentException("The Input is null");
		BinaryNumber product ;
		if(multiplyMe.equals(BinaryNumber.ZERO)) {//If "multiplyMe" equals to zero then return zero.
			product = new BinaryNumber(BinaryNumber.ZERO);
			return product;
		}


		if(this.signum()==multiplyMe.signum()) {
			if(this.signum()==1){//If both are positive
				product = this.multiplyPositive(multiplyMe);
			}
			else {//If both are negative
				BinaryNumber thisNegated = this.negate() ;
				product = thisNegated.multiplyPositive(multiplyMe.negate());
			}

		}
		else {//If their signs do not match
			if(this.signum()==-1) {//If this is negative, negate him and multiply as usual. At the end negate the result.
				BinaryNumber thisNegated = this.negate() ;
				product = thisNegated.multiplyPositive(multiplyMe);
				product = product.negate();
			}
			else {//If "multiplyMe" is negative, negate him and multiply as usual. At the end negate the result.
				BinaryNumber otherNegated = multiplyMe.negate() ;
				product = this.multiplyPositive(otherNegated);
				product = product.negate();
			}

		}
		return product;
	}

	private BinaryNumber multiplyPositive(BinaryNumber multiplyMe) {
		BinaryNumber product = new BinaryNumber(this);
		for(int i = 1; i< multiplyMe.toInt(); i++) 
			product.add(this);//Add to the product himself * "multiplyMe" times

		return product;
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.11 ================================================
	// Do not change this method
	public BinaryNumber divide(BinaryNumber divisor) {
		// Do not remove or change the next two lines
		if(divisor==null)
			throw new IllegalArgumentException("The Input is null");
		if (divisor.equals(ZERO)) // Do not change this line
			throw new RuntimeException("Cannot divide by zero."); // Do not change this line
		BinaryNumber quotient = new BinaryNumber('0');
		if(this.equals(ZERO))
			return quotient;//0 divide everything equals 0.
		
		if(this.signum()==1 & divisor.signum()==1 ){//case 1: both are positive
			quotient = this.dividePositive(divisor);
		}
		else if(this.signum()==-1 & divisor.signum()==-1 ){//case 2: both are negative
			quotient = this.negate().dividePositive(divisor.negate());//minus divide minus is equal to plus divide plus
		}
		else if(this.signum()==1 & divisor.signum()==-1 ){//case 3: this is positive and divisor negative
			quotient = this.dividePositive(divisor.negate());//Divide their positive value and negate the result
			quotient = quotient.negate();
		}
		else if(this.signum()==-1 & divisor.signum()==1 ){//case 4: this is negative and divisor positive
			quotient = this.negate().dividePositive(divisor);//Divide their positive value and negate the result
			quotient = quotient.negate();
		}
		return quotient;
	}

	private BinaryNumber dividePositive(BinaryNumber divisor) {
		BinaryNumber division = new BinaryNumber(this);
		BinaryNumber result = new BinaryNumber('0');//Counting the number of times that we can subtract the divisor 
		while(division.toInt()>0) {
			division = division.subtract(divisor);
			result.add(ONE);//result = result + 1 ;
		}
		if(division.toInt()<0)//If this doesn't divide with the divisor without a remainder, then subtract one from result and return it.
			result = result.subtract(ONE);
		
			return result;

	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.12 ================================================
	public BinaryNumber(String s) {
		if(s==null ||s.isEmpty())
			throw new IllegalArgumentException("The Input is either null or empty");
		boolean numberSign = true;//Indicator for number sign.
		bits = new BitList();
		int number = Integer.parseInt(s);
		if(number < 0) {//If the number is negative, convert it to binary like it was positive
			numberSign = false;
			number = -number;
		}
			while(number!=0) {
				bits.addLast(new Bit(number%2));
				number = number/2;
			}	  
			bits.addLast(Bit.ZERO);
		
		if(!numberSign) {//If the number is negative then calculate the negated value and insert it to this object
			BinaryNumber negate = this.negate();
			this.bits = negate.bits;		
		}
			
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 3.13 ================================================
	public String toIntString() {
		// Do not remove or change the next two lines
		if (!isLegal()) // Do not change this line
			throw new RuntimeException("I am illegal.");// Do not change this line
		//
		throw new UnsupportedOperationException("Delete this line and implement the method.");
	}
    public String toString1() { 
    	if (!isLegal()) // Do not change this line
			throw new RuntimeException("I am illegal.");// Do not change this line
    	BinaryNumber thisCopy = new BinaryNumber(this);
        String sum = "";
        String num = "";
        int baseCounter = 0;
        int index = this.length()-2;
        //the decimal value is equal to the sum of the following: bit value * base^(the location of the digit, when the digit in the units place is the first one(0), and so on)
		Iterator<Bit> thisIter = thisCopy.bits.iterator();//Creating two iterators that goes from the end of the list to the beginning.
		
        while(thisIter.hasNext()){
        	Bit current = thisIter.next();
        	if(!thisIter.hasNext() & current.equals(Bit.ONE)) {//If it's the last (sign) digit and it's equals to one.
        		num = String.valueOf(current.toInt());
        		for(int h = 0 ; h< baseCounter; h++) //This part is wrong 
        			num = multiplyBy2(num);
        		BinaryNumber num1 = new BinaryNumber(num);
        		num1 = num1.negate();
        		System.out.println(num1 + "hey");
        		num = num1.toString();
        		sum = addition(num,sum);
        	}
        		
        	else if(current.equals(Bit.ONE)) {
        		num = String.valueOf(current.toInt());
        		for(int h = 0 ; h< baseCounter; h++) 
        			num = multiplyBy2(num);
        		sum = addition(num,sum);
			}
        	baseCounter++;
        }
        if(sum.isEmpty())//If the sum is zero return 0.
        	sum = "0";
        return sum;
    }
    private String addition(String num1, String num2) {//Assumes num1 is bigger than num2.
    	String ans = "";
    	int temp = 0;
    	int carry = 0;
    	//Combining the numbers until we reach to the end of the smaller(num2) number.
    	for(int i = num1.length()-1, j=num2.length()-1; j>=0 ;i--,j--) {
    		temp =  (num1.charAt(i)-'0') + (num2.charAt(j)-'0') + carry;
    			if(temp>=10) {//If the the result of the addition is bigger than 10, insert the last digit and insert 1 to carry.
    				ans = temp%10 + ans;
    				carry = 1;
    				if(i==0)//If we reached to the end of num2 and we have a carry then add it to the string.
        	    		ans = carry + ans;
    			}
    			else {//Else insert the  digit and assign 0 to the carry. 
    				carry = 0;
    				ans = temp + ans;
    			}
    	}

        	for(int c = num1.length()-num2.length()-1 ; c >=0 ; c--) {//Finishing the rest of remaining addition.(only for num1).
        		temp =  (num1.charAt(c)-'0')  + carry;
        	   	if(temp>=10) {//If the the result of the addition is bigger than 10, insert the last digit and insert 1 to carry.
        	    	ans = temp%10 + ans;
        	    	carry = 1;
        	    	if(c==0) //If we reached to the end of num1 and we have a carry then add it to the string.
        	    		ans = carry + ans;
        	    	}
        	    	else {//Else insert the  digit and assign 0 to the carry. 
        	    		carry = 0;
        	    		ans = temp + ans;
        	    	}
        		}
    			
     	return ans;
     	}
    	
    

    private String multiplyBy2(String a) {
    	String ans = "";
    	int temp = 0;
    	int carry = 0;
    	int multiplier = 2;//Multiplier which is always 2.
    	for(int i = a.length()-1 ; i >= 0  ; i--) {
    		temp = (a.charAt(i)-'0') * multiplier + carry;
    		if(temp>=10) {//If the the result of the multiplication is bigger than 10, insert the last digit and insert 1 to carry.
    			ans = temp%10 + ans;
    			carry = 1;
    			if(i==0)//If we reached to the end and we have a carry then add it to the string.
    				ans = carry + ans;
    		}
    		else {//Else insert the  digit and assign 0 to the carry. 
				carry = 0;
				ans = temp + ans;
			}
    			
    	}
    	return ans;
    }

	// Returns this * 2
	public BinaryNumber multBy2() {
		BinaryNumber output = new BinaryNumber(this);
		output.bits.shiftLeft();
		output.bits.reduce();
		return output;
	}

	// Returns this / 2;
	public BinaryNumber divBy2() {
		BinaryNumber output = new BinaryNumber(this);
		if (!equals(ZERO)) {
			if (signum() == -1) {
				output.negate();
				output.bits.shiftRight();
				output.negate();
			} else output.bits.shiftRight();
		}
		return output;
	}

}
