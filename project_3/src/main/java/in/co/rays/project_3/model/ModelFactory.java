package in.co.rays.project_3.model;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * ModelFactory decides which model implementation run....
 * 
 * @authormalay dongre
 * 
 */
public final class ModelFactory {

	private static ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.project_3.bundle.system");
	private static final String DATABASE = rb.getString("DATABASE");
	private static ModelFactory mFactory = null;
	private static HashMap modelCache = new HashMap();

	private ModelFactory() {

	}

	public static ModelFactory getInstance() {
		if (mFactory == null) {
			mFactory = new ModelFactory();
		}
		return mFactory;
	}
	
	public CandidateModelInt getCandidateModel(){

		CandidateModelInt candidateModel = (CandidateModelInt) modelCache.get("candidateModel");
		if (candidateModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				candidateModel = new CandidateModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				candidateModel = new CandidateModelJDBCImpl();
			}
			modelCache.put("candidateModel", candidateModel);
		}
		return candidateModel;
	}
	
	
	public DescriptionModelInt getDescriptionModel() {

		DescriptionModelInt descriptionModel = (DescriptionModelInt) modelCache.get("descriptionModel");
		if (descriptionModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				descriptionModel = new DescriptionModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				descriptionModel = new DescriptionModelJDBCImpl();
			}
			modelCache.put("descriptionModel", descriptionModel);
		}

		return descriptionModel;
	}
	
	public InventoryModelHibInt getInventoryModel() {

		InventoryModelHibInt inventoryModel = (InventoryModelHibInt) modelCache.get("inventoryModel");
		if (inventoryModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				inventoryModel = new InventoryModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				inventoryModel = new InventoryModelJDBCImpl();
			}
			modelCache.put("inventoryModel", inventoryModel);
		}

		return inventoryModel;
	}

	
	public TransportationModelHibInt getTransportationModel() {

		TransportationModelHibInt transportationModel = (TransportationModelHibInt) modelCache.get("transportationModel");
		if (transportationModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				transportationModel = new TransportationModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				transportationModel = new TransportationModelJDBCImpl();
			}
			modelCache.put("transportationModel", transportationModel);
		}

		return transportationModel;
	}

	public EmployeeModelInt getEmployeeModel() {

		EmployeeModelInt employeeModel = (EmployeeModelInt) modelCache.get("employeeModel");
		if (employeeModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				employeeModel = new EmployeeModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				employeeModel = new EmployeeModelJDBCImpl();
			}
			modelCache.put("employeeModel", employeeModel);
		}

		return employeeModel;
	}

	public PatientModelInt getPatientModel() {
		PatientModelInt patientModel = (PatientModelInt) modelCache.get("patientModel");
		if (patientModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				patientModel = new PatientModelHibImpl();
			}
			if ("JDBC".equals(DATABASE)) {
				patientModel = new PatientModelHibImpl();
			}
			modelCache.put("patientModel", patientModel);
		}
		return patientModel;
	}

	public UserModelInt getUserModel() {

		UserModelInt userModel = (UserModelInt) modelCache.get("userModel");
		if (userModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				userModel = new UserModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				userModel = new UserModelJDBCImpl();
			}
			modelCache.put("userModel", userModel);
		}

		return userModel;
	}

	public MarksheetModelInt getMarksheetModel() {
		MarksheetModelInt marksheetModel = (MarksheetModelInt) modelCache.get("marksheetModel");
		if (marksheetModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				marksheetModel = new MarksheetModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				marksheetModel = new MarksheetModelJDBCImpl();
			}
			modelCache.put("marksheetModel", marksheetModel);
		}
		return marksheetModel;
	}

	public CollegeModelInt getCollegeModel() {
		CollegeModelInt collegeModel = (CollegeModelInt) modelCache.get("collegeModel");
		if (collegeModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				collegeModel = new CollegeModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				collegeModel = new CollegeModelJDBCImpl();
			}
			modelCache.put("collegeModel", collegeModel);
		}
		return collegeModel;
	}

	public RoleModelInt getRoleModel() {
		RoleModelInt roleModel = (RoleModelInt) modelCache.get("roleModel");
		if (roleModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				roleModel = new RoleModelHibImp();

			}
			if ("JDBC".equals(DATABASE)) {
				roleModel = new RoleModelJDBCImpl();
			}
			modelCache.put("roleModel", roleModel);
		}
		return roleModel;
	}

	public StudentModelInt getStudentModel() {
		StudentModelInt studentModel = (StudentModelInt) modelCache.get("studentModel");
		if (studentModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				studentModel = new StudentModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				studentModel = new StudentModelJDBCImpl();
			}
			modelCache.put("studentModel", studentModel);
		}

		return studentModel;
	}

	public CourseModelInt getCourseModel() {
		CourseModelInt courseModel = (CourseModelInt) modelCache.get("courseModel");
		if (courseModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				courseModel = new CourseModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				courseModel = new CourseModelJDBCImpl();
			}
			modelCache.put("courseModel", courseModel);
		}

		return courseModel;
	}

	public TimetableModelInt getTimetableModel() {

		TimetableModelInt timetableModel = (TimetableModelInt) modelCache.get("timetableModel");

		if (timetableModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				timetableModel = new TimetableModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				timetableModel = new TimetableModelJDBCImpl();
			}
			modelCache.put("timetableModel", timetableModel);
		}

		return timetableModel;
	}

	public SubjectModelInt getSubjectModel() {
		SubjectModelInt subjectModel = (SubjectModelInt) modelCache.get("subjectModel");
		if (subjectModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				subjectModel = new SubjectModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				subjectModel = new SubjectModelJDBCImpl();
			}
			modelCache.put("subjectModel", subjectModel);
		}

		return subjectModel;
	}

	public FacultyModelInt getFacultyModel() {
		FacultyModelInt facultyModel = (FacultyModelInt) modelCache.get("facultyModel");
		if (facultyModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				facultyModel = new FacultyModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				facultyModel = new FacultyModelJDBCImpl();
			}
			modelCache.put("facultyModel", facultyModel);
		}

		return facultyModel;
	}

	public ProductModelInt getProductModel() {
		ProductModelInt productModel = (ProductModelInt) modelCache.get("productModel");
		if (productModel == null) {
			if ("Hibernate".equals(DATABASE)) {
				productModel = new ProductModelHibImp();
			}
			if ("JDBC".equals(DATABASE)) {
				productModel = new ProductModelHibImp();
			}
			modelCache.put("productModel", productModel);
		}
		return productModel;
	}
}
