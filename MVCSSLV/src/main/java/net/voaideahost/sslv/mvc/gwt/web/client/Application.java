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
	private String loginUserName = appConst.VAL_EMPTY();
	private String loginState = appConst.STATUS_NOT_LOGGED_IN();
	private String actionState = appConst.VAL_INITIALIZING();
	private int currentAppPage = 1;
	private String appViewMode = appConst.VIEW_MODE_ALL();
	private int idleTime = 0;
	private double pageLoadingStartTime = getMillis();
	private int mainListingPageNumber = 1;
	private int totalAds = 0;
	private boolean buttonViewAdsPressed = false;
	private int viewAdSelectedRow = 0;
	private int currentViewEditTableRow = 0;

	// Global Widgets
	final FlexTable menuTable = new FlexTable();
	final FlexTable flex = new FlexTable();
	final HTML appActionLabel = new HTML(appConst.VAL_EMPTY());
	final HTML pageLoadTimeLabel = new HTML();

	// Panels
	final VerticalPanel loginPanel = new VerticalPanel();
	final HorizontalPanel mainPanel = new HorizontalPanel();
	final HorizontalPanel pagesPanel = new HorizontalPanel();

	// Resources
	final Image imageLoading = new Image();
	final Image imageCross = new Image();
	final Image imagePlus = new Image();

	public void onModuleLoad() {

		// Init Resources
		imageLoading.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/loading.gif");

		// HEADER - Navigation Menu
		final HorizontalPanel menuPanel = new HorizontalPanel();
		menuTable.addStyleName("cw-FlexTable");
		menuTable.setWidth("1024px");
		menuTable.getRowFormatter().setStyleName(0, "cw-FlexTable-navigation");

		// Navigation Menu Buttons
		final Anchor homeButton = new Anchor("Home");
		menuTable.setWidget(0, 0, homeButton);
		menuTable.getCellFormatter().setWidth(0, 0, "16%");
		menuTable.setWidget(0, 1, new Anchor("Add"));
		menuTable.getCellFormatter().setWidth(0, 1, "16%");
		menuTable.setWidget(0, 2, new Anchor("Admin"));
		menuTable.getCellFormatter().setWidth(0, 2, "16%");
		final Anchor loginButton = new Anchor("Login");
		menuTable.setWidget(0, 3, loginButton);
		menuPanel.add(menuTable);
		RootPanel.get("header1").add(menuPanel);

		// Action Status Label
		final HorizontalPanel statusPanel = new HorizontalPanel();
		setActionState(getActionState());
		statusPanel.add(appActionLabel);
		statusPanel.add(imageLoading);
		RootPanel.get("body0").add(statusPanel);
		setActionState(appConst.VAL_INITIALIZING());

		// MAIN CONTAINER
		flex.addStyleName("cw-FlexTable");
		flex.setWidth("1024px");
		flex.getColumnFormatter().setWidth(0, "5%");
		flex.getColumnFormatter().setWidth(1, "50%");
		flex.getColumnFormatter().setWidth(2, "18%");
		flex.getColumnFormatter().setWidth(3, "14%");
		mainPanel.add(flex);
		RootPanel.get("body2").add(mainPanel);

		// MAIN CONTAINER - Page numbers
		final FlexTable pageNumberTable = new FlexTable();
		pageNumberTable.setHTML(0, 0, "Pages");
		pagesPanel.add(pageNumberTable);
		RootPanel.get("body3").add(pagesPanel);

		// FOOTER
		final HorizontalPanel footerPanel = new HorizontalPanel();
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
		final HorizontalPanel pageLoadTimerPanel = new HorizontalPanel();
		pageLoadTimerPanel.add(pageLoadTimeLabel);
		RootPanel.get("footer2").add(pageLoadTimerPanel);

		// Async Calls

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

			/*
			 * 
			 * 
			 * ASYNC METHODS
			 */

			AsyncCallback<Integer> getTotalAdsFromServer = new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {

					setIdleTime(0);
					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();
				}

				@Override
				public void onSuccess(Integer result) {

					setTotalAds(result.intValue());
					setActionState(appConst.VAL_TOTAL_ADS());
				}

			};

			AsyncCallback<String> getAdDescFromServer = new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {

					setIdleTime(0);
					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();
				}

				@Override
				public void onSuccess(String result) {

					drawViewEditTable(result);
				}
			};

			AsyncCallback<String> getMainListingByPageFromServer = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {

					setIdleTime(0);
					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();
				}

				public void onSuccess(String result) {

					drawMainListingTable(result);
				}
			};

			/*
			 * 
			 * 
			 * DRAW TABLE METHODS
			 */

			void drawViewEditTable(String result) {

				// Draw ViewEditBox

				String ownerName = flex.getText(getViewAdSelectedRow() - 1, 3);
				final boolean isEditor = isLoggedIn() && ownerName.equals(getLoginUserName());

				// View / Edit Box
				HorizontalPanel viewEditPanel = new HorizontalPanel();
				viewEditPanel.setVisible(false);
				viewEditPanel.setWidth("100%");
				final FlexTable viewEditTable = new FlexTable();
				viewEditTable.addStyleName("cw-FlexTable-view-edit-box");
				viewEditTable.setWidth("100%");
				viewEditTable.getColumnFormatter().setWidth(0, "50px");
				viewEditTable.getColumnFormatter().setWidth(1, "300px");
				viewEditTable.getColumnFormatter().setWidth(2, "300px");
				viewEditTable.getColumnFormatter().setWidth(3, "30px");

				// Name Label
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 0, new HTML("<b>Name</b>"));

				// Text Box
				TextBox nameTextBox = new TextBox();
				Anchor adsName = (Anchor) flex.getWidget(getViewAdSelectedRow() - 1, 1);
				nameTextBox.setText(adsName.getHTML());
				nameTextBox.setWidth("95%");
				nameTextBox.setEnabled(isEditor);
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 1, nameTextBox);
				setCurrentViewEditTableRow(1);

				// Separator
				HTML htmlSeparator1 = new HTML("<hr />");
				viewEditTable.getFlexCellFormatter().setColSpan(currentViewEditTableRow, 0, 4);
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 0, htmlSeparator1);
				setCurrentViewEditTableRow(1);

				// Parse JSON
				String json = result;
				JSONArray array = (JSONArray) JSONParser.parseStrict(json);
				JSONObject v;

				// AdDesc
				viewEditTable.setHTML(getCurrentViewEditTableRow(), 1, "<b>CRITERIA</b>");
				viewEditTable.setHTML(getCurrentViewEditTableRow(), 2, "<b>VALUE</b>");
				setCurrentViewEditTableRow(1);

				int i = 0;
				for (i = 0; i < array.size(); i++) {
					v = array.get(i).isObject();

					// Adding vetRow
					String vetCriteria = v.get("criteria").isString().stringValue();
					String vetValue = v.get("value").isString().stringValue();
					setViewEditTableRow(viewEditTable, vetCriteria, vetValue, getCurrentViewEditTableRow(), isEditor);

					// Increase iterator
					setCurrentViewEditTableRow(1);
				}

				// Image Pluss
				if (isEditor) {
					final Image newPlusImage = new Image();
					newPlusImage.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/plus.gif");
					newPlusImage.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							setIdleTime(0);
							int rowIndex = viewEditTable.getCellForEvent(event).getRowIndex();
							viewEditTable.insertRow(rowIndex);
							setViewEditTableRow(viewEditTable, appConst.VAL_EMPTY(), appConst.VAL_EMPTY(), rowIndex, isEditor);
						}
					});
					viewEditTable.setWidget(getCurrentViewEditTableRow(), 1, newPlusImage);
					setCurrentViewEditTableRow(1);
				}

				// Separator 2
				HTML htmlSeparator2 = new HTML("<hr />");
				viewEditTable.getFlexCellFormatter().setColSpan(currentViewEditTableRow, 0, 4);
				htmlSeparator2.setWidth("98%");
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 0, htmlSeparator2);
				setCurrentViewEditTableRow(1);

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
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 1, closeAndSaveButtonPanel);
				viewEditPanel.add(viewEditTable);

				// Flex
				flex.insertRow(getViewAdSelectedRow());
				flex.getFlexCellFormatter().setColSpan(getViewAdSelectedRow(), 0, 4);
				flex.setWidget(getViewAdSelectedRow(), 0, viewEditPanel);
				viewEditPanel.setVisible(true);

				// Ad Count
				setActionState(appConst.VAL_TOTAL_ADS());

				// Timing
				setLoadingTime(getMillis() - getPageLoadingStartTime());
			}

			void setViewEditTableRow(final FlexTable vetTable, String vetCriteria, String vetValue, final int vetRowNumber, boolean vetIsEditor) {

				// Criteria Box
				TextBox criteriaBox = new TextBox();
				criteriaBox.setWidth("95%");
				criteriaBox.setText(new HTML(vetCriteria).getHTML());
				criteriaBox.setEnabled(vetIsEditor);
				vetTable.setWidget(vetRowNumber, 1, criteriaBox);

				// Value Box
				TextBox valueBox = new TextBox();
				valueBox.setWidth("95%");
				valueBox.setText(new HTML(vetValue).getHTML());
				valueBox.setEnabled(vetIsEditor);
				vetTable.setWidget(vetRowNumber, 2, valueBox);

				// Image Cross
				if (vetIsEditor) {
					final Image newCrossImage = new Image();
					newCrossImage.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/cross.png");
					newCrossImage.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							setIdleTime(0);
							int rowIndex = vetTable.getCellForEvent(event).getRowIndex();
							vetTable.removeRow(rowIndex);
						}
					});
					vetTable.setWidget(vetRowNumber, 3, newCrossImage);
					vetTable.getCellFormatter().setAlignment(vetRowNumber, 3, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
				}
			}

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
			}

			/*
			 * 
			 * 
			 * ASYNC METHOD CALLS
			 */

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
				setIdleTime(0);
				setCurrentAppPage(4);
				showLoginPanel();
			}

			/*
			 * 
			 * 
			 * ASYNC METHODS
			 */

			AsyncCallback<String> doLoginToServerAsync = new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {

					setIdleTime(0);
					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();
				}

				@Override
				public void onSuccess(String result) {

					setIdleTime(0);
					DialogBox box = alertWidget("JSon", result, 0, 0);
					box.show();
				}

			};

			/*
			 * 
			 * 
			 * DRAW TABLE METHODS
			 */

			public void showLoginPanel() {

				// Panel
				if (!isLoggedIn()) {
					final FlexTable loginForm1 = new FlexTable();
					loginForm1.setWidget(0, 0, new Label("Username"));
					final TextBox textUsername = new TextBox();
					loginForm1.setWidget(0, 1, textUsername);
					loginForm1.setWidget(1, 0, new Label("Password"));
					final TextBox textPassword = new TextBox();
					loginForm1.setWidget(1, 1, textPassword);
					final FlexTable loginForm = new FlexTable();
					loginForm.setWidget(0, 0, loginForm1);
					final Button loginButton = new Button("Login");
					loginButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							setIdleTime(0);
							setActionState(appConst.STATUS_LOGGING_IN());
							doLoginToServer(textUsername.getText());
						}
					});
					loginForm.setWidget(0, 1, loginButton);
					loginPanel.add(loginForm);
					RootPanel.get("body1").add(loginPanel);
				} else {

					// TODO Draw Panel if Logged In
				}
			}

			/*
			 * 
			 * 
			 * ASYNC METHOD CALLS
			 */

			private void doLoginToServer(String userName) {

				gwtService.doLogin(userName, doLoginToServerAsync);
			}
		}

		/*
		 * 
		 * 
		 * INITIALIZE APPLICATION BLOCK - finally onModuleLoad()
		 */

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

		// Show Panels

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

	/*
	 * 
	 * 
	 * GLOBAL SERVICE METHODS BLOCK
	 */

	public void changeMenuItemColorAsSelected() {

		switch (getCurrentAppPage()) {
			case 1 :
				menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation-current-page");
				menuTable.getCellFormatter().setStyleName(0, 3, "cw-FlexTable-navigation");
				loginPanel.setVisible(false);
				mainPanel.setVisible(true);
				pagesPanel.setVisible(true);
				break;
			case 4 :
				menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation");
				menuTable.getCellFormatter().setStyleName(0, 3, "cw-FlexTable-navigation-current-page");
				loginPanel.setVisible(true);
				mainPanel.setVisible(false);
				pagesPanel.setVisible(false);
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

		// Restore Total Ads message after different actions
		if (idleTime > 10 && !getActionState().equals(appConst.VAL_TOTAL_ADS())) {
			setActionState(appConst.VAL_TOTAL_ADS());
		}
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
		// TODO Async Call doLogout()
		setLoginState(appConst.STATUS_LOGGED_OUT());
	}

	void doLogin() {

		setLoginState(appConst.STATUS_LOGGING_IN());

		// Async Call for updating the session
		// TODO Async Call doLogin()
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

	public int getCurrentViewEditTableRow() {

		return currentViewEditTableRow;
	}

	public void setCurrentViewEditTableRow(int currentViewEditTableRow) {

		this.currentViewEditTableRow += currentViewEditTableRow;
	}
}
