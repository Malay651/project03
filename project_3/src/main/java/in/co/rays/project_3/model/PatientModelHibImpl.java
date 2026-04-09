package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.util.HibDataSource;

public class PatientModelHibImpl implements PatientModelInt {

	@Override
	public long add(PatientDTO dto) throws ApplicationException {

		Session session = HibDataSource.getSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();

		} catch (HibernateException e) {
			
			if (tx != null) {
				tx.rollback();
			}
			HibDataSource.handleException(e);
			throw new ApplicationException("Exception in User Add " + e.getMessage());	
			}finally {
		
			session.close();
		}
		return dto.getId();
	}

	public void delete(PatientDTO dto)  {

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

	public void update(PatientDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in User Add " + e.getMessage());	
		} finally {
			session.close();
		}
	}

	public PatientDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		PatientDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (PatientDTO) session.get(PatientDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in User Add " + e.getMessage());	
		} finally {
			session.close();
		}
		return dto;
	}

	@Override
	public List search(PatientDTO dto) {

		return search(null, 0, 0);
	}

	@Override
	public List search(PatientDTO dto, int pageNo, int pageSize) {

		Session session = null;
		ArrayList<PatientDTO> list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(PatientDTO.class);
			
			if (dto != null) {
				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}
				if (dto.getName() != null && dto.getName().length() > 0) {
					criteria.add(Restrictions.like("name", dto.getName() + "%"));
				}

				if (dto.getDisease() != null && dto.getDisease().length() > 0) {
					criteria.add(Restrictions.like("disease", dto.getDisease() + "%"));
				}

				if (dto.getGender() != null && dto.getGender().length() > 0) {
					criteria.add(Restrictions.like("gender", dto.getGender() + "%"));
				}

			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = (ArrayList<PatientDTO>) criteria.list();

		} catch (HibernateException e) {
			
			

		} finally {

			session.close();
		}

		return list;

	}
}
