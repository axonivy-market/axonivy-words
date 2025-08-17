package com.axonivy.connector.word;

import java.io.InputStream;
import java.util.function.Supplier;
import com.aspose.words.License;
import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.environment.Ivy;

public final class WordFactory {
	private static License license;

	public static void loadLicense() {
		if (license == null) {
			try {
				InputStream in = ThirdPartyLicenses.getDocumentFactoryLicense();
				if (in != null) {
					license = new License();
					license.setLicense(in);
				}
			} catch (Exception e) {
				Ivy.log().error(e);
				license = null;
			}
		}
	}

	public static <T> T get(Supplier<T> supplier) {
		loadLicense();
		return supplier.get();
	}

	public static void run(Runnable run) {
		loadLicense();
		run.run();
	}
}
