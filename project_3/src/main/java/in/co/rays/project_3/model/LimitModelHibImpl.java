package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.LimitDTO;
import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class LimitModelHibImpl implements LimitModelInt {

    public long add(LimitDTO dto) throws ApplicationException, DuplicateRecordException {

        LimitDTO existDTO = findByCode(dto.getLimitCode());

        if (existDTO != null) {
            throw new DuplicateRecordException("Limit Code already exists");
        }

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

            throw new ApplicationException("Exception in Limit Add " + e.getMessage());

        } finally {
            session.close();
        }

        return dto.getId();
    }

    public void delete(LimitDTO dto) {

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

    public void update(LimitDTO dto) throws ApplicationException {

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
            throw new ApplicationException("Exception in Limit Update " + e.getMessage());

        } finally {
            session.close();
        }
    }

    public LimitDTO findByCode(String code) throws ApplicationException {

        Session session = null;
        LimitDTO dto = null;

        try {
            session = HibDataSource.getSession();

            Criteria criteria = session.createCriteria(LimitDTO.class);
			criteria.add(Restrictions.eq("limitCode", code));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (LimitDTO) list.get(0);
			}

        } catch (HibernateException e) {

            throw new ApplicationException("Exception in Limit findByCode " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    public LimitDTO findByPk(long pk) throws ApplicationException {

        Session session = null;
        LimitDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (LimitDTO) session.get(LimitDTO.class, pk);

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Limit FindByPK " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    public List search(LimitDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List search(LimitDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        ArrayList<LimitDTO> list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(LimitDTO.class);

            if (dto != null) {

                if (dto.getLimitCode() != null && dto.getLimitCode().length() > 0) {
                    criteria.add(Restrictions.like("limitCode", dto.getLimitCode() + "%"));
                }

                if (dto.getLimitName() != null && dto.getLimitName().length() > 0) {
                    criteria.add(Restrictions.like("limitName", dto.getLimitName() + "%"));
                }

                if (dto.getStatus() != null && dto.getStatus().length() > 0) {
                    criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
                }

                if (dto.getMaxValue() != null && dto.getMaxValue().length() > 0) {
                    criteria.add(Restrictions.like("maxValue", dto.getMaxValue()));
                }
            }

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                criteria.setFirstResult(pageNo);
                criteria.setMaxResults(pageSize);
            }

            list = (ArrayList<LimitDTO>) criteria.list();

        } catch (HibernateException e) {
            e.printStackTrace();

        } finally {
            session.close();
        }

        return list;
    }
}