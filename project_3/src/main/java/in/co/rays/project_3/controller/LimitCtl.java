package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.LimitDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.LimitModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/LimitCtl" })
public class LimitCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(LimitCtl.class);

    /**
     * Preload Status List
     */
    @Override
    protected void preload(HttpServletRequest request) {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("Active", "Active");
        map.put("Inactive", "Inactive");

        request.setAttribute("statusMap", map);
    }

    /**
     * Validate Fields
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("LimitCtl validate method started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("limitCode"))) {
            request.setAttribute("limitCode", PropertyReader.getValue("error.require", "Limit Code"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("limitName"))) {
            request.setAttribute("limitName", PropertyReader.getValue("error.require", "Limit Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("maxValue"))) {
            request.setAttribute("maxValue", PropertyReader.getValue("error.require", "Max Value"));
            pass = false;
        } else if (!DataValidator.isInteger(request.getParameter("maxValue"))) {
            request.setAttribute("maxValue", "Max Value must be a number");
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status", PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        log.debug("LimitCtl validate method ended with result: " + pass);

        return pass;
    }

    /**
     * Populate DTO
     */
    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        log.debug("LimitCtl populateDTO started");

        LimitDTO dto = new LimitDTO();

        dto.setLimitCode(DataUtility.getString(request.getParameter("limitCode")));
        dto.setLimitName(DataUtility.getString(request.getParameter("limitName")));
        dto.setMaxValue(DataUtility.getString(request.getParameter("maxValue")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        log.debug("LimitCtl populateDTO ended");

        return dto;
    }

    /**
     * Handle GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("LimitCtl doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));
        LimitModelInt model = ModelFactory.getInstance().getLimitModel();

        if (id > 0) {
            try {
                LimitDTO dto = model.findByPk(id);
                ServletUtility.setDto(dto, request);
            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("LimitCtl doGet ended");
    }

    /**
     * Handle POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("LimitCtl doPost started");

        String op = request.getParameter("operation");
        long id = DataUtility.getLong(request.getParameter("id"));

        LimitModelInt model = ModelFactory.getInstance().getLimitModel();

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

            LimitDTO dto = (LimitDTO) populateDTO(request);

            try {

                if (id > 0) {
                    dto.setId(id);
                    model.update(dto);
                    ServletUtility.setSuccessMessage("Limit Updated Successfully", request);
                } else {
                    model.add(dto);
                    ServletUtility.setSuccessMessage("Limit Added Successfully", request);
                }

                ServletUtility.setDto(dto, request);

            } catch (ApplicationException e) {

                ServletUtility.setErrorMessage(e.getMessage(), request);
                ServletUtility.forward(getView(), request, response);
                return;

            } catch (DuplicateRecordException e) {

                ServletUtility.setErrorMessage("Limit Code Already Exists", request);
                ServletUtility.setDto(dto, request);
                ServletUtility.forward(getView(), request, response);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.LIMIT_CTL, request, response);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.LIMIT_LIST_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("LimitCtl doPost ended");
    }

    @Override
    protected String getView() {
        return ORSView.LIMIT_VIEW;
    }
}