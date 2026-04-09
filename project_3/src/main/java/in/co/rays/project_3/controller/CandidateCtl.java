package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.CandidateDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.CandidateModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(urlPatterns = { "/ctl/CandidateCtl" })
public class CandidateCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CandidateCtl.class);

	/**
	 * Preload Skill Set List
	 */
	@Override
	protected void preload(HttpServletRequest request) {

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("Java", "Java");
		map.put("Python", "Python");
		map.put("PHP", "PHP");
		map.put("React", "React");
		map.put("Angular", "Angular");

		request.setAttribute("map", map);
	}

	/**
	 * Validate Candidate Fields
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("CandidateCtl validate method started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("candidateCode"))) {
			request.setAttribute("candidateCode", PropertyReader.getValue("error.require", "Candidate Code"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("candidateName"))) {
			request.setAttribute("candidateName", PropertyReader.getValue("error.require", "Candidate Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("email"))) {
			request.setAttribute("email", PropertyReader.getValue("error.require", "Email"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("email"))) {
			request.setAttribute("email", "Invalid Email Format");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("skillSet"))) {
			request.setAttribute("skillSet", PropertyReader.getValue("error.require", "Skill Set"));
			pass = false;
		}

		log.debug("CandidateCtl validate method ended with result: " + pass);

		return pass;
	}

	/**
	 * Populate DTO
	 */
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {

		log.debug("CandidateCtl populateDTO started");

		CandidateDTO dto = new CandidateDTO();

		dto.setCandidateCode(DataUtility.getString(request.getParameter("candidateCode")));
		dto.setCandidateName(DataUtility.getString(request.getParameter("candidateName")));
		dto.setEmail(DataUtility.getString(request.getParameter("email")));
		dto.setSkillSet(DataUtility.getString(request.getParameter("skillSet")));

		populateBean(dto, request);

		log.debug("CandidateCtl populateDTO ended");

		return dto;
	}

	/**
	 * Handle GET
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("CandidateCtl doGet started");

		long id = DataUtility.getLong(request.getParameter("id"));
		CandidateModelInt model = ModelFactory.getInstance().getCandidateModel();

		if (id > 0) {
			try {
				CandidateDTO dto = model.findByPk(id);
				ServletUtility.setDto(dto, request);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("CandidateCtl doGet ended");
	}

	/**
	 * Handle POST
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.debug("CandidateCtl doPost started");

		String op = request.getParameter("operation");
		long id = DataUtility.getLong(request.getParameter("id"));

		CandidateModelInt model = ModelFactory.getInstance().getCandidateModel();

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			CandidateDTO dto = (CandidateDTO) populateDTO(request);

			try {

				if (id > 0) {
					dto.setId(id);
					model.update(dto);
					ServletUtility.setSuccessMessage("Candidate Updated Successfully", request);
				} else {
					model.add(dto);
					ServletUtility.setSuccessMessage("Candidate Added Successfully", request);
				}

				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {

				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.forward(getView(), request, response);
				return;

			} catch (DuplicateRecordException e) {

				ServletUtility.setErrorMessage("Candidate Code Already Exists", request);
				ServletUtility.setDto(dto, request);
				ServletUtility.forward(getView(), request, response);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CANDIDATE_CTL, request, response);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.CANDIDATE_LIST_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("CandidateCtl doPost ended");
	}

	@Override
	protected String getView() {
		return ORSView.CANDIDATE_VIEW;
	}
}