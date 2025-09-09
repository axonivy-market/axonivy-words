package com.axonivy.utils.axon.ivy.words.service;

public final class FileUtil {
  /**
   * check if a String is a valid file name.<br>
   * In the filename we don't accept \/:<>*?|", tab, backspace, returnline and
   * null<br>
   * @param filename
   * @return true if the given File name is valid, else false
   */
  public static boolean isFileNameValid(String filename) {
    boolean b = true;
    if (filename.contains("\\") ||
            filename.contains("/") ||
            filename.contains("<") ||
            filename.contains(">") ||
            filename.contains("*") ||
            filename.contains("|") ||
            filename.contains("?") ||
            filename.contains(":") ||
            filename.contains("\b") ||
            filename.contains("\t") ||
            filename.contains("\n") ||
            filename.contains("\0") ||
            filename.contains("\""))
      b = false;

    return b;
  }

}
