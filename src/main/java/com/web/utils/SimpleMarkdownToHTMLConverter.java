package com.web.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class SimpleMarkdownToHTMLConverter {


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
    String htmlOutput = convertLinks(markdownInput);
    htmlOutput = convertHeadings(htmlOutput);
    htmlOutput = convertParagraphs(htmlOutput);   
    
    return htmlOutput;
  }

  protected String convertLinks(String input) {
    // use reluctant quantifier to find as many matches as possible
    String patternStr = "(\\[.+?\\])(\\(.+?\\))";
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      String linkText = matcher.group(1);
      linkText = linkText.substring(1, linkText.length() - 1);
      String linkUrl = matcher.group(2);
      linkUrl = linkUrl.substring(1, linkUrl.length() - 1);
      input = input.replaceFirst(patternStr, "<a href=\"" + linkUrl + "\">" + linkText + "</a>");
    }
    return input;
  }
  
  protected String convertHeadings(String input) {
    String patternStr = "(?m)(^#{1,6}\\s)(.*$)";
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      String hashMatch = matcher.group(1); // get rid of whitespace
      int headingLevel = hashMatch.trim().length(); // count the number of # characters to determine heading level (ignore trailing whitespace
                                             // character)
      String headingContent = matcher.group(2); // get rid of leading # characters
      input = input.replaceFirst(patternStr, ("<h" + headingLevel + ">" + headingContent + "</h" + headingLevel + ">"));
    }
    return input;
  }
  
  protected String convertParagraphs(String input) {
    String patternStr = "(\n\n|^)((?!\\s*<).+?)(\n\n|$)";
    Pattern pattern = Pattern.compile(patternStr, Pattern.DOTALL);

    Matcher matcher = pattern.matcher(input);
    if (matcher.find()) {
      String content = matcher.group(2);
      if (!content.trim().isEmpty()) {
        input = input.replaceFirst(Pattern.quote(content), "<p>" + content + "</p>");
        return convertParagraphs(input);
      }
    }

    return input;
  }

}
