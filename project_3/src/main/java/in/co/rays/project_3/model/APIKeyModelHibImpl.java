package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.APIKeyDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class APIKeyModelHibImpl implements APIKeyModelInt {

    public long add(APIKeyDTO dto) throws ApplicationException, DuplicateRecordException {

        APIKeyDTO existDTO = findByCode(dto.getApikeyCode());

        if (existDTO != null) {
            throw new DuplicateRecordException("API Key Code already exists");
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

            throw new ApplicationException("Exception in APIKey Add " + e.getMessage());

        } finally {
            session.close();
        }

        return dto.getId();
    }

    public void delete(APIKeyDTO dto) {

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

    public void update(APIKeyDTO dto) throws ApplicationException {

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
            throw new ApplicationException("Exception in APIKey Update " + e.getMessage());

        } finally {
            session.close();
        }
    }

    public APIKeyDTO findByCode(String code) throws ApplicationException {

        Session session = null;
        APIKeyDTO dto = null;

        try {
            session = HibDataSource.getSession();

            Query query = session.createQuery("from APIKeyDTO where apikeyCode = :code");
            query.setParameter("code", code);

            List list = query.list();

            if (list != null && list.size() > 0) {
                dto = (APIKeyDTO) list.get(0);
            }

        } catch (HibernateException e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return dto;
    }

    public APIKeyDTO findByPk(long pk) throws ApplicationException {

        Session session = null;
        APIKeyDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (APIKeyDTO) session.get(APIKeyDTO.class, pk);

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in APIKey FindByPK " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    public List search(APIKeyDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    public List search(APIKeyDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        ArrayList<APIKeyDTO> list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(APIKeyDTO.class);

            if (dto != null) {

                if (dto.getApikeyCode() != null && dto.getApikeyCode().length() > 0) {
                    criteria.add(Restrictions.like("apikeyCode", dto.getApikeyCode() + "%"));
                }

                if (dto.getKeyvalue() != null && dto.getKeyvalue().length() > 0) {
                    criteria.add(Restrictions.like("keyvalue", dto.getKeyvalue() + "%"));
                }

                if (dto.getIssuedTo() != null && dto.getIssuedTo().length() > 0) {
                    criteria.add(Restrictions.like("issuedTo", dto.getIssuedTo() + "%"));
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

            list = (ArrayList<APIKeyDTO>) criteria.list();

        } catch (HibernateException e) {
            e.printStackTrace();

        } finally {
            session.close();
        }

        return list;
    }
}