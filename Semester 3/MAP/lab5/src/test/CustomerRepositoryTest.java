package test;

import domain.Customer;
import domain.Vehicle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import repository.CustomerRepository;
import repository.CustomerRepositoryImpl;
import utils.MileageUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CustomerRepositoryTest {

	private CustomerRepository customerRepository;
	private static final String PROPERTY_TO_LOAD_DATA="customerTestLoadFile";
	@Before
	public void setUp() {
		this.customerRepository = new CustomerRepositoryImpl(PROPERTY_TO_LOAD_DATA);
	}

	@Test
	public void testAddCustomer() {
		Customer c1 = new Customer("1", "Ana", 20);
		Customer c2 = new Customer("2", "Bob", 30);
		c1.setVehicle(new Vehicle("NT50TOD", 12.5D, 2018, MileageUnit.KM));
		customerRepository.addCustomer(c1);
		customerRepository.addCustomer(c2);
		customerRepository.initialLoadOfCustomers(PROPERTY_TO_LOAD_DATA);
		List<Customer> customerList = customerRepository.getAllCustomers(PROPERTY_TO_LOAD_DATA);
		// Verificări
		assertEquals(2, customerList.size());
		assertEquals(c1.getName(), customerList.get(0).getName());
		assertEquals(c2.getName(), customerList.get(1).getName());
	}
}
