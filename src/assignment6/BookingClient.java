/* MULTITHREADING <BookingClient.java>
 * EE422C Project 6 submission by
 * Replace <...> with your actual data.
 * Adrian Gallegos
 * ag76424
 * 17360
 * Slip days used: 1
 * Spring 2022
 */
package assignment6;

import java.util.Map;

import assignment6.Flight.Seat;
import assignment6.Flight.SeatClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.lang.Thread;

public class BookingClient {

	public Map<String, SeatClass[]> office;					// Keeps track of customers and their desired SeatClass along with which ticket office they're at
	public Flight flight;									// Keeps track of the flight name as well as the number of first/business/economy seats on the flight
	public List<String> ticketOffices = new ArrayList<>();	// Keeps track of ticket office ID's
	public List<Integer> customerIDs = new ArrayList<>();	// Keeps track of customer ID's
	
	
	/**
     * @param offices maps ticket office id to seat class preferences of customers in line
     * @param flight the flight for which tickets are sold for
     */
    public BookingClient(Map<String, SeatClass[]> offices, Flight flight) {
        // TODO: Implement this constructor
    	this.office = offices;
		this.flight = flight;
		Iterator<String> itr = office.keySet().iterator();
    	while (itr.hasNext()) {								// Copies the ticket offices from office and makes an arraylist from them
    		String key = itr.next();
			ticketOffices.add(key);
		}
    }

    /**
     * Starts the ticket office simulation by creating (and starting) threads
     * for each ticket office to sell tickets for the given flight
     *
     * @return list of threads used in the simulation,
     * should have as many threads as there are ticket offices
     */
    public List<Thread> simulate() {
        // TODO: Implement this method
    	List<Thread> threads = new LinkedList<>();
		int i = 0;							// counter to increment through all the offices (TO1, TO2,..., TOn)
		while (i < office.size()) {
			String ticketOfficeId = ticketOffices.get(i);
			Thread currentThread = new Thread() {
				@Override
				public void run() {
					SeatClass[] current = office.get(ticketOfficeId);          		// Get the SeatClass of the customers at this ticket office
					int currentCustomer = 0;
					while (currentCustomer < current.length) { 				   	    // Continue if there are more customers in line
						synchronized (flight.firstSeatsQueue) {		 				// Ensures only 1 thread can access the queues at a time
							synchronized (flight.businessSeatsQueue) {
								synchronized (flight.economySeatsQueue) {
									SeatClass seatclass = current[currentCustomer];
									Seat availableSeat = flight.getNextAvailableSeat(seatclass); 	// Get the next available seat
									int customerNum = currentCustomer + 1;							
									if (!customerIDs.isEmpty()) {							// Check if the customer ID list is not empty
										while(customerIDs.contains(customerNum)) {    		// While the customer ID list has the current customer's ID, increment the customer's ID by 1
											customerNum = customerNum + 1;
										}
									}
									customerIDs.add(customerNum);					        // Add customer ID to the customer ID list
									flight.printTicket(ticketOfficeId, availableSeat, customerNum);  // Print the customer's ticket
									currentCustomer++;										// Go to the next customer
								}
							}
						}
						try {
							Thread.sleep(flight.getPrintDelay());			// Thread is sent to sleep so other threads can run
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			};
			threads.add(currentThread);						// Add current thread to the thread list
			currentThread.start();							// Begin thread
			i++;
		}
		return threads;
    }

    public static void main(String[] args) {
        // TODO: Initialize test data to description
    	Map<String, SeatClass[]> offices = new HashMap<String, SeatClass[]>(){
    		{
    			put("TO1", new SeatClass[] {SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS});
    			put("TO3", new SeatClass[] {SeatClass.BUSINESS, SeatClass.ECONOMY, SeatClass.ECONOMY});
    			put("TO2", new SeatClass[] {SeatClass.FIRST, SeatClass.BUSINESS, SeatClass.ECONOMY, 
    				SeatClass.ECONOMY});
    			put("TO5", new SeatClass[] {SeatClass.BUSINESS, SeatClass.BUSINESS, SeatClass.BUSINESS});
    			put("TO4", new SeatClass[] {SeatClass.ECONOMY, SeatClass.ECONOMY, SeatClass.ECONOMY});
    		}
    	};
    	
    	Flight flight = new Flight("TR123", 1, 1, 1);
    	BookingClient bc = new BookingClient(offices, flight);
    	List<Thread> threadList = bc.simulate();
    	
    	for (int i = 0; i < flight.getTransactionLog().size(); i++) {
			System.out.println(flight.getTransactionLog().get(i));
		}
    }
}
