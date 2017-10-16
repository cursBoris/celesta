package ru.curs.celesta.dbutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;

import org.junit.*;

import ru.curs.celesta.*;
import ru.curs.celesta.syscursors.LogSetupCursor;

public class BasicCursorTest {

	private static Celesta celesta;

	private SessionContext sc = new SessionContext("super", "foo");
	private BasicCursor c;

	@BeforeClass
	public static void init() throws IOException, CelestaException {
		Properties properties = new Properties();
		properties.setProperty("score.path", "score");
		properties.setProperty("h2.in-memory", "true");

		try {
			celesta = Celesta.createInstance(properties);
		} catch (CelestaException e) {
			// do nothing, Celesta is initialized!
		}
	}

	@AfterClass
	public static void destroy() {
		celesta.close();
	}

	@Before
	public void before() throws CelestaException {
		c = new LogSetupCursor(celesta.callContext(sc));
	}

	@After
	public void after() throws CelestaException {
		c.close();
	}

	@Test
	public void cursorIsNavigable() throws CelestaException {
		c.setFilter("grainid", "'b'%");
		LogSetupCursor c2 = (LogSetupCursor) c;
		c2.setGrainid("grainval");
		c2.setTablename("tablenameval");
		c2.setI(true);
		c2.setM(false);
		c2.setD(true);
		boolean itWas = false;
		try {
			c.navigate("=s><+-");
		} catch (CelestaException e) {
			// System.out.println(e.getMessage());
			itWas = true;
		}
		assertTrue(itWas);
		c.navigate("=><+-");
	}

	@Test
	public void fieldsAreAssignable() throws CelestaException {
		LogSetupCursor lsc = (LogSetupCursor) c;
		assertNull(lsc.getGrainid());
		lsc.setValue("grainid", "asdFsaf");

		assertEquals("asdFsaf", lsc.getGrainid());

		boolean itWas = false;
		try {
			lsc.setValue("asdfasdf", "sswe");
		} catch (CelestaException e) {
			itWas = true;
		}
		assertTrue(itWas);

	}
}