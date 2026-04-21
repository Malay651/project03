package in.co.rays.project_3.dto;

public class LimitDTO extends BaseDTO {

	private String limitCode;
	private String limitName;
	private String maxValue;
	private String status;
	
	public String getLimitCode() {
		return limitCode;
	}
	public void setLimitCode(String limitCode) {
		this.limitCode = limitCode;
	}
	public String getLimitName() {
		return limitName;
	}
	public void setLimitName(String limitName) {
		this.limitName = limitName;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
