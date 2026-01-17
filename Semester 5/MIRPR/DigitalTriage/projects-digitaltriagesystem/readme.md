# Digital Triage System
AI Team Members:
   - Tapuc Delia Elena
   - Titieni Robert Paul 
   - Costan Alex Ionut
   - Stiube Denis-Antonio
### Documentation:
https://www.overleaf.com/read/rwthkfsbfdvy#feecdb
### Teaser:
https://youtu.be/W1sExHFd76M?si=XKLwkLsG8vR--tcI
### Teaser (MIRPR+PPI):
https://youtu.be/3vtCCv5swHw?si=BlwEd21ueGZxoIbt
## Overview

The Digital Triage System is a rule-based application designed to assist individuals in identifying the most appropriate healthcare professional based on their symptoms.
The app allows users to input their symptoms, severity, and medical history to recommend the most likely diagnosis and provide the appropriate provider (e.g., General Practitioner, Cardiologist, etc.).
This app uses a simple rule-based approach to match symptoms to possible conditions and determines the urgency and confidence of the diagnosis based on user inputs.

## Features

* **Symptom Selection**: Users can select from a list of symptoms (such as fever, headache, cough, etc.).
* **Severity Selector**: A slider allows users to select the severity level of their symptoms.
* **Personal Information**: Users are asked to input their name, age, and medical history.
* **Analysis Button**: After inputting symptoms and severity, the user can press the "Analyze Symptoms" button to receive a recommendation.

## Results (What the app shows):

* **Primary diagnosis (condition)**: The most likely diagnosis based on selected symptoms.
* **Recommended healthcare provider**: The provider recommended based on the condition (e.g., General Practitioner, Cardiologist, etc.).
* **Urgency**: Whether the diagnosis is an emergency or routine.
* **Confidence**: Confidence level in the diagnosis based on the severity and symptoms.
* **Alternative conditions**: A list of alternative diagnoses to consider.

## Prerequisites

* Python 3.x
* Kivy and KivyMD libraries

## Installation Steps

1. Clone the repository or download the app's code.
2. Create a virtual environment (optional but recommended):

    ```bash
    python -m venv .venv
    ```

3. Install the required dependencies:

    ```bash
    pip install -r requirements.txt
    ```

4. Run the app:

    ```bash
    python main.py
    ```
