package in.co.rays.project_3.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.EmployeeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

/**
 * Hibernate implementation of Employee Model
 * 
 * This class performs CRUD operations on Employee using Hibernate
 * 
 * @author Rays
 * @version 1.0
 */
public class EmployeeModelHibImpl implements EmployeeModelInt {

	private static Logger log = Logger.getLogger(EmployeeModelHibImpl.class);

	/**
	 * Adds a new Employee
	 * 
	 * @param dto EmployeeDTO
	 * @return primary key
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("EmployeeModelHibImpl add method started");

		EmployeeDTO existDto = findByUsername(dto.getUsername());
		if (existDto != null) {
			throw new DuplicateRecordException("User Already already exist");
		}

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
			throw new ApplicationException("Exception in Employee Add " + e.getMessage());

		} finally {
			session.close();
		}

		log.debug("EmployeeModelHibImpl add method ended");
		return dto.getId();
	}

	/**
	 * Updates Employee
	 * 
	 * @param dto EmployeeDTO
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public void update(EmployeeDTO dto) throws ApplicationException, DuplicateRecordException {

		log.debug("EmployeeModelHibImpl update method started");

		Session session = null;
		Transaction tx = null;

		EmployeeDTO existDto = findByUsername(dto.getUsername());
		if (existDto != null && existDto.getId() != dto.getId()) {
			throw new DuplicateRecordException("Username is already exist");
		}

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
			throw new ApplicationException("Exception in Employee update " + e.getMessage());

		} finally {
			session.close();
		}

		log.debug("EmployeeModelHibImpl update method ended");
	}

	/**
	 * Deletes Employee
	 * 
	 * @param dto EmployeeDTO
	 * @throws ApplicationException
	 */
	public void delete(EmployeeDTO dto) throws ApplicationException {

		log.debug("EmployeeModelHibImpl delete method started");

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
			HibDataSource.handleException(e);
			throw new ApplicationException("Exception in Employee Delete " + e.getMessage());

		} finally {
			session.close();
		}

		log.debug("EmployeeModelHibImpl delete method ended");
	}

	/**
	 * Finds Employee by Primary Key
	 * 
	 * @param pk primary key
	 * @return EmployeeDTO
	 * @throws ApplicationException
	 */
	public EmployeeDTO findByPK(long pk) throws ApplicationException {

		log.debug("EmployeeModelHibImpl findByPK method started");

		Session session = null;
		EmployeeDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (EmployeeDTO) session.get(EmployeeDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception in getting employee by pk");

		} finally {
			session.close();
		}

		log.debug("EmployeeModelHibImpl findByPK method ended");
		return dto;
	}

	/**
	 * Finds Employee by Username
	 * 
	 * @param username login id
	 * @return EmployeeDTO
	 * @throws ApplicationException
	 */
	public EmployeeDTO findByUsername(String username) throws ApplicationException {

		log.debug("EmployeeModelHibImpl findByUsername method started");

		Session session = null;
		EmployeeDTO dto = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);
			criteria.add(Restrictions.eq("username", username));
			List list = criteria.list();

			if (list.size() == 1) {
				dto = (EmployeeDTO) list.get(0);
			}

		} catch (HibernateException e) {
			e.printStackTrace();
			HibDataSource.handleException(e);
			throw new ApplicationException("Exception in getting employee by username ");

		} finally {
			session.close();
		}

		log.debug("EmployeeModelHibImpl findByUsername method ended");
		return dto;
	}

	/**
	 * Returns list of all Employees
	 * 
	 * @return List
	 * @throws ApplicationException
	 */
	public List list() throws ApplicationException {

		log.debug("EmployeeModelHibImpl list method started");
		List list = search(null, 0, 0);
		log.debug("EmployeeModelHibImpl list method ended");
		return list;
	}

	/**
	 * Searches Employees with pagination
	 * 
	 * @param dto      EmployeeDTO
	 * @param pageNo   page number
	 * @param pageSize page size
	 * @return List
	 * @throws ApplicationException
	 */
	public List search(EmployeeDTO dto, int pageNo, int pageSize) throws ApplicationException {

		log.debug("EmployeeModelHibImpl search method started");

		Session session = null;
		List list = null;

		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(EmployeeDTO.class);

			if (dto != null) {

				if (dto.getId() != null) {
					criteria.add(Restrictions.like("id", dto.getId()));
				}

				if (dto.getFullName() != null && dto.getFullName().length() > 0) {
					criteria.add(Restrictions.like("fullName", dto.getFullName() + "%"));
				}

				if (dto.getUsername() != null && dto.getUsername().length() > 0) {
					criteria.add(Restrictions.like("username", dto.getUsername() + "%"));
				}

				if (dto.getPassword() != null && dto.getPassword().length() > 0) {
					criteria.add(Restrictions.like("password", dto.getPassword() + "%"));
				}

				if (dto.getBirthDate() != null && dto.getBirthDate().getTime() > 0) {
					criteria.add(Restrictions.eq("birthDate", dto.getBirthDate()));
				}

				if (dto.getContactNo() != null && dto.getContactNo().length() > 0) {
					criteria.add(Restrictions.like("contactNo", dto.getContactNo() + "%"));
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}

			list = criteria.list();

		} catch (HibernateException e) {
			
			HibDataSource.handleException(e);
			throw new ApplicationException("Exception in Employee search");

		} finally {
			session.close();
		}

		log.debug("EmployeeModelHibImpl search method ended");
		return list;
	}
}
