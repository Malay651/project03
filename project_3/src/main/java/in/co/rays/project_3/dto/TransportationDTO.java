package in.co.rays.project_3.dto;

import java.util.Date;

public class TransportationDTO extends BaseDTO{
	
	private String Description;
	
	private String Mode;
	
	private Date orderDate;
	
	private String Cost;

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getMode() {
		return Mode;
	}

	public void setMode(String mode) {
		Mode = mode;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getCost() {
		return Cost;
	}

	public void setCost(String cost) {
		Cost = cost;
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
