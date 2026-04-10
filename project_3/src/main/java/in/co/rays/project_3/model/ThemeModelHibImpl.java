package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.ThemeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class ThemeModelHibImpl implements ThemeModelInt {

    @Override
    public long add(ThemeDTO dto) throws ApplicationException, DuplicateRecordException {

        ThemeDTO existDTO = findByCode(dto.getThemeCode());

        if (existDTO != null) {
            throw new DuplicateRecordException("Theme Code already exists");
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

            throw new ApplicationException("Exception in Theme Add " + e.getMessage());

        } finally {
            session.close();
        }

        return dto.getId();
    }

    @Override
    public void delete(ThemeDTO dto) throws ApplicationException {

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

            throw new ApplicationException("Exception in Theme Delete " + e.getMessage());

        } finally {
            session.close();
        }
    }

    @Override
    public void update(ThemeDTO dto) throws ApplicationException, DuplicateRecordException {

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
            throw new ApplicationException("Exception in Theme Update " + e.getMessage());

        } finally {
            session.close();
        }
    }

    public ThemeDTO findByCode(String code) throws ApplicationException {

        Session session = null;
        ThemeDTO dto = null;

        try {
            session = HibDataSource.getSession();

            Query query = session.createQuery("from ThemeDTO where themeCode = :code");
            query.setParameter("code", code);

            List list = query.list();

            if (list != null && list.size() > 0) {
                dto = (ThemeDTO) list.get(0);
            }

        } catch (HibernateException e) {

            throw new ApplicationException("Exception in Theme findByCode " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    @Override
    public ThemeDTO findByPK(long pk) throws ApplicationException {

        Session session = null;
        ThemeDTO dto = null;

        try {
            session = HibDataSource.getSession();
            dto = (ThemeDTO) session.get(ThemeDTO.class, pk);

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Theme findByPK " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    @Override
    public ThemeDTO findByName(String name) throws ApplicationException {

        Session session = null;
        ThemeDTO dto = null;

        try {
            session = HibDataSource.getSession();

            Query query = session.createQuery("from ThemeDTO where themeName = :name");
            query.setParameter("name", name);

            List list = query.list();

            if (list != null && list.size() > 0) {
                dto = (ThemeDTO) list.get(0);
            }

        } catch (HibernateException e) {

            throw new ApplicationException("Exception in Theme findByName " + e.getMessage());

        } finally {
            session.close();
        }

        return dto;
    }

    @Override
    public List list() throws ApplicationException {
        return list(0, 0);
    }

    @Override
    public List list(int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        List list = null;

        try {
            session = HibDataSource.getSession();
            Query query = session.createQuery("from ThemeDTO");

            if (pageSize > 0) {
                pageNo = (pageNo - 1) * pageSize;
                query.setFirstResult(pageNo);
                query.setMaxResults(pageSize);
            }

            list = query.list();

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Theme list " + e.getMessage());

        } finally {
            session.close();
        }

        return list;
    }

    @Override
    public List search(ThemeDTO dto) throws ApplicationException {
        return search(dto, 0, 0);
    }

    @Override
    public List search(ThemeDTO dto, int pageNo, int pageSize) throws ApplicationException {

        Session session = null;
        ArrayList<ThemeDTO> list = null;

        try {
            session = HibDataSource.getSession();
            Criteria criteria = session.createCriteria(ThemeDTO.class);

            if (dto != null) {

                if (dto.getThemeCode() != null && dto.getThemeCode().length() > 0) {
                    criteria.add(Restrictions.like("themeCode", dto.getThemeCode() + "%"));
                }

                if (dto.getThemeName() != null && dto.getThemeName().length() > 0) {
                    criteria.add(Restrictions.like("themeName", dto.getThemeName() + "%"));
                }

                if (dto.getColor() != null && dto.getColor().length() > 0) {
                    criteria.add(Restrictions.like("color", dto.getColor() + "%"));
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

            list = (ArrayList<ThemeDTO>) criteria.list();

        } catch (HibernateException e) {
            throw new ApplicationException("Exception in Theme search " + e.getMessage());

        } finally {
            session.close();
        }

        return list;
    }
}