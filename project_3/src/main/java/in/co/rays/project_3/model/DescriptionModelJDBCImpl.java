package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.DescriptionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

public class DescriptionModelJDBCImpl implements DescriptionModelInt {

    private static Logger log = Logger.getLogger(DescriptionModelJDBCImpl.class);
    private Connection con = null;

    // ================= NEXT PK =================
    public long nextPK() throws DatabaseException {

        long pk = 0;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT MAX(ID) FROM ST_DESCRIPTION");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pk = rs.getLong(1);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            log.error("Database Exception..", e);
            throw new DatabaseException("Exception getting next pk");

        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return pk + 1;
    }

    // ================= ADD =================
    public long add(DescriptionDTO dto)
            throws ApplicationException, DuplicateRecordException {

        if (findByDepartmentName(dto.getDepartmentName()) != null) {
            throw new DuplicateRecordException("Department Name already exists");
        }

        long pk = 0;

        try {
            pk = nextPK();
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO ST_DESCRIPTION (ID, DEPARTMENT_NAME, DEPARTMENT_HEAD, LOCATION, STATUS, CREATED_BY, MODIFIED_BY) VALUES (?,?,?,?,?,?,?)");

            ps.setLong(1, pk);
            ps.setString(2, dto.getDepartmentName());
            ps.setString(3, dto.getDepartmentHead());
            ps.setString(4, dto.getLocation());
            ps.setString(5, dto.getStatus());
            ps.setString(6, dto.getCreatedBy());
            ps.setString(7, dto.getModifiedBy());

            ps.executeUpdate();
            ps.close();
            con.commit();

        } catch (Exception e) {

            log.error("Database Exception..", e);

            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Add rollback exception " + ex.getMessage());
            }

            throw new ApplicationException("Exception in add Description");

        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return pk;
    }

    // ================= DELETE =================
    public void delete(DescriptionDTO dto) {

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM ST_DESCRIPTION WHERE ID=?");

            ps.setLong(1, dto.getId());
            ps.executeUpdate();
            ps.close();
            con.commit();

        } catch (Exception e) {

            log.error("Database Exception..", e);

            try {
                con.rollback();
            } catch (Exception ex) {
            }

        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }

    // ================= UPDATE =================
    public void update(DescriptionDTO dto)
            throws ApplicationException, DuplicateRecordException {

        DescriptionDTO existDto = findByDepartmentName(dto.getDepartmentName());

        if (existDto != null && existDto.getId() != dto.getId()) {
            throw new DuplicateRecordException("Department Name already exists");
        }

        try {
            con = JDBCDataSource.getConnection();
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE ST_DESCRIPTION SET DEPARTMENT_NAME=?, DEPARTMENT_HEAD=?, LOCATION=?, STATUS=?, MODIFIED_BY=? WHERE ID=?");

            ps.setString(1, dto.getDepartmentName());
            ps.setString(2, dto.getDepartmentHead());
            ps.setString(3, dto.getLocation());
            ps.setString(4, dto.getStatus());
            ps.setString(5, dto.getModifiedBy());
            ps.setLong(6, dto.getId());

            ps.executeUpdate();
            ps.close();
            con.commit();

        } catch (Exception e) {

            log.error("Database Exception..", e);

            try {
                con.rollback();
            } catch (Exception ex) {
                throw new ApplicationException("Update rollback exception " + ex.getMessage());
            }

            throw new ApplicationException("Exception in update Description");

        } finally {
            JDBCDataSource.closeConnection(con);
        }
    }

    // ================= FIND BY PK =================
    public DescriptionDTO findByPK(long pk) throws ApplicationException {

        DescriptionDTO dto = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM ST_DESCRIPTION WHERE ID=?");

            ps.setLong(1, pk);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                dto = new DescriptionDTO();
                dto.setId(rs.getLong(1));
                dto.setDepartmentName(rs.getString(2));
                dto.setDepartmentHead(rs.getString(3));
                dto.setLocation(rs.getString(4));
                dto.setStatus(rs.getString(5));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in findByPK Description");

        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return dto;
    }

    // ================= FIND BY DEPARTMENT NAME =================
    public DescriptionDTO findByDepartmentName(String departmentName)
            throws ApplicationException {

        DescriptionDTO dto = null;

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM ST_DESCRIPTION WHERE DEPARTMENT_NAME=?");

            ps.setString(1, departmentName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                dto = new DescriptionDTO();
                dto.setId(rs.getLong(1));
                dto.setDepartmentName(rs.getString(2));
                dto.setDepartmentHead(rs.getString(3));
                dto.setLocation(rs.getString(4));
                dto.setStatus(rs.getString(5));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            throw new ApplicationException("Exception in findByDepartmentName");

        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return dto;
    }

    // ================= SEARCH =================
    public List<DescriptionDTO> search(DescriptionDTO dto) {
        return search(dto, 0, 0);
    }

    public List<DescriptionDTO> search(DescriptionDTO dto, int pageNo, int pageSize) {

        StringBuffer sql = new StringBuffer("SELECT * FROM ST_DESCRIPTION WHERE 1=1");

        if (dto != null) {

            if (dto.getId() > 0) {
                sql.append(" AND ID = " + dto.getId());
            }

            if (dto.getDepartmentName() != null && dto.getDepartmentName().length() > 0) {
                sql.append(" AND DEPARTMENT_NAME LIKE '" + dto.getDepartmentName() + "%'");
            }

            if (dto.getLocation() != null && dto.getLocation().length() > 0) {
                sql.append(" AND LOCATION LIKE '" + dto.getLocation() + "%'");
            }
        }

        if (pageSize > 0) {
            pageNo = (pageNo - 1) * pageSize;
            sql.append(" LIMIT " + pageNo + "," + pageSize);
        }

        List<DescriptionDTO> list = new ArrayList<>();

        try {
            con = JDBCDataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                DescriptionDTO dto1 = new DescriptionDTO();

                dto1.setId(rs.getLong(1));
                dto1.setDepartmentName(rs.getString(2));
                dto1.setDepartmentHead(rs.getString(3));
                dto1.setLocation(rs.getString(4));
                dto1.setStatus(rs.getString(5));

                list.add(dto1);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
        } finally {
            JDBCDataSource.closeConnection(con);
        }

        return list;
    }
}