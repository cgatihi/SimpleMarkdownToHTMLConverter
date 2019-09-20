package com.web.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class SimpleMarkdownToHTMLConverter {

  private boolean isParagraphOpen = false;

  public static void main(String[] args) {
    try {
      File markdownInputFile = new File(args[0]);
      String markdownInputString = FileUtils.readFileToString(markdownInputFile, StandardCharsets.UTF_8);
      
      SimpleMarkdownToHTMLConverter converter = new SimpleMarkdownToHTMLConverter();
      String htmlOutput = converter.convertToHTML(markdownInputString);
      
      System.out.println(htmlOutput);
       
    } catch (IOException e) {
      System.err.println("Error processing input file: " + e.getMessage());
    }
  }

  public String convertToHTML(String markdownInput) {
    StringBuilder htmlOutput = new StringBuilder();
    if (markdownInput.trim().length() > 0) {
      List<String> lines = Arrays.asList(markdownInput.split("\n"));
      for (String line : lines) {
        htmlOutput.append(convertLine(line));
      }
      if (isParagraphOpen) {
        htmlOutput.append("</p>");
      }
    }
    return htmlOutput.toString().trim();
  }

  protected String convertLine(String line) {
    line = convertLinks(line);
    return formatLine(line);
  }

  protected String formatLine(String line) {
    String patternString1 = "(^#{1,6}\\s)";
    Pattern pattern = Pattern.compile(patternString1);
    Matcher matcher = pattern.matcher(line);

    StringBuilder newLine = new StringBuilder();
    if (matcher.find()) {
      // found a heading line
      String match = matcher.group(1); // get rid of whitespace
      int headingLevel = match.length() - 1; // count the number of # characters to determine heading level (ignore trailing whitespace
                                             // character)
      line = line.substring(match.length()); // get rid of leading # characters
      newLine.append("<h" + headingLevel + ">" + line + "</h" + headingLevel + ">\n");
      isParagraphOpen = false;
    }
    else if (line.equals("")) {
      // found a blank line
      if (isParagraphOpen) {
        newLine.append("</p>\n");
        isParagraphOpen = false;
      }
      newLine.append("\n");
    }
    else {
      // found unformatted text
      if (!isParagraphOpen) {
        newLine.append("<p>");
        isParagraphOpen = true;
      }
      else {
        newLine.append("\n");
      }
      newLine.append(line);
    }
    return newLine.toString();
  }

  protected String convertLinks(String line) {
    // use reluctant quantifier to find as many matches as possible
    String patternStr = "(\\[.+?\\])(\\(.+?\\))";
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(line);

    while (matcher.find()) {
      String linkText = matcher.group(1);
      linkText = linkText.substring(1, linkText.length() - 1);
      String linkUrl = matcher.group(2);
      linkUrl = linkUrl.substring(1, linkUrl.length() - 1);
      line = line.replaceFirst(patternStr, "<a href=\"" + linkUrl + "\">" + linkText + "</a>");
    }
    return line;
  }

}
