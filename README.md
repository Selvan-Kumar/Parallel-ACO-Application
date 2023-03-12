# Parallel ACO Application 
- The application allows investigation of ACO on TSP with support for parallel independent colonies. 
- Launch the application in IDE by running the App.java class. 

## User Guide
1. To input you own TSP file, ensure that the file is in the following format with a .tsp extension: 

    total number of cities\
    name of city 1,latitude,longitude\
    name of city 2,latitude,longitude

2. Click on the file button and navigate to your file. Select open. You will see that the field next to TSP: label has 
your filename. This means that your file is in the application. You can upload as many files as you want. Only the most
recently uploaded file will be tracked by the application. 

3. Use the combobox next to Alpha: label to set alpha parameter. 

4. Use the combobox next to Beta: label to set beta parameter. 

5. Use the combobox next to Evaporation: label to set evaporation rate.

6. Use the combobox next to Iterations: label to set number of iterations.

7. Use the combobox next to Colonies: label to set number of parallel. A high value might result in delayed output.
Do exercise discretion when setting the parameter. 

8. Click on the Generate Button to see result. For high values in parameters, it might take a while to reflect results. 
This is because the result only appends after the optimization is complete. Please wait patiently to obtain results.

9. You will see the results in the table. 

10. To delete a result, double click on the row. It will open a pop up. Click on yes. There will be a confirmation 
message for deletion. 

11. To delete entire table, click on Clear Button. There will be a popup asking if you wish to reset file. 
Click on yes if you want to reset to default TSP file. Otherwise click on No. 
Another popup will appear asking to delete entire table. Click on Yes to delete table. 

12. To Export results to CSV, click on File in the menubar and select Export Results. 
Navigate to your desired location and give the file a suitable name. Click Save. 

13. To see a sample list of cities, click on File in the menubar and select Show Cities. 
This will display a list of 238 cities in the correct format. You may use this to create your own TSP files. 

Some results might take a while to see in the table due to its execution time. Please wait patiently. 
Please be wary of your computing environment and use your discretion when using high value parameter setting. 


