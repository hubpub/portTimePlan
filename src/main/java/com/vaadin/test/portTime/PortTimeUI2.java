package com.vaadin.test.portTime;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.test.portTime.PortTimeForm;
import com.vaadin.test.portTime.backend.PortTime;
import com.vaadin.test.portTime.backend.PortTimeService;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import static jdk.nashorn.internal.codegen.Compiler.LOG;

/* User Interface written in Java.
 *
 * Define the user interface shown on the Vaadin generated web page by extending the UI class.
 * By default, a new UI instance is automatically created when the page is loaded. To reuse
 * the same instance, add @PreserveOnRefresh.
 */
@Title("port_times")
@Theme("runo")
public class PortTimeUI2 extends UI {

    /* Hundreds of widgets.
     * Vaadin's user interface components are just Java objects that encapsulate
     * and handle cross-browser support and client-server communication. The
     * default Vaadin components are in the com.vaadin.ui package and there
     * are over 500 more in vaadin.com/directory.
     */
    TextField textFild_filter = new TextField();
    Grid grid_portTimeList = new Grid();
    Button button_newPortTime = new Button("New PortTime");

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
        button_newPortTime.addClickListener(e -> portTimeForm.edit(new PortTime()));

        textFild_filter.setInputPrompt("Filter port...");
        textFild_filter.addTextChangeListener(e -> refreshPortTimes(e.getText()));

        grid_portTimeList.setContainerDataSource(new BeanItemContainer<>(PortTime.class));
        grid_portTimeList.setColumnOrder("port", "eta", "etd");
        grid_portTimeList.removeColumn("id");
//        grid_portTimeList.removeColumn("birthDate");
//        grid_portTimeList.removeColumn("phone");
        grid_portTimeList.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid_portTimeList.addSelectionListener(e
                -> portTimeForm.edit((PortTime) grid_portTimeList.getSelectedRow()));
        try {
            Chart chart = new Chart(ChartType.BAR);
            chart.setWidth("400px");
            chart.setHeight("300px");
// Modify the default configuration a bit
            Configuration conf = chart.getConfiguration();
            conf.setTitle("Planets");
            conf.setSubTitle("The bigger they are the harder they pull");
            conf.getLegend().setEnabled(false); // Disable legend

// The data
            ListSeries series = new ListSeries("Diameter");
            series.setData(4900, 12100, 12800,
                    6800, 143000, 125000,
                    51100, 49500);
            conf.addSeries(series);

// Set the category labels on the axis correspondingly
            XAxis xaxis = new XAxis();
            xaxis.setCategories("Mercury", "Venus", "Earth",
                    "Mars", "Jupiter", "Saturn",
                    "Uranus", "Neptune");
            xaxis.setTitle("Planet");
            conf.addxAxis(xaxis);

// Set the Y axis title
            YAxis yaxis = new YAxis();
            yaxis.setTitle("Diameter");
            yaxis.getLabels().setFormatter(
                    "function() {return Math.floor(this.value/1000) + \' Mm\';}");
            yaxis.getLabels().setStep(2);
            conf.addyAxis(yaxis);

//        layout.addComponent(chart);
        } catch (Throwable t) {
            LOG.info("Failure during static initialization", t);
            throw t;
        }

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
        HorizontalLayout filterPort = new HorizontalLayout(textFild_filter, button_newPortTime);
        filterPort.setWidth("100%");
        textFild_filter.setWidth("100%");
        filterPort.setExpandRatio(textFild_filter, 1);

        VerticalLayout leftDown = new VerticalLayout(filterPort, grid_portTimeList);
        leftDown.setSizeFull();
        grid_portTimeList.setSizeFull();
        leftDown.setExpandRatio(grid_portTimeList, 1);

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
        refreshPortTimes(textFild_filter.getValue());
    }

    private void refreshPortTimes(String stringFilter) {
        grid_portTimeList.setContainerDataSource(new BeanItemContainer<>(
                PortTime.class, portTimeCollectionService.findAll(stringFilter)));
        portTimeForm.setVisible(false);
    }

    /*  Deployed as a Servlet or Portlet.
     *
     *  You can specify additional servlet parameters like the URI and UI
     *  class name and turn on production mode when you have finished developing the application.
     */
    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = PortTimeUI2.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

}
