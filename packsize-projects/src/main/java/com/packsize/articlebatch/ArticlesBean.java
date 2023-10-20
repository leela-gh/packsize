package com.packsize.articlebatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.packsize.PackSizeLogger;

@Component
@Scope(value = "session")
public class ArticlesBean {
	
	@Autowired
	private ArticleComponent articleComponent;
	
	public void updateDesignValues() {
		PackSizeLogger.info("In updateDesignValues()");
		
		articleComponent.updateDesignValues();
	}
	
	public void reSet() {
		PackSizeLogger.info("In reSet()");
		
		articleComponent.reSet();
	}
	
	public void validations() {
		PackSizeLogger.info("In validations()");
		
		articleComponent.validations();
	}
	
	public ArticleComponent getArticleComponent() {
		return articleComponent;
	}

	public void setArticleComponent(ArticleComponent articleComponent) {
		this.articleComponent = articleComponent;
	}
}