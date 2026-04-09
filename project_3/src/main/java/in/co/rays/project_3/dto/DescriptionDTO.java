package in.co.rays.project_3.dto;

public class DescriptionDTO extends BaseDTO {

	private String departmentName;

	private String departmentHead;

	private String location;

	private String status;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentHead() {
		return departmentHead;
	}

	public void setDepartmentHead(String departmentHead) {
		this.departmentHead = departmentHead;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getKey() {

		return null;
	}

	@Override
	public String getValue() {

		return null;
	}

}
