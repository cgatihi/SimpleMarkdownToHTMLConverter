package com.web.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleMarkdownToHTMLConverterTests {
  
  private SimpleMarkdownToHTMLConverter converter;

  @Before
  public void setUp() {
    converter = new SimpleMarkdownToHTMLConverter();
  }
  
  @Test
  public void testEmpty() {
    Assert.assertEquals("", converter.convertToHTML(""));
  }
  
  @Test
  public void testWhiteSpace() {
    Assert.assertEquals("\n\n", converter.convertToHTML("\n\n"));
  }
  
  private void compareSampleInputOutputFiles(String inputPath, String outputPath) throws Exception {
    File markdownInputFile = new File(inputPath);
    File htmlOutputFile = new File(outputPath);
    String markdownInputString = FileUtils.readFileToString(markdownInputFile, StandardCharsets.UTF_8);
    String htmlOutputString = FileUtils.readFileToString(htmlOutputFile, StandardCharsets.UTF_8);
    Assert.assertEquals(htmlOutputString, converter.convertToHTML(markdownInputString));
  }
  
  @Test
  public void testSample1() throws Exception {
    compareSampleInputOutputFiles("sample_input/sample1.txt", "sample_output/sample1.html");
    
  }
  
  @Test
  public void testSample2() throws Exception {
    compareSampleInputOutputFiles("sample_input/sample2.txt", "sample_output/sample2.html");
  }
  
  @Test
  public void testHeadingFormatting() {
    String testString = "foo";
    StringBuilder markdownLine = new StringBuilder(" ");
    markdownLine.append(testString);
    for(int i=1; i <= 10; i++) {
      markdownLine.insert(0, '#');
      String result = converter.convertHeadings(markdownLine.toString());
      if(i <= 6) {
        Assert.assertEquals("<h" + i + ">" + testString + "</h" + i + ">", result);
      } else {
        Assert.assertFalse(result.startsWith("<h" + i + ">"));
        Assert.assertFalse(result.endsWith("</h" + i + ">"));
      }
    }
  }
}
