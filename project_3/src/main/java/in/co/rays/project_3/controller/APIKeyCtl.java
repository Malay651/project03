package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.APIKeyDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.APIKeyModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/APIKeyCtl" })
public class APIKeyCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(APIKeyCtl.class);

    /**
     * Validate Fields
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("APIKeyCtl validate method started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("apikeyCode"))) {
            request.setAttribute("apikeyCode",
                    PropertyReader.getValue("error.require", "API Key Code"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("keyvalue"))) {
            request.setAttribute("keyvalue",
                    PropertyReader.getValue("error.require", "Key Value"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("issuedTo"))) {
            request.setAttribute("issuedTo",
                    PropertyReader.getValue("error.require", "Issued To"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status",
                    PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        log.debug("APIKeyCtl validate ended with result: " + pass);

        return pass;
    }

    /**
     * Populate DTO
     */
    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        log.debug("APIKeyCtl populateDTO started");

        APIKeyDTO dto = new APIKeyDTO();

        dto.setApikeyCode(DataUtility.getString(request.getParameter("apikeyCode")));
        dto.setKeyvalue(DataUtility.getString(request.getParameter("keyvalue")));
        dto.setIssuedTo(DataUtility.getString(request.getParameter("issuedTo")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        log.debug("APIKeyCtl populateDTO ended");

        return dto;
    }

    /**
     * Handle GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("APIKeyCtl doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));
        APIKeyModelInt model = ModelFactory.getInstance().getAPIKeyModel();

        if (id > 0) {
            try {
                APIKeyDTO dto = model.findByPk(id);
                ServletUtility.setDto(dto, request);
            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("APIKeyCtl doGet ended");
    }

    /**
     * Handle POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("APIKeyCtl doPost started");

        String op = request.getParameter("operation");
        long id = DataUtility.getLong(request.getParameter("id"));

        APIKeyModelInt model = ModelFactory.getInstance().getAPIKeyModel();

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

            APIKeyDTO dto = (APIKeyDTO) populateDTO(request);

            try {

                if (id > 0) {
                    dto.setId(id);
                    model.update(dto);
                    ServletUtility.setSuccessMessage("API Key Updated Successfully", request);
                } else {
                    model.add(dto);
                    ServletUtility.setSuccessMessage("API Key Added Successfully", request);
                }

                ServletUtility.setDto(dto, request);

            } catch (ApplicationException e) {

                ServletUtility.setErrorMessage(e.getMessage(), request);
                ServletUtility.forward(getView(), request, response);
                return;

            } catch (DuplicateRecordException e) {

                ServletUtility.setErrorMessage("API Key Code Already Exists", request);
                ServletUtility.setDto(dto, request);
                ServletUtility.forward(getView(), request, response);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.APIKEY_CTL, request, response);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.APIKEY_LIST_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("APIKeyCtl doPost ended");
    }

    @Override
    protected String getView() {
        return ORSView.APIKEY_VIEW;
    }
}