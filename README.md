# stock_analysis
Portfolio management and stock tracking program with login system and ticker analysis.

## Critical Starting Information

You must have a CSV file titled "logins.csv" (or other name, if you'd like to change it) for the login system to work. Make sure to change the filepath to wherever you have your file.

You must have a PostgreSQL server created to make any changes to the portfolios. Edit the database properties to whatever you'd like, in the methods starting on lines 653 and 680 of wireframe.java.

You must have internet connection to run analysis.

## Clarification of Files

**wireframe.java:** Main program. Run this to execute the program.

**stockChecker:** Contains the code for the Analysis portion of the main program.

**StockCheckerConsole:** Separate program. Run to display stock data.

**Stock:** Helper class, defines a stock and its properties.

**PECompare, ChangeCompare:** Helper classes, define the Comparator for the Analysis portion of the program. Sorts the result list by either P/E Ratio or Percent Change Today. Decide which one on line 103 of the stockChecker class.

## To use the program:

1. Download all the files.
2. Edit the information specified above.
3. Run wireframe.java and register with a username and password to begin. This data will be stored in your CSV file.
4. Add a portfolio by specifying a portfolio name.
5. Add tickers to your portfolio. Once a stock is added, your portfolio information will be saved in the database, and your portfolio can be retrieved from your database.
6. Press "Analysis" once you've finalized your portfolio.

## Authors

- Gabe Argush \- _main author_
