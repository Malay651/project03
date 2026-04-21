package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.UsageDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class UsageModelHibImpl implements UsageModelInt {

    @Override
    public long add(UsageDTO dto) throws ApplicationException, DuplicateRecordException {

        UsageDTO existDTO = findByCode(dto.getUsageCode());

        if (existDTO != null) {
            throw new DuplicateRecordException("Usage Code Already exists");
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

            throw new ApplicationException("Exception in Usage Add " + e.getMessage());

        } finally {
            session.close();
        }

        return dto.getId();
    }

    @Override
    public void delete(UsageDTO dto) {

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

    @Override
    public void update(UsageDTO dto) throws ApplicationException {

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
            throw new ApplicationException("Exception in Usage Update " + e.getMessage());

        } finally {
            session.close();
        }
    }

    @Override
    public UsageDTO findByCode(String code) throws ApplicationException {

        Session session = null;
        UsageDTO dto = null;

        try {
            session = HibDataSource.getSession();

            Query query = session.createQuery("from UsageDTO where usageCode = :code");
            query.setParameter("code", code);

            List list = query.list();

            if (list != null && list.size() > 0) {
                dto = (UsageDTO) list.get(0);
            }

        } catch (HibernateException e) {

            throw new ApplicationException("Exception in Usage findByCode " + e.getMessage());

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return dto;
    }

    @Override
    public UsageDTO findByPk(long pk) throws ApplicationException {

        Session session = null;
        UsageDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (UsageDTO) session.get(UsageDTO.class, pk);

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Usage findByPK " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    @Override
    public List search(UsageDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    @Override
    public List search(UsageDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        ArrayList<UsageDTO> list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(UsageDTO.class);

            if (dto != null) {

                if (dto.getUsageCode() != null && dto.getUsageCode().length() > 0) {
                    criteria.add(Restrictions.like("usageCode", dto.getUsageCode() + "%"));
                }

                if (dto.getUsageName() != null && dto.getUsageName().length() > 0) {
                    criteria.add(Restrictions.like("usageName", dto.getUsageName() + "%"));
                }

                if (dto.getUsageType() != null && dto.getUsageType().length() > 0) {
                    criteria.add(Restrictions.like("usageType", dto.getUsageType() + "%"));
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

            list = (ArrayList<UsageDTO>) criteria.list();

        } catch (HibernateException e) {
            e.printStackTrace();

        } finally {
            session.close();
        }

        return list;
    }
}