package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
		final Button homeButton = new Button("Home");
		homeButton.addStyleName("sendButton");
		menuTable.setWidget(0, 0, homeButton);
		menuTable.getCellFormatter().setWidth(0, 0, "16%");
		menuTable.setWidget(0, 1, new Button("Add"));
		menuTable.getCellFormatter().setWidth(0, 1, "16%");
		menuTable.setWidget(0, 2, new Button("Admin"));
		menuTable.getCellFormatter().setWidth(0, 2, "16%");
		menuTable.setWidget(0, 3, new TextBox());
		menuTable.setWidget(0, 4, new Button("Login"));
		headerPanel.add(menuTable);
		RootPanel.get("headerWidgetStub").add(headerPanel);

		// Ad Count Label
		final HTML adCountLabel = new HTML("<p>" + appConst.VAL_LOADING() + "</p>");
		RootPanel.get("preMainContainerStub").add(adCountLabel);

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
		RootPanel.get("mainContainerStub").add(mainPanel);

		// Page numbers
		HorizontalPanel pagesPanel = new HorizontalPanel();
		final FlexTable pageNumberTable = new FlexTable();
		pageNumberTable.setHTML(0, 0, "Pages");
		pagesPanel.add(pageNumberTable);
		RootPanel.get("secondContainerStub").add(pagesPanel);

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
		RootPanel.get("footerWidgetStub").add(footerPanel);

		// HANDLERS
		// Create a handler for the sendButton and nameField
		class HomeButtonClickHandler implements ClickHandler {

			private int homeButtonClickHandlerPageNumber = 1;
			private int homeButtonCliclHandlerTotalAds = 0;

			public void onClick(ClickEvent event) {

				homeButton.setEnabled(false);
				setPageNumber(1);
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

			AsyncCallback<String> getMainListingByPageFromServerAsyncCall = new AsyncCallback<String>() {
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
						flex.setHTML(i1 + 1, 1, v.get("name").isString().stringValue());

						// Date
						String dateCreated = v.get("created").isString().stringValue();
						flex.setHTML(i1 + 1, 2, dateCreated);

						flex.setHTML(i1 + 1, 3, v.get("owner").isString().stringValue().toString());

						final String idStub = v.get("id").isNumber().toString();
						final String adsStub = v.get("owner").isString().stringValue().toString() + ": " + v.get("name").isString().stringValue();

						final Button buttonViewAds = new Button("View");
						buttonViewAds.setWidth("100%");
						buttonViewAds.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {

								// Window.alert("4.5");
								DialogBox box = alertWidget(idStub, adsStub, buttonViewAds.getAbsoluteLeft(), buttonViewAds.getAbsoluteTop());
								box.show();
							}
						});
						flex.setWidget(i1 + 1, 4, buttonViewAds);
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

									adCountLabel.setHTML("<p>" + appConst.VAL_LOADING() + "</p>");

									int n;
									for (n = 1; n < pageNumberTable.getCellCount(0); n++) {
										Button button = (Button) pageNumberTable.getWidget(0, n);
										button.setEnabled(false);
										if (n == iPage) {
											button.setHTML("<b>" + iPage + "</b>");
										}
									}
									setPageNumber(iPage);
									getMainListingByPageFromServer();
								}
							});
							pageNumberTable.setWidget(0, i2, pageButton);
						}
					}

					// Repatriate button :) finally..
					homeButton.setEnabled(true);
				}
			};

			private void getTotalAdsFromServer() {

				gwtService.getTotalAds(getTotalAdsFromServer);
			}

			private void getMainListingByPageFromServer() {

				homeButton.setEnabled(false);
				gwtService.getMainListing(getPageNumber(), getMainListingByPageFromServerAsyncCall);
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
		}

		// Add a handler refresh Flex Table
		HomeButtonClickHandler mainButtonClickHandler = new HomeButtonClickHandler();
		homeButton.addClickHandler(mainButtonClickHandler);

		// Initialise()
		switch (currentAppPage) {
		case 1:
			menuTable.getCellFormatter().setStyleName(0, 0, "cw-FlexTable-navigation-current-page");
			mainButtonClickHandler.getTotalAdsFromServer();
			mainButtonClickHandler.getMainListingByPageFromServer();
			break;
		default:
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
