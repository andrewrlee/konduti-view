/*
Copyright 2007-2009 WebDriver committers
Copyright 2007-2009 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package uk.co.optimisticpanda.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import uk.co.optimisticpanda.storybuilder.DebugReporter;

/**
 * A reporting decorator for use with PageFactory. Will decorate 1) all of the
 * WebElement fields and 2) List<WebElement> fields that have @FindBy or
 * 
 * @FindBys annotation with a proxy that locates the elements using the passed
 *          in ElementLocatorFactory.
 */
public class ReportingFieldDecorator extends DefaultFieldDecorator {

	private final DebugReporter reporter;
	
	public ReportingFieldDecorator(ElementLocatorFactory factory, DebugReporter reporter) {
		super(factory);
		this.reporter = reporter;
	}

	protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
		InvocationHandler handler = new ElementHandler(reporter, locator);
		return (WebElement) Proxy.newProxyInstance(loader, new Class[] { WebElement.class, WrapsElement.class, Locatable.class }, handler);
	}

	@SuppressWarnings("unchecked")
	protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
		InvocationHandler handler = new ElementListHandler(reporter, locator);
		return (List<WebElement>) Proxy.newProxyInstance(loader, new Class[] { List.class }, handler);
	}

	public static class ElementHandler implements InvocationHandler{
		private final ElementLocator locator;
		private final DebugReporter reporter;

		  public ElementHandler(DebugReporter reporter, ElementLocator locator) {
		    this.reporter = reporter;
			this.locator = locator;
		  }

		  public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		    WebElement element = locator.findElement();

		    if ("getWrappedElement".equals(method.getName())) {
		      return element;
		    }

		    try {
		    	
		      return method.invoke(element, objects);
		    } catch (InvocationTargetException e) {
		      throw e.getCause();
		    }
		  }
	}
	public static class ElementListHandler implements InvocationHandler {
		  private final ElementLocator locator;

		  public ElementListHandler(DebugReporter reporter, ElementLocator locator) {
		    this.locator = locator;
		  }

		  public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
		    List<WebElement> elements = locator.findElements();

		    try {
		      return method.invoke(elements, objects);
		    } catch (InvocationTargetException e) {
		      // Unwrap the underlying exception
		      throw e.getCause();
		    }
		  }
		}
}
