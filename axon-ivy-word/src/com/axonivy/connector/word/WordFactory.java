package com.axonivy.connector.word;

import java.util.function.Supplier;
import com.aspose.words.License;
import ch.ivyteam.ivy.ThirdPartyLicenses;

public final class WordFactory {
	private static License license;

	public static void loadLicense() {
		if (license == null) {
			try (var lic = ThirdPartyLicenses.getDocumentFactoryLicense();) {
				if (lic != null) {
					license = new License();
					license.setLicense(lic);
				}
			} catch (Exception e) {
				e.printStackTrace();
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
