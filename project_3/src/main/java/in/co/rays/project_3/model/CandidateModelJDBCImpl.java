package in.co.rays.project_3.model;

import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.CandidateDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.util.JDBCDataSource;

public class CandidateModelJDBCImpl implements CandidateModelInt {

    private static Logger log = Logger.getLogger(CandidateModelJDBCImpl.class);

    /**
     * Get Next PK
     */
    public long nextPK() throws DatabaseException {

        long pk = 0;

        try (Connection con = JDBCDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT MAX(ID) FROM ST_CANDIDATE");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                pk = rs.getLong(1);
            }

        } catch (Exception e) {
            throw new DatabaseException("Exception in getting PK");
        }

        return pk + 1;
    }

    /**
     * Add Candidate
     */
    public long add(CandidateDTO dto) throws ApplicationException {

        long pk = 0;

        try (Connection con = JDBCDataSource.getConnection()) {

            pk = nextPK();
            con.setAutoCommit(false);

            String sql = "INSERT INTO ST_CANDIDATE "
                    + "(ID, CANDIDATE_CODE, CANDIDATE_NAME, EMAIL, SKILL_SET, "
                    + "CREATED_BY, MODIFIED_BY, CREATED_DATETIME, MODIFIED_DATETIME) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setLong(1, pk);
            ps.setString(2, dto.getCandidateCode());
            ps.setString(3, dto.getCandidateName());
            ps.setString(4, dto.getEmail());
            ps.setString(5, dto.getSkillSet());
            ps.setString(6, dto.getCreatedBy());
            ps.setString(7, dto.getModifiedBy());
            ps.setTimestamp(8, dto.getCreatedDatetime());
            ps.setTimestamp(9, dto.getModifiedDatetime());

            ps.executeUpdate();
            con.commit();

        } catch (Exception e) {
            throw new ApplicationException("Exception in add Candidate");
        }

        return pk;
    }

    /**
     * Update Candidate
     */
    public void update(CandidateDTO dto) throws ApplicationException {

        try (Connection con = JDBCDataSource.getConnection()) {

            con.setAutoCommit(false);

            String sql = "UPDATE ST_CANDIDATE SET "
                    + "CANDIDATE_CODE=?, CANDIDATE_NAME=?, EMAIL=?, SKILL_SET=?, "
                    + "CREATED_BY=?, MODIFIED_BY=?, CREATED_DATETIME=?, MODIFIED_DATETIME=? "
                    + "WHERE ID=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, dto.getCandidateCode());
            ps.setString(2, dto.getCandidateName());
            ps.setString(3, dto.getEmail());
            ps.setString(4, dto.getSkillSet());
            ps.setString(5, dto.getCreatedBy());
            ps.setString(6, dto.getModifiedBy());
            ps.setTimestamp(7, dto.getCreatedDatetime());
            ps.setTimestamp(8, dto.getModifiedDatetime());
            ps.setLong(9, dto.getId());

            ps.executeUpdate();
            con.commit();

        } catch (Exception e) {
            throw new ApplicationException("Exception in update Candidate");
        }
    }

    /**
     * Delete Candidate
     */
    public void delete(CandidateDTO dto) {

        try (Connection con = JDBCDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM ST_CANDIDATE WHERE ID=?")) {

            ps.setLong(1, dto.getId());
            ps.executeUpdate();

        } catch (Exception e) {
        }
    }

    /**
     * Find By PK
     */
    public CandidateDTO findByPK(long pk) throws ApplicationException {

        CandidateDTO dto = null;

        try (Connection con = JDBCDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM ST_CANDIDATE WHERE ID=?")) {

            ps.setLong(1, pk);
            ResultSet rs = ps.executeQuery();

           
        } catch (Exception e) {
            throw new ApplicationException("Exception in findByPK Candidate");
        }

        return dto;
    }

    /**
     * Search with Pagination
     */
    public List<CandidateDTO> search(CandidateDTO dto, int pageNo, int pageSize)
            throws ApplicationException {

        List<CandidateDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ST_CANDIDATE WHERE 1=1");

        if (dto != null) {

            if (dto.getCandidateCode() != null && dto.getCandidateCode().length() > 0) {
                sql.append(" AND CANDIDATE_CODE LIKE '" + dto.getCandidateCode() + "%'");
            }

            if (dto.getCandidateName() != null && dto.getCandidateName().length() > 0) {
                sql.append(" AND CANDIDATE_NAME LIKE '" + dto.getCandidateName() + "%'");
            }

            if (dto.getEmail() != null && dto.getEmail().length() > 0) {
                sql.append(" AND EMAIL LIKE '" + dto.getEmail() + "%'");
            }

            if (dto.getSkillSet() != null && dto.getSkillSet().length() > 0) {
                sql.append(" AND SKILL_SET LIKE '" + dto.getSkillSet() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }

        try (Connection con = JDBCDataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            
        } catch (Exception e) {
            throw new ApplicationException("Exception in search Candidate");
        }

        return list;
    }

	@Override
	public List search(CandidateDTO dto) throws ApplicationException {

		return search(dto, 0, 0);
	}

	@Override
	public CandidateDTO findByCode(String code) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CandidateDTO findByPk(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

  
 
}