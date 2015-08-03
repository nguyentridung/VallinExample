package com.example.helloworld;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("helloworld")
public class HelloworldUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = HelloworldUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		final HorizontalLayout hlayout = new HorizontalLayout();
		final VerticalLayout layout2 = new VerticalLayout();
		final VerticalLayout layout1 = new VerticalLayout();
		hlayout.addComponent(layout1);
		hlayout.addComponent(layout2);
		layout1.setMargin(true);
		setContent(hlayout);

		final Button button2 = new Button("Push it!");
		button2.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Notification.show("Push!");
			}
		});
		layout1.addComponent(button2);
		layout1.addComponent(new Image(null, new ClassResource("example.jpg")));
		ThemeResource resource = new ThemeResource("example1.jpg");

		// Use the resource
		Image image = new Image("My Theme Image", resource);
		layout1.addComponent(image);
		TextField name = new TextField("Name");
		layout1.addComponent(name);

		Button button = new Button("Dung");
		button.setDescription("<h2><img src=\"../VAADIN/themes/helloworld/google.png\"/>"
				+ "A richtext tooltip</h2>"
				+ "<ul>"
				+ "  <li>Use rich formatting with HTML</li>"
				+ "  <li>Include images from themes</li>"
				+ "  <li>etc.</li>"
				+ "</ul>");
		layout1.addComponent(button);

		// Component with an icon from another theme ('runo')
		Button ok = new Button("OK");
		ok.setIcon(new ThemeResource("../helloworld/google.png"));
		layout1.addComponent(ok);

		// Component for which the locale is meaningful
		InlineDateField date = new InlineDateField("Datum");

		// German language specified with ISO 639-1 language
		// code and ISO 3166-1 alpha-2 country code.
		date.setLocale(new Locale("vi", "VI"));

		date.setResolution(Resolution.DAY);
		layout1.addComponent(date);

		// A field with automatic validation disabled
		final TextField field = new TextField("Name");
		field.setValidationVisible(false);
		layout2.addComponent(field);

		// Define validation
		field.addValidator(new StringLengthValidator(
				"The name must be 1-10 letters (was {0})", 1, 10, true));

		// Run validation
		Button validate = new Button("Validate");
		validate.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					field.validate();
				} catch (InvalidValueException e) {
					Notification.show(e.getMessage());
				}
			}
		});
		layout2.addComponent(validate);
		// spacing
		Label emptyLabel = new Label("&nbsp;", ContentMode.HTML);
		layout2.addComponent(emptyLabel);

		// Have a bean container to put the beans in
		final BeanItemContainer<Planet> container = new BeanItemContainer<Planet>(
				Planet.class);
		container.addItem(new Planet(1, "Peter"));
		container.addItem(new Planet(2, "Nany"));
		container.addItem(new Planet(3, "Messi"));
		container.addItem(new Planet(4, "Ronaldo"));

		final ComboBox select = new ComboBox("Select or Add a Planet",
				container);

		// Use the name property for item captions
		select.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		select.setItemCaptionPropertyId("name");
		select.setNewItemsAllowed(true);
		// Custom handling for new items
		select.setNewItemHandler(new NewItemHandler() {
			@Override
			public void addNewItem(String newItemCaption) {
				// Create a new bean - can't set all properties
				Planet newPlanet = new Planet(0, newItemCaption);
				container.addBean(newPlanet);

				// Remember to set the selection to the new item
				select.select(newPlanet);

				Notification.show("Added new planet called " + newItemCaption);
			}
		});
		layout2.addComponent(select);
		// A selection component with some items
		ListSelect selectListSelect = new ListSelect("My Selection");
		selectListSelect.addItems("Mercury", "Venus", "Earth", "Mars",
				"Jupiter", "Saturn", "Uranus", "Neptune");
		layout2.addComponent(selectListSelect);
		// Multiple selection mode
		selectListSelect.setMultiSelect(true);

		// Feedback on value changes

		selectListSelect.setImmediate(true);
		selectListSelect.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				// some feedback
				layout2.addComponent(new Label("Selected: "
						+ event.getProperty().getValue().toString()));
			}
		});
		layout2.addComponent(emptyLabel);
		// Have a label with some text
		Label label = new Label("edit this");

		// Get the label's text to initialize a field
		TextField editor = new TextField(null, // No caption
				label.getValue());

		// Change the label's text
		editor.addValueChangeListener(event -> // Java 8
		label.setValue(editor.getValue()));
		editor.setImmediate(true); // Send on Enter

		// Display nicely -------------------------------------------
		Label arrows = new Label(FontAwesome.EXCHANGE.getHtml(),
				ContentMode.HTML);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponents(label, arrows, editor);
		horizontalLayout.setSpacing(true);
		for (Component c : horizontalLayout)
			horizontalLayout.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
		layout2.addComponent(horizontalLayout);

		// A container with a defined width.
		Panel panel = new Panel("Panel Containing a Label");
		panel.setWidth("300px");

		panel.setContent(new Label("This is a Label inside a Panel. There is "
				+ "enough text in the label to make the text "
				+ "wrap when it exceeds the width of the panel."));
		layout2.addComponent(panel);

		// Label in different modes
		FormLayout formLayout = new FormLayout();
		formLayout.addStyleName("withborders");
		formLayout.setWidth("600px");

		Label textLabel = new Label(
				"Text where formatting characters, such as \\n, "
						+ "and HTML, such as <b>here</b>, are quoted.",
				ContentMode.TEXT);

		Label preLabel = new Label(
				"Preformatted text is shown in an HTML <pre> tag.\n"
						+ "Formatting such as\n" + "  * newlines\n"
						+ "  * whitespace\n"
						+ "and such are preserved. HTML tags, \n"
						+ "such as <b>bold</b>, are quoted.",
				ContentMode.PREFORMATTED);

		Label htmlLabel = new Label(
				"In HTML mode, all HTML formatting tags, such as \n" + "<ul>"
						+ "  <li><b>bold</b></li>"
						+ "  <li>itemized lists</li>" + "  <li>etc.</li>"
						+ "</ul> " + "are preserved.", ContentMode.HTML);

		textLabel.setCaption("TEXT");
		preLabel.setCaption("PREFORMATTED");
		htmlLabel.setCaption("HTML");
		formLayout.addComponents(textLabel, preLabel, htmlLabel);
		// layout2.addComponent(new Label("&nbsp;", ContentMode.HTML));

		Label gap = new Label();
		gap.setHeight("1em");
		layout2.addComponent(gap);
		layout2.addComponent(formLayout);
		// VerticalLayout 3
		VerticalLayout layout3 = new VerticalLayout();
		hlayout.addComponent(layout3);
		// Image link
		Link iconic = new Link("Click into google.com", new ExternalResource(
				"http://google.com/"));
		iconic.setIcon(new ThemeResource("google.png"));
		iconic.addStyleName("icon-after-caption");
		iconic.setTargetName("_blank");
		// open link in popup
		iconic.setTargetBorder(Link.TARGET_BORDER_NONE);
		iconic.setTargetHeight(300);
		iconic.setTargetWidth(400);
		layout3.addComponent(iconic);

		// Create a text field
		TextField tf = new TextField("A Field");

		// Put some initial content in it
		tf.setValue("Stuff in the field");
		// Handle changes in the value

		tf.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				// TODO Auto-generated method stub
				// Assuming that the value type is a String
				String value = (String) event.getProperty().getValue();

				// Do something with the value
				Notification.show("Value is: " + value);
			}
		});

		// Fire value changes immediately when the field loses focus
		tf.setImmediate(true);

		layout3.addComponent(tf);

		// Text field with maximum length
		final TextField tf1 = new TextField("My Eventful Field");
		tf1.setValue("Initial content");
		tf1.setMaxLength(20);
		tf1.setStyleName("dashing");

		// Counter for input length
		final Label counter = new Label();
		counter.setValue(tf1.getValue().length() + " of " + tf1.getMaxLength());

		// Display the current length interactively in the counter
		tf1.addTextChangeListener(new TextChangeListener() {
			public void textChange(TextChangeEvent event) {
				int len = event.getText().length();
				counter.setValue(len + " of " + tf1.getMaxLength());
			}
		});

		// The lazy mode is actually the default
		tf1.setTextChangeEventMode(TextChangeEventMode.LAZY);
		layout3.addComponent(tf1);
		layout3.addComponent(counter);
		// Create the area
		layout3.addComponent(new Label("&nbsp;",ContentMode.HTML));
		TextArea area = new TextArea("Big Area");
		area.setHeight("5em");      
		// Put some content in it
		area.setValue("A row\n"+
		              "Another row\n"+
		              "Yet another row");
		layout3.addComponent(area);
		
		//textarea set wordwrap for long text
		TextArea area1 = new TextArea("Wrapping");
		area1.setWordwrap(true); // The default
		area1.setValue("A quick brown fox jumps over the lazy dog");
		area1.setReadOnly(true);
		layout3.addComponent(area1);
		
		// Create a rich text area
		final RichTextArea rtarea = new RichTextArea();
		rtarea.setCaption("My Rich Text Area");

		// Set initial content as HTML
		rtarea.setValue("Hello" +
		    "This rich text area contains some text.");
		layout3.addComponent(rtarea);
		
		// Create a DateField with the default style
		DateField date1 = new DateField();
			

		// Set the date and time to present
		date1.setValue(new Date());
		Label space = new Label();
		space.setHeight("1em");
		layout3.addComponent(space);
		layout3.addComponent(date1);

	}

}

// A class that implements toString()
class Planet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String name;

	public Planet(int id, String name) {
		this.id = id;
		this.name = name;
	}

}
