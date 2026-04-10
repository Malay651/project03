package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.ThemeDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ThemeModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/ThemeCtl" })
public class ThemeCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;
    private static Logger log = Logger.getLogger(ThemeCtl.class);

    /**
     * Preload (Status List)
     */
    @Override
    protected void preload(HttpServletRequest request) {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("Active", "Active");
        map.put("Inactive", "Inactive");

        request.setAttribute("statusMap", map);
    }

    /**
     * Validate Theme Fields
     */
    @Override
    protected boolean validate(HttpServletRequest request) {

        log.debug("ThemeCtl validate method started");

        boolean pass = true;

        if (DataValidator.isNull(request.getParameter("themeCode"))) {
            request.setAttribute("themeCode",
                    PropertyReader.getValue("error.require", "Theme Code"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("themeName"))) {
            request.setAttribute("themeName",
                    PropertyReader.getValue("error.require", "Theme Name"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("color"))) {
            request.setAttribute("color",
                    PropertyReader.getValue("error.require", "Color"));
            pass = false;
        }

        if (DataValidator.isNull(request.getParameter("status"))) {
            request.setAttribute("status",
                    PropertyReader.getValue("error.require", "Status"));
            pass = false;
        }

        log.debug("ThemeCtl validate method ended with result: " + pass);

        return pass;
    }

    /**
     * Populate DTO
     */
    @Override
    protected BaseDTO populateDTO(HttpServletRequest request) {

        log.debug("ThemeCtl populateDTO started");

        ThemeDTO dto = new ThemeDTO();

        dto.setThemeCode(DataUtility.getString(request.getParameter("themeCode")));
        dto.setThemeName(DataUtility.getString(request.getParameter("themeName")));
        dto.setColor(DataUtility.getString(request.getParameter("color")));
        dto.setStatus(DataUtility.getString(request.getParameter("status")));

        populateBean(dto, request);

        log.debug("ThemeCtl populateDTO ended");

        return dto;
    }

    /**
     * Handle GET
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("ThemeCtl doGet started");

        long id = DataUtility.getLong(request.getParameter("id"));
        ThemeModelInt model = ModelFactory.getInstance().getThemeModel();

        if (id > 0) {
            try {
                ThemeDTO dto = model.findByPK(id);
                ServletUtility.setDto(dto, request);
            } catch (ApplicationException e) {
                ServletUtility.handleException(e, request, response);
                return;
            }
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("ThemeCtl doGet ended");
    }

    /**
     * Handle POST
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        log.debug("ThemeCtl doPost started");

        String op = request.getParameter("operation");
        long id = DataUtility.getLong(request.getParameter("id"));

        ThemeModelInt model = ModelFactory.getInstance().getThemeModel();

        if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

            ThemeDTO dto = (ThemeDTO) populateDTO(request);

            try {

                if (id > 0) {
                    dto.setId(id);
                    model.update(dto);
                    ServletUtility.setSuccessMessage("Theme Updated Successfully", request);
                } else {
                    model.add(dto);
                    ServletUtility.setSuccessMessage("Theme Added Successfully", request);
                }

                ServletUtility.setDto(dto, request);

            } catch (ApplicationException e) {

                ServletUtility.setErrorMessage(e.getMessage(), request);
                ServletUtility.forward(getView(), request, response);
                return;

            } catch (DuplicateRecordException e) {

                ServletUtility.setErrorMessage("Theme Code Already Exists", request);
                ServletUtility.setDto(dto, request);
                ServletUtility.forward(getView(), request, response);
                return;
            }

        } else if (OP_RESET.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.THEME_CTL, request, response);
            return;

        } else if (OP_CANCEL.equalsIgnoreCase(op)) {

            ServletUtility.redirect(ORSView.THEME_LIST_CTL, request, response);
            return;
        }

        ServletUtility.forward(getView(), request, response);

        log.debug("ThemeCtl doPost ended");
    }

    @Override
    protected String getView() {
        return ORSView.THEME_VIEW;
    }
}