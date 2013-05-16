package net.voaideahost.sslv.mvc.gwt.web.client;

import java.util.Date;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Application implements EntryPoint {

	private static final String SERVER_ERROR = "WARNING! An error occurred while " + "attempting to contact the server. Please check your network "
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
	private boolean initializing = true;
	private String loginUserName = appConst.VAL_EMPTY();
	private String loginState = appConst.VAL_INITIALIZING();
	private String actionState = appConst.VAL_INITIALIZING();
	private int currentAppPage = 1;
	private String appViewMode = appConst.VAL_VIEW_MODE_ALL();
	private int idleTime = 0;
	private double pageLoadingStartTime = getMillis();
	private int mainListingPageNumber = 1;
	private int totalAds = 0;
	private boolean buttonViewAdsPressed = false;
	private int viewAdSelectedRow = 0;
	private int currentViewEditTableRow = 0;
	private JSONObject user;

	// Global Widgets
	final FlexTable menuTable = new FlexTable();
	final FlexTable flex = new FlexTable();
	final HTML appActionLabel = new HTML();
	final HTML pageLoadTimeLabel = new HTML();
	final FlexTable userTable = new FlexTable();
	final FlexTable viewEditTable = new FlexTable();

	// Panels
	final VerticalPanel loginPanel = new VerticalPanel();
	final VerticalPanel logoutPanel = new VerticalPanel();
	final HorizontalPanel mainPanel = new HorizontalPanel();
	final HorizontalPanel pagesPanel = new HorizontalPanel();
	final HorizontalPanel viewEditPanel = new HorizontalPanel();

	// Resources
	final Image imageLoading = new Image();
	final Image imageLogin = new Image();
	final Image imageLogout = new Image();

	public void onModuleLoad() {

		// Init Resources
		imageLoading.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/loading.gif");
		imageLogin.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/login.jpg");
		imageLogout.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/logout.jpg");

		// HEADER - Navigation Menu
		final HorizontalPanel menuPanel = new HorizontalPanel();
		menuTable.addStyleName("cw-FlexTable");
		menuTable.setWidth("1024px");
		menuTable.getRowFormatter().setStyleName(0, "cw-FlexTable-navigation");

		// Navigation Menu Buttons
		final HTML homeButton = new HTML("Home");
		menuTable.setWidget(0, 0, homeButton);
		menuTable.getCellFormatter().setWidth(0, 0, "16%");
		menuTable.getFlexCellFormatter().getElement(0, 0).getStyle().setCursor(Cursor.POINTER);
		menuTable.setWidget(0, 1, new HTML("Add"));
		menuTable.getCellFormatter().setWidth(0, 1, "16%");
		menuTable.getFlexCellFormatter().getElement(0, 1).getStyle().setCursor(Cursor.POINTER);
		menuTable.setWidget(0, 2, new HTML("Admin"));
		menuTable.getCellFormatter().setWidth(0, 2, "16%");
		menuTable.getFlexCellFormatter().getElement(0, 2).getStyle().setCursor(Cursor.POINTER);
		final HTML loginButton = new HTML("Login");
		menuTable.setWidget(0, 3, loginButton);
		menuTable.getFlexCellFormatter().getElement(0, 3).getStyle().setCursor(Cursor.POINTER);
		menuPanel.add(menuTable);
		RootPanel.get("header1").add(menuPanel);

		// Action Status Label
		final HorizontalPanel statusPanelMain = new HorizontalPanel();
		final HorizontalPanel statusPanel = new HorizontalPanel();
		final FlexTable statusPanelTable = new FlexTable();
		statusPanelTable.addStyleName("cw-FlexTable-view-edit-box");
		statusPanel.add(appActionLabel);
		statusPanel.add(imageLoading);
		statusPanelTable.setWidget(0, 0, statusPanel);
		statusPanelMain.add(statusPanelTable);
		RootPanel.get("body0").add(statusPanelMain);
		setActionState(appConst.VAL_INITIALIZING());

		// LOGIN PANEL
		RootPanel.get("body1a").add(loginPanel);
		RootPanel.get("body1b").add(logoutPanel);

		// MAIN CONTAINER
		flex.addStyleName("cw-FlexTable-main-list-tr");
		flex.setWidth("1024px");
		flex.getColumnFormatter().setWidth(0, "5%");
		flex.getColumnFormatter().setWidth(1, "50%");
		flex.getColumnFormatter().setWidth(2, "18%");
		flex.getColumnFormatter().setWidth(3, "14%");
		mainPanel.add(flex);
		RootPanel.get("body2").add(mainPanel);

		// MAIN CONTAINER - Page numbers
		final FlexTable pageNumberTable = new FlexTable();
		pageNumberTable.addStyleName("cw-FlexTable-view-edit-box");
		pagesPanel.add(pageNumberTable);
		RootPanel.get("body3").add(pagesPanel);

		// FOOTER
		final HorizontalPanel footerPanel = new HorizontalPanel();
		final FlexTable footerTable = new FlexTable();
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
		final FlexTable pageLoadTimeTable = new FlexTable();
		pageLoadTimeTable.addStyleName("cw-FlexTable-view-edit-box");
		pageLoadTimeTable.setWidget(0, 0, pageLoadTimeLabel);
		pageLoadTimerPanel.add(pageLoadTimeTable);
		RootPanel.get("footer2").add(pageLoadTimerPanel);

		// Async Calls

		// HANDLERS

		// Create a handler for the sendButton and nameField
		class HomeButtonClickHandler implements ClickHandler {

			public void onClick(ClickEvent event) {

				// Nothing here
			}

			/*
			 * 
			 * 
			 * ASYNC METHODS
			 */

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

			private void drawViewEditTable(String result) {

				// Init
				setCurrentViewEditTableRow(0);

				// Draw ViewEditBox
				final String ownerName = flex.getText(getViewAdSelectedRow() - 1, 3);
				final boolean isEditor = isLoggedIn() && ownerName.equals(getLoginUserName());

				// View / Edit Box
				viewEditPanel.clear();
				viewEditTable.clear();
				viewEditTable.removeAllRows();
				viewEditTable.setStyleName("cw-FlexTable-view-edit-box");
				viewEditTable.setWidth("1008px");
				viewEditTable.getColumnFormatter().setWidth(0, "50px");
				viewEditTable.getColumnFormatter().setWidth(1, "300px");
				viewEditTable.getColumnFormatter().setWidth(2, "300px");
				viewEditTable.getColumnFormatter().setWidth(3, "30px");

				// Name Label
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 0, new HTML("<b>Name</b>"));

				// Text Box
				final TextBox nameTextBox = new TextBox();
				final HTML adsName = (HTML) flex.getWidget(getViewAdSelectedRow() - 1, 1);
				nameTextBox.setText(adsName.getHTML());
				nameTextBox.setWidth("95%");
				nameTextBox.setEnabled(isEditor);
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 1, nameTextBox);
				setCurrentViewEditTableRow(1);

				// Separator
				final HTML htmlSeparator1 = new HTML("<hr />");
				viewEditTable.getFlexCellFormatter().setColSpan(getCurrentViewEditTableRow(), 0, 4);
				htmlSeparator1.setWidth("1001px");
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 0, htmlSeparator1);
				setCurrentViewEditTableRow(1);

				// Parse JSON
				final String json = result;
				final JSONArray array = (JSONArray) JSONParser.parseStrict(json);
				JSONObject v;

				// AdDesc
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 1, new HTML("<b>CRITERIA</b>"));
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 2, new HTML("<b>VALUE</b>"));
				setCurrentViewEditTableRow(1);

				String vetCriteria;
				String vetValue;
				int i = 0;
				for (i = 0; i < array.size(); i++) {
					v = array.get(i).isObject();

					// Adding vetRow
					vetCriteria = v.get("criteria").isString().stringValue();
					vetValue = v.get("value").isString().stringValue();
					addViewEditTableRow(vetCriteria, vetValue, getCurrentViewEditTableRow(), isEditor);

					// Increase iterator
					setCurrentViewEditTableRow(1);
				}

				// Image Pluss
				if (isEditor) {
					final Image newPlusImage = new Image();
					newPlusImage.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/plus.gif");
					newPlusImage.addMouseOverHandler(new MouseOverHandler() {

						@Override
						public void onMouseOver(MouseOverEvent event) {

							newPlusImage.getElement().getStyle().setCursor(Cursor.POINTER);
						}
					});
					newPlusImage.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							setIdleTime(0);
							final int rowIndex = viewEditTable.getCellForEvent(event).getRowIndex();
							viewEditTable.insertRow(rowIndex);
							addViewEditTableRow(appConst.VAL_EMPTY(), appConst.VAL_EMPTY(), rowIndex, isEditor);
						}
					});
					viewEditTable.setWidget(getCurrentViewEditTableRow(), 1, newPlusImage);
					setCurrentViewEditTableRow(1);
				}

				// Separator 2
				final HTML htmlSeparator2 = new HTML("<hr />");
				viewEditTable.getFlexCellFormatter().setColSpan(getCurrentViewEditTableRow(), 0, 4);
				htmlSeparator2.setWidth("1001px");
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 0, htmlSeparator2);
				setCurrentViewEditTableRow(1);

				// Buttons Panel
				final HorizontalPanel closeAndSaveButtonPanel = new HorizontalPanel();

				// Close Button
				final Button viewEditCloseButton = new Button("Close");
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
				final HTML buttonSeparator = new HTML("&nbsp;&nbsp;");
				closeAndSaveButtonPanel.add(buttonSeparator);

				// Save Button
				final Button viewEditSaveButton = new Button("Save");
				viewEditSaveButton.setEnabled(isEditor);
				viewEditSaveButton.setVisible(isEditor);
				closeAndSaveButtonPanel.add(viewEditSaveButton);
				viewEditTable.setWidget(getCurrentViewEditTableRow(), 1, closeAndSaveButtonPanel);
				viewEditPanel.add(viewEditTable);

				// TODO fix css

				// Flex
				flex.insertRow(getViewAdSelectedRow());
				flex.getFlexCellFormatter().setColSpan(getViewAdSelectedRow(), 0, 4);
				flex.setWidget(getViewAdSelectedRow(), 0, viewEditPanel);
				// flex.getRowFormatter().setStyleName(getViewAdSelectedRow(),
				// "cw-FlexTable-view-edit-box");
				viewEditPanel.setVisible(true);

				// Ad Count
				setActionState(appConst.VAL_TOTAL_ADS());

				// Timing
				setLoadingTime(getMillis() - getPageLoadingStartTime());
			}

			private void addViewEditTableRow(String vetCriteria, String vetValue, final int vetRowNumber, boolean vetIsEditor) {

				// Criteria Box
				final TextBox criteriaBox = new TextBox();
				criteriaBox.setWidth("95%");
				criteriaBox.setText(new HTML(vetCriteria).getHTML());
				criteriaBox.setEnabled(vetIsEditor);
				viewEditTable.setWidget(vetRowNumber, 1, criteriaBox);

				// Value Box
				final TextBox valueBox = new TextBox();
				valueBox.setWidth("95%");
				valueBox.setText(new HTML(vetValue).getHTML());
				valueBox.setEnabled(vetIsEditor);
				viewEditTable.setWidget(vetRowNumber, 2, valueBox);

				// Image Cross
				if (vetIsEditor) {
					final Image newCrossImage = new Image();
					newCrossImage.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/cross.png");
					newCrossImage.addMouseOverHandler(new MouseOverHandler() {

						@Override
						public void onMouseOver(MouseOverEvent event) {

							newCrossImage.getElement().getStyle().setCursor(Cursor.POINTER);
						}
					});
					newCrossImage.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							setIdleTime(0);
							final int rowIndex = viewEditTable.getCellForEvent(event).getRowIndex();
							viewEditTable.removeRow(rowIndex);
						}
					});
					viewEditTable.setWidget(vetRowNumber, 3, newCrossImage);
					viewEditTable.getCellFormatter().setAlignment(vetRowNumber, 3, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
				}
			}

			private void drawMainListingTable(String result) {

				flex.clear();
				flex.removeAllRows();

				// Parse JSON
				final String json = result;
				final JSONArray array = (JSONArray) JSONParser.parseStrict(json);
				JSONObject v;

				// Total Ads array[0].id
				v = array.get(0).isObject();
				String sTotalAds = v.get("id").isNumber().toString();
				setTotalAds(Integer.parseInt(sTotalAds));

				// Header row
				flex.setHTML(0, 0, "<b>Id</b>");
				flex.setHTML(0, 1, "<b>Summary</b>");
				flex.setHTML(0, 2, "<b>Date</b>");
				flex.setHTML(0, 3, "<b>User</b>");
				flex.getRowFormatter().setStyleName(0, "cw-FlexTable-main-list-th");

				// Data rows
				int arrayBoundary = array.size();

				int i1 = 1;
				for (i1 = 1; i1 < arrayBoundary; i1++) {
					v = array.get(i1).isObject();

					// ID
					final String adsId = v.get("id").isNumber().toString();
					flex.setText(i1, 0, adsId);

					// Name
					flex.setWidget(i1, 1, new HTML(v.get("name").isString().stringValue()));

					// Date
					final String dateCreatedS = v.get("created").isString().stringValue();
					final Date dateCreatedD = DateTimeFormat.getFormat("M d, y h:m:s a").parse(dateCreatedS);
					flex.setHTML(i1, 2, DateTimeFormat.getFormat("yyyy.MM.dd HH:mm:ss").format(dateCreatedD).toString());

					// Owner
					flex.setHTML(i1, 3, v.get("owner").isString().stringValue());

					// Creating Edit Click Handler
					flex.getFlexCellFormatter().getElement(i1, 0).getStyle().setCursor(Cursor.POINTER);
					flex.getFlexCellFormatter().getElement(i1, 1).getStyle().setCursor(Cursor.POINTER);
					flex.getFlexCellFormatter().getElement(i1, 2).getStyle().setCursor(Cursor.POINTER);
					flex.getFlexCellFormatter().getElement(i1, 3).getStyle().setCursor(Cursor.POINTER);

					flex.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							final int row = flex.getCellForEvent(event).getRowIndex() + 1;

							// General
							setIdleTime(0);

							// Disable multiple view/edit boxes
							if (isButtonViewAdsPressed() || getActionState().equals(appConst.VAL_LOADING()))
								return;

							setButtonViewAdsPressed(true);
							setViewAdSelectedRow(row);

							// General
							setPageLoadingStartTime(getMillis());
							setActionState(appConst.VAL_LOADING());

							// Async call
							getGetAdDescFromServer(Integer.parseInt(adsId));
						}
					});
				}

				// Page Buttons
				drawPageButtons();

				// Ad Count
				setActionState(appConst.VAL_TOTAL_ADS());

				// Timing
				setLoadingTime(getMillis() - getPageLoadingStartTime());
			}

			private void drawPageButtons() {

				// Draw buttons
				int ads_per_page;
				if (getAppViewMode().equals(appConst.VAL_VIEW_MODE_ALL()))
					ads_per_page = appConst.VAL_ADS_PER_VIEW_MODE_ALL();
				else
					ads_per_page = appConst.VAL_ADS_PER_VIEW_MODE_USER();

				int buttonsToDraw = Math.round(getTotalAds() / ads_per_page);

				if (buttonsToDraw > 0) {

					if (buttonsToDraw * ads_per_page == getTotalAds())
						buttonsToDraw -= 1;

					pageNumberTable.clear();
					pageNumberTable.setHTML(0, 0, "Pages");

					int i2 = 1;
					for (i2 = 1; i2 <= buttonsToDraw + 1; i2++) {

						final Button pageButton = new Button(Integer.toString(i2));
						pageButton.setSize("25px", "25px");

						final int iPage = i2;
						final int fButtonToDraw = buttonsToDraw + 1;
						if (i2 == getMainListingPageNumber())
							pageButton.setHTML("<b>" + iPage + "</b>");

						pageButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {

								if (getActionState().equals(appConst.VAL_LOADING()))
									return;

								// General
								setPageLoadingStartTime(getMillis());
								setIdleTime(0);
								setCurrentAppPage(1);
								setMainListingPageNumber(iPage);
								setActionState(appConst.VAL_LOADING());
								setButtonViewAdsPressed(false);

								// Page Button
								int n;
								for (n = 1; n <= fButtonToDraw; n++) {

									Button button = (Button) pageNumberTable.getWidget(0, n);
									button.setEnabled(false);

									if (n == getMainListingPageNumber())
										button.setHTML("<b>" + getMainListingPageNumber() + "</b>");
								}

								// Async call
								getMainListingByPageFromServer();
							}
						});
						pageNumberTable.setWidget(0, i2, pageButton);
					}

				} else {

					// Draw one button, but disable it once
					pageNumberTable.clear();
					pageNumberTable.setHTML(0, 0, "Pages");
					Button firstPageButton = new Button();
					firstPageButton.setHTML("<b>1</b>");
					firstPageButton.setEnabled(false);
					pageNumberTable.setWidget(0, 1, firstPageButton);
				}
			}

			/*
			 * 
			 * 
			 * ASYNC METHOD CALLS
			 */

			private void getMainListingByPageFromServer() {

				gwtService.getMainListing(getAppViewMode(), getMainListingPageNumber(), getLoginUserName(), getMainListingByPageFromServer);
			}

			private void getGetAdDescFromServer(int adDescId) {

				gwtService.getAdDesc(adDescId, getAdDescFromServer);
			}
		}

		// Login Handler
		class LoginButtonClickHandler implements ClickHandler {

			// Class Wide
			final FlexTable loginForm1 = new FlexTable();
			final FlexTable loginForm = new FlexTable();
			final FlexTable logoutForm1 = new FlexTable();
			final FlexTable logoutForm = new FlexTable();
			final TextBox textUserName = new TextBox();
			final TextBox textPassword = new TextBox();
			final Button loginButton = new Button("Login");
			final Button logoutButton = new Button("Logout");

			@Override
			public void onClick(ClickEvent event) {

				// Sort of Initialize block
				setPageLoadingStartTime(getMillis());
				setIdleTime(0);
				setCurrentAppPage(4);
				drawLoginPanel();
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
					setLoginState(appConst.STATUS_NOT_LOGGED_IN());
					setActionState(appConst.ACTION_LOGGING_IN_FAILED());

					// Alert
					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();

				}

				@Override
				public void onSuccess(String result) {

					setIdleTime(0);

					// Actions
					doLogin(result);
					drawLoginPanel();

					// Show proper screen
					setCurrentAppPage(getCurrentAppPage());
				}

			};

			AsyncCallback<String> doLogoutFromServerAsync = new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {

					setIdleTime(0);
					setLoginState(appConst.STATUS_NOT_LOGGED_IN());
					setActionState(appConst.STATUS_LOGGED_OUT());
					setLoginUserName(appConst.VAL_EMPTY());
					setCookie(appConst.VAL_EMPTY());

					// Alert
					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();

				}

				@Override
				public void onSuccess(String result) {

					setIdleTime(0);

					// Actions
					doLogout(result);
					drawLoginPanel();
				}

			};

			/*
			 * 
			 * 
			 * DRAW TABLE METHODS
			 */

			private void drawLoginPanel() {

				if (isLoggedIn()) {

					logoutPanel.setVisible(false);
					loginPanel.setVisible(true);

					loginPanel.clear();
					userTable.clear();
					loginForm1.clear();
					loginForm.clear();
					loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
					loginForm.setStyleName("cw-FlexTable-view-edit-box");

					loginForm1.addStyleName("cw-FlexTable-main-list-tr");
					loginForm1.setWidth("1024px");
					loginForm1.setHTML(0, 0, "<b>Id</b>");
					loginForm1.setHTML(0, 1, "<b>Name</b>");
					loginForm1.setHTML(0, 2, "<b>Surname</b>");
					loginForm1.setHTML(0, 3, "<b>e-mail</b>");
					loginForm1.setHTML(0, 4, "<b>Phone</b>");
					loginForm1.getRowFormatter().setStyleName(0, "cw-FlexTable-main-list-th");

					loginForm1.getColumnFormatter().setWidth(0, "10%");
					loginForm1.getColumnFormatter().setWidth(1, "25%");
					loginForm1.getColumnFormatter().setWidth(2, "25%");
					loginForm1.getColumnFormatter().setWidth(3, "20%");
					loginForm1.getColumnFormatter().setWidth(4, "20%");

					loginForm1.setHTML(1, 0, getUser().get("id").isString().stringValue());
					loginForm1.setHTML(1, 1, getUser().get("name").isString().stringValue());
					loginForm1.setHTML(1, 2, getUser().get("surName").isString().stringValue());
					loginForm1.setHTML(1, 3, getUser().get("eMail").isString().stringValue());
					loginForm1.setHTML(1, 4, getUser().get("phone").isString().stringValue());

					loginPanel.add(loginForm1);

					// Separator
					loginPanel.add(new HTML("&nbsp;&nbsp;"));

					// SessionID & Idle Time
					userTable.addStyleName("cw-FlexTable-main-list-tr");
					userTable.setWidth("1024px");

					// Header Row
					userTable.setHTML(0, 0, "<b>SessionID</b>");
					userTable.setHTML(0, 1, "<b>Idle Time (sec)</b>");
					userTable.getRowFormatter().setStyleName(0, "cw-FlexTable-main-list-th");

					userTable.getColumnFormatter().setWidth(0, "50%");
					userTable.getColumnFormatter().setWidth(1, "50%");

					userTable.setHTML(1, 0, getCookie());
					userTable.setHTML(1, 1, Integer.toString(getIdleTime()));

					loginPanel.add(userTable);

					// Separator
					loginPanel.add(new HTML("&nbsp;&nbsp;"));
					loginPanel.add(new HTML("&nbsp;&nbsp;"));

					// AppViewMode
					loginPanel.add(new HTML("View Mode Selector"));
					final RadioButton viewModeAll = new RadioButton("viewMode", "All");
					final RadioButton viewModeUser = new RadioButton("viewMode", "User only");

					viewModeAll.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {

							if (event.getValue())
								setAppViewMode(appConst.VAL_VIEW_MODE_ALL());
						}
					});

					viewModeUser.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {

							if (event.getValue())
								setAppViewMode(appConst.VAL_VIEW_MODE_USER());
						}
					});

					if (getAppViewMode().equals(appConst.VAL_VIEW_MODE_ALL())) {
						viewModeUser.setValue(false);
						viewModeAll.setValue(true);
					} else {
						viewModeUser.setValue(true);
						viewModeAll.setValue(false);
					}

					loginPanel.add(viewModeAll);
					loginPanel.add(viewModeUser);

					// Separator
					loginPanel.add(new HTML("&nbsp;&nbsp;"));
					loginPanel.add(new HTML("&nbsp;&nbsp;"));

					// Logout Button
					final FlexTable logoutFlexTable = new FlexTable();
					logoutFlexTable.clear();
					logoutButton.setEnabled(true);
					logoutButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							doOnLogoutButtonOrImagePress();
						}
					});
					logoutFlexTable.setWidget(0, 0, logoutButton);
					logoutFlexTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
					imageLogout.addMouseOverHandler(new MouseOverHandler() {

						@Override
						public void onMouseOver(MouseOverEvent event) {

							imageLogout.getElement().getStyle().setCursor(Cursor.POINTER);
						}
					});
					imageLogout.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							if (getActionState().equals(appConst.STATUS_LOGGING_OUT()))
								return;
							doOnLogoutButtonOrImagePress();
						}
					});
					imageLogout.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/logout.jpg");
					logoutFlexTable.setWidget(0, 1, imageLogout);
					logoutFlexTable.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

					// Panel
					loginPanel.add(logoutFlexTable);

				} else {

					loginPanel.setVisible(false);
					logoutPanel.setVisible(true);

					logoutPanel.clear();
					logoutForm1.clear();
					logoutForm.clear();
					logoutForm.setStyleName("cw-FlexTable-view-edit-box");

					logoutForm1.setWidget(0, 0, new Label("Username"));
					textUserName.setEnabled(true);
					logoutForm1.setWidget(0, 1, textUserName);
					logoutForm1.setWidget(1, 0, new Label("Password"));
					textPassword.setEnabled(true);
					logoutForm1.setWidget(1, 1, textPassword);
					logoutForm.setWidget(0, 0, logoutForm1);
					logoutForm.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);

					// Key Handlers
					textUserName.setText(appConst.VAL_EMPTY());
					textUserName.addKeyDownHandler(new KeyDownHandler() {

						@Override
						public void onKeyDown(KeyDownEvent event) {

							if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

								doOnLoginButtonOrImageOrEnterKeyPress();
							}
						}
					});
					textPassword.setText(appConst.VAL_EMPTY());
					textPassword.addKeyDownHandler(new KeyDownHandler() {

						@Override
						public void onKeyDown(KeyDownEvent event) {

							if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {

								doOnLoginButtonOrImageOrEnterKeyPress();
							}
						}
					});

					// Login Button
					loginButton.setEnabled(true);
					loginButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							doOnLoginButtonOrImageOrEnterKeyPress();
						}
					});
					logoutForm.setWidget(0, 1, loginButton);
					logoutForm.getFlexCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
					imageLogin.addMouseOverHandler(new MouseOverHandler() {

						@Override
						public void onMouseOver(MouseOverEvent event) {

							imageLogin.getElement().getStyle().setCursor(Cursor.POINTER);
						}
					});
					imageLogin.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							doOnLoginButtonOrImageOrEnterKeyPress();
						}
					});
					imageLogin.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/login.jpg");
					logoutForm.setWidget(0, 2, imageLogin);
					logoutForm.getFlexCellFormatter().setAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);

					// Panel
					logoutPanel.add(logoutForm);
				}

				// Timing
				setLoadingTime(getMillis() - getPageLoadingStartTime());
			}

			private void doOnLoginButtonOrImageOrEnterKeyPress() {

				if (getActionState().equals(appConst.STATUS_LOGGING_IN()))
					return;

				// TIMING
				setPageLoadingStartTime(getMillis());
				setIdleTime(0);

				// Disable Buttons and TextFields
				loginButton.setEnabled(false);
				textUserName.setEnabled(false);
				textPassword.setEnabled(false);
				imageLogin.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/loading32.gif");

				// doLogin
				setActionState(appConst.STATUS_LOGGING_IN());
				setLoginUserName(textUserName.getText());
				doLoginToServer(textUserName.getText(), getCookie());
			}

			private void doOnLogoutButtonOrImagePress() {

				if (getActionState().equals(appConst.STATUS_LOGGING_OUT()))
					return;

				// Timing
				setPageLoadingStartTime(getMillis());
				setIdleTime(0);

				// Disable Buttons and TextFields
				logoutButton.setEnabled(false);
				imageLogout.setUrl(appConst.VAL_CONTEXT_ROOT() + "images/loading32.gif");

				// doLogout
				setActionState(appConst.STATUS_LOGGING_OUT());
				doLogoutFromServer(getLoginUserName(), getCookie());
			}

			/*
			 * 
			 * 
			 * ASYNC METHOD CALLS
			 */

			private void doLoginToServer(String userName, String cookie) {

				gwtService.doLogin(userName, cookie, doLoginToServerAsync);
			}

			private void doLogoutFromServer(String userName, String cookie) {

				gwtService.doLogout(userName, cookie, doLogoutFromServerAsync);
			}
		}

		/*
		 * 
		 * 
		 * INITIALIZE APPLICATION BLOCK - finally onModuleLoad()
		 */

		// Add handlers to menu buttons
		final HomeButtonClickHandler mainButtonClickHandler = new HomeButtonClickHandler();
		homeButton.addClickHandler(mainButtonClickHandler);

		final LoginButtonClickHandler loginButtonClickHandler = new LoginButtonClickHandler();
		loginButton.addClickHandler(loginButtonClickHandler);

		menuTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				int cell = menuTable.getCellForEvent(event).getCellIndex();
				switch (cell) {
					case 0 :

						// General
						setPageLoadingStartTime(getMillis());
						setIdleTime(0);
						setMainListingPageNumber(1);
						setCurrentAppPage(1);
						setButtonViewAdsPressed(false);

						// Prevent double call
						if (getActionState().equals(appConst.VAL_LOADING()))
							return;
						setActionState(appConst.VAL_LOADING());

						// Async Call
						mainButtonClickHandler.getMainListingByPageFromServer();
						break;
					case 3 :

						// General
						setPageLoadingStartTime(getMillis());
						setIdleTime(0);
						setCurrentAppPage(4);

						// Draw Login Panel
						loginButtonClickHandler.drawLoginPanel();
						break;
					default :
						break;
				}
			}
		});

		// InitializeApp()
		mainButtonClickHandler.getMainListingByPageFromServer();
		loginButtonClickHandler.doLoginToServer(getLoginUserName(), getCookie());

		// InitializeUser()
		setLoginState(getLoginState());

		// InitializeColours()
		changeMenuItemColorAsSelected();

		// Show Panels

		// InitializeIdleIdleTimer()
		final RepeatingCommand idleTimer = new Scheduler.RepeatingCommand() {

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
				logoutPanel.setVisible(false);
				mainPanel.setVisible(true);
				pagesPanel.setVisible(true);
				break;
			case 4 :
				menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation");
				menuTable.getCellFormatter().setStyleName(0, 3, "cw-FlexTable-navigation-current-page");
				loginPanel.setVisible(isLoggedIn());
				if (isLoggedIn()) {
					logoutPanel.setVisible(false);
				} else {
					logoutPanel.setVisible(true);
				}
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
		HTML loginAnchor = (HTML) menuTable.getWidget(0, 3);
		String loginAnchorText;
		if (action.equals(appConst.STATUS_LOGGED_IN()) || action.equals(appConst.STATUS_LOGGING_IN())) {
			loginAnchorText = "Status: " + action + " as " + getLoginUserName();
		} else {
			loginAnchorText = "Status: " + action;
		}
		loginAnchor.setHTML(loginAnchorText);
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

	public void checkLoginStatus() {

		if ((getLoginState().equals(appConst.STATUS_LOGGED_IN()) || getLoginState().equals(appConst.STATUS_LOGGING_IN()))
				&& getIdleTime() >= appConst.VAL_MAX_IDLE_TIME()) {
			doLogout(appConst.STATUS_LOGGED_OUT_IDLE());
		}

		if (getCurrentAppPage() == 4 && isLoggedIn()) {

			userTable.setHTML(1, 1, Integer.toString(getIdleTime()));
		}
	}

	public void doLogout(String result) {

		setLoginUserName(appConst.VAL_EMPTY());
		setLoginState(appConst.STATUS_NOT_LOGGED_IN());

		if (result.equals(appConst.STATUS_LOGGED_OUT_IDLE())) {
			setActionState(result);
		} else {
			setActionState(appConst.STATUS_LOGGED_OUT());
		}

		setCookie(appConst.VAL_EMPTY());
		setAppViewMode(appConst.VAL_VIEW_MODE_ALL());
	}

	public void doLogin(String result) {

		// Parse JSON
		String json = result;

		// Login Actions
		if (json.equals(appConst.VAL_EMPTY()) || json.equals(appConst.STATUS_NO_SUCH_USER())) {

			setLoginUserName(appConst.VAL_EMPTY());
			setLoginState(appConst.STATUS_NOT_LOGGED_IN());
			if (!json.equals(appConst.VAL_LOADING()) && !json.equals(appConst.VAL_INITIALIZING()))
				setActionState(json);
			setCookie(appConst.VAL_EMPTY());

		} else {

			JSONArray array = (JSONArray) JSONParser.parseStrict(json);
			JSONObject v;

			// USER
			v = array.get(0).isObject();
			String id = v.get("id").isString().stringValue();
			if (getLoginUserName().equals(appConst.VAL_EMPTY())) {
				setLoginUserName(id);
			}
			setLoginState(appConst.STATUS_LOGGED_IN());
			setActionState(appConst.STATUS_LOGGED_IN());
			setUser(v);

			// Session ID
			v = array.get(1).isObject();
			id = v.get("id").isString().stringValue();
			setCookie(id);
		}
	}

	public String getActionState() {

		return actionState;
	}

	public void setActionState(String actionState) {

		// TODO Introduce MSG_TYPE_????
		this.actionState = actionState;
		if (actionState.equals(appConst.VAL_TOTAL_ADS())) {
			imageLoading.setVisible(false);
			appActionLabel.setHTML("Showing total of " + getTotalAds() + " ads&nbsp;|&nbsp;" + getAppViewMode() + "");
		} else if (actionState.equals(appConst.VAL_INITIALIZING()) || actionState.equals(appConst.VAL_LOADING())
				|| actionState.equals(appConst.STATUS_LOGGING_IN()) || actionState.equals(appConst.STATUS_LOGGING_OUT())) {
			imageLoading.setVisible(true);
			appActionLabel.setHTML("Status: " + actionState + "");
		} else {
			imageLoading.setVisible(false);
			appActionLabel.setHTML("Status: " + actionState + "");
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

		if (currentViewEditTableRow == 0) {
			this.currentViewEditTableRow = currentViewEditTableRow;
		} else {
			this.currentViewEditTableRow += currentViewEditTableRow;
		}
	}

	public void setCookie(String cookie) {

		final Date date = new Date();
		long nowLong = date.getTime();
		nowLong = nowLong + (1000 * 60 * 60);
		date.setTime(nowLong);
		Cookies.setCookie(appConst.VAL_COOKIE(), cookie, date);
	}

	public String getCookie() {

		final String cookie = Cookies.getCookie(appConst.VAL_COOKIE()).toString();
		return cookie == null ? appConst.VAL_EMPTY() : cookie;
	}

	public JSONObject getUser() {

		return user;
	}

	public void setUser(JSONObject user) {

		this.user = user;
	}

	public boolean isInitializing() {

		return initializing;
	}

	public void setInitializing(boolean initializing) {

		this.initializing = initializing;
	}
}
