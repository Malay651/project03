package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.UsageDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.UsageModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/UsageCtl" })
public class UsageCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(UsageCtl.class);

    /**
     * Preload Usage Type List
     */
    @Override
    protected void preload(HttpServletRequest request) {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("Internal", "Internal");
        map.put("External", "External");
        map.put("Commercial", "Commercial");

        request.setAttribute("usageTypeMap", map);
    }

    /**
     * Validate Fields
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("UsageCtl validate method started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("usageCode"))) {
            request.setAttribute("usageCode",
                    PropertyReader.getValue("error.require", "Usage Code"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("usageName"))) {
            request.setAttribute("usageName",
                    PropertyReader.getValue("error.require", "Usage Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("usageType"))) {
            request.setAttribute("usageType",
                    PropertyReader.getValue("error.require", "Usage Type"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status",
                    PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        log.debug("UsageCtl validate method ended with result: " + pass);

        return pass;
    }

    /**
     * Populate DTO
     */
    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        log.debug("UsageCtl populateDTO started");

        UsageDTO dto = new UsageDTO();

        dto.setUsageCode(DataUtility.getString(request.getParameter("usageCode")));
        dto.setUsageName(DataUtility.getString(request.getParameter("usageName")));
        dto.setUsageType(DataUtility.getString(request.getParameter("usageType")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        log.debug("UsageCtl populateDTO ended");

        return dto;
    }

    /**
     * Handle GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("UsageCtl doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));
        UsageModelInt model = ModelFactory.getInstance().getUsageModel();

        if (id > 0) {
            try {
                UsageDTO dto = model.findByPk(id);
                ServletUtility.setDto(dto, request);
            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("UsageCtl doGet ended");
    }

    /**
     * Handle POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("UsageCtl doPost started");

        String op = request.getParameter("operation");
        long id = DataUtility.getLong(request.getParameter("id"));

        UsageModelInt model = ModelFactory.getInstance().getUsageModel();

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

            UsageDTO dto = (UsageDTO) populateDTO(request);

            try {

                if (id > 0) {
                    dto.setId(id);
                    model.update(dto);
                    ServletUtility.setSuccessMessage("Usage Updated Successfully", request);
                } else {
                    model.add(dto);
                    ServletUtility.setSuccessMessage("Usage Added Successfully", request);
                }

                ServletUtility.setDto(dto, request);

            } catch (ApplicationException e) {

                ServletUtility.setErrorMessage(e.getMessage(), request);
                ServletUtility.forward(getView(), request, response);
                return;

            } catch (DuplicateRecordException e) {

                ServletUtility.setErrorMessage("Usage Code Already Exists", request);
                ServletUtility.setDto(dto, request);
                ServletUtility.forward(getView(), request, response);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.USAGE_CTL, request, response);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.USAGE_LIST_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("UsageCtl doPost ended");
    }

    @Override
    protected String getView() {
        return ORSView.USAGE_VIEW;
    }
}