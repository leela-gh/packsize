package com.packsize.articlebatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session")
public class ArticlesBean {
	
	private static final Logger logger = LogManager.getLogger();
	@Autowired
	private ArticleComponent articleComponent;
	
	public void updateDesignValues() {
		logger.info("In updateDesignValues()");
		
		articleComponent.updateDesignValues();
	}
	
	public void reSet() {
		logger.info("In reSet()");
		
		articleComponent.reSet();
	}
	
	public void validations() {
		logger.info("In validations()");
		
		articleComponent.validations();
	}
	
	public ArticleComponent getArticleComponent() {
		return articleComponent;
	}

	public void setArticleComponent(ArticleComponent articleComponent) {
		this.articleComponent = articleComponent;
	}
}