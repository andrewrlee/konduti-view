package uk.co.optimisticpanda.base;

import java.util.logging.Level;
import java.util.logging.LogManager;

import org.slf4j.bridge.SLF4JBridgeHandler;

import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

public enum LoggingSetup {
	INSTANCE;
	
	public static void setup() {
		// Redirect java.util.logging --> SLF4J
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.install();
		java.util.logging.Logger.getLogger("global").setLevel(Level.FINEST);
		// Redirect System.out --> SLF4J
		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();

	}
}
