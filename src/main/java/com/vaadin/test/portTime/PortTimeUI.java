package com.vaadin.test.portTime;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.test.portTime.backend.PortTime;
import com.vaadin.test.portTime.backend.PortTimeService;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

/* User Interface written in Java.
 *
 * Define the user interface shown on the Vaadin generated web page by extending the UI class.
 * By default, a new UI instance is automatically created when the page is loaded. To reuse
 * the same instance, add @PreserveOnRefresh.
 */
@Title("port_times")
@Theme("valo")
public class PortTimeUI extends UI {







	/* Hundreds of widgets.
	 * Vaadin's user interface components are just Java objects that encapsulate
	 * and handle cross-browser support and client-server communication. The
	 * default Vaadin components are in the com.vaadin.ui package and there
	 * are over 500 more in vaadin.com/directory.
     */
    TextField filter = new TextField();
    Grid portTimeList = new Grid();
    Button newPortTime = new Button("New PortTime");

    // portTimeForm is an example of a custom component class
    PortTimeForm portTimeForm = new PortTimeForm();

    // PortTimeService is a in-memory mock DAO that mimics
    // a real-world datasource. Typically implemented for
    // example as EJB or Spring Data based portTimeCollectionService.
    PortTimeService portTimeCollectionService = PortTimeService.createDemoService();


    /* The "Main method".
     *
     * This is the entry point method executed to initialize and configure
     * the visible user interface. Executed on every browser reload because
     * a new instance is created for each web page loaded.
     */
    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }


    private void configureComponents() {
         /* Synchronous event handling.
         *
         * Receive user interaction events on the server-side. This allows you
         * to synchronously handle those events. Vaadin automatically sends
         * only the needed changes to the web page without loading a new page.
         */
        newPortTime.addClickListener(e -> portTimeForm.edit(new PortTime()));

        filter.setInputPrompt("Filter port...");
        filter.addTextChangeListener(e -> refreshPortTimes(e.getText()));

        portTimeList.setContainerDataSource(new BeanItemContainer<>(PortTime.class));
        portTimeList.setColumnOrder( "port", "eta", "etd");
        portTimeList.removeColumn("id");
//        portTimeList.removeColumn("birthDate");
//        portTimeList.removeColumn("phone");
        portTimeList.setSelectionMode(Grid.SelectionMode.SINGLE);
        portTimeList.addSelectionListener(e
                -> portTimeForm.edit((PortTime) portTimeList.getSelectedRow()));
        refreshPortTime();
    }

    /* Robust layouts.
     *
     * Layouts are components that contain other components.
     * HorizontalLayout contains TextField and Button. It is wrapped
     * with a Grid into VerticalLayout for the left side of the screen.
     * Allow user to resize the components with a SplitPanel.
     *
     * In addition to programmatically building layout in Java,
     * you may also choose to setup layout declaratively
     * with Vaadin Designer, CSS and HTML.
     */
    private void buildLayout() {
        HorizontalLayout filterPort = new HorizontalLayout(filter, newPortTime);
        filterPort.setWidth("100%");
        filter.setWidth("100%");
        filterPort.setExpandRatio(filter, 1);

        VerticalLayout leftDown = new VerticalLayout(filterPort, portTimeList);
        leftDown.setSizeFull();
        portTimeList.setSizeFull();
        leftDown.setExpandRatio(portTimeList, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(leftDown, portTimeForm);
        mainLayout.setSizeFull();
        mainLayout.setExpandRatio(leftDown, 1);

        // Split and allow resizing
        setContent(mainLayout);
    }

    /* Choose the design patterns you like.
     *
     * It is good practice to have separate data access methods that
     * handle the back-end access and/or the user interface updates.
     * You can further split your code into classes to easier maintenance.
     * With Vaadin you can follow MVC, MVP or any other design pattern
     * you choose.
     */
    void refreshPortTime() {
        refreshPortTimes(filter.getValue());
    }

    private void refreshPortTimes(String stringFilter) {
        portTimeList.setContainerDataSource(new BeanItemContainer<>(
                PortTime.class, portTimeCollectionService.findAll(stringFilter)));
        portTimeForm.setVisible(false);
    }




    /*  Deployed as a Servlet or Portlet.
     *
     *  You can specify additional servlet parameters like the URI and UI
     *  class name and turn on production mode when you have finished developing the application.
     */
    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = PortTimeUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


}
