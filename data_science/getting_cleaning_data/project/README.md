This file describes how run_analysis.R script works.

What you need in order to quickly observe results:

* It is not needed to download the data manually because the script will download and unzip it for you.
  If you already have the data in your PC than you can skip first two lines.
* Run the script and wait a few seconds for completion.
* Explore result data set in run_analysis.txt

Following is brief description of what the script does:

* Merges train and test data in one data frame. As result we get data frame with dimension 10299x561.
  It represents all statistics gained but it still doesn't have descriptive names.
* Filters data set from step 1 only by mean and std variables (10299x68)
* Assigns the data from step 2 with descriptive names and labels. Now we have data ready for average calculation
* Uses dplyr package for calculation of average of all variables and creates final data set, that is saved in run_analysis.txt file.
