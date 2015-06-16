# Script for getting cleaning data project
# Download file with data
download.file(url = "https://d396qusza40orc.cloudfront.net/getdata%2Fprojectfiles%2FUCI%20HAR%20Dataset.zip", 
              destfile = "./data/project_data.zip", method = "curl")
# Unzip data
unzip(zipfile = "./data/project_data.zip",exdir = "./data/", overwrite = TRUE)

#Load data to data frames and join test and train data
activities_stats <- rbind(read.table("./data/UCI HAR Dataset/test/X_test.txt"),
                          read.table("./data/UCI HAR Dataset/train/X_train.txt"))

activities <- rbind(read.table("./data/UCI HAR Dataset/test/y_test.txt"),
                    read.table("./data/UCI HAR Dataset/train/y_train.txt"))

volunteers <- rbind(read.table("./data/UCI HAR Dataset/test/subject_test.txt"),
                    read.table("./data/UCI HAR Dataset/train/subject_train.txt"))

activity_labels <- read.table("./data/UCI HAR Dataset/activity_labels.txt")

features <- read.table("./data//UCI HAR Dataset//features.txt")
#Choose only mean and std measurements
filtered_features <- grep("-mean\\(\\)|-std\\(\\)", features[,2])
filtered_activities_stats <- activities_stats[,filtered_features]
names(filtered_activities_stats) <- features[filtered_features,2]
# Label activities
activities[,1] <- activity_labels[activities[,1],2]
names(activities) <- "Activity"
names(volunteers) <- "Volunteers"

# Create tidy dataset
tidy_set <- cbind(volunteers, activities, filtered_activities_stats)

# Group by volunteers and activities and calculate mean for all features
library(dplyr)
result <- tidy_set %>% group_by(Volunteers, Activity) %>% summarise_each(funs(mean))
# Write table to txt file
write.table(result, file = "./data/run_analysis.txt", row.names = FALSE)