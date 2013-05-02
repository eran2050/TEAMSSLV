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
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Application implements EntryPoint {

	@SuppressWarnings ("unused")
	private static final String			SERVER_ERROR	= "An error occurred while "
																+ "attempting to contact the server. Please check your network "
																+ "connection and try again.";

	public final AppConst				appConst		= GWT.create(AppConst.class);

	private final IndexGWTWrapperAsync	gwtService		= GWT.create(IndexGWTWrapper.class);

	public void onModuleLoad() {

		// Header / Main Container / Footer
		DecoratorPanel decorHeaderPanel = new DecoratorPanel();
		decorHeaderPanel.setStyleName("gwt-DecoratorPanel");

		// HEADER
		HorizontalPanel headerPanel = new HorizontalPanel();
		FlexTable menuTable = new FlexTable();
		menuTable.addStyleName("cw-FlexTable");
		menuTable.addStyleName("table.nm");
		menuTable.addStyleName("table.td");

		// Buttons
		final Button mainButton = new Button("Main");
		mainButton.addStyleName("sendButton");
		menuTable.setWidget(0, 0, mainButton);
		menuTable.setWidget(0, 1, new Button("Add"));
		menuTable.setWidget(0, 2, new Button("Admin"));
		menuTable.setWidget(0, 3, new Button("Login"));
		headerPanel.add(menuTable);
		decorHeaderPanel.add(headerPanel);
		RootPanel.get("headerWidgetStub").add(decorHeaderPanel);

		// MAIN CONTAINER
		HorizontalPanel mainPanel = new HorizontalPanel();
		DecoratorPanel decorMainPanel = new DecoratorPanel();
		decorMainPanel.setStyleName("gwt-DecoratorPanel");
		final FlexTable flex = new FlexTable();
		flex.setHTML(0, 0, "Loading...");
		mainPanel.add(flex);
		decorMainPanel.add(mainPanel);
		RootPanel.get("mainContainer").add(decorMainPanel);

		// FOOTER
		HorizontalPanel footerPanel = new HorizontalPanel();
		DecoratorPanel decorFooterPanel = new DecoratorPanel();
		decorFooterPanel.setStyleName("gwt-DecoratorPanel");
		FlexTable footerTable = new FlexTable();
		footerTable.setHTML(0, 0, appConst.APP_VERSION());
		footerPanel.add(footerTable);
		footerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		decorFooterPanel.add(footerPanel);
		RootPanel.get("footerWidgetStub").add(decorFooterPanel);

		// HANDLERS
		// Create a handler for the sendButton and nameField
		class MainButtonClickHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				getMainListingByPageFromServer();
			}

			private void getMainListingByPageFromServer() {

				// Then, we send the input to the server.
				mainButton.setEnabled(false);
				gwtService.getMainListing(1, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {

						// Show the RPC error message to the user
						mainButton.setEnabled(true);
						mainButton.setFocus(true);
					}

					public void onSuccess(String result) {

						flex.clear();
						flex.setCellPadding(3);
						flex.setCellSpacing(3);
						flex.addStyleName("cw-FlexTable");

						// Parse JSON
						String json = result;
						JSONArray array = (JSONArray) JSONParser
								.parseStrict(json);
						JSONObject v;
						int i;
						int col = 0;

						// Header row
						flex.setHTML(0, 0, "id");
						flex.setHTML(0, 1, "name");
						flex.setHTML(0, 2, "owner");
						flex.setHTML(0, 3, "created");
						flex.getRowFormatter().setStyleName(0, "th");

						// Data rows
						for (i = 0; i < array.size(); i++) {
							v = array.get(i).isObject();
							flex.setHTML(i + 1, col++, v.get("id").isNumber()
									.toString());
							flex.setHTML(i + 1, col++, v.get("name").isString()
									.stringValue());
							flex.setHTML(i + 1, col++, v.get("owner")
									.isString().stringValue().toString());
							flex.setHTML(i + 1, col++, v.get("created")
									.isString().stringValue());
							flex.setWidget(i + 1, col++, new Button("View"));
							col = 0;
						}
						mainButton.setEnabled(true);
					}
				});
			}
		}

		// Add a handler refresh Flex Table
		MainButtonClickHandler mainButtonClickHandler = new MainButtonClickHandler();
		// Initialise()
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
