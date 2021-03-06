package com.mvpvaadin.mailexample.readmail;

import com.mvplite.event.EventBus;
import com.mvplite.event.ShowViewEvent;
import com.mvplite.view.NavigateableView;
import com.mvpvaadin.mailexample.data.Mail;
import com.mvpvaadin.mailexample.data.User;
import com.mvpvaadin.mailexample.service.MailService;
import com.mvpvaadin.mailexample.writemail.ShowWriteMailEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

public class ReadMailViewImpl extends Panel implements ReadMailView{

	private static final long serialVersionUID = -582201495923291510L;

	private final EventBus eventBus;
	private final ReadMailPresenter presenter;
	private final NavigateableView parent;
	
	private Button readButton;
	private Label fromLabel;
	private Label toLabel;
	private Label subjectLabel;
	private Label dateLabel;
	private Label messageBodyLabel;
	
	
	public ReadMailViewImpl(NavigateableView parent, EventBus eventBus, 
			User user, MailService mailService){
		this.eventBus = eventBus;
		presenter = new ReadMailPresenter(this, eventBus, user, mailService);
		this.parent = parent;
		generateUI();
	}
	
	private void generateUI(){
		this.setStyleName(Runo.PANEL_LIGHT);
		this.addStyleName("panelWhite");
		
		VerticalLayout layout = new VerticalLayout();
		readButton = new Button("mark as unread");
		readButton.setStyleName(Runo.BUTTON_SMALL);
		readButton.addListener(new ClickListener() {
			
			private static final long serialVersionUID = 960166525199315579L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(presenter.getCurrentMail().isRead()){
					presenter.markMailAsUnread();
					readButton.setCaption("mark as unread");
				}
				else{
					presenter.markMailAsRead();
					readButton.setCaption("mark as read");
				}
			}
		});
		
		
		Button answerButton = new Button("answer");
		answerButton.setStyleName(Runo.BUTTON_SMALL);
		answerButton.addListener(new ClickListener() {
			
			private static final long serialVersionUID = 960166525199315579L;

			@Override
			public void buttonClick(ClickEvent event) {
				eventBus.fireEvent(new ShowWriteMailEvent(
						presenter.getCurrentMail().getSender(), 
						"RE: "+presenter.getCurrentMail().getSubject()));
			}
		});
	
		
		fromLabel = new Label();
		fromLabel.setContentMode(Label.CONTENT_XHTML);
		toLabel = new Label();
		toLabel.setContentMode(Label.CONTENT_XHTML);
		subjectLabel = new Label();
		subjectLabel.setContentMode(Label.CONTENT_XHTML);
		dateLabel = new Label();
		dateLabel.setContentMode(Label.CONTENT_XHTML);
		messageBodyLabel = new Label();
		messageBodyLabel.setContentMode(Label.CONTENT_XHTML);
		
		
		
		
		HorizontalLayout fromButtonLayout = new HorizontalLayout();
		fromButtonLayout.setWidth("100%");
		fromButtonLayout.addComponent(fromLabel);
		fromButtonLayout.addComponent(answerButton);
		fromButtonLayout.addComponent(readButton);
		
		fromButtonLayout.setComponentAlignment(fromLabel, Alignment.MIDDLE_LEFT);
		fromButtonLayout.setComponentAlignment(answerButton, Alignment.MIDDLE_RIGHT);
		fromButtonLayout.setComponentAlignment(readButton, Alignment.MIDDLE_RIGHT);
		fromButtonLayout.setExpandRatio(fromLabel, 1);		
		
		layout.addComponent(fromButtonLayout);
		layout.addComponent(toLabel);
		layout.addComponent(dateLabel);
		layout.addComponent(subjectLabel);
		layout.addComponent(messageBodyLabel);
		
		
		this.setSizeFull();
		layout.setSizeFull();
		layout.setComponentAlignment(fromButtonLayout, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(toLabel, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(dateLabel, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(subjectLabel, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(messageBodyLabel, Alignment.TOP_LEFT);
		
		
		layout.setExpandRatio(messageBodyLabel, 1);
		setContent(layout);
	}
	
	@Override
	public NavigateableView getParentView() {
		return parent;
	}

	@Override
	public String getUriFragment() {
		return ""+presenter.getCurrentMail().getId();
	}

	@Override
	public String getBreadcrumbTitle() {
		return presenter.getCurrentMail().getSubject();
	}

	@Override
	public void setMail(Mail mail) {
		
		this.setCaption(mail.getSubject());
		
		presenter.markMailAsRead();
		
		if (mail.isRead())
			readButton.setCaption("mark as unread");
		else
			readButton.setCaption("mark as read");
		
		fromLabel.setValue("<b>From:</b> "+mail.getSender());
		toLabel.setValue("<b>To:</b> "+mail.getReceiver());
		dateLabel.setValue("<b>Date:</b> "+mail.getDate());
		subjectLabel.setValue("<b>Subject:</b> "+mail.getSubject());
		messageBodyLabel.setValue(mail.getMessage());
		
		fromLabel.setContentMode(Label.CONTENT_XHTML);
		toLabel.setContentMode(Label.CONTENT_XHTML);
		dateLabel.setContentMode(Label.CONTENT_XHTML);
		subjectLabel.setContentMode(Label.CONTENT_XHTML);
		messageBodyLabel.setContentMode(Label.CONTENT_XHTML);
		
		
	}

	@Override
	public ShowViewEvent getEventToShowThisView() {
		return new ShowReadMailEvent(presenter.getCurrentMail());
	}

	public ReadMailPresenter getPresenter() {
		return presenter;
	}

}
