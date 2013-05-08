package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Application implements EntryPoint {

	private static final String SERVER_ERROR = "An error occurred while " + "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	public final AppConst appConst = GWT.create(AppConst.class);

	private final IndexGWTWrapperAsync gwtService = GWT.create(IndexGWTWrapper.class);

	public static DialogBox alertWidget(final String header, final String content, int left, int top) {
		final DialogBox box = new DialogBox();
		box.setAnimationEnabled(true);
		if (left == 0 && top == 0) {
			box.center();
		} else {
			box.setPopupPosition(left, top);
		}
		final VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		box.setText(header);
		panel.add(new HTML("<b>" + content + "</b>"));
		final Button buttonClose = new Button("Close", new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				box.hide();
			}
		});

		// few empty labels to make widget larger
		final Label emptyLabel = new Label("");
		emptyLabel.setSize("auto", "25px");
		panel.add(emptyLabel);
		panel.add(emptyLabel);
		buttonClose.setWidth("90px");
		panel.add(buttonClose);
		panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_CENTER);
		box.add(panel);
		return box;
	}

	// Application wide params
	private String loginUserName = "sux";
	private String loginState = appConst.STATUS_LOGGED_IN();
	private String actionState = "Status: " + appConst.VAL_INITIALIZING();
	private int currentAppPage = 1;
	private String appViewMode = appConst.VIEW_MODE_ALL();
	private int idleTime = 0;

	// Global Widgets
	FlexTable menuTable = new FlexTable();
	final FlexTable flex = new FlexTable();
	final HTML adCountLabel = new HTML(appConst.VAL_EMPTY());

	public void onModuleLoad() {

		// Header / Main Container / Footer

		// HEADER
		HorizontalPanel headerPanel = new HorizontalPanel();
		menuTable.addStyleName("cw-FlexTable");
		menuTable.setWidth("1024px");
		menuTable.getRowFormatter().setStyleName(0, "cw-FlexTable-navigation");

		// Buttons
		final Anchor homeButton = new Anchor("Home");
		menuTable.setWidget(0, 0, homeButton);
		menuTable.getCellFormatter().setWidth(0, 0, "16%");
		menuTable.setWidget(0, 1, new Anchor("Add"));
		menuTable.getCellFormatter().setWidth(0, 1, "16%");
		menuTable.setWidget(0, 2, new Anchor("Admin"));
		menuTable.getCellFormatter().setWidth(0, 2, "16%");
		final Anchor loginButton = new Anchor("Login");
		menuTable.setWidget(0, 3, loginButton);
		headerPanel.add(menuTable);
		RootPanel.get("header1").add(headerPanel);

		// Ad Count Label
		HorizontalPanel statusPanel = new HorizontalPanel();
		setActionState(getActionState());
		statusPanel.add(adCountLabel);
		RootPanel.get("body0").add(statusPanel);

		// MAIN CONTAINER
		HorizontalPanel mainPanel = new HorizontalPanel();
		flex.addStyleName("cw-FlexTable");
		flex.setWidth("1024px");
		flex.getColumnFormatter().setWidth(0, "5%");
		flex.getColumnFormatter().setWidth(1, "50%");
		flex.getColumnFormatter().setWidth(2, "18%");
		flex.getColumnFormatter().setWidth(3, "14%");
		mainPanel.add(flex);
		RootPanel.get("body1").add(mainPanel);

		// Page numbers
		HorizontalPanel pagesPanel = new HorizontalPanel();
		final FlexTable pageNumberTable = new FlexTable();
		pageNumberTable.setHTML(0, 0, "Pages");
		pagesPanel.add(pageNumberTable);
		RootPanel.get("body2").add(pagesPanel);

		// FOOTER
		HorizontalPanel footerPanel = new HorizontalPanel();
		FlexTable footerTable = new FlexTable();
		footerTable.addStyleName("cw-FlexTable");
		footerTable.setWidth("1024px");
		footerTable.setHTML(0, 0, "T2CSupp&nbsp;Staff&nbsp;(c)&nbsp;" + appConst.APP_VERSION() + "&nbsp;");
		footerPanel.add(footerTable);
		footerTable.getColumnFormatter().setWidth(0, "100%");
		footerTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		footerTable.getRowFormatter().setStyleName(0, "cw-FlexTable-footer");
		RootPanel.get("footer1").add(footerPanel);

		// Page Load Timer
		HorizontalPanel pageLoadTimerPanel = new HorizontalPanel();
		final HTML pageLoadTimeLabel = new HTML();
		pageLoadTimerPanel.add(pageLoadTimeLabel);
		RootPanel.get("footer2").add(pageLoadTimerPanel);

		// HANDLERS
		// Create a handler for the sendButton and nameField
		class HomeButtonClickHandler implements ClickHandler {

			private int homeButtonClickHandlerPageNumber = 1;
			private int homeButtonCliclHandlerTotalAds = 0;
			private boolean buttonViewAdsPressed = false;
			private int viewAdSelectedRow = 0;
			private double pageTime1 = getMillis();

			public void onClick(ClickEvent event) {

				// Sort of Initialize block
				pageTime1 = getMillis();
				setIdleTime(0);
				setMainListingTablePageNumber(1);
				setCurrentAppPage(1);
				setButtonViewAdsPressed(false);

				// Async Calls
				getTotalAdsFromServer();
				getMainListingByPageFromServer();
			}

			AsyncCallback<Integer> getTotalAdsFromServer = new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {

					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();
					setIdleTime(0);
				}

				@Override
				public void onSuccess(Integer result) {

					setTotalAds(result.intValue());
					adCountLabel.setHTML("<p>Total Ads: " + getTotalAds() + "</p>");
				}

			};

			void drawViewEditTable(String result) {

				// Draw ViewEditBox
				int currentTableRow = 0;

				// Flex
				flex.insertRow(getViewAdSelectedRow());
				flex.getFlexCellFormatter().setColSpan(getViewAdSelectedRow(), 0, 4);

				// View / Edit Box
				HorizontalPanel viewEditPanel = new HorizontalPanel();
				viewEditPanel.setWidth("100%");
				FlexTable viewEditTable = new FlexTable();
				viewEditTable.addStyleName("cw-FlexTable-view-edit-box");
				viewEditTable.setWidth("100%");

				// Name Label
				viewEditTable.setWidget(currentTableRow, 0, new HTML("<b>Name</b>"));
				viewEditTable.getColumnFormatter().setWidth(0, "50px");

				// Text Box
				TextBox nameTextBox = new TextBox();
				Anchor adsName = (Anchor) flex.getWidget(getViewAdSelectedRow() - 1, 1);
				nameTextBox.setText(adsName.getHTML());
				nameTextBox.setWidth("290px");
				nameTextBox.setEnabled(false);
				viewEditTable.setWidget(currentTableRow++, 1, nameTextBox);
				viewEditTable.getColumnFormatter().setWidth(1, "300px");

				// Separator
				HTML htmlSeparator = new HTML("<hr />");
				viewEditTable.getFlexCellFormatter().setColSpan(currentTableRow, 0, 4);
				viewEditTable.setWidget(currentTableRow++, 0, htmlSeparator);

				// Parse JSON
				String json = result;
				JSONArray array = (JSONArray) JSONParser.parseStrict(json);
				JSONObject v;

				// AdDesc
				viewEditTable.setHTML(currentTableRow, 1, "<b>CRITERIA</b>");
				viewEditTable.setHTML(currentTableRow++, 2, "<b>VALUE</b>");
				int i = 0;
				for (i = 0; i < array.size(); i++) {
					v = array.get(i).isObject();

					TextBox criteriaBox = new TextBox();
					criteriaBox.setWidth("290px");
					criteriaBox.setText(new HTML(v.get("criteria").isString().stringValue()).getHTML());
					criteriaBox.setEnabled(false);
					viewEditTable.setWidget(currentTableRow, 1, criteriaBox);

					TextBox valueBox = new TextBox();
					valueBox.setWidth("290px");
					valueBox.setText(new HTML(v.get("value").isString().stringValue()).getHTML());
					valueBox.setEnabled(false);
					viewEditTable.setWidget(currentTableRow++, 2, valueBox);
				}

				// Separator 2
				viewEditTable.getFlexCellFormatter().setColSpan(currentTableRow, 0, 4);
				viewEditTable.setWidget(currentTableRow++, 0, htmlSeparator);

				// Buttons Panel
				HorizontalPanel closeAndSaveButtonPanel = new HorizontalPanel();

				// Close Button
				Button viewEditCloseButton = new Button("Close");
				viewEditCloseButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						setIdleTime(0);
						setCurrentAppPage(1);
						flex.removeRow(getViewAdSelectedRow());
						setButtonViewAdsPressed(false);
					}
				});
				closeAndSaveButtonPanel.add(viewEditCloseButton);

				// Separator
				HTML buttonSeparator = new HTML("&nbsp;");
				closeAndSaveButtonPanel.add(buttonSeparator);

				// Save Button
				Button viewEditSaveButton = new Button("Save");
				viewEditSaveButton.setEnabled(false);
				closeAndSaveButtonPanel.add(viewEditSaveButton);
				viewEditTable.setWidget(currentTableRow, 1, closeAndSaveButtonPanel);
				viewEditPanel.add(viewEditTable);

				// Flex
				flex.setWidget(getViewAdSelectedRow(), 0, viewEditPanel);

				// Ad Count
				adCountLabel.setHTML("<p>Total Ads:&nbsp;" + getTotalAds() + "</p>");

				// Timing
				double pageTime2 = getMillis();
				double pageTime3 = (pageTime2 - pageTime1);
				pageLoadTimeLabel.setHTML("Page generated in " + pageTime3 + " msec");
			}

			AsyncCallback<String> getAdDescFromServer = new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {

					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();
				}

				@Override
				public void onSuccess(String result) {

					drawViewEditTable(result);
				}

			};

			void drawMainListingTable(String result) {

				adCountLabel.setHTML("<p>Total Ads:&nbsp;" + getTotalAds() + "</p>");
				flex.clear();
				flex.removeAllRows();

				// Parse JSON
				String json = result;
				JSONArray array = (JSONArray) JSONParser.parseStrict(json);
				JSONObject v;

				// Header row
				flex.setHTML(0, 0, "<b>Id</b>");
				flex.setHTML(0, 1, "<b>Summary</b>");
				flex.setHTML(0, 2, "<b>Date</b>");
				flex.setHTML(0, 3, "<b>User</b>");
				flex.getRowFormatter().setStyleName(0, "cw-FlexTable-main-list");

				// Data rows
				int arrayBoundary = array.size();

				int i1 = 0;
				for (i1 = 0; i1 < arrayBoundary; i1++) {
					v = array.get(i1).isObject();

					final String adsId = v.get("id").isNumber().toString();
					flex.setText(i1 + 1, 0, adsId);

					// Date
					String dateCreated = v.get("created").isString().stringValue();
					flex.setHTML(i1 + 1, 2, dateCreated);
					flex.setHTML(i1 + 1, 3, v.get("owner").isString().stringValue());

					// Creating Edit Click Handler
					final int selectedRow = i1 + 2;
					final Anchor buttonViewAds = new Anchor();
					String adsName = v.get("name").isString().stringValue();
					buttonViewAds.setText(new HTML(adsName).getHTML());
					buttonViewAds.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							// Disable all buttons from click, if there is
							// one open box
							setIdleTime(0);
							setCurrentAppPage(1);
							if (isButtonViewAdsPressed()) {
								DialogBox buttonPressedBox = alertWidget("Information", "Please, close opened edit box first!",
										buttonViewAds.getAbsoluteLeft(), buttonViewAds.getAbsoluteTop());
								buttonPressedBox.show();
								return;
							}
							setButtonViewAdsPressed(true);
							setViewAdSelectedRow(selectedRow);

							// General
							pageTime1 = getMillis();
							adCountLabel.setHTML("<p>" + appConst.VAL_LOADING() + "</p>");

							// Async call
							getGetAdDescFromServer(Integer.parseInt(adsId));
						}
					});
					flex.setWidget(i1 + 1, 1, buttonViewAds);
				}

				// Draw buttons
				int buttonsToDraw = Math.round(getTotalAds() / appConst.VAL_ADS_PER_MAIN_PAGE());

				if (buttonsToDraw > 0) {
					if (buttonsToDraw * appConst.VAL_ADS_PER_MAIN_PAGE() == getTotalAds())
						buttonsToDraw -= 1;
					pageNumberTable.clear();

					int i2 = 1;
					for (i2 = 1; i2 <= buttonsToDraw + 1; i2++) {
						final Button pageButton = new Button(Integer.toString(i2));
						pageButton.setSize("25px", "25px");

						final int iPage = i2;
						if (i2 == getMainListingTablePageNumber()) {
							pageButton.setHTML("<b>" + iPage + "</b>");
						}

						pageButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {

								// General
								pageTime1 = getMillis();
								setIdleTime(0);
								setCurrentAppPage(1);
								adCountLabel.setHTML("<p>" + appConst.VAL_LOADING() + "</p>");
								setButtonViewAdsPressed(false);

								// Page Button
								int n;
								for (n = 1; n < pageNumberTable.getCellCount(0); n++) {
									Button button = (Button) pageNumberTable.getWidget(0, n);
									button.setEnabled(false);
									if (n == iPage) {
										button.setHTML("<b>" + iPage + "</b>");
									}
								}
								setMainListingTablePageNumber(iPage);

								// Async call
								getMainListingByPageFromServer();
							}
						});
						pageNumberTable.setWidget(0, i2, pageButton);
					}
				}

				// Timing
				double pageTime2 = getMillis();
				double pageTime3 = (pageTime2 - pageTime1);
				pageLoadTimeLabel.setHTML("Page generated in " + pageTime3 + " msec");
			}

			AsyncCallback<String> getMainListingByPageFromServer = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {

					homeButton.setEnabled(true);
					homeButton.setFocus(true);
					setIdleTime(0);
				}

				public void onSuccess(String result) {

					drawMainListingTable(result);
				}
			};

			private void getTotalAdsFromServer() {

				gwtService.getTotalAds(getTotalAdsFromServer);
			}

			private void getMainListingByPageFromServer() {

				homeButton.setEnabled(false);
				gwtService.getMainListing(getMainListingTablePageNumber(), getMainListingByPageFromServer);
			}

			public int getMainListingTablePageNumber() {

				return homeButtonClickHandlerPageNumber;
			}

			public void setMainListingTablePageNumber(int mainButtonClickHandlerPageNumber) {

				this.homeButtonClickHandlerPageNumber = mainButtonClickHandlerPageNumber;
			}

			public int getTotalAds() {

				return homeButtonCliclHandlerTotalAds;
			}

			public void setTotalAds(int mainButtonCliclHandlerTotalAds) {

				this.homeButtonCliclHandlerTotalAds = mainButtonCliclHandlerTotalAds;
			}

			public boolean isButtonViewAdsPressed() {
				return buttonViewAdsPressed;
			}

			public void setButtonViewAdsPressed(boolean buttonViewAdsPressed) {
				this.buttonViewAdsPressed = buttonViewAdsPressed;
			}

			public void getGetAdDescFromServer(int adDescId) {

				gwtService.getAdDesc(adDescId, getAdDescFromServer);
			}

			public int getViewAdSelectedRow() {
				return viewAdSelectedRow;
			}

			public void setViewAdSelectedRow(int viewAdSelectedRow) {
				this.viewAdSelectedRow = viewAdSelectedRow;
			}
		}

		// Login Handler
		class LoginButtonClickHandler implements ClickHandler {

			@SuppressWarnings("unused")
			private double pageTime1 = getMillis();

			@Override
			public void onClick(ClickEvent event) {

				// Sort of Initialize block
				pageTime1 = getMillis();
				// setIdleTime(0);
				setCurrentAppPage(4);
				showLoginPanel();
			}

			public void showLoginPanel() {
				DialogBox newBox = alertWidget("Login Panel", "Idle time is: " + getIdleTime(), 0, 0);
				newBox.show();

				// Panel
			}
		}

		// Add handlers to anchors
		HomeButtonClickHandler mainButtonClickHandler = new HomeButtonClickHandler();
		homeButton.addClickHandler(mainButtonClickHandler);

		LoginButtonClickHandler loginButtonClickHandler = new LoginButtonClickHandler();
		loginButton.addClickHandler(loginButtonClickHandler);

		// InitializeApp()
		mainButtonClickHandler.getTotalAdsFromServer();
		mainButtonClickHandler.getMainListingByPageFromServer();

		// InitializeUser()
		setLoginState(getLoginState());

		// InitializeIdleTime()
		RepeatingCommand idleTimer = new Scheduler.RepeatingCommand() {

			@Override
			public boolean execute() {
				setIdleTime(getIdleTime() + 1);
				return true;
			}
		};
		Scheduler.get().scheduleFixedPeriod(idleTimer, appConst.VAL_SECOND_MS());
	}

	public void changeMenuItemColorAsSelected() {

		switch (getCurrentAppPage()) {
			case 1 :
				menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation-current-page");
				menuTable.getCellFormatter().setStyleName(0, 3, "cw-FlexTable-navigation");
				break;
			case 4 :
				menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation");
				menuTable.getCellFormatter().setStyleName(0, 3, "cw-FlexTable-navigation-current-page");
				break;
			default :
				break;
		}
	}

	public String getLoginState() {

		return loginState;
	}

	public void setLoginState(String action) {

		this.loginState = action;

		// Update Status
		Anchor loginAnchor = (Anchor) menuTable.getWidget(0, 3);
		String loginAnchorText;
		if (action.equals(appConst.STATUS_LOGGED_IN()) || action.equals(appConst.STATUS_LOGGING_IN())) {
			loginAnchorText = "Status: " + action + " as " + getLoginUserName();
		} else {
			loginAnchorText = "Status: " + action;
		}
		loginAnchor.setText(loginAnchorText);
	}
	public String getLoginUserName() {

		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {

		this.loginUserName = loginUserName;
	}

	public int getCurrentAppPage() {

		return currentAppPage;
	}

	public void setCurrentAppPage(int currentAppPage) {

		this.currentAppPage = currentAppPage;
		changeMenuItemColorAsSelected();
	}

	public String getAppViewMode() {

		return appViewMode;
	}

	public void setAppViewMode(String appViewMode) {

		this.appViewMode = appViewMode;
	}

	public double getMillis() {

		return Duration.currentTimeMillis();
	}

	public int getIdleTime() {

		return idleTime;
	}

	public void setIdleTime(int idleTime) {

		this.idleTime = idleTime;
		checkLoginStatus();
	}

	void checkLoginStatus() {

		if ((getLoginState().equals(appConst.STATUS_LOGGED_IN()) || getLoginState().equals(appConst.STATUS_LOGGING_IN()))
				&& getIdleTime() >= appConst.VAL_MAX_IDLE_TIME()) {
			doLogout();
		}
	}

	void doLogout() {

		setLoginState(appConst.STATUS_LOGGING_OUT());
		setLoginUserName(appConst.VAL_EMPTY());

		// Async Call for updating the session
		// TODO
	}

	void doLogin() {

		// Async Call for updating the session
		// TODO
	}

	public String getActionState() {

		return actionState;
	}

	public void setActionState(String actionState) {

		this.actionState = actionState;
		adCountLabel.setText(actionState);
	}
}
