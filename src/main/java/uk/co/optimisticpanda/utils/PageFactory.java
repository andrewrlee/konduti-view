package uk.co.optimisticpanda.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import uk.co.optimisticpanda.storybuilder.DebugReporter;

public class PageFactory {


  /**
   * Similar to the other "initElements" methods, but takes an {@link ElementLocatorFactory} which
   * is used for providing the mechanism for fniding elements. If the ElementLocatorFactory returns
   * null then the field won't be decorated.
   * 
   * @param factory The factory to use
   * @param page The object to decorate the fields of
   */
  public static void initElements(WebDriver driver, DebugReporter reporter, Object page) {
    final ElementLocatorFactory factoryRef = new DefaultElementLocatorFactory(driver);
    org.openqa.selenium.support.PageFactory.initElements( //
    		new ReportingFieldDecorator(factoryRef, reporter), // 
    		page)
    		;
  }
	
}
