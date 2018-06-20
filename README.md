# repeg

`repeg` is a java library to for creating parsers based on the principals of [Parsing Expression Grammars (PEG)](https://en.wikipedia.org/wiki/Parsing_expression_grammar).

This project was created back in 2010 but was cleaned up and published in 2019.  Since it has only just been made public,
consider this project in beta phase. 

*Work is ongoing to get this library published to maven central.  First full release ETA end Q1-2020*

## Requirements

* Java 1.8 or higher.
 
## Main features

* Parsers/Expressions are defined in code (java) using fluent API.  Ease of use and understanding are primary objectives of this project.
* Zero transitive dependencies - `repeg` does not require and further libraries
* Plain old java (i.e. no byte code magic) so easy to integrate in restricted environments.

## Future plans

* Ability to define parsers in a dedicated DSL
* Generate standalone parser with no dependencies to `repeg`
* Built-in support for indented languages (such as python or yaml)
* Investigate ratpack style parsing (Tests so far do not suggest much need for this.  I would welcome any feedback and examples of
languages where ratpack parsing may be of an advantage).

## Examples

This project comes with several examples which you can use to get you started:

* [Calculator](src/test/java/net/netnook/repeg/examples/calculator/ParserFactory.java) - parse and evaluate simple maths expressions such as `(7+2)*5`.
* [Template](src/test/java/net/netnook/repeg/examples/template/ParserFactory.java) - a very simple template language mimicking the style of jtwig.  
* [Todo](src/test/java/net/netnook/repeg/examples/todo/ParserFactory.java) - a parser for a text based todo list.
* [IsoDuration](src/test/java/net/netnook/repeg/examples/isoduration/ParserFactory.java) - a (partial) implementation of a parser for ISO durations such as `P1Y6MT7M3S`.
* [Persons (CSV)](src/test/java/net/netnook/repeg/examples/persons/csv/ParserFactory.java) - an parser for CSV file containing person information.
* [Persons (JSON)](src/test/java/net/netnook/repeg/examples/persons/json/ParserFactory.java) - same as above, but parsing JSON input.

Note:  The last 3 examples are provided to show what *can* be done but not necessarily what *should* be done with `repeg`.  A
parser for an ISO Duration can be hand-made relatively easily and will deliver much better performance.  For common formats such
as CSV and JSON, there are plenty of specialised parsers/libraries available which will handle corner-cases better than the 
examples and perform much better at the same time.
 
