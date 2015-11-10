package com.vaadin.tutorial.addressbook;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.tutorial.addressbook.backend.PortTime;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.
 */
public class PortTimeForm extends FormLayout {

    Button save = new Button("Save", this::save);
    Button cancel = new Button("Cancel", this::cancel);
    
    TextField port = new TextField("PortName:");
    DateField eta = new DateField("ETA:");
    DateField etd = new DateField("ETD:");
    DateField ata = new DateField("ATA:");
    DateField atd = new DateField("ATD:");
    DateField cutOff = new DateField("ATD:");

    PortTime portTime;

    // Easily bind forms to beans and manage validation and buffering
    BeanFieldGroup<PortTime> formFieldBindings;

    public PortTimeForm() {
        configureComponents();
        addEditPannel();
    }

    private void configureComponents() {
        /* Highlight primary actions.
         *
         * With Vaadin built-in styles you can highlight the primary save button
         * and give it a keyboard shortcut for a better UX.
         */
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void addEditPannel() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        actions.setSpacing(true);
//        HorizontalLayout et = new HorizontalLayout(eta, etd);
//        et.setSpacing(true);
        
        addComponents(actions, port, eta, etd, ata, atd, cutOff);
    }

    /* Use any JVM language.
     *
     * Vaadin supports all languages supported by Java Virtual Machine 1.6+.
     * This allows you to program user interface in Java 8, Scala, Groovy or any other
     * language you choose.
     * The new languages give you very powerful tools for organizing your code
     * as you choose. For example, you can implement the listener methods in your
     * compositions or in separate controller classes and receive
     * to various Vaadin component events, like button clicks. Or keep it simple
     * and compact with Lambda expressions.
     */
    public void save(Button.ClickEvent event) {
        try {
            // Commit the fields from UI to DAO
            formFieldBindings.commit();

            // Save DAO to backend with direct synchronous portTimeCollectionService API
            getUI().portTimeCollectionService.save(portTime);

            String msg = String.format("Saved '%s %s'.",
                    portTime.getPort(),
                    portTime.getCut_off());
            Notification.show(msg, Type.TRAY_NOTIFICATION);
            getUI().refreshPortTime();
        } catch (FieldGroup.CommitException e) {
            // Validation exceptions could be shown here
        }
    }

    public void cancel(Button.ClickEvent event) {
        // Place to call business logic.
        Notification.show("Cancelled", Type.TRAY_NOTIFICATION);
        getUI().portTimeList.select(null);
    }

    void edit(PortTime portTime) {
        this.portTime = portTime;
        if (portTime != null) {
            // Bind the properties of the portTime POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(portTime, this);
            port.focus();
        }
        setVisible(portTime != null);
    }

    @Override
    public PortTimeUI getUI() {
        return (PortTimeUI) super.getUI();
    }

}
