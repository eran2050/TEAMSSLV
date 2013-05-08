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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
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
	private String loginUserName = "SUX";
	private String loginState = appConst.STATUS_LOGGED_IN();
	private String actionState = appConst.VAL_INITIALIZING();
	private int currentAppPage = 1;
	private String appViewMode = appConst.VIEW_MODE_ALL();
	private int idleTime = 0;
	private double pageLoadingStartTime = getMillis();
	private int mainListingPageNumber = 1;
	private int totalAds = 0;
	private boolean buttonViewAdsPressed = false;
	private int viewAdSelectedRow = 0;

	// Global Widgets
	FlexTable menuTable = new FlexTable();
	final FlexTable flex = new FlexTable();
	final HTML appActionLabel = new HTML(appConst.VAL_EMPTY());
	final HTML pageLoadTimeLabel = new HTML();

	// Resources
	final Image imageLoading = new Image();
	final Image imageCross = new Image();
	final Image imagePlus = new Image();

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
		statusPanel.add(appActionLabel);
		statusPanel.add(imageLoading);
		imageLoading.setVisible(false);
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
		pageNumberTable.setVisible(false);
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
		pageLoadTimerPanel.add(pageLoadTimeLabel);
		RootPanel.get("footer2").add(pageLoadTimerPanel);

		// Init Resources
		imageLoading.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/loading.gif");

		// HANDLERS
		// Create a handler for the sendButton and nameField
		class HomeButtonClickHandler implements ClickHandler {

			public void onClick(ClickEvent event) {

				// Sort of Initialize block
				setPageLoadingStartTime(getMillis());
				setIdleTime(0);
				setMainListingPageNumber(1);
				setCurrentAppPage(1);
				setButtonViewAdsPressed(false);
				setActionState(appConst.VAL_LOADING());

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
					setActionState(appConst.VAL_TOTAL_ADS());
				}

			};

			void drawViewEditTable(String result) {

				// Draw ViewEditBox
				int currentTableRow = 0;
				String ownerName = flex.getText(getViewAdSelectedRow() - 1, 3);
				boolean isEditor = isLoggedIn() && ownerName.equals(getLoginUserName());

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
				nameTextBox.setEnabled(isEditor);
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

					// Criteria Box
					TextBox criteriaBox = new TextBox();
					criteriaBox.setWidth("290px");
					criteriaBox.setText(new HTML(v.get("criteria").isString().stringValue()).getHTML());
					criteriaBox.setEnabled(isEditor);
					viewEditTable.setWidget(currentTableRow, 1, criteriaBox);

					// Value Box
					TextBox valueBox = new TextBox();
					valueBox.setWidth("290px");
					valueBox.setText(new HTML(v.get("value").isString().stringValue()).getHTML());
					valueBox.setEnabled(isEditor);
					viewEditTable.setWidget(currentTableRow, 2, valueBox);

					// Image Cross
					if (isEditor) {
						final Image newCrossImage = new Image();
						newCrossImage.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/cross.png");
						viewEditTable.setWidget(currentTableRow, 3, newCrossImage);
						viewEditTable.getCellFormatter().setAlignment(currentTableRow, 3, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
					}

					// Increase iterator
					currentTableRow++;
				}

				// Image Pluss
				if (isEditor) {
					final Image newPlusImage = new Image();
					newPlusImage.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/plus.gif");
					viewEditTable.setWidget(currentTableRow++, 1, newPlusImage);
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
				HTML buttonSeparator = new HTML("&nbsp;&nbsp;");
				closeAndSaveButtonPanel.add(buttonSeparator);

				// Save Button
				Button viewEditSaveButton = new Button("Save");
				viewEditSaveButton.setEnabled(isEditor);
				viewEditSaveButton.setVisible(isEditor);
				closeAndSaveButtonPanel.add(viewEditSaveButton);
				viewEditTable.setWidget(currentTableRow, 1, closeAndSaveButtonPanel);
				viewEditPanel.add(viewEditTable);

				// Flex
				// TODO check for validity
				flex.insertRow(getViewAdSelectedRow());
				flex.getFlexCellFormatter().setColSpan(getViewAdSelectedRow(), 0, 4);
				flex.setWidget(getViewAdSelectedRow(), 0, viewEditPanel);

				// Ad Count
				setActionState(appConst.VAL_TOTAL_ADS());

				// Timing
				setLoadingTime(getMillis() - getPageLoadingStartTime());
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

							// General
							setIdleTime(0);
							setCurrentAppPage(1);

							// Disable all buttons from click, if there is
							// one open box
							if (isButtonViewAdsPressed()) {
								DialogBox buttonPressedBox = alertWidget("Information", "Please, close opened edit box first!",
										buttonViewAds.getAbsoluteLeft(), buttonViewAds.getAbsoluteTop());
								buttonPressedBox.show();
								return;
							}
							setButtonViewAdsPressed(true);
							setViewAdSelectedRow(selectedRow);

							// General
							setPageLoadingStartTime(getMillis());
							setActionState(appConst.VAL_LOADING());

							// Async call
							getGetAdDescFromServer(Integer.parseInt(adsId));
						}
					});
					flex.setWidget(i1 + 1, 1, buttonViewAds);
				}

				// Page Buttons
				drawPageButtons();

				// Ad Count
				setActionState(appConst.VAL_TOTAL_ADS());

				// Timing
				setLoadingTime(getMillis() - getPageLoadingStartTime());
			}

			void drawPageButtons() {

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
						if (i2 == getMainListingPageNumber()) {
							pageButton.setHTML("<b>" + iPage + "</b>");
						}

						pageButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {

								// General
								setPageLoadingStartTime(getMillis());
								setIdleTime(0);
								setCurrentAppPage(1);
								setActionState(appConst.VAL_LOADING());
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
								setMainListingPageNumber(iPage);

								// Async call
								getMainListingByPageFromServer();
							}
						});
						pageNumberTable.setWidget(0, i2, pageButton);
					}
				}
				pageNumberTable.setVisible(true);
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
				gwtService.getMainListing(getMainListingPageNumber(), getMainListingByPageFromServer);
			}

			public void getGetAdDescFromServer(int adDescId) {

				gwtService.getAdDesc(adDescId, getAdDescFromServer);
			}
		}

		// Login Handler
		class LoginButtonClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {

				// Sort of Initialize block
				// setIdleTime(0);
				setCurrentAppPage(4);
				showLoginPanel();
			}

			public void showLoginPanel() {

				String various = "<br> " + getLoginState();
				DialogBox newBox = alertWidget("Login Panel", "Idle time is: " + getIdleTime() + various, 0, 0);
				newBox.show();

				// Panel
				// TODO
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

		// InitializeColours()
		changeMenuItemColorAsSelected();

		// InitializeIdleIdleTimer()
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
		case 1:
			menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation-current-page");
			menuTable.getCellFormatter().setStyleName(0, 3, "cw-FlexTable-navigation");
			break;
		case 4:
			menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation");
			menuTable.getCellFormatter().setStyleName(0, 3, "cw-FlexTable-navigation-current-page");
			break;
		default:
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
		setLoginState(appConst.STATUS_LOGGED_OUT());
	}

	void doLogin() {

		setLoginState(appConst.STATUS_LOGGING_IN());

		// Async Call for updating the session
		// TODO
	}

	public String getActionState() {

		return actionState;
	}

	public void setActionState(String actionState) {

		this.actionState = actionState;
		if (actionState.equals(appConst.VAL_TOTAL_ADS())) {
			imageLoading.setVisible(false);
			appActionLabel.setHTML("<p>Showing total of " + getTotalAds() + " ads.</p>");
		} else if (actionState.equals(appConst.VAL_INITIALIZING()) || actionState.equals(appConst.VAL_LOADING())
				|| actionState.equals(appConst.STATUS_LOGGING_IN()) || actionState.equals(appConst.STATUS_LOGGING_OUT())) {
			imageLoading.setVisible(true);
			appActionLabel.setHTML("<p>Status: " + actionState + "</p");
		} else {
			imageLoading.setVisible(false);
			appActionLabel.setHTML("<p>Status: " + actionState + "</p>");
		}
	}

	public void setLoadingTime(double loadTime) {

		pageLoadTimeLabel.setHTML("Page generated in " + loadTime + " msec");
	}

	public double getPageLoadingStartTime() {

		return pageLoadingStartTime;
	}

	public void setPageLoadingStartTime(double pageLoadingStartTime) {

		this.pageLoadingStartTime = pageLoadingStartTime;
	}

	public boolean isLoggedIn() {

		return getLoginState().equals(appConst.STATUS_LOGGED_IN());
	}

	public int getMainListingPageNumber() {

		return mainListingPageNumber;
	}

	public void setMainListingPageNumber(int mainListingPageNumber) {

		this.mainListingPageNumber = mainListingPageNumber;
	}

	public int getTotalAds() {

		return totalAds;
	}

	public void setTotalAds(int totalAds) {

		this.totalAds = totalAds;
	}

	public boolean isButtonViewAdsPressed() {

		return buttonViewAdsPressed;
	}

	public void setButtonViewAdsPressed(boolean buttonViewAdsPressed) {

		this.buttonViewAdsPressed = buttonViewAdsPressed;
	}

	public int getViewAdSelectedRow() {

		return viewAdSelectedRow;
	}

	public void setViewAdSelectedRow(int viewAdSelectedRow) {

		this.viewAdSelectedRow = viewAdSelectedRow;
	}
}
