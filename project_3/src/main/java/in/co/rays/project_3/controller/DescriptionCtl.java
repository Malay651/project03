package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.DescriptionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.DescriptionModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/DescriptionCtl" })
public class DescriptionCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(DescriptionCtl.class);

    @Override
    protected void preload(HttpServletRequest request) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Active", "Active");
        map.put("Inactive", "Inactive");

        request.setAttribute("map", map);
    }

    /**
     * Validate Description input fields
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("DescriptionCtl validate method started");
        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("departmentName"))) {
            request.setAttribute("departmentName",
                    PropertyReader.getValue("error.require", "Department Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("departmentHead"))) {
            request.setAttribute("departmentHead",
                    PropertyReader.getValue("error.require", "Department Head"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("location"))) {
            request.setAttribute("location",
                    PropertyReader.getValue("error.require", "Location"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status",
                    PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        log.debug("DescriptionCtl validate method ended with result: " + pass);
        return pass;
    }

    /**
     * Populate DescriptionDTO from request
     */
    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        log.debug("DescriptionCtl populateDTO method started");

        DescriptionDTO dto = new DescriptionDTO();

        dto.setDepartmentName(DataUtility.getString(request.getParameter("departmentName")));
        dto.setDepartmentHead(DataUtility.getString(request.getParameter("departmentHead")));
        dto.setLocation(DataUtility.getString(request.getParameter("location")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        log.debug("DescriptionCtl populateDTO method ended");

        return dto;
    }

    /**
     * Handles GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("DescriptionCtl doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));
        DescriptionModelInt model = ModelFactory.getInstance().getDescriptionModel();

        if (id > 0) {
            try {
                DescriptionDTO dto = model.findByPK(id);
                ServletUtility.setDto(dto, request);
            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("DescriptionCtl doGet ended");
    }

    /**
     * Handles POST request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("DescriptionCtl doPost started");

        String op = request.getParameter("operation");
        long id = DataUtility.getLong(request.getParameter("id"));

        DescriptionModelInt model = ModelFactory.getInstance().getDescriptionModel();

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

            DescriptionDTO dto = (DescriptionDTO) populateDTO(request);

            try {
                if (id > 0) {
                    dto.setId(id);
                    model.update(dto);
                    ServletUtility.setSuccessMessage("Description Updated Successfully", request);
                } else {
                    model.add(dto);
                    ServletUtility.setSuccessMessage("Description Added Successfully", request);
                }

                ServletUtility.setDto(dto, request);

            } catch (ApplicationException e) {
                ServletUtility.setErrorMessage(e.getMessage(), request);
                ServletUtility.forward(getView(), request, response);
                return;

            } catch (DuplicateRecordException e) {
                ServletUtility.setErrorMessage("Department Name Already Exists", request);
                ServletUtility.setDto(dto, request);
                ServletUtility.forward(getView(), request, response);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.DESCRITPION_CTL, request, response);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.DESCRIPTION_LIST_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);
        log.debug("DescriptionCtl doPost ended");
    }

    @Override
    protected String getView() {
        log.debug("DescriptionCtl getView called");
        return ORSView.DESCRIPTION_VIEW;
    }
}