package com.axonivy.connector.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.aspose.words.License;
import com.axonivy.connector.word.WordFactory;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
class WordFactoryTest {

	@BeforeEach
	void resetLicenseField() throws Exception {
		var field = WordFactory.class.getDeclaredField("license");
		field.setAccessible(true);
		field.set(null, null);
	}

	/** Helper to run test code with mocks already set up */
	private void withMockedLicense(TestLogic logic) throws Exception {
		try (MockedStatic<ThirdPartyLicenses> mockedThirdParty = Mockito.mockStatic(ThirdPartyLicenses.class)) {
			InputStream dummyStream = mock(InputStream.class);
			mockedThirdParty.when(ThirdPartyLicenses::getDocumentFactoryLicense).thenReturn(dummyStream);

			try (MockedConstruction<License> mockedLicenseCtor = Mockito.mockConstruction(License.class,
					(mock, context) -> doNothing().when(mock).setLicense(any(InputStream.class)))) {
				logic.run(dummyStream, mockedThirdParty, mockedLicenseCtor);
			}
		}
	}

	@Test
	void testLoadLicenseWithoutRealAsposeFile() throws Exception {
		withMockedLicense((stream, mockedLicenses, licenseConstruction) -> {
			WordFactory.loadLicense();
			WordFactory.loadLicense();
			mockedLicenses.verify(ThirdPartyLicenses::getDocumentFactoryLicense);
			assertEquals(1, licenseConstruction.constructed().size());
			verify(licenseConstruction.constructed().get(0)).setLicense(stream);
		});
	}

	@Test
	void testGetCallsLoadLicenseAndReturnsValue() throws Exception {
		withMockedLicense((stream, mockedThirdParty, mockedLicenseCtor) -> {
			Supplier<String> supplier = () -> "HelloWorld";
			String result = WordFactory.get(supplier);
			assertEquals("HelloWorld", result);
			mockedThirdParty.verify(ThirdPartyLicenses::getDocumentFactoryLicense, times(1));
			assertEquals(1, mockedLicenseCtor.constructed().size());
			verify(mockedLicenseCtor.constructed().get(0)).setLicense(stream);
		});
	}

	@Test
	void testRunCallsLoadLicenseAndExecutesRunnable() throws Exception {
		withMockedLicense((stream, mockedThirdParty, mockedLicenseCtor) -> {
			final boolean[] executed = { false };
			Runnable runnable = () -> executed[0] = true;
			WordFactory.run(runnable);
			assertTrue(executed[0]);
			mockedThirdParty.verify(ThirdPartyLicenses::getDocumentFactoryLicense, times(1));
			assertEquals(1, mockedLicenseCtor.constructed().size());
			verify(mockedLicenseCtor.constructed().get(0)).setLicense(stream);
		});
	}

	/** Functional interface for shared test logic */
	@FunctionalInterface
	private interface TestLogic {
		void run(InputStream stream, MockedStatic<ThirdPartyLicenses> mockedThirdParty,
				MockedConstruction<License> mockedLicenseCtor) throws Exception;
	}
}