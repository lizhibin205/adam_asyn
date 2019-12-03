/**
 * 
 */
package org.springframework.adam.common.bean;

/**
 * @author user
 *
 */
public class ThreadHolder {

	private long begin;

	private String runningAccountId;

	private Integer runningAccountFlag;

	private Integer requestLogFlag;

	private String remark;

	public long getBegin() {
		return begin;
	}

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public String getRunningAccountId() {
		return runningAccountId;
	}

	public void setRunningAccountId(String runningAccountId) {
		this.runningAccountId = runningAccountId;
	}

	public Integer getRunningAccountFlag() {
		return runningAccountFlag;
	}

	public void setRunningAccountFlag(Integer runningAccountFlag) {
		this.runningAccountFlag = runningAccountFlag;
	}

	public Integer getRequestLogFlag() {
		return requestLogFlag;
	}

	public void setRequestLogFlag(Integer requestLogFlag) {
		this.requestLogFlag = requestLogFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void copy(ThreadHolder threadHolder) {
		this.begin = threadHolder.getBegin();
		this.runningAccountId = threadHolder.getRunningAccountId();
		this.runningAccountFlag = threadHolder.getRunningAccountFlag();
		this.requestLogFlag = threadHolder.getRequestLogFlag();
		this.remark = threadHolder.getRemark();
	}

	public ThreadHolder clone() {
		ThreadHolder th = new ThreadHolder();
		th.setBegin(this.begin);
		th.setRunningAccountId(this.runningAccountId);
		th.setRunningAccountFlag(this.runningAccountFlag);
		th.setRequestLogFlag(this.requestLogFlag);
		th.setRemark(this.remark);
		return th;
	}

	public void append(ThreadHolder threadHolder) {
		this.remark = this.remark + threadHolder.getRemark();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(512);
		builder.append("ThreadHolder [begin=");
		builder.append(begin);
		builder.append(", runningAccountId=");
		builder.append(runningAccountId);
		builder.append(", runningAccountFlag=");
		builder.append(runningAccountFlag);
		builder.append(", requestLogFlag=");
		builder.append(requestLogFlag);
		builder.append(", remark=");
		builder.append(remark);
		builder.append("]");
		return builder.toString();
	}

}
