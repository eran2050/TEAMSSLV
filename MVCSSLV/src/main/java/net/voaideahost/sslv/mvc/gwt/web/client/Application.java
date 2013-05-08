package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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

	public void onModuleLoad() {

		// Header / Main Container / Footer
		int currentAppPage = 1;

		// HEADER
		HorizontalPanel headerPanel = new HorizontalPanel();
		FlexTable menuTable = new FlexTable();
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
		menuTable.setWidget(0, 3, new Anchor("Login"));
		headerPanel.add(menuTable);
		RootPanel.get("header1").add(headerPanel);

		// Ad Count Label
		HorizontalPanel statusPanel = new HorizontalPanel();
		final HTML adCountLabel = new HTML("<p>" + appConst.VAL_LOADING() + "</p>");
		statusPanel.add(adCountLabel);
		RootPanel.get("body0").add(statusPanel);

		// MAIN CONTAINER
		HorizontalPanel mainPanel = new HorizontalPanel();
		final FlexTable flex = new FlexTable();
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
			private double pageTime1 = Duration.currentTimeMillis();

			public void onClick(ClickEvent event) {

				// Sort of Initialize block
				pageTime1 = Duration.currentTimeMillis();
				setPageNumber(1);
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
				}

				@Override
				public void onSuccess(Integer result) {

					setTotalAds(result.intValue());
					adCountLabel.setHTML("<p>Total Ads:&nbsp;" + getTotalAds() + "</p>");
				}

			};

			AsyncCallback<String> getAdDescFromServer = new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {

					DialogBox box = alertWidget("Connection failure", SERVER_ERROR, 0, 0);
					box.show();
				}

				@Override
				public void onSuccess(String result) {

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
					HTML htmlSeparator2 = new HTML("<hr />");
					viewEditTable.getFlexCellFormatter().setColSpan(currentTableRow, 0, 4);
					viewEditTable.setWidget(currentTableRow++, 0, htmlSeparator2);

					// Buttons Panel
					HorizontalPanel closeAndSaveButtonPanel = new HorizontalPanel();

					// Close Button
					Button viewEditCloseButton = new Button("Close");
					viewEditCloseButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

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
					double pageTime2 = Duration.currentTimeMillis();
					double pageTime3 = (pageTime2 - pageTime1);
					pageLoadTimeLabel.setHTML("Page generated in " + pageTime3 + " msec");
				}

			};

			AsyncCallback<String> getMainListingByPageFromServer = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {

					homeButton.setEnabled(true);
					homeButton.setFocus(true);
				}

				public void onSuccess(String result) {

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

						flex.setHTML(i1 + 1, 0, v.get("id").isNumber().toString());

						// Date
						String dateCreated = v.get("created").isString().stringValue();
						flex.setHTML(i1 + 1, 2, dateCreated);
						flex.setHTML(i1 + 1, 3, v.get("owner").isString().stringValue());

						// Creating Edit Click Handler
						final String adsId = v.get("id").isNumber().toString();
						final int selectedRow = i1 + 2;
						final Anchor buttonViewAds = new Anchor();
						buttonViewAds.setHTML(v.get("name").isString().stringValue());

						buttonViewAds.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {

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
								pageTime1 = Duration.currentTimeMillis();
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
							if (i2 == getPageNumber()) {
								pageButton.setHTML("<b>" + iPage + "</b>");
							}

							pageButton.addClickHandler(new ClickHandler() {

								public void onClick(ClickEvent event) {

									// General
									pageTime1 = Duration.currentTimeMillis();
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
									setPageNumber(iPage);

									// Async call
									getMainListingByPageFromServer();
								}
							});
							pageNumberTable.setWidget(0, i2, pageButton);
						}
					}
					double pageTime2 = Duration.currentTimeMillis();
					double pageTime3 = (pageTime2 - pageTime1);
					pageLoadTimeLabel.setHTML("Page generated in " + pageTime3 + " msec");
				}
			};

			private void getTotalAdsFromServer() {

				gwtService.getTotalAds(getTotalAdsFromServer);
			}

			private void getMainListingByPageFromServer() {

				homeButton.setEnabled(false);
				gwtService.getMainListing(getPageNumber(), getMainListingByPageFromServer);
			}

			public int getPageNumber() {

				return homeButtonClickHandlerPageNumber;
			}

			public void setPageNumber(int mainButtonClickHandlerPageNumber) {

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

		// Add a handler refresh Flex Table
		HomeButtonClickHandler mainButtonClickHandler = new HomeButtonClickHandler();
		homeButton.addClickHandler(mainButtonClickHandler);

		// Initialise()
		switch (currentAppPage) {
			case 1 :
				menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation-current-page");
				mainButtonClickHandler.getTotalAdsFromServer();
				mainButtonClickHandler.getMainListingByPageFromServer();
				break;
			default :
				break;
		}

		/*
		 * // default GWT Hello World final Button sendButton = new
		 * Button("Send"); final TextBox nameField = new TextBox();
		 * nameField.setText("GWT User"); final Label errorLabel = new Label();
		 * 
		 * // We can add style names to widgets
		 * sendButton.addStyleName("sendButton");
		 * 
		 * // Add the nameField and sendButton to the RootPanel // Use
		 * RootPanel.get() to get the entire body element
		 * RootPanel.get("nameFieldContainer").add(nameField);
		 * RootPanel.get("sendButtonContainer").add(sendButton);
		 * RootPanel.get("errorLabelContainer").add(errorLabel);
		 * 
		 * // Focus the cursor on the name field when the app loads
		 * nameField.setFocus(true); nameField.selectAll();
		 * 
		 * // Create the popup dialog box final DialogBox dialogBox = new
		 * DialogBox(); dialogBox.setText("Remote Procedure Call");
		 * dialogBox.setAnimationEnabled(true); final Button closeButton = new
		 * Button("Close");
		 * 
		 * // We can set the id of a widget by accessing its Element
		 * closeButton.getElement().setId("closeButton"); final Label
		 * textToServerLabel = new Label(); final HTML serverResponseLabel = new
		 * HTML(); VerticalPanel dialogVPanel = new VerticalPanel();
		 * dialogVPanel.addStyleName("dialogVPanel"); dialogVPanel.add(new
		 * HTML("<b>Sending name to the server:</b>"));
		 * dialogVPanel.add(textToServerLabel); dialogVPanel.add(new
		 * HTML("<br><b>Server replies:</b>"));
		 * dialogVPanel.add(serverResponseLabel);
		 * dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		 * dialogVPanel.add(closeButton); dialogBox.setWidget(dialogVPanel);
		 * 
		 * // Add a handler to close the DialogBox
		 * closeButton.addClickHandler(new ClickHandler() { public void
		 * onClick(ClickEvent event) { dialogBox.hide();
		 * sendButton.setEnabled(true); sendButton.setFocus(true); } });
		 * 
		 * // Create a handler for the sendButton and nameField class
		 * SendButtonClickHandler implements ClickHandler, KeyUpHandler { public
		 * void onClick(ClickEvent event) { sendNameToServer(); }
		 * 
		 * public void onKeyUp(KeyUpEvent event) { if (event.getNativeKeyCode()
		 * == KeyCodes.KEY_ENTER) { sendNameToServer(); } }
		 * 
		 * private void sendNameToServer() { String textToServer =
		 * nameField.getText();
		 * 
		 * // Then, we send the input to the server.
		 * sendButton.setEnabled(false);
		 * textToServerLabel.setText(textToServer);
		 * serverResponseLabel.setText(""); gwtService.greet(textToServer, new
		 * AsyncCallback<String>() { public void onFailure(Throwable caught) {
		 * 
		 * // Show the RPC error message to the user
		 * dialogBox.setText("Remote Procedure Call - Failure");
		 * serverResponseLabel .addStyleName("serverResponseLabelError");
		 * serverResponseLabel.setHTML(SERVER_ERROR); dialogBox.center();
		 * closeButton.setFocus(true); }
		 * 
		 * public void onSuccess(String result) {
		 * dialogBox.setText("Remote Procedure Call"); serverResponseLabel
		 * .removeStyleName("serverResponseLabelError");
		 * serverResponseLabel.setHTML(result); dialogBox.center();
		 * closeButton.setFocus(true); } }); } }
		 * 
		 * // Add a handler to send the name to the server
		 * SendButtonClickHandler handler = new SendButtonClickHandler();
		 * sendButton.addClickHandler(handler);
		 * nameField.addKeyUpHandler(handler);
		 */
	}
}
