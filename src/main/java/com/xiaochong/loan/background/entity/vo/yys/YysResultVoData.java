package com.xiaochong.loan.background.entity.vo.yys;

public class YysResultVoData {
	
	private String task_id;//认证任务的ID
	private String guide_code;//只是下一步作什么
	private String guide_message;//下一步指示的描述
	private String img_code;//base64的图片验证码
	private String trace_id;//trace_id
	
	
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	public String getGuide_code() {
		return guide_code;
	}
	public void setGuide_code(String guide_code) {
		this.guide_code = guide_code;
	}
	public String getGuide_message() {
		return guide_message;
	}
	public void setGuide_message(String guide_message) {
		this.guide_message = guide_message;
	}
	public String getImg_code() {
		return img_code;
	}
	public void setImg_code(String img_code) {
		this.img_code = img_code;
	}

	public String getTrace_id() {
		return trace_id;
	}

	public void setTrace_id(String trace_id) {
		this.trace_id = trace_id;
	}
}
