---
name: implementation-skill
description: "This skills is added to build a simplified Payroll Management Android app."
version: 1.0.0
---

## Goal

The app allows users to:

- View all created payrolls
- Create a new payroll with employees
- View payroll details
- Review wages, taxes, and net totals


## Feature Requirements

### 1. Payroll List Screen

When the user opens the app, show a list of all created payrolls.

Each payroll item must display:

- Creation date
- Number of employees
- Total payroll amount

The screen must have a button to create a new payroll.

### 2. Create Payroll Screen

A payroll contains one or more employees.

Each employee has:

| Field | Description |
| --- | --- | --- |
| Name | Employee full name |
| Wages | Total compensation amount for this employee |
| Exempt flag | Boolean flag. If true, no taxes are deducted |

Tax rule:

- If employee's wages are greater than 1000 and exempt flag is false, then tax is 5% of wages.
- Otherwise, tax is 0.

After creating payroll, it must appear immediately in the payroll list.

### 3. Payroll Detail Screen

When user taps a payroll item, navigate to detail screen.

For each employee show:

| Field | Formula |
| --- | --- | --- |
| Total | Wages |
| Taxes | 5% of wages (if applicable) |
| Net | Total – Taxes |

Payroll summary (bottom of screen):

| Field | Formula |
| --- | --- | --- |
| Total | Sum of all employee wages |
| Total Taxes | Sum of all employee taxes (hide this row if 0) |
| Total Net | Sum of all employee net amounts |

### Example:

| Employee | Wages | Exempt | Taxes | Net |
| --- | --- | --- | --- | --- | --- |
| Sarah Mitchell | 900 | Yes | 0 | 900 |
| James Caldwell | 1900 | No | 95 | 1805 |
| Laura Nguyen | 2000 | No | 100 | 1900 |

### Payroll Summary Example:

| Total | Total Taxes | Total Net |
| --- | --- | --- | --- |
| 4800 | 195 | Yes | 0 | 4605 |