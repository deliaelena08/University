package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import domain.Customer;
import repository.CustomerRepository;

public class CustomerServiceImpl implements CustomerService{
	private static final String PROPERTY_TO_LOAD_DATA="customerInitialLoadFile";
	private CustomerRepository customerRepository;
	

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}


	@Override
	public List<Customer> sortCustomersByNameAscending() {
		List<Customer> customers=customerRepository.getAllCustomers(PROPERTY_TO_LOAD_DATA);
		List<Customer> sortedCustomers=customers.stream().
				sorted(Comparator.comparing(Customer::getName))
				.toList();
		return sortedCustomers;
	}



	@Override
	public void addCustomer(Customer customer) {
		customerRepository.addCustomer(customer);		
	}



	@Override
	public List<Customer> getAll(String property) {
		return customerRepository.getAllCustomers(property);
	}

}
