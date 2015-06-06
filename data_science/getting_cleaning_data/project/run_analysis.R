# Script for getting cleaning data project

# 1. Merge the training and the test datasets
x_test_set <- read.table("./data/uci_har_dataset/test/X_test.txt")
y_test_set <- read.table("./data/uci_har_dataset/test/y_test.txt")
subject_test <- read.table("./data/uci_har_dataset/test/subject_test.txt")
x_train_set <- read.table("./data//uci_har_dataset/train/X_train.txt")
y_train_set <- read.table("./data//uci_har_dataset/train/y_train.txt")
subject_train <- read.table("./data/uci_har_dataset/train/subject_train.txt")
merged_x_set <- merge(x_test_set, x_train_set, all="TRUE")
merged_y_set <- merge(y_test_set, y_train_set, all="TRUE")
merged_subject <- merge(subject_test, subject_train, all="TRUE")

activity_labels <- read.table("./data/uci_har_dataset/activity_labels.txt")
head (activity_labels)
features <- read.table("./data//uci_har_dataset//features.txt")
head(features)

mean_std_only <- grep("-mean\\(\\)|-std\\(\\)", features[,2])
factor(x = merged_x_set, labels = features[,2])


