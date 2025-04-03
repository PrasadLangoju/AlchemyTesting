/**
 * @version 8
 */
  
package com.orasi;
  
import java.util.*;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.orasi.event.spi.*;
import static com.orasi.shared_library.*;
import com.orasi.integration.*;
import com.orasi.event.Event;
import com.orasi.event.action.EventAction;
import com.orasi.event.handler.AbstractEventHandler;
import com.orasi.event.hook.EventHook;
import com.orasi.model.*;
import org.apache.commons.cli.*;
import com.orasi.datasource.*;
import java.io.InputStream;
import java.util.logging.LogManager;
import com.google.gson.Gson;

  
  
public class SuiteExecutionWrapper {

  static {
    try ( InputStream iS = SuiteExecutionWrapper.class.getClassLoader().getResourceAsStream( "logging.properties") ) {
      LogManager.getLogManager().readConfiguration(iS);
    } catch( Exception e ) {
      e.printStackTrace();
    }
  }

  private static final Random numberGenerator = new Random();
  public static final String generateCharacters(int characterCount) {
    StringBuilder sB = new StringBuilder();
    for (int i = 0; i < characterCount; i++) {
      int rV = numberGenerator.nextInt(26);

      rV += numberGenerator.nextBoolean() ? 65 : 97;

      sB.append((char) rV);

    }

    return sB.toString();
  }

  private static int failureCount = 0;
  private static int failureLevel = 0;

  private static class TestFailureHandler extends AbstractEventHandler<TestPayload> {

      public TestFailureHandler() {
        super( EventAction.ALL, EventHook.FAILURE.getId(), TestEvent.EVENT_TYPE );
      }
    
      @Override
      protected void _handleEvent(Event<TestPayload> e) {
        if ( e.getPayload().getFailureType() > failureLevel ) {
          failureLevel = e.getPayload().getFailureType();
        }
        failureCount++;
      }

    }
  
    private final Options cliOptions = new Options();
  private static CommandLine cli;
  private int threadCount = 30;
  private static final Logger log = LoggerFactory.getLogger(SuiteExecutionWrapper.class);
  private static final SuiteExecutionWrapper singleton = new SuiteExecutionWrapper();
  private int executionId;
  private int checkFailureLevel = 2;

  private String name;
  private String description;
  private int id;
  private String userName;
  private List<String> testList;
  private List<String> targetList;
  private List<Router> routerList = new ArrayList<>(5);

  private static final Map<String, Map<String, String>> environmentMap = new HashMap<>(10);
  
    private SuiteExecutionWrapper() {
  
    }

    public CommandLine getCommandLine() {
      return cli;
    }

    public Options getOptions() {
      return cliOptions;
    }

    public void addOptions(Option[] options) {
      for (Option option : options) {
        cliOptions.addOption(option);
      }
    }

    public String getOption( String name, String defaultValue ) {
      if ( cli.hasOption(name) ) {
        if ( SuiteExecutionWrapper.instance().getOptions().getOption(name).hasArg() ) {
          return cli.getOptionValue(name);
        } else {
          return "true";
        }
      } else {
        return defaultValue;
      }
    }
  
    public String getOption( String key, String name, String defaultValue ) {
      String keyName = key + "_" + name;
      if ( cli.hasOption(keyName) ) {
        if ( SuiteExecutionWrapper.instance().getOptions().getOption(keyName).hasArg() ) {
          return cli.getOptionValue(keyName);
        } else {
          return "true";
        }
      } else {
        return defaultValue;
      }
    }

  private static boolean hasValue(String option, String value) {
    String[] oV = cli.getOptionValues(option);
    if (oV != null) {
      for (String v : oV) {
        if (v.equals(value)) {
          return true;
        }
      }
      return false;
    } else {
      return true;
    }
    
  }

   
    public static void main(String[] args) {

            System.out.println( "                                                                                \n" +
"                                                                                \n" +
"                                                                                \n" +
"                                                                        &##     \n" +
"                                                               &######&  %%     \n" +
"                              &##################%              &&#& &%#& ####% \n" +
"                             &#####################&         %#% %#######& %#   \n" +
"                            ########################&      #####%##&  %####%    \n" +
"                          &###########################    %##########%%###%     \n" +
"                         ##############################&%##%  #######%          \n" +
"                       &#####################################%#####%%##%        \n" +
"                      #################  &#####################%%#####          \n" +
"                    &################      ###################%  ####           \n" +
"                   ################%         ######################             \n" +
"                  ################%           ####################              \n" +
"                %################&             &################&               \n" +
"               ###################            %###################              \n" +
"             &#####################%         ######################             \n" +
"           &#########################      &########################%           \n" +
"           ###########################%   ############################&         \n" +
"         ############################### ##############################         \n" +
"        ################&##############################&#################       \n" +
"      #################   &###########################&  &################      \n" +
"    &################      &########################%      ################%    \n" +
"   &################         %#####################&        &################   \n" +
"  ################&           %###################            ################% \n" +
" ################&              %%%%%%%%%%%%%%%%&              &################");
      
      System.out.println( "\n\nAlchemy by Orasi");
      System.out.println( "Copyright Orasi, Inc. | All Rights Reserved.\n\n");

      DataSourceProviderFactory.instance().setDataSourceProvider( DataManager.instance() );

      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("h", "help", false, "Print this message"));
      Option o = new Option("t", "tags", true, "A space separated list of test tags.  Each test that contains any of the supplied tags will be executed.");
      o.setArgs(Option.UNLIMITED_VALUES);
      SuiteExecutionWrapper.instance().getOptions().addOption(o);

      o = new Option("it", "includetests", true, "A space separated list of test identifiers that will be executed");
      o.setArgs(Option.UNLIMITED_VALUES);
      SuiteExecutionWrapper.instance().getOptions().addOption(o);

      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("e", "environment", true, "The environment variable set to use during execution"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("p", "threadcount", true, "The maximum amount of threads to run in parellel"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("n", "noreturn", false, "Allow the process to complete without forcing it to exit with a return code"));
 
      o = new Option("ir", "includerouters", true, "A space separated list of router identifiers.  Only targets belonging to these routers will run");
      o.setArgs(Option.UNLIMITED_VALUES);
      SuiteExecutionWrapper.instance().getOptions().addOption(o);

      o = new Option("iw", "includeintegrations", true, "A space separated list of integration identifiers to use");
      o.setArgs(Option.UNLIMITED_VALUES);
      SuiteExecutionWrapper.instance().getOptions().addOption(o);

      o = new Option("ie", "includetargets", true, "A space separated list of execution target identifiers.  Only the names targets will run");
      o.setArgs(Option.UNLIMITED_VALUES);
      SuiteExecutionWrapper.instance().getOptions().addOption(o);
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("a", "name", true, "Override the default name of this test suite"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("u", "user", true, "Override the name of the executing user"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("d", "description", true, "Override the description of this test suite"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("q", "queryids", false, "List the ID's of all elements that make up this suite"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("rd", "rundevelopment", false, "Indicates that tests in the development state should run"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("rq", "runquarantine", false, "Indicates that tests in the quarantine state should run"));
      SuiteExecutionWrapper.instance().getOptions().addOption(new Option("f", "failurelevel", true, "What error level fails a test suite.  1: Infrastructure, 2: Automation Failure (default), 3: Application Failure, 4: Unknown Error"));

      SuiteExecutionWrapper.instance().addOptions( new com.orasi.integration.embedded.selenium.SeleniumEmbedded().getOptions() );
      SuiteExecutionWrapper.instance().addOptions( new com.orasi.integration.html.HTMLSerializer().getOptions() );
      SuiteExecutionWrapper.instance().addOptions( new com.orasi.integration.console.ExecutionConsole().getOptions() );
      SuiteExecutionWrapper.instance().addOptions( new com.orasi.integration.cloud.ExecutionCloud().getOptions() );
      

      CommandLineParser cP = new DefaultParser();
      try {
        cli = cP.parse(SuiteExecutionWrapper.instance().getOptions(), args);
      } catch( ParseException pE ) {
        System.err.println("Failed to parse command line: " + pE.getMessage());
      }

      // Default Environment Properties 
      DataManager.instance().addDefaultEnvironmentProperty( "url", DataManager.instance().replaceValues( "" ) + "" );



      Map<String,String> envMap;
      //* A production configuration containing a Production URL */
      envMap = new HashMap<>( 10 );
      
      environmentMap.put( "Production", envMap );
//* A staging configuration containing a Staging URL */
      envMap = new HashMap<>( 10 );
      
      environmentMap.put( "Staging", envMap );

      

      if ( cli.hasOption( "e" ) ) {
        envMap = environmentMap.get( cli.getOptionValue( "e" ) );
        if ( envMap == null ) {
          System.err.println( "Specified Environment " + cli.getOptionValue( "e" ) + " does not exist" );
        } else {
          DataManager.instance().addEnvironmentProperties( envMap );
        }
      }

      



      if ( cli.hasOption( "h" ) ) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar ecommerce_application_testing", SuiteExecutionWrapper.instance().getOptions() );
        System.exit( 0 );
      }

      if ( cli.hasOption( "q" ) ) {
        System.out.println( "Suite" );
        System.out.println( "ecommerce application testing (Suite): 32592.3911" ); 
        System.out.println( "\r\nTests" );
        System.out.println( "Test One (Test): 32592.3921" );
        System.out.println( "T2 (Test): 32592.4073" );
        
        System.out.println( "\r\nFunctions" );
        
        
        
        System.out.println( "\r\nExecution Targets" );
	System.out.println( "Grid (Router): 32592.4065" );
	System.out.println( "Chrome (Execution Target): 32592.4066" );
	System.out.println( "Firefox (Execution Target): 32592.4067" );
	System.out.println( "Microsoft Edge (Execution Target): 32592.4068" );
	System.out.println( "ecommerce application testing router (Router): 32592.3914" );
	System.out.println( "Chrome (Execution Target): 32592.3915" );
	System.out.println( "Firefox (Execution Target): 32592.3916" );
	System.out.println( "Microsoft Edge (Execution Target): 32592.3917" );
	
        System.out.println( "\r\nSites and Pages" );
        System.out.println( "www.facebook.com (Site): 32592.4075" );
        System.out.println( "Facebook  log in or sign up (Page): 32592.4079" );
        System.out.println( "www.snapdeal.com (Site): 32592.3923" );
        System.out.println( "Shop Online for Men, Women & Kids Clothing, Shoes, Home Decor Items (Page): 32592.3926" );
        System.out.println( "default (Page): 32592.3964" );
        System.out.println( "Snapdeal.com - Online shopping India- Discounts - shop Online Perfumes, Watches, sunglasses etc (Page): 32592.3966" );
        System.out.println( "Buy Mandoth Cotton Blend Regular Fit Full Sleeves Men's Formal Shirt - White ( Pack of 1 ) Online at Best Price in India - Snapdeal (Page): 32592.4028" );
        
        System.out.println( "\r\nData Sources" );
        System.out.println( "Data (Data Source): 32592.4071" );
        
        System.out.println( "\r\nPlugins" );
        System.out.println( "Integrated Selenium Server (Plugin): 32592.3912" );
        System.out.println( "HTML Generator (Plugin): 32592.3918" );
        System.out.println( "Alchemy Execution Console (Plugin): 32592.3919" );
        System.out.println( "Alchemy GridWorks (Plugin): 32592.4172" );
        
        
        System.out.println( "\r\n\r\nImported Suites" ); 
	
        System.exit( 0 );
      }

      DataSource<String,DataTable> _dS = null;
      DataTable<String,DataSource,DataField,DataRow> _dT = null;
      DataField<String> _dF = null; 
      // Data
      /* Add a description of Data */
      _dS = (DataSource) new com.orasi.datasource.spi.CSVDataSource();
      _dS.setId( "32592.4071" );
      _dS.setEncryptionEnabled( 0 == 1 );
      _dS.setEncryptionKey( "" );
      _dS.setProperty( "File Name", DataManager.instance().replaceValues( "C:\\Users\\m.prasad\\Downloads\\Credentials.csv" ) + "" );
      _dS.setProperty( "Field Index", DataManager.instance().replaceValues( "0" ) + "" );
      _dS.setProperty( "Ignore First Row", DataManager.instance().replaceValues( "true" ) + "" );
      
      _dS.initialize();
      _dT = _dS.createTable( "32592.4072" );
      _dT.setId( "Credentials.csv" );
      _dT.setLockable( false );
      _dT.initialize();
      _dS.addTable( "32592.4072", _dT );
      _dF = _dT.createField( "﻿url" );
      _dF.setEncryptionEnabled( false );
      _dF.setProperty( "Field Index", DataManager.instance().replaceValues( "0" ) + "" );
      _dF.initialize();
      _dT.addField( "﻿url", _dF );
      _dF.populate();
      _dF = _dT.createField( "UserName" );
      _dF.setEncryptionEnabled( false );
      _dF.setProperty( "Field Index", DataManager.instance().replaceValues( "1" ) + "" );
      _dF.initialize();
      _dT.addField( "UserName", _dF );
      _dF.populate();
      _dF = _dT.createField( "Password" );
      _dF.setEncryptionEnabled( false );
      _dF.setProperty( "Field Index", DataManager.instance().replaceValues( "2" ) + "" );
      _dF.initialize();
      _dT.addField( "Password", _dF );
      _dF.populate();
      _dT.populate();
      _dS.populate();
      DataManager.instance().registerDataSource( "32592.4071", _dS );
      

      

      if ( cli.hasOption( "a" ) ) {
        SuiteExecutionWrapper.instance().setName(cli.getOptionValue( "a" ));
      } else {
        SuiteExecutionWrapper.instance().setName("ecommerce application testing");
      }
      if ( cli.hasOption( "u" ) ) {
        SuiteExecutionWrapper.instance().setUserName(cli.getOptionValue( "u" ));
      } else {
        SuiteExecutionWrapper.instance().setUserName("");
      }
      if ( cli.hasOption( "d" ) ) {
        SuiteExecutionWrapper.instance().setDescription(cli.getOptionValue( "d" ));
      } else {
        SuiteExecutionWrapper.instance().setDescription("No description was added for ecommerce application testing");
      }

      //
      // Configure the test level information
      //
      List<String> testList = new ArrayList(5);
      if ( hasValue( "it", "32592.3921" ) ) {
        if ( 0 == 0 || ( 0 == 1 && cli.hasOption( "rd" ) ) || ( 0 == 2 && cli.hasOption( "rq" ) ) ) {
          TestManager.instance().registerTest( new org.org_1.ecommerce_application_testing.test_one() );
          testList.add("Test One");
        }
      }
      
      if ( hasValue( "it", "32592.4073" ) ) {
        if ( 0 == 0 || ( 0 == 1 && cli.hasOption( "rd" ) ) || ( 0 == 2 && cli.hasOption( "rq" ) ) ) {
          TestManager.instance().registerTest( new org.org_1.ecommerce_application_testing.t2() );
          testList.add("T2");
        }
      }
      
      

      
      



      
      //
      // Register the execution routers here
      //
      Router r;
      /*
      Routers from Grid
      Add a description of Grid
      */
      if ( hasValue( "ir", "32592.4065" ) ) { 
        r = new Router( 8, "Grid", "32592.4065" ,"{\"name\":\"Grid\",\"description\":\"Grid\",\"alchemyId\":4065,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"routerId\":8,\"status\":0,\"userId\":0,\"propertyList\":[{\"name\":\"URL\",\"value\":\"#{alchemy.cloud.url}\"}]}" );
      
        r.addProperty( "URL", DataManager.instance().replaceValues( "#{alchemy.cloud.url}" ) + "" );
        SuiteExecutionWrapper.instance().routerList.add(r);
      }
      /*
      Routers from ecommerce application testing Execution Package
      Add a description of ecommerce application testing Execution Package
      */
      if ( hasValue( "ir", "32592.3914" ) ) { 
        r = new Router( 1, "ecommerce application testing router", "32592.3914" ,"{\"name\":\"ecommerce application testing router\",\"description\":\"Add a description of ecommerce application testing router\",\"alchemyId\":3914,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"routerId\":1,\"status\":0,\"userId\":0,\"propertyList\":[{\"name\":\"URL\",\"value\":\"http://localhost:4444/wd/hub\"}],\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\"}" );
      
        r.addProperty( "URL", DataManager.instance().replaceValues( "http://localhost:4444/wd/hub" ) + "" );
        SuiteExecutionWrapper.instance().routerList.add(r);
      }
      
 
      //
      // Configure the endpoint details here
      //
      List<ExecutionTarget> targetList = new ArrayList<>(10);
      ExecutionTarget eT;
      /*
      Targets from Grid
      Add a description of Grid
      */
      if ( hasValue( "ie", "32592.4066" ) ) {
        eT = new ExecutionTarget( "Chrome", "32592.4066", "32592.4065", 1 ,"{\"name\":\"Chrome\",\"description\":\"Chrome\",\"alchemyId\":4066,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":1,\"executionRouterID\":{\"alchemyId\":4065,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"chrome\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"referenceSuiteID\":0}" );
        if ( eT.getMaximumAvailable() > 0 ) {
          eT.addProperty( "browserName", DataManager.instance().replaceValues( "chrome" ) + "" );
          eT.addProperty( "platformName", DataManager.instance().replaceValues( "ANY" ) + "" );
          targetList.add(eT);
        }
      }
      if ( hasValue( "ie", "32592.4067" ) ) {
        eT = new ExecutionTarget( "Firefox", "32592.4067", "32592.4065", 1 ,"{\"name\":\"Firefox\",\"description\":\"Firefox\",\"alchemyId\":4067,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":1,\"executionRouterID\":{\"alchemyId\":4065,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"firefox\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"referenceSuiteID\":0}" );
        if ( eT.getMaximumAvailable() > 0 ) {
          eT.addProperty( "browserName", DataManager.instance().replaceValues( "firefox" ) + "" );
          eT.addProperty( "platformName", DataManager.instance().replaceValues( "ANY" ) + "" );
          targetList.add(eT);
        }
      }
      if ( hasValue( "ie", "32592.4068" ) ) {
        eT = new ExecutionTarget( "Microsoft Edge", "32592.4068", "32592.4065", 1 ,"{\"name\":\"Microsoft Edge\",\"description\":\"Microsoft Edge\",\"alchemyId\":4068,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":1,\"executionRouterID\":{\"alchemyId\":4065,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"MicrosoftEdge\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"referenceSuiteID\":0}" );
        if ( eT.getMaximumAvailable() > 0 ) {
          eT.addProperty( "browserName", DataManager.instance().replaceValues( "MicrosoftEdge" ) + "" );
          eT.addProperty( "platformName", DataManager.instance().replaceValues( "ANY" ) + "" );
          targetList.add(eT);
        }
      }
      /*
      Targets from ecommerce application testing Execution Package
      Add a description of ecommerce application testing Execution Package
      */
      if ( hasValue( "ie", "32592.3915" ) ) {
        eT = new ExecutionTarget( "Chrome", "32592.3915", "32592.3914", 2 ,"{\"name\":\"Chrome\",\"description\":\"Add a description of Chrome\",\"alchemyId\":3915,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":2,\"executionRouterID\":{\"alchemyId\":3914,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"chrome\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\",\"referenceSuiteID\":0}" );
        if ( eT.getMaximumAvailable() > 0 ) {
          eT.addProperty( "browserName", DataManager.instance().replaceValues( "chrome" ) + "" );
          eT.addProperty( "platformName", DataManager.instance().replaceValues( "ANY" ) + "" );
          targetList.add(eT);
        }
      }
      if ( hasValue( "ie", "32592.3916" ) ) {
        eT = new ExecutionTarget( "Firefox", "32592.3916", "32592.3914", 2 ,"{\"name\":\"Firefox\",\"description\":\"Add a description of Firefox\",\"alchemyId\":3916,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":2,\"executionRouterID\":{\"alchemyId\":3914,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"firefox\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\",\"referenceSuiteID\":0}" );
        if ( eT.getMaximumAvailable() > 0 ) {
          eT.addProperty( "browserName", DataManager.instance().replaceValues( "firefox" ) + "" );
          eT.addProperty( "platformName", DataManager.instance().replaceValues( "ANY" ) + "" );
          targetList.add(eT);
        }
      }
      if ( hasValue( "ie", "32592.3917" ) ) {
        eT = new ExecutionTarget( "Microsoft Edge", "32592.3917", "32592.3914", 2 ,"{\"name\":\"Microsoft Edge\",\"description\":\"Add a description of Microsoft Edge\",\"alchemyId\":3917,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":2,\"executionRouterID\":{\"alchemyId\":3914,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"MicrosoftEdge\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\",\"referenceSuiteID\":0}" );
        if ( eT.getMaximumAvailable() > 0 ) {
          eT.addProperty( "browserName", DataManager.instance().replaceValues( "MicrosoftEdge" ) + "" );
          eT.addProperty( "platformName", DataManager.instance().replaceValues( "ANY" ) + "" );
          targetList.add(eT);
        }
      }
      

      //
      // This will add all targets to the correct router
      //
      targetList.forEach((t) -> {
        SuiteExecutionWrapper.instance().routerList.forEach((_t) -> {
          _t.addTarget(t);
        });
      });


      if ( cli.hasOption( "t" ) ) {
        TestManager.instance().setTags( cli.getOptionValues( "t" ));
      }

      if (testList.isEmpty() || targetList.isEmpty()) {
        if (testList.isEmpty()) {
          throw new IllegalArgumentException("No tests were added - nothing to do");
        }

        if (targetList.isEmpty()) {
          throw new IllegalArgumentException("No Execution Targets were defined - nowhere to run your tests");
        }
      } else {


      //
      // Configuration Integrations
      //
      Integration cI = null;
      if ( hasValue( "iw", "32592.3912" ) ) { 
        cI = new com.orasi.integration.embedded.selenium.SeleniumEmbedded();
        log.warn( "Enabling Integration: " + cI.getName() );
        cI.setProperty( "host", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "host", DataManager.instance().replaceValues( "localhost" ) + "" ) );
        cI.setProperty( "port", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "port", DataManager.instance().replaceValues( "4444" ) + "" ) );
        cI.setProperty( "managed", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "managed", DataManager.instance().replaceValues( "true" ) + "" ) );
      
        cI.initialize();
        cI.getHandlers().forEach((t) -> {
          addEventHandler(t);
        });
      }if ( hasValue( "iw", "32592.3918" ) ) { 
        cI = new com.orasi.integration.html.HTMLSerializer();
        log.warn( "Enabling Integration: " + cI.getName() );
        cI.setProperty( "outputFolder", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "outputFolder", DataManager.instance().replaceValues( "c:/Reports" ) + "" ) );
        cI.setProperty( "launchUi", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "launchUi", DataManager.instance().replaceValues( "true" ) + "" ) );
        cI.setProperty( "suiteTemplate", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "suiteTemplate", DataManager.instance().replaceValues( "" ) + "" ) );
        cI.setProperty( "testTemplate", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "testTemplate", DataManager.instance().replaceValues( "" ) + "" ) );
        cI.setProperty( "sourceTemplate", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "sourceTemplate", DataManager.instance().replaceValues( "" ) + "" ) );
      
        cI.initialize();
        cI.getHandlers().forEach((t) -> {
          addEventHandler(t);
        });
      }if ( hasValue( "iw", "32592.3919" ) ) { 
        cI = new com.orasi.integration.console.ExecutionConsole();
        log.warn( "Enabling Integration: " + cI.getName() );
        cI.setProperty( "color", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "color", DataManager.instance().replaceValues( "true" ) + "" ) );
      
        cI.initialize();
        cI.getHandlers().forEach((t) -> {
          addEventHandler(t);
        });
      }if ( hasValue( "iw", "32592.4172" ) ) { 
        cI = new com.orasi.integration.cloud.ExecutionCloud();
        log.warn( "Enabling Integration: " + cI.getName() );
        cI.setProperty( "cloudUrl", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "cloudUrl", DataManager.instance().replaceValues( "https://alchemytesting.cloud" ) + "" ) );
        cI.setProperty( "apiKey", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "apiKey", DataManager.instance().replaceValues( "" ) + "" ) );
        cI.setProperty( "secretKey", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "secretKey", DataManager.instance().replaceValues( "" ) + "" ) );
        cI.setProperty( "launchUI", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "launchUI", DataManager.instance().replaceValues( "false" ) + "" ) );
        cI.setProperty( "gridInactivityTimeout", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "gridInactivityTimeout", DataManager.instance().replaceValues( "60" ) + "" ) );
        cI.setProperty( "nodeInactivityTimeout", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "nodeInactivityTimeout", DataManager.instance().replaceValues( "30" ) + "" ) );
        cI.setProperty( "nodeInactivityStartupDelay", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "nodeInactivityStartupDelay", DataManager.instance().replaceValues( "30" ) + "" ) );
        cI.setProperty( "gridId", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "gridId", DataManager.instance().replaceValues( "" ) + "" ) );
        cI.setProperty( "localTesting", SuiteExecutionWrapper.instance().getOption( cI.getKey(), "localTesting", DataManager.instance().replaceValues( "false" ) + "" ) );
      
        cI.initialize();
        cI.getHandlers().forEach((t) -> {
          addEventHandler(t);
        });
      }

        //
      // Get all active targets
      //
      List<String> targetNames = new ArrayList<>(10);
      List<String> localTargetNames = new ArrayList<>(10);
      SuiteExecutionWrapper.instance().routerList.forEach((_t) -> {
        _t.getTargetList().forEach((t) -> {
          
          String useName = t.getName() + " from " + _t.getName();
          useName = useName.toLowerCase();
          
          if ( localTargetNames.contains( useName ) ) {
            t.setName( t.getName() + "_" + generateCharacters( 5 ) );
            targetNames.add(t.getName() + " from " + _t.getName());
          } else {
            localTargetNames.add( useName );
            targetNames.add(t.getName() + " from " + _t.getName());
          }
        });
      });

      SuiteExecutionWrapper.instance().targetList = targetNames;
      SuiteExecutionWrapper.instance().testList = TestManager.instance().getTestNames();
      SuiteExecutionWrapper.instance().start();
      }  
    }
    
    public int getExecutionId() {
      return executionId;
    }
  
    public static SuiteExecutionWrapper instance() {
      return singleton;
    }
  
    private ExecutorService eS;
  
    public void start() {
      
      if ( cli.hasOption( "p" ) ) {
        try { threadCount = Integer.parseInt( cli.getOptionValue( "p" ) ); } catch( Exception e ) {}
      }

      if ( cli.hasOption( "f" ) ) {
        try { checkFailureLevel = Integer.parseInt( cli.getOptionValue( "f" ) ); } catch( Exception e ) {}
      }

      executionId = 1;

      int totalTargets = 0;
      for ( Router _t : SuiteExecutionWrapper.instance().routerList ) {
        for ( ExecutionTarget t : _t.getTargetList() ){
          totalTargets++;
        }
      }

      Map<String, String> cliMap = new HashMap<>(5);
      for (Option o : cli.getOptions()) {
        if (o.getLongOpt() != null && !o.getLongOpt().trim().isEmpty()) {
          cliMap.put(o.getLongOpt() + " (-" + o.getOpt() + ")", String.join(",", o.getValuesList()));
        } else {
          cliMap.put("-" + o.getOpt(), String.join(",", o.getValuesList()));
        }
      }

      if ( totalTargets == 0 ) {
        throw new IllegalArgumentException( "No targets were defined therefore there was nowhere to run your tests" );
      }

      if ( TestManager.instance().getSize() == 0 ) {
        throw new IllegalArgumentException( "There were no tests to run" );
      }
      

      int totalTasks = TestManager.instance().getSize() * totalTargets;

      ExecutionQueue eQ = new ExecutionQueue(totalTasks > threadCount ? threadCount : totalTasks, 3, 3);
      if ( threadCount > totalTasks ) {
        threadCount = totalTasks;
      }

      SuitePayload suitePayload = new SuitePayload();
      suitePayload.setExecutionIdentifier( executionId );
      suitePayload.setName(name);
      suitePayload.setDescription(description);
      suitePayload.setUserName(userName);
      suitePayload.setSuiteDetail(  "{\"id\":7213,\"name\":\"ecommerce application testing\",\"description\":\"No description was added for ecommerce application testing\",\"userId\":15,\"userConfidence\":0,\"organizationId\":1,\"organizationConfidence\":0,\"status\":1,\"endpointId\":1,\"endpointStyleId\":1,\"targetId\":7381,\"targetConfigurationId\":0,\"targetVersionId\":0,\"reviewFlag\":0,\"importTests\":0,\"importFunctions\":0,\"importSites\":0,\"importTargets\":0,\"importData\":0,\"shareCount\":3,\"conductorList\":[{\"reviewFlag\":0,\"id\":6477,\"name\":\"Grid\",\"description\":\"Add a description of Grid\",\"alchemyId\":4064,\"alchemySeed\":32592,\"organizationId\":1,\"userId\":15,\"version\":0,\"targetDetail\":\"[{\\\"name\\\": \\\"Chrome\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"alchemyId\\\": 4066, \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Chrome\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"browserName\\\", \\\"value\\\": \\\"chrome\\\"}, {\\\"name\\\": \\\"platformName\\\", \\\"value\\\": \\\"ANY\\\"}], \\\"organizationId\\\": 0, \\\"maximumAvailable\\\": 1, \\\"referenceSuiteID\\\": 0, \\\"executionRouterID\\\": {\\\"alchemyId\\\": 4065, \\\"alchemySeed\\\": 32592}}, {\\\"name\\\": \\\"Firefox\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"alchemyId\\\": 4067, \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Firefox\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"browserName\\\", \\\"value\\\": \\\"firefox\\\"}, {\\\"name\\\": \\\"platformName\\\", \\\"value\\\": \\\"ANY\\\"}], \\\"organizationId\\\": 0, \\\"maximumAvailable\\\": 1, \\\"referenceSuiteID\\\": 0, \\\"executionRouterID\\\": {\\\"alchemyId\\\": 4065, \\\"alchemySeed\\\": 32592}}, {\\\"name\\\": \\\"Microsoft Edge\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"alchemyId\\\": 4068, \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Microsoft Edge\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"browserName\\\", \\\"value\\\": \\\"MicrosoftEdge\\\"}, {\\\"name\\\": \\\"platformName\\\", \\\"value\\\": \\\"ANY\\\"}], \\\"organizationId\\\": 0, \\\"maximumAvailable\\\": 1, \\\"referenceSuiteID\\\": 0, \\\"executionRouterID\\\": {\\\"alchemyId\\\": 4065, \\\"alchemySeed\\\": 32592}}]\",\"routerDetail\":\"[{\\\"name\\\": \\\"Grid\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"routerId\\\": 8, \\\"alchemyId\\\": 4065, \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Grid\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"URL\\\", \\\"value\\\": \\\"#{alchemy.cloud.url}\\\"}], \\\"organizationId\\\": 0}]\",\"status\":1,\"lockUserId\":0,\"changed\":false,\"createDate\":\"Apr 3, 2025, 6:09:19 AM\",\"modifyDate\":\"Apr 3, 2025, 6:09:19 AM\",\"targetList\":[{\"name\":\"Chrome\",\"description\":\"Chrome\",\"alchemyId\":4066,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":1,\"executionRouterID\":{\"alchemyId\":4065,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"chrome\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"referenceSuiteID\":0},{\"name\":\"Firefox\",\"description\":\"Firefox\",\"alchemyId\":4067,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":1,\"executionRouterID\":{\"alchemyId\":4065,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"firefox\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"referenceSuiteID\":0},{\"name\":\"Microsoft Edge\",\"description\":\"Microsoft Edge\",\"alchemyId\":4068,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":1,\"executionRouterID\":{\"alchemyId\":4065,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"MicrosoftEdge\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"referenceSuiteID\":0}],\"routerList\":[{\"name\":\"Grid\",\"description\":\"Grid\",\"alchemyId\":4065,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"routerId\":8,\"status\":0,\"userId\":0,\"propertyList\":[{\"name\":\"URL\",\"value\":\"#{alchemy.cloud.url}\"}]}],\"acls\":[]},{\"reviewFlag\":0,\"id\":6476,\"name\":\"ecommerce application testing Execution Package\",\"description\":\"Add a description of ecommerce application testing Execution Package\",\"alchemyId\":3913,\"alchemySeed\":32592,\"organizationId\":1,\"userId\":15,\"version\":0,\"targetDetail\":\"[{\\\"name\\\": \\\"Chrome\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"alchemyId\\\": 3915, \\\"createDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"modifyDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Add a description of Chrome\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"browserName\\\", \\\"value\\\": \\\"chrome\\\"}, {\\\"name\\\": \\\"platformName\\\", \\\"value\\\": \\\"ANY\\\"}], \\\"organizationId\\\": 0, \\\"maximumAvailable\\\": 2, \\\"referenceSuiteID\\\": 0, \\\"executionRouterID\\\": {\\\"alchemyId\\\": 3914, \\\"alchemySeed\\\": 32592}}, {\\\"name\\\": \\\"Firefox\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"alchemyId\\\": 3916, \\\"createDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"modifyDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Add a description of Firefox\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"browserName\\\", \\\"value\\\": \\\"firefox\\\"}, {\\\"name\\\": \\\"platformName\\\", \\\"value\\\": \\\"ANY\\\"}], \\\"organizationId\\\": 0, \\\"maximumAvailable\\\": 2, \\\"referenceSuiteID\\\": 0, \\\"executionRouterID\\\": {\\\"alchemyId\\\": 3914, \\\"alchemySeed\\\": 32592}}, {\\\"name\\\": \\\"Microsoft Edge\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"alchemyId\\\": 3917, \\\"createDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"modifyDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Add a description of Microsoft Edge\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"browserName\\\", \\\"value\\\": \\\"MicrosoftEdge\\\"}, {\\\"name\\\": \\\"platformName\\\", \\\"value\\\": \\\"ANY\\\"}], \\\"organizationId\\\": 0, \\\"maximumAvailable\\\": 2, \\\"referenceSuiteID\\\": 0, \\\"executionRouterID\\\": {\\\"alchemyId\\\": 3914, \\\"alchemySeed\\\": 32592}}]\",\"routerDetail\":\"[{\\\"name\\\": \\\"ecommerce application testing router\\\", \\\"status\\\": 0, \\\"userId\\\": 0, \\\"changed\\\": false, \\\"routerId\\\": 1, \\\"alchemyId\\\": 3914, \\\"createDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"modifyDate\\\": \\\"Apr 3, 2025, 6:01:22 AM\\\", \\\"alchemySeed\\\": 32592, \\\"description\\\": \\\"Add a description of ecommerce application testing router\\\", \\\"propertyList\\\": [{\\\"name\\\": \\\"URL\\\", \\\"value\\\": \\\"http://localhost:4444/wd/hub\\\"}], \\\"organizationId\\\": 0}]\",\"status\":1,\"lockUserId\":0,\"changed\":false,\"createDate\":\"Apr 3, 2025, 6:05:25 AM\",\"modifyDate\":\"Apr 3, 2025, 6:05:25 AM\",\"targetList\":[{\"name\":\"Chrome\",\"description\":\"Add a description of Chrome\",\"alchemyId\":3915,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":2,\"executionRouterID\":{\"alchemyId\":3914,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"chrome\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\",\"referenceSuiteID\":0},{\"name\":\"Firefox\",\"description\":\"Add a description of Firefox\",\"alchemyId\":3916,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":2,\"executionRouterID\":{\"alchemyId\":3914,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"firefox\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\",\"referenceSuiteID\":0},{\"name\":\"Microsoft Edge\",\"description\":\"Add a description of Microsoft Edge\",\"alchemyId\":3917,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"maximumAvailable\":2,\"executionRouterID\":{\"alchemyId\":3914,\"alchemySeed\":32592},\"propertyList\":[{\"name\":\"browserName\",\"value\":\"MicrosoftEdge\"},{\"name\":\"platformName\",\"value\":\"ANY\"}],\"status\":0,\"userId\":0,\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\",\"referenceSuiteID\":0}],\"routerList\":[{\"name\":\"ecommerce application testing router\",\"description\":\"Add a description of ecommerce application testing router\",\"alchemyId\":3914,\"alchemySeed\":32592,\"organizationId\":0,\"changed\":false,\"routerId\":1,\"status\":0,\"userId\":0,\"propertyList\":[{\"name\":\"URL\",\"value\":\"http://localhost:4444/wd/hub\"}],\"createDate\":\"Apr 3, 2025, 6:01:22 AM\",\"modifyDate\":\"Apr 3, 2025, 6:01:22 AM\"}],\"acls\":[]}],\"version\":8,\"lockUserId\":15,\"testDisplay\":0,\"alchemyId\":3911,\"alchemySeed\":32592,\"referenceSuiteID\":0,\"changed\":false}" );
      suitePayload.setTestList(testList);
      suitePayload.setTargetList(targetList);
      suitePayload.setTotalTests(totalTasks);
      suitePayload.setOtherValue( "threadCount", threadCount );
      suitePayload.setOtherValue( "requestedThreadCount", totalTasks );
      suitePayload.setOtherValue( "cliMap", cliMap );
      suitePayload.setOtherValue( "routers", new Gson().toJson( SuiteExecutionWrapper.instance().routerList ) );
      notifyListeners( new SuiteEvent( suitePayload, name, 1 ) );

      
      log.warn(TestManager.instance().getSize() + " Tests executing across " + totalTargets + " devices ");
      log.warn("Submitting " + totalTasks + " tasks for execution");
      addEventHandler(new TestFailureHandler());

      TestManager.instance().getTests().forEach((t) -> {
        SuiteExecutionWrapper.instance().routerList.forEach((r) -> {
          r.getTargetList().forEach((_t) -> {
            eQ.addExecution(new EndpointDevice(r, _t), t);
          });
        });
      });

      eQ.run();

    boolean suiteFailed = (failureCount > 0 && failureLevel >= checkFailureLevel) || failureCount >= totalTasks;

    if (suiteFailed) {
      log.atWarn().log("Notifying Integration of Suite Failure");
      try {
        notifyListeners(new SuiteEvent(suitePayload, name, EventHook.FAILURE.getId()));
      } catch (Exception e) {
        log.atError().log("Failed to notify suites of failure");
      }
    } else {

      log.atWarn().log("Notifying Integration of Suite Success");
      try {
        notifyListeners(new SuiteEvent(suitePayload, name, EventHook.SUCCESS.getId()));
      } catch (Exception e) {
        log.atError().log("Failed to notify suites of success");
      }
    }

    log.atWarn().log("Notifying Integration of Suite Complete");
    try {
      notifyListeners(new SuiteEvent(suitePayload, name, EventHook.AFTER.getId()));
    } catch (Exception e) {
      log.atError().log("Failed to notify suites of success");
    }

    if (!cli.hasOption("n")) {
      if (suiteFailed) {
        System.exit(-1);
      } else {
        System.exit(0);
      }
    }
  }
  
  
    /**
     * @return the name
     */
    public String getName() {
      return name;
    }
  
    /**
     * @param name the name to set
     */
    private void setName(String name) {
      this.name = name;
    }
  
    /**
     * @return the description
     */
    public String getDescription() {
      return description;
    }
  
    /**
     * @param description the description to set
     */
    private void setDescription(String description) {
      this.description = description;
    }
  
    /**
     * @return the id
     */
    public int getId() {
      return id;
    }
  
    /**
     * @param id the id to set
     */
    private void setId(int id) {
      this.id = id;
    }
  
    /**
     * @return the userName
     */
    public String getUserName() {
      return userName;
    }
  
    /**
     * @param userName the userName to set
     */
    private void setUserName(String userName) {
      this.userName = userName;
    }
  
  }
  