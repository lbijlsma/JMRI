package jmri.jmrit.vsdecoder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractButton;
import jmri.util.swing.JmriMouseEvent;
import jmri.util.swing.JmriMouseListener;
import org.jdom2.Element;

/**
 * Button trigger.
 *
 * <hr>
 * This file is part of JMRI.
 * <p>
 * JMRI is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <p>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 *
 * @author Mark Underwood Copyright (C) 2011
 */
public class ButtonTrigger extends Trigger implements ActionListener, JmriMouseListener {

    enum ButtonAction {
    }

    boolean match_value;

    public ButtonTrigger(String name) {
        this(name, false);
    }

    public ButtonTrigger(String name, boolean bv) {
        super(name);
        this.setTriggerType(Trigger.TriggerType.BUTTON);
        match_value = bv;
    }

    public void setMatchValue(boolean bv) {
        match_value = bv;
    }

    public boolean getMatchValue() {
        return match_value;
    }

    // Button action functions called directly from the enclosing SoundEvent.
    public void mouseDown() {
        log.debug("buttonTrigger {} mouseDown() called.", getName());
        if (match_value) {
            this.callback.takeAction();
        }
    }

    public void mouseUp() {
        log.debug("buttonTrigger {} mouseUp() called.", getName());
        if (!match_value) {
            this.callback.takeAction();
        }
    }

    public void click(boolean v) {
        log.debug("buttonTrigger {} click({}) called.", getName(), v);
        if (v == match_value) {
            this.callback.takeAction();
        }
    }

    // PropertyChangeListener functions
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // Button triggers respond to the button methods above, not to
        // property change events.  Do nothing.
    }

    // ActionListener function(s)
    @Override
    public void actionPerformed(ActionEvent e) {
        log.debug("ButtonTrigger.actionPerformed() {}", this.getName());
        this.click(((AbstractButton) e.getSource()).isSelected());
    }

    // MouseListener functions
    @Override
    public void mousePressed(JmriMouseEvent e) {
        log.debug("MouseListener.mousePressed() {}", this.getName());
        this.mouseDown();
    }

    @Override
    public void mouseReleased(JmriMouseEvent e) {
        log.debug("MouseListener.mouseReleased() {}", this.getName());
        this.mouseUp();
    }

    @Override
    public void mouseEntered(JmriMouseEvent e) {
    }

    @Override
    public void mouseExited(JmriMouseEvent e) {
    }

    @Override
    public void mouseClicked(JmriMouseEvent e) {
    }

    @Override
    public Element getXml() {
        Element me = new Element("Trigger");

        log.debug("Bool Trigger getXml():");
        log.debug("  trigger_name = {}", this.getName());
        log.debug("  event_name = {}", this.event_name);
        log.debug("  target_name = {}", target.getName());
        log.debug("  match = {}", Boolean.valueOf(match_value).toString());
        log.debug("  action = {}", this.getTriggerType().toString());

        me.setAttribute("name", this.getName());
        me.setAttribute("type", "BOOLEAN");
        me.addContent(new Element("event-name").addContent(event_name));
        me.addContent(new Element("target-name").addContent(target.getName()));
        me.addContent(new Element("match").addContent(Boolean.valueOf(match_value).toString()));
        me.addContent(new Element("action").addContent(this.getTriggerType().toString()));

        return me;
    }

    @Override
    public void setXml(Element e) {
        // Get common stuff
        super.setXml(e);
        // Only do this if this is a ButtonTrigger type Element
        if (e.getAttribute("type").getValue().equals("BUTTON")) {
            match_value = Boolean.parseBoolean(e.getChild("match").getValue());
        }
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ButtonTrigger.class);

}
