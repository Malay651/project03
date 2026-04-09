package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.TransportationDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.util.HibDataSource;

public class TransportationModelHibImpl implements TransportationModelHibInt {

	public long add(TransportationDTO dto) throws ApplicationException {

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

			throw new ApplicationException("Exception in Transportation Add " + e.getMessage());

		} finally {
			session.close();
		}

		return dto.getId();
	}

	public void delete(TransportationDTO dto) {

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

	public void update(TransportationDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Transportation Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	public TransportationDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		TransportationDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (TransportationDTO) session.get(TransportationDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Transportation FindByPK " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	public List search(TransportationDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(TransportationDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<TransportationDTO> list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(TransportationDTO.class);
			if (dto != null) {

				if (dto.getDescription() != null && dto.getDescription().length() > 0) {
					criteria.add(Restrictions.like("Description", dto.getDescription() + "%"));
					System.out.println(dto.getDescription());
				}

				if (dto.getMode() != null && dto.getMode().length() > 0) {
					criteria.add(Restrictions.like("Mode", dto.getMode() + "%"));
				}

			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = (ArrayList<TransportationDTO>) criteria.list();

		} catch (HibernateException e) {

		e.printStackTrace();
			
		} finally {
			session.close();
		}

		return list;
	}
}
