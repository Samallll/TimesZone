package com.timeszone.model.dto;

public class ReturnReasonDTO {

	private Integer returReasonId;
	
	private String returnReason;
	
	private String comment;
	
	private Integer orderId;

	public Integer getReturReasonId() {
		return returReasonId;
	}

	public void setReturReasonId(Integer returReasonId) {
		this.returReasonId = returReasonId;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public ReturnReasonDTO() {
		super();
	}

}
