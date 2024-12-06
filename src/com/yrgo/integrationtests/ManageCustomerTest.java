package com.yrgo.integrationtests;

/*
Test creating a new customer.
Test finding an existing customer.
*/

import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;

import com.yrgo.services.customers.CustomerNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration({"/other-tiers.xml", "/datasource-test.xml" })
public class ManageCustomerTest {

    @Autowired
    private CustomerManagementService customerService;

    @Disabled
    @Test
    public void testCreateNewCustomer() {

        customerService.newCustomer(new Customer("CS03939", "Acme", "Good Customer"));
        List<Customer> allCustomers = customerService.getAllCustomers();
        assertEquals(1, allCustomers.size());

    }

    @Test
    public void testFindCustomer() throws RecordNotFoundException, CustomerNotFoundException {
        Customer testCustomer = new Customer("CS03939", "Acme", "Good Customer");
        customerService.newCustomer(testCustomer);

        Customer foundCustomer = null;
        try{
            foundCustomer = customerService.findCustomerById("CS03939");
            assertEquals(testCustomer, foundCustomer);
        } catch (CustomerNotFoundException e){
            fail("No customer was found");
        }

 }
}
