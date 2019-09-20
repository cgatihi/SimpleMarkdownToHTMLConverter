# SimpleMarkdownToHTMLConverter
A simple "Git" style markdown => HTML converter written in Java with only the following simple conversion rules:

```
| Markdown                               | HTML                                              |
| -------------------------------------- | ------------------------------------------------- |
| `# Heading 1`                          | `<h1>Heading 1</h1>`                              |
| `## Heading 2`                         | `<h2>Heading 2</h2>`                              |
| `...`                                  | `...`                                             |
| `###### Heading 6`                     | `<h6>Heading 6</h6>`                              |
| `Unformatted text`                     | `<p>Unformatted text</p>`                         |
| `[Link text](https://www.example.com)` | `<a href="https://www.example.com">Link text</a>` |
| `Blank line`                           | `Ignored`                                         |
```

## Build and Run from command line ##

1. Ensure you are in the `SimpleMarkdownToHTMLConverter` directory.

2. `mvn clean package`

3. `java -jar target/SimpleMarkdownToHTMLConverter.jar sample_input/sample1.txt`

This will invoke the command line application with one of the two sample files in the `sample_input` folder.  But you may pass any input file to the application:

`java -jar target/SimpleMarkdownToHTMLConverter.jar <path_to_markdown_input>`

And the output will be the converted HTML based on the simple rules outlined above.  Here's the ouptut for the `sample_input/sample1.txt` example run above:

```
<h1>Sample Document</h1>

<p>Hello!</p>

<p>This is sample markdown for the <a href="https://www.test.com">Test</a> homework assignment.</p>
```
