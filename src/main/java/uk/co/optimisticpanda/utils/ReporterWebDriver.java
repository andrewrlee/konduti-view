package uk.co.optimisticpanda.utils;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.Logs;

import com.google.common.collect.Lists;

import uk.co.optimisticpanda.storybuilder.DebugReporter;

public class ReporterWebDriver implements WebDriver, TakesScreenshot {

	private DebugReporter reporter;
	private final WebDriver delegate;

	public ReporterWebDriver(WebDriver delegate){
		this.delegate = delegate;
	}
	
	public void setDebugReporter(DebugReporter debugReporter){
		reporter = debugReporter;
	}
	
	@Override
	public void get(String url) {
		reporter.reportStepMessage("Loading page with url '" + url +"'" );
		delegate.get(url);
	}

	@Override
	public String getCurrentUrl() {
		reporter.reportStepMessage("The current url is '" + delegate.getCurrentUrl() + "'");
		return delegate.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		reporter.reportStepMessage("The current page title is '" + delegate.getTitle() + "'");
		return delegate.getTitle();
	}

	@Override
	public List<WebElement> findElements(By by) {
		List<WebElement> elements = Lists.newArrayList();
		for (WebElement element : delegate.findElements(by)) {
			elements.add(new DebugWebElement(reporter, element));
		}
		return elements;
	}

	@Override
	public WebElement findElement(By by) {
		return new DebugWebElement(reporter, delegate.findElement(by));
	}

	@Override
	public String getPageSource() {
		return delegate.getPageSource();
	}

	@Override
	public void close() {
		reporter.reportStepMessage("closing!");
		delegate.close();
	}

	@Override
	public void quit() {
		reporter.reportStepMessage("quitting!");
		delegate.quit();
	}
	
	@Override
	public Set<String> getWindowHandles() {
		return delegate.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return delegate.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		reporter.reportStepMessage("switching to.. ");
		return delegate.switchTo();
	}

	@Override
	public Navigation navigate() {
		return new DebugNavigation(reporter, delegate.navigate());
	}

	@Override
	public Options manage() {
		return new DebugOptions(reporter, delegate.manage());
	}
	
	@Override
	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
		return ((TakesScreenshot) delegate).getScreenshotAs(target);
	}
	
	public boolean isTakesScreenshot(){
		return delegate instanceof TakesScreenshot;
	}
	
	private static class DebugOptions implements Options{

		private final DebugReporter reporter;
		private final Options delegate;

		public DebugOptions(DebugReporter reporter, Options delegate){
			this.reporter = reporter;
			this.delegate = delegate;
		}
		
		@Override
		public void addCookie(Cookie cookie) {
			reporter.reportStepMessage("adding cookie '" + cookie + "'");
			delegate.addCookie(cookie);
			
		}

		@Override
		public void deleteCookieNamed(String name) {
			reporter.reportStepMessage("deleting cookie called '" + name + "'");
			delegate.deleteCookieNamed(name);
		}

		@Override
		public void deleteCookie(Cookie cookie) {
			reporter.reportStepMessage("deleting cookie called '" + cookie.getName() +"'");
			delegate.deleteCookie(cookie);
		}

		@Override
		public void deleteAllCookies() {
			reporter.reportStepMessage("deleting all cookies");
			delegate.deleteAllCookies();
		}

		@Override
		public Set<Cookie> getCookies() {
			return delegate.getCookies();
		}

		@Override
		public Cookie getCookieNamed(String name) {
			return delegate.getCookieNamed(name);
		}

		@Override
		public Timeouts timeouts() {
			return delegate.timeouts();
		}

		@Override
		public ImeHandler ime() {
			return delegate.ime();
		}

		@Override
		public Window window() {
			return delegate.window();
		}

		@Override
		public Logs logs() {
			return delegate.logs();
		}
	}
	private static class DebugNavigation implements Navigation{

		private final DebugReporter reporter;
		private final Navigation delegate;

		public DebugNavigation(DebugReporter reporter, Navigation delegate){
			this.reporter = reporter;
			this.delegate = delegate;
		}
		
		@Override
		public void back() {
			reporter.reportStepMessage("clicking back");
			delegate.back();
		}

		@Override
		public void forward() {
			reporter.reportStepMessage("clicking forward");
			delegate.forward();
		}

		@Override
		public void to(String url) {
			reporter.reportStepMessage("navigating to '" + url + "'");
			delegate.to(url);
		}

		@Override
		public void to(URL url) {
			reporter.reportStepMessage("navigating to '" + url + "'");
			delegate.to(url);
		}

		@Override
		public void refresh() {
			reporter.reportStepMessage("refreshing");
			delegate.refresh();
		}
	}
	
	public static class DebugWebElement implements WebElement{

		private final WebElement delegate;
		private DebugReporter reporter;

		public DebugWebElement(DebugReporter reporter, WebElement delegate){
			this.delegate = delegate;
			this.reporter = reporter;
		}
		
		@Override
		public void click() {
			reporter.reportStepMessage("Clicking " + getIdentifier());
			delegate.click();
		}

		
		@Override
		public void submit() {
			reporter.reportStepMessage("Submitting " + getIdentifier() );
			delegate.submit();
		}

		@Override
		public void sendKeys(CharSequence... keysToSend) {
			reporter.reportStepMessage("typing '" + Arrays.toString(keysToSend) + "' in to "  + getIdentifier());
			delegate.sendKeys(keysToSend);
		}

		@Override
		public void clear() {
			reporter.reportStepMessage("Clearing " + getIdentifier());
			delegate.clear();
		}

		@Override
		public String getTagName() {
			return delegate.getTagName();
		}

		@Override
		public String getAttribute(String name) {
			return delegate.getAttribute(name);
		}

		@Override
		public boolean isSelected() {
			return delegate.isSelected();
		}

		@Override
		public boolean isEnabled() {
			return delegate.isEnabled();
		}

		@Override
		public String getText() {
			return delegate.getText();
		}

		@Override
		public List<WebElement> findElements(By by) {
			return delegate.findElements(by);
		}

		@Override
		public WebElement findElement(By by) {
			return delegate.findElement(by);
		}

		@Override
		public boolean isDisplayed() {
			return delegate.isDisplayed();
		}

		@Override
		public Point getLocation() {
			return delegate.getLocation();
		}

		@Override
		public Dimension getSize() {
			return delegate.getSize();
		}

		@Override
		public String getCssValue(String propertyName) {
			return delegate.getCssValue(propertyName);
		}
		private String getIdentifier(){
			if (delegate.getAttribute("id") != null){
				return "element with id '"  + delegate.getAttribute("id") + "'";
			}else if (delegate.getAttribute("name") != null){
				return "element with name '"  + delegate.getAttribute("name") + "'";
			}
			return "element '" + this.toString() + "'"; 
		}
		
	}

}
