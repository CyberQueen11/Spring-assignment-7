package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomerDaoJpaImpl implements CustomerDao{

    private static final String GET_ALL_CUSTOMERS = "SELECT customer FROM Customer customer";
    private static final String GET_BY_ID = "SELECT customer FROM Customer customer WHERE customer.customerId = :customerId";
    private static final String GET_BY_NAME = "SELECT customer FROM Customer customer WHERE customer.companyName = :name";
    private static final String GET_FULL_CUST_DETAIL = "SELECT customer FROM Customer customer LEFT JOIN FETCH customer.calls WHERE customer.customerId = :customerId";

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        System.out.println("Creating customer...");
        em.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try{
            return(Customer)em.createQuery(GET_BY_ID)
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String name) {
        return em.createQuery(GET_BY_NAME, Customer.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        System.out.println("Updating customer...");
        Customer existingCustomer = em.find(Customer.class, customerToUpdate.getCustomerId());
        if(existingCustomer == null){
            throw new RecordNotFoundException();
        }
        em.merge(customerToUpdate);
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        System.out.println("Deleting customer...");
        Customer cust = em.find(Customer.class, oldCustomer.getCustomerId());
        if (cust == null) {
            throw new RecordNotFoundException();
        }
        em.remove(cust);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em.createQuery(GET_ALL_CUSTOMERS, Customer.class).getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        try {
            return em.createQuery(GET_FULL_CUST_DETAIL, Customer.class)
                    .setParameter("customerId", customerId)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e){
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer cust = em.find(Customer.class, customerId);
        if(cust == null){
            throw new RecordNotFoundException();
        }
        cust.addCall(newCall);
        em.merge(cust);
    }
}
