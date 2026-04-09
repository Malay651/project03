package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.CandidateDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class CandidateModelHibImpl implements CandidateModelInt {

	public long add(CandidateDTO dto) throws ApplicationException, DuplicateRecordException {

		CandidateDTO existDTO = findByCode(dto.getCandidateCode());

		if (existDTO != null) {

			throw new DuplicateRecordException("Candidate Code Already exist");

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

			throw new ApplicationException("Exception in Candidate Add " + e.getMessage());

		} finally {
			session.close();
		}

		return dto.getId();
	}

	public void delete(CandidateDTO dto) {

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

	public void update(CandidateDTO dto) throws ApplicationException {

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
			throw new ApplicationException("Exception in Candidate Update " + e.getMessage());

		} finally {
			session.close();
		}
	}

	public CandidateDTO findByCode(String code) throws ApplicationException {

		Session session = null;
		CandidateDTO dto = null;

		try {
			session = HibDataSource.getSession();

			// Check entity name here - replace 'CandidateDTO' with actual entity name if needed
			Query query = session.createQuery("from CandidateDTO where candidateCode = ?");

			query.setParameter("code", code);

			List list = query.list();

			if (list != null && list.size() > 0) {
				dto = (CandidateDTO) list.get(0);
			}

		} catch (HibernateException e) {

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return dto;
	}
	public CandidateDTO findByPk(long pk) throws ApplicationException {

		Session session = null;
		CandidateDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (CandidateDTO) session.get(CandidateDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in Candidate FindByPK " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	public List search(CandidateDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List search(CandidateDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		ArrayList<CandidateDTO> list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(CandidateDTO.class);

			if (dto != null) {

				if (dto.getCandidateCode() != null && dto.getCandidateCode().length() > 0) {
					criteria.add(Restrictions.like("candidateCode", dto.getCandidateCode() + "%"));
				}

				if (dto.getCandidateName() != null && dto.getCandidateName().length() > 0) {
					criteria.add(Restrictions.like("candidateName", dto.getCandidateName() + "%"));
				}

				if (dto.getEmail() != null && dto.getEmail().length() > 0) {
					criteria.add(Restrictions.like("email", dto.getEmail() + "%"));
				}

				if (dto.getSkillSet() != null && dto.getSkillSet().length() > 0) {
					criteria.add(Restrictions.like("skillSet", dto.getSkillSet() + "%"));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = (ArrayList<CandidateDTO>) criteria.list();

		} catch (HibernateException e) {
			e.printStackTrace();

		} finally {
			session.close();
		}

		return list;
	}
}