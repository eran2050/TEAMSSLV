package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Application implements EntryPoint {

	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	public final AppConst appConst = GWT.create(AppConst.class);

	private final IndexGWTWrapperAsync gwtService = GWT
			.create(IndexGWTWrapper.class);

	public static DialogBox alertWidget(final String header,
			final String content) {
		final DialogBox box = new DialogBox();
		box.setAnimationEnabled(true);
		box.center();
		final VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		box.setText(header);
		panel.add(new Label(content));
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
		panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);
		box.add(panel);
		return box;
	}

	public void onModuleLoad() {

		// Header / Main Container / Footer
		DecoratorPanel decorHeaderPanel = new DecoratorPanel();
		decorHeaderPanel.setStyleName("gwt-DecoratorPanel");

		// HEADER
		HorizontalPanel headerPanel = new HorizontalPanel();
		FlexTable menuTable = new FlexTable();
		menuTable.addStyleName("cw-FlexTable");
		// menuTable.addStyleName("table.nm");
		// menuTable.addStyleName("table.td");

		// Buttons
		final Button mainButton = new Button("Home");
		mainButton.addStyleName("sendButton");
		menuTable.setWidget(0, 0, mainButton);
		menuTable.setWidget(0, 1, new Button("Add"));
		menuTable.setWidget(0, 2, new Button("Admin"));
		menuTable.setWidget(0, 3, new TextBox());
		menuTable.setWidget(0, 4, new Button("Login"));
		headerPanel.add(menuTable);
		decorHeaderPanel.add(headerPanel);
		RootPanel.get("headerWidgetStub").add(decorHeaderPanel);

		// MAIN CONTAINER
		DecoratorPanel decorMainPanel = new DecoratorPanel();
		HorizontalPanel mainPanel = new HorizontalPanel();
		decorMainPanel.setStyleName("gwt-DecoratorPanel");
		final FlexTable flex = new FlexTable();
		flex.setHTML(0, 0, "Loading...");
		mainPanel.add(flex);
		decorMainPanel.add(mainPanel);
		RootPanel.get("mainContainerStub").add(decorMainPanel);

		// Page numbers
		DecoratorPanel decorPages = new DecoratorPanel();
		decorPages.setStyleName("gwt-DecoratorPanel");
		HorizontalPanel pagesPanel = new HorizontalPanel();
		final FlexTable pageNumberTable = new FlexTable();
		pageNumberTable.setHTML(0, 0, "Pages");
		pageNumberTable.setStyleName("cw-FlexTable");
		pagesPanel.add(pageNumberTable);
		decorPages.add(pagesPanel);
		RootPanel.get("secondContainerStub").add(decorPages);

		// FOOTER
		HorizontalPanel footerPanel = new HorizontalPanel();
		DecoratorPanel decorFooterPanel = new DecoratorPanel();
		decorFooterPanel.setStyleName("gwt-DecoratorPanel");
		FlexTable footerTable = new FlexTable();
		footerTable.setHTML(0, 0, "T2CSupp&nbsp;Staff&nbsp;(c)&nbsp;"
				+ appConst.APP_VERSION() + "&nbsp;");
		footerPanel.add(footerTable);
		footerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		decorFooterPanel.add(footerPanel);
		RootPanel.get("footerWidgetStub").add(decorFooterPanel);

		// Alert Widget
		// RootPanel.get("thirdContainerStub").add(alertWidget);

		// HANDLERS
		// Create a handler for the sendButton and nameField
		class MainButtonClickHandler implements ClickHandler {

			private int mainButtonClickHandlerPageNumber = 1;
			private int mainButtonCliclHandlerTotalAds = 0;

			public void onClick(ClickEvent event) {

				mainButton.setEnabled(false);
				getTotalAdsFromServer();
				getMainListingByPageFromServer();
			}

			AsyncCallback<Integer> getTotalAdsFromServer = new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {

					Window.alert(SERVER_ERROR);
				}

				@Override
				public void onSuccess(Integer result) {

					setTotalAds(result.intValue());
				}

			};

			AsyncCallback<String> getMainListingByPageFromServerAsyncCall = new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {

					mainButton.setEnabled(true);
					mainButton.setFocus(true);
				}

				public void onSuccess(String result) {

					flex.clear();
					flex.setCellPadding(3);
					flex.setCellSpacing(5);
					flex.addStyleName("cw-FlexTable");

					// Parse JSON
					String json = result;
					JSONArray array = (JSONArray) JSONParser.parseStrict(json);
					JSONObject v;

					// Header row
					flex.setHTML(0, 0, "id");
					flex.setHTML(0, 1, "name");
					flex.setHTML(0, 2, "owner");
					flex.setHTML(0, 3, "created");

					// Data rows
					int arrayBoundary = 0;

					if (getPageNumber() * appConst.VAL_ADS_PER_MAIN_PAGE() <= getTotalAds()
							&& getTotalAds() != 0) {
						arrayBoundary = appConst.VAL_ADS_PER_MAIN_PAGE();
					}

					if (getPageNumber() * appConst.VAL_ADS_PER_MAIN_PAGE() > getTotalAds()
							&& getTotalAds() != 0) {
						arrayBoundary = getTotalAds()
								- ((getPageNumber() - 1) * appConst
										.VAL_ADS_PER_MAIN_PAGE()) + 1;
					}

					int i;
					for (i = 0; i < arrayBoundary; i++) {
						v = array.get(i).isObject();

						flex.setHTML(i + 1, 0, v.get("id").isNumber()
								.toString());
						flex.setHTML(i + 1, 1, v.get("name").isString()
								.stringValue());
						flex.setHTML(i + 1, 2, v.get("owner").isString()
								.stringValue().toString());
						flex.setHTML(i + 1, 3, v.get("created").isString()
								.stringValue());

						final String idStub = v.get("id").isNumber().toString();
						final String adsStub = v.get("owner").isString()
								.stringValue().toString()
								+ ": " + v.get("name").isString().stringValue();

						Button buttonViewAds = new Button("View");
						buttonViewAds.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {

								DialogBox box = alertWidget(idStub, adsStub);
								box.show();
							}
						});
						flex.setWidget(i + 1, 4, buttonViewAds);
					}

					// Clear the rest of the table
					Window.alert("getRowcount=" + flex.getRowCount()
							+ ", arrayBoundary=" + arrayBoundary);

					// if (flex.getRowCount() > arrayBoundary - 1) {
					// for (i = arrayBoundary; i < flex.getRowCount(); i++)
					// flex.removeRow(i);
					// }

					// Draw buttons
					int buttonsToDraw = (int) (getTotalAds() % appConst
							.VAL_ADS_PER_MAIN_PAGE()) - 2;
					if (buttonsToDraw < 0)
						buttonsToDraw = 0;

					i = 0;
					pageNumberTable.clear();
					for (i = 0; i < buttonsToDraw; i++) {
						final Button pageButton = new Button(
								Integer.toString(i + 1));
						pageButton.setSize("30px", "30px");

						final int iPage = i;
						pageButton.addClickHandler(new ClickHandler() {

							public void onClick(ClickEvent event) {

								int i = 0;
								Button button;
								for (i = 1; i < pageNumberTable.getCellCount(0); i++) {
									button = (Button) pageNumberTable
											.getWidget(0, i);
									button.setEnabled(false);
									if (i == getPageNumber()) {
										button.setFocus(true);
									}
								}
								setPageNumber(iPage + 1);
								getMainListingByPageFromServer();
							}
						});
						pageNumberTable.setWidget(0, i + 1, pageButton);
					}

					// Repatriate button :) finally..
					mainButton.setEnabled(true);
				}
			};

			private void getTotalAdsFromServer() {

				gwtService.getTotalAds(getTotalAdsFromServer);
			}

			private void getMainListingByPageFromServer() {

				mainButton.setEnabled(false);
				gwtService.getMainListing(getPageNumber(),
						getMainListingByPageFromServerAsyncCall);
			}

			public int getPageNumber() {
				return mainButtonClickHandlerPageNumber;
			}

			public void setPageNumber(int mainButtonClickHandlerPageNumber) {
				this.mainButtonClickHandlerPageNumber = mainButtonClickHandlerPageNumber;
			}

			public int getTotalAds() {
				return mainButtonCliclHandlerTotalAds;
			}

			public void setTotalAds(int mainButtonCliclHandlerTotalAds) {
				this.mainButtonCliclHandlerTotalAds = mainButtonCliclHandlerTotalAds;
			}
		}

		// Add a handler refresh Flex Table
		MainButtonClickHandler mainButtonClickHandler = new MainButtonClickHandler();

		// Initialise()
		mainButtonClickHandler.getTotalAdsFromServer();
		mainButtonClickHandler.getMainListingByPageFromServer();
		mainButton.addClickHandler(mainButtonClickHandler);

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
