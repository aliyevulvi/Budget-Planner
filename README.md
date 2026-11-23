# Budget Planner

A desktop budget management application built with Java and Swing. It helps users track income, savings, expenses, categories, budget limits, reports, backups, and PostgreSQL synchronization.

## Features

- Create budget records with income and savings information
- List, update, and delete budget periods
- Record expenses by category and date
- Set general and category-based spending limits
- Manage expense categories and payment templates
- Display expense charts with JFreeChart
- Generate PDF reports and XLSX files
- Store local data in JSON files
- Synchronize data with PostgreSQL
- Create backups and restore application data

## Technologies

- Java 17
- Java Swing
- Maven
- PostgreSQL and JDBC
- Jackson
- OpenPDF
- Apache POI
- JFreeChart
- Log4j 2

## Requirements

- JDK 17 or later
- Maven 3.8 or later
- A PostgreSQL database

## Installation

1. Clone the repository:

```bash
git clone https://github.com/<your-username>/BudgetPlanner.git
cd BudgetPlanner
```

2. Prepare a PostgreSQL JDBC connection URL:

```text
jdbc:postgresql://server:port/database?ssl=require&user=username&password=password
```

> If example connection details or credentials exist in `link.txt` or Git history, replace them with credentials for your own environment. Never commit database passwords.

3. In the project root, ensure the `data` directory contains a `url.txt` file with the PostgreSQL JDBC URL:

```text
jdbc:postgresql://server:port/database?ssl=require&user=username&password=password
```

The application stores local data in the `data` directory and creates reports and backups in folders inside the application directory.

4. Build the project:

```bash
mvn clean package
```

5. Run the application:

```bash
java -jar target/BudgetPlanner-1.0-SNAPSHOT.jar
```

Alternatively, run the `main` method in `aliyew.Main` from your IDE.

## Database Setup

The application uses the `app_schema` PostgreSQL schema. It creates the following tables automatically when database operations are available:

- `app_schema.tb_records`
- `app_schema.tb_expenses`

The database user must have permission to create the schema and manage tables in that schema.

## Usage

1. Create a new budget record.
2. Enter income, savings, and period information.
3. Add expense categories and record expenses.
4. Define spending limits.
5. Generate PDF or XLSX reports.
6. Synchronize the local changes with PostgreSQL.
7. Use the settings panel to create or restore backups.

## Testing

Run the tests with Maven:

```bash
mvn test
```

## Project Structure

```text
BudgetPlanner/
├── src/
│   ├── main/java/aliyew/
│   │   ├── Main.java           # Application entry point
│   │   ├── UIManager.java      # Swing user interface
│   │   ├── DBManager.java      # PostgreSQL operations
│   │   ├── JsonManager.java    # Local JSON data management
│   │   ├── BackupManager.java  # Backup and restore operations
│   │   ├── PDFManager.java     # PDF reports
│   │   └── XlsxManager.java    # Excel reports
│   └── test/java/aliyew/
├── data/                       # Local data files
├── pom.xml
└── README.md
```

## Security

Do not store database passwords or private connection URLs in source files or commit history. Rotate any credential that has already been committed to a public repository.

## License

No license file has been added to this project yet.
