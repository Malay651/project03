package in.co.rays.project_3.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CandidateDTO;
import in.co.rays.project_3.dto.CourseDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.CandidateModelInt;
import in.co.rays.project_3.model.CourseModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

/**
 * course functionality ctl.to perform add,delete ,update operation
 * 
 * @authormalay dongre
 *
 */

@WebServlet(urlPatterns = { "/ctl/CourseCtl" })
public class CourseCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CourseCtl.class);

	protected boolean validate(HttpServletRequest request) {

		log.debug("course ctl validate start");

		boolean pass = true;
		if (DataValidator.isNull(request.getParameter("courseName"))) {
			request.setAttribute("courseName", PropertyReader.getValue("error.require", "Course name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("courseName"))) {
			request.setAttribute("courseName", PropertyReader.getValue("error.name", "Course name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		} /*
			 * else if (!DataValidator.isName(request.getParameter("description"))) {
			 * request.setAttribute("description", PropertyReader.getValue("error.name",
			 * "Description")); pass = false; }
			 */
		if (DataValidator.isNull(request.getParameter("duration"))) {
			request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
			pass = false;
		}
		log.debug("course ctl validate end");
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug("course ctl populate bean start");

		CourseDTO dto = new CourseDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setCourseName(DataUtility.getString(request.getParameter("courseName")));
		dto.setDescription(DataUtility.getString(request.getParameter("description")));
		dto.setDuration(DataUtility.getString(request.getParameter("duration")));
		populateBean(dto, request);

		log.debug("course ctl populate bean end");

		return dto;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("course ctl do get start");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));
		CourseModelInt model = ModelFactory.getInstance().getCourseModel();

		if (id > 0 || op != null) {
			CourseDTO dto;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("course ctl do get end");
	}

	/**
	 * Submit logic inside it
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("course ctl do post start");

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		CourseModelInt model = ModelFactory.getInstance().getCourseModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			CourseDTO dto = (CourseDTO) populateDTO(request);

			try {

				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setSuccessMessage("Course Updated Successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Course Added Successfully", request);
				}

				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {

				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.forward(getView(), request, response);
				return;

			} catch (DuplicateRecordException e) {

				ServletUtility.setErrorMessage("Course Already Exists", request);
				ServletUtility.setDto(dto, request);
				ServletUtility.forward(getView(), request, response);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.COURSE_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("course ctl do post end");

	}

	@Override
	protected String getView() {

		return ORSView.COURSE_VIEW;
	}

}
