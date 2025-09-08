package com.axonivy.utils.word.demo.util;



import java.io.IOException;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

public class FilesUtil {
  /*
   * set file reference into the session
   */
  public static void setFileRef(File ivyFile) {
    Ivy.session().setAttribute("docRef", Ivy.html().fileLink(ivyFile).getAbsolute());
    Ivy.log().info(ivyFile.getPath());
    Ivy.session().setAttribute("docFilename", ivyFile.getName());
  }

  /*
   * set file reference into the session
   */
  public static void setFileRef(java.io.File file) throws IOException {
    File ivyFile = new File( "/" + file.getName(), false);
    setFileRef(ivyFile);
  }
}
