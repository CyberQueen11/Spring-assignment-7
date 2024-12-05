package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ActionDaoJpaImpl implements ActionDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Action newAction) {
        System.out.println("Creating action...");
        em.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {
        String query = "SELECT action FROM Action action WHERE action.owningUser = :userId AND action.complete = false";
        return em.createQuery(query, Action.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        System.out.println("Updating action...");
        Action existingAction = em.find(Action.class, actionToUpdate.getActionId());
        if(existingAction == null){
            throw new RecordNotFoundException();
        }
        em.merge(actionToUpdate);
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        System.out.println("Deleting action...");
        Action action = em.find(Action.class, oldAction.getActionId());
        if(action == null){
            throw new RecordNotFoundException();
        }
        em.remove(action);
    }
}