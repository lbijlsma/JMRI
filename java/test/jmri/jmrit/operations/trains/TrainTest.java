package jmri.jmrit.operations.trains;

import org.junit.Assert;
import org.junit.jupiter.api.*;

import jmri.InstanceManager;
import jmri.jmrit.operations.OperationsTestCase;
import jmri.jmrit.operations.locations.*;
import jmri.jmrit.operations.locations.schedules.*;
import jmri.jmrit.operations.rollingstock.cars.*;
import jmri.jmrit.operations.rollingstock.engines.*;
import jmri.jmrit.operations.routes.*;
import jmri.jmrit.operations.setup.Setup;
import jmri.util.JUnitOperationsUtil;

/**
 * Tests for the Operations Trains class Last manually cross-checked on 20090131
 * <p>
 * Still to do:
 * <p>
 * Train: DepartureTime, ArrivalTime
 * <p>
 * Train: numberCarsWorked
 * <p>
 * Train: isTraininRoute
 * <p>
 * Train: getBuild, setBuildEnabled, buildIfSelected
 * <p>
 * Train: printBuildReport, printManifest, printReport
 * <p>
 * Train: getPrint, setPrint, printIfSelected
 * <p>
 * Train: setTrainIconCoordinates
 * <p>
 * Train: terminateIfSelected
 * <p>
 * Train: load/move/get/create Train Icon
 * <p>
 * Train: get/set Lead Engine
 * <p>
 * Train: setIconColor
 * <p>
 * Train: reset
 * <p>
 * Train: xml read/write
 * <p>
 * Train: Most build scenarios.
 * <p>
 * TrainBuilder: Everything. TrainSwitchLists: Everything.
 *
 * @author Bob Coleman Copyright (C) 2008, 2009
 */
public class TrainTest extends OperationsTestCase {

    // the managers used in this set of tests
    TrainManager tmanager = null;
    RouteManager rmanager = null;
    LocationManager lmanager = null;
    EngineManager emanager = null;
    // CarManager cmanager = null;
    CarTypes ct = null;

    // test Train creation
    @Test
    public void testCreate() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());
    }

    // test Train public constants
    @Test
    public void testConstants() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

        Assert.assertEquals("Train Constant NONE", 0, Train.NO_CABOOSE_OR_FRED);
        Assert.assertEquals("Train Constant CABOOSE", 1, Train.CABOOSE);
        Assert.assertEquals("Train Constant FRED", 2, Train.FRED);

        Assert.assertEquals("Train Constant ALLROADS", "All", Train.ALL_ROADS);
        Assert.assertEquals("Train Constant INCLUDEROADS", "Include", Train.INCLUDE_ROADS);
        Assert.assertEquals("Train Constant EXCLUDEROADS", "Exclude", Train.EXCLUDE_ROADS);

        Assert.assertEquals("Train Constant DISPOSE_CHANGED_PROPERTY", "TrainDispose", Train.DISPOSE_CHANGED_PROPERTY);
        Assert.assertEquals("Train Constant STOPS_CHANGED_PROPERTY", "TrainStops", Train.STOPS_CHANGED_PROPERTY);
        Assert.assertEquals("Train Constant TYPES_CHANGED_PROPERTY", "TrainTypes", Train.TYPES_CHANGED_PROPERTY);
        Assert.assertEquals("Train Constant ROADS_CHANGED_PROPERTY", "TrainRoads", Train.ROADS_CHANGED_PROPERTY);
        Assert.assertEquals("Train Constant STATUS_CHANGED_PROPERTY", "TrainStatus", Train.STATUS_CHANGED_PROPERTY);
        Assert.assertEquals("Train Constant DEPARTURETIME_CHANGED_PROPERTY", "TrainDepartureTime",
                Train.DEPARTURETIME_CHANGED_PROPERTY);

        Assert.assertEquals("Train Constant AUTO", "Auto", Train.AUTO);
    }

    // test Train attributes
    @Test
    public void testAttributes() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());
        Assert.assertEquals("Train toString", "TESTTRAINNAME", train1.toString());

        train1.setName("TESTNEWNAME");
        Assert.assertEquals("Train New Name", "TESTNEWNAME", train1.getName());
        train1.setComment("TESTCOMMENT");
        Assert.assertEquals("Train Comment", "TESTCOMMENT", train1.getCommentWithColor());
        train1.setDescription("TESTDESCRIPTION");
        Assert.assertEquals("Train Description", "TESTDESCRIPTION", train1.getDescription());
        train1.setCabooseRoad("TESTCABOOSEROAD");
        Assert.assertEquals("Train Caboose Road", "TESTCABOOSEROAD", train1.getCabooseRoad());
        train1.setEngineModel("TESTENGINEMODEL");
        Assert.assertEquals("Train Engine Model", "TESTENGINEMODEL", train1.getEngineModel());
        train1.setEngineRoad("TESTENGINEROAD");
        Assert.assertEquals("Train Engine Road", "TESTENGINEROAD", train1.getEngineRoad());
        train1.setBuilt(true);
        Assert.assertTrue("Train Built true", train1.isBuilt());
        train1.setBuilt(false);
        Assert.assertFalse("Train Built false", train1.isBuilt());
        train1.setNumberEngines("13");
        Assert.assertEquals("Train Number Engines", "13", train1.getNumberEngines());
        train1.setCarRoadOption("INCLUDEROADS");
        Assert.assertEquals("Train Road Option INCLUDEROADS", "INCLUDEROADS", train1.getCarRoadOption());
        train1.setCarRoadOption("EXCLUDEROADS");
        Assert.assertEquals("Train Road Option EXCLUDEROADS", "EXCLUDEROADS", train1.getCarRoadOption());
        train1.setCarRoadOption("ALLROADS");
        Assert.assertEquals("Train Road Option ALLROADS", "ALLROADS", train1.getCarRoadOption());
        train1.setStatusCode(Train.CODE_UNKNOWN);
        Assert.assertEquals("Train Status", Train.UNKNOWN, train1.getStatus());
        train1.setRequirements(Train.CABOOSE);
        Assert.assertEquals("Train Requirements CABOOSE", 1, train1.getRequirements());
        Assert.assertTrue("requires caboose", train1.isCabooseNeeded());
        train1.setRequirements(Train.FRED);
        Assert.assertEquals("Train Requirements FRED", 2, train1.getRequirements());
        Assert.assertTrue("requires FRED", train1.isFredNeeded());
        train1.setRequirements(Train.NO_CABOOSE_OR_FRED);
        Assert.assertEquals("Train Requirements NONE", 0, train1.getRequirements());
        Assert.assertFalse("no caboose", train1.isCabooseNeeded());
        Assert.assertFalse("no FRED", train1.isFredNeeded());
        train1.setDepartureTime("12", "55");
        Assert.assertEquals("Train departure hour", "12", train1.getDepartureTimeHour());
        Assert.assertEquals("Train departure minute", "55", train1.getDepartureTimeMinute());
        Assert.assertEquals("Train departure hour and minute", "12:55", train1.getDepartureTime());
    }

    @Test
    public void testTrainDefaults() {

        Train train = new Train("1", "test name");
        Assert.assertTrue(train.isAllowLocalMovesEnabled());
        Assert.assertFalse(train.isAllowReturnToStagingEnabled());
        Assert.assertTrue(train.isAllowThroughCarsEnabled());
        Assert.assertFalse(train.isBuildConsistEnabled());
        Assert.assertTrue(train.isBuildEnabled());
        Assert.assertFalse(train.isBuildTrainNormalEnabled());
        Assert.assertFalse(train.isBuilt());
        Assert.assertTrue(train.isLocalSwitcher());
        Assert.assertFalse(train.isModified());
        Assert.assertTrue(train.isOnlyPassengerCars());
        Assert.assertFalse(train.isPrinted());
        Assert.assertFalse(train.isSendCarsToTerminalEnabled());
        Assert.assertFalse(train.isSendCarsWithCustomLoadsToStagingEnabled());
        Assert.assertFalse(train.isServiceAllCarsWithFinalDestinationsEnabled());
        Assert.assertTrue(train.isShowArrivalAndDepartureTimesEnabled());
        Assert.assertFalse(train.isTrainEnRoute());

    }

    // test Train route
    @Test
    public void testRoute() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

        Route r1 = new Route("TESTROUTEID", "TESTROUTENAME");

        train1.setRoute(r1);
        Assert.assertEquals("Train Route Name", "TESTROUTENAME", train1.getTrainRouteName());

        Route rnew = new Route("TESTROUTEID2", "TESTNEWROUTENAME");

        Location l1 = new Location("TESTLOCATIONID1", "TESTNEWROUTEDEPTNAME");
        rnew.addLocation(l1);
        Location l2 = new Location("TESTLOCATIONID2", "TESTLOCATIONNAME2");
        rnew.addLocation(l2);
        Location l3 = new Location("TESTLOCATIONID3", "TESTNEWROUTECURRNAME");
        rnew.addLocation(l3);
        Location l4 = new Location("TESTLOCATIONID4", "TESTLOCATIONNAME4");
        rnew.addLocation(l4);
        Location l5 = new Location("TESTLOCATIONID5", "TESTNEWROUTETERMNAME");
        rnew.addLocation(l5);

        train1.setRoute(rnew);
        Assert.assertEquals("Train New Route Name", "TESTNEWROUTENAME", train1.getTrainRouteName());

        Assert.assertEquals("Train New Route Departure Name", "TESTNEWROUTEDEPTNAME", train1.getTrainDepartsName());
        Assert.assertEquals("Train New Route Terminates Name", "TESTNEWROUTETERMNAME", train1.getTrainTerminatesName());

        RouteLocation rl1test;
        rl1test = rnew.getLastLocationByName("TESTNEWROUTECURRNAME");
        train1.setCurrentLocation(rl1test);
        Assert.assertEquals("Train New Route Current Name", "TESTNEWROUTECURRNAME", train1.getCurrentLocationName());
        rl1test = train1.getCurrentRouteLocation();
        Assert.assertEquals("Train New Route Current Name by Route Location", "TESTNEWROUTECURRNAME",
                rl1test.getName());
    }

    // test Train skip locations support
    @Test
    public void testSkipLocations() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

        Location loc2 = new Location("LOCATIONID2", "TESTLOCATION2");
        RouteLocation rl2 = new RouteLocation("TESTLOCATIONID2", loc2);
        train1.addTrainSkipsLocation(rl2);
        Assert.assertTrue("Location 2 to be skipped", train1.isLocationSkipped(rl2));

        Location loc4 = new Location("LOCATIONID4", "TESTLOCATION4");
        RouteLocation rl4 = new RouteLocation("TESTLOCATIONID4", loc4);
        train1.addTrainSkipsLocation(rl4);
        Assert.assertTrue("Location 4 to be skipped", train1.isLocationSkipped(rl4));

        train1.deleteTrainSkipsLocation(rl2);
        Assert.assertFalse("Location 2 not to be skipped", train1.isLocationSkipped(rl2));
        Assert.assertTrue("Location 4 still to be skipped", train1.isLocationSkipped(rl4));

        train1.deleteTrainSkipsLocation(rl4);
        Assert.assertFalse("Location 2 still not to be skipped", train1.isLocationSkipped(rl2));
        Assert.assertFalse("Location 4 not to be skipped", train1.isLocationSkipped(rl4));
    }

    // test Train accepts types support
    @Test
    public void testAcceptsTypes() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

        // add the HopperTest cartype here, so that the train doesn't
        // know about it.
        InstanceManager.getDefault(CarTypes.class).addName("HopperTest");

        // Caboose is one of the default car types
        Assert.assertTrue("Train accepts type name Caboose", train1.isTypeNameAccepted(Bundle.getMessage("Caboose")));
        Assert.assertFalse("Train does not accept type name HopperTest", train1.isTypeNameAccepted("HopperTest"));

        train1.addTypeName("HopperTest");
        Assert.assertTrue("Train still accepts type name Caboose",
                train1.isTypeNameAccepted(Bundle.getMessage("Caboose")));
        Assert.assertTrue("Train accepts type name HopperTest", train1.isTypeNameAccepted("HopperTest"));

        train1.deleteTypeName(Bundle.getMessage("Caboose"));
        Assert.assertFalse("Train no longer accepts type name Caboose",
                train1.isTypeNameAccepted(Bundle.getMessage("Caboose")));
        Assert.assertTrue("Train still accepts type name HopperTest", train1.isTypeNameAccepted("HopperTest"));
    }

    @Test
    public void testReplaceType() {

        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        // replacing a car type also needs to adjust load names accepted by this train
        Assert.assertTrue(train1.addLoadName("Boxcar" + CarLoad.SPLIT_CHAR + "NutAndBolts"));

        Assert.assertTrue("Train accepts type name Boxcar", train1.isTypeNameAccepted("Boxcar"));
        Assert.assertFalse("Train does not accept type name BOXCAR", train1.isTypeNameAccepted("BOXCAR"));

        train1.replaceType("Boxcar", "BOXCAR");

        Assert.assertFalse("Train does not accept type name Boxcar", train1.isTypeNameAccepted("Boxcar"));
        Assert.assertTrue("Train accepts type name BOXCAR", train1.isTypeNameAccepted("BOXCAR"));

        // Boxcar with NutAndBolts no longer exists
        Assert.assertFalse(train1.deleteLoadName("Boxcar" + CarLoad.SPLIT_CHAR + "NutAndBolts"));
        // BOXCAR with NutAndBolts should exists
        Assert.assertTrue(train1.deleteLoadName("BOXCAR" + CarLoad.SPLIT_CHAR + "NutAndBolts"));
    }

    // test train accepts road names support
    @Test
    public void testAcceptsRoadNames() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

        train1.setCarRoadOption(Train.ALL_ROADS);
        Assert.assertTrue("Train accepts (ALLROADS) Road name CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertTrue("Train accepts (ALLROADS) Road name VIA", train1.isCarRoadNameAccepted("VIA"));

        train1.setCarRoadOption(Train.INCLUDE_ROADS);
        Assert.assertTrue(train1.addCarRoadName("CP"));
        Assert.assertFalse(train1.addCarRoadName("CP")); // returns false if name already exists
        Assert.assertTrue("Train accepts (INCLUDEROADS) Road name CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertFalse("Train does not accept (INCLUDEROADS) Road name VIA", train1.isCarRoadNameAccepted("VIA"));

        train1.addCarRoadName("VIA");
        Assert.assertTrue("Train still accepts (INCLUDEROADS) Road name CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertTrue("Train accepts (INCLUDEROADS) Road name VIA", train1.isCarRoadNameAccepted("VIA"));

        Assert.assertTrue(train1.deleteCarRoadName("CP")); // returns true if name exists
        Assert.assertFalse(train1.deleteCarRoadName("CP"));
        Assert.assertFalse("Train no longer accepts (INCLUDEROADS) Road name CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertTrue("Train still accepts (INCLUDEROADS) Road name VIA", train1.isCarRoadNameAccepted("VIA"));

        train1.setCarRoadOption(Train.EXCLUDE_ROADS);
        Assert.assertTrue("Train does accept (EXCLUDEROADS) Road name CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertFalse("Train does not accept (EXCLUDEROADS) Road name VIA", train1.isCarRoadNameAccepted("VIA"));

        train1.addCarRoadName("CP");
        Assert.assertFalse("Train does not accept (EXCLUDEROADS) Road name CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertFalse("Train still does not accept (EXCLUDEROADS) Road name VIA",
                train1.isCarRoadNameAccepted("VIA"));

        train1.deleteCarRoadName("VIA");
        Assert.assertFalse("Train still does not accepts (EXCLUDEROADS) Road name CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertTrue("Train now accepts (EXCLUDEROADS) Road name VIA", train1.isCarRoadNameAccepted("VIA"));
    }

    @Test
    public void testReplaceRoadName() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        // there are 9 places for road names
        train1.setCabooseRoad("A");
        train1.setEngineRoad("B");
        train1.setSecondLegCabooseRoad("D");
        train1.setThirdLegCabooseRoad("E");
        train1.setSecondLegEngineRoad("F");
        train1.setThirdLegEngineRoad("G");

        train1.setCarRoadOption(Train.INCLUDE_ROADS);
        Assert.assertTrue(train1.addCarRoadName("C"));
        train1.setCabooseRoadOption(Train.INCLUDE_ROADS);
        Assert.assertTrue(train1.addCabooseRoadName("A"));
        train1.setLocoRoadOption(Train.INCLUDE_ROADS);
        Assert.assertTrue(train1.addLocoRoadName("F"));

        // confirm
        Assert.assertEquals("caboose road", "A", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "B", train1.getEngineRoad());
        Assert.assertEquals("Second Leg Caboose Road", "D", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "E", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "F", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "G", train1.getThirdLegEngineRoad());

        Assert.assertEquals("car road", true, train1.isCarRoadNameAccepted("C"));
        Assert.assertEquals("car road", false, train1.isCarRoadNameAccepted("c"));
        Assert.assertEquals("caboose road", true, train1.isCabooseRoadNameAccepted("A"));
        Assert.assertEquals("caboose road", false, train1.isCabooseRoadNameAccepted("a"));
        Assert.assertEquals("loco road", true, train1.isLocoRoadNameAccepted("F"));
        Assert.assertEquals("loco road", false, train1.isLocoRoadNameAccepted("f"));

        train1.replaceRoad("A", "a");

        // confirm
        Assert.assertEquals("caboose road", "a", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "B", train1.getEngineRoad());
        Assert.assertEquals("Second Leg Caboose Road", "D", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "E", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "F", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "G", train1.getThirdLegEngineRoad());

        Assert.assertEquals("car road", true, train1.isCarRoadNameAccepted("C"));
        Assert.assertEquals("car road", false, train1.isCarRoadNameAccepted("c"));
        Assert.assertEquals("caboose road", true, train1.isCabooseRoadNameAccepted("a"));
        Assert.assertEquals("caboose road", false, train1.isCabooseRoadNameAccepted("A"));
        Assert.assertEquals("loco road", true, train1.isLocoRoadNameAccepted("F"));
        Assert.assertEquals("loco road", false, train1.isLocoRoadNameAccepted("f"));

        train1.replaceRoad("B", "b");

        // confirm
        Assert.assertEquals("caboose road", "a", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "b", train1.getEngineRoad());
        Assert.assertEquals("Second Leg Caboose Road", "D", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "E", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "F", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "G", train1.getThirdLegEngineRoad());

        Assert.assertEquals("car road", true, train1.isCarRoadNameAccepted("C"));
        Assert.assertEquals("car road", false, train1.isCarRoadNameAccepted("c"));
        Assert.assertEquals("caboose road", true, train1.isCabooseRoadNameAccepted("a"));
        Assert.assertEquals("caboose road", false, train1.isCabooseRoadNameAccepted("A"));
        Assert.assertEquals("loco road", true, train1.isLocoRoadNameAccepted("F"));
        Assert.assertEquals("loco road", false, train1.isLocoRoadNameAccepted("f"));

        train1.replaceRoad("C", "c");

        // confirm
        Assert.assertEquals("caboose road", "a", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "b", train1.getEngineRoad());
        Assert.assertEquals("Second Leg Caboose Road", "D", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "E", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "F", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "G", train1.getThirdLegEngineRoad());

        Assert.assertEquals("car road", true, train1.isCarRoadNameAccepted("c"));
        Assert.assertEquals("car road", false, train1.isCarRoadNameAccepted("C"));
        Assert.assertEquals("caboose road", true, train1.isCabooseRoadNameAccepted("a"));
        Assert.assertEquals("caboose road", false, train1.isCabooseRoadNameAccepted("A"));
        Assert.assertEquals("loco road", true, train1.isLocoRoadNameAccepted("F"));
        Assert.assertEquals("loco road", false, train1.isLocoRoadNameAccepted("f"));

        train1.replaceRoad("D", "d");

        // confirm
        Assert.assertEquals("caboose road", "a", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "b", train1.getEngineRoad());
        Assert.assertEquals("Second Leg Caboose Road", "d", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "E", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "F", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "G", train1.getThirdLegEngineRoad());

        Assert.assertEquals("car road", true, train1.isCarRoadNameAccepted("c"));
        Assert.assertEquals("car road", false, train1.isCarRoadNameAccepted("C"));
        Assert.assertEquals("caboose road", true, train1.isCabooseRoadNameAccepted("a"));
        Assert.assertEquals("caboose road", false, train1.isCabooseRoadNameAccepted("A"));
        Assert.assertEquals("loco road", true, train1.isLocoRoadNameAccepted("F"));
        Assert.assertEquals("loco road", false, train1.isLocoRoadNameAccepted("f"));

        train1.replaceRoad("E", "e");

        // confirm
        Assert.assertEquals("caboose road", "a", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "b", train1.getEngineRoad());
        Assert.assertEquals("Second Leg Caboose Road", "d", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "e", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "F", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "G", train1.getThirdLegEngineRoad());

        train1.replaceRoad("F", "f");

        // confirm
        Assert.assertEquals("caboose road", "a", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "b", train1.getEngineRoad());
        Assert.assertEquals("Second Leg Caboose Road", "d", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "e", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "f", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "G", train1.getThirdLegEngineRoad());

        Assert.assertEquals("car road", true, train1.isCarRoadNameAccepted("c"));
        Assert.assertEquals("car road", false, train1.isCarRoadNameAccepted("C"));
        Assert.assertEquals("caboose road", true, train1.isCabooseRoadNameAccepted("a"));
        Assert.assertEquals("caboose road", false, train1.isCabooseRoadNameAccepted("A"));
        Assert.assertEquals("loco road", true, train1.isLocoRoadNameAccepted("f"));
        Assert.assertEquals("loco road", false, train1.isLocoRoadNameAccepted("F"));

        train1.replaceRoad("G", "g");

        // confirm
        Assert.assertEquals("caboose road", "a", train1.getCabooseRoad());
        Assert.assertEquals("engine road", "b", train1.getEngineRoad());
        Assert.assertEquals("rolling stock road", true, train1.isCarRoadNameAccepted("c"));
        Assert.assertEquals("Second Leg Caboose Road", "d", train1.getSecondLegCabooseRoad());
        Assert.assertEquals("Third Leg Caboose Road", "e", train1.getThirdLegCabooseRoad());
        Assert.assertEquals("Second Leg Engine Road", "f", train1.getSecondLegEngineRoad());
        Assert.assertEquals("Third Leg Engine Road", "g", train1.getThirdLegEngineRoad());
    }

    // test train accepts load names support
    @Test
    public void testAcceptsLoadNames() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

        train1.setLoadOption(Train.ALL_LOADS);
        Assert.assertTrue("Train accepts (ALLLOADS) Load name BOXES", train1.isLoadNameAccepted("BOXES"));
        Assert.assertTrue("Train accepts (ALLLOADS) Load name WOOD", train1.isLoadNameAccepted("WOOD"));

        train1.setLoadOption(Train.INCLUDE_LOADS);
        Assert.assertTrue(train1.addLoadName("BOXES"));
        Assert.assertFalse(train1.addLoadName("BOXES")); // returns false if name already exists
        Assert.assertTrue("Train accepts (INCLUDELOADS) Load name BOXES", train1.isLoadNameAccepted("BOXES"));
        Assert.assertFalse("Train does not accept (INCLUDELOADS) Load name WOOD", train1.isLoadNameAccepted("WOOD"));

        Assert.assertTrue(train1.addLoadName("WOOD"));
        Assert.assertTrue("Train still accepts (INCLUDELOADS) Load name BOXES", train1.isLoadNameAccepted("BOXES"));
        Assert.assertTrue("Train accepts (INCLUDELOADS) Load name WOOD", train1.isLoadNameAccepted("WOOD"));

        Assert.assertTrue(train1.addLoadName("Boxcar" + CarLoad.SPLIT_CHAR + "SCREWS"));
        Assert.assertFalse("Train does not accept (INCLUDELOADS) Load name SCREWS",
                train1.isLoadNameAccepted("SCREWS"));
        Assert.assertTrue("Train still accepts (INCLUDELOADS) Load name BOXES",
                train1.isLoadNameAccepted("BOXES", "Boxcar"));
        Assert.assertTrue("Train accepts (INCLUDELOADS) Load WOOD carried by Boxcar",
                train1.isLoadNameAccepted("WOOD", "Boxcar"));
        Assert.assertTrue("Train accepts (INCLUDELOADS) Load Boxcar with SCREWS",
                train1.isLoadNameAccepted("SCREWS", "Boxcar"));

        Assert.assertTrue(train1.deleteLoadName("BOXES")); // returns true if name exists
        Assert.assertFalse(train1.deleteLoadName("BOXES"));
        Assert.assertFalse("Train no longer accepts (INCLUDELOADS) Load name BOXES",
                train1.isLoadNameAccepted("BOXES"));
        Assert.assertTrue("Train still accepts (INCLUDELOADS) Load name WOOD", train1.isLoadNameAccepted("WOOD"));

        train1.setLoadOption(Train.EXCLUDE_LOADS);
        Assert.assertTrue("Train does accept (EXCLUDELOADS) Load name BOXES", train1.isLoadNameAccepted("BOXES"));
        Assert.assertFalse("Train does not accept (EXCLUDELOADS) Load name WOOD", train1.isLoadNameAccepted("WOOD"));

        Assert.assertTrue(train1.addLoadName("BOXES"));
        Assert.assertFalse("Train does not accept (EXCLUDELOADS) Load name BOXES", train1.isLoadNameAccepted("BOXES"));
        Assert.assertFalse("Train still does not accept (EXCLUDELOADS) Load name WOOD",
                train1.isLoadNameAccepted("WOOD"));

        Assert.assertTrue(train1.deleteLoadName("WOOD"));
        Assert.assertFalse("Train still does not accepts (EXCLUDELOADS) Load name BOXES",
                train1.isLoadNameAccepted("BOXES"));
        Assert.assertTrue("Train now accepts (EXCLUDELOADS) Load name WOOD", train1.isLoadNameAccepted("WOOD"));
    }

    @Test
    public void testAcceptsOwnerNames() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        Assert.assertEquals("Train Id", "TESTTRAINID", train1.getId());
        Assert.assertEquals("Train Name", "TESTTRAINNAME", train1.getName());

        train1.setOwnerOption(Train.ALL_OWNERS);
        Assert.assertTrue("Train accepts (ALLOWNERS) Owner name BOB", train1.isOwnerNameAccepted("BOB"));
        Assert.assertTrue("Train accepts (ALLOWNERS) Owner name DAN", train1.isOwnerNameAccepted("DAN"));

        train1.setOwnerOption(Train.INCLUDE_OWNERS);
        Assert.assertTrue(train1.addOwnerName("BOB"));
        Assert.assertFalse(train1.addOwnerName("BOB")); // returns false if name already exists
        Assert.assertTrue("Train accepts (INCLUDEOWNERS) Owner name BOB", train1.isOwnerNameAccepted("BOB"));
        Assert.assertFalse("Train does not accept (INCLUDEOWNERS) Owner name DAN", train1.isOwnerNameAccepted("DAN"));

        Assert.assertTrue(train1.addOwnerName("DAN"));
        Assert.assertTrue("Train still accepts (INCLUDEOWNERS) Owner name BOB", train1.isOwnerNameAccepted("BOB"));
        Assert.assertTrue("Train accepts (INCLUDEOWNERS) Owner name DAN", train1.isOwnerNameAccepted("DAN"));

        Assert.assertTrue(train1.deleteOwnerName("BOB")); // returns true if name exists
        Assert.assertFalse(train1.deleteOwnerName("BOB"));
        Assert.assertFalse("Train no longer accepts (INCLUDEOWNERS) Owner name BOB", train1.isOwnerNameAccepted("BOB"));
        Assert.assertTrue("Train still accepts (INCLUDEOWNERS) Owner name DAN", train1.isOwnerNameAccepted("DAN"));

        train1.setOwnerOption(Train.EXCLUDE_OWNERS);
        Assert.assertTrue("Train does accept (EXCLUDEOWNERS) Owner name BOB", train1.isOwnerNameAccepted("BOB"));
        Assert.assertFalse("Train does not accept (EXCLUDEOWNERS) Owner name DAN", train1.isOwnerNameAccepted("DAN"));

        Assert.assertTrue(train1.addOwnerName("BOB"));
        Assert.assertFalse("Train does not accept (EXCLUDEOWNERS) Owner name BOB", train1.isOwnerNameAccepted("BOB"));
        Assert.assertFalse("Train still does not accept (EXCLUDEOWNERS) Owner name DAN",
                train1.isOwnerNameAccepted("DAN"));

        Assert.assertTrue(train1.deleteOwnerName("DAN"));
        Assert.assertFalse("Train still does not accepts (EXCLUDEOWNERS) Owner name BOB",
                train1.isOwnerNameAccepted("BOB"));
        Assert.assertTrue("Train now accepts (EXCLUDEOWNERS) Owner name DAN", train1.isOwnerNameAccepted("DAN"));

        // test replace
        Assert.assertTrue("Train does accept (EXCLUDEOWNERS) Owner name bob", train1.isOwnerNameAccepted("bob"));
        train1.replaceOwner("BOB", "bob");
        Assert.assertFalse("Train does not accept (EXCLUDEOWNERS) Owner name bob", train1.isOwnerNameAccepted("bob"));
    }

    @Test
    public void testEngineModels() {
        Train train1 = new Train("TESTTRAINID", "TESTTRAINNAME");

        train1.setEngineModel("TestEngineModel");
        train1.setSecondLegEngineModel("Test2ndLegEngineModel");
        train1.setThirdLegEngineModel("Test3rdLegEngineModel");

        // confirm
        Assert.assertEquals("Engine model", "TestEngineModel", train1.getEngineModel());
        Assert.assertEquals("Engine model", "Test2ndLegEngineModel", train1.getSecondLegEngineModel());
        Assert.assertEquals("Engine model", "Test3rdLegEngineModel", train1.getThirdLegEngineModel());

        // test replace
        train1.replaceModel("TestEngineModel", "NewEngineModel");

        // confirm
        Assert.assertEquals("Engine model", "NewEngineModel", train1.getEngineModel());
        Assert.assertEquals("Engine model", "Test2ndLegEngineModel", train1.getSecondLegEngineModel());
        Assert.assertEquals("Engine model", "Test3rdLegEngineModel", train1.getThirdLegEngineModel());

        // test replace
        train1.replaceModel("Test2ndLegEngineModel", "New2ndLegEngineModel");

        // confirm
        Assert.assertEquals("Engine model", "NewEngineModel", train1.getEngineModel());
        Assert.assertEquals("Engine model", "New2ndLegEngineModel", train1.getSecondLegEngineModel());
        Assert.assertEquals("Engine model", "Test3rdLegEngineModel", train1.getThirdLegEngineModel());

        // test replace
        train1.replaceModel("Test3rdLegEngineModel", "New3rdLegEngineModel");

        // confirm
        Assert.assertEquals("Engine model", "NewEngineModel", train1.getEngineModel());
        Assert.assertEquals("Engine model", "New2ndLegEngineModel", train1.getSecondLegEngineModel());
        Assert.assertEquals("Engine model", "New3rdLegEngineModel", train1.getThirdLegEngineModel());
    }

    @Test
    public void testNoRouteBuild() {

        Train train = tmanager.newTrain("Test");

        // build train without a route, should fail
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, no route", train.isBuilt());
        Assert.assertEquals("Train build failed", Train.CODE_BUILD_FAILED, train.getStatusCode());

    }

    @Test
    public void testRouteLocationBuild() {

        Train train = tmanager.newTrain("Test");

        // exercise manifest build
        train.setRailroadName("Working Railroad");
        train.setComment("One Hard Working Train");

        // now add a route that doesn't have any locations
        Route route = rmanager.newRoute("TestRoute");
        train.setRoute(route);
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, no route locations", train.isBuilt());
        Assert.assertEquals("Train build failed", Train.CODE_BUILD_FAILED, train.getStatusCode());
    }

    @Test
    public void testRouteLocationsBuild() {

        Train train = tmanager.newTrain("Test");

        // exercise manifest build
        train.setRailroadName("Working Railroad");
        train.setComment("One Hard Working Train");

        Route route = rmanager.newRoute("TestRoute");
        train.setRoute(route);

        // now add a location to the route
        Location depart = lmanager.newLocation("depart");
        RouteLocation rl = route.addLocation(depart);
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build", train.isBuilt());
        Assert.assertEquals("Train built", Train.CODE_PARTIAL_BUILT, train.getStatusCode());

        // delete location
        lmanager.deregister(depart);
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, location deleted", train.isBuilt());
        Assert.assertEquals("Train build failed", Train.CODE_BUILD_FAILED, train.getStatusCode());

        // recreate location
        depart = lmanager.newLocation("depart");
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, location recreated, but not part of route", train.isBuilt());

        route.deleteLocation(rl);
        route.addLocation(depart);
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, route repaired", train.isBuilt());

        Location terminate = lmanager.newLocation("terminate");
        rl = route.addLocation(terminate);
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, route has two locations", train.isBuilt());

        // delete terminal location
        lmanager.deregister(terminate);
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, terminal location deleted", train.isBuilt());

        route.deleteLocation(rl);
        terminate = lmanager.newLocation("terminate");
        rl = route.addLocation(terminate);
        Assert.assertNotNull(rl);
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, route has been repaired", train.isBuilt());

        Location middle = lmanager.newLocation("middle");
        // staging tracks in the middle of the route are ignored
        middle.addTrack("staging in the middle", Track.STAGING);

        // next 5 lines exercise manifest build messages
        Setup.setPrintLocationCommentsEnabled(true);
        middle.setComment("Middle comment");
        rl = route.addLocation(middle, 2); // put location in middle of route
        rl.setDepartureTime("12:30");
        rl.setComment("This location has a departure time");

        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, three location route", train.isBuilt());

        // delete location in the middle
        lmanager.deregister(middle);
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, middle location deleted", train.isBuilt());

        // remove the middle location from the route
        route.deleteLocation(rl);
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, two location route", train.isBuilt());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testBuildRequiresCars() {

        Train train = tmanager.newTrain("Test");

        // exercise manifest build
        train.setRailroadName("Working Railroad");
        train.setComment("One Hard Working Train");

        // now add a route that doesn't have any locations
        Route route = rmanager.newRoute("TestRoute");
        train.setRoute(route);

        // now add locations to the route
        Location depart = lmanager.newLocation("depart");
        route.addLocation(depart);

        Location terminate = lmanager.newLocation("terminate");
        route.addLocation(terminate);

        Location middle = lmanager.newLocation("middle");
        route.addLocation(middle, 2); // put location in middle of route

        // Build option require cars

        // restore control
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, build doesn't require cars", train.isBuilt());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testAutoEnginesBuildFailNoEngines() {

        Train train = tmanager.newTrain("AutoEngineTest");
        train.setNumberEngines(Train.AUTO);

        Route route = JUnitOperationsUtil.createThreeLocationRoute();
        train.setRoute(route);

        // Auto Engines calculates the number of engines based on requested moves in the
        // route
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, no engines", train.isBuilt());
    }

    @Test
    public void testAutoEnginesSingleEngine() {

        Train train = tmanager.newTrain("AutoEngineTest");
        train.setNumberEngines(Train.AUTO);

        Route route = JUnitOperationsUtil.createThreeLocationRoute();
        train.setRoute(route);

        RouteLocation rA = route.getDepartsRouteLocation();
        RouteLocation rB = route.getRouteLocationBySequenceNumber(2);
        RouteLocation rC = route.getTerminatesRouteLocation();

        Assert.assertEquals("confirm location", "Acton", rA.getLocation().getName());
        Assert.assertEquals("confirm location", "Boston", rB.getLocation().getName());
        Assert.assertEquals("confirm location", "Chelmsford", rC.getLocation().getName());

        // place four engines at the start of the route
        Location A = rA.getLocation();
        Track actonYard1 = A.getTrackByName("Acton Yard 1", null);
        Assert.assertNotNull(actonYard1);
        placeFourEngines(actonYard1);
        
        rA.setMaxCarMoves(8);
        rB.setMaxCarMoves(9);

        // 17 moves requires 2 engines, build should fail
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, needs 2 engines", train.isBuilt());

        // 16 moves requires 1 engine
        rB.setMaxCarMoves(8);

        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, only needs a single engine", train.isBuilt());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testAutoEnginesTwoEngines() {

        Train train = tmanager.newTrain("AutoEngineTest");
        train.setNumberEngines(Train.AUTO);

        Route route = JUnitOperationsUtil.createThreeLocationRoute();
        train.setRoute(route);

        // place four engines at the start of the route
        RouteLocation rA = route.getDepartsRouteLocation();
        Location A = rA.getLocation();
        Track actonYard1 = A.getTrackByName("Acton Yard 1", null);
        Assert.assertNotNull(actonYard1);
        placeFourEngines(actonYard1);
        
        // 17 moves requires 2 engines, build should fail
        rA.setMaxCarMoves(10);

        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, only single engines", train.isBuilt());

        // now build a consist of two engines
        Consist c = InstanceManager.getDefault(ConsistManager.class).newConsist("c");
        Engine e1 = emanager.getByRoadAndNumber("E", "1");
        Engine e2 = emanager.getByRoadAndNumber("E", "2");
        e1.setConsist(c);
        e2.setConsist(c);

        // train should require two engines
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build", train.isBuilt());

        Assert.assertEquals("e1 should be assigned to train", train, e1.getTrain());
        Assert.assertEquals("e2 should be assigned to train", train, e2.getTrain());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testAutoEnginesGrade() {

        Train train = tmanager.newTrain("AutoEngineTest");
        train.setNumberEngines(Train.AUTO);

        Route route = JUnitOperationsUtil.createThreeLocationRoute();
        train.setRoute(route);

        RouteLocation rA = route.getDepartsRouteLocation();
        RouteLocation rB = route.getRouteLocationBySequenceNumber(2);
        RouteLocation rC = route.getTerminatesRouteLocation();

        // create demand for 4 engines
        rA.setMaxCarMoves(12);
        rB.setMaxCarMoves(12);
        rC.setMaxCarMoves(12);
        rB.setGrade(2.5); // 2.5% grade!

        // place four engines at the start of the route
        Location A = rA.getLocation();
        Track actonYard1 = A.getTrackByName("Acton Yard 1", null);
        Assert.assertNotNull(actonYard1);
        placeFourEngines(actonYard1);

        Consist c = InstanceManager.getDefault(ConsistManager.class).newConsist("c");
        Engine e1 = emanager.getByRoadAndNumber("E", "1");
        Engine e2 = emanager.getByRoadAndNumber("E", "2");
        e1.setConsist(c);
        e2.setConsist(c);

        // train should require four engines
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, needs four engines, only two", train.isBuilt());

        Engine e3 = emanager.getByRoadAndNumber("E", "3");
        e3.setConsist(c);
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, needs four engines, only three", train.isBuilt());

        Engine e4 = emanager.getByRoadAndNumber("E", "4");
        e4.setConsist(c);
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, four engines available", train.isBuilt());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testMaxEngines() {

        Train train = tmanager.newTrain("AutoEngineTest");
        train.setNumberEngines(Train.AUTO);

        Route route = JUnitOperationsUtil.createThreeLocationRoute();
        train.setRoute(route);

        RouteLocation rA = route.getDepartsRouteLocation();
        RouteLocation rB = route.getRouteLocationBySequenceNumber(2);
        RouteLocation rC = route.getTerminatesRouteLocation();

        // create demand for 4 engines
        rA.setMaxCarMoves(12);
        rB.setMaxCarMoves(12);
        rC.setMaxCarMoves(12);
        rB.setGrade(2.5); // 2.5% grade!

        // place four engines at the start of the route
        Location A = rA.getLocation();
        Track actonYard1 = A.getTrackByName("Acton Yard 1", null);
        Assert.assertNotNull(actonYard1);
        placeFourEngines(actonYard1);

        Consist c = InstanceManager.getDefault(ConsistManager.class).newConsist("c");
        Engine e1 = emanager.getByRoadAndNumber("E", "1");
        Engine e2 = emanager.getByRoadAndNumber("E", "2");
        Engine e3 = emanager.getByRoadAndNumber("E", "3");
        Engine e4 = emanager.getByRoadAndNumber("E", "4");
        e1.setConsist(c);
        e2.setConsist(c);
        e3.setConsist(c);
        e4.setConsist(c);

        Setup.setMaxNumberEngines(3); // limit the maximum to three engines
        Assert.assertFalse(train.build());
        Assert.assertFalse("Train should not build, needs four engines, three is the maximum allowed", train.isBuilt());

        // remove one engine from consist, train should build
        c.delete(e4);
        Assert.assertTrue(train.build());
        Assert.assertTrue("Train should build, three engines available", train.isBuilt());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    // test spur and yard moves
    // tests manual setting of destinations and trains for engines, cars, and
    // cabooses.
    // tests consists and kernels
    @Test
    public void testSpursYards() {

        // Create two Cabooses, six Boxcars, and two Flats
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("CP", "10", Bundle.getMessage("Caboose"), "32", null, 10);
        c1.setCaboose(true);
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("CP", "200", Bundle.getMessage("Caboose"), "32", null, 11);
        c2.setCaboose(true);
        Car c3 = JUnitOperationsUtil.createAndPlaceCar("CP", "30", "Boxcar", "40", null, 12);
        Car c4 = JUnitOperationsUtil.createAndPlaceCar("CP", "4000", "Boxcar", "40", null, 13);
        Car c5 = JUnitOperationsUtil.createAndPlaceCar("CP", "5", "Boxcar", "40", null, 14);
        Car c6 = JUnitOperationsUtil.createAndPlaceCar("CP", "60", "Boxcar", "40", null, 15);
        Car c7 = JUnitOperationsUtil.createAndPlaceCar("CP", "700", "Boxcar", "50", null, 16);
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("CP", "8000", "Boxcar", "60", null, 17);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("CP", "9", "Flat", "40", null, 18);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("CP", "1000", "Flat", "40", null, 19);

        // place two engines in a consist
        Consist con1 = InstanceManager.getDefault(ConsistManager.class).newConsist("CP");

        Engine e1 = emanager.newRS("CP", "10");
        e1.setModel("GP30");
        e1.setConsist(con1);
        Engine e2 = emanager.newRS("CP", "20");
        e2.setModel("GP30");
        e2.setConsist(con1);

        // Set up a turn route of 3 locations:
        // Foxboro-Acton-Nashua-Acton-Foxboro
        // Foxboro (2 tracks, yard and spur),
        // Acton (2 tracks, spurs), and Nashua (2 tracks, yards).
        Location foxboro = lmanager.newLocation("Foxboro");
        Assert.assertEquals("Location 1 Name", "Foxboro", foxboro.getName());
        Assert.assertEquals("Location 1 Initial Length", 0, foxboro.getLength());

        Track foxboroSpur = foxboro.addTrack("Foxboro Spur", Track.SPUR);
        foxboroSpur.setLength(600);
        Assert.assertEquals("Location 1s1 Name", "Foxboro Spur", foxboroSpur.getName());
        Assert.assertEquals("Location 1s1 LocType", "Spur", foxboroSpur.getTrackType());
        Assert.assertEquals("Location 1s1 Length", 600, foxboroSpur.getLength());
        Assert.assertEquals("Default directions", Track.EAST + Track.WEST + Track.SOUTH + Track.NORTH,
                foxboroSpur.getTrainDirections());

        Track foxboroYard = foxboro.addTrack("Foxboro Yard", Track.YARD);
        foxboroYard.setLength(400);
        Assert.assertEquals("Location 1s2 Name", "Foxboro Yard", foxboroYard.getName());
        Assert.assertEquals("Location 1s2 LocType", "Yard", foxboroYard.getTrackType());
        Assert.assertEquals("Location 1s2 Length", 400, foxboroYard.getLength());

        Assert.assertEquals("Location 1 Length", 1000, foxboro.getLength());

        Location acton = lmanager.newLocation("Acton");
        Assert.assertEquals("Location 2 Name", "Acton", acton.getName());

        Track actonSpur1 = acton.addTrack("Acton Spur 1", Track.SPUR);
        actonSpur1.setLength(543);
        actonSpur1.setMoves(1);
        Assert.assertEquals("Location 2s1 Name", "Acton Spur 1", actonSpur1.getName());
        Assert.assertEquals("Location 2s1 LocType", Track.SPUR, actonSpur1.getTrackType());
        Assert.assertEquals("Location 2s1 Length", 543, actonSpur1.getLength());

        Track actonSpur2 = acton.addTrack("Acton Spur 2", Track.SPUR);
        actonSpur2.setLength(345);

        Assert.assertEquals("Acton Length", 888, acton.getLength());

        Location nashua = lmanager.newLocation("Nashua");
        Track nashuaYard1 = nashua.addTrack("Nashua Yard 1", Track.YARD);
        nashuaYard1.setLength(301);

        Track nashuaYard2 = nashua.addTrack("Nashua Yard 2", Track.YARD);
        nashuaYard2.setLength(402);

        Assert.assertEquals("Location 3 Length", 703, nashua.getLength());

        // define the route. A three location turn
        Setup.setCarMoves(6); // set default to 6 moves per location
        Route r1 = rmanager.newRoute("Foxboro-Acton-Nashua-Acton-Foxboro");
        RouteLocation rl1 = r1.addLocation(foxboro);
        rl1.setTrainIconX(25); // set the train icon coordinates
        rl1.setTrainIconY(225);
        RouteLocation rl2 = r1.addLocation(acton);
        rl2.setTrainIconX(75); // set the train icon coordinates
        rl2.setTrainIconY(225);
        RouteLocation rl3 = r1.addLocation(nashua);
        rl3.setTrainIconX(125); // set the train icon coordinates
        rl3.setTrainIconY(225);
        RouteLocation rl4 = r1.addLocation(acton);
        rl4.setTrainIconX(175); // set the train icon coordinates
        rl4.setTrainIconY(225);
        RouteLocation rl5 = r1.addLocation(foxboro);
        rl5.setTrainIconX(225); // set the train icon coordinates
        rl5.setTrainIconY(225);
        rl5.setPickUpAllowed(false); // don't include cars at destination

        // define the train
        Train t1 = tmanager.newTrain("FF");
        t1.setRoute(r1);

        // request helper engines to improve test coverage
        t1.setSecondLegOptions(Train.HELPER_ENGINES);
        t1.setSecondLegStartRouteLocation(rl1);
        t1.setSecondLegEndRouteLocation(rl2);

        t1.setThirdLegOptions(Train.HELPER_ENGINES);
        t1.setThirdLegStartRouteLocation(rl3);
        t1.setThirdLegEndRouteLocation(rl4);

        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 1", t1.isBuilt());
        Assert.assertEquals("should be 0 cars", 0, t1.getNumberCarsWorked());

        // place the cars on the tracks
        Assert.assertEquals("Place c1", Track.OKAY, c1.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c2", Track.OKAY, c2.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c3", Track.OKAY, c3.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c4", Track.OKAY, c4.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c5", Track.OKAY, c5.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c6", Track.OKAY, c6.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c7", Track.OKAY, c7.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c8", Track.OKAY, c8.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c9", Track.OKAY, c9.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c10", Track.OKAY, c10.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(foxboro, foxboroSpur));

        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 2", t1.isBuilt());
        Assert.assertEquals("should be 6 cars", 6, t1.getNumberCarsWorked());

        // check car destinations
        Assert.assertEquals("Destination c1", "", c1.getDestinationName());
        Assert.assertEquals("Destination c2", "", c2.getDestinationName());
        Assert.assertEquals("Destination c3", "Acton", c3.getDestinationName());
        Assert.assertEquals("Destination c4", "Nashua", c4.getDestinationName());
        Assert.assertEquals("Destination c5", "Acton", c5.getDestinationName());
        Assert.assertEquals("Destination c6", "Nashua", c6.getDestinationName());
        Assert.assertEquals("Destination c7", "Acton", c7.getDestinationName());
        Assert.assertEquals("Destination c8", "Nashua", c8.getDestinationName());
        Assert.assertEquals("Destination c9", "", c9.getDestinationName());
        Assert.assertEquals("Destination c10", "", c10.getDestinationName());
        Assert.assertEquals("Destination e1", "", e1.getDestinationName());

        // release cars from train
        Assert.assertTrue("reset train", t1.reset());
        Assert.assertEquals("Train reset", Train.CODE_TRAIN_RESET, t1.getStatusCode());
        // set c3, c5, c6, c8, c10 destination to be Nashua
        c3.setDestination(nashua, nashuaYard1);
        c5.setDestination(nashua, nashuaYard1);
        c6.setDestination(nashua, nashuaYard1);
        c8.setDestination(nashua, nashuaYard1);
        c10.setDestination(nashua, nashuaYard1);
        // set c5 and c9 to be serviced by train TT
        Train t2 = tmanager.newTrain("TT");
        c5.setTrain(t2);
        c9.setTrain(t2);
        // set c6 to be serviced by train FF
        c6.setTrain(t1);

        // require a caboose
        t1.setRequirements(Train.CABOOSE);
        t1.setShowArrivalAndDepartureTimes(false); // increase test coverage
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 3", t1.isBuilt());

        // check car destinations
        Assert.assertEquals("2 Destination  c1", "Foxboro", c1.getDestinationName());
        Assert.assertEquals("2 Destination  c2", "", c2.getDestinationName());
        Assert.assertEquals("2 Destination  c3", "Nashua", c3.getDestinationName());
        Assert.assertEquals("2 Destination  c4", "Acton", c4.getDestinationName());
        Assert.assertEquals("2 Destination  c5", "Nashua", c5.getDestinationName());
        Assert.assertEquals("2 Destination  c6", "Nashua", c6.getDestinationName());
        Assert.assertEquals("2 Destination  c7", "Acton", c7.getDestinationName());
        Assert.assertEquals("2 Destination  c8", "Nashua", c8.getDestinationName());
        Assert.assertEquals("2 Destination  c9", "", c9.getDestinationName());
        Assert.assertEquals("2 Destination  c10", "Nashua", c10.getDestinationName());

        // move and terminate the train
        t1.move(); // to Acton
        Assert.assertEquals("confirm train location", "Acton", t1.getCurrentLocationName());
        t1.move(); // to Nashua
        Assert.assertEquals("confirm train location", "Nashua", t1.getCurrentLocationName());
        t1.move(); // to Acton
        Assert.assertEquals("confirm train location", "Acton", t1.getCurrentLocationName());
        t1.move(); // to Foxboro
        Assert.assertEquals("confirm train location", "Foxboro", t1.getCurrentLocationName());
        t1.move(); // terminate

        // check car destinations
        Assert.assertEquals("3 Destination c1", "", c1.getDestinationName());
        Assert.assertEquals("3 Destination c2", "", c2.getDestinationName());
        Assert.assertEquals("3 Destination c3", "", c3.getDestinationName());
        Assert.assertEquals("3 Destination c4", "", c4.getDestinationName());
        Assert.assertEquals("3 Destination c5", "Nashua", c5.getDestinationName());
        Assert.assertEquals("3 Destination c6", "", c6.getDestinationName());
        Assert.assertEquals("3 Destination c7", "", c7.getDestinationName());
        Assert.assertEquals("3 Destination c8", "", c8.getDestinationName());
        Assert.assertEquals("3 Destination c9", "", c9.getDestinationName());
        Assert.assertEquals("3 Destination c10", "Nashua", c10.getDestinationName());

        // check car locations
        Assert.assertEquals("Location c1", "Foxboro", c1.getLocationName());
        Assert.assertEquals("Location c2", "Foxboro", c2.getLocationName());
        Assert.assertEquals("Location c3", "Nashua", c3.getLocationName());
        Assert.assertEquals("Location c4", "Acton", c4.getLocationName());
        Assert.assertEquals("Location c5", "Foxboro", c5.getLocationName());
        Assert.assertEquals("Location c6", "Nashua", c6.getLocationName());
        Assert.assertEquals("Location c7", "Acton", c7.getLocationName());
        Assert.assertEquals("Location c8", "Nashua", c8.getLocationName());
        Assert.assertEquals("Location c9", "Foxboro", c9.getLocationName());
        Assert.assertEquals("Location c10", "Foxboro", c10.getLocationName());

        // now set caboose destinations that aren't the terminal
        Assert.assertEquals("set destination c1", Track.OKAY, c1.setDestination(acton, actonSpur1));
        Assert.assertEquals("set destination c2", Track.OKAY, c2.setDestination(nashua, nashuaYard1));

        // train requires a caboose, should fail
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train built 4", t1.isBuilt());

        // Set caboose destination to be the terminal
        Assert.assertEquals("set caboose destination", Track.OKAY, c2.setDestination(foxboro, foxboroYard));
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 5", t1.isBuilt());
        Assert.assertTrue("train reset 5", t1.reset());

        // set the cabooses to train FF
        c1.setTrain(t2);
        c2.setTrain(t2);

        // build should fail
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train built 6", t1.isBuilt());

        // set caboose to train TT
        c1.setTrain(t1);
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 7", t1.isBuilt());

        // check car destinations
        Assert.assertEquals("4 Destination c1", "Foxboro", c1.getDestinationName());
        Assert.assertEquals("4 Destination c2", "", c2.getDestinationName());
        Assert.assertEquals("4 Destination c3", "Acton", c3.getDestinationName());
        Assert.assertEquals("4 Destination c4", "Nashua", c4.getDestinationName());
        Assert.assertEquals("4 Destination c5", "Nashua", c5.getDestinationName());
        Assert.assertEquals("4 Destination c6", "Foxboro", c6.getDestinationName());
        Assert.assertEquals("4 Destination c7", "Nashua", c7.getDestinationName());
        Assert.assertEquals("4 Destination c8", "Foxboro", c8.getDestinationName());
        Assert.assertEquals("4 Destination c9", "", c9.getDestinationName());
        Assert.assertEquals("4 Destination c10", "Acton", c10.getDestinationName());

        // add an engine
        t1.setNumberEngines("2");
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 8", t1.isBuilt());
        Assert.assertEquals("5 Destination e1", "Foxboro", e1.getDestinationName());
        Assert.assertEquals("5 Destination e2", "Foxboro", e2.getDestinationName());

        t1.reset();
        // assign lead engine to train TT
        e1.setTrain(t2);
        // should fail
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train built 9", t1.isBuilt());

        // assign one of the consist engine to train TT
        e1.setTrain(t1);
        e2.setTrain(t2); // shouldn't pay attention to the other engine
        // should build
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 10", t1.isBuilt());
        t1.reset();

        // both engines should release
        Assert.assertEquals("6 Destination e1", "", e1.getDestinationName());
        Assert.assertEquals("6 Train e1", "", e1.getTrainName());
        Assert.assertEquals("6 Destination e2", "", e2.getDestinationName());
        Assert.assertEquals("6 Train e2", "", e2.getTrainName());

        // now try setting engine destination that isn't the terminal
        Assert.assertEquals("set destination e1", Track.OKAY, e1.setDestination(acton, actonSpur1));
        // should fail
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train built 11", t1.isBuilt());

        e1.setDestination(foxboro, foxboroYard);
        e2.setDestination(acton, actonSpur1); // program should ignore
        // should build
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 12", t1.isBuilt());

        // set lead engine's track to null
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(foxboro, null));
        // should not build
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train will not build engine track is null", t1.isBuilt());
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(foxboro, foxboroSpur));

        // should now build
        Assert.assertTrue(t1.build());

        // move and terminate the train
        t1.move(); // to Acton
        t1.move(); // to Nashua
        t1.move(); // to Acton
        t1.move(); // to Foxboro
        t1.move(); // terminate

        // check engine final locations
        Assert.assertEquals("Location e1", "Foxboro", e1.getLocationName());
        Assert.assertEquals("Location e2", "Foxboro", e2.getLocationName());

        // move c7 & c8 to Foxboro to help test kernels
        Assert.assertEquals("Place c7", Track.OKAY, c7.setLocation(foxboro, foxboroSpur));
        Assert.assertEquals("Place c8", Track.OKAY, c8.setLocation(foxboro, foxboroSpur));
        // now test kernels
        Kernel k1 = InstanceManager.getDefault(KernelManager.class).newKernel("group of cars");
        c8.setKernel(k1); // lead car
        c7.setKernel(k1);
        c7.setTrain(t2); // program should ignore
        c3.setLocation(foxboro, foxboroSpur);
        c3.setKernel(k1);
        c3.setDestination(foxboro, foxboroSpur); // program should ignore (produces debug warning)

        // should build
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 12", t1.isBuilt());
        // Confirm partial build
        Assert.assertEquals("Train built", Train.CODE_PARTIAL_BUILT, t1.getStatusCode());
        Assert.assertEquals("12 Location c3", "Foxboro", c3.getLocationName());
        Assert.assertEquals("12 Location c7", "Foxboro", c7.getLocationName());
        Assert.assertEquals("12 Location c8", "Foxboro", c8.getLocationName());
        Assert.assertEquals("12 Destination c3", "Nashua", c3.getDestinationName());
        Assert.assertEquals("12 Destination c7", "Nashua", c7.getDestinationName());
        Assert.assertEquals("12 Destination c8", "Nashua", c8.getDestinationName());

        // move and terminate the train
        t1.move(); // to Acton
        Assert.assertEquals("Train en route", Train.CODE_TRAIN_EN_ROUTE, t1.getStatusCode());
        t1.move(); // to Nashua
        Assert.assertEquals("Train en route", Train.CODE_TRAIN_EN_ROUTE, t1.getStatusCode());
        t1.move(); // to Acton
        Assert.assertEquals("Train en route", Train.CODE_TRAIN_EN_ROUTE, t1.getStatusCode());
        t1.move(); // to Foxboro
        Assert.assertEquals("Train en route", Train.CODE_TRAIN_EN_ROUTE, t1.getStatusCode());
        t1.move(); // terminate
        Assert.assertEquals("Train Terminated", Train.CODE_TERMINATED, t1.getStatusCode());

        Assert.assertEquals("13 Location c3", "Nashua", c3.getLocationName());
        Assert.assertEquals("13 Location c7", "Nashua", c7.getLocationName());
        Assert.assertEquals("13 Location c8", "Nashua", c8.getLocationName());
        Assert.assertEquals("13 Destination c3", "", c3.getDestinationName());
        Assert.assertEquals("13 Destination c7", "", c7.getDestinationName());
        Assert.assertEquals("13 Destination c8", "", c8.getDestinationName());
        Assert.assertEquals("13 Train c3", "", c3.getTrainName());
        Assert.assertEquals("13 Train c7", "", c7.getTrainName());
        Assert.assertEquals("13 Train c8", "", c8.getTrainName());

        // now test car with FRED
        c1.setCaboose(false);
        c2.setCaboose(false);
        c1.setFred(true);
        c2.setFred(true);
        c1.setTypeName("Boxcar"); // change the type, now Boxcar with FRED
        c2.setTypeName("Boxcar");
        c2.setTrain(null);

        // train requires a caboose, there are none, should fail
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train built 14", t1.isBuilt());

        // change requirement to car with FRED
        t1.setRequirements(Train.FRED);

        // train requires a car with FRED, should pass
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 15", t1.isBuilt());
        Assert.assertTrue("train reset 15", t1.reset()); // release cars

        // now set FRED destinations that aren't the terminal
        Assert.assertEquals("set destination c1", Track.OKAY, c1.setDestination(acton, actonSpur1));
        Assert.assertEquals("set destination c2", Track.OKAY, c2.setDestination(nashua, nashuaYard1));

        // train requires a car with FRED, should fail
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train built 16", t1.isBuilt());

        // Set FRED destination to be the terminal
        Assert.assertEquals("set destination c2", Track.OKAY, c2.setDestination(foxboro, foxboroYard));
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 17", t1.isBuilt());
        Assert.assertTrue("train reset 17", t1.reset());

        // set the cars with FRED to train FF
        c1.setTrain(t2);
        c2.setTrain(t2);

        // build should fail
        Assert.assertFalse(t1.build());
        Assert.assertFalse("train built 18", t1.isBuilt());

        // set car with FRED to train TT
        c2.setTrain(t1);
        Assert.assertTrue(t1.build());
        Assert.assertTrue("train built 19", t1.isBuilt());

        // test car out of service
        c2.setOutOfService(true);
        Assert.assertFalse(t1.build());
        Assert.assertFalse("required car is out of service", t1.isBuilt());

        // test location unknown
        c2.setOutOfService(false);
        c2.setLocationUnknown(true);
        Assert.assertFalse(t1.build());
        Assert.assertFalse("required car location is unknown", t1.isBuilt());

        c2.setLocationUnknown(false);
        Assert.assertTrue(t1.build());
        Assert.assertTrue("need car is available", t1.isBuilt());

        c2.setWait(1);
        Assert.assertFalse(t1.build());
        Assert.assertFalse("required car will wait for next train", t1.isBuilt());

        Assert.assertTrue(t1.build());
        Assert.assertTrue("next train!", t1.isBuilt());
        Assert.assertEquals("CP 4000 destination", "Nashua", c4.getDestinationName());

        // test train and location direction controls
        c8.setLocation(acton, actonSpur1); // place led car of kernel in Action Spur 1
        acton.setTrainDirections(Location.EAST + Location.SOUTH + Location.WEST); // train is north bound

        // build should fail, cars c3 and c7 which is part of c8 kernel are on the wrong
        // track
        Assert.assertFalse(t1.build());
        Assert.assertFalse("Train direction test", t1.isBuilt());
        c3.setLocation(acton, actonSpur1); // place c3 Action Spur 1
        c7.setLocation(acton, actonSpur1); // place c7 Action Spur 1

        Assert.assertTrue(t1.build());
        Assert.assertTrue("Train direction test", t1.isBuilt());
        Assert.assertEquals("CP 1000 destination", "Foxboro", c10.getDestinationName());
        Assert.assertEquals("CP 30 at Acton, not serviced", null, c3.getTrain());
        Assert.assertEquals("CP 700 at Acton, not serviced", null, c7.getTrain());
        Assert.assertEquals("CP 8000 at Acton, not serviced", null, c8.getTrain());

        // restore Acton
        acton.setTrainDirections(Location.NORTH); // train is north bound
        Assert.assertTrue(t1.build());

        Assert.assertEquals("CP 1000 destination is now", "Acton", c10.getDestinationName());
        Assert.assertEquals("CP 30 at Acton", t1, c3.getTrain());
        Assert.assertEquals("CP 700 at Acton", t1, c7.getTrain());
        Assert.assertEquals("CP 8000 at Acton", t1, c8.getTrain());

        // restrict train direction at the track level
        actonSpur2.setTrainDirections(Track.EAST + Track.SOUTH + Track.WEST);
        // take one car out of kernel
        c3.setKernel(null);
        c3.setLocation(acton, actonSpur1); // place car in Action Spur 1
        c8.setLocation(acton, actonSpur2); // place lead car in Action Yard
        c7.setLocation(acton, actonSpur2); // place c7 in Action Yard
        Assert.assertTrue(t1.build());

        Assert.assertEquals("CP 1000 destination track", "Acton Spur 1", c1.getDestinationTrackName());
        Assert.assertEquals("CP 200 at Foxboro Spur", t1, c2.getTrain());
        Assert.assertEquals("CP 30 at Acton Spur 1", t1, c3.getTrain());
        Assert.assertEquals("CP 30 destination track", "Nashua Yard 2", c3.getDestinationTrackName());
        Assert.assertEquals("CP 4000 at Foxboro Spur", t1, c4.getTrain());
        Assert.assertEquals("CP 4000 destination", "Nashua", c4.getDestinationName());
        Assert.assertEquals("CP 4000 destination track", "Nashua Yard 2", c4.getDestinationTrackName());
        Assert.assertEquals("CP 60 destination track", "", c6.getDestinationTrackName());
        Assert.assertEquals("CP 700 at Acton, not serviced, part of kernel CP 8000", null, c7.getTrain());
        Assert.assertEquals("CP 8000 at Acton, Acton Spur 2 not serviced", null, c8.getTrain());

        // test train length restrictions
        rl1.setMaxTrainLength(155); // only enough for the two engines, train needs a car with FRED
        Assert.assertFalse(t1.build());
        Assert.assertFalse("Train length test, can't service car with FRED", t1.isBuilt());

        // build failed after engines were assigned to train 1
        Assert.assertEquals("Engine assignment ignores train length restrictions", null, e1.getTrain());
        Assert.assertEquals("Engine assignment ignores train length restrictions", null, e2.getTrain());

        Assert.assertEquals("Check CP30 engine length", "56", e1.getLength());
        Assert.assertEquals("Check CP 200 length", "32", c2.getLength());
        rl1.setMaxTrainLength(156); // enough for the two engines and a car with FRED 56 + 56 + 32 + 12(couplers) =
                                    // 156

        Assert.assertTrue(t1.build());
        Assert.assertTrue("Train length test, just enough length for engines and car with FRED", t1.isBuilt());
        Assert.assertEquals("CP 200 at Foxboro Spur", t1, c2.getTrain());
        Assert.assertEquals("CP 200 destination track", "Foxboro Spur", c2.getDestinationTrackName());
        Assert.assertEquals("CP 30 at Acton Spur 1", t1, c3.getTrain());
        Assert.assertEquals("CP 30 destination track", "Nashua Yard 2", c3.getDestinationTrackName());
        Assert.assertEquals("CP 4000 at Foxboro Spur", t1, c4.getTrain());
        Assert.assertEquals("CP 60 destination track", "", c6.getDestinationTrackName());
        Assert.assertEquals("CP 700 at Acton, not serviced, part of kernel CP 8000", null, c7.getTrain());
        Assert.assertEquals("CP 8000 at Acton, Acton Spur 2 not serviced", null, c8.getTrain());
        Assert.assertEquals("CP 1000 ", t1, c10.getTrain());

        // Increase the train length from the departure location
        rl1.setMaxTrainLength(1000);
        rl2.setMaxTrainLength(156); // restrict train length from Acton
        Assert.assertTrue(t1.build());
        Assert.assertTrue("Train length test, just enough length for engines and car with FRED", t1.isBuilt());
        Assert.assertEquals("CP 200 at Foxboro Spur", t1, c2.getTrain());
        Assert.assertEquals("CP 200 destination track", "Foxboro Spur", c2.getDestinationTrackName());
        Assert.assertEquals("CP 30 at Acton Spur 1", t1, c3.getTrain());
        Assert.assertEquals("CP 30 destination track", "Foxboro Spur", c3.getDestinationTrackName());
        Assert.assertEquals("CP 4000 at Foxboro Yard", t1, c4.getTrain());
        Assert.assertEquals("CP 4000 destination track", "Foxboro Yard", c4.getDestinationTrackName());
        Assert.assertEquals("CP 60 destination track", "", c6.getDestinationTrackName());
        Assert.assertEquals("CP 1000 part of train", t1, c10.getTrain());
        Assert.assertEquals("CP 1000 destination track", "Acton Spur 1", c10.getDestinationTrackName());

        // test setting car's destination to Foxboro Spur
        c2.setDestination(foxboro, foxboroSpur);
        Assert.assertTrue(t1.build());
        Assert.assertTrue("car with FRED has destination", t1.isBuilt());
        t1.reset();

        // again, but now change car type serviced by Foxboro Yard
        c2.setDestination(foxboro, foxboroSpur);
        foxboroSpur.deleteTypeName("Boxcar");
        Assert.assertFalse(t1.build());
        Assert.assertFalse("car with FRED has destination that won't accept it", t1.isBuilt());
        foxboroSpur.addTypeName("Boxcar");

        c6.setDestination(acton, actonSpur2); // destination Action Spur 2
        actonSpur2.deleteTypeName("Boxcar"); // don't allow Boxcar to drop
        Assert.assertTrue(t1.build());
        Assert.assertTrue("car with FRED has destination that will now accept it", t1.isBuilt());
        Assert.assertEquals("CP 60 can't be delivered", null, c6.getTrain());

        c2.setLocation(foxboro, null);
        Assert.assertFalse(t1.build());
        Assert.assertFalse("need car doesn't have a track assignment", t1.isBuilt());

        JUnitOperationsUtil.checkOperationsShutDownTask();
        // end testSpursYards
    }

    /**
     * Test a route of one location (local train). Locations that don't have a train
     * direction assigned can only be served by a local train. Creates two locations
     * Westford and Chelmsford and 9 cars. Westford has 2 yards, 2 spurs, 3
     * interchange tracks. Chelmsford has 1 yard. Chelmsford is used to test that a
     * train with two locations will not service certain tracks.
     */
    @Test
    public void testLocal() {

        // Create locations used
        Location westford = lmanager.newLocation("Westford");
        Location chelmsford = lmanager.newLocation("Chelmsford");

        Track westfordYard1;
        westfordYard1 = westford.addTrack("Westford Yard 1", Track.YARD);
        westfordYard1.setTrainDirections(Track.WEST + Track.EAST);
        westfordYard1.setLength(500);

        Track westfordYard2;
        westfordYard2 = westford.addTrack("Westford Yard 2", Track.YARD);
        westfordYard2.setTrainDirections(Track.WEST + Track.EAST);
        westfordYard2.setLength(500);

        Track westfordSpur3;
        westfordSpur3 = westford.addTrack("Westford Spur 3", Track.SPUR);
        westfordSpur3.setTrainDirections(0); // Only local moves allowed
        westfordSpur3.setLength(300);

        Track westfordSpur4;
        westfordSpur4 = westford.addTrack("Westford Spur 4", Track.SPUR);
        westfordSpur4.setTrainDirections(0); // Only local moves allowed
        westfordSpur4.setLength(300);

        Track westfordInterchange5;
        westfordInterchange5 = westford.addTrack("Westford Interchange 5", Track.INTERCHANGE);
        westfordInterchange5.setTrainDirections(0); // Only local moves allowed
        westfordInterchange5.setLength(300);

        Track westfordInterchange6;
        westfordInterchange6 = westford.addTrack("Westford Interchange 6", Track.INTERCHANGE);
        westfordInterchange6.setTrainDirections(Track.WEST + Track.EAST);
        westfordInterchange6.setLength(300);

        Track westfordInterchange7;
        westfordInterchange7 = westford.addTrack("Westford Interchange 7", Track.INTERCHANGE);
        westfordInterchange7.setTrainDirections(0); // Only local moves allowed
        westfordInterchange7.setLength(300);

        Track chelmsfordYard1;
        chelmsfordYard1 = chelmsford.addTrack("Chelmsford Yard 1", Track.YARD);
        chelmsfordYard1.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordYard1.setLength(900);

        // now bias track selection by moves
        westfordYard1.setMoves(3); // no yard to yard moves expected
        westfordYard2.setMoves(4); // no yard to yard moves expected
        westfordSpur3.setMoves(10); // this will be the 5th location assigned
        westfordSpur4.setMoves(10); // this will be the 6th location assigned
        westfordInterchange5.setMoves(9); // this will be the 2nd location assigned
        westfordInterchange6.setMoves(9); // this will be the 3rd location assigned
        westfordInterchange7.setMoves(8); // this will be the first and 4th location assigned

        // Create route with only one location
        Route rte1 = rmanager.newRoute("Local Route");
        Setup.setCarMoves(7); // set the default moves to 7
        RouteLocation rl1 = rte1.addLocation(westford);

        // Create train
        Train train1 = tmanager.newTrain("Local Train");
        train1.setRoute(rte1);

        // Set up 8 box cars and 1 flat cars

        // should be the 7th car assigned to train
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("BM", "1", "Boxcar", "90", westfordYard1, 17);

        // should be the 6th car assigned to train
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("CP", "2", "Boxcar", "80", westfordYard1, 15);

        // should be the 1st car assigned to train
        Car c3 = JUnitOperationsUtil.createAndPlaceCar("XP", "3", "Flat Car", "70", westfordYard1, 0);

        // should be the 5th car assigned to train
        Car c4 = JUnitOperationsUtil.createAndPlaceCar("UP", "4", "Boxcar", "60", westfordYard1, 6);

        // should be the 2nd car assigned to train
        Car c5 = JUnitOperationsUtil.createAndPlaceCar("UP", "5", "Boxcar", "50", westfordYard2, 1);

        // should be the 4th car assigned to train
        Car c6 = JUnitOperationsUtil.createAndPlaceCar("CP", "6", "Boxcar", "40", westfordYard2, 3);
        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "7", "Boxcar", "50", westfordYard2, 18);

        // should be the 3rd car assigned to train
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("XP", "8", "Boxcar", "60", westfordYard2, 2);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("XP", "9", "Boxcar", "90", westfordSpur3, 19);

        // do cars have the right default loads?
        Assert.assertEquals("Car c1 load should be E", "E", c1.getLoadName());
        Assert.assertEquals("Car c2 load should be E", "E", c2.getLoadName());
        Assert.assertEquals("Car c3 load should be E", "E", c3.getLoadName());
        Assert.assertEquals("Car c4 load should be E", "E", c4.getLoadName());
        Assert.assertEquals("Car c5 load should be E", "E", c5.getLoadName());
        Assert.assertEquals("Car c6 load should be E", "E", c6.getLoadName());
        Assert.assertEquals("Car c7 load should be E", "E", c7.getLoadName());
        Assert.assertEquals("Car c8 load should be E", "E", c8.getLoadName());
        Assert.assertEquals("Car c9 load should be E", "E", c9.getLoadName());

        // Build train
        Assert.assertTrue(train1.build());

        // check train status
        Assert.assertEquals("Train 1 built", Train.CODE_BUILT, train1.getStatusCode());

        Assert.assertEquals("Train 1 After Build Departs Name", "Westford", train1.getTrainDepartsName());
        Assert.assertEquals("Train 1 After Build Terminates Name", "Westford", train1.getTrainTerminatesName());
        Assert.assertEquals("Train 1 After Build Next Location Name", "", train1.getNextLocationName());
        Assert.assertEquals("Train 1 After Build Built Status", true, train1.isBuilt());

        // are the right cars assigned to the train?
        // the default moves is 7, therefore only 7 cars should be moved based on their
        // move counts
        Assert.assertEquals("Car c1 After Build should be assigned to Train 1", train1, c1.getTrain());
        Assert.assertEquals("Car c2 After Build should be assigned to Train 1", train1, c2.getTrain());
        Assert.assertEquals("Car c3 After Build should be assigned to Train 1", train1, c3.getTrain());
        Assert.assertEquals("Car c4 After Build should NOT be assigned to Train 1", train1, c4.getTrain());
        Assert.assertEquals("Car c5 After Build should be assigned to Train 1", train1, c5.getTrain());
        Assert.assertEquals("Car c6 After Build should be assigned to Train 1", train1, c6.getTrain());
        Assert.assertEquals("Car c7 After Build should NOT be assigned to Train 1", null, c7.getTrain());
        Assert.assertEquals("Car c8 After Build should be assigned to Train 1", train1, c8.getTrain());
        Assert.assertEquals("Car c9 After Build should NOT be assigned to Train 1", null, c9.getTrain());

        // now check to see if cars are going to be delivered to the right places?
        Assert.assertEquals("Car c1 After Build destination", "Westford Interchange 5", c1.getDestinationTrackName());
        Assert.assertEquals("Car c2 After Build destination", "Westford Spur 4", c2.getDestinationTrackName());
        Assert.assertEquals("Car c3 After Build destination", "Westford Interchange 7", c3.getDestinationTrackName());
        Assert.assertEquals("Car c4 After Build destination", "Westford Spur 3", c4.getDestinationTrackName());
        Assert.assertEquals("Car c5 After Build destination", "Westford Interchange 5", c5.getDestinationTrackName());
        Assert.assertEquals("Car c6 After Build destination", "Westford Interchange 7", c6.getDestinationTrackName());
        Assert.assertEquals("Car c8 After Build destination", "Westford Interchange 6", c8.getDestinationTrackName());

        // are the pickup and drop counts correct?
        Assert.assertEquals("Drop count for Westford", 7, westford.getDropRS());
        Assert.assertEquals("Drop count for Westford track Westford Yard 1", 0, westfordYard1.getDropRS());
        Assert.assertEquals("Drop count for Westford track Westford Yard 2", 0, westfordYard2.getDropRS());
        Assert.assertEquals("Drop count for Westford track Westford Spur 3", 1, westfordSpur3.getDropRS());
        Assert.assertEquals("Drop count for Westford track Westford Spur 4", 1, westfordSpur4.getDropRS());
        Assert.assertEquals("Drop count for Westford track Westford Interchange 5", 2,
                westfordInterchange5.getDropRS());
        Assert.assertEquals("Drop count for Westford track Westford Interchange 6", 1,
                westfordInterchange6.getDropRS());
        Assert.assertEquals("Drop count for Westford track Westford Interchange 7", 2,
                westfordInterchange7.getDropRS());
        Assert.assertEquals("Pickup count for Westford", 7, westford.getPickupRS());
        Assert.assertEquals("Pickup count for Westford track Westford Yard 1", 4, westfordYard1.getPickupRS());
        Assert.assertEquals("Pickup count for Westford track Westford Yard 2", 3, westfordYard2.getPickupRS());
        Assert.assertEquals("Pickup count for Westford track Westford Spur 3", 0, westfordSpur3.getPickupRS());
        Assert.assertEquals("Pickup count for Westford track Westford Spur 4", 0, westfordSpur4.getPickupRS());
        Assert.assertEquals("Pickup count for Westford track Westford Interchange 5", 0,
                westfordInterchange5.getPickupRS());
        Assert.assertEquals("Pickup count for Westford track Westford Interchange 6", 0,
                westfordInterchange6.getPickupRS());
        Assert.assertEquals("Pickup count for Westford track Westford Interchange 7", 0,
                westfordInterchange7.getPickupRS());

        train1.move();
        // Train should not be in route since there's only one location
        Assert.assertEquals("Train 1 not in route", false, train1.isTrainEnRoute());
        // check train status
        Assert.assertEquals("Train 1 not en route", Train.TERMINATED, getTrainStatus(train1));
        Assert.assertEquals("Train 1 not en route", Train.CODE_TERMINATED, train1.getStatusCode());
        // check for correct tracks
        Assert.assertEquals("Car c1 After Move location", "Westford Interchange 5", c1.getTrackName());
        Assert.assertEquals("Car c2 After Move location", "Westford Spur 4", c2.getTrackName());
        Assert.assertEquals("Car c3 After Move location", "Westford Interchange 7", c3.getTrackName());
        Assert.assertEquals("Car c4 After Move location", "Westford Spur 3", c4.getTrackName());
        Assert.assertEquals("Car c5 After Move location", "Westford Interchange 5", c5.getTrackName());
        Assert.assertEquals("Car c6 After Move location", "Westford Interchange 7", c6.getTrackName());
        Assert.assertEquals("Car c8 After Move location", "Westford Interchange 6", c8.getTrackName());

        // do cars have the right loads?
        Assert.assertEquals("Car c1 After Move load should be E", "E", c1.getLoadName());
        Assert.assertEquals("Car c2 After Move load should be L", "L", c2.getLoadName());
        Assert.assertEquals("Car c3 After Move load should be E", "E", c3.getLoadName());
        Assert.assertEquals("Car c4 After Move load should be L", "L", c4.getLoadName());
        Assert.assertEquals("Car c5 After Move load should be E", "E", c5.getLoadName());
        Assert.assertEquals("Car c6 After Move load should be E", "E", c6.getLoadName());
        Assert.assertEquals("Car c7 After Move load should be E", "E", c7.getLoadName());
        Assert.assertEquals("Car c8 After Move load should be E", "E", c8.getLoadName());
        Assert.assertEquals("Car c9 After Move load should be E", "E", c9.getLoadName());

        // are the pickup and drop counts correct?
        Assert.assertEquals("Move 1 Drop count for Westford", 0, westford.getDropRS());
        Assert.assertEquals("Move 1 Drop count for Westford track Westford Yard 1", 0, westfordYard1.getDropRS());
        Assert.assertEquals("Move 1 Drop count for Westford track Westford Yard 2", 0, westfordYard2.getDropRS());
        Assert.assertEquals("Move 1 Drop count for Westford track Westford Spur 3", 0, westfordSpur3.getDropRS());
        Assert.assertEquals("Move 1 Drop count for Westford track Westford Spur 4", 0, westfordSpur4.getDropRS());
        Assert.assertEquals("Move 1 Drop count for Westford track Westford Interchange 5", 0,
                westfordInterchange5.getDropRS());
        Assert.assertEquals("Move 1 Drop count for Westford track Westford Interchange 6", 0,
                westfordInterchange6.getDropRS());
        Assert.assertEquals("Move 1 Drop count for Westford track Westford Interchange 7", 0,
                westfordInterchange7.getDropRS());
        Assert.assertEquals("Move 1 Pickup count for Westford", 0, westford.getPickupRS());
        Assert.assertEquals("Move 1 Pickup count for Westford track Westford Yard 1", 0, westfordYard1.getPickupRS());
        Assert.assertEquals("Move 1 Pickup count for Westford track Westford Yard 2", 0, westfordYard2.getPickupRS());
        Assert.assertEquals("Move 1 Pickup count for Westford track Westford Spur 3", 0, westfordSpur3.getPickupRS());
        Assert.assertEquals("Move 1 Pickup count for Westford track Westford Spur 4", 0, westfordSpur4.getPickupRS());
        Assert.assertEquals("Move 1 Pickup count for Westford track Westford Interchange 5", 0,
                westfordInterchange5.getPickupRS());
        Assert.assertEquals("Move 1 Pickup count for Westford track Westford Interchange 6", 0,
                westfordInterchange6.getPickupRS());
        Assert.assertEquals("Move 1 Pickup count for Westford track Westford Interchange 7", 0,
                westfordInterchange7.getPickupRS());

        // Verify that an extra move will not change train status.
        train1.move();
        Assert.assertEquals("Train 1 After 2nd Move Status", Train.TERMINATED, getTrainStatus(train1));
        Assert.assertEquals("Train 1 After 2nd Move Status", Train.CODE_TERMINATED, train1.getStatusCode());

        // build the train again, now there are cars on all tracks
        rl1.setMaxCarMoves(10); // try and use all 9/10 of the cars
        Assert.assertTrue(train1.build());
        // c1, c3, c5, c6, c8 are at interchange tracks and should not be assigned to
        // train1
        Assert.assertEquals("Car c1 After Build 2 should NOT be assigned to Train 1", null, c1.getTrain());
        Assert.assertEquals("Car c2 After Build 2 should be assigned to Train 1", train1, c2.getTrain());
        Assert.assertEquals("Car c3 After Build 2 should NOT be assigned to Train 1", null, c3.getTrain());
        Assert.assertEquals("Car c4 After Build 2 should be assigned to Train 1", train1, c4.getTrain());
        Assert.assertEquals("Car c5 After Build 2 should NOT be assigned to Train 1", null, c5.getTrain());
        Assert.assertEquals("Car c6 After Build 2 should NOT be assigned to Train 1", null, c6.getTrain());
        Assert.assertEquals("Car c7 After Build 2 should be assigned to Train 1", train1, c7.getTrain());
        Assert.assertEquals("Car c8 After Build 2 should NOT be assigned to Train 1", null, c8.getTrain());
        Assert.assertEquals("Car c9 After Build 2 should be assigned to Train 1", train1, c9.getTrain());

        // now check to see if cars are going to be delivered to the right places?
        Assert.assertEquals("Car c2 After Build 2 destination", "Westford Yard 1", c2.getDestinationTrackName());
        Assert.assertEquals("Car c4 After Build 2 destination", "Westford Yard 1", c4.getDestinationTrackName());
        Assert.assertEquals("Car c7 After Build 2 destination", "Westford Interchange 6", c7.getDestinationTrackName());
        Assert.assertEquals("Car c9 After Build 2 destination", "Westford Yard 2", c9.getDestinationTrackName());
        // move and terminate
        train1.move();
        Assert.assertEquals("Train 1 After 2nd build Status", Train.TERMINATED, getTrainStatus(train1));
        Assert.assertEquals("Train 1 After 2nd build Status", Train.CODE_TERMINATED, train1.getStatusCode());

        // are cars at the right location?
        Assert.assertEquals("Car c2 After Move 2 location", "Westford Yard 1", c2.getTrackName());
        Assert.assertEquals("Car c4 After Move 2 location", "Westford Yard 1", c4.getTrackName());
        Assert.assertEquals("Car c7 After Move 2 location", "Westford Interchange 6", c7.getTrackName());
        Assert.assertEquals("Car c9 After Move 2 location", "Westford Yard 2", c9.getTrackName());

        // do cars have the right loads?
        Assert.assertEquals("Car c1 After Move 2 load should be E", "E", c1.getLoadName());
        Assert.assertEquals("Car c2 After Move 2 load should be L", "L", c2.getLoadName());
        Assert.assertEquals("Car c3 After Move 2 load should be E", "E", c3.getLoadName());
        Assert.assertEquals("Car c4 After Move 2 load should be L", "L", c4.getLoadName());
        Assert.assertEquals("Car c5 After Move 2 load should be E", "E", c5.getLoadName());
        Assert.assertEquals("Car c6 After Move 2 load should be E", "E", c6.getLoadName());
        Assert.assertEquals("Car c7 After Move 2 load should be E", "E", c7.getLoadName());
        Assert.assertEquals("Car c8 After Move 2 load should be E", "E", c8.getLoadName());
        Assert.assertEquals("Car c9 After Move 2 load should be E", "E", c9.getLoadName());

        // try a new route, this should allow cars to move from interchange
        // Create route with only one location
        Route rte2;
        rte2 = rmanager.newRoute("Local Route 2");
        RouteLocation rl2 = rte2.addLocation(westford);
        rl2.setMaxCarMoves(8); // move 8 of the 9 cars available
        // and assign the new route to train 1
        train1.setRoute(rte2);
        Assert.assertTrue(train1.build());
        // we should be able to pickup cars at the interchange tracks
        Assert.assertEquals("Car c1 After Build 3 should be assigned to Train 1", train1, c1.getTrain());
        Assert.assertEquals("Car c2 After Build 3 should be assigned to Train 1", train1, c2.getTrain());
        Assert.assertEquals("Car c3 After Build 3 should be assigned to Train 1", train1, c3.getTrain());
        Assert.assertEquals("Car c4 After Build 3 should be assigned to Train 1", train1, c4.getTrain());
        Assert.assertEquals("Car c5 After Build 3 should be assigned to Train 1", train1, c5.getTrain());
        Assert.assertEquals("Car c6 After Build 3 should be assigned to Train 1", train1, c6.getTrain());
        Assert.assertEquals("Car c7 After Build 3 should be assigned to Train 1", train1, c7.getTrain());
        Assert.assertEquals("Car c8 After Build 3 should be assigned to Train 1", train1, c8.getTrain());
        Assert.assertEquals("Car c9 After Build 3 should NOT be assigned to Train 1", null, c9.getTrain());

        // now check to see if cars are going to be delivered to the right places?
        Assert.assertEquals("Car c1 After Build 3 destination", "Westford Yard 1", c1.getDestinationTrackName());
        Assert.assertEquals("Car c2 After Build 3 destination", "Westford Spur 3", c2.getDestinationTrackName());
        Assert.assertEquals("Car c3 After Build 3 destination", "Westford Yard 1", c3.getDestinationTrackName());
        Assert.assertEquals("Car c4 After Build 3 destination", "Westford Interchange 7", c4.getDestinationTrackName());
        Assert.assertEquals("Car c5 After Build 3 destination", "Westford Yard 2", c5.getDestinationTrackName());
        Assert.assertEquals("Car c6 After Build 3 destination", "Westford Yard 2", c6.getDestinationTrackName());
        Assert.assertEquals("Car c7 After Build 3 destination", "Westford Yard 2", c7.getDestinationTrackName());
        Assert.assertEquals("Car c8 After Build 3 destination", "Westford Yard 1", c8.getDestinationTrackName());

        // move and terminate
        train1.move();
        Assert.assertEquals("Train 1 After 2nd build Status", Train.TERMINATED, getTrainStatus(train1));
        Assert.assertEquals("Train 1 After 2nd build Status", Train.CODE_TERMINATED, train1.getStatusCode());

        // Final check to see if cars were delivered.
        Assert.assertEquals("Car c1 After Move 3 location", "Westford Yard 1", c1.getTrackName());
        Assert.assertEquals("Car c2 After Move 3 location", "Westford Spur 3", c2.getTrackName());
        Assert.assertEquals("Car c3 After Move 3 location", "Westford Yard 1", c3.getTrackName());
        Assert.assertEquals("Car c4 After Move 3 location", "Westford Interchange 7", c4.getTrackName());
        Assert.assertEquals("Car c5 After Move 3 location", "Westford Yard 2", c5.getTrackName());
        Assert.assertEquals("Car c6 After Move 3 location", "Westford Yard 2", c6.getTrackName());
        Assert.assertEquals("Car c7 After Move 3 location", "Westford Yard 2", c7.getTrackName());
        Assert.assertEquals("Car c8 After Move 3 location", "Westford Yard 1", c8.getTrackName());

        // do cars have the right loads?
        Assert.assertEquals("Car c1 After Move 3 load should be E", "E", c1.getLoadName());
        Assert.assertEquals("Car c2 After Move 3 load should be E", "E", c2.getLoadName());
        Assert.assertEquals("Car c3 After Move 3 load should be E", "E", c3.getLoadName());
        Assert.assertEquals("Car c4 After Move 3 load should be L", "L", c4.getLoadName());
        Assert.assertEquals("Car c5 After Move 3 load should be E", "E", c5.getLoadName());
        Assert.assertEquals("Car c6 After Move 3 load should be E", "E", c6.getLoadName());
        Assert.assertEquals("Car c7 After Move 3 load should be E", "E", c7.getLoadName());
        Assert.assertEquals("Car c8 After Move 3 load should be E", "E", c8.getLoadName());
        Assert.assertEquals("Car c9 After Move 3 load should be E", "E", c9.getLoadName());

        // check car move counts
        Assert.assertEquals("Car c1 Move count", 19, c1.getMoves());
        Assert.assertEquals("Car c2 Move count", 18, c2.getMoves());
        Assert.assertEquals("Car c3 Move count", 2, c3.getMoves());
        Assert.assertEquals("Car c4 Move count", 9, c4.getMoves());
        Assert.assertEquals("Car c5 Move count", 3, c5.getMoves());
        Assert.assertEquals("Car c6 Move count", 5, c6.getMoves());
        Assert.assertEquals("Car c7 Move count", 20, c7.getMoves());
        Assert.assertEquals("Car c8 Move count", 4, c8.getMoves());
        Assert.assertEquals("Car c9 Move count", 20, c9.getMoves());

        // now try and use a train with more than one location
        // Create route with two locations
        Route rte3;
        rte3 = rmanager.newRoute("Westford to Chelmsford");
        RouteLocation rl3 = rte3.addLocation(westford);
        rl3.setTrainDirection(RouteLocation.WEST);
        rl3.setMaxCarMoves(10);
        RouteLocation rl4 = rte3.addLocation(chelmsford);
        rl4.setTrainDirection(RouteLocation.WEST);
        // and assign the new route to train 1
        train1.setRoute(rte3);
        rl4.setMaxCarMoves(10);
        rl4.setTrainIconX(175); // set the train icon coordinates
        rl4.setTrainIconY(50);

        Assert.assertTrue(train1.build());
        // should not pick up cars at Westford Spur 3, Westford Spur 4, Westford
        // Interchange 5
        // and Westford Interchange 7
        Assert.assertEquals("Car c1 After Build 4 should be assigned to Train 1", train1, c1.getTrain());
        Assert.assertEquals("Car c2 After Build 4 should NOT be assigned to Train 1", null, c2.getTrain());
        Assert.assertEquals("Car c3 After Build 4 should be assigned to Train 1", train1, c3.getTrain());
        Assert.assertEquals("Car c4 After Build 4 should NOT be assigned to Train 1", null, c4.getTrain());
        Assert.assertEquals("Car c5 After Build 4 should be assigned to Train 1", train1, c5.getTrain());
        Assert.assertEquals("Car c6 After Build 4 should be assigned to Train 1", train1, c6.getTrain());
        Assert.assertEquals("Car c7 After Build 4 should be assigned to Train 1", train1, c7.getTrain());
        Assert.assertEquals("Car c8 After Build 4 should be assigned to Train 1", train1, c8.getTrain());
        Assert.assertEquals("Car c9 After Build 4 should be assigned to Train 1", train1, c9.getTrain());

        train1.move();
        // Train in route since there's two locations
        Assert.assertEquals("Train 1 in route to Chelmsford", true, train1.isTrainEnRoute());
        Assert.assertEquals("Train 1 in route to Chelmsford", Train.CODE_TRAIN_EN_ROUTE, train1.getStatusCode());
        train1.move();
        // 7 cars should in Chelmsford, the other 2 in Westford
        Assert.assertEquals("Car c1 After Move 4 location", "Chelmsford Yard 1", c1.getTrackName());
        Assert.assertEquals("Car c2 verify location", "Westford Spur 3", c2.getTrackName());
        Assert.assertEquals("Car c3 After Move 4 location", "Chelmsford Yard 1", c3.getTrackName());
        Assert.assertEquals("Car c4 verify location", "Westford Interchange 7", c4.getTrackName());
        Assert.assertEquals("Car c5 After Move 4 location", "Chelmsford Yard 1", c5.getTrackName());
        Assert.assertEquals("Car c6 After Move 4 location", "Chelmsford Yard 1", c6.getTrackName());
        Assert.assertEquals("Car c7 After Move 4 location", "Chelmsford Yard 1", c7.getTrackName());
        Assert.assertEquals("Car c8 After Move 4 location", "Chelmsford Yard 1", c8.getTrackName());
        Assert.assertEquals("Car c9 After Move 4 location", "Chelmsford Yard 1", c9.getTrackName());

        // do cars have the right loads?
        Assert.assertEquals("Car c1 After Move 4 load should be E", "E", c1.getLoadName());
        Assert.assertEquals("Car c2 After Move 4 load should be E", "E", c2.getLoadName());
        Assert.assertEquals("Car c3 After Move 4 load should be E", "E", c3.getLoadName());
        Assert.assertEquals("Car c4 After Move 4 load should be L", "L", c4.getLoadName());
        Assert.assertEquals("Car c5 After Move 4 load should be E", "E", c5.getLoadName());
        Assert.assertEquals("Car c6 After Move 4 load should be E", "E", c6.getLoadName());
        Assert.assertEquals("Car c7 After Move 4 load should be E", "E", c7.getLoadName());
        Assert.assertEquals("Car c8 After Move 4 load should be E", "E", c8.getLoadName());
        Assert.assertEquals("Car c9 After Move 4 load should be E", "E", c9.getLoadName());

        // check car move counts
        Assert.assertEquals("Car c1 Move count", 20, c1.getMoves());
        Assert.assertEquals("Car c2 Move count", 18, c2.getMoves());
        Assert.assertEquals("Car c3 Move count", 3, c3.getMoves());
        Assert.assertEquals("Car c4 Move count", 9, c4.getMoves());
        Assert.assertEquals("Car c5 Move count", 4, c5.getMoves());
        Assert.assertEquals("Car c6 Move count", 6, c6.getMoves());
        Assert.assertEquals("Car c7 Move count", 21, c7.getMoves());
        Assert.assertEquals("Car c8 Move count", 5, c8.getMoves());
        Assert.assertEquals("Car c9 Move count", 21, c9.getMoves());

        train1.move();
        Assert.assertEquals("Train 1 After 4th build Status", Train.TERMINATED, getTrainStatus(train1));
        Assert.assertEquals("Train 1 After 4th build Status", Train.CODE_TERMINATED, train1.getStatusCode());

        // test spur to spur
        train1.setRoute(rte1);

        // bias track selection to spurs
        westfordSpur3.setMoves(2);
        westfordSpur4.setMoves(2);
        Assert.assertTrue(train1.build());

        Assert.assertTrue("local testing spur to spur", train1.isBuilt());
        Assert.assertEquals("car UP 4 at interchange, destination Westford Spur 3", westfordSpur3,
                c4.getDestinationTrack());
        Assert.assertEquals("car CP 2 at spur, destination Westford Yard 1", westfordYard1, c2.getDestinationTrack());

        // bias track selection to interchanges
        westfordSpur3.setMoves(12);
        westfordSpur4.setMoves(12);
        westfordInterchange5.setMoves(2);
        westfordInterchange6.setMoves(2);
        Assert.assertTrue(train1.build());

        Assert.assertTrue("local testing spur to spur", train1.isBuilt());
        Assert.assertEquals("car UP 4 at interchange, destination", "Westford Yard 2", c4.getDestinationTrackName());
        Assert.assertEquals("car CP 2 at spur, destination", "Westford Interchange 5", c2.getDestinationTrackName());

        // set CP 2 destination, currently at Westford, Westford Spur 3
        train1.reset(); // release CP2 from train so we can set the car's destination
        c2.setDestination(westford, null);
        westfordSpur3.setMoves(1); // bias to same track
        Assert.assertTrue(train1.build());

        Assert.assertTrue("local testing spur to spur", train1.isBuilt());
        Assert.assertEquals("car UP 4 at interchange, destination", "Westford Spur 3", c4.getDestinationTrackName());
        Assert.assertEquals("car CP 2 at spur, destination", "Westford Interchange 6", c2.getDestinationTrackName());

        // CP 2 is at Westford Spur 3, set destination to be the same
        train1.reset();
        c2.setDestination(westford, westfordSpur3);
        Assert.assertTrue(train1.build());

        Assert.assertTrue("local testing spur to spur", train1.isBuilt());
        Assert.assertEquals("car UP 4 at interchange, destination", "Westford Spur 3", c4.getDestinationTrackName());
        Assert.assertEquals("car CP 2 at spur, destination", "Westford Spur 3", c2.getDestinationTrackName());

        train1.move();
        Assert.assertEquals("Train 1 terminated", Train.TERMINATED, getTrainStatus(train1));
        Assert.assertEquals("Train 1 terminated", Train.CODE_TERMINATED, train1.getStatusCode());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    /**
     * tests cars with custom loads. A train with two locations, the second has
     * schedules.
     */
    @Test
    public void testScheduleLoadsA() {

        ScheduleManager smanager = InstanceManager.getDefault(ScheduleManager.class);

        // create schedules
        Schedule sch1 = smanager.newSchedule("Schedule 1");
        ScheduleItem sch1Item1 = sch1.addItem("Boxcar");
        // request a UP Boxcar
        sch1Item1.setRoadName("UP");
        ScheduleItem sch1Item2 = sch1.addItem("Flat Car");
        // request an empty car and load it with Scrap
        sch1Item2.setReceiveLoadName("E");
        sch1Item2.setShipLoadName("Scrap");
        ScheduleItem sch1Item3 = sch1.addItem("Gon");
        // request a loaded car and load it with Tin
        sch1Item3.setReceiveLoadName("L");
        sch1Item3.setShipLoadName("Tin");
        InstanceManager.getDefault(CarLoads.class).addName("Gon", "Tin"); // Allows c13 which is part of a kernel to get
                                                                          // a new load

        Schedule sch2 = smanager.newSchedule("Schedule 2");
        ScheduleItem sch2Item1 = sch2.addItem("Coil Car");
        sch2Item1.setCount(2);
        sch2.addItem("Boxcar");

        // Create locations used
        Location newWestford = lmanager.newLocation("New Westford");
        Location newChelmsford = lmanager.newLocation("New Chelmsford");

        Track westfordYard1;
        westfordYard1 = newWestford.addTrack("Westford Yard 1", Track.YARD);
        westfordYard1.setTrainDirections(Track.WEST + Track.EAST);
        westfordYard1.setLength(900);

        Track westfordYard2;
        westfordYard2 = newWestford.addTrack("Westford Yard 2", Track.YARD);
        westfordYard2.setTrainDirections(Track.WEST + Track.EAST);
        westfordYard2.setLength(500);
        westfordYard2.deleteTypeName("Coil Car");

        Track westfordExpress3;
        westfordExpress3 = newWestford.addTrack("Westford Express 3", Track.SPUR);
        westfordExpress3.setTrainDirections(Track.WEST + Track.EAST);
        westfordExpress3.setLength(300);
        westfordExpress3.deleteTypeName("Gon");
        westfordExpress3.deleteTypeName("Coil Car");

        Track westfordExpress4;
        westfordExpress4 = newWestford.addTrack("Westford Express 4", Track.SPUR);
        westfordExpress4.setTrainDirections(Track.WEST + Track.EAST);
        westfordExpress4.setLength(300);
        westfordExpress4.deleteTypeName("Gon");
        westfordExpress4.deleteTypeName("Coil Car");

        Track chelmsfordFreight1;
        chelmsfordFreight1 = newChelmsford.addTrack("Chelmsford Freight 1", Track.SPUR);
        chelmsfordFreight1.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordFreight1.setLength(900);
        chelmsfordFreight1.deleteTypeName("Coil Car");
        chelmsfordFreight1.setSchedule(sch1);
        chelmsfordFreight1.setScheduleMode(Track.SEQUENTIAL);
        // start the schedule with 2nd item Flat Car
        chelmsfordFreight1.setScheduleItemId(sch1.getItemsBySequenceList().get(1).getId());

        Track chelmsfordFreight2;
        chelmsfordFreight2 = newChelmsford.addTrack("Chelmsford Freight 2", Track.SPUR);
        chelmsfordFreight2.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordFreight2.setLength(900);
        chelmsfordFreight2.deleteTypeName("Coil Car");
        chelmsfordFreight2.setSchedule(sch1);
        chelmsfordFreight2.setScheduleMode(Track.SEQUENTIAL);
        // start the schedule with 3rd item Gon
        chelmsfordFreight2.setScheduleItemId(sch1.getItemsBySequenceList().get(2).getId());

        Track chelmsfordYard3;
        chelmsfordYard3 = newChelmsford.addTrack("Chelmsford Yard 3", Track.YARD);
        chelmsfordYard3.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordYard3.setLength(900);
        chelmsfordYard3.deleteTypeName("Gon");
        chelmsfordYard3.deleteTypeName("Coil Car");

        Track chelmsfordFreight4;
        chelmsfordFreight4 = newChelmsford.addTrack("Chelmsford Freight 4", Track.SPUR);
        chelmsfordFreight4.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordFreight4.setLength(900);
        chelmsfordFreight4.setSchedule(sch2);
        chelmsfordFreight4.setScheduleMode(Track.SEQUENTIAL);

        // Create route with 2 location
        Route rte1;
        rte1 = rmanager.newRoute("Two Location Route");
        RouteLocation rl1 = rte1.addLocation(newWestford);
        rl1.setTrainDirection(RouteLocation.EAST);
        rl1.setMaxCarMoves(12);

        RouteLocation rl2 = rte1.addLocation(newChelmsford);
        rl2.setTrainDirection(RouteLocation.EAST);
        rl2.setMaxCarMoves(12);

        // Create train
        Train train1;
        train1 = tmanager.newTrain("NWNC");
        train1.setRoute(rte1);

        // Set up 13 cars

        Car c1 = JUnitOperationsUtil.createAndPlaceCar("BM", "S1", "Gon", "90", westfordYard1, 13);
        c1.setLoadName("L");

        Car c2 = JUnitOperationsUtil.createAndPlaceCar("UP", "2", "Boxcar", "80", westfordYard1, 12);
        Car c3 = JUnitOperationsUtil.createAndPlaceCar("XP", "S3", "Flat Car", "70", westfordYard1, 0);
        c3.setLoadName("L");
        c3.setDestination(newChelmsford, null); // force this car to Chelmsford

        Car c4 = JUnitOperationsUtil.createAndPlaceCar("PU", "S4", "Boxcar", "60", westfordYard1, 10);
        Car c5 = JUnitOperationsUtil.createAndPlaceCar("UP", "S5", "Gon", "50", westfordYard2, 9);
        c5.setLoadName("L");

        Car c6 = JUnitOperationsUtil.createAndPlaceCar("CP", "S6", "Boxcar", "40", westfordYard2, 8);
        c6.setLoadName("L");

        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "S7", "Boxcar", "50", westfordYard2, 7);
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("XP", "S8", "Gon", "60", westfordYard2, 6);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("XP", "S9", "Flat Car", "90", westfordYard2, 5);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("CP", "S10", "Coil Car", "40", westfordYard1, 2);
        c10.setLoadName("L");

        Car c11 = JUnitOperationsUtil.createAndPlaceCar("CP", "S11", "Coil Car", "40", westfordYard1, 3);
        c11.setLoadName("Coils");

        Car c12 = JUnitOperationsUtil.createAndPlaceCar("CP", "S12", "Coil Car", "40", westfordYard1, 4);
        Car c13 = JUnitOperationsUtil.createAndPlaceCar("UP", "S13", "Gon", "50", westfordYard2, 1);
        c13.setLoadName("L");

        // place two cars in a kernel
        Kernel k1 = InstanceManager.getDefault(KernelManager.class).newKernel("TwoCars");
        c5.setKernel(k1);
        c13.setKernel(k1);

        Assert.assertTrue(train1.build());

        // Schedule sch1 should cause c2 to be delivered to Chelmsford Freight 2
        Assert.assertEquals("c2 destination", "Chelmsford Freight 2", c2.getDestinationTrackName());
        // Schedule sch1 and sch2 should reject c3, to be delivered to Chelmsford Yard 3
        Assert.assertEquals("c3 destination", "Chelmsford Yard 3", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination", "Chelmsford Yard 3", c4.getDestinationTrackName());
        // Schedule sch1 should cause c5 & c13 to be delivered to Chelmsford Freight 2
        Assert.assertEquals("c5 destination", "Chelmsford Freight 2", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination", "Chelmsford Yard 3", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination", "Chelmsford Freight 4", c7.getDestinationTrackName());
        Assert.assertEquals("c9 destination", "Chelmsford Freight 1", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination", "Chelmsford Freight 4", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination", "Chelmsford Freight 4", c11.getDestinationTrackName());
        // C13 is part of kernel
        Assert.assertEquals("c13 destination", "Chelmsford Freight 2", c13.getDestinationTrackName());

        // move and terminate train
        train1.move();
        train1.move();
        train1.move();

        Assert.assertEquals("c1 track", "Chelmsford Freight 1", c1.getTrackName());
        Assert.assertEquals("c1 load", "Tin", c1.getLoadName());
        Assert.assertEquals("c2 track", "Chelmsford Freight 2", c2.getTrackName());
        Assert.assertEquals("c2 load", "L", c2.getLoadName());
        Assert.assertEquals("c3 track", "Chelmsford Yard 3", c3.getTrackName());
        Assert.assertEquals("c3 load", "L", c3.getLoadName());
        Assert.assertEquals("c4 track", "Chelmsford Yard 3", c4.getTrackName());
        Assert.assertEquals("c4 load", "E", c4.getLoadName());
        Assert.assertEquals("c5 track", "Chelmsford Freight 2", c5.getTrackName());
        Assert.assertEquals("c5 load", "Tin", c5.getLoadName());
        Assert.assertEquals("c6 track", "Chelmsford Yard 3", c6.getTrackName());
        Assert.assertEquals("c6 load", "L", c6.getLoadName());
        Assert.assertEquals("c7 track", "Chelmsford Freight 4", c7.getTrackName());
        Assert.assertEquals("c7 load", "L", c7.getLoadName());
        Assert.assertEquals("c8 track", "Westford Yard 2", c8.getTrackName());
        Assert.assertEquals("c8 load", "E", c8.getLoadName());
        Assert.assertEquals("c9 track", "Chelmsford Freight 1", c9.getTrackName());
        Assert.assertEquals("c9 load", "Scrap", c9.getLoadName());
        Assert.assertEquals("c10 track", "Chelmsford Freight 4", c10.getTrackName());
        Assert.assertEquals("c10 load", "E", c10.getLoadName());
        Assert.assertEquals("c11 track", "Chelmsford Freight 4", c11.getTrackName());
        Assert.assertEquals("c11 load", "E", c11.getLoadName());
        Assert.assertEquals("c12 track", "Westford Yard 1", c12.getTrackName());
        Assert.assertEquals("c12 load", "E", c12.getLoadName());
        Assert.assertEquals("c13 track", "Chelmsford Freight 2", c13.getTrackName());
        Assert.assertEquals("c13 load", "Tin", c13.getLoadName());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    /**
     * tests cars with custom loads. train to staging, staging removes custom loads.
     */
    @Test
    public void testScheduleLoadsB() {

        // Create locations used
        Location newChelmsford = lmanager.newLocation("New Chelmsford");
        Location newBedford = lmanager.newLocation("New Bedford");

        Track chelmsfordFreight1;
        chelmsfordFreight1 = newChelmsford.addTrack("Chelmsford Freight 1", Track.SPUR);
        chelmsfordFreight1.setLength(900);

        Track chelmsfordFreight2;
        chelmsfordFreight2 = newChelmsford.addTrack("Chelmsford Freight 2", Track.SPUR);
        chelmsfordFreight2.setLength(900);

        Track chelmsfordYard3;
        chelmsfordYard3 = newChelmsford.addTrack("Chelmsford Yard 3", Track.YARD);
        chelmsfordYard3.setLength(900);

        Track chelmsfordFreight4;
        chelmsfordFreight4 = newChelmsford.addTrack("Chelmsford Freight 4", Track.SPUR);
        chelmsfordFreight4.setLength(900);

        Track bedfordYard1;
        bedfordYard1 = newBedford.addTrack("Bedford Yard 1", Track.STAGING);
        bedfordYard1.setLength(900);
        bedfordYard1.setRemoveCustomLoadsEnabled(true);

        // Set up 13 cars
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("BM", "S1", "Gon", "90", chelmsfordFreight1, 13);
        c1.setLoadName("Tin");
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("UP", "2", "Boxcar", "80", chelmsfordFreight2, 12);
        c2.setLoadName("L");
        Car c3 = JUnitOperationsUtil.createAndPlaceCar("XP", "S3", "Flat Car", "70", chelmsfordYard3, 0);
        c3.setLoadName("L");
        Car c4 = JUnitOperationsUtil.createAndPlaceCar("PU", "S4", "Boxcar", "60", chelmsfordYard3, 10);
        Car c5 = JUnitOperationsUtil.createAndPlaceCar("UP", "S5", "Gon", "50", chelmsfordFreight2, 9);
        c5.setLoadName("Tin");
        Car c6 = JUnitOperationsUtil.createAndPlaceCar("CP", "S6", "Boxcar", "40", chelmsfordYard3, 8);
        c6.setLoadName("L");
        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "S7", "Boxcar", "50", chelmsfordFreight4, 7);
        c7.setLoadName("L");

        Car c9 = JUnitOperationsUtil.createAndPlaceCar("XP", "S9", "Flat Car", "90", chelmsfordFreight1, 5);
        c9.setLoadName("Scrap");
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("CP", "S10", "Coil Car", "40", chelmsfordFreight4, 2);
        c10.setLoadName("E");
        Car c11 = JUnitOperationsUtil.createAndPlaceCar("CP", "S11", "Coil Car", "40", chelmsfordFreight4, 3);
        c11.setLoadName("E");

        Car c13 = JUnitOperationsUtil.createAndPlaceCar("UP", "S13", "Gon", "50", chelmsfordFreight2, 1);
        c13.setLoadName("Tin");

        // place two cars in a kernel
        Kernel k1 = InstanceManager.getDefault(KernelManager.class).newKernel("TwoCars");
        c5.setKernel(k1);
        c13.setKernel(k1);

        // create a route to staging to test remove custom loads
        // Create route with 2 location
        Route rte2;
        rte2 = rmanager.newRoute("Chelmsford to Staging");
        RouteLocation r2rl1 = rte2.addLocation(newChelmsford);
        r2rl1.setTrainDirection(RouteLocation.EAST);
        r2rl1.setMaxCarMoves(12);
        RouteLocation r2rl3 = rte2.addLocation(newBedford);
        r2rl3.setTrainDirection(RouteLocation.EAST);
        r2rl3.setMaxCarMoves(12);

        // Create train
        Train train1 = tmanager.newTrain("Chelmsford to Bedford");
        train1.setRoute(rte2);
        Assert.assertTrue(train1.build());

        // move and terminate train
        train1.move();
        train1.move();
        train1.move();

        Assert.assertEquals("c1 track to staging", "Bedford Yard 1", c1.getTrackName());
        Assert.assertEquals("c1 load to staging", "E", c1.getLoadName());
        Assert.assertEquals("c2 track to staging", "Bedford Yard 1", c2.getTrackName());
        Assert.assertEquals("c2 load to staging", "L", c2.getLoadName());
        Assert.assertEquals("c3 track to staging", "Bedford Yard 1", c3.getTrackName());
        Assert.assertEquals("c3 load to staging", "L", c3.getLoadName());
        Assert.assertEquals("c4 track to staging", "Bedford Yard 1", c4.getTrackName());
        Assert.assertEquals("c4 load to staging", "E", c4.getLoadName());
        Assert.assertEquals("c5 track to staging", "Bedford Yard 1", c5.getTrackName());
        Assert.assertEquals("c5 load to staging", "E", c5.getLoadName());
        Assert.assertEquals("c6 track to staging", "Bedford Yard 1", c6.getTrackName());
        Assert.assertEquals("c6 load to staging", "L", c6.getLoadName());
        Assert.assertEquals("c7 track to staging", "Bedford Yard 1", c7.getTrackName());
        Assert.assertEquals("c7 load to staging", "L", c7.getLoadName());

        Assert.assertEquals("c9 track to staging", "Bedford Yard 1", c9.getTrackName());
        Assert.assertEquals("c9 load to staging", "E", c9.getLoadName());
        Assert.assertEquals("c10 track to staging", "Bedford Yard 1", c10.getTrackName());
        Assert.assertEquals("c10 load to staging", "E", c10.getLoadName());
        Assert.assertEquals("c11 track to staging", "Bedford Yard 1", c11.getTrackName());
        Assert.assertEquals("c11 load to staging", "E", c11.getLoadName());

        Assert.assertEquals("c13 track to staging", "Bedford Yard 1", c13.getTrackName());
        Assert.assertEquals("c13 load to staging", "E", c13.getLoadName());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    /**
     * tests cars with custom loads. Train out of staging should generate custom
     * loads.
     */
    @Test
    public void testScheduleLoadsC() {

        ScheduleManager smanager = InstanceManager.getDefault(ScheduleManager.class);

        // create schedules
        Schedule sch1 = smanager.newSchedule("Schedule 1");
        ScheduleItem sch1Item1 = sch1.addItem("Boxcar");
        // request a UP Boxcar
        sch1Item1.setRoadName("UP");
        sch1Item1.setReceiveLoadName("Metal 1");

        ScheduleItem sch1Item2 = sch1.addItem("Flat Car");
        // request an empty car and load it with Scrap
        sch1Item2.setReceiveLoadName("Metal 2");
        sch1Item2.setShipLoadName("Scrap");

        ScheduleItem sch1Item3 = sch1.addItem("Gon");
        // request a loaded car and load it with Tin
        sch1Item3.setReceiveLoadName("Metal 3");
        sch1Item3.setShipLoadName("Tin");

        InstanceManager.getDefault(CarLoads.class).addName("Boxcar", "Metal 1");
        InstanceManager.getDefault(CarLoads.class).addName("Flat Car", "Metal 2");
        InstanceManager.getDefault(CarLoads.class).addName("Gon", "Metal 3");
        InstanceManager.getDefault(CarLoads.class).addName("Gon", "Tin");

        Schedule sch2 = smanager.newSchedule("Schedule 2");
        ScheduleItem sch2Item1 = sch2.addItem("Coil Car");
        sch2Item1.setCount(2);
        sch2.addItem("Boxcar");

        // Create locations used
        Location newWestford = lmanager.newLocation("New Westford");
        Location newChelmsford = lmanager.newLocation("New Chelmsford");
        Location newBedford = lmanager.newLocation("New Bedford");

        Track westfordYard1;
        westfordYard1 = newWestford.addTrack("Westford Yard 1", Track.YARD);
        westfordYard1.setTrainDirections(Track.WEST + Track.EAST);
        westfordYard1.setLength(900);

        Track westfordYard2;
        westfordYard2 = newWestford.addTrack("Westford Yard 2", Track.YARD);
        westfordYard2.setTrainDirections(Track.WEST + Track.EAST);
        westfordYard2.setLength(500);
        westfordYard2.deleteTypeName("Coil Car");

        Track westfordExpress3;
        westfordExpress3 = newWestford.addTrack("Westford Express 3", Track.SPUR);
        westfordExpress3.setTrainDirections(Track.WEST + Track.EAST);
        westfordExpress3.setLength(300);
        westfordExpress3.deleteTypeName("Gon");
        westfordExpress3.deleteTypeName("Coil Car");

        Track westfordExpress4;
        westfordExpress4 = newWestford.addTrack("Westford Express 4", Track.SPUR);
        westfordExpress4.setTrainDirections(Track.WEST + Track.EAST);
        westfordExpress4.setLength(300);
        westfordExpress4.deleteTypeName("Gon");
        westfordExpress4.deleteTypeName("Coil Car");

        Track chelmsfordFreight1;
        chelmsfordFreight1 = newChelmsford.addTrack("Chelmsford Freight 1", Track.SPUR);
        chelmsfordFreight1.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordFreight1.setLength(900);
        chelmsfordFreight1.deleteTypeName("Coil Car");
        chelmsfordFreight1.setSchedule(sch1);
        chelmsfordFreight1.setScheduleMode(Track.SEQUENTIAL);
        // start the schedule with 2nd item Flat Car
        chelmsfordFreight1.setScheduleItemId(sch1.getItemsBySequenceList().get(1).getId());

        Track chelmsfordFreight2;
        chelmsfordFreight2 = newChelmsford.addTrack("Chelmsford Freight 2", Track.SPUR);
        chelmsfordFreight2.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordFreight2.setLength(900);
        chelmsfordFreight2.deleteTypeName("Coil Car");
        chelmsfordFreight2.setSchedule(sch1);
        chelmsfordFreight2.setScheduleMode(Track.SEQUENTIAL);
        // start the schedule with 3rd item Gon
        chelmsfordFreight2.setScheduleItemId(sch1.getItemsBySequenceList().get(2).getId());

        Track chelmsfordYard3;
        chelmsfordYard3 = newChelmsford.addTrack("Chelmsford Yard 3", Track.YARD);
        chelmsfordYard3.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordYard3.setLength(900);
        chelmsfordYard3.deleteTypeName("Gon");
        chelmsfordYard3.deleteTypeName("Coil Car");

        Track chelmsfordFreight4;
        chelmsfordFreight4 = newChelmsford.addTrack("Chelmsford Freight 4", Track.SPUR);
        chelmsfordFreight4.setTrainDirections(Track.WEST + Track.EAST);
        chelmsfordFreight4.setLength(900);
        chelmsfordFreight4.setSchedule(sch2);
        chelmsfordFreight4.setScheduleMode(Track.SEQUENTIAL);

        Track bedfordYard1;
        bedfordYard1 = newBedford.addTrack("Bedford Yard 1", Track.STAGING);
        bedfordYard1.setTrainDirections(Track.WEST + Track.EAST);
        bedfordYard1.setLength(900);
        bedfordYard1.setRemoveCustomLoadsEnabled(true);

        // create a route from staging to test generate schedule load
        // Create route with 3 locations
        Route rte3;
        rte3 = rmanager.newRoute("Staging to Westford");
        RouteLocation r3rl1 = rte3.addLocation(newBedford);
        r3rl1.setTrainDirection(RouteLocation.EAST);
        r3rl1.setMaxCarMoves(11); // there are 11 cars departing staging
        RouteLocation r3rl2 = rte3.addLocation(newChelmsford);
        r3rl2.setTrainDirection(RouteLocation.EAST);
        r3rl2.setMaxCarMoves(12);
        RouteLocation r3rl3 = rte3.addLocation(newWestford);
        r3rl3.setTrainDirection(RouteLocation.EAST);
        r3rl3.setMaxCarMoves(12);

        // Create train
        Train train1;
        train1 = tmanager.newTrain("BCW");
        train1.setRoute(rte3);

        // Set up 13 cars
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("BM", "S1", "Gon", "90", bedfordYard1, 13);
        c1.setLoadName("E");
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("UP", "2", "Boxcar", "80", bedfordYard1, 12);
        c2.setLoadName("L");
        Car c3 = JUnitOperationsUtil.createAndPlaceCar("XP", "S3", "Flat Car", "70", bedfordYard1, 0);
        c3.setLoadName("L");
        Car c4 = JUnitOperationsUtil.createAndPlaceCar("PU", "S4", "Boxcar", "60", bedfordYard1, 10);
        Car c5 = JUnitOperationsUtil.createAndPlaceCar("UP", "S5", "Gon", "50", bedfordYard1, 9);
        c5.setLoadName("E");
        Car c6 = JUnitOperationsUtil.createAndPlaceCar("CP", "S6", "Boxcar", "40", bedfordYard1, 8);
        c6.setLoadName("L");
        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "S7", "Boxcar", "50", bedfordYard1, 7);
        c7.setLoadName("L");
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("XP", "S8", "Gon", "60", westfordYard2, 6);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("XP", "S9", "Flat Car", "90", bedfordYard1, 5);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("CP", "S10", "Coil Car", "40", bedfordYard1, 2);
        c10.setLoadName("E");
        Car c11 = JUnitOperationsUtil.createAndPlaceCar("CP", "S11", "Coil Car", "40", bedfordYard1, 3);
        c11.setLoadName("E");
        Car c12 = JUnitOperationsUtil.createAndPlaceCar("CP", "S12", "Coil Car", "40", westfordYard1, 4);
        Car c13 = JUnitOperationsUtil.createAndPlaceCar("UP", "S13", "Gon", "50", bedfordYard1, 1);
        c13.setLoadName("E");

        // place two cars in a kernel
        Kernel k1 = InstanceManager.getDefault(KernelManager.class).newKernel("TwoCars");
        c5.setKernel(k1);
        c13.setKernel(k1);

        Assert.assertEquals("c1 track to staging", "Bedford Yard 1", c1.getTrackName());
        Assert.assertEquals("c1 load to staging", "E", c1.getLoadName());
        Assert.assertEquals("c2 track to staging", "Bedford Yard 1", c2.getTrackName());
        Assert.assertEquals("c2 load to staging", "L", c2.getLoadName());
        Assert.assertEquals("c3 track to staging", "Bedford Yard 1", c3.getTrackName());
        Assert.assertEquals("c3 load to staging", "L", c3.getLoadName());
        Assert.assertEquals("c4 track to staging", "Bedford Yard 1", c4.getTrackName());
        Assert.assertEquals("c4 load to staging", "E", c4.getLoadName());
        Assert.assertEquals("c5 track to staging", "Bedford Yard 1", c5.getTrackName());
        Assert.assertEquals("c5 load to staging", "E", c5.getLoadName());
        Assert.assertEquals("c6 track to staging", "Bedford Yard 1", c6.getTrackName());
        Assert.assertEquals("c6 load to staging", "L", c6.getLoadName());
        Assert.assertEquals("c7 track to staging", "Bedford Yard 1", c7.getTrackName());
        Assert.assertEquals("c7 load to staging", "L", c7.getLoadName());
        Assert.assertEquals("c8 track to staging", "Westford Yard 2", c8.getTrackName());
        Assert.assertEquals("c8 load to staging", "E", c8.getLoadName());
        Assert.assertEquals("c9 track to staging", "Bedford Yard 1", c9.getTrackName());
        Assert.assertEquals("c9 load to staging", "E", c9.getLoadName());
        Assert.assertEquals("c10 track to staging", "Bedford Yard 1", c10.getTrackName());
        Assert.assertEquals("c10 load to staging", "E", c10.getLoadName());
        Assert.assertEquals("c11 track to staging", "Bedford Yard 1", c11.getTrackName());
        Assert.assertEquals("c11 load to staging", "E", c11.getLoadName());
        Assert.assertEquals("c12 track to staging", "Westford Yard 1", c12.getTrackName());
        Assert.assertEquals("c12 load to staging", "E", c12.getLoadName());
        Assert.assertEquals("c13 track to staging", "Bedford Yard 1", c13.getTrackName());
        Assert.assertEquals("c13 load to staging", "E", c13.getLoadName());

        bedfordYard1.setAddCustomLoadsAnySpurEnabled(true); // generate schedule loads

        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train Bedford Chelmsford Westford build status", true, train1.isBuilt());

        Assert.assertEquals("c1 load from staging", "Metal 3", c1.getLoadName());
        Assert.assertEquals("c2 load from staging", "L", c2.getLoadName());
        Assert.assertEquals("c3 load from staging", "L", c3.getLoadName());
        Assert.assertEquals("c4 load from staging", "E", c4.getLoadName());
        Assert.assertEquals("c5 load from staging", "Metal 3", c5.getLoadName());
        Assert.assertEquals("c6 load from staging", "L", c6.getLoadName());
        Assert.assertEquals("c7 load from staging", "L", c7.getLoadName());
        Assert.assertEquals("c8 load from staging", "E", c8.getLoadName());
        Assert.assertEquals("c9 load from staging", "Metal 2", c9.getLoadName());
        Assert.assertEquals("c10 load from staging", "E", c10.getLoadName());
        Assert.assertEquals("c11 load from staging", "E", c11.getLoadName());
        Assert.assertEquals("c13 load from staging", "Metal 3", c13.getLoadName());

        // move and terminate train
        train1.move();
        train1.move();
        train1.move();
        train1.move();

        Assert.assertEquals("c1 track from staging terminated", "Chelmsford Freight 1", c1.getTrackName());
        Assert.assertEquals("c1 load from staging terminated", "Tin", c1.getLoadName());
        Assert.assertEquals("c2 track from staging terminated", "Westford Yard 2", c2.getTrackName());
        Assert.assertEquals("c2 load from staging terminated", "L", c2.getLoadName());
        Assert.assertEquals("c3 track from staging terminated", "Westford Yard 1", c3.getTrackName());
        Assert.assertEquals("c3 load from staging terminated", "L", c3.getLoadName());
        Assert.assertEquals("c4 track from staging terminated", "Westford Express 4", c4.getTrackName());
        Assert.assertEquals("c4 load from staging terminated", "L", c4.getLoadName());
        Assert.assertEquals("c5 track from staging terminated", "Chelmsford Freight 2", c5.getTrackName());
        Assert.assertEquals("c5 load from staging terminated", "Tin", c5.getLoadName());
        Assert.assertEquals("c6 track from staging terminated", "Westford Express 3", c6.getTrackName());
        Assert.assertEquals("c6 load from staging terminated", "E", c6.getLoadName());
        Assert.assertEquals("c7 track from staging terminated", "Westford Yard 2", c7.getTrackName());
        Assert.assertEquals("c7 load from staging terminated", "L", c7.getLoadName());
        Assert.assertEquals("c8 track from staging terminated", "Westford Yard 2", c8.getTrackName());
        Assert.assertEquals("c8 load from staging terminated", "E", c8.getLoadName());
        Assert.assertEquals("c9 track from staging terminated", "Chelmsford Freight 1", c9.getTrackName());
        Assert.assertEquals("c9 load from staging terminated", "Scrap", c9.getLoadName());
        Assert.assertEquals("c10 track from staging terminated", "Chelmsford Freight 4", c10.getTrackName());
        Assert.assertEquals("c10 load from staging terminated", "L", c10.getLoadName());
        Assert.assertEquals("c11 track from staging terminated", "Westford Yard 1", c11.getTrackName());
        Assert.assertEquals("c11 load from staging terminated", "E", c11.getLoadName());
        Assert.assertEquals("c12 track from staging terminated", "Westford Yard 1", c12.getTrackName());
        Assert.assertEquals("c12 load from staging terminated", "E", c12.getLoadName());
        Assert.assertEquals("c13 track from staging terminated", "Chelmsford Freight 2", c13.getTrackName());
        Assert.assertEquals("c13 load from staging terminated", "Tin", c13.getLoadName());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testInterchange() {

        Setup.setMaxTrainLength(500);

        ct.addName("XCaboose");

        // confirm no locations
        Assert.assertEquals("number of locations", 0, lmanager.getNumberOfLocations());
        // Create locations used
        Location loc1 = lmanager.newLocation("Old Westford");
        Location loc2 = lmanager.newLocation("Old Chelmsford");
        Location loc3 = lmanager.newLocation("Old Bedford");

        Track loc1trk1;
        loc1trk1 = loc1.addTrack("Westford Yard 1", Track.YARD);
        loc1trk1.setTrainDirections(Track.WEST + Track.EAST);
        loc1trk1.setLength(900);

        Track loc1trk2;
        loc1trk2 = loc1.addTrack("Westford Yard 2", Track.YARD);
        loc1trk2.setTrainDirections(Track.WEST + Track.EAST);
        loc1trk2.setLength(500);
        loc1trk2.deleteTypeName("Coil Car");
        loc1trk2.deleteTypeName("XCaboose");

        Track loc2trk1;
        loc2trk1 = loc2.addTrack("Chelmsford Interchange 1", Track.INTERCHANGE);
        loc2trk1.setTrainDirections(Track.WEST + Track.EAST);
        loc2trk1.setLength(900);
        loc2trk1.deleteTypeName("Coil Car");
        loc2trk1.deleteTypeName("XCaboose");

        Track loc2trk2;
        loc2trk2 = loc2.addTrack("Chelmsford Interchange 2", Track.INTERCHANGE);
        loc2trk2.setTrainDirections(Track.WEST + Track.EAST);
        loc2trk2.setLength(900);
        loc2trk2.deleteTypeName("XCaboose");

        Track loc2trk3;
        loc2trk3 = loc2.addTrack("Chelmsford Yard 3", Track.YARD);
        loc2trk3.setTrainDirections(Track.WEST + Track.EAST);
        loc2trk3.setLength(900);
        loc2trk3.deleteTypeName("Gon");
        loc2trk3.deleteTypeName("Coil Car");
        loc2trk3.deleteTypeName("XCaboose");

        Track loc2trk4;
        loc2trk4 = loc2.addTrack("Chelmsford Freight 4", Track.SPUR);
        loc2trk4.setTrainDirections(Track.WEST + Track.EAST);
        loc2trk4.setLength(900);
        loc2trk4.deleteTypeName("Gon");
        loc2trk4.deleteTypeName("XCaboose");

        loc2trk3.setMoves(20); // bias interchange tracks
        loc2trk4.setMoves(20);

        Track loc3trk1;
        loc3trk1 = loc3.addTrack("Bedford Yard 1", Track.YARD);
        loc3trk1.setTrainDirections(Track.WEST + Track.EAST);
        loc3trk1.setLength(900);

        // Create route with 3 location
        Route rte1;
        rte1 = rmanager.newRoute("Route 1 East");
        RouteLocation r1l1 = rte1.addLocation(loc1);
        r1l1.setTrainDirection(RouteLocation.EAST);
        r1l1.setMaxCarMoves(4);
        r1l1.setTrainIconX(125); // set the train icon coordinates
        r1l1.setTrainIconY(100);
        RouteLocation r1l2 = rte1.addLocation(loc2);
        r1l2.setTrainDirection(RouteLocation.EAST);
        r1l2.setMaxCarMoves(3);
        r1l2.setTrainIconX(25); // set the train icon coordinates
        r1l2.setTrainIconY(125);
        RouteLocation r1l3 = rte1.addLocation(loc3);
        r1l3.setTrainDirection(RouteLocation.EAST);
        r1l3.setMaxCarMoves(3);
        r1l3.setTrainIconX(75); // set the train icon coordinates
        r1l3.setTrainIconY(125);

        // Create route with 3 location
        Route rte2;
        rte2 = rmanager.newRoute("Route 2 East");
        RouteLocation r2l1 = rte2.addLocation(loc1);
        r2l1.setTrainDirection(RouteLocation.EAST);
        r2l1.setMaxCarMoves(2);
        r2l1.setTrainIconX(125); // set the train icon coordinates
        r2l1.setTrainIconY(125);
        RouteLocation r2l2 = rte2.addLocation(loc2);
        r2l2.setTrainDirection(RouteLocation.EAST);
        r2l2.setMaxCarMoves(6);
        r2l2.setTrainIconX(175); // set the train icon coordinates
        r2l2.setTrainIconY(125);
        RouteLocation r2l3 = rte2.addLocation(loc3);
        r2l3.setTrainDirection(RouteLocation.EAST);
        r2l3.setMaxCarMoves(6);
        r2l3.setTrainIconX(25); // set the train icon coordinates
        r2l3.setTrainIconY(150);

        // Create trains
        Train train1;
        train1 = tmanager.newTrain("TT1OWOB");
        train1.setRoute(rte1);

        Train train2;
        train2 = tmanager.newTrain("TT2OWOB");
        train2.setRoute(rte1);

        Train train3;
        train3 = tmanager.newTrain("TT3OWOB");
        train3.setRoute(rte1);

        // Set up 7 box cars and 2 flat cars
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("BM", "Q1", "Gon", "90", loc1trk1, 20);
        c1.setLoadName("L");
        c1.setWeightTons("10");

        Car c2 = JUnitOperationsUtil.createAndPlaceCar("UP", "Q2", "Boxcar", "80", loc1trk1, 18);
        c2.setWeightTons("20");

        Car c3 = JUnitOperationsUtil.createAndPlaceCar("XP", "Q3", "Flat Car", "70", loc1trk1, 17);
        c3.setWeightTons("30");

        Car c4 = JUnitOperationsUtil.createAndPlaceCar("PU", "Q4", "Boxcar", "60", loc1trk1, 16);
        c4.setWeightTons("40");

        Car c5 = JUnitOperationsUtil.createAndPlaceCar("UP", "Q5", "Gon", "50", loc1trk2, 15);
        c5.setLoadName("L");
        c5.setWeightTons("50");

        Car c6 = JUnitOperationsUtil.createAndPlaceCar("CP", "Q6", "Boxcar", "40", loc1trk2, 14);
        c6.setLoadName("L");
        c6.setWeightTons("60");

        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "Q7", "Boxcar", "50", loc1trk2, 13);
        c7.setWeightTons("70");

        Car c8 = JUnitOperationsUtil.createAndPlaceCar("XP", "Q8", "Gon", "60", loc1trk2, 12);
        c8.setWeightTons("80");

        Car c9 = JUnitOperationsUtil.createAndPlaceCar("XP", "Q9", "Flat Car", "90", loc1trk2, 11);
        c9.setLoadName("L");
        c9.setWeightTons("90");

        Car c10 = JUnitOperationsUtil.createAndPlaceCar("CP", "Q10", "Coil Car", "40", loc1trk1, 8);
        c10.setLoadName("L");
        c10.setWeightTons("100");

        Car c11 = JUnitOperationsUtil.createAndPlaceCar("CP", "Q11", "Coil Car", "40", loc1trk1, 9);
        c11.setLoadName("Coils");
        c11.setWeightTons("110");

        Car c12 = JUnitOperationsUtil.createAndPlaceCar("CP", "Q12", "Coil Car", "40", loc1trk1, 10);
        c12.setWeightTons("120");

        Car c13 = JUnitOperationsUtil.createAndPlaceCar("CP", "Q13", "XCaboose", "40", loc1trk1, 7);
        c13.setCaboose(true);
        c13.setWeightTons("130");

        Assert.assertTrue(train1.build());
        Assert.assertTrue(train2.build());

        // now check to where cars are going to be delivered
        Assert.assertEquals("c1 destination", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination", "", c4.getDestinationTrackName());

        Assert.assertEquals("c9 destination", "Chelmsford Interchange 1", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination", "Bedford Yard 1", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination", "Chelmsford Interchange 2", c11.getDestinationTrackName());
        Assert.assertEquals("c12 destination", "Bedford Yard 1", c12.getDestinationTrackName());

        Assert.assertEquals("c5 destination", "Chelmsford Interchange 2", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination", "Bedford Yard 1", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination", "Chelmsford Interchange 1", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination", "Bedford Yard 1", c8.getDestinationTrackName());

        // now check which trains
        Assert.assertEquals("c9 train", train1, c9.getTrain());
        Assert.assertEquals("c10 train", train1, c10.getTrain());
        Assert.assertEquals("c11 train", train1, c11.getTrain());
        Assert.assertEquals("c12 train", train1, c12.getTrain());

        Assert.assertEquals("c5 train", train2, c5.getTrain());
        Assert.assertEquals("c6 train", train2, c6.getTrain());
        Assert.assertEquals("c7 train", train2, c7.getTrain());
        Assert.assertEquals("c8 train", train2, c8.getTrain());

        // try restricting interchange 1 to train1 and interchange 2 to train2
        loc2trk1.setDropOption(Track.TRAINS);
        loc2trk1.addDropId(train1.getId());
        loc2trk2.setDropOption(Track.TRAINS);
        loc2trk2.addDropId(train2.getId());

        c5.setMoves(16);

        Assert.assertTrue(train1.build());
        Assert.assertTrue(train2.build());

        // now check to where cars are going to be delivered
        Assert.assertEquals("c1 destination 2", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 2", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 2", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 2", "Chelmsford Interchange 2", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 2", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 2", "Bedford Yard 1", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 2", "Chelmsford Interchange 2", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 2", "Bedford Yard 1", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 2", "Chelmsford Interchange 1", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 2", "Bedford Yard 1", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 2", "Bedford Yard 1", c11.getDestinationTrackName());
        Assert.assertEquals("c12 destination 2", "Chelmsford Freight 4", c12.getDestinationTrackName());

        // now check which trains
        Assert.assertEquals("c9 train", train1, c9.getTrain());
        Assert.assertEquals("c10 train", train1, c10.getTrain());
        Assert.assertEquals("c11 train", train1, c11.getTrain());
        Assert.assertEquals("c12 train", train1, c12.getTrain());

        Assert.assertEquals("c4 train", train2, c4.getTrain());
        Assert.assertEquals("c6 train", train2, c6.getTrain());
        Assert.assertEquals("c7 train", train2, c7.getTrain());
        Assert.assertEquals("c8 train", train2, c8.getTrain());

        // move and terminate
        Assert.assertEquals("Check train 1 departure location name", "Old Westford", train1.getCurrentLocationName());
        Assert.assertEquals("Check train 1 departure location", r1l1, train1.getCurrentRouteLocation());
        train1.move(); // #1
        Assert.assertEquals("Check train 1 location name", "Old Chelmsford", train1.getCurrentLocationName());
        Assert.assertEquals("Check train 1 location", r1l2, train1.getCurrentRouteLocation());
        train1.move(); // #2
        Assert.assertEquals("Check train 1 location name", "Old Bedford", train1.getCurrentLocationName());
        Assert.assertEquals("Check train 1 location", r1l3, train1.getCurrentRouteLocation());
        train1.move(); // #3 terminate
        Assert.assertEquals("Check train 1 location name", "", train1.getCurrentLocationName());
        Assert.assertEquals("Check train 1 location", null, train1.getCurrentRouteLocation());

        Assert.assertEquals("Check train 2 departure location name", "Old Westford", train2.getCurrentLocationName());
        Assert.assertEquals("Check train 2 departure location", r1l1, train2.getCurrentRouteLocation());
        train2.move(); // #1
        Assert.assertEquals("Check train 2 location name", "Old Chelmsford", train2.getCurrentLocationName());
        Assert.assertEquals("Check train 2 location", r1l2, train2.getCurrentRouteLocation());
        train2.move(); // #2
        Assert.assertEquals("Check train 2 location name", "Old Bedford", train2.getCurrentLocationName());
        Assert.assertEquals("Check train 2 location", r1l3, train2.getCurrentRouteLocation());
        train2.move(); // #3 terminate
        Assert.assertEquals("Check train 2 location name", "", train2.getCurrentLocationName());
        Assert.assertEquals("Check train 2 location", null, train2.getCurrentRouteLocation());

        r1l1.setMaxCarMoves(2);
        r1l2.setMaxCarMoves(6);
        r1l3.setMaxCarMoves(6);
        Assert.assertTrue(train3.build()); // note that train3 uses rte1, should not pickup cars at interchange

        Assert.assertEquals("c1 destination 3", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 3", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 3", "Chelmsford Yard 3", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 3", "", c4.getDestinationTrackName());
        Assert.assertEquals("c5 destination 3", "Bedford Yard 1", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 3", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 3", "", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 3", "", c8.getDestinationTrackName());
        Assert.assertEquals("c9 destination 3", "", c9.getDestinationTrackName());
        Assert.assertEquals("c12 destination 3", "Bedford Yard 1", c12.getDestinationTrackName());

        // Change the route to 2, should be able to pickup c4, c7, c9
        train3.reset();
        train3.setRoute(rte2);

        Assert.assertEquals("c2 move count", 18, c2.getMoves());
        Assert.assertEquals("c3 move count", 17, c3.getMoves());
        Assert.assertEquals("c4 move count", 17, c4.getMoves());

        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 4", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 4", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 4", "Chelmsford Yard 3", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 4", "Bedford Yard 1", c4.getDestinationTrackName());
        Assert.assertEquals("c5 destination 4", "Bedford Yard 1", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 4", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 4", "Bedford Yard 1", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 4", "", c8.getDestinationTrackName());
        Assert.assertEquals("c9 destination 4", "Bedford Yard 1", c9.getDestinationTrackName());
        Assert.assertEquals("c12 destination 4", "Bedford Yard 1", c12.getDestinationTrackName());

        // bias c3
        c3.setMoves(20);

        // Change back to route to 1, should be able to pickup c4, c7
        train3.reset();
        train3.setRoute(rte1);
        loc2trk2.setPickupOption(Track.TRAINS);
        loc2trk2.addPickupId(train3.getId());
        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 5", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 5", "Chelmsford Freight 4", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 5", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 5", "Bedford Yard 1", c4.getDestinationTrackName());
        Assert.assertEquals("c5 destination 5", "Bedford Yard 1", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 5", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 5", "Bedford Yard 1", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 5", "", c8.getDestinationTrackName());
        Assert.assertEquals("c9 destination 5", "", c9.getDestinationTrackName());
        Assert.assertEquals("c12 destination 5", "Bedford Yard 1", c12.getDestinationTrackName());

        // check car move counts
        Assert.assertEquals("c1 move count", 20, c1.getMoves());
        Assert.assertEquals("c2 move count", 18, c2.getMoves());
        Assert.assertEquals("c3 move count", 20, c3.getMoves());
        Assert.assertEquals("c4 move count", 17, c4.getMoves());
        Assert.assertEquals("c5 move count", 16, c5.getMoves());
        Assert.assertEquals("c6 move count", 15, c6.getMoves());
        Assert.assertEquals("c7 move count", 14, c7.getMoves());
        Assert.assertEquals("c8 move count", 13, c8.getMoves());
        Assert.assertEquals("c9 move count", 12, c9.getMoves());
        Assert.assertEquals("c12 move count", 11, c12.getMoves());

        c1.setMoves(18);

        // Change back to route to 1, should be able to pickup c4, c7, and c9
        train3.reset();
        train3.setRoute(rte1);
        loc2trk1.setPickupOption(Track.ROUTES);
        loc2trk1.addPickupId(rte1.getId());
        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 6", "Bedford Yard 1", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 6", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 6", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 6", "Bedford Yard 1", c4.getDestinationTrackName());
        Assert.assertEquals("c5 destination 6", "Bedford Yard 1", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 6", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 6", "Bedford Yard 1", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 6", "", c8.getDestinationTrackName());
        Assert.assertEquals("c9 destination 6", "Bedford Yard 1", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 6", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 6", "", c11.getDestinationTrackName());
        Assert.assertEquals("c12 destination 6", "Bedford Yard 1", c12.getDestinationTrackName());

        // now allow train 3 to drop
        train3.reset();
        loc2trk1.setDropOption(Track.ROUTES);
        loc2trk1.addDropId(rte1.getId());

        c5.setMoves(18);
        c2.setMoves(19);
        c1.setMoves(20);
        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 7", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 7", "Chelmsford Interchange 1", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 7", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 7", "Bedford Yard 1", c4.getDestinationTrackName());
        Assert.assertEquals("c5 destination 7", "Bedford Yard 1", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 7", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 7", "Bedford Yard 1", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 7", "", c8.getDestinationTrackName());
        Assert.assertEquals("c9 destination 7", "Bedford Yard 1", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 7", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 7", "", c11.getDestinationTrackName());
        Assert.assertEquals("c12 destination 7", "Bedford Yard 1", c12.getDestinationTrackName());

        // move and terminate
        train3.move();
        train3.move();
        train3.move();
        train3.move();

        // check tracks
        Assert.assertEquals("c1 track", "Westford Yard 1", c1.getTrackName());
        Assert.assertEquals("c2 track", "Chelmsford Interchange 1", c2.getTrackName());
        Assert.assertEquals("c3 track", "Westford Yard 1", c3.getTrackName());
        Assert.assertEquals("c4 track", "Bedford Yard 1", c4.getTrackName());
        Assert.assertEquals("c5 track", "Bedford Yard 1", c5.getTrackName());
        Assert.assertEquals("c6 track", "Bedford Yard 1", c6.getTrackName());
        Assert.assertEquals("c7 track", "Bedford Yard 1", c7.getTrackName());
        Assert.assertEquals("c8 track", "Bedford Yard 1", c8.getTrackName());
        Assert.assertEquals("c9 track", "Bedford Yard 1", c9.getTrackName());
        Assert.assertEquals("c10 track", "Bedford Yard 1", c10.getTrackName());
        Assert.assertEquals("c11 track", "Bedford Yard 1", c11.getTrackName());
        Assert.assertEquals("c12 track", "Bedford Yard 1", c12.getTrackName());

        // check train length and tonnage
        Assert.assertEquals("Depart Westford length", 138, r1l1.getTrainLength());
        Assert.assertEquals("Depart Old Chelmsford length", 310, r1l2.getTrainLength());

        // In train 2 cars, c2 E and c5 L car weight 20/3 + 50 = 56
        Assert.assertEquals("Depart Old Westford tonnage", 56, r1l1.getTrainWeight());
        // In train 5 cars, c4 E, c5 L, c7 E, c9 L, c12 L = 40/3 + 50 + 70/3 + 90 + 120
        // = 296
        Assert.assertEquals("Depart Old Chelmsford tonnage", 296, r1l2.getTrainWeight());

        // test route pickup and drop controls
        train3.setRequirements(Train.CABOOSE);
        r1l1.setPickUpAllowed(false);

        c1.setMoves(10);
        c3.setMoves(21);
        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 8", "", c1.getDestinationTrackName());
        Assert.assertEquals("c5 destination 8", "", c5.getDestinationTrackName());
        Assert.assertEquals("c3 destination 8", "", c3.getDestinationTrackName());
        Assert.assertEquals("c13 destination 8", "Bedford Yard 1", c13.getDestinationTrackName());

        r1l1.setPickUpAllowed(true);
        r1l2.setPickUpAllowed(false);
        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 9", "Chelmsford Interchange 1", c1.getDestinationTrackName());
        Assert.assertEquals("c5 destination 9", "", c5.getDestinationTrackName());
        Assert.assertEquals("c3 destination 9", "", c3.getDestinationTrackName());
        Assert.assertEquals("c13 destination 9", "Bedford Yard 1", c13.getDestinationTrackName());

        r1l2.setPickUpAllowed(true);
        r1l2.setDropAllowed(false); // Old Chelmsford
        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 10", "Bedford Yard 1", c1.getDestinationTrackName());
        Assert.assertEquals("c5 destination 10", "", c5.getDestinationTrackName());
        Assert.assertEquals("c3 destination 10", "", c3.getDestinationTrackName());
        Assert.assertEquals("c13 destination 10", "Bedford Yard 1", c13.getDestinationTrackName());

        train3.reset();
        // try forcing c1 to Chelmsford
        c1.setDestination(loc2, null);
        Assert.assertTrue(train3.build());
        Assert.assertEquals("c1 destination Old Chelmsford", "", c1.getDestinationTrackName());

        // confirm that c1 isn't part of this train
        Assert.assertNull("c1 isn't assigned to a train", c1.getTrain());
     // code currently eliminates the car's destination TODO is this correct?
        Assert.assertNull("c1 destination has been set to null", c1.getDestination());
        Assert.assertNull("c1 next destination should be null", c1.getFinalDestination());
        Assert.assertNull("c1 next destination track should be null", c1.getFinalDestinationTrack());

        // try without moves
        r1l2.setDropAllowed(true);
        r1l2.setMaxCarMoves(0);
        c1.setDestination(loc2, null);
        Assert.assertTrue(train3.build());
        Assert.assertEquals("c1 destination Old Chelmsford, no moves", "", c1.getDestinationTrackName());

        c1.setDestination(null, null);
        r1l2.setMaxCarMoves(6);
        r1l3.setDropAllowed(false); // Should be able to drop off caboose
        Assert.assertTrue(train3.build());

        Assert.assertEquals("c1 destination 11", "Chelmsford Interchange 1", c1.getDestinationTrackName());
        Assert.assertEquals("c5 destination 11", "", c5.getDestinationTrackName());
        Assert.assertEquals("c3 destination 11", "", c3.getDestinationTrackName());
        Assert.assertEquals("c13 destination 11", "Bedford Yard 1", c13.getDestinationTrackName());

        // test to see if FRED also get delivered
        train3.setRequirements(Train.FRED);
        Assert.assertEquals("Place c2 at start of route", Track.OKAY, c2.setLocation(loc1, loc1trk2));
        c2.setFred(true);
        Assert.assertTrue(train3.build());

        Assert.assertTrue("Train 3 built", train3.isBuilt());
        Assert.assertEquals("c1 destination 12", "Chelmsford Interchange 1", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 12", "Bedford Yard 1", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 12", "", c3.getDestinationTrackName());
        Assert.assertEquals("c13 destination 12", "", c13.getDestinationTrackName());

        // move and terminate
        train3.move();
        train3.move();
        train3.move();
        train3.move();

        Assert.assertEquals("c1 track 12", "Chelmsford Interchange 1", c1.getTrackName());
        Assert.assertEquals("c2 track 12", "Bedford Yard 1", c2.getTrackName());
        Assert.assertEquals("c3 track 12", "Westford Yard 1", c3.getTrackName());
        Assert.assertEquals("c13 track 12", "Westford Yard 1", c13.getTrackName());

        // test previous car delivered pickup interchange operation
        loc2trk1.setDropOption(Track.ANY);
        loc2trk1.setPickupOption(Track.TRAINS);
        loc2trk2.setDropOption(Track.ANY);
        loc2trk2.setPickupOption(Track.TRAINS);

        // Place car with FRED back at start of route
        Assert.assertEquals("Place c2 again", Track.OKAY, c2.setLocation(loc1, loc1trk2));

        Assert.assertTrue(train3.build());
        Assert.assertTrue("train 3 should build", train3.isBuilt());
        Assert.assertEquals("car BM Q1 should not be part of train", null, c1.getTrain());
        Assert.assertEquals("car XP Q3 should be part of train", train3, c3.getTrain());

        // put some cars at start of Route
        Assert.assertEquals("Place c1 again", Track.OKAY, c1.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place c2 again", Track.OKAY, c2.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place c3 again", Track.OKAY, c3.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place c4 again", Track.OKAY, c4.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place c6 again", Track.OKAY, c6.setLocation(loc2, loc2trk1));
        Assert.assertEquals("Place c7 again", Track.OKAY, c7.setLocation(loc2, loc2trk2));

        r1l1.setMaxCarMoves(5);
        loc2trk1.setDropOption(Track.ROUTES);
        loc2trk1.setPickupOption(Track.ROUTES);
        loc2trk2.setDropOption(Track.ROUTES);
        loc2trk2.setPickupOption(Track.ROUTES);

        Assert.assertTrue(train3.build());
        Assert.assertTrue("train 3 should build", train3.isBuilt());
        Assert.assertEquals("BM Q1 in train", null, c1.getTrain());
        Assert.assertEquals("UP Q2 in train", train3, c2.getTrain());
        Assert.assertEquals("XP Q3 in train", train3, c3.getTrain());
        Assert.assertEquals("PU Q4 in train", train3, c4.getTrain());
        Assert.assertEquals("UP Q5 in train", null, c5.getTrain());
        Assert.assertEquals("CP Q6 in train", null, c6.getTrain());
        Assert.assertEquals("UP Q7 in train", null, c7.getTrain());

        Assert.assertEquals("UP Q2 destination", "Bedford Yard 1", c2.getDestinationTrackName());
        Assert.assertEquals("XP Q3 destination", "Chelmsford Freight 4", c3.getDestinationTrackName());
        Assert.assertEquals("PU Q4 destination", "Chelmsford Yard 3", c4.getDestinationTrackName());
        Assert.assertEquals("UP Q5 destination", "", c5.getDestinationTrackName());

        // interchange testing done, now test replace car type and road
        Assert.assertTrue("loc1 should accept Boxcar", loc1.acceptsTypeName("Boxcar"));
        Assert.assertTrue("loc2 should accept Boxcar", loc2.acceptsTypeName("Boxcar"));

        // replace should modify locations and trains
        ct.replaceName("Boxcar", "boxcar");

        Assert.assertFalse("loc1 should not accept Boxcar", loc1.acceptsTypeName("Boxcar"));
        Assert.assertFalse("loc2 should not accept Boxcar", loc2.acceptsTypeName("Boxcar"));
        Assert.assertFalse("track loc1trk1 should not accept Boxcar", loc1trk1.isTypeNameAccepted("Boxcar"));
        Assert.assertFalse("track loc2trk1 should not accept Boxcar", loc2trk1.isTypeNameAccepted("Boxcar"));
        Assert.assertFalse("train 1 should not accept Boxcar", train1.isTypeNameAccepted("Boxcar"));
        Assert.assertFalse("train 2 should not accept Boxcar", train2.isTypeNameAccepted("Boxcar"));
        Assert.assertFalse("train 3 should not accept Boxcar", train3.isTypeNameAccepted("Boxcar"));

        Assert.assertTrue("loc1 should accept boxcar", loc1.acceptsTypeName("boxcar"));
        Assert.assertTrue("loc2 should accept boxcar", loc2.acceptsTypeName("boxcar"));
        Assert.assertTrue("track loc1trk1 should accept boxcar", loc1trk1.isTypeNameAccepted("boxcar"));
        Assert.assertTrue("track loc2trk1 should accept boxcar", loc2trk1.isTypeNameAccepted("boxcar"));
        Assert.assertTrue("train 1 should accept boxcar", train1.isTypeNameAccepted("boxcar"));
        Assert.assertTrue("train 2 should accept boxcar", train2.isTypeNameAccepted("boxcar"));
        Assert.assertTrue("train 3 should accept boxcar", train3.isTypeNameAccepted("boxcar"));

        ct.replaceName("boxcar", "Boxcar");

        Assert.assertTrue("loc1 should accept Boxcar", loc1.acceptsTypeName("Boxcar"));
        Assert.assertTrue("loc2 should accept Boxcar", loc2.acceptsTypeName("Boxcar"));
        Assert.assertTrue("track loc1trk1 should accept Boxcar", loc1trk1.isTypeNameAccepted("Boxcar"));
        Assert.assertTrue("track loc2trk1 should accept Boxcar", loc2trk1.isTypeNameAccepted("Boxcar"));
        Assert.assertTrue("train 1 should accept Boxcar", train1.isTypeNameAccepted("Boxcar"));
        Assert.assertTrue("train 2 should accept Boxcar", train2.isTypeNameAccepted("Boxcar"));
        Assert.assertTrue("train 3 should accept Boxcar", train3.isTypeNameAccepted("Boxcar"));

        // now test road name replace
        CarRoads cr = InstanceManager.getDefault(CarRoads.class);
        cr.addName("CP");

        loc1trk1.setRoadOption(Track.INCLUDE_ROADS);
        loc1trk1.addRoadName("CP");
        loc1trk1.addRoadName("PC");
        train1.setCarRoadOption(Train.INCLUDE_ROADS);
        train1.addCarRoadName("CP");
        train1.addCarRoadName("PC");
        train1.setCabooseRoad("CP");
        train1.setEngineRoad("CP");

        Assert.assertTrue("track loc1trk1 should accept road CP", loc1trk1.isRoadNameAccepted("CP"));
        Assert.assertTrue("track loc1trk1 should accept road PC", loc1trk1.isRoadNameAccepted("PC"));
        Assert.assertFalse("track loc1trk1 should Not accept road PC", loc1trk1.isRoadNameAccepted("UP"));
        Assert.assertTrue("Train 1 should accept road CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertTrue("Train 1 should accept road PC", train1.isCarRoadNameAccepted("PC"));
        Assert.assertFalse("Train 1 should Not accept road UP", train1.isCarRoadNameAccepted("UP"));
        Assert.assertEquals("Caboose road", "CP", train1.getCabooseRoad());
        Assert.assertEquals("Engine road", "CP", train1.getEngineRoad());

        cr.replaceName("CP", "UP");

        Assert.assertFalse("after replace track loc1trk1 should Not accept road CP", loc1trk1.isRoadNameAccepted("CP"));
        Assert.assertTrue("after replace track loc1trk1 should accept road PC", loc1trk1.isRoadNameAccepted("PC"));
        Assert.assertTrue("after replace track loc1trk1 should accept road PC", loc1trk1.isRoadNameAccepted("UP"));
        Assert.assertFalse("after replace Train 1 should Not accept road CP", train1.isCarRoadNameAccepted("CP"));
        Assert.assertTrue("after replace Train 1 should accept road PC", train1.isCarRoadNameAccepted("PC"));
        Assert.assertTrue("after replace Train 1 should accept road UP", train1.isCarRoadNameAccepted("UP"));
        Assert.assertEquals("Caboose road", "UP", train1.getCabooseRoad());
        Assert.assertEquals("Engine road", "UP", train1.getEngineRoad());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testCabooseAndCarWithFred() {

        // register the road names used
        CarRoads cr = InstanceManager.getDefault(CarRoads.class);
        cr.addName("UP");
        cr.addName("SP");
        cr.addName("NH");

        // place two engines in a consist
        Consist con1 = InstanceManager.getDefault(ConsistManager.class).newConsist("C1");

        Engine e1 = emanager.newRS("UP", "1");
        e1.setModel("GP40");
        e1.setConsist(con1);
        Engine e2 = emanager.newRS("SP", "2");
        e2.setModel("GP40");
        e2.setConsist(con1);

        // Create 3 locations
        Location loc1 = lmanager.newLocation("Harvard");
        Track loc1trk1 = loc1.addTrack("Harvard Yard", Track.YARD);
        loc1trk1.setLength(1000);

        Location loc2 = lmanager.newLocation("Arlington");
        Track loc2trk1 = loc2.addTrack("Arlington Yard", Track.YARD);
        loc2trk1.setLength(1000);

        Location loc3 = lmanager.newLocation("Boston");
        Track loc3trk1 = loc3.addTrack("Boston Yard", Track.YARD);
        loc3trk1.setLength(1000);
        loc3trk1.deleteTypeName("Diesel");
        Track loc3trk2 = loc3.addTrack("Boston Engine Yard", Track.YARD);
        loc3trk2.setLength(200);
        loc3trk2.deleteTypeName("Boxcar");
        loc3trk2.deleteTypeName("Flat");
        loc3trk2.deleteTypeName(Bundle.getMessage("Caboose"));

        // Create route with 3 location
        Route rte1 = rmanager.newRoute("Route 2 Boston");
        RouteLocation rl1 = rte1.addLocation(loc1);
        RouteLocation rl2 = rte1.addLocation(loc2);
        rl2.setTrainIconX(75); // set the train icon coordinates
        rl2.setTrainIconY(150);
        RouteLocation rl3 = rte1.addLocation(loc3);
        rl3.setTrainIconX(125); // set the train icon coordinates
        rl3.setTrainIconY(150);

        // Create train
        Train train1 = tmanager.newTrain("HTB");
        train1.setRoute(rte1);

        // Place cars
        // Set up three cabooses and six box cars
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("UP", "1", Bundle.getMessage("Caboose"), "32", loc1trk1, 10);
        c1.setCaboose(true);

        Car c2 = JUnitOperationsUtil.createAndPlaceCar("SP", "2", Bundle.getMessage("Caboose"), "30", loc1trk1, 5);
        c2.setCaboose(true);

        Car c3 = JUnitOperationsUtil.createAndPlaceCar("NH", "3", Bundle.getMessage("Caboose"), "33", loc1trk1, 0);
        c3.setCaboose(true);

        Car c4 = JUnitOperationsUtil.createAndPlaceCar("UP", "4", "Boxcar", "40", loc1trk1, 16);
        c4.setFred(true);

        Car c5 = JUnitOperationsUtil.createAndPlaceCar("SP", "5", "Boxcar", "40", loc1trk1, 8);
        c5.setFred(true);

        Car c6 = JUnitOperationsUtil.createAndPlaceCar("NH", "6", "Boxcar", "40", loc1trk1, 2);
        c6.setFred(true);

        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "7", "Flat", "40", loc1trk1, 5);
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("SP", "8", "Boxcar", "40", loc1trk1, 4);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("NH", "9", "Boxcar", "40", loc1trk1, 3);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("NH", "10", "Boxcar", "40", loc2trk1, 10);
        Car c11 = JUnitOperationsUtil.createAndPlaceCar("SP", "11", "Boxcar", "40", loc2trk1, 3);

        // Place engines
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e2", Track.OKAY, e2.setLocation(loc1, loc1trk1));

        // no requirements, so no caboose or FRED or engines
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 1", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 1", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 1", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 1", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 1", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 1", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 1", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 1", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 1", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 1", "Boston Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 1", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 1", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 1", "", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 1", "", e2.getDestinationTrackName());

        // no engines, so the caboose with least moves should be used
        train1.setRequirements(Train.CABOOSE);
        // don't allow pickups at second location Arlington
        rl2.setPickUpAllowed(false);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 2", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 2", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 2", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 2", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 2", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 2", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 2", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 2", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 2", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 2", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 2", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 2", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 2", "", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 2", "", e2.getDestinationTrackName());

        // there's a caboose c1 that matches lead engine
        train1.setNumberEngines("2");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 3", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 3", "Boston Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 3", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 3", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 3", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 3", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 3", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 3", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 3", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 3", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 3", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 3", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 3", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 3", "Boston Engine Yard", e2.getDestinationTrackName());

        // should default to the caboose with the least moves
        e1.setRoadName("X");
        // allow pickups at Arlington
        rl2.setPickUpAllowed(true);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 4", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 4", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 4", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 4", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 4", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 4", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 4", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 4", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 4", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 4", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 4", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 4", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 4", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 4", "Boston Engine Yard", e2.getDestinationTrackName());

        // don't allow drops at Boston, caboose and engines should still drop there
        rl3.setDropAllowed(false);
        // should not take NH caboose
        e1.setRoadName("NH");
        // now require a SP caboose
        train1.setCabooseRoad("SP");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 5", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 5", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 5", "Boston Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 5", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 5", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 5", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 5", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 5", "Arlington Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 5", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 5", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 5", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 5", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 5", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 5", "Boston Engine Yard", e2.getDestinationTrackName());

        // allow drops at Boston
        rl3.setDropAllowed(true);
        // should take car with FRED and road SP
        train1.setRequirements(Train.FRED);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 6", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 6", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 6", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 6", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 6", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 6", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 6", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 6", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 6", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 6", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 6", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 6", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 6", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 6", "Boston Engine Yard", e2.getDestinationTrackName());

        // should take car with FRED least number of moves
        train1.setCabooseRoad("");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 7", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 7", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 7", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 7", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 7", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 7", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 7", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 7", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 7", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 7", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 7", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 7", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 7", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 7", "Boston Engine Yard", e2.getDestinationTrackName());

        // now exclude road NH, engine road is NH and should be rejected
        train1.addLocoRoadName("NH");
        train1.setLocoRoadOption(Train.EXCLUDE_ROADS);
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 7a", false, train1.isBuilt());
        // now override by setting a road for the engine
        train1.setLocoRoadOption(Train.ALL_ROADS);
        train1.addCarRoadName("NH");
        train1.setCarRoadOption(Train.EXCLUDE_ROADS);
        train1.setEngineRoad("NH");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 8", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 8", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 8", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 8", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 8", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 8", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 8", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 8", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 8", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 8", "", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 8", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 8", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 8", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 8", "Boston Engine Yard", e2.getDestinationTrackName());

        // now only include NH
        train1.setCarRoadOption(Train.INCLUDE_ROADS);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 9", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 9", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 9", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 9", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 9", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 9", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 9", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 9", "", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 9", "", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 9", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 9", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 9", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 9", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 9", "Boston Engine Yard", e2.getDestinationTrackName());

        // don't allow boxcar, car with FRED required, build should fail
        loc3.deleteTypeName("Boxcar");
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 9a", false, train1.isBuilt());
        loc3.addTypeName("Boxcar");

        // add staging
        Track loc1trk2 = loc1.addTrack("Harvard Staging", Track.STAGING);
        loc1trk2.setLength(1000);
        // now depart staging, must take all cars in staging
        rl1.setMaxCarMoves(9); // there are nine cars departing staging
        // Place cars
        Assert.assertEquals("Move c1", Track.OKAY, c1.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Move c2", Track.OKAY, c2.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Move c3", Track.OKAY, c3.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Move c4", Track.OKAY, c4.setLocation(loc1, loc1trk2));

        Assert.assertEquals("Move c5", Track.OKAY, c5.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Move c6", Track.OKAY, c6.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Move c7", Track.OKAY, c7.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Move c8", Track.OKAY, c8.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Move c9", Track.OKAY, c9.setLocation(loc1, loc1trk2));

        // Place engines
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place e2", Track.OKAY, e2.setLocation(loc1, loc1trk2));

        // program requires only staging at any location, so we don't test with yard
        loc1.deleteTrack(loc1trk1);
        // All engines and cars in staging must move! Cabooses and cars with FRED to
        // terminal
        train1.setNumberEngines("0");
        Assert.assertFalse(train1.build());
        // train only accepted engine and cars with NH road therefore build should fail
        Assert.assertEquals("Train 1 After Build from staging", false, train1.isBuilt());
        // try again but now accept all roads
        train1.setCarRoadOption(Train.ALL_ROADS);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 10", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 10", "Boston Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 10", "Boston Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 10", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 10", "Boston Yard", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 10", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 10", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 10", "Arlington Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 10", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 10", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 10", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 10", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 10", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 10", "Boston Engine Yard", e2.getDestinationTrackName());

        // exclude road NH
        train1.setCarRoadOption(Train.EXCLUDE_ROADS);
        Assert.assertFalse(train1.build());
        // should fail since there are NH roads in staging
        Assert.assertEquals("Train 1 After Build 11", false, train1.isBuilt());

        // reduce Boston moves to 6, to force non caboose and FRED cars to Arlington
        rl3.setMaxCarMoves(6);
        train1.setCarRoadOption(Train.ALL_ROADS);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 12", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 12", "Boston Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 12", "Boston Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 12", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 12", "Boston Yard", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 12", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 12", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 12", "Arlington Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 12", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 12", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 12", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 12", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 12", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 12", "Boston Engine Yard", e2.getDestinationTrackName());

        // Reduce Arlington to only two moves, this should cause train build to fail
        rl2.setMaxCarMoves(2);

        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 13", false, train1.isBuilt());

        // restore number of moves
        rl2.setMaxCarMoves(7);
        rl3.setMaxCarMoves(7);
        // don't allow drops at Boston
        rl3.setDropAllowed(false);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 14", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 14", "Boston Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 14", "Boston Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 14", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 14", "Boston Yard", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 14", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 14", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 14", "Arlington Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 14", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 14", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 14", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 14", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 14", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 14", "Boston Engine Yard", e2.getDestinationTrackName());

        // Reduce Arlington to only two moves, this should cause train build to fail
        rl2.setMaxCarMoves(2);
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 15", false, train1.isBuilt());

        // Don't allow cabooses at Boston, should cause build failure
        rl2.setMaxCarMoves(7);
        loc3.deleteTypeName(Bundle.getMessage("Caboose"));
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 16", false, train1.isBuilt());

        // Don't allow boxcars, should also cause build failure
        loc3.addTypeName(Bundle.getMessage("Caboose"));
        loc3.deleteTypeName("Boxcar");
        train1.setRequirements(Train.NO_CABOOSE_OR_FRED);
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 17", false, train1.isBuilt());

        // allow the three road names we're testing
        loc3.addTypeName("Boxcar");
        loc3trk1.addRoadName("NH");
        loc3trk1.addRoadName("SP");
        loc3trk1.addRoadName("UP");
        loc3trk1.setRoadOption(Track.INCLUDE_ROADS);
        loc3trk2.addRoadName("NH");
        loc3trk2.addRoadName("SP");
        loc3trk2.addRoadName("UP");
        loc3trk2.setRoadOption(Track.INCLUDE_ROADS);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 18", true, train1.isBuilt());

        // now remove type Diesel, this should cause a failure
        loc3trk2.deleteTypeName("Diesel");
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 19", false, train1.isBuilt());

        // now restore type Diesel
        loc3trk2.addTypeName("Diesel");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 20", true, train1.isBuilt());

        // Set the track length too short missing one set of couplers, two engines
        loc3trk2.setLength(Integer.parseInt(e1.getLength()) + Integer.parseInt(e2.getLength()) + Engine.COUPLERS);
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 20.1", false, train1.isBuilt());

        // restore track length
        loc3trk2.setLength(Integer.parseInt(e1.getLength()) + Integer.parseInt(e2.getLength()) + 2 * Engine.COUPLERS);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 20.2", true, train1.isBuilt());

        // change lead engine road name, should cause build failure since Boston only
        // accepts NH, SP, and UP.
        train1.setEngineRoad(""); // reset engine road requirements, was "NH"
        e1.setRoadName("X"); // was "NH"
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 21", false, train1.isBuilt());

        e1.setRoadName("UP");
        loc3trk1.deleteRoadName("NH"); // this test that a caboose fails
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 22", false, train1.isBuilt());

        loc3trk1.addRoadName("NH");
        c6.setRoadName("X"); // this test that a car with FRED fails
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 23", false, train1.isBuilt());

        loc3trk1.addRoadName("X");
        loc2trk1.deleteTypeName("Flat"); // this test that an ordinary car must move
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 24", false, train1.isBuilt());

        loc2trk1.addTypeName("Flat"); // restore
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 25", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 25", "Boston Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 25", "Boston Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 25", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 25", "Boston Yard", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 25", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 25", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 25", "Arlington Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 25", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 25", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 25", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 25", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 25", "Boston Engine Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 25", "Boston Engine Yard", e2.getDestinationTrackName());

        train1.reset();
        // send caboose SP 2 from staging to track that will not service it
        loc3trk2.addTypeName(Bundle.getMessage("Caboose"));
        loc3trk2.setLength(200);
        c2.setDestination(loc3, loc3trk2);
        loc3trk2.deleteTypeName(Bundle.getMessage("Caboose"));
        Assert.assertFalse(train1.build());

        Assert.assertEquals("Train 1 After Build with caboose bad destination", false, train1.isBuilt());
        c2.setDestination(null, null);
        Assert.assertTrue(train1.build());

        train1.move();
        train1.move();
        train1.move();

        // check final locations
        Assert.assertEquals("c1 location 24", "Boston Yard", c1.getTrackName());
        Assert.assertEquals("c2 location 24", "Boston Yard", c2.getTrackName());
        Assert.assertEquals("c3 location 24", "Boston Yard", c3.getTrackName());
        Assert.assertEquals("c4 location 24", "Boston Yard", c4.getTrackName());

        Assert.assertEquals("c5 location 24", "Boston Yard", c5.getTrackName());
        Assert.assertEquals("c6 location 24", "Boston Yard", c6.getTrackName());
        Assert.assertEquals("c7 location 24", "Arlington Yard", c7.getTrackName());
        Assert.assertEquals("c8 location 24", "Arlington Yard", c8.getTrackName());

        Assert.assertEquals("c9 location 24", "Arlington Yard", c9.getTrackName());
        Assert.assertEquals("c10 location 24", "Arlington Yard", c10.getTrackName());
        Assert.assertEquals("c11 location 24", "Arlington Yard", c11.getTrackName());

        Assert.assertEquals("e1 location 24", "Boston Engine Yard", e1.getTrackName());
        Assert.assertEquals("e2 location 24", "Boston Engine Yard", e2.getTrackName());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testCarWithFred() {

        Setup.setStagingTrainCheckEnabled(false);

        // register the road names used
        CarRoads cr = InstanceManager.getDefault(CarRoads.class);
        cr.addName("UP");
        cr.addName("SP");
        cr.addName("NH");

        CarLoads cl = InstanceManager.getDefault(CarLoads.class);
        cl.addName("Boxcar", "boxes");
        cl.addName("Caboose", "Crew");

        Engine e1 = emanager.newRS("UP", "1");
        e1.setModel("GP40");

        // Create 3 locations
        Location harvard = lmanager.newLocation("Harvard");
        Track harvardTrack = harvard.addTrack("Harvard Yard", Track.YARD);
        harvardTrack.setLength(1000);

        Location arlington = lmanager.newLocation("Arlington");
        Track loc2trk1 = arlington.addTrack("Arlington Yard", Track.YARD);
        loc2trk1.setLength(1000);

        Location boston = lmanager.newLocation("Boston");
        Track bostonTrack = boston.addTrack("Boston Yard", Track.STAGING);
        bostonTrack.setLength(1000);

        // Create route with 3 location
        Route rte1 = rmanager.newRoute("Route 2 Boston");
        RouteLocation rl1 = rte1.addLocation(harvard);
        rl1.setMaxCarMoves(15);
        rte1.addLocation(arlington);
        RouteLocation rl3 = rte1.addLocation(boston);
        rl3.setMaxCarMoves(15);

        // Create train
        Train train1 = tmanager.newTrain("HTB with FRED");
        train1.setRoute(rte1);

        // Place cars
        // Set up three cabooses and six box cars, three with FREDs
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("UP", "1", "Caboose", "32", harvardTrack, 10);
        c1.setCaboose(true);

        Car c2 = JUnitOperationsUtil.createAndPlaceCar("SP", "2", "Caboose", "30", harvardTrack, 5);
        c2.setCaboose(true);

        Car c3 = JUnitOperationsUtil.createAndPlaceCar("NH", "3", "Caboose", "33", harvardTrack, 0);
        c3.setCaboose(true);

        Car c4 = JUnitOperationsUtil.createAndPlaceCar("UP", "4", "Boxcar", "40", harvardTrack, 16);
        c4.setFred(true);

        Car c5 = JUnitOperationsUtil.createAndPlaceCar("SP", "5", "Boxcar", "40", harvardTrack, 8);
        c5.setFred(true);

        Car c6 = JUnitOperationsUtil.createAndPlaceCar("NH", "6", "Boxcar", "40", harvardTrack, 2);
        c6.setFred(true);

        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "7", "Flat", "40", harvardTrack, 5);
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("SP", "8", "Boxcar", "40", harvardTrack, 4);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("NH", "9", "Boxcar", "40", harvardTrack, 3);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("NH", "10", "Boxcar", "40", loc2trk1, 10);
        Car c11 = JUnitOperationsUtil.createAndPlaceCar("SP", "11", "Boxcar", "40", loc2trk1, 3);

        // Place engines
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(harvard, harvardTrack));

        // no requirements, so no caboose or FRED or engines
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 1", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 1", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 1", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 1", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 1", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 1", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 1", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 1", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 1", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 1", "Boston Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 1", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 1", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 1", "", e1.getDestinationTrackName());

        // should take car with FRED and road SP
        train1.setRequirements(Train.FRED);
        train1.setCabooseRoad("SP");
        train1.setNumberEngines("1");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 2", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 2", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 2", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 2", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 2", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 2", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 2", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 2", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 2", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 2", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 2", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 2", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 2", "Boston Yard", e1.getDestinationTrackName());

        Assert.assertEquals("c5 load name 2", "E", c5.getLoadName());

        // should take car with FRED least number of moves
        train1.setCabooseRoad("");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 3", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 3", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 3", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 3", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 3", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 3", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 3", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 3", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 3", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 3", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 3", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 3", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 3", "Boston Yard", e1.getDestinationTrackName());

        Assert.assertEquals("load name 3", "E", c6.getLoadName());

        // now depart staging, all cars must go!
        harvardTrack.setTrackType(Track.STAGING);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 4", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 4", "Boston Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 4", "Boston Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 4", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 4", "Boston Yard", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 4", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 4", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 4", "Boston Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 4", "Arlington Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 4", "Arlington Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 4", "Boston Yard", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 4", "Boston Yard", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 4", "Boston Yard", e1.getDestinationTrackName());

        Assert.assertEquals("c1 load name 4", "E", c1.getLoadName());
        Assert.assertEquals("c2 load name 4", "E", c2.getLoadName());
        Assert.assertEquals("c3 load name 4", "E", c3.getLoadName());
        Assert.assertEquals("c4 load name 4", "E", c4.getLoadName());
        Assert.assertEquals("c5 load name 4", "E", c5.getLoadName());
        Assert.assertEquals("c6 load name 4", "E", c6.getLoadName());

        // don't allow cars with "E" into staging. build should fail
        bostonTrack.addLoadName("E");
        bostonTrack.setLoadOption(Track.EXCLUDE_LOADS);
        train1.build();
        Assert.assertFalse(train1.build());

        // generate custom loads out of staging
        harvardTrack.setAddCustomLoadsAnyStagingTrackEnabled(true);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 4", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 5", "Boston Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 5", "Boston Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 5", "Boston Yard", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 5", "Boston Yard", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 5", "Boston Yard", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 5", "Boston Yard", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 5", "Arlington Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 5", "Boston Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 5", "Boston Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 5", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 5", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 5", "Boston Yard", e1.getDestinationTrackName());

        Assert.assertEquals("c1 load name 5", "Crew", c1.getLoadName());
        Assert.assertEquals("c2 load name 5", "Crew", c2.getLoadName());
        Assert.assertEquals("c3 load name 5", "Crew", c3.getLoadName());
        Assert.assertEquals("c4 load name 5", "boxes", c4.getLoadName());
        Assert.assertEquals("c5 load name 5", "boxes", c5.getLoadName());
        Assert.assertEquals("c6 load name 5", "boxes", c6.getLoadName());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testCabooseAndCarWithFredOutOfStaging() {

        // register the road names used
        CarRoads cr = InstanceManager.getDefault(CarRoads.class);
        cr.addName("UP");
        cr.addName("SP");
        cr.addName("NH");

        CarLoads cl = InstanceManager.getDefault(CarLoads.class);
        cl.addName("Boxcar", "boxes");
        cl.addName("Boxcar", "Bolts");
        cl.addName("Caboose", "Crew");
        cl.addName("Caboose", "crew");

        Engine e1 = emanager.newRS("UP", "1");
        e1.setModel("GP40");

        // Create 3 locations
        Location harvard = lmanager.newLocation("Harvard");
        Track harvardTrack = harvard.addTrack("Harvard Staging", Track.STAGING);
        harvardTrack.setLength(1000);

        Location arlington = lmanager.newLocation("Arlington");
        Track arlingtonTrack = arlington.addTrack("Arlington Spur", Track.SPUR);
        arlingtonTrack.setLength(1000);

        Location boston = lmanager.newLocation("Boston");
        Track bostonTrack = boston.addTrack("Boston Spur", Track.SPUR);
        bostonTrack.setLength(1000);

        // Create route with 3 location
        Route rte1 = rmanager.newRoute("Route 2 Boston");
        RouteLocation rl1 = rte1.addLocation(harvard);
        rl1.setMaxCarMoves(15);
        RouteLocation rl2 = rte1.addLocation(arlington);
        RouteLocation rl3 = rte1.addLocation(boston);
        rl3.setMaxCarMoves(15);

        // Create train
        Train train1 = tmanager.newTrain("HTB out of staging");
        train1.setRoute(rte1);

        // Place cars
        // Set up three cabooses and six box cars, three with FREDs
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("UP", "1", "Caboose", "32", harvardTrack, 10);
        c1.setCaboose(true);

        Car c2 = JUnitOperationsUtil.createAndPlaceCar("SP", "2", "Caboose", "30", harvardTrack, 5);
        c2.setCaboose(true);

        Car c3 = JUnitOperationsUtil.createAndPlaceCar("NH", "3", "Caboose", "33", harvardTrack, 0);
        c3.setCaboose(true);

        Car c4 = JUnitOperationsUtil.createAndPlaceCar("UP", "4", "Boxcar", "40", harvardTrack, 16);
        c4.setFred(true);

        Car c5 = JUnitOperationsUtil.createAndPlaceCar("SP", "5", "Boxcar", "40", harvardTrack, 8);
        c5.setFred(true);

        Car c6 = JUnitOperationsUtil.createAndPlaceCar("NH", "6", "Boxcar", "40", harvardTrack, 2);
        c6.setFred(true);

        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "7", "Boxcar", "40", harvardTrack, 5);
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("SP", "8", "Boxcar", "40", harvardTrack, 4);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("NH", "9", "Boxcar", "40", harvardTrack, 3);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("NH", "10", "Boxcar", "40", arlingtonTrack, 10);
        Car c11 = JUnitOperationsUtil.createAndPlaceCar("SP", "11", "Boxcar", "40", arlingtonTrack, 3);

        // Place engines
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(harvard, harvardTrack));

        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 1", true, train1.isBuilt());

        // check destinations, all cars in staging must depart
        Assert.assertEquals("c1 destination 1", "Boston Spur", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 1", "Boston Spur", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 1", "Boston Spur", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 1", "Boston Spur", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 1", "Boston Spur", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 1", "Boston Spur", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 1", "Boston Spur", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 1", "Arlington Spur", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 1", "Arlington Spur", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 1", "Boston Spur", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 1", "Boston Spur", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 1", "Boston Spur", e1.getDestinationTrackName());

        Assert.assertEquals("c1 load name 1", "E", c1.getLoadName());
        Assert.assertEquals("c2 load name 1", "E", c2.getLoadName());
        Assert.assertEquals("c3 load name 1", "E", c3.getLoadName());
        Assert.assertEquals("c4 load name 1", "E", c4.getLoadName());
        Assert.assertEquals("c5 load name 1", "E", c5.getLoadName());
        Assert.assertEquals("c6 load name 1", "E", c6.getLoadName());

        // don't allow cars with "E" into Boston. build should fail
        bostonTrack.addLoadName("E");
        bostonTrack.setLoadOption(Track.EXCLUDE_LOADS);
        train1.build();
        Assert.assertFalse(train1.build());

        // generate custom loads out of staging to staging
        harvardTrack.setAddCustomLoadsAnyStagingTrackEnabled(true);
        Assert.assertFalse(train1.build()); // should still fail

        harvardTrack.setAddCustomLoadsEnabled(true);
        Assert.assertFalse(train1.build()); // should still fail no schedule demanding custom loads

        ScheduleManager smanager = InstanceManager.getDefault(ScheduleManager.class);
        Schedule schedule1 = smanager.newSchedule("Schedule 1");
        ScheduleItem sch1Item1 = schedule1.addItem("Boxcar");
        sch1Item1.setReceiveLoadName("boxes");
        ScheduleItem sch1Item2 = schedule1.addItem("Caboose");
        sch1Item2.setReceiveLoadName("Crew");
        bostonTrack.setSchedule(schedule1);

        Schedule schedule2 = smanager.newSchedule("Schedule 2");
        ScheduleItem sch2Item1 = schedule2.addItem("Boxcar");
        sch2Item1.setReceiveLoadName("Bolts");
        ScheduleItem sch2Item2 = schedule2.addItem("Caboose");
        sch2Item2.setReceiveLoadName("crew");
        arlingtonTrack.setSchedule(schedule2);

        train1.build();
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 2", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 2", "Boston Spur", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 2", "Boston Spur", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 2", "Boston Spur", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 2", "Boston Spur", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 2", "Boston Spur", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 2", "Boston Spur", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 2", "Arlington Spur", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 2", "Arlington Spur", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 2", "Arlington Spur", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 2", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 2", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 2", "Boston Spur", e1.getDestinationTrackName());

        Assert.assertEquals("c1 load name 2", "Crew", c1.getLoadName());
        Assert.assertEquals("c2 load name 2", "Crew", c2.getLoadName());
        Assert.assertEquals("c3 load name 2", "Crew", c3.getLoadName());
        Assert.assertEquals("c4 load name 2", "boxes", c4.getLoadName());
        Assert.assertEquals("c5 load name 2", "boxes", c5.getLoadName());
        Assert.assertEquals("c6 load name 2", "boxes", c6.getLoadName());

        // now remove caboose at Arlington
        train1.setSecondLegOptions(Train.REMOVE_CABOOSE);
        train1.setSecondLegStartRouteLocation(rl2);

        train1.build();
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 3", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 3", "Arlington Spur", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 3", "Arlington Spur", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 3", "Arlington Spur", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 3", "Arlington Spur", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 3", "Arlington Spur", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 3", "Arlington Spur", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 3", "Boston Spur", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 3", "Boston Spur", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 3", "Arlington Spur", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 3", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 3", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 3", "Boston Spur", e1.getDestinationTrackName());

        Assert.assertEquals("c1 load name 3", "crew", c1.getLoadName());
        Assert.assertEquals("c2 load name 3", "crew", c2.getLoadName());
        Assert.assertEquals("c3 load name 3", "crew", c3.getLoadName());
        Assert.assertEquals("c4 load name 3", "Bolts", c4.getLoadName());
        Assert.assertEquals("c5 load name 3", "Bolts", c5.getLoadName());
        Assert.assertEquals("c6 load name 3", "Bolts", c6.getLoadName());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    /**
     * Test owner names, built dates, engine type, cabooses, route drops & pulls,
     * car unreachable destination out of staging, train / route direction control.
     */
    @Test
    public void testTrainBuildOptions() {

        // create 2 consists and a single engine for testing
        Consist con1 = InstanceManager.getDefault(ConsistManager.class).newConsist("C1");

        Engine e1 = emanager.newRS("UP", "1");
        e1.setModel("GP30");
        e1.setOwnerName("AT");
        e1.setBuilt("1957");
        e1.setConsist(con1);
        e1.setMoves(5);
        Engine e2 = emanager.newRS("SP", "2");
        e2.setModel("GP30");
        e2.setOwnerName("AT");
        e2.setBuilt("1957");
        e2.setConsist(con1);
        e2.setMoves(5);

        // single engine
        Engine e3 = emanager.newRS("SP", "3");
        e3.setModel("GP40");
        e3.setOwnerName("DAB");
        e3.setBuilt("1957");

        Consist con2 = InstanceManager.getDefault(ConsistManager.class).newConsist("C2");

        Engine e4 = emanager.newRS("UP", "10");
        e4.setModel("GP40");
        e4.setOwnerName("DAB");
        e4.setBuilt("1944");
        e4.setConsist(con2);
        e4.setMoves(20);
        Engine e5 = emanager.newRS("SP", "20");
        e5.setModel("GP40");
        e5.setOwnerName("DAB");
        e5.setBuilt("1944");
        e5.setConsist(con2);
        e5.setMoves(20);

        // 3 engine consist
        Consist con3 = InstanceManager.getDefault(ConsistManager.class).newConsist("C3");

        Engine e6 = emanager.newRS("UP", "100");
        e6.setModel("GP40");
        e6.setOwnerName("DAB");
        e6.setBuilt("1944");
        e6.setConsist(con3);
        e6.setMoves(2);
        Engine e7 = emanager.newRS("SP", "200");
        e7.setModel("GP40");
        e7.setOwnerName("DAB");
        e7.setBuilt("1944");
        e7.setConsist(con3);
        e7.setMoves(2);
        Engine e8 = emanager.newRS("SP", "300");
        e8.setModel("GP40");
        e8.setOwnerName("DAB");
        e8.setBuilt("1944");
        e8.setConsist(con3);
        e8.setMoves(2);

        // Create 5 locations
        Location loc1 = lmanager.newLocation("Harvard");
        Track loc1trk1 = loc1.addTrack("Harvard Yard", Track.YARD);
        loc1trk1.setLength(1000);
        Track loc1trk2 = loc1.addTrack("Harvard Yard 2", Track.YARD);
        loc1trk2.setLength(1000);

        Location loc2 = lmanager.newLocation("Arlington");
        Track loc2trk1 = loc2.addTrack("Arlington Yard", Track.YARD);
        loc2trk1.setLength(1000);

        Location loc3 = lmanager.newLocation("Boston");
        Track loc3trk1 = loc3.addTrack("Boston Yard", Track.YARD);
        loc3trk1.setLength(1000);
        Track loc3trk2 = loc3.addTrack("Boston Yard 2", Track.YARD);
        loc3trk2.setLength(1000);

        Location loc4 = lmanager.newLocation("Chelmsford");
        Track loc4trk1 = loc4.addTrack("Chelmsford Yard", Track.YARD);
        loc4trk1.setLength(1000);
        Track loc4trk2 = loc4.addTrack("Chelmsford Yard 2", Track.YARD);
        loc4trk2.setLength(1000);

        Location loc5 = lmanager.newLocation("Westford");
        Track loc5trk1 = loc5.addTrack("Westford Yard", Track.YARD);
        loc5trk1.setLength(1000);
        Track loc5trk2 = loc5.addTrack("Westford Yard 2", Track.YARD);
        loc5trk2.setLength(1000);

        // Create route with 4 locations
        Setup.setCarMoves(7); // set default to 7 moves per location
        Route rte1 = rmanager.newRoute("Route 2 Chelmsford");
        rte1.addLocation(loc1);
        RouteLocation rl2 = rte1.addLocation(loc2);
        rl2.setTrainIconX(175); // set the train icon coordinates
        rl2.setTrainIconY(150);
        RouteLocation rl3 = rte1.addLocation(loc3);
        rl3.setTrainIconX(25); // set the train icon coordinates
        rl3.setTrainIconY(175);
        RouteLocation rl4 = rte1.addLocation(loc4);
        rl4.setTrainIconX(75); // set the train icon coordinates
        rl4.setTrainIconY(175);

        // don't allow pickup or drops at Arlington
        rl2.setDropAllowed(false);
        rl2.setPickUpAllowed(false);

        // Create train
        Train train1 = tmanager.newTrain("Harvard to Chelmsford");
        train1.setRoute(rte1);

        // train skips Boston
        train1.addTrainSkipsLocation(rl3);

        // Place cars
        // Set up 3 cabooses, 1 Flat, and 7 boxcars
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("PU", "1", Bundle.getMessage("Caboose"), "32", "AT", "1943",
                loc1trk1, 10);
        c1.setCaboose(true);
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("SP", "2", Bundle.getMessage("Caboose"), "30", "DAB", "1957",
                loc1trk2, 5);
        c2.setCaboose(true);
        Car c3 = JUnitOperationsUtil.createAndPlaceCar("UP", "3", Bundle.getMessage("Caboose"), "33", "DAB", "1944",
                loc2trk1, 0);
        c3.setCaboose(true);
        Car c4 = JUnitOperationsUtil.createAndPlaceCar("UP", "4", "Boxcar", "40", "DAB", "1958", loc5trk1, 16);
        c4.setFred(true);

        Car c5 = JUnitOperationsUtil.createAndPlaceCar("SP", "5", "Boxcar", "40", "DAB", "1958", loc1trk1, 8);
        c5.setFred(true);
        Car c6 = JUnitOperationsUtil.createAndPlaceCar("NH", "6", "Boxcar", "40", "DAB", "1958", loc1trk1, 2);
        c6.setFred(true);
        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "7", "Flat", "40", "DAB", "1958", loc1trk1, 5);
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("SP", "8", "Boxcar", "40", "DAB", "1958", loc1trk1, 4);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("NH", "9", "Boxcar", "40", "DAB", "1944", loc1trk1, 3);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("NH", "10", "Boxcar", "40", "DAB", "1958", loc2trk1, 10);
        Car c11 = JUnitOperationsUtil.createAndPlaceCar("SP", "11", "Boxcar", "40", "DAB", "1958", loc2trk1, 3);

        // set c9 destination not part of train's route
        Assert.assertEquals("Destination c9", Track.OKAY, c9.setDestination(loc5, loc5trk1));

        // Place engines
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e2", Track.OKAY, e2.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e3", Track.OKAY, e3.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e4", Track.OKAY, e4.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e5", Track.OKAY, e5.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e6", Track.OKAY, e6.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e7", Track.OKAY, e7.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e8", Track.OKAY, e8.setLocation(loc1, loc1trk1));

        train1.setRequirements(Train.CABOOSE);
        train1.setNumberEngines("2");
        train1.setOwnerOption(Train.ALL_OWNERS);

        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 1", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 1", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 1", "Chelmsford Yard 2", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 1", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 1", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 1", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 1", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 1", "Chelmsford Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 1", "Chelmsford Yard 2", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 1", "Westford Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 1", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 1", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 1", "Chelmsford Yard", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 1", "Chelmsford Yard", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 1", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 1", "", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 1", "", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 1", "", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 1", "", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 1", "", e8.getDestinationTrackName());

        // Allow c9 to be used
        // set c9 destination Chelmsford
        Assert.assertEquals("Destination c9", Track.OKAY, c9.setDestination(loc4, null));

        // check that train direction and track direction feature works properly
        loc1trk2.setTrainDirections(Location.SOUTH);

        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build test track direction", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 1a", "Chelmsford Yard", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 1a", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 1a", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 1a", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 1a", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 1a", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 1a", "Chelmsford Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 1a", "Chelmsford Yard 2", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 1a", "Chelmsford Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 1a", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 1a", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 1a", "Chelmsford Yard 2", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 1a", "Chelmsford Yard 2", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 1a", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 1a", "", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 1a", "", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 1a", "", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 1a", "", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 1a", "", e8.getDestinationTrackName());

        loc1trk2.setTrainDirections(Location.NORTH);

        Assert.assertTrue(train1.addOwnerName("DAB"));
        Assert.assertFalse(train1.addOwnerName("DAB")); // returns false if name already exists
        train1.setOwnerOption(Train.INCLUDE_OWNERS);

        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 2", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 2", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 2", "Chelmsford Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 2", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 2", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 2", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 2", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 2", "Chelmsford Yard", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 2", "Chelmsford Yard 2", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 2", "Chelmsford Yard", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 2", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 2", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 2", "", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 2", "", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 2", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 2", "Chelmsford Yard 2", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 2", "Chelmsford Yard 2", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 2", "", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 2", "", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 2", "", e8.getDestinationTrackName());

        // test that build fails if Diesel type is not allowed
        train1.deleteTypeName("Diesel");
        Assert.assertFalse(train1.build());
        Assert.assertEquals("Train 1 After Build 3", false, train1.isBuilt());

        // restore type Diesel and allow all owners
        train1.addTypeName("Diesel");
        train1.setOwnerOption(Train.ALL_OWNERS);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 4", true, train1.isBuilt());

        // Only allow rolling stock built after 1956
        train1.setBuiltStartYear("1956");
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 5", true, train1.isBuilt());
        // check destinations
        Assert.assertEquals("c1 destination 5", "", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 5", "Chelmsford Yard", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 5", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 5", "", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 5", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 5", "", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 5", "Chelmsford Yard 2", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 5", "Chelmsford Yard", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 5", "", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 5", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 5", "", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 5", "Chelmsford Yard 2", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 5", "Chelmsford Yard 2", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 5", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 5", "", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 5", "", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 5", "", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 5", "", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 5", "", e8.getDestinationTrackName());

        train1.reset();
        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    /**
     * Test build using aggressive mode. Train departs regular location and staging.
     * Test number of engines, caboose. Train movement and car location updates.
     * Alternate track feature. Cars in a kernel departing staging.
     */
    @Test
    public void testAggressiveBuildOption() {

        Setup.setBuildAggressive(true);

        // create 2 consists and a single engine for testing
        Consist con1 = InstanceManager.getDefault(ConsistManager.class).newConsist("C1");

        Engine e1 = emanager.newRS("UP", "1");
        e1.setModel("GP30");
        e1.setOwnerName("AT");
        e1.setBuilt("1957");
        e1.setConsist(con1);
        e1.setMoves(5);
        Engine e2 = emanager.newRS("SP", "2");
        e2.setModel("GP30");
        e2.setOwnerName("AT");
        e2.setBuilt("1957");
        e2.setConsist(con1);
        e2.setMoves(5);

        // single engine
        Engine e3 = emanager.newRS("SP", "3");
        e3.setModel("GP40");
        e3.setOwnerName("DAB");
        e3.setBuilt("1957");

        Consist con2 = InstanceManager.getDefault(ConsistManager.class).newConsist("C2");

        Engine e4 = emanager.newRS("UP", "10");
        e4.setModel("GP40");
        e4.setOwnerName("DAB");
        e4.setBuilt("1944");
        e4.setConsist(con2);
        e4.setMoves(20);
        Engine e5 = emanager.newRS("SP", "20");
        e5.setModel("GP40");
        e5.setOwnerName("DAB");
        e5.setBuilt("1944");
        e5.setConsist(con2);
        e5.setMoves(20);

        // 3 engine consist
        Consist con3 = InstanceManager.getDefault(ConsistManager.class).newConsist("C3");

        Engine e6 = emanager.newRS("UP", "100");
        e6.setModel("GP40");
        e6.setOwnerName("DAB");
        e6.setBuilt("1944");
        e6.setConsist(con3);
        e6.setMoves(2);
        Engine e7 = emanager.newRS("SP", "200");
        e7.setModel("GP40");
        e7.setOwnerName("DAB");
        e7.setBuilt("1944");
        e7.setConsist(con3);
        e7.setMoves(2);
        Engine e8 = emanager.newRS("SP", "300");
        e8.setModel("GP40");
        e8.setOwnerName("DAB");
        e8.setBuilt("1944");
        e8.setConsist(con3);
        e8.setMoves(2);

        // Create 5 locations
        Location loc1 = lmanager.newLocation("New Harvard");
        Track loc1trk1 = loc1.addTrack("Harvard Yard 1", Track.YARD);
        loc1trk1.setLength(1000);
        Track loc1trk2 = loc1.addTrack("Harvard Yard 2", Track.YARD);
        loc1trk2.setLength(1000);

        Location loc2 = lmanager.newLocation("New Arlington");
        Track loc2trk1 = loc2.addTrack("Arlington Spur", Track.SPUR);
        loc2trk1.setLength(50);

        Location loc3 = lmanager.newLocation("New Boston");
        Track loc3trk1 = loc3.addTrack("Boston Yard 1", Track.YARD);
        loc3trk1.setLength(50);
        Track loc3trk2 = loc3.addTrack("Boston Yard 2", Track.YARD);
        loc3trk2.setLength(50);

        Location loc4 = lmanager.newLocation("New Chelmsford");
        Track loc4trk1 = loc4.addTrack("Chelmsford Yard 1", Track.YARD);
        loc4trk1.setLength(50);
        Track loc4trk2 = loc4.addTrack("Chelmsford Yard 2", Track.YARD);
        loc4trk2.setLength(50);

        Location loc5 = lmanager.newLocation("New Westford");
        Track loc5trk1 = loc5.addTrack("Westford Yard 1", Track.YARD);
        loc5trk1.setLength(1000);
        Track loc5trk2 = loc5.addTrack("Westford Yard 2", Track.YARD);
        loc5trk2.setLength(1000);

        Car c1 = JUnitOperationsUtil.createAndPlaceCar("PU", "13", "Caboose", "32", "AT", "1943", loc1trk1, 10);
        c1.setCaboose(true);
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("SP", "23", "Boxcar", "30", "DAB", "1957", loc1trk2, 5);
        Car c3 = JUnitOperationsUtil.createAndPlaceCar("UP", "33", "Boxcar", "33", "DAB", "1944", loc2trk1, 0);
        Car c4 = JUnitOperationsUtil.createAndPlaceCar("UP", "43", "Boxcar", "40", "DAB", "1958", loc1trk1, 16);
        Car c5 = JUnitOperationsUtil.createAndPlaceCar("SP", "53", "Boxcar", "40", "DAB", "1958", loc1trk2, 8);
        Car c6 = JUnitOperationsUtil.createAndPlaceCar("NH", "63", "Boxcar", "40", "DAB", "1958", loc1trk1, 2);
        Car c7 = JUnitOperationsUtil.createAndPlaceCar("UP", "73", "Flat", "40", "DAB", "1958", loc3trk1, 5);
        Car c8 = JUnitOperationsUtil.createAndPlaceCar("SP", "83", "Boxcar", "40", "DAB", "1958", loc3trk2, 4);
        Car c9 = JUnitOperationsUtil.createAndPlaceCar("NH", "93", "Boxcar", "40", "DAB", "1944", loc4trk1, 3);
        Car c10 = JUnitOperationsUtil.createAndPlaceCar("NH", "103", "Boxcar", "40", "DAB", "1958", loc4trk2, 10);
        Car c11 = JUnitOperationsUtil.createAndPlaceCar("SP", "113", "Boxcar", "40", "DAB", "1958", loc1trk1, 3);

        // Create route with 5 location
        Route rte1 = rmanager.newRoute("Route 3 Westford");
        rte1.addLocation(loc1);
        rte1.addLocation(loc2);
        rte1.addLocation(loc3);
        rte1.addLocation(loc4);
        rte1.addLocation(loc5);

        // Create train
        Train train1 = tmanager.newTrain("Harvard to Westford Aggressive");
        train1.setRoute(rte1);

        // Place engines
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e2", Track.OKAY, e2.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e3", Track.OKAY, e3.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e4", Track.OKAY, e4.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e5", Track.OKAY, e5.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e6", Track.OKAY, e6.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e7", Track.OKAY, e7.setLocation(loc1, loc1trk1));
        Assert.assertEquals("Place e8", Track.OKAY, e8.setLocation(loc1, loc1trk1));

        train1.setRequirements(Train.CABOOSE);
        train1.setNumberEngines("3");

        // confirm default
        Assert.assertEquals("All owners allowed", Train.ALL_OWNERS, train1.getOwnerOption());

        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 After Build 1", true, train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 1", "Westford Yard 2", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 1", "Westford Yard 1", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 1", "Westford Yard 2", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 1", "Boston Yard 1", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 1", "Arlington Spur", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 1", "Westford Yard 2", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 1", "Westford Yard 2", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 1", "Westford Yard 1", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 1", "", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 1", "", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 1", "Westford Yard 2", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 1", "", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 1", "", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 1", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 1", "", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 1", "", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 1", "Westford Yard 1", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 1", "Westford Yard 1", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 1", "Westford Yard 1", e8.getDestinationTrackName());

        // test departing from staging in aggressive mode
        Assert.assertTrue(train1.reset());
        loc1trk1.setTrackType(Track.STAGING);
        loc1trk2.setTrackType(Track.STAGING);
        Assert.assertFalse(train1.build());
        Assert.assertFalse("Train 1 After Build from staging, eight loco on departure track", train1.isBuilt());

        // move locos to other departure track
        Assert.assertEquals("Place e1", Track.OKAY, e1.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place e2", Track.OKAY, e2.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place e3", Track.OKAY, e3.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place e4", Track.OKAY, e4.setLocation(loc1, loc1trk2));
        Assert.assertEquals("Place e5", Track.OKAY, e5.setLocation(loc1, loc1trk2));

        Assert.assertTrue(train1.build());
        Assert.assertTrue("Train 1 After Build from staging, three loco on departure track", train1.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 2", "Westford Yard 2", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 2", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 2", "Westford Yard 2", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 2", "Boston Yard 2", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 2", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 2", "Arlington Spur", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 2", "Westford Yard 1", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 2", "Westford Yard 2", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 2", "Westford Yard 2", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 2", "Westford Yard 1", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 2", "Chelmsford Yard 1", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 2", "", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 2", "", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 2", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 2", "", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 2", "", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 2", "Westford Yard 1", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 2", "Westford Yard 1", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 2", "Westford Yard 1", e8.getDestinationTrackName());

        // test train move to a specific location
        Assert.assertFalse("Old Harvard is not part of this trains route", train1.move("Old Harvard"));
        Assert.assertFalse("Train departs New Harvard already there", train1.move("New Harvard"));
        Assert.assertTrue("Next location in train's route is New Arlington", train1.move("New Arlington"));
        Assert.assertFalse("Train is at New Arlington", train1.move("New Arlington"));
        // next location is New Boston, skip it and go directly to New Chelmsford
        Assert.assertTrue("New Chelmsford is in train's route", train1.move("New Chelmsford"));
        Assert.assertTrue("Next location in train's route is New Westford", train1.move("New Westford"));
        Assert.assertFalse("Train is at New Westford last location in train's route", train1.move("New Westford"));

        train1.move(); // terminate train

        // now try with a train returning to staging, test alternate track feature
        // Create train
        Train train2 = tmanager.newTrain("Westford to Harvard Aggressive");
        Route rte2 = rmanager.copyRoute(rte1, "Route 4 Harvard", true);
        train2.setRoute(rte2);
        train2.setRequirements(Train.CABOOSE);
        train2.setNumberEngines("3");

        // add 2 yard tracks to spur at Arlington
        Track loc2trk2 = loc2.addTrack("Arlington Yard 1", Track.YARD);
        loc2trk2.setLength(50); // only enough room for one car
        Track loc2trk3 = loc2.addTrack("Arlington Alternate Track", Track.SPUR);
        loc2trk3.setLength(100); // only enough room for two cars

        // set the alternate for Arlington spur
        loc2trk1.setAlternateTrack(loc2trk3);

        // send cars to Arlington spur
        c3.setFinalDestination(loc2);
        c3.setFinalDestinationTrack(loc2trk1);
        c8.setFinalDestination(loc2);
        c8.setFinalDestinationTrack(loc2trk1);
        c9.setFinalDestination(loc2);
        c9.setFinalDestinationTrack(loc2trk1);
        c11.setFinalDestination(loc2);
        c11.setFinalDestinationTrack(loc2trk1);

        Assert.assertTrue(train2.build());
        Assert.assertTrue("Train 2 returns to staging", train2.isBuilt());

        // check destinations
        Assert.assertEquals("c1 destination 3", "Harvard Yard 1", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 3", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 3", "Arlington Alternate Track", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 3", "Harvard Yard 1", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 3", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 3", "Harvard Yard 1", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 3", "Chelmsford Yard 2", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 3", "Arlington Yard 1", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 3", "Arlington Alternate Track", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 3", "Boston Yard 1", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 3", "Arlington Spur", c11.getDestinationTrackName());

        Assert.assertEquals("e1 destination 3", "", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 3", "", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 3", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 3", "", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 3", "", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 3", "Harvard Yard 1", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 3", "Harvard Yard 1", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 3", "Harvard Yard 1", e8.getDestinationTrackName());

        // check that cars on alternate track are sent to Arlington Spur
        Assert.assertEquals("next dest Arlingtion", loc2, c3.getFinalDestination());
        Assert.assertEquals("next dest track Arlingtion Spur", loc2trk1, c3.getFinalDestinationTrack());
        Assert.assertEquals("next dest Arlingtion", loc2, c8.getFinalDestination());
        Assert.assertEquals("next dest track Arlingtion Spur", loc2trk1, c8.getFinalDestinationTrack());
        Assert.assertEquals("next dest Arlingtion", loc2, c9.getFinalDestination());
        Assert.assertEquals("next dest track Arlingtion Spur", loc2trk1, c9.getFinalDestinationTrack());
        Assert.assertEquals("next dest null", null, c11.getFinalDestination());
        Assert.assertEquals("next dest track null", null, c11.getFinalDestinationTrack());

        // test train move to a an exact location
        Assert.assertFalse("Old Harvard is not part of this trains route", train2.moveToNextLocation("Old Harvard"));
        Assert.assertFalse("New Harvard is the last location in this trains route",
                train2.moveToNextLocation("New Harvard"));
        Assert.assertFalse("New Boston is the 3rd to last location in this trains route",
                train2.moveToNextLocation("New Boston"));
        Assert.assertFalse("New Westford is the current location in this trains route",
                train2.moveToNextLocation("New Westford"));
        Assert.assertFalse("New Arlington is the 2nd to last location in this trains route",
                train2.moveToNextLocation("New Arlington"));
        Assert.assertTrue("New Chelmsford is the next location in this trains route",
                train2.moveToNextLocation("New Chelmsford"));
        Assert.assertTrue("New Boston is the next location in this trains route",
                train2.moveToNextLocation("New Boston"));
        Assert.assertTrue("New Arlington is the next location in this trains route",
                train2.moveToNextLocation("New Arlington"));
        Assert.assertTrue("New Harvard is the next location in this trains route",
                train2.moveToNextLocation("New Harvard"));
        Assert.assertFalse("Train is at New Harvard", train2.moveToNextLocation("New Harvard"));

        train2.move(); // terminate train

        // put C6 and C12 into a kernel
        Car c12 = JUnitOperationsUtil.createAndPlaceCar("SP", "123", "Boxcar", "40", "DAB", "1958", loc1trk1, 3);
        Kernel kernel = InstanceManager.getDefault(KernelManager.class).newKernel("C6andC12");
        c6.setKernel(kernel);
        c12.setKernel(kernel);

        // place car in staging but not on a track
        Car c13 = JUnitOperationsUtil.createAndPlaceCar("SP", "133", "Boxcar", "40", "DAB", "1958", loc1trk1, 3);
        c13.setLocation(loc1, null);

        // now test train returning to staging
        Setup.setStagingTrackImmediatelyAvail(false);
        rte1.addLocation(loc1); // return to staging
        Assert.assertFalse(train1.build());
        // should fail, can't return to staging track
        Assert.assertEquals("Train 1 deaprting and returning to staging", false, train1.isBuilt());
        // change mode
        Setup.setStagingTrackImmediatelyAvail(true);
        Assert.assertTrue(train1.build());
        Assert.assertEquals("Train 1 departing and returning to staging", true, train1.isBuilt());
        Assert.assertEquals("check departure track name", "Harvard Yard 1", train1.getDepartureTrack().getName());
        Assert.assertEquals("check departure and arrival track", train1.getDepartureTrack(),
                train1.getTerminationTrack());

        // check destinations
        Assert.assertEquals("c1 destination 3", "Harvard Yard 1", c1.getDestinationTrackName());
        Assert.assertEquals("c2 destination 3", "", c2.getDestinationTrackName());
        Assert.assertEquals("c3 destination 3", "", c3.getDestinationTrackName());
        Assert.assertEquals("c4 destination 3", "Boston Yard 2", c4.getDestinationTrackName());

        Assert.assertEquals("c5 destination 3", "", c5.getDestinationTrackName());
        Assert.assertEquals("c6 destination 3", "Westford Yard 1", c6.getDestinationTrackName());
        Assert.assertEquals("c7 destination 3", "Harvard Yard 1", c7.getDestinationTrackName());
        Assert.assertEquals("c8 destination 3", "Arlington Spur", c8.getDestinationTrackName());

        Assert.assertEquals("c9 destination 3", "", c9.getDestinationTrackName());
        Assert.assertEquals("c10 destination 3", "Chelmsford Yard 1", c10.getDestinationTrackName());
        Assert.assertEquals("c11 destination 3", "Westford Yard 2", c11.getDestinationTrackName());
        Assert.assertEquals("c12 destination 3", "Westford Yard 1", c12.getDestinationTrackName());

        Assert.assertEquals("c13 destination 3", "", c13.getDestinationTrackName());
        Assert.assertNull("track should be null", c13.getTrack());

        Assert.assertEquals("e1 destination 3", "", e1.getDestinationTrackName());
        Assert.assertEquals("e2 destination 3", "", e2.getDestinationTrackName());
        Assert.assertEquals("e3 destination 3", "", e3.getDestinationTrackName());
        Assert.assertEquals("e4 destination 3", "", e4.getDestinationTrackName());
        Assert.assertEquals("e5 destination 3", "", e5.getDestinationTrackName());
        Assert.assertEquals("e6 destination 3", "Harvard Yard 1", e6.getDestinationTrackName());
        Assert.assertEquals("e7 destination 3", "Harvard Yard 1", e7.getDestinationTrackName());
        Assert.assertEquals("e8 destination 3", "Harvard Yard 1", e8.getDestinationTrackName());

        train1.terminate();

        // check car locations
        Assert.assertEquals("c1 location", "Harvard Yard 1", c1.getTrackName());
        Assert.assertEquals("c2 location", "Harvard Yard 2", c2.getTrackName());
        Assert.assertEquals("c3 location", "Arlington Alternate Track", c3.getTrackName());
        Assert.assertEquals("c4 location", "Boston Yard 2", c4.getTrackName());

        Assert.assertEquals("c5 location", "Harvard Yard 2", c5.getTrackName());
        Assert.assertEquals("c6 location", "Westford Yard 1", c6.getTrackName());
        Assert.assertEquals("c7 location", "Harvard Yard 1", c7.getTrackName());
        Assert.assertEquals("c8 location", "Arlington Spur", c8.getTrackName());

        Assert.assertEquals("c9 location", "Arlington Alternate Track", c9.getTrackName());
        Assert.assertEquals("c10 location", "Chelmsford Yard 1", c10.getTrackName());
        Assert.assertEquals("c11 location", "Westford Yard 2", c11.getTrackName());

        Assert.assertEquals("e1 location", "Harvard Yard 2", e1.getTrackName());
        Assert.assertEquals("e2 location", "Harvard Yard 2", e2.getTrackName());
        Assert.assertEquals("e3 location", "Harvard Yard 2", e3.getTrackName());
        Assert.assertEquals("e4 location", "Harvard Yard 2", e4.getTrackName());
        Assert.assertEquals("e5 location", "Harvard Yard 2", e5.getTrackName());
        Assert.assertEquals("e6 location", "Harvard Yard 1", e6.getTrackName());
        Assert.assertEquals("e7 location", "Harvard Yard 1", e7.getTrackName());
        Assert.assertEquals("e8 location", "Harvard Yard 1", e8.getTrackName());

        JUnitOperationsUtil.checkOperationsShutDownTask();
    }

    @Test
    public void testTrainServicesCar() {

        Route route = JUnitOperationsUtil.createFiveLocationRoute();

        // to increase test coverage place the two cars in a kernel
        Car c1 = JUnitOperationsUtil.createAndPlaceCar("A", "1", "Boxcar", "40", null, 0);
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("A", "2", "Boxcar", "40", null, 0);
        Kernel k2 = InstanceManager.getDefault(KernelManager.class).newKernel("2 cars");
        c1.setKernel(k2);
        c2.setKernel(k2);

        Train train1 = tmanager.newTrain("testTrainServicesCar");

        // no route
        Assert.assertFalse(train1.isServiceable(c1));

        train1.setRoute(route);
        // car not on track
        Assert.assertFalse(train1.isServiceable(c1));

        Location boston = lmanager.getLocationByName("Boston");
        Track bostonSpur1 = boston.getTrackByName("Boston Spur 1", null);

        Assert.assertEquals("Place car on track", Track.OKAY, c1.setLocation(boston, bostonSpur1));

        // should be serviced by train
        Assert.assertTrue(train1.isServiceable(c1));

        // test road name option
        train1.setCarRoadOption(Train.EXCLUDE_ROADS);
        train1.addCarRoadName("A");

        // should be not be serviced by train
        Assert.assertFalse(train1.isServiceable(c1));

        train1.setCarRoadOption(Train.INCLUDE_ROADS);

        // should be serviced by train
        Assert.assertTrue(train1.isServiceable(c1));

        // test caboose road name option
        train1.setCabooseRoadOption(Train.EXCLUDE_ROADS);
        train1.addCabooseRoadName("A");
        // should be serviced by train
        Assert.assertTrue(train1.isServiceable(c1));

        // make car a caboose
        c1.setCaboose(true);
        // should be not be serviced by train
        Assert.assertFalse(train1.isServiceable(c1));
        c1.setCaboose(false);

        // give the car a destination
        Location chelmsford = lmanager.getLocationByName("Chelmsford");
        c1.setDestination(chelmsford, null);

        // should be serviced by train
        Assert.assertTrue(train1.isServiceable(c1));

        // don't allow boxcars
        chelmsford.deleteTypeName("Boxcar");
        Assert.assertFalse(train1.isServiceable(c1));
        chelmsford.addTypeName("Boxcar");
        Assert.assertTrue(train1.isServiceable(c1));

        // don't allow Boston spur to service train
        bostonSpur1.setPickupOption(Track.EXCLUDE_TRAINS);
        bostonSpur1.addPickupId(train1.getId());
        Assert.assertFalse(train1.isServiceable(c1));
        bostonSpur1.setPickupOption(Track.ANY);
        Assert.assertTrue(train1.isServiceable(c1));

        // train is to skip Boston
        RouteLocation bostonRl = route.getLastLocationByName("Boston");
        train1.addTrainSkipsLocation(bostonRl);
        Assert.assertFalse(train1.isServiceable(c1));
        train1.deleteTrainSkipsLocation(bostonRl);
        Assert.assertTrue(train1.isServiceable(c1));

        // train is to skip Chelmsford
        RouteLocation chelmsfordRl = route.getLastLocationByName("Chelmsford");
        train1.addTrainSkipsLocation(chelmsfordRl);
        Assert.assertFalse(train1.isServiceable(c1));
        train1.deleteTrainSkipsLocation(chelmsfordRl);
        Assert.assertTrue(train1.isServiceable(c1));

        // only allow car to terminal
        train1.setSendCarsToTerminalEnabled(true);
        Assert.assertFalse(train1.isServiceable(c1));
        train1.setSendCarsToTerminalEnabled(false);
        Assert.assertTrue(train1.isServiceable(c1));

        // don't allow destination track to service train
        Track chelmsfordSpur1 = chelmsford.getTrackByName("Chelmsford Spur 1", null);
        c1.setDestination(chelmsford, chelmsfordSpur1);
        chelmsfordSpur1.setDropOption(Track.EXCLUDE_TRAINS);
        chelmsfordSpur1.addDropId(train1.getId());
        Assert.assertFalse(train1.isServiceable(c1));
        chelmsfordSpur1.setDropOption(Track.ANY);
        Assert.assertTrue(train1.isServiceable(c1));

        // give car a destination that can't be reached
        Location acton = lmanager.getLocationByName("Acton");
        Track actonSpur1 = acton.getTrackByName("Acton Spur 1", null);

        c1.setDestination(acton, actonSpur1);
        Assert.assertFalse(train1.isServiceable(c1));
    }

    @Test
    public void testTrainServicesCarNoThrough() {

        Route route = JUnitOperationsUtil.createFiveLocationRoute();
        Train train1 = tmanager.newTrain("testTrainServicesCar");
        train1.setRoute(route);

        Car c1 = JUnitOperationsUtil.createAndPlaceCar("A", "1", "Boxcar", "40", null, 0);

        // place car at start of route, send to last location of route
        Location acton = lmanager.getLocationByName("Acton");
        Track actonSpur1 = acton.getTrackByName("Acton Spur 1", null);
        // give the car a destination
        Location essex = lmanager.getLocationByName("Essex");
        c1.setDestination(essex, null);

        Assert.assertEquals("Place car on track", Track.OKAY, c1.setLocation(acton, actonSpur1));

        // should be serviced by train
        Assert.assertTrue(train1.isServiceable(c1));

        // don't allow through cars
        train1.setAllowThroughCarsEnabled(false);
        Assert.assertFalse(train1.isServiceable(c1));

        // caboose, car with FRED, and passenger cars are exceptions
        c1.setCaboose(true);
        Assert.assertTrue(train1.isServiceable(c1));
        c1.setCaboose(false);
        Assert.assertFalse(train1.isServiceable(c1));

        c1.setFred(true);
        Assert.assertTrue(train1.isServiceable(c1));
        c1.setFred(false);
        Assert.assertFalse(train1.isServiceable(c1));

        c1.setPassenger(true);
        Assert.assertTrue(train1.isServiceable(c1));
        c1.setPassenger(false);
        Assert.assertFalse(train1.isServiceable(c1));
    }

    @Test
    public void testTrainServicesCarLocal() {

        Route route = JUnitOperationsUtil.createFiveLocationRoute();
        Train train1 = tmanager.newTrain("testTrainServicesCar");
        train1.setRoute(route);

        Car c1 = JUnitOperationsUtil.createAndPlaceCar("A", "1", "Boxcar", "40", null, 0);
        Car c2 = JUnitOperationsUtil.createAndPlaceCar("A", "2", "Boxcar", "40", null, 0);

        // place car at start of route
        Location acton = lmanager.getLocationByName("Acton");
        Track actonSpur1 = acton.getTrackByName("Acton Spur 1", null);
        // give the car a destination
        Track actonYard1 = acton.getTrackByName("Acton Yard 1", null);
        c1.setDestination(acton, actonYard1);

        Assert.assertEquals("Place car on track", Track.OKAY, c1.setLocation(acton, actonSpur1));
        
        // place car middle of route
        Location chelmsford = lmanager.getLocationByName("Chelmsford");
        Track chelmsfordSpur1 = chelmsford.getTrackByName("Chelmsford Spur 1", null);
        // give the car a destination
        Track chelmsfordYard1 = chelmsford.getTrackByName("Chelmsford Yard 1", null);
        c2.setDestination(chelmsford, chelmsfordYard1);

        Assert.assertEquals("Place car on track", Track.OKAY, c2.setLocation(chelmsford, chelmsfordSpur1));
        
        // local move should override
        train1.setSendCarsToTerminalEnabled(true);

        // should be serviced by train
        Assert.assertTrue(train1.isServiceable(c1));
        Assert.assertTrue(train1.isServiceable(c2));

        // don't allow local moves
        train1.setAllowLocalMovesEnabled(false);
        Assert.assertFalse(train1.isServiceable(c1));

        // caboose, car with FRED, and passenger cars are exceptions
        c1.setCaboose(true);
        Assert.assertTrue(train1.isServiceable(c1));
        c1.setCaboose(false);
        Assert.assertFalse(train1.isServiceable(c1));

        c1.setFred(true);
        Assert.assertTrue(train1.isServiceable(c1));
        c1.setFred(false);
        Assert.assertFalse(train1.isServiceable(c1));

        c1.setPassenger(true);
        Assert.assertTrue(train1.isServiceable(c1));
        c1.setPassenger(false);
        Assert.assertFalse(train1.isServiceable(c1));
    }

    private void placeFourEngines(Track track) {
        // place 4 engines at the start of the route
        Engine e1 = emanager.newRS("E", "1");
        e1.setModel("GP40");
        Engine e2 = emanager.newRS("E", "2");
        e2.setModel("GP40");
        Engine e3 = emanager.newRS("E", "3");
        e3.setModel("GP40");
        Engine e4 = emanager.newRS("E", "4");
        e4.setModel("GP40");

        Assert.assertEquals("confirm placement", Track.OKAY, e1.setLocation(track.getLocation(), track));
        Assert.assertEquals("confirm placement", Track.OKAY, e2.setLocation(track.getLocation(), track));
        Assert.assertEquals("confirm placement", Track.OKAY, e3.setLocation(track.getLocation(), track));
        Assert.assertEquals("confirm placement", Track.OKAY, e4.setLocation(track.getLocation(), track));
    }

    private String getTrainStatus(Train train) {
        String[] status = train.getStatus().split(" ");
        return status[0];
    }

    // from here down is testing infrastructure
    // Ensure minimal setup for log4J
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        tmanager = InstanceManager.getDefault(TrainManager.class);
        rmanager = InstanceManager.getDefault(RouteManager.class);
        lmanager = InstanceManager.getDefault(LocationManager.class);
        emanager = InstanceManager.getDefault(EngineManager.class);
        // cmanager = InstanceManager.getDefault(CarManager.class);
        ct = InstanceManager.getDefault(CarTypes.class);

        // turn off build fail messages
        tmanager.setBuildMessagesEnabled(false);
        // disable build reports
        tmanager.setBuildReportEnabled(false);

        ct.addName("Boxcar");
        ct.addName("Flat");
        ct.addName("Flat Car");
        ct.addName("Gon");
        ct.addName("Coil Car");
        ct.addName(Bundle.getMessage("Caboose"));

        Setup.setRouterBuildReportLevel(Setup.BUILD_REPORT_VERY_DETAILED);
        Setup.setCarMoves(7); // set default to 7 moves per location
    }

    @Override
    @AfterEach
    public void tearDown() {
        super.tearDown();
    }

}
