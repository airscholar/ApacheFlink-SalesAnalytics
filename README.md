# Apache Flink for Sales Analytics

This repository contains an end-to-end data engineering project using Apache Flink, focused on performing sales analytics. The project demonstrates how to ingest, process, and analyze sales data, showcasing the capabilities of Apache Flink for big data processing.

## Project Overview

The project reads sales and product data from CSV files, performs a join operation to aggregate data, and computes total sales per category. It then sorts the results and writes them back to a CSV file. This example serves as a practical demonstration of using Apache Flink for complex data transformations and analytics.

### Features

- Data ingestion from CSV files
- Use of POJOs for data representation
- Dataset joins and aggregations
- Custom output formats for writing data

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Apache Flink
- Java Development Kit (JDK)
- Maven or SBT (for building the project)

### Installation

1. **Clone the repository:**

   ```bash
   https://github.com/airscholar/ApacheFlink-SalesAnalytics.git
   ```

2. **Navigate to the project directory:**

   ```bash
   cd ApacheFlink-SalesAnalytics
   ```

3. **Build the project:**

   ```bash
   mvn clean install
   ```

### Running the Application

1. **Start your Apache Flink cluster.**

2. **Submit the Flink job:**

   ```bash
   flink run -c salesAnalysis.DataBatchJob target/SalesAnalysis-1.0-SNAPSHOT.jar
   ```

3. **Check the output.**

   The processed data will be written to the specified output file.

### Video
[![Sales Analytics with Apache Flink](https://img.youtube.com/vi/jhJQp46QB_c/0.jpg)](https://youtu.be/jhJQp46QB_c)