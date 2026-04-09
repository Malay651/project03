package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.DescriptionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.util.HibDataSource;

public class DescriptionModelHibImpl implements DescriptionModelInt {

    // ================= ADD =================
    public long add(DescriptionDTO dto) throws ApplicationException {

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

            throw new ApplicationException("Exception in Description Add " + e.getMessage());

        } finally {
            session.close();
        }

        return dto.getId();
    }

    // ================= DELETE =================
    public void delete(DescriptionDTO dto) {

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

    // ================= UPDATE =================
    public void update(DescriptionDTO dto) throws ApplicationException {

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
            throw new ApplicationException("Exception in Description Update " + e.getMessage());

        } finally {
            session.close();
        }
    }

    // ================= FIND BY PK =================
    public DescriptionDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        DescriptionDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (DescriptionDTO) session.get(DescriptionDTO.class, pk);

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Description FindByPK " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    // ================= SEARCH =================
    public List<DescriptionDTO> search(DescriptionDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List<DescriptionDTO> search(DescriptionDTO dto, int pageNo, int pageSize) {

        Session session = null;
        ArrayList<DescriptionDTO> list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(DescriptionDTO.class);

            if (dto != null) {

                if (dto.getDepartmentName() != null && dto.getDepartmentName().length() > 0) {
                    criteria.add(Restrictions.like("departmentName", dto.getDepartmentName() + "%"));
                }

                if (dto.getDepartmentHead() != null && dto.getDepartmentHead().length() > 0) {
                    criteria.add(Restrictions.like("departmentHead", dto.getDepartmentHead() + "%"));
                }

                if (dto.getLocation() != null && dto.getLocation().length() > 0) {
                    criteria.add(Restrictions.like("location", dto.getLocation() + "%"));
                }

                if (dto.getStatus() != null && dto.getStatus().length() > 0) {
                    criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = (ArrayList<DescriptionDTO>) criteria.list();

        } catch (HibernateException e) {
            e.printStackTrace();

        } finally {
            session.close();
        }

        return list;
    }
}