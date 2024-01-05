package com.packsize.login;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.packsize.PackSizeLogger;

@Component
@SessionScope
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	private static final Logger logger = LogManager.getLogger();
	
	private String pwd;
	private String msg;
	private String user;

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	//validate login
	public void validateUsernamePassword() {
		logger.info("In validateUsernamePassword");
		
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		boolean valid = true;
		if (valid) {
			HttpSession session = SessionUtils.getSession();
			session.setAttribute("username", user);
			try {
				//context.redirect(context.getRequestContextPath() + "/warehousepages/warehouseLanding.xhtml");
				context.redirect(context.getRequestContextPath() + "/timecard/timeCardHome.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Username and Passowrd",
							"Please enter correct username and Password"));
		}
	}

	//logout event, invalidate session
	public void logout() {
		logger.info("In logout");
		
		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		try {
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			context.redirect(context.getRequestContextPath() + "/login.xhtml");
		} catch (IOException e) {
			PackSizeLogger.error(e.getMessage());
		}
	}
}
