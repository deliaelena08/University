package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import domain.Customer;
import domain.Vehicle;
import utils.IOHandler;


public class CustomerRepositoryImpl implements CustomerRepository {

	private static String PATH_TO_CUSTOMER_FILE;

	private List<Customer> customers;

	public CustomerRepositoryImpl(String filePath) {
		customers = new ArrayList<>();
		PATH_TO_CUSTOMER_FILE = filePath;
	}

	@Override
	public void addCustomer(Customer customer) {
		FileWriter fileWriter = IOHandler.initializeDataWriter(PATH_TO_CUSTOMER_FILE);
		try {
			fileWriter.write(customer.getPic() + "," + customer.getName() + "," + customer.getAge());
			fileWriter.write("\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally{
			try {
				fileWriter.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	public void initialLoadOfCustomers(String property) {
		// read from a text file the customer information
		try (BufferedReader bufferedReader=IOHandler.initializeBufferReader(property)){
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] arguments = line.split(",");
				Customer customer = new Customer(arguments[0], arguments[1], Integer.parseInt(arguments[2]));
				customers.add(customer);
			}
		} catch (IOException e) {
			System.out.println("Errors while loading data from text file:" + e.getStackTrace());
		}
		//try with resources automatically closes the file

	}

	@Override
	public List<Customer> getAllCustomers(String property) {
		customers.clear();//clear the list of customer before data loading, avoid messing up the data
		initialLoadOfCustomers(property);
		return customers;
	}

}
