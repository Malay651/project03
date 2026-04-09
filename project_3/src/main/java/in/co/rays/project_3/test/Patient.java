package in.co.rays.project_3.test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.dto.RoleDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PatientModelHibImpl;
import in.co.rays.project_3.model.PatientModelInt;
import in.co.rays.project_3.model.UserModelInt;

public class Patient {

	public static PatientModelInt model = new PatientModelHibImpl();

	public static void main(String[] args) throws ApplicationException {
		testAdd();

		testSearch();
	}

	private static void testSearch() {

		PatientDTO dto1 = new PatientDTO();
		// Agar filter chahiye to uncomment karo
		// dto1.setName("Rahul");
		// dto1.setDisease("Fever");

		List<PatientDTO> list = model.search(null, 0, 0);

		for (PatientDTO dto : list) {
			System.out.println(dto.getId() + "\t" + dto.getName() + "\t" + dto.getDisease());
		}

	}

	private static void testAdd() throws ApplicationException {

		PatientDTO dto = new PatientDTO();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		dto.setId(1L);
		dto.setName("Akbar");
		dto.setGender("Male");
		dto.setDisease("Covid");
		dto.setMobileNo("9876543213");
		dto.setCreatedBy("root");
		dto.setModifiedBy("root");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));

		long pk = model.add(dto);
		System.out.println("nextgggggg" + pk);

	}

}
