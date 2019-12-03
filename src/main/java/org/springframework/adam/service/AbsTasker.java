/**
 * 
 */
package org.springframework.adam.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.adam.client.ILogService;
import org.springframework.adam.common.bean.ResultVo;
import org.springframework.adam.common.bean.ServiceInfo;
import org.springframework.adam.common.bean.contants.BaseReslutCodeConstants;
import org.springframework.adam.common.utils.AdamExceptionUtils;
import org.springframework.adam.service.task.DoComplateTasker;
import org.springframework.adam.service.task.DoFailTasker;
import org.springframework.adam.service.task.DoServiceTasker;
import org.springframework.adam.service.task.DoSuccessTasker;

/**
 * @author USER
 *
 */
public abstract class AbsTasker<T1, T2> {

	private static final Log log = LogFactory.getLog(AbsTasker.class);

	protected ServiceInfo<T1, T2> serviceInfo;

	protected ILogService logService;

	protected IServiceBefore<T1, T2> serviceBefore;

	protected String type;

	public AbsTasker(IService<T1, T2> service, ILogService logService, IServiceBefore<T1, T2> serviceBefore, String type) {
		super();
		if (null != service) {
			this.serviceInfo = new ServiceInfo<T1, T2>(service);
		} else {
			this.serviceInfo = null;
		}
		this.logService = logService;
		this.serviceBefore = serviceBefore;
		this.type = type;
	}

	public abstract AbsCallbacker doTask(T1 income, ResultVo<T2> output) throws Exception;

	/**
	 * 增加头日志
	 * 
	 * @param service
	 * @param income
	 * @param output
	 * @param methodName
	 * @param remark
	 */
	protected void addBeginLog(T1 income, ResultVo<T2> output) {
		addLog(income, output, "begin", null);
	}

	/**
	 * 增加尾日志
	 * 
	 * @param service
	 * @param income
	 * @param output
	 * @param methodName
	 * @param remark
	 * @param begin
	 */
	protected void addEndLog(T1 income, ResultVo<T2> output, Long beginTime) {
		addLog(income, output, "end", beginTime);
	}

	/**
	 * 增加日志
	 * 
	 * @param service
	 * @param income
	 * @param output
	 * @param methodName
	 * @param remark
	 * @param begin
	 */
	private void addLog(T1 income, ResultVo<T2> output, String remark, Long beginTime) {
		if (!logService.isNeedLog()) {
			return;
		}

		if (null == serviceInfo) {
			return;
		}

		String methodName = serviceInfo.getSimpleClassName() + "." + this.type;
		try {
			logService.sendRunningAccountLog(income, output, methodName, remark, beginTime);
		} catch (Exception e) {
			log.error("logService error", e);
		}
	}

	/**
	 * @param income
	 * @param output
	 * @param retryTime
	 * @param isSetResultCode
	 * @return
	 */
	protected AbsCallbacker exc(T1 income, ResultVo<T2> output, boolean isSetResultCode) {
		AbsCallbacker absCallbacker = null;
		// 如果为空则说明只有一种情况就是DoFinalTask的,但是也不会走到这里的
		if (null == this.serviceInfo) {
			return null;
		}
		long begin = System.currentTimeMillis();
		addBeginLog(income, output);
		try {
			if (DoServiceTasker.TYPE.equals(this.type)) {
				absCallbacker = serviceInfo.getService().doService(income, output);
			} else if (DoSuccessTasker.TYPE.equals(this.type)) {
				absCallbacker = serviceInfo.getService().doSuccess(income, output);
			} else if (DoFailTasker.TYPE.equals(this.type)) {
				absCallbacker = serviceInfo.getService().doFail(income, output);
			} else if (DoComplateTasker.TYPE.equals(this.type)) {
				absCallbacker = serviceInfo.getService().doComplate(income, output);
			}
		} catch (Exception e) {
			log.error(e, e);
			if (isSetResultCode) {
				if (output.success()) {
					output.setResultCode(this.getClass(), BaseReslutCodeConstants.CODE_900000);
				}
			}
			output.setResultMsg("system error occur:" + AdamExceptionUtils.getStackTrace(e));
			// 不能放finally，要不然resultCode就不是真实的
		} finally {
			addEndLog(income, output, begin);
		}
		return absCallbacker;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ServiceInfo<T1, T2> getServiceInfo() {
		return serviceInfo;
	}

}
