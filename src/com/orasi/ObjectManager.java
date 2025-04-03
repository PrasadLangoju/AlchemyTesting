
package com.orasi;

import java.util.*;
import org.openqa.selenium.By;
import org.openqa.selenium.By.*;
import org.openqa.selenium.*;
import com.orasi.datasource.*;


public class ObjectManager {
  private static final ObjectManager singleton = new ObjectManager();
  
  public static final ObjectManager instance() {
    return singleton;
  }
  
  private final Map<String,ByFactory> objectMap = new HashMap<>( 10 );
  
  private ObjectManager() {
    ByFactoryCollection bC = null;
    /*
    Site: www.facebook.com
    Add a description of www.facebook.com
    */
    /* Page: Facebook  log in or sign up 
    
    */

    

bC = new ByFactoryCollection("email", "32592.4081", "");
bC.add( new ByFactory( ByXPath.class, "//input[@type='text']", "AttributeRule", "32592.4083", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@type='text']", "AttributeRule", "32592.4085", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@class='inputtext _55r1 _6luy']", "AttributeRule", "32592.4087", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@class='inputtext _55r1 _6luy']", "AttributeRule", "32592.4089", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@data-testid='royal-email']", "AttributeRule", "32592.4091", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@data-testid='royal-email']", "AttributeRule", "32592.4093", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@placeholder='Email address or phone number']", "AttributeRule", "32592.4095", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@placeholder='Email address or phone number']", "AttributeRule", "32592.4097", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@autofocus='1']", "AttributeRule", "32592.4099", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@autofocus='1']", "AttributeRule", "32592.4101", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@aria-label='Email address or phone number']", "AttributeRule", "32592.4103", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@aria-label='Email address or phone number']", "AttributeRule", "32592.4105", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@id=\"email\"]", "IDRule", "32592.4107", "" ) );
bC.add( new ByFactory( ById.class, "email", "IDRule", "32592.4109", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@id=\"email\"]", "IDRule", "32592.4111", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@name=\"email\"]", "NameRule", "32592.4113", "" ) );
bC.add( new ByFactory( ByName.class, "email", "NameRule", "32592.4115", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@name=\"email\"]", "NameRule", "32592.4117", "" ) );


objectMap.put( "32592.4081", bC );


bC = new ByFactoryCollection("pass", "32592.4123", "");
bC.add( new ByFactory( ByXPath.class, "//input[@type='password']", "AttributeRule", "32592.4125", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@type='password']", "AttributeRule", "32592.4127", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@class='inputtext _55r1 _6luy _9npi']", "AttributeRule", "32592.4129", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@class='inputtext _55r1 _6luy _9npi']", "AttributeRule", "32592.4131", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@data-testid='royal-pass']", "AttributeRule", "32592.4133", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@data-testid='royal-pass']", "AttributeRule", "32592.4135", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@placeholder='Password']", "AttributeRule", "32592.4137", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@placeholder='Password']", "AttributeRule", "32592.4139", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@aria-label='Password']", "AttributeRule", "32592.4141", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@aria-label='Password']", "AttributeRule", "32592.4143", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@id=\"pass\"]", "IDRule", "32592.4145", "" ) );
bC.add( new ByFactory( ById.class, "pass", "IDRule", "32592.4147", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@id=\"pass\"]", "IDRule", "32592.4149", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@name=\"pass\"]", "NameRule", "32592.4151", "" ) );
bC.add( new ByFactory( ByName.class, "pass", "NameRule", "32592.4153", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@name=\"pass\"]", "NameRule", "32592.4155", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@class='_6luy _55r1 _1kbt _9nyi']/input[1]", "ParentRule[AttributeRule]", "32592.4157", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@class='_6luy _55r1 _1kbt _9nyi']/input[1]", "ParentRule[AttributeRule]", "32592.4159", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@id=\"passContainer\"]/input[1]", "ParentRule[IDRule]", "32592.4161", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@id=\"passContainer\"]/input[1]", "ParentRule[IDRule]", "32592.4163", "" ) );


objectMap.put( "32592.4123", bC );
/*
    Site: www.snapdeal.com
    Add a description of www.snapdeal.com
    */
    /* Page: Shop Online for Men, Women & Kids Clothing, Shoes, Home Decor Items 
    
    */

    

bC = new ByFactoryCollection("keyword", "32592.3929", "");
bC.add( new ByFactory( ByXPath.class, "//div[@class='overlap']/following-sibling::input[1]", "SiblingRule", "32592.3931", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@class='col-xs-20 searchformInput keyword']", "AttributeRule", "32592.3933", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@class='col-xs-20 searchformInput keyword']", "AttributeRule", "32592.3935", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@onkeypress='clickGo(event, this)']", "AttributeRule", "32592.3937", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@onkeypress='clickGo(event, this)']", "AttributeRule", "32592.3939", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@placeholder='Search products & brands']", "AttributeRule", "32592.3941", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@placeholder='Search products & brands']", "AttributeRule", "32592.3943", "" ) );
bC.add( new ByFactory( ByXPath.class, "//input[@id=\"inputValEnter\"]", "IDRule", "32592.3945", "" ) );
bC.add( new ByFactory( ById.class, "inputValEnter", "IDRule", "32592.3947", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@id=\"inputValEnter\"]", "IDRule", "32592.3949", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@class='col-xs-14 search-box-wrapper']/input[1]", "ParentRule[AttributeRule]", "32592.3951", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@class='col-xs-14 search-box-wrapper']/input[1]", "ParentRule[AttributeRule]", "32592.3953", "" ) );
bC.add( new ByFactory( ByXPath.class, "(//input[@autocomplete='off'])[2]", "IndexedAttributeRule", "32592.3955", "" ) );
bC.add( new ByFactory( ByXPath.class, "(//input[@type='text'])[2]", "IndexedAttributeRule", "32592.3957", "" ) );
bC.add( new ByFactory( ByXPath.class, "/body[1]/div[2]/div[4]/div[2]/div[1]/div[2]/input[1]", "AbsoluteRule", "32592.3959", "" ) );


objectMap.put( "32592.3929", bC );
/* Page: default 
    
    */

    /* Page: Snapdeal.com - Online shopping India- Discounts - shop Online Perfumes, Watches, sunglasses etc 
    
    */

    

bC = new ByFactoryCollection("dp-widget-linkhashAdded", "32592.3968", "");
bC.add( new ByFactory( ByXPath.class, "//a[./input[@value='https://g.sdlcdn.com/imgs/k/t/64x75/Mandoth-Cotton-Blend-Regular-Fit-SDL625762009-1-76a23.jpg?w=220&h=258&sharp=7']]", "ChildRule[AttributeRule]", "32592.3970", "" ) );
bC.add( new ByFactory( ByXPath.class, "//a[./*[@value='https://g.sdlcdn.com/imgs/k/t/64x75/Mandoth-Cotton-Blend-Regular-Fit-SDL625762009-1-76a23.jpg?w=220&h=258&sharp=7']]", "ChildRule[AttributeRule]", "32592.3972", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@data-js-pos='0']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3974", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@data-js-pos='0']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3976", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@data-catid='658920218315']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3978", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@data-catid='658920218315']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3980", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@data-defaultsupcforfmcg='SDL625762009']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3982", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@data-defaultsupcforfmcg='SDL625762009']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3984", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@data-vendorcode='S08efe']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3986", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@data-vendorcode='S08efe']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3988", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@data-defaultcatalogueidforfmcg='658920218315']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3990", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@data-defaultcatalogueidforfmcg='658920218315']/div[1]/a[1]", "ParentRule[AttributeRule]", "32592.3992", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@id=\"686464225564\"]/div[1]/a[1]", "ParentRule[IDRule]", "32592.3994", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@id=\"686464225564\"]/div[1]/a[1]", "ParentRule[IDRule]", "32592.3996", "" ) );
bC.add( new ByFactory( ByXPath.class, "(//a[@class='dp-widget-link hashAdded'])[1]", "IndexedAttributeRule", "32592.3998", "" ) );
bC.add( new ByFactory( ByXPath.class, "(//a[@pogid='686464225564'])[1]", "IndexedAttributeRule", "32592.4000", "" ) );
bC.add( new ByFactory( ByXPath.class, "(//a[@href='https://www.snapdeal.com/product/mandoth-100-percent-cotton-white/686464225564#bcrumbSearch:Shirt'])[1]", "IndexedAttributeRule", "32592.4002", "" ) );
bC.add( new ByFactory( ByXPath.class, "(//a[@data-position='0;107471'])[1]", "IndexedAttributeRule", "32592.4004", "" ) );
bC.add( new ByFactory( ByXPath.class, "(//a[@target='_blank'])[2]", "IndexedAttributeRule", "32592.4006", "" ) );


objectMap.put( "32592.3968", bC );


bC = new ByFactoryCollection("VIEWDETAILS", "32592.4009", "");
bC.add( new ByFactory( ByXPath.class, "//div[@class='rating-sec clearfix parT5 padB5']/following-sibling::a[1]", "SiblingRule", "32592.4011", "" ) );
bC.add( new ByFactory( ByXPath.class, "//a[@class=' btn btn-theme-secondary prodDetailBtn']", "AttributeRule", "32592.4013", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@class=' btn btn-theme-secondary prodDetailBtn']", "AttributeRule", "32592.4015", "" ) );
bC.add( new ByFactory( ByXPath.class, "//a[@href='/product/mandoth-100-percent-cotton-white/686464225564']", "AttributeRule", "32592.4017", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@href='/product/mandoth-100-percent-cotton-white/686464225564']", "AttributeRule", "32592.4019", "" ) );
bC.add( new ByFactory( ByXPath.class, "//div[@class='col-xs-13 quickProductDescPanel']/a[1]", "ParentRule[AttributeRule]", "32592.4021", "" ) );
bC.add( new ByFactory( ByXPath.class, "//*[@class='col-xs-13 quickProductDescPanel']/a[1]", "ParentRule[AttributeRule]", "32592.4023", "" ) );
bC.add( new ByFactory( ByXPath.class, "/body[1]/div[7]/div[2]/div[4]/div[1]/div[1]/div[2]/a[1]", "AbsoluteRule", "32592.4025", "" ) );


objectMap.put( "32592.4009", bC );
/* Page: Buy Mandoth Cotton Blend Regular Fit Full Sleeves Men's Formal Shirt - White ( Pack of 1 ) Online at Best Price in India - Snapdeal 
    
    */

    



  }
  
  public By getObject( Object alchemyIdentifier, Map<String,Object> contextMap, DataSourceProvider dM ) {
    
    if ( alchemyIdentifier instanceof By ) {
      return (By) alchemyIdentifier;
    }
    
    ByFactory by = objectMap.get( alchemyIdentifier + "" );
    if ( by == null ) {
      return new By() {
        @Override
        public List<WebElement> findElements(SearchContext sc) {
          throw new RuntimeException( "Could not find and object using [" + alchemyIdentifier + "]" );
        }
      };
    }
    return by.create(contextMap, dM);
  }

  public ByFactory getObject( String alchemyIdentifier ) {
   
    return objectMap.get( alchemyIdentifier );
  }
}
