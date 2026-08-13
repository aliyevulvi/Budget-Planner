# Budget Planner

A simple Java desktop budgeting application for tracking income, expenses, savings, and generating reports in PDF format.

## Features

- Create and manage budget records
- Add income and savings values
- Record daily expenses by category
- View all records and expenses
- Generate category-based reports
- Export reports to PDF
- PostgreSQL database integration

## Tech Stack

- Java 17
- Maven
- PostgreSQL
- OpenPDF

## Project Structure

```text
BudgetPlanner/
├── src/
│   ├── main/
│   │   ├── java/aliyew/
│   │   │   ├── ConsoleUI.java
│   │   │   ├── DBManager.java
│   │   │   ├── Expense.java
│   │   │   ├── Main.java
│   │   │   ├── PDFManager.java
│   │   │   ├── Record.java
│   │   │   └── Report.java
│   │   └── resources/
│   └── test/java/aliyew/
│       └── AppTest.java
├── pom.xml
├── README.md
└── link.txt
```

## Requirements

- Java 17 or newer
- Maven 3.8+
- PostgreSQL database access

## Setup

1. Clone the repository:

```bash
git clone https://github.com/your-username/BudgetPlanner.git
cd BudgetPlanner
```

2. Configure the database connection in `DBManager.java` if needed.

3. Build the project:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn exec:java
```

If `exec-maven-plugin` is not configured, run the main class from your IDE or use:

```bash
mvn package
java -cp target/classes:target/dependency/* aliyew.Main
```

## Usage

- Launch the app from `Main`.
- Create a record with income and saving information.
- Add expenses grouped by category.
- View summaries and generate a report PDF.

## Notes

This project currently connects to a PostgreSQL database through the JDBC URL defined in `DBManager`. Make sure the database is reachable and the required tables exist.

## License

This project is licensed under the MIT License.
