/* MULTITHREADING <Flight.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Adrian Gallegos
 * ag76424
 * 17360
 * Slip days used: 1
 * Spring 2022
 */
package assignment6;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Flight {
    /**
     * the delay time you will use when print tickets
     */
    private int printDelay; // 50 ms. Use it to fix the delay time between prints.
    private SalesLogs log;
    // my instantiated variables (non-static)
	public String flightNo;
	public int firstNumRows;
	public int businessNumRows;
	public int economyNumRows;
	
	public Queue<Seat> firstSeatsQueue = new LinkedList<>();	// holds all available first class seats
	public Queue<Seat> businessSeatsQueue = new LinkedList<>();	// holds all available business class seats
	public Queue<Seat> economySeatsQueue = new LinkedList<>();	// holds all available economy class seats

	boolean flightBooked = false;	// Determines whether the flight has been sold out or not
	// SalesLogs salesLog = new SalesLogs();

    public Flight(String flightNo, int firstNumRows, int businessNumRows, int economyNumRows) {
    	this.printDelay = 50;// 50 ms. Use it to fix the delay time between
    	this.log = new SalesLogs();
        // TODO: Implement the rest of this constructor
    	this.flightNo = flightNo;
    	this.firstNumRows = firstNumRows;
    	this.businessNumRows = businessNumRows;
    	this.economyNumRows = economyNumRows;
    	
    	int firstRows = 0;
    	int businessRows = 0;
    	int economyRows = 0;
    	for (; firstRows < firstNumRows; firstRows++) {
    		for (int seatNum = 0; seatNum < 4; seatNum++) {
    			SeatLetter seat = null;
    			if (seatNum == 0)
    				seat = SeatLetter.A;
    			if (seatNum == 1)
    				seat = SeatLetter.B;
    			if (seatNum == 2)
    				seat = SeatLetter.E;
    			if (seatNum == 3)
    				seat = SeatLetter.F;
    			SeatClass first = SeatClass.FIRST;
    			Seat currentSeat = new Seat(first, firstRows + 1, seat);
    			firstSeatsQueue.add(currentSeat);
    		}
    	}
    	for (; businessRows < businessNumRows; businessRows++) {
    		for (int seatNum = 0; seatNum < 6; seatNum++) {
    			SeatLetter seat = null;
    			if (seatNum == 0)
    				seat = SeatLetter.A;
    			if (seatNum == 1)
    				seat = SeatLetter.B;
    			if (seatNum == 2)
    				seat = SeatLetter.C;
    			if (seatNum == 3)
    				seat = SeatLetter.D;
    			if (seatNum == 4)
    				seat = SeatLetter.E;
    			if (seatNum == 5)
    				seat = SeatLetter.F;
    			SeatClass business = SeatClass.BUSINESS;
    			Seat currentSeat = new Seat(business, firstRows + businessRows + 1, seat);
    			businessSeatsQueue.add(currentSeat);
    		}
    	}
    	for (; economyRows < economyNumRows; economyRows++) {
    		for (int seatNum = 0; seatNum < 6; seatNum++) {
    			SeatLetter seat = null;
    			if (seatNum == 0)
    				seat = SeatLetter.A;
    			if (seatNum == 1)
    				seat = SeatLetter.B;
    			if (seatNum == 2)
    				seat = SeatLetter.C;
    			if (seatNum == 3)
    				seat = SeatLetter.D;
    			if (seatNum == 4)
    				seat = SeatLetter.E;
    			if (seatNum == 5)
    				seat = SeatLetter.F;
    			SeatClass economy = SeatClass.ECONOMY;
    			Seat currentSeat = new Seat(economy, firstRows + businessRows + economyRows + 1, seat);
    			economySeatsQueue.add(currentSeat);
    		}
    	}
    }
    
    public void setPrintDelay(int printDelay) {
        this.printDelay = printDelay;
    }

    public int getPrintDelay() {
        return printDelay;
    }

    /**
     * Returns the next available seat not yet reserved for a given class
     *
     * @param seatClass a seat class(FIRST, BUSINESS, ECONOMY)
     * @return the next available seat or null if flight is full
     */
	public Seat getNextAvailableSeat(SeatClass seatClass) {
		// TODO: Implement this method
		// First class was requested and is available
        if (seatClass == SeatClass.FIRST && !(firstSeatsQueue.isEmpty())) {
        	Seat foundSeat = firstSeatsQueue.poll();
        	log.addSeat(foundSeat);
        	return foundSeat;
        }
        // First class was requested but isn't available.
        // However, Business class is available
        else if (seatClass == SeatClass.FIRST && firstSeatsQueue.isEmpty()
        		&& !(businessSeatsQueue.isEmpty())) {
        	Seat foundSeat = businessSeatsQueue.poll();
        	log.addSeat(foundSeat);
        	return foundSeat;
        }
        // First class was requested but first and business class aren't available.
        // However, economy class is available
        else if (seatClass == SeatClass.FIRST && firstSeatsQueue.isEmpty()
        		&& businessSeatsQueue.isEmpty() && !(economySeatsQueue.isEmpty())) {
        	Seat foundSeat = economySeatsQueue.poll();
        	log.addSeat(foundSeat);
        	return foundSeat;
        }
        // Business class was requested and is available
        else if (seatClass == SeatClass.BUSINESS && !(businessSeatsQueue.isEmpty())) {
        	Seat foundSeat = businessSeatsQueue.poll();
        	log.addSeat(foundSeat);
        	return foundSeat;
        }
        // Business class was requested but is not available.
        // However, economy class is available
        else if (seatClass == SeatClass.BUSINESS && businessSeatsQueue.isEmpty()
        		&& !(economySeatsQueue.isEmpty())) {
        	Seat foundSeat = economySeatsQueue.poll();
        	log.addSeat(foundSeat);
        	return foundSeat;
        }
        // Economy class was requested and is available
        else if (seatClass == SeatClass.ECONOMY && !(economySeatsQueue.isEmpty())) {
        	Seat foundSeat = economySeatsQueue.poll();
        	log.addSeat(foundSeat);
        	return foundSeat;
        }
        // The economy class is sold out -> the flight is now considered full
        // Or some weirdo asked for a non-existent class (jk not possible here)
		return null;
	}

	/**
     * Prints a ticket to the console for the customer after they reserve a seat.
     *
     * @param seat a particular seat in the airplane
     * @return a flight ticket or null if a ticket office failed to reserve the seat
     */
	public Ticket printTicket(String officeId, Seat seat, int customer) {
		// TODO: Implement this method
		if (!flightBooked) { 								// Checks if the flight is sold out
			if (seat != null) { 							// Checks that the seat returned from getNextAvailableSeat() is not null
				Ticket currentTicket = new Ticket(flightNo, officeId, seat, customer);

				System.out.println(currentTicket.toString());  	// print current ticket to console
				log.addTicket(currentTicket);					// add current ticket to ticket log
				return currentTicket;							// return current ticket
			}
			if (economySeatsQueue.peek() == null) {				// Checks and ensures economy class is full
				System.out.println("Sorry, we are sold out!");
				flightBooked = true;							// Adjusts the value of flightBooked, which ensures no other thread can process/print tickets
			}
		}
        return null;
    }

	/**
     * Lists all seats sold for this flight in order of purchase.
     *
     * @return list of seats sold
     */
    public List<Seat> getSeatLog() {
        // TODO: Implement this method
        return log.getSeatLog();
    }

    /**
     * Lists all tickets sold for this flight in order of printing.
     *
     * @return list of tickets sold
     */
    public List<Ticket> getTransactionLog() {
        // TODO: Implement this method
        return log.getTicketLog();
    }
    
    static enum SeatClass {
		FIRST(0), BUSINESS(1), ECONOMY(2);

		private Integer intValue;

		private SeatClass(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	static enum SeatLetter {
		A(0), B(1), C(2), D(3), E(4), F(5);

		private Integer intValue;

		private SeatLetter(final Integer intValue) {
			this.intValue = intValue;
		}

		public Integer getIntValue() {
			return intValue;
		}
	}

	/**
     * Represents a seat in the airplane
     * FIRST Class: 1A, 1B, 1E, 1F ... 
     * BUSINESS Class: 2A, 2B, 2C, 2D, 2E, 2F  ...
     * ECONOMY Class: 3A, 3B, 3C, 3D, 3E, 3F  ...
     * (Row numbers for each class are subject to change)
     */
	static class Seat {
		private SeatClass seatClass;
		private int row;
		private SeatLetter letter;

		public Seat(SeatClass seatClass, int row, SeatLetter letter) {
			this.seatClass = seatClass;
			this.row = row;
			this.letter = letter;
		}

		public SeatClass getSeatClass() {
			return seatClass;
		}

		public void setSeatClass(SeatClass seatClass) {
			this.seatClass = seatClass;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public SeatLetter getLetter() {
			return letter;
		}

		public void setLetter(SeatLetter letter) {
			this.letter = letter;
		}

		@Override
		public String toString() {
			return Integer.toString(row) + letter + " (" + seatClass.toString() + ")";
		}
	}

	/**
 	 * Represents a flight ticket purchased by a customer
 	 */
	static class Ticket {
		private String flightNo;
		private String officeId;
		private Seat seat;
		private int customer;
		public static final int TICKET_STRING_ROW_LENGTH = 31;

		public Ticket(String flightNo, String officeId, Seat seat, int customer) {
			this.flightNo = flightNo;
			this.officeId = officeId;
			this.seat = seat;
			this.customer = customer;
		}

		public int getCustomer() {
			return customer;
		}

		public void setCustomer(int customer) {
			this.customer = customer;
		}

		public String getOfficeId() {
			return officeId;
		}

		public void setOfficeId(String officeId) {
			this.officeId = officeId;
		}
		
		@Override
		public String toString() {
			String result, dashLine, flightLine, officeLine, seatLine, customerLine, eol;

			eol = System.getProperty("line.separator");

			dashLine = new String(new char[TICKET_STRING_ROW_LENGTH]).replace('\0', '-');

			flightLine = "| Flight Number: " + flightNo;
			for (int i = flightLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				flightLine += " ";
			}
			flightLine += "|";

			officeLine = "| Ticket Office ID: " + officeId;
			for (int i = officeLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				officeLine += " ";
			}
			officeLine += "|";

			seatLine = "| Seat: " + seat.toString();
			for (int i = seatLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				seatLine += " ";
			}
			seatLine += "|";

			customerLine = "| Customer: " + customer;
			for (int i = customerLine.length(); i < TICKET_STRING_ROW_LENGTH - 1; ++i) {
				customerLine += " ";
			}
			customerLine += "|";

			result = dashLine + eol + flightLine + eol + officeLine + eol + seatLine + eol + customerLine + eol
					+ dashLine;

			return result;
		}
	}

	/**
	 * SalesLogs are security wrappers around an ArrayList of Seats and one of Tickets
	 * that cannot be altered, except for adding to them.
	 * getSeatLog returns a copy of the internal ArrayList of Seats.
	 * getTicketLog returns a copy of the internal ArrayList of Tickets.
	 */
	static class SalesLogs {
		private ArrayList<Seat> seatLog;
		private ArrayList<Ticket> ticketLog;

		private SalesLogs() {
			seatLog = new ArrayList<Seat>();
			ticketLog = new ArrayList<Ticket>();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Seat> getSeatLog() {
			return (ArrayList<Seat>) seatLog.clone();
		}

		@SuppressWarnings("unchecked")
		public ArrayList<Ticket> getTicketLog() {
			return (ArrayList<Ticket>) ticketLog.clone();
		}

		public void addSeat(Seat s) {
			seatLog.add(s);
		}

		public void addTicket(Ticket t) {
			ticketLog.add(t);
		}
	}
}
