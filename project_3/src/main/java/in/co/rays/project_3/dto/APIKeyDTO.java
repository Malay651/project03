package in.co.rays.project_3.dto;

public class APIKeyDTO extends BaseDTO {

	private String apikeyCode;
	private String keyvalue;
	private String issuedTo;
	private String status;
	
	
	public String getApikeyCode() {
		return apikeyCode;
	}
	public void setApikeyCode(String apikeyCode) {
		this.apikeyCode = apikeyCode;
	}
	public String getKeyvalue() {
		return keyvalue;
	}
	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}
	public String getIssuedTo() {
		return issuedTo;
	}
	public void setIssuedTo(String issuedTo) {
		this.issuedTo = issuedTo;
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
