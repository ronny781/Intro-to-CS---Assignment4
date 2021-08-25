import java.util.*;
import java.util.LinkedList;
import java.util.LinkedList.Node;
import java.util.function.Consumer;

import java.util.Iterator;

public class BitList extends LinkedList<Bit> {
	private int numberOfOnes;


	// Do not change the constructor
	public BitList() {
		numberOfOnes = 0;
	}

	// Do not change the method
	public int getNumberOfOnes() {
		return numberOfOnes;
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.1 ================================================

	public void addLast(Bit element) {
		if(element == null)//if element is null throw an IllegalArgumentException.
			throw new IllegalArgumentException("input argument is null");
		super.addLast(element);
		if(element.toInt()==1)//If the value of the bit is "1" add one to our field.
			numberOfOnes++;


	}

	public void addFirst(Bit element) {
		if(element == null)//if element is null throw an IllegalArgumentException.
			throw new IllegalArgumentException("input argument is null");
		super.addFirst(element);
		if(element.toInt()==1)//If the value of the bit is "1" add one to our field.
			numberOfOnes++;
	}

	public Bit removeLast() {
		Bit bit = super.getLast();
		if(bit.toInt()==1)//If the value of the bit is "1" subtract one from our field.
			numberOfOnes--;
		super.removeLast();
		return bit;
	}

	public Bit removeFirst() {
		Bit bit = super.getFirst();
		if(bit.toInt()==1) //If the value of the bit is "1" subtract one from our field.
			numberOfOnes--;
		super.removeFirst();
		return bit;

	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.2 ================================================
	public String toString() {
		Iterator<Bit> listIter = this.iterator();
		String accumulator=""; //String accumulator for all bits in the list 
		while(listIter.hasNext())
			accumulator=listIter.next() + accumulator;
		accumulator = "<" + accumulator + ">"; //Adds the "<>" at the end and the start of 
		return accumulator;

	}


	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.3 ================================================
	public BitList(BitList other) {
		if(other == null)
			throw new IllegalArgumentException("Other is null");
		int numberOfOnes = 0; //Count how many times the number "1" appears in the list in order to initialize the field.
		Iterator<Bit> BitIter = other.iterator(); // Creating iterator to iterate the given list.
		while(BitIter.hasNext()) {
			Bit bit = BitIter.next();
			if(bit.toInt()==1) //if the value of the bit is "1" add it to out counter.
				numberOfOnes++;
			this.addLast(bit);
		}
		this.numberOfOnes = numberOfOnes;
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.4 ================================================

	public boolean isNumber() {
		boolean isNumber = true;
		if(this.isEmpty())//If the list in empty return false
			isNumber = false;
		else if(this.getLast().toInt()==1) {//If the MSB is "1" then check the rest.
			isNumber = false;
			Iterator<Bit> iter = this.iterator(); 
			while(iter.hasNext())//if we found a "1" after the first "1" digit then it's a valid number.
				//I don't want to check the MSB digit.So i want to check every link in the list except the last one.
				//So if i found 1 in the rest of the list and it's not the last one(iter.hasNext()) is here for that, then the list is valid.
				if(iter.next().toInt()==1 & !isNumber & iter.hasNext())
					isNumber = true; 
		}	
		return isNumber;

	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.5 ================================================
	public boolean isReduced() {
		boolean isReduced = false;  
		if(this.isNumber()){//First term: if the number is not legal assign false to isReduced and don't even check the other terms.
			if((numberOfOnes==2 && this.size()==2)||(numberOfOnes==1 && this.size()==2 & this.getFirst().equals(Bit.ONE))|(numberOfOnes==0 & this.size()==1))//Term "a"
				isReduced = true;
			else if(this.size()>=3 && ((this.getLast().equals(Bit.ONE) && this.get(this.size()-2).equals(Bit.ZERO)) ||
					(this.getLast().equals(Bit.ZERO)&&this.get(this.size()-2).equals(Bit.ONE)))) //Term "b". I used double || and && because i used expensive methods(get(index)).
				isReduced = true;//this.size()-2 means i wan't to check the one before the last one in the list.
			else if(this.size()>=3 && (numberOfOnes==2 && (this.getLast().equals(Bit.ONE)&&this.get(this.size()-2).equals(Bit.ONE))))//Term "c" 
				isReduced = true;//get(this.size()-2) means i wan't to check the one before the last one in the list.
		}
		return isReduced;   

	}

	public void reduce() {
		if(!this.isReduced() & this.isNumber())//If the number is already reduced or if the number isn't valid don't do anything.
			while(!this.isReduced()) {//While the number isn't reduced keep removing the last bit, and check of if it's "reduced" every time.
				this.removeLast();
			}
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.6 ================================================
	public BitList complement() {
		BitList complement = new BitList();
		Iterator<Bit> iter = this.iterator();
		while(iter.hasNext()) { //This loop intend to assign the opposite values of the list to a new list.
			Bit bit = iter.next();
			complement.addLast(bit.negate());
		}
		return complement;
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.7 ================================================
	public Bit shiftRight() {//Removes the first bit in the list and return it, unless the list is empty.
		if(this.isEmpty())
			return null;
		Bit bit = this.getFirst();
		this.removeFirst();
		return bit;
	}

	public void shiftLeft() {//Adds zero at the beginning of the list
		this.addFirst(Bit.ZERO);
	}

	//=========================== Intro2CS 2021, ASSIGNMENT 4, TASK 2.8 ================================================
	public void padding(int newLength) {
		if(newLength<0) 
			throw new IllegalArgumentException("Wrong Input:Please input a valid length");
		if(this.isEmpty())
			throw new  IllegalArgumentException("Can't pad empty list");
		if(this.size()<newLength) {//If the newLength is smaller or equal don't do anything.
			Bit bit = new Bit(this.getLast().toInt());//Determine which bit we need to add in the end.
			int numberOfBitsToAdd = newLength-this.size();
			for(int i = 0 ; i < numberOfBitsToAdd;i++ ) //Add the bit newLength-this.size() times.
				this.addLast(bit);   	
		}
	}

	//----------------------------------------------------------------------------------------------------------
	// The following overriding methods must not be changed.
	//----------------------------------------------------------------------------------------------------------
	public boolean add(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public void add(int index, Bit element) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public Bit remove(int index) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean offer(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean offerFirst(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean offerLast(Bit e) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public Bit set(int index, Bit element) {
		throw new UnsupportedOperationException("Do not use this method!");
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Do not use this method!");
	}
}
