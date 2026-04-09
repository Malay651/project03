package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.InventoryDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.util.HibDataSource;

public class InventoryModelHibImpl implements InventoryModelHibInt {

    public long add(InventoryDTO dto) throws ApplicationException {

        Session session = HibDataSource.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(dto);
            tx.commit();

        } catch (HibernateException e) {

            HibDataSource.handleException(e);

            if (tx != null) {
                tx.rollback();
            }

            throw new ApplicationException("Exception in Inventory Add " + e.getMessage());

        } finally {
            session.close();
        }

        return dto.getId();
    }

    public void delete(InventoryDTO dto) {

        Session session = null;
        Transaction tx = null;

        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.delete(dto);
            tx.commit();

        } catch (HibernateException e) {

            if (tx != null) {
                tx.rollback();
            }

        } finally {
            session.close();
        }
    }

    public void update(InventoryDTO dto) throws ApplicationException {

        Session session = null;
        Transaction tx = null;

        try {
            session = HibDataSource.getSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(dto);
            tx.commit();

        } catch (HibernateException e) {

            if (tx != null) {
                tx.rollback();
            }

            HibDataSource.handleException(e);
            throw new ApplicationException("Exception in Inventory Update " + e.getMessage());

        } finally {
            session.close();
        }
    }

    public InventoryDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        InventoryDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (InventoryDTO) session.get(InventoryDTO.class, pk);

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Inventory FindByPK " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    public List search(InventoryDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List search(InventoryDTO dto, int pageNo, int pageSize) {

        Session session = null;
        ArrayList<InventoryDTO> list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(InventoryDTO.class);

            if (dto != null) {

              

                if (dto.getItemName() != null && dto.getItemName().length() > 0) {
                    criteria.add(Restrictions.like("itemName", dto.getItemName() + "%"));
                }

                if (dto.getQuantity() != null) {
                    criteria.add(Restrictions.eq("quantity", dto.getQuantity()));
                }

                if (dto.getPrice() != null) {
                    criteria.add(Restrictions.eq("price", dto.getPrice()));
                }

                if (dto.getItemStatus() != null && dto.getItemStatus().length() > 0) {
                    criteria.add(Restrictions.like("itemStatus", dto.getItemStatus() + "%"));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = (ArrayList<InventoryDTO>) criteria.list();

        } catch (HibernateException e) {

            e.printStackTrace();

        } finally {
            session.close();
        }

        return list;
    }
}