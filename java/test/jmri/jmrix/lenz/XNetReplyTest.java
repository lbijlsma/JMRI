package jmri.jmrix.lenz;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import jmri.Turnout;
import jmri.util.JUnitUtil;

import org.junit.Assert;
import org.junit.jupiter.api.*;

/**
 * Tests for the jmri.jmrix.lenz.XNetReply class
 *
 * @author Bob Jacobsen
 * @author  Paul Bender Copyright (C) 2004-2018
 */
public class XNetReplyTest extends jmri.jmrix.AbstractMessageTestBase {
       
    protected XNetReply msg = null;

    // Test the string constructor.
    @Test
    public void testStringCtor() {
        msg = new XNetReply("12 34 AB 03 19 06 0B B1");
        Assert.assertEquals("length", 8, msg.getNumDataElements());
        Assert.assertEquals("0th byte", 0x12, msg.getElement(0) & 0xFF);
        Assert.assertEquals("1st byte", 0x34, msg.getElement(1) & 0xFF);
        Assert.assertEquals("2nd byte", 0xAB, msg.getElement(2) & 0xFF);
        Assert.assertEquals("3rd byte", 0x03, msg.getElement(3) & 0xFF);
        Assert.assertEquals("4th byte", 0x19, msg.getElement(4) & 0xFF);
        Assert.assertEquals("5th byte", 0x06, msg.getElement(5) & 0xFF);
        Assert.assertEquals("6th byte", 0x0B, msg.getElement(6) & 0xFF);
        Assert.assertEquals("7th byte", 0xB1, msg.getElement(7) & 0xFF);
    }

    // Test the string constructor with an empty string paramter.
    @Test
    public void testStringCtorEmptyString() {
        Assert.assertEquals("length", 0, msg.getNumDataElements());
        assertEquals("empty reply", "", msg.toString());
    }

    // Test the copy constructor.
    @Test
    public void testCopyCtor() {
        XNetReply x = new XNetReply("12 34 AB 03 19 06 0B B1");
        msg = new XNetReply(x);
        Assert.assertEquals("length", x.getNumDataElements(), msg.getNumDataElements());
        Assert.assertEquals("0th byte", x.getElement(0), msg.getElement(0));
        Assert.assertEquals("1st byte", x.getElement(1), msg.getElement(1));
        Assert.assertEquals("2nd byte", x.getElement(2), msg.getElement(2));
        Assert.assertEquals("3rd byte", x.getElement(3), msg.getElement(3));
        Assert.assertEquals("4th byte", x.getElement(4), msg.getElement(4));
        Assert.assertEquals("5th byte", x.getElement(5), msg.getElement(5));
        Assert.assertEquals("6th byte", x.getElement(6), msg.getElement(6));
        Assert.assertEquals("7th byte", x.getElement(7), msg.getElement(7));
    }

    // Test the XNetMessage constructor.
    @Test
    public void testXNetMessageCtor() {
        XNetMessage x = new XNetMessage("12 34 AB 03 19 06 0B B1");
        msg = new XNetReply(x);
        Assert.assertEquals("length", x.getNumDataElements(), msg.getNumDataElements());
        Assert.assertEquals("0th byte", x.getElement(0)& 0xFF, msg.getElement(0)& 0xFF);
        Assert.assertEquals("1st byte", x.getElement(1)& 0xFF, msg.getElement(1)& 0xFF);
        Assert.assertEquals("2nd byte", x.getElement(2)& 0xFF, msg.getElement(2)& 0xFF);
        Assert.assertEquals("3rd byte", x.getElement(3)& 0xFF, msg.getElement(3)& 0xFF);
        Assert.assertEquals("4th byte", x.getElement(4)& 0xFF, msg.getElement(4)& 0xFF);
        Assert.assertEquals("5th byte", x.getElement(5)& 0xFF, msg.getElement(5)& 0xFF);
        Assert.assertEquals("6th byte", x.getElement(6)& 0xFF, msg.getElement(6)& 0xFF);
        Assert.assertEquals("7th byte", x.getElement(7)& 0xFF, msg.getElement(7)& 0xFF);
    }

    // check parity operations
    @Test
    public void testParity() {
        msg = new XNetReply("21 21 00");
        Assert.assertEquals("parity set test 1", 0, msg.getElement(2));
        assertTrue("parity check test 1", msg.checkParity());

        msg = new XNetReply("21 21 00");
        msg.setElement(0, 0x21);
        msg.setElement(1, ~0x21);
        msg.setParity();
        Assert.assertEquals("parity set test 2", 0xFF, msg.getElement(2));
        assertTrue("parity check test 2", msg.checkParity());

        msg = new XNetReply("21 21 00");
        msg.setElement(0, 0x18);
        msg.setElement(1, 0x36);
        msg.setParity();
        Assert.assertEquals("parity set test 3", 0x2E, msg.getElement(2));
        assertTrue("parity check test 3", msg.checkParity());

        msg = new XNetReply("21 21 00");
        msg.setElement(0, 0x87);
        msg.setElement(1, 0x31);
        msg.setParity();
        Assert.assertEquals("parity set test 4", 0xB6, msg.getElement(2));
        assertTrue("parity check test 4", msg.checkParity());

        msg = new XNetReply("21 21 00");
        msg.setElement(0, 0x18);
        msg.setElement(1, 0x36);
        msg.setElement(2, 0x0e);
        assertFalse("parity check test 5", msg.checkParity());

        msg = new XNetReply("21 21 00");
        msg.setElement(0, 0x18);
        msg.setElement(1, 0x36);
        msg.setElement(2, 0x8e);
        assertFalse("parity check test 6", msg.checkParity());
    }

    // test accessor methods for elements.
    // check getOpCodeHex
    @Test
    public void testGetOpCodeHex(){
       msg = new XNetReply("63 14 01 04 72");
       Assert.assertEquals("getOpCodeHex Return Value","0x63",msg.getOpCodeHex());
    }


    // check getElementBCD
    @Test
    public void testGetElementBCD(){
       msg=new XNetReply("63 14 01 04 72");
       Assert.assertEquals("getElementBCD Return Value", 14,(long)msg.getElementBCD(1));
    }

    // check skipPrefix
    @Test
    public void testSkipPrefix(){
       msg=new XNetReply("63 14 01 04 72");
       // skip prefix currently always returns -1, there is no prefix.
       Assert.assertEquals("skipPrefix return value",-1, msg.skipPrefix(0));
    }


    // get information from specific types of messages.

    // check is service mode response
    @Test
    public void testIsServiceModeResponse() {
        // CV 1 in direct mode.
        XNetReply r = new XNetReply("63 14 01 04 72");
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 257 in direct mode.
        r = new XNetReply("63 15 01 04 72");
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 513 in direct mode.
        r = new XNetReply("63 16 01 04 72");
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 769 in direct mode.
        r = new XNetReply("63 17 01 04 72");
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 1 in paged mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 286 in direct mode.
        r = new XNetReply("63 15 1E 14 7C");
        Assert.assertTrue(r.isServiceModeResponse());
        // not a service mode response.
        r = new XNetReply("01 04 05");
        Assert.assertFalse(r.isServiceModeResponse());
        // Command Station Version reply
        r = new XNetReply("63 21 36 00 55");
        Assert.assertFalse(r.isServiceModeResponse());
    }
 
   @Test
    public void testToMonitorStringServiceModeDirectResponse(){
        XNetReply r = new XNetReply("63 14 01 04 72");
        Assert.assertEquals("Monitor String","Service Mode: Direct Programming Mode Response: CV:1 Value:4",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringServiceModePagedResponse(){
        XNetReply r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Monitor String","Service Mode: Register or Paged Mode Response: CV:1 Value:4",r.toMonitorString());
    }

    // check is paged mode response
    @Test
    public void testIsPagedModeResponse() {
        // CV 1 in direct mode.
        XNetReply r = new XNetReply("63 14 01 04 72");
        Assert.assertFalse(r.isPagedModeResponse());
        // CV 1 in paged mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertTrue(r.isPagedModeResponse());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertTrue(r.isPagedModeResponse());
        // CV 286 in direct mode.
        r = new XNetReply("63 15 1E 14 7C");
        Assert.assertFalse(r.isPagedModeResponse());
    }

    // check is direct mode response
    @Test
    public void testIsDirectModeResponse() {
        // CV 1 in direct mode.
        XNetReply r = new XNetReply("63 14 01 04 72");
        Assert.assertTrue(r.isDirectModeResponse());
        // CV 257 in direct mode.
        r = new XNetReply("63 15 01 04 72");
        Assert.assertTrue(r.isDirectModeResponse());
        // CV 513 in direct mode.
        r = new XNetReply("63 16 01 04 72");
        Assert.assertTrue(r.isDirectModeResponse());
        // CV 769 in direct mode.
        r = new XNetReply("63 17 01 04 72");
        Assert.assertTrue(r.isDirectModeResponse());
        // CV 1 in paged mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isDirectModeResponse());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isDirectModeResponse());
        // CV 286 in direct mode.
        r = new XNetReply("63 15 1E 14 7C");
        Assert.assertTrue(r.isDirectModeResponse());
    }

    // check get service mode CV Number response code.
    @Test
    public void testGetServiceModeCVNumber() {
        // CV 1 in direct mode.
        XNetReply r = new XNetReply("63 14 01 04 72");
        Assert.assertEquals("Direct Mode CV<256", 1, r.getServiceModeCVNumber());
        // CV 1 in paged mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Paged Mode CV<256", 1, r.getServiceModeCVNumber());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Register Mode CV<256", 1, r.getServiceModeCVNumber());
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 286 in direct mode.
        r = new XNetReply("63 15 1E 14 7C");
        Assert.assertEquals("Direct Mode CV>256", 286, r.getServiceModeCVNumber());
        // not a service mode response.
        r = new XNetReply("01 04 05");
        Assert.assertEquals("non-ServiceMode message", -1, r.getServiceModeCVNumber());
    }

    // check get service mode CV Value response code.
    @Test
    public void testGetServiceModeCVValue() {
        // CV 1 in direct mode.
        XNetReply r = new XNetReply("63 14 01 04 72");
        Assert.assertEquals("Direct Mode CV<256", 4, r.getServiceModeCVValue());
        // CV 1 in paged mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Paged Mode CV<256", 4, r.getServiceModeCVValue());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Register Mode CV<256", 4, r.getServiceModeCVValue());
        Assert.assertTrue(r.isServiceModeResponse());
        // CV 286 in direct mode.
        r = new XNetReply("63 15 1E 14 7C");
        Assert.assertEquals("Direct Mode CV>256", 20, r.getServiceModeCVValue());
        // not a service mode response.
        r = new XNetReply("01 04 05");
        Assert.assertEquals("Non Service Mode Response", -1, r.getServiceModeCVValue());
    }

    // From feedback Messages
    // check is this a feedback response
    @Test
    public void testIsFeedbackResponse() {
        // feedback message for turnout
        XNetReply r = new XNetReply("42 05 48 0f");
        Assert.assertTrue(r.isFeedbackMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isFeedbackMessage());
    }

    // check is this a broadcast feedback response
    @Test
    public void testIsFeedbackBroadcastResponse() {
        // feedback message for turnout
        XNetReply r = new XNetReply("42 05 48 0f");
        Assert.assertTrue(r.isFeedbackBroadcastMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isFeedbackBroadcastMessage());
    }

    // getting the address from a feedback response
    @Test
    public void testGetTurnoutMsgAddr() {
        // feedback message for turnout 21
        XNetReply r = new XNetReply("42 05 01 63");
        Assert.assertEquals("Broadcast Turnout Message Address", 21, r.getTurnoutMsgAddr(1));

        // feedback message for turnout 22, which returns address 21 
        // (addresses are in pairs).
        r = new XNetReply("42 05 04 43");
        Assert.assertEquals("Turnout Message Address", 21, r.getTurnoutMsgAddr());
        // turnout 22 with feedback 
        r = new XNetReply("42 05 24 63");
        Assert.assertEquals("Turnout Message Address", 21, r.getTurnoutMsgAddr() );
        // turnout 21 with feedback 
        r = new XNetReply("42 05 22 65");
        Assert.assertEquals("Turnout Message Address", 21, r.getTurnoutMsgAddr() );

        // feedback message for turnout 21 or 22,but neither 21 or 22 operated.
        r = new XNetReply("42 05 00 47");
        Assert.assertEquals("Turnout Message Address", 21, r.getTurnoutMsgAddr());
        // feedback message for turnout 24, returns 23.
        r = new XNetReply("42 05 14 53");
        Assert.assertEquals("Turnout Message Address", 23, r.getTurnoutMsgAddr());
        // feedback message for turnout 23
        r = new XNetReply("42 05 11 53");
        Assert.assertEquals("Turnout Message Address", 23, r.getTurnoutMsgAddr());
        // feedback message for turnout 23 or 24,but neither 23 or 24 operated.
        r = new XNetReply("42 05 10 53");
        Assert.assertEquals("Turnout Message Address", 23, r.getTurnoutMsgAddr());

        // feedback message for turnout 24, returns address 23.
        r = new XNetReply("42 05 18 53");
        Assert.assertEquals("Turnout Message Address", 23, r.getTurnoutMsgAddr());

        // feedback message for a feedback encoder, should return -1.
        r = new XNetReply("42 05 48 0F");
        Assert.assertEquals("Turnout Message Address for Feedback encoder", -1, r.getTurnoutMsgAddr() );

        // feedback message for a non-feedback message, should return -1.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Turnout Message Address for Feedback Other message", -1, r.getTurnoutMsgAddr() );

    }

    // getting the address from a broadcast feedback response
    @Test
    public void testGetBroadcastTurnoutMsgAddr() {
        // feedback for turnout 21  
        XNetReply r = new XNetReply("42 05 01 63");
        Assert.assertEquals("Broadcast Turnout Message Address", 21, r.getTurnoutMsgAddr(1));

        // feedback message for turnout 22, which returns address 21 
        // (addresses are in pairs).
        r = new XNetReply("42 05 04 43");
        Assert.assertEquals("Broadcast Turnout Message Address", 21, r.getTurnoutMsgAddr(1));

        // turnout 22 with feedback 
        r = new XNetReply("42 05 24 63");
        Assert.assertEquals("Broadcast Turnout Message Address", 21, r.getTurnoutMsgAddr(1));

        // feedback message for turnout 21 or 22, but neither 21 or 22 operated.
        r = new XNetReply("42 05 00 47");
        Assert.assertEquals("Broadcast Turnout Message Address", 21, r.getTurnoutMsgAddr(1));

        // feedback message for turnout 23
        r = new XNetReply("42 05 14 53");
        Assert.assertEquals("Broadcast Turnout Message Address", 23, r.getTurnoutMsgAddr(1));

        // feedback message for turnout 24, returns address 23.
        r = new XNetReply("42 05 18 53");
        Assert.assertEquals("Broadcast Turnout Message Address", 23, r.getTurnoutMsgAddr(1));

        // feedback message for turnout 23
        r = new XNetReply("42 05 11 53");
        Assert.assertEquals("Turnout Message Address", 23, r.getTurnoutMsgAddr(1));

        // feedback message for turnout 23 or 24, but neither 23 or 24 operated.
        r = new XNetReply("42 05 10 53");
        Assert.assertEquals("Broadcast Turnout Message Address", 23, r.getTurnoutMsgAddr(1));

        // feedback message for a feedback encoder, should return -1.
        r = new XNetReply("42 05 48 0F");
        Assert.assertEquals("Broadcast Turnout Message Address for Feedback encoder", -1, r.getTurnoutMsgAddr(1) );

        // feedback message for a non-feedback message, should return -1.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Broadcast Turnout Message Address for Feedback Other message", -1, r.getTurnoutMsgAddr(1) );
    }

    // getting the feedback message type (turnout without feedback, 
    // turnout with feedback, or sensor)
    @Test
    public void testGetFeedbackMessageType() {
        // feedback message for turnout
        XNetReply r = new XNetReply("42 05 04 43");
        Assert.assertEquals("Feedback Message Type", 0, r.getFeedbackMessageType() );
        r = new XNetReply("42 05 24 63");
        Assert.assertEquals("Feedback Message Type", 1, r.getFeedbackMessageType() );
        r = new XNetReply("42 05 48 0F");
        Assert.assertEquals("Feedback Message Type", 2, r.getFeedbackMessageType() );
        r = new XNetReply("63 10 01 04 76"); // not a feedback message.
        Assert.assertEquals("Feedback Message Type", -1, r.getFeedbackMessageType() );
    }

    // getting the feedback message type (turnout without feedback, 
    // turnout with feedback, or sensor) from a broadcast feedback message.
    @Test
    public void testGetBroadcastFeedbackMessageType() {
        // feedback message for turnout
        XNetReply r = new XNetReply("42 05 04 43");
        Assert.assertEquals("Broadcast Feedback Message Type", 0, r.getFeedbackMessageType(1) );
        r = new XNetReply("42 05 24 63");
        Assert.assertEquals("Broadcast Feedback Message Type", 1, r.getFeedbackMessageType(1) );
        r = new XNetReply("42 05 48 0F");
        Assert.assertEquals("Broadcast Feedback Message Type", 2, r.getFeedbackMessageType(1) );
        r = new XNetReply("63 10 01 04 76"); // not a feedback message.
        Assert.assertEquals("Broadcast Feedback Message Type", -1, r.getFeedbackMessageType(1) );
    }

    // getting the status from a turnout feedback response
    @Test
    public void testGetTurnoutMessageStatus() {
        // feedback message for turnout 22, closed
        XNetReply r = new XNetReply("42 05 04 43");
        Assert.assertEquals("Turnout Status", jmri.Turnout.CLOSED, r.getTurnoutStatus(0));
        // feedback message for turnout 22, thrown
        r = new XNetReply("42 05 08 4F");
        Assert.assertEquals("Turnout Status", jmri.Turnout.THROWN, r.getTurnoutStatus(0));

        // ask for address 21
        Assert.assertEquals("Turnout Status", -1, r.getTurnoutStatus(1));
        // feedback message for turnout 22, with invalid state.
        r = new XNetReply("42 05 0C 45");
        Assert.assertEquals("Turnout Status", -1 , r.getTurnoutStatus(0));

        // feedback message for turnout 21, closed
        r = new XNetReply("42 05 01 46");
        Assert.assertEquals("Turnout Status", jmri.Turnout.CLOSED, r.getTurnoutStatus(1));
        // feedback message for turnout 21, thrown
        r = new XNetReply("42 05 02 45");
        Assert.assertEquals("Turnout Status", jmri.Turnout.THROWN, r.getTurnoutStatus(1));
        // ask for address 22.
        Assert.assertEquals("Turnout Status", -1, r.getTurnoutStatus(0));
        // send invalid value for parameter (only 0 and 1 are valid).
        Assert.assertEquals("Turnout Status", -1, r.getTurnoutStatus(3));
        // feedback message for turnout 21, with invalid state.
        r = new XNetReply("42 05 03 47");
        Assert.assertEquals("Turnout Status", -1 , r.getTurnoutStatus(1));
        // turnout status for a feedback message.
        r = new XNetReply("42 05 48 0F");
        Assert.assertEquals("Feedback Message Turnout Status", -1, r.getTurnoutStatus(1) );
        // feedback message for a non-feedback message, should return -1.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Turnout Message Status for a non-FeedbackMessage", -1, r.getTurnoutStatus(1) );
    }

    // getting the status from a turnout broadcast feedback response
    @Test
    public void testGetBroadcastTurnoutMessageStatus() {
        // feedback message for turnout 22, closed
        XNetReply r = new XNetReply("42 05 04 43");
        Assert.assertEquals("Broadcast Turnout Status", jmri.Turnout.CLOSED, r.getTurnoutStatus(1,0));
        // feedback message for turnout 22, thrown
        r = new XNetReply("42 05 08 4F");
        Assert.assertEquals("Broadcast Turnout Status", jmri.Turnout.THROWN, r.getTurnoutStatus(1,0));

        // ask for address 21
        Assert.assertEquals("Broadcast Turnout Status", -1, r.getTurnoutStatus(1, 1));
        // feedback message for turnout 22, with invalid state.
        r = new XNetReply("42 05 0C 45");
        Assert.assertEquals("Broadcast Turnout Status", -1 , r.getTurnoutStatus(1,0));
        // feedback message for turnout 21, closed
        r = new XNetReply("42 05 01 46");
        Assert.assertEquals("Broadcast Turnout Status", jmri.Turnout.CLOSED, r.getTurnoutStatus(1,1));
        // feedback message for turnout 21, thrown
        r = new XNetReply("42 05 02 45");
        Assert.assertEquals("Broadcast Turnout Status", jmri.Turnout.THROWN, r.getTurnoutStatus(1,1));
        // ask for address 22.
        Assert.assertEquals("Broadcast Turnout Status", -1, r.getTurnoutStatus(1, 0));
        // send invalid value for parameter (only 0 and 1 are valid).
        Assert.assertEquals("Broadcast Turnout Status", -1, r.getTurnoutStatus(1, 3));
        // feedback message for turnout 21, with invalid state.
        r = new XNetReply("42 05 03 47");
        Assert.assertEquals("Broadcast Turnout Status", -1 , r.getTurnoutStatus(1,1));
        // turnout status for a feedback message.
        r = new XNetReply("42 05 48 0F");
        Assert.assertEquals("Broadcast Feedback Message Turnout Status", -1, r.getTurnoutStatus(1,1) );
        // feedback message for a non-feedback message, should return -1.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertEquals("Broadcast Turnout Message Status for a non-FeedbackMessage", -1, r.getTurnoutStatus(1,1) );
    }

    // getting the address from a feedback encoder response
    @Test
    public void testGetEncoderMsgAddr() {
        // feedback message for sensor
        XNetReply r = new XNetReply("42 05 48 0f");
        Assert.assertEquals("Feedback Encoder Message Address", 5, r.getFeedbackEncoderMsgAddr());
        // turnout
        r = new XNetReply("42 05 08 4F");
        Assert.assertEquals("Feedback Encoder Message Address", -1, r.getFeedbackEncoderMsgAddr());
        r = new XNetReply("63 10 01 04 76"); // not a feedback message.
        Assert.assertEquals("Feedback Encoder Message Address", -1, r.getFeedbackEncoderMsgAddr());
    }

    // getting the address from a broadcast feedback encoder response
    @Test
    public void testGetBroadcastEncoderMsgAddr() {
        // feedback message for turnout
        XNetReply r = new XNetReply("42 05 48 0f");
        Assert.assertEquals("Broadcast Feedback Encoder Message Address", 5, r.getFeedbackEncoderMsgAddr(1));
        // turnout
        r = new XNetReply("42 05 08 4F");
        Assert.assertEquals("Feedback Encoder Message Address", -1, r.getFeedbackEncoderMsgAddr(1));
        r = new XNetReply("63 10 01 04 76"); // not a feedback message.
        Assert.assertEquals("Feedback Encoder Message Address", -1, r.getFeedbackEncoderMsgAddr(1));
    }

    @Test
    public void testToMonitorStringFeedbackResponse() {
        // feedback message for turnout
        XNetReply r = new XNetReply("42 05 00 47");
        String targetString =
             "Feedback Response: Turnout without Feedback Turnout: 21 State: Not Operated; Turnout: 22 State: Not Operated";
        Assert.assertEquals("Monitor String",targetString, r.toMonitorString());
        r = new XNetReply("42 05 05 42");
        targetString =
             "Feedback Response: Turnout without Feedback Turnout: 21 State: Thrown Left; Turnout: 22 State: Thrown Left";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 0A 4C");
        targetString =
             "Feedback Response: Turnout without Feedback Turnout: 21 State: Thrown Right; Turnout: 22 State: Thrown Right";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 0F 48");
        targetString =
             "Feedback Response: Turnout without Feedback Turnout: 21 State: <invalid>; Turnout: 22 State: <invalid>";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 20 67");
        targetString =
              "Feedback Response: Turnout with Feedback Turnout: 21 State: Not Operated Motion Complete; Turnout: 22 State: Not Operated Motion Complete";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 25 62");
        targetString = "Feedback Response: Turnout with Feedback Turnout: 21 State: Thrown Left Motion Complete; Turnout: 22 State: Thrown Left Motion Complete";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 2A 6C");
        targetString =
              "Feedback Response: Turnout with Feedback Turnout: 21 State: Thrown Right Motion Complete; Turnout: 22 State: Thrown Right Motion Complete";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 2F 68");
        targetString =
              "Feedback Response: Turnout with Feedback Turnout: 21 State: <invalid> Motion Complete; Turnout: 22 State: <invalid> Motion Complete";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 48 0F");
        targetString =
              "Feedback Response: Feedback Encoder Base Address: 5 Contact: 1 State: Off; Contact: 2 State: Off; Contact: 3 State: Off; Contact: 4 State: On; ";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("42 05 57 0F");
        targetString =
              "Feedback Response: Feedback Encoder Base Address: 5 Contact: 5 State: On; Contact: 6 State: On; Contact: 7 State: On; Contact: 8 State: Off; ";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    // throttle related replies.
    @Test
    public void testGetThrottleMsgAddr() {
        // locomotive taken over by another device reply
        // short address
        XNetReply r = new XNetReply("E3 40 00 04 57");
        Assert.assertEquals("Throttle Message Address", 4, r.getThrottleMsgAddr());
        // long address
        r = new XNetReply("E3 40 C1 04 61");
        Assert.assertEquals("Throttle Message Address", 260, r.getThrottleMsgAddr());
        // not a throttle message.
        r = new XNetReply("42 05 48 0f");
        Assert.assertEquals("Throttle Message Address", -1, r.getThrottleMsgAddr());
    }

    // check is this a throttle response
    @Test
    public void testIsThrottleMessage() {
        // MUED Locomotive Address
        XNetReply r= new XNetReply("E2 24 04 C2");
        Assert.assertTrue(r.isThrottleMessage());
        // Locomotive Taken Over by another device
        r = new XNetReply("E3 40 C1 04 61");
        Assert.assertTrue(r.isThrottleMessage());
        // Normal Locomotive Information reply
        r= new XNetReply("E4 04 00 04 00 E4");
        Assert.assertTrue(r.isThrottleMessage());
        // MUED Locomotive Information reply
        r = new XNetReply("E5 14 C1 04 00 00 34");
        Assert.assertTrue(r.isThrottleMessage());
        // DH Address
        r = new XNetReply("E6 64 00 64 C1 C1 04 E2");
        Assert.assertTrue(r.isThrottleMessage());
        // XNet V1, locomotive available for operation
        r = new XNetReply("83 01 00 00 82");
        Assert.assertTrue(r.isThrottleMessage());
        // XNet V1, locomotive not available for operation
        r = new XNetReply("A3 01 00 00 A2");
        Assert.assertTrue(r.isThrottleMessage());
        // XNet V2, locomotive available for operation
        r = new XNetReply("84 01 00 00 00 85");
        Assert.assertTrue(r.isThrottleMessage());
        // XNet V2, locomotive not available for operation
        r = new XNetReply("A4 01 00 00 00 A5");
        Assert.assertTrue(r.isThrottleMessage());


        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isThrottleMessage());
    }

    @Test
    public void testToMonitorStringThrottleTakeoverMsg() {
        XNetReply r = new XNetReply("E3 40 00 04 57");
        String targetString = "Locomotive Information Response: Locomotive 4 is being operated by another device.";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("E3 40 C1 04 61");
        targetString = "Locomotive Information Response: Locomotive 260 is being operated by another device.";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    @Test
    public void testToMonitorStringNormalLocoInfoResponse() {
        XNetReply r= new XNetReply("E4 04 00 04 00 E4");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 128 Speed Step Mode,Speed Step: 0. Address is Free for Operation. F0 Off; F1 Off; F2 Off; F3 On; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
        r= new XNetReply("E4 04 04 04 00 E0");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 128 Speed Step Mode,Speed Step: 3. Address is Free for Operation. F0 Off; F1 Off; F2 Off; F3 On; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
        r= new XNetReply("E4 0A 00 04 00 EA");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 28 Speed Step Mode,Speed Step: 0. Address in use by another device. F0 Off; F1 Off; F2 Off; F3 On; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
        r= new XNetReply("E4 0A 0A 04 00 E0");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 28 Speed Step Mode,Speed Step: 17. Address in use by another device. F0 Off; F1 Off; F2 Off; F3 On; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
        r= new XNetReply("E4 01 00 1F FF F5");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 27 Speed Step Mode,Speed Step: 0. Address is Free for Operation. F0 On; F1 On; F2 On; F3 On; F4 On; F5 On; F6 On; F7 On; F8 On; F9 On; F10 On; F11 On; F12 On; ",r.toMonitorString());
        r= new XNetReply("E4 01 05 1F FF F0");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 27 Speed Step Mode,Speed Step: 7. Address is Free for Operation. F0 On; F1 On; F2 On; F3 On; F4 On; F5 On; F6 On; F7 On; F8 On; F9 On; F10 On; F11 On; F12 On; ",r.toMonitorString());
        r= new XNetReply("E4 00 00 00 00 E4");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 14 Speed Step Mode,Speed Step: 0. Address is Free for Operation. F0 Off; F1 Off; F2 Off; F3 Off; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
        r= new XNetReply("E4 00 04 00 00 E0");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Normal Unit,Reverse,in 14 Speed Step Mode,Speed Step: 3. Address is Free for Operation. F0 Off; F1 Off; F2 Off; F3 Off; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringMULocoInfoResponse() {
        XNetReply r = new XNetReply("E5 14 C1 04 00 00 34");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive in Multiple Unit,Forward,in 128 Speed Step Mode,Speed Step: 64. Address is Free for Operation.F0 Off; F1 Off; F2 Off; F3 On; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringMUEliteLocoInfoResponse() {
        XNetReply r = new XNetReply("E5 F8 C1 04 00 00 34");
        Assert.assertEquals("Monitor String","Elite Speed/Direction Information: Locomotive 260,Reverse,in 14 Speed Step Mode,Speed Step: 0. Address is Free for Operation. ",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringEliteLocoFnInfoResponse() {
        XNetReply r = new XNetReply("E5 F9 C1 04 00 00 34");
        Assert.assertEquals("Monitor String","Elite Function Information: Locomotive 260 F0 Off; F1 Off; F2 Off; F3 Off; F4 Off; F5 Off; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 Off; F12 Off; ",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringMUBaseAddressInfoResponse() {
        XNetReply r = new XNetReply("E2 14 C1 37");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Multi Unit Base Address,Forward,in 128 Speed Step Mode,Speed Step: 64. Address is Free for Operation. ",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHLocoInfoResponse() {
        XNetReply r = new XNetReply("E6 64 00 64 C1 C1 04 E2");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive in Double Header,Reverse,in 128 Speed Step Mode,Speed Step: 0. Address is Free for Operation. F0 Off; F1 Off; F2 Off; F3 On; F4 Off; F5 On; F6 Off; F7 Off; F8 Off; F9 Off; F10 Off; F11 On; F12 On;  Second Locomotive in Double Header is: 260",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringV1LocoAvailable() {
        XNetReply r = new XNetReply("83 01 00 00 82");
        // this isn't actually translated to text, since we don't expect
        // to see a version 1 XBus system
        Assert.assertEquals("Monitor String","83 01 00 00 82",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringV1LocoNotAvailable() {
        XNetReply r = new XNetReply("A3 01 00 00 A2");
        // this isn't actually translated to text, since we don't expect
        // to see a version 1 XBus system
        Assert.assertEquals("Monitor String","A3 01 00 00 A2",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringV2LocoAvailable() {
        XNetReply r = new XNetReply("84 01 00 00 00 85");
        // this isn't actually translated to text, since we don't expect
        // to see a version 2 XBus system
        Assert.assertEquals("Monitor String","84 01 00 00 00 85",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringV2LocoNotAvailable() {
        XNetReply r = new XNetReply("A4 01 00 00 00 A5");
        // this isn't actually translated to text, since we don't expect
        // to see a version 2 XBus system
        Assert.assertEquals("Monitor String","A4 01 00 00 00 A5",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringNormalLocoFunctionMomentaryResponse() {
        XNetReply r= new XNetReply("E3 50 54 04 E3");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive Function Status: F0 Momentary; F1 Continuous; F2 Continuous; F3 Momentary; F4 Continuous; F5 Continuous; F6 Continuous; F7 Momentary; F8 Continuous; F9 Continuous; F10 Continuous; F11 Continuous; F12 Continuous; ",r.toMonitorString());
        r= new XNetReply("E3 50 00 00 93");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive Function Status: F0 Continuous; F1 Continuous; F2 Continuous; F3 Continuous; F4 Continuous; F5 Continuous; F6 Continuous; F7 Continuous; F8 Continuous; F9 Continuous; F10 Continuous; F11 Continuous; F12 Continuous; ",r.toMonitorString());
        r= new XNetReply("E3 50 5F FF 13");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive Function Status: F0 Momentary; F1 Momentary; F2 Momentary; F3 Momentary; F4 Momentary; F5 Momentary; F6 Momentary; F7 Momentary; F8 Momentary; F9 Momentary; F10 Momentary; F11 Momentary; F12 Momentary; ",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringNormalLocoFunctionHighMomentaryResponse() {
        XNetReply r= new XNetReply("E4 51 00 54 04 E5");
        Assert.assertEquals("Monitor String","Locomotive F13-F28 Momentary Status: F13 Continuous; F14 Continuous; F15 Continuous; F16 Continuous; F17 Continuous; F18 Continuous; F19 Continuous; F20 Continuous; F21 Continuous; F22 Continuous; F23 Momentary; F24 Continuous; F25 Momentary; F26 Continuous; F27 Momentary; F28 Continuous; ",r.toMonitorString());
        r= new XNetReply("E4 51 00 00 00 95");
        Assert.assertEquals("Monitor String","Locomotive F13-F28 Momentary Status: F13 Continuous; F14 Continuous; F15 Continuous; F16 Continuous; F17 Continuous; F18 Continuous; F19 Continuous; F20 Continuous; F21 Continuous; F22 Continuous; F23 Continuous; F24 Continuous; F25 Continuous; F26 Continuous; F27 Continuous; F28 Continuous; ",r.toMonitorString());
        r= new XNetReply("E4 51 FF FF FF 45");
        Assert.assertEquals("Monitor String","Locomotive F13-F28 Momentary Status: F13 Momentary; F14 Momentary; F15 Momentary; F16 Momentary; F17 Momentary; F18 Momentary; F19 Momentary; F20 Momentary; F21 Momentary; F22 Momentary; F23 Momentary; F24 Momentary; F25 Momentary; F26 Momentary; F27 Momentary; F28 Momentary; ",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringNormalLocoFunctionHighResponse() {
        XNetReply r= new XNetReply("E3 52 54 04 E3");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive F13-F28 Status: F13 Off; F14 Off; F15 On; F16 Off; F17 On; F18 Off; F19 On; F20 Off; F21 Off; F22 Off; F23 On; F24 Off; F25 Off; F26 Off; F27 Off; F28 Off; ",r.toMonitorString());
        r= new XNetReply("E3 52 00 00 91");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive F13-F28 Status: F13 Off; F14 Off; F15 Off; F16 Off; F17 Off; F18 Off; F19 Off; F20 Off; F21 Off; F22 Off; F23 Off; F24 Off; F25 Off; F26 Off; F27 Off; F28 Off; ",r.toMonitorString());
        r= new XNetReply("E3 52 FF FF 91");
        Assert.assertEquals("Monitor String","Locomotive Information Response: Locomotive F13-F28 Status: F13 On; F14 On; F15 On; F16 On; F17 On; F18 On; F19 On; F20 On; F21 On; F22 On; F23 On; F24 On; F25 On; F26 On; F27 On; F28 On; ",r.toMonitorString());
    }


   // check if this is a throttle takeover response
    @Test
    public void testIsThrottleTakenOverMessage() {
        // Normal Locomotive Information reply
        XNetReply r = new XNetReply("E3 40 C1 04 61");
        Assert.assertTrue(r.isThrottleTakenOverMessage());
        // Function reply
        r = new XNetReply("E3 08 00 00 E6");
        Assert.assertFalse(r.isThrottleTakenOverMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isThrottleTakenOverMessage());
    }

   // check is this a consist response
    @Test
    public void testIsConsistMessage() {
        // MU/DH  Error
        XNetReply r = new XNetReply("E1 81 60");
        Assert.assertTrue(r.isConsistMessage());
        // DH Info (XNetV1)
        r = new XNetReply("C5 04 00 00 00 00 C1");
        Assert.assertTrue(r.isConsistMessage());
        // DH Info (XNetV2)
        r = new XNetReply("C6 04 00 00 00 00 00 C2");
        Assert.assertTrue(r.isConsistMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isConsistMessage());
    }


   // some common messages.
   // check is this an OK message
    @Test
    public void testIsOkMessage() {
        // "OK" message
        XNetReply r = new XNetReply("01 04 05");
        Assert.assertTrue(r.isOkMessage());
        // Error message
        r = new XNetReply("01 01 00");
        Assert.assertFalse(r.isOkMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isOkMessage());
    }

    @Test
    public void testToMonitorStringOKMessage(){
        XNetReply r = new XNetReply("01 04 05");
        Assert.assertEquals("Monitor String", "Command Successfully Sent/Normal Operations Resumed after timeout",r.toMonitorString());
    }

   // check is this an Timeslot Restored message
    @Test
    public void testIsTimeSlotRestoredMessage() {
        // Timeslot restored message
        XNetReply r = new XNetReply("01 07 06");
        Assert.assertTrue(r.isTimeSlotRestored());
        // Error message
        r = new XNetReply("01 01 00");
        Assert.assertFalse(r.isTimeSlotRestored());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isTimeSlotRestored());
    }

   // check is this an Timeslot Revoked message
    @Test
    public void testIsTimeSlotRevokedMessage() {
        // Timeslot restored message
        XNetReply r = new XNetReply("01 05 04");
        Assert.assertTrue(r.isTimeSlotRevoked());
        // Error message
        r = new XNetReply("01 01 00");
        Assert.assertFalse(r.isTimeSlotRevoked());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isTimeSlotRevoked());
    }

    // check is this a CS Busy message
    @Test
    public void testIsCSBusyMessage() {
        // CS Busy Message
        XNetReply r = new XNetReply("61 81 e0");
        Assert.assertTrue(r.isCSBusyMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isCSBusyMessage());
    }

    @Test
    public void testToMonitorStringCSBusyMessage(){
        XNetReply r = new XNetReply("61 81 e0");
        Assert.assertEquals("Monitor String", "Command Station Busy",r.toMonitorString());
    }

    // check is this a CS transfer error message
    @Test
    public void testIsCSTransferError() {
        // Command Station Transfer Error Message
        XNetReply r = new XNetReply("61 80 e1");
        Assert.assertTrue(r.isCSTransferError());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isCSTransferError());
    }

    @Test
    public void testToMonitorStringCSTransferError(){
        XNetReply r = new XNetReply("61 80 e1");
        Assert.assertEquals("Monitor String", "Command Station Reported Transfer Error",r.toMonitorString());
    }


   // check is this a Communication Error  message
    @Test
    public void testIsCommErrorMessage() {
        // Error between interface and the PC
        XNetReply r = new XNetReply("01 01 00");
        Assert.assertTrue(r.isCommErrorMessage());
        // Error between interface and the Command Station
        r = new XNetReply("01 02 03");
        Assert.assertTrue(r.isCommErrorMessage());
        // Unkonwn Communication Error
        r = new XNetReply("01 03 02");
        Assert.assertTrue(r.isCommErrorMessage());
        // LI10x Buffer Overflow
        r = new XNetReply("01 06 07");
        Assert.assertTrue(r.isCommErrorMessage());
        // LIUSB request resend of data.
        r = new XNetReply("01 0A 0B");
        Assert.assertTrue(r.isCommErrorMessage());
        // Timeslot Error
        r = new XNetReply("01 05 04");
        Assert.assertTrue(r.isCommErrorMessage());
        // Timeslot Restored
        r = new XNetReply("01 07 06");
        Assert.assertTrue(r.isCommErrorMessage());
        // Data sent while there is no Timeslot
        r = new XNetReply("01 08 09");
        Assert.assertTrue(r.isCommErrorMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isCommErrorMessage());
    }

    @Test
    public void testToMonitorStringErrorPCtoLI(){
        XNetReply r = new XNetReply("01 01 00");
        Assert.assertEquals("Monitor String", "Error occurred between the interface and the PC",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringErrorLItoCS(){
        XNetReply r = new XNetReply("01 02 03");
        Assert.assertEquals("Monitor String", "Error occurred between the interface and the command station.",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringErrorUnknown(){
        XNetReply r = new XNetReply("01 03 02");
        Assert.assertEquals("Monitor String", "Unknown Communication Error",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringErrorNoTimeslot(){
        XNetReply r = new XNetReply("01 05 04");
        Assert.assertEquals("Monitor String", "The Command Station is no longer providing the LI a timeslot for communication",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringErrorBufferOverflow(){
        XNetReply r = new XNetReply("01 06 07");
        Assert.assertEquals("Monitor String", "Buffer overflow in the LI",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringTimeSlotRestored(){
        XNetReply r = new XNetReply("01 07 06");
        Assert.assertEquals("Monitor String", "Timeslot Restored",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDataSentNoTimeslot(){
        XNetReply r = new XNetReply("01 08 09");
        Assert.assertEquals("Monitor String", "Request Sent while the CS is not providing a Timeslot",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringErrorBadData(){
        XNetReply r = new XNetReply("01 09 08");
        Assert.assertEquals("Monitor String", "Bad Data in Request sent to CS",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringRetransmissionRequested(){
        XNetReply r = new XNetReply("01 0A 0B");
        Assert.assertEquals("Monitor String", "Retransmission Requested",r.toMonitorString());
    }

    // check is this a Timeslot message  message
    @Test
    public void testIsTimeSlotErrorMessage() {
        // Timeslot Error
        XNetReply r = new XNetReply("01 05 04");
        Assert.assertTrue(r.isTimeSlotErrorMessage());
        // Timeslot Restored
        r = new XNetReply("01 07 06");
        Assert.assertTrue(r.isTimeSlotErrorMessage());
        // Data sent while there is no Timeslot
        r = new XNetReply("01 08 09");
        Assert.assertTrue(r.isTimeSlotErrorMessage());
        // CV 1 in register mode.
        r = new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isTimeSlotErrorMessage());
    }

    // check if this message is a retransmittable error message.
    @Test
    public void testIsRetransmittableErrorMsg(){
       XNetReply r = new XNetReply("61 81 e3"); // CS Busy Message
       Assert.assertTrue(r.isRetransmittableErrorMsg());
       r = new XNetReply("61 80 e1"); // transfer error
       Assert.assertTrue(r.isRetransmittableErrorMsg());
       r = new XNetReply("01 06 07"); // Buffer overflow (Comm Error)
       Assert.assertTrue(r.isRetransmittableErrorMsg());
       r = new XNetReply("01 04 05"); // OK message
       Assert.assertFalse(r.isRetransmittableErrorMsg());
    }

    // check if this is an unsolicited message
    @Test
    public void testIsUnsolicitedMessage() {
        // CV 1 in register mode.
        XNetReply r= new XNetReply("63 10 01 04 76");
        Assert.assertFalse(r.isUnsolicited());
        r.setUnsolicited();
        Assert.assertTrue(r.isUnsolicited());
        // Throttle taken over message
        r = new XNetReply("E3 40 C1 04 61");
        Assert.assertTrue(r.isUnsolicited());
        // feedback message.
        r = new XNetReply("42 05 48 0f");
        Assert.assertFalse(r.isUnsolicited());
    }

    // check toMonitor string for informational messages from the command station
    // not covered elsewhere.
    @Test
    public void testToMonitorStringBCEmergencyOff(){
        XNetReply r = new XNetReply("61 00 61");
        Assert.assertEquals("Monitor String","Broadcast: Emergency Off (short circuit)",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringBCNormalOpers(){
        XNetReply r = new XNetReply("61 01 60");
        Assert.assertEquals("Monitor String","Broadcast: Normal Operations Resumed",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringBCServiceModeEntry(){
        XNetReply r = new XNetReply("61 02 63");
        Assert.assertEquals("Monitor String", "Broadcast: Service Mode Entry",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringServiceModeCSReady(){
        XNetReply r = new XNetReply("61 11 70");
        Assert.assertEquals("Monitor String","Service Mode: Command Station Ready",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringServiceModeShortCircuit(){
        XNetReply r = new XNetReply("61 12 73");
        Assert.assertEquals("Monitor String","Service Mode: Short Circuit",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringServiceModeByteNotFound(){
        XNetReply r = new XNetReply("61 13 72");
        Assert.assertEquals("Monitor String","Service Mode: Data Byte Not Found",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringServiceModeCSBusy(){
        XNetReply r = new XNetReply("61 1F 7E");
        Assert.assertEquals("Monitor String","Service Mode: Command Station Busy",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringCSNotSupported(){
        XNetReply r = new XNetReply("61 82 E3");
        Assert.assertEquals("Monitor String","XpressNet Instruction not supported by Command Station",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHV1_V2ErrorNotOperated(){
        XNetReply r = new XNetReply("61 83 E2");
        Assert.assertEquals("Monitor String","XBus V1 and V2 MU+DH error: Selected Locomotive has not been operated by this XpressNet device or address 0 selected",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHV1_V2ErrorInUse(){
        XNetReply r = new XNetReply("61 84 E5");
        Assert.assertEquals("Monitor String","XBus V1 and V2 MU+DH error: Selected Locomotive is being operated by another XpressNet device",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHV1_V2ErrorAlreadyDH(){
        XNetReply r = new XNetReply("61 85 E4");
        Assert.assertEquals("Monitor String","XBus V1 and V2 MU+DH error: Selected Locomotive already in MU or DH",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHV1_V2ErrorNonZeroSpeed(){
        XNetReply r = new XNetReply("61 86 E7");
        Assert.assertEquals("Monitor String","XBus V1 and V2 MU+DH error: Unit selected for MU or DH has speed setting other than 0",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringErrorNotOperated(){
        XNetReply r = new XNetReply("E1 81 60");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: Selected Locomotive has not been operated by this XpressNet device or address 0 selected",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHErrorInUse(){
        XNetReply r = new XNetReply("E1 82 63");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: Selected Locomotive is being operated by another XpressNet device",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHErrorAlreadyDH(){
        XNetReply r = new XNetReply("E1 83 62");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: Selected Locomotive already in MU or DH",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHErrorNonZeroSpeed(){
        XNetReply r = new XNetReply("E1 84 65");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: Unit selected for MU or DH has speed setting other than 0",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHErrorLocoNotMUed(){
        XNetReply r = new XNetReply("E1 85 64");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: Locomotive not in a MU",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHErrorAddressNotMUBase(){
        XNetReply r = new XNetReply("E1 86 67");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: Locomotive address not a multi-unit base address",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHErrorCanNotDelete(){
        XNetReply r = new XNetReply("E1 87 66");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: It is not possible to delete the locomotive",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringDHErrorCSStackFull(){
        XNetReply r = new XNetReply("E1 88 69");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: The Command Station Stack is Full",r.toMonitorString());
    }

   @Test
    public void testToMonitorStringDHErrorOther(){
        XNetReply r = new XNetReply("E1 89 69");
        Assert.assertEquals("Monitor String","XpressNet MU+DH error: 9",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringLIVersionReply(){
        XNetReply r = new XNetReply("02 01 36 34");
        Assert.assertEquals("Monitor String","LI10x Hardware Version: 0.1 Software Version: 3.6",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringLIAddressReply(){
        XNetReply r = new XNetReply("F2 01 01 F2");
        Assert.assertEquals("Monitor String","RESPONSE LI101 Address 1",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringLIBaud1Reply(){
        XNetReply r = new XNetReply("F2 02 01 F1");
        Assert.assertEquals("Monitor String","RESPONSE LI101 Baud Rate: 19,200 bps (default)",r.toMonitorString());
    }
    @Test
    public void testToMonitorStringLIBaud2Reply(){
        XNetReply r = new XNetReply("F2 02 02 F2");
        Assert.assertEquals("Monitor String","RESPONSE LI101 Baud Rate: 38,400 bps",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringLIBaud3Reply(){
        XNetReply r = new XNetReply("F2 02 03 F1");
        Assert.assertEquals("Monitor String","RESPONSE LI101 Baud Rate: 57,600 bps",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringLIBaud4Reply(){
        XNetReply r = new XNetReply("F2 02 04 F4");
        Assert.assertEquals("Monitor String","RESPONSE LI101 Baud Rate: 115,200 bps",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringLIBaud5Reply(){
        XNetReply r = new XNetReply("F2 02 05 F1");
        Assert.assertEquals("Monitor String","RESPONSE LI101 Baud Rate: <undefined>",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringCSStatusReply(){
        XNetReply r = new XNetReply("62 22 00 40");
        String targetString =  "Command Station Status: Manual power-up Mode; ";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
        r = new XNetReply("62 22 FF BF");
        targetString =  "Command Station Status: Emergency Off; Emergency Stop; Service Mode; Powering up; Auto power-up Mode; RAM check error!";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    @Test
    public void testToMonitorStringCSVersionReply(){
        jmri.util.IntlUtilities.valueOf(3.6);
        XNetReply r = new XNetReply("63 21 36 00 55");
        Assert.assertEquals("Monitor String","Command Station Software Version: 3.6 Type: LZ100/LZV100",r.toMonitorString());
        r = new XNetReply("63 21 36 01 55");
        Assert.assertEquals("Monitor String","Command Station Software Version: 3.6 Type: LH200",r.toMonitorString());
        r = new XNetReply("63 21 36 02 55");
        Assert.assertEquals("Monitor String","Command Station Software Version: 3.6 Type: Compact or Other",r.toMonitorString());
        r = new XNetReply("63 21 36 10 55");
        Assert.assertEquals("Monitor String","Command Station Software Version: 3.6 Type: multiMaus",r.toMonitorString());
        r = new XNetReply("63 21 36 20 55");
        Assert.assertEquals("Monitor String","Command Station Software Version: 3.6 Type: 32",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringCSV1VersionReply(){
        XNetReply r = new XNetReply("62 21 21 62");
        Assert.assertEquals("Monitor String","Command Station Software Version: 2.1 Type: Unknown (X-Bus V1 or V2)",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringBCEmeregncyStop(){
        XNetReply r = new XNetReply("81 00 81");
        Assert.assertEquals("Monitor String","Broadcast: Emergency Stop (track power on)",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringSearchResponseNormalLoco(){
        XNetReply r = new XNetReply("E3 30 C1 04 11");
        String targetString = "Locomotive Information Response: Search Response: Normal Locomotive: 260";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    @Test
    public void testToMonitorStringSearchResponseDoubleHeaderLoco(){
        XNetReply r = new XNetReply("E3 31 C1 04 17");
        String targetString = "Locomotive Information Response: Search Response: Loco in Double Header: 260";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    @Test
    public void testToMonitorStringSearchResponseMUBaseLoco(){
        XNetReply r = new XNetReply("E3 32 00 04 C5");
        String targetString = "Locomotive Information Response: Search Response: MU Base Address: 4";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    @Test
    public void testToMonitorStringSearchResponseMULoco(){
        XNetReply r = new XNetReply("E3 33 C1 04 15");
        String targetString = "Locomotive Information Response: Search Response: Loco in MU: 260";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    @Test
    public void testToMonitorStringSearchResponseFail(){
        XNetReply r = new XNetReply("E3 34 C1 04 15");
        String targetString = "Locomotive Information Response: Search Response: Search failed for: 260";
        Assert.assertEquals("Monitor String",targetString,r.toMonitorString());
    }

    // the following are invalid by the XpressNet Standard, but we want to
    // to make sure the code prints out the message contents.
    @Test
    public void testToMonitorStringInvalidLIMessage(){
        XNetReply r = new XNetReply("01 FF FE");
        Assert.assertEquals("Monitor String","01 FF FE",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringInvalidLI101Message(){
        XNetReply r = new XNetReply("F2 FF FF F2");
        Assert.assertEquals("Monitor String","F2 FF FF F2",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringInvalidCSInfoMessage(){
        XNetReply r = new XNetReply("61 FF 9E");
        Assert.assertEquals("Monitor String","61 FF 9E",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringInvalidCSEStopMessage(){
        XNetReply r = new XNetReply("81 FF 7E");
        Assert.assertEquals("Monitor String","81 FF 7E",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringInvalidServiceModeReply(){
        XNetReply r = new XNetReply("63 FF FF 00 63");
        Assert.assertEquals("Monitor String","63 FF FF 00 63",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringInvalidCSStatusReply(){
        XNetReply r = new XNetReply("62 FF FF 62");
        Assert.assertEquals("Monitor String","62 FF FF 62",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringInvalidLocoInfoReply(){
        XNetReply r = new XNetReply("E3 FF FF 00 E3");
        Assert.assertEquals("Monitor String","E3 FF FF 00 E3",r.toMonitorString());
    }

    @Test
    public void testToMonitorStringInvalidFeedbackReply(){
        XNetReply r = new XNetReply("42 FF FF 42");
        Assert.assertEquals("Monitor String","Feedback Response: 255 255",r.toMonitorString());
    }
    
    /**
     * Checks that the number of feedback items are correctly returned.
     */
    @Test
    public void testFeedbackItemsCount() {
        XNetReply r = new XNetReply("42 05 48 0f");
        assertEquals("Feedback has single item", 1, r.getFeedbackMessageItems());
        r = new XNetReply("46 05 48 06 48 07 48 0f");
        assertEquals("Feedback has 3 items", 3, r.getFeedbackMessageItems());
        r = new XNetReply("E3 32 00 04 C5");
        assertEquals(0, r.getFeedbackMessageItems());
    }
    
    /**
     * Checks that all information is consistent from the Feedback item, 
     * and are consistent with data served from XNetReply.
     * 
     * @param reply the original reply
     * @param startByte start byte where the Feedback item came from
     * @param address the expected address
     * @param aStatus the expected status
     * @param tType the expected feedback type
     * @param fItem the expected feedback address 
     */
    private void assertTurnoutFeedbackData(XNetReply reply, int startByte, int address, 
            int aStatus, int tType, FeedbackItem fItem) {
        
        // general accessory feedback constraints
        assertFalse("Must not be encoder", fItem.isEncoder());
        assertNull("Encoder functions disabled", fItem.getEncoderStatus(address));
        assertTrue("Must be accessory", fItem.isAccessory());
        
        // info consistent with the reply's original accessors
        assertEquals("Motion same as reply", reply.isFeedbackMotionComplete(startByte), fItem.isMotionComplete());
        if ((address & 0x01) == 1) {
            assertTrue("Accepts reply's odd address", fItem.matchesAddress(reply.getTurnoutMsgAddr(startByte)));
            assertEquals(address, reply.getTurnoutMsgAddr(startByte));
        } else {
            assertTrue("Accepts reply's even address", fItem.matchesAddress(reply.getTurnoutMsgAddr(startByte) + 1));
            assertEquals(address, reply.getTurnoutMsgAddr(startByte) + 1);
        }
        assertEquals("Solicited same as reply", reply.isUnsolicited(), fItem.isUnsolicited());
        
        assertEquals("Invalid feedback type", tType, fItem.getType());
        assertEquals("Raw accessory status", aStatus, fItem.getAccessoryStatus());

        int lowAddress = (address & 0x01) > 0 ? address - 1 : address - 2;
        int pairAddress = (address & 0x01) > 0 ? address + 1 : address - 1;
        int highAddress = (address & 0x01) > 0 ? address + 2 : address + 1;
        
        assertTrue("Must accept own address", fItem.matchesAddress(address));
        assertFalse("Must not accept other pair's address", fItem.matchesAddress(pairAddress));
        assertFalse("Must not accept other addresses", fItem.matchesAddress(lowAddress));
        assertFalse("Must not accept other addresses", fItem.matchesAddress(highAddress));

        int tStatus;
        switch (aStatus) {
            case 0x00: tStatus = -1; break; // not operated; shouldn't be UNKNOWN ?
            case 0x01: tStatus = Turnout.CLOSED; break;
            case 0x02: tStatus = Turnout.THROWN; break;
            case 0x03: tStatus = -1; break; // invalid; shouldn't be INCONSISTENT ?
            default:
                throw new IllegalArgumentException();
        }
        assertEquals("Turnout status", tStatus, fItem.getTurnoutStatus());
        
        // check the paired item:
        FeedbackItem paired = fItem.pairedAccessoryItem();
        assertNotNull("Accessory fedbacks are always in pairs", paired);
        assertFalse("Must not be encoder", paired.isEncoder());
        assertTrue("Must be accessory", paired.isAccessory());
        assertEquals("Invalid feedback type", tType, paired.getType());
        assertEquals("Solicited same as reply", reply.isUnsolicited(), paired.isUnsolicited());
        assertFalse("Must not accept pair's address", paired.matchesAddress(address));
    }
    
    /**
     * Checks that information can be read from single-item feedback and
     * the reply are consistent.
     */
    @Test
    public void testSingleFeedbackTurnoutItem() {
        // 5 * 4, lower nibble = 21 (N/A) +22 (T)
        // movement NOT complete; turnout WITH feedback.
        XNetReply r = new XNetReply("42 05 28 0f");
        Optional<FeedbackItem> selected = r.selectTurnoutFeedback(20);
        assertFalse("Incorrect turnout number", selected.isPresent());
        selected = r.selectTurnoutFeedback(23);
        assertFalse("Incorrect turnout number", selected.isPresent());
        
        selected = r.selectTurnoutFeedback(21);
        assertTrue(selected.isPresent());
        
        FeedbackItem oddItem = selected.get();

        assertTrue("Motion completed", oddItem.isMotionComplete());
        assertTurnoutFeedbackData(r, 1, 21, 0, 1, oddItem);
        
        selected = r.selectTurnoutFeedback(22);
        assertTrue(selected.isPresent());
        
        FeedbackItem evenItem = selected.get();
        assertTurnoutFeedbackData(r, 1, 22, 2, 1, evenItem);
        
        assertFalse(r.isUnsolicited());
        
        // 5 * 4, upper nibble = 23 (C) + 24 (T)
        // movement IS complete; turnout WITHOUT feedback.
        r = new XNetReply("42 05 95 0f");
        selected = r.selectTurnoutFeedback(23);
        assertTrue(selected.isPresent());
        
        oddItem = selected.get();
        assertFalse("Motion incomplete", oddItem.isMotionComplete());
        assertTurnoutFeedbackData(r, 1, 23, 1, 0, oddItem);
        
        selected = r.selectTurnoutFeedback(24);
        assertTrue(selected.isPresent());
        evenItem = selected.get();
        assertTurnoutFeedbackData(r, 1, 24, 1, 0, evenItem);
    }
    
    /**
     * Checks that feedback module item gives invalid / erroneous / null
     * information when used as accessory.
     */
    @Test
    public void testOtherRepliesAsAccessoryFeedback() {
        XNetReply r = new XNetReply("42 05 58 0f");
        // test directly the item
        FeedbackItem item = new FeedbackItem(r, 45, 0x58);
        assertEquals(45, item.getAddress());
        for (int a = 45; a < 45 + 4; a++) {
            assertTrue(item.matchesAddress(a));
            // last bit is set, all others are false.
            assertEquals("Bit state for sensor " + a, a == 48, item.getEncoderStatus(a));
        }
        // does not match accessory for 0x05, 0x58
        assertFalse(item.matchesAddress(21));
        assertEquals(3, item.getAccessoryStatus());
        assertNull(item.pairedAccessoryItem());

        // check that no turnout feedback can be selected
        for (int i = 1 ; i < 1024; i++) {
            assertFalse(r.selectTurnoutFeedback(i).isPresent());
        }
        
        // no accessory feedback present
        r = new XNetReply("E3 40 C1 04 61");
        for (int i = 1 ; i < 1024; i++) {
            assertFalse(r.selectTurnoutFeedback(i).isPresent());
        }
    }
    
    /**
     * Checks that encoder feedback will return null for turnout
     * feedbacks.
     */
    @Test
    public void testOtherRepliesAsEncoder() {
        XNetReply r = new XNetReply("42 05 28 0f");
        for (int i = 1 ; i < 1024; i++) {
            assertNull(r.selectModuleFeedback(i));
        }
        r = new XNetReply("E3 40 C1 04 61");
        for (int i = 1 ; i < 1024; i++) {
            assertNull(r.selectModuleFeedback(i));
        }

    }

    
    /**
     * Checks that select will not filter out accessories the
     * invalid state.
     */
    @Test
    public void testInvalidAccessoryStateFiltered() {
        XNetReply r = new XNetReply("42 05 2B 0f");
        Optional<FeedbackItem> opt = r.selectTurnoutFeedback(21);
        assertTrue(opt.isPresent());
        assertEquals(-1, r.getTurnoutStatus(1));
        
        opt = r.selectTurnoutFeedback(22);
        assertTrue(opt.isPresent());
        assertEquals(Turnout.THROWN, opt.get().getTurnoutStatus());
        assertEquals(r.getTurnoutStatus(0), opt.get().getTurnoutStatus());
    }
    
    @Test
    public void testSingleEncoderModuleFeedback() {
        // feedback 5 * 8  + 4 (upper nibble) (+1) = 45
        XNetReply r = new XNetReply("42 05 58 0f");
        
        assertNull(r.selectModuleFeedback(44));
        
        for (int i = 45; i < 45 + 4; i++) {
            Boolean b = r.selectModuleFeedback(i);
            assertNotNull(b);
            // the highest bit in the nibble is set
            assertEquals("sensor id " + i, i == 48, b);
        }
        
        assertNull(r.selectModuleFeedback(49));
        
        r = new XNetReply("42 04 41 0f");

        assertNull(r.selectModuleFeedback(32));
        for (int i = 33; i < 33 + 4; i++) {
            Boolean b = r.selectModuleFeedback(i);
            assertNotNull(b);
            // the lowest bit in the nibble is set
            assertEquals("sensor id " + i, i == 33, b);
        }
        assertNull(r.selectModuleFeedback(37));
    }
    
    @Test
    public void testMultipleFeedbackTurnoutItem() {
        // 1st pair: 21 (T) + 22 (C), with feedback
        // 2nd pair: encoder feedback, NOT 25+26!
        // 3rd pair: 31 (C) + 32 (N), without feedback, motion incomplete
        XNetReply r = new XNetReply("46 05 29 06 48 07 91 0f");
        
        Optional<FeedbackItem> selected;
        FeedbackItem odd;
        FeedbackItem even;
        selected = r.selectTurnoutFeedback(21);
        assertTrue(selected.isPresent());
        odd = selected.get();
        selected = r.selectTurnoutFeedback(22);
        assertTrue(selected.isPresent());
        even = selected.get();
        assertTrue(odd.isMotionComplete());
        assertTurnoutFeedbackData(r, 1, 21, 1, 1, odd);
        assertTurnoutFeedbackData(r, 1, 22, 2, 1, even);

        // check that 
        selected = r.selectTurnoutFeedback(25);
        assertFalse(selected.isPresent());
        selected = r.selectTurnoutFeedback(26);
        assertFalse(selected.isPresent());

        selected = r.selectTurnoutFeedback(31);
        assertTrue(selected.isPresent());
        odd = selected.get();
        selected = r.selectTurnoutFeedback(32);
        assertTrue(selected.isPresent());
        even = selected.get();
        
        assertFalse(odd.isMotionComplete());
        assertTurnoutFeedbackData(r, 5, 31, 1, 0, odd);
        assertTurnoutFeedbackData(r, 5, 32, 0, 0, even);
    }
    
    @Test
    public void testMultipleEncoderModuleFeedback() {
        // feedback 6 * 8  (lower nibble) (+1) = 49
        XNetReply r = new XNetReply("46 05 29 06 48 07 91 0f");
        
        // the 05-29 is a turnout feedback: must not be reported
        // as encoder 05 * 8
        assertNull(r.selectModuleFeedback(41));
        assertNull(r.selectModuleFeedback(44));
        assertNull(r.selectModuleFeedback(48));
        
        for (int i = 49; i < 49 + 4; i++) {
            Boolean b = r.selectModuleFeedback(i);
            assertNotNull(b);
            // the highest bit in the nibble is set
            assertEquals("sensor id " + i, i == (49 + 3), b);
        }
        
        // 07-91 is again a turnout, must not be mistaken for upper nibble of
        // encoder 7
        assertNull(r.selectModuleFeedback(7 * 8 + 4 + 1));
    }
    
    // The minimal setup for log4J
    @BeforeEach
    @Override
    public void setUp() {
        JUnitUtil.setUp();
        m = msg = new XNetReply();
    }

    @Override
    @AfterEach
    public void tearDown() {
        m = msg = null;
        JUnitUtil.tearDown();
    }

}
