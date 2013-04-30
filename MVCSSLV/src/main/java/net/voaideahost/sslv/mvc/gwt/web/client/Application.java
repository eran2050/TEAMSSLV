package net.voaideahost.sslv.mvc.gwt.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
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
	private static final String AppVersion = "T2CSupp Staff&nbsp;(c)&nbsp;"
			+ "LALALA" + "&nbsp;";

	private final IndexGWTWrapperAsync gwtService = GWT
			.create(IndexGWTWrapper.class);

	public void onModuleLoad() {

		// Header / Main Container / Footer
		DecoratorPanel decorPanel = new DecoratorPanel();

		// HEADER
		HorizontalPanel headerPanel = new HorizontalPanel();
		headerPanel.setSpacing(5);
		final Button mainButton = new Button("Main");
		headerPanel.add(mainButton);
		headerPanel.add(new Button("Add"));
		headerPanel.add(new Button("Admin"));
		headerPanel.add(new Button("Login"));
		headerPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_JUSTIFY);
		decorPanel.add(headerPanel);
		RootPanel.get("headerWidgetStub").add(headerPanel);

		// MAIN CONTAINER
		final FlexTable flex = new FlexTable();
		decorPanel.add(flex);
		RootPanel.get("mainContainer").add(flex);

		// FOOTER
		HorizontalPanel footerPanel = new HorizontalPanel();
		footerPanel.setSpacing(5);
		footerPanel.add(new HTML(AppVersion));
		footerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		decorPanel.add(footerPanel);
		RootPanel.get("footerWidgetStub").add(footerPanel);

		// Compiling everything
		RootPanel.get("bodyContainer").add(decorPanel);

		// HANDLERS
		// Create a handler for the sendButton and nameField
		class MainButtonClickHandler implements ClickHandler {
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			private void sendNameToServer() {

				// Then, we send the input to the server.
				mainButton.setEnabled(false);
				gwtService.getMainListing(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {

						// Show the RPC error message to the user
						mainButton.setEnabled(true);
						mainButton.setFocus(true);
					}

					public void onSuccess(String result) {

						flex.clear();
						flex.setHTML(0, 0, "OK");

						// Parse JSON
						String json = result;
						JSONArray array = (JSONArray) JSONParser
								.parseStrict(json);
						JSONObject val;
						int i;
						int col = 0;
						for (i = 0; i < array.size(); i++) {
							val = array.get(i).isObject();
							flex.setHTML(i, col++, val.get("id").isString()
									.toString());
							flex.setHTML(i, col++, val.get("name").isString()
									.toString());
							flex.setHTML(i, col++, val.get("owner").isString()
									.toString());
							flex.setHTML(i, col++, val.get("created")
									.isString().toString());
							col = 0;
						}
						mainButton.setEnabled(true);
						mainButton.setFocus(true);
					}
				});
			}
		}

		// Add a handler refresyh Flex Table
		MainButtonClickHandler mainButtonClickHandler = new MainButtonClickHandler();
		mainButton.addClickHandler(mainButtonClickHandler);

		// default GWT Hello World
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class SendButtonClickHandler implements ClickHandler, KeyUpHandler {
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			private void sendNameToServer() {
				String textToServer = nameField.getText();

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				gwtService.greet(textToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {

						// Show the RPC error message to the user
						dialogBox.setText("Remote Procedure Call - Failure");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}

					public void onSuccess(String result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel
								.removeStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(result);
						dialogBox.center();
						closeButton.setFocus(true);
					}
				});
			}
		}

		// Add a handler to send the name to the server
		SendButtonClickHandler handler = new SendButtonClickHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
