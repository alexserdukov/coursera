pollutantmean <- function (directory, pollutant, id = 1:332) {
  full_list <- list.files(directory, full.names = TRUE)
  for(i in id){
    csvFile <- read.csv(full_list[i])
    if (i == id[1])
      df <- rbind(csvFile)
    else
      df <- rbind(df, csvFile)
  }
  subset_df<-df[, pollutant] 
  round(mean(subset_df, na.rm=TRUE),3)
  
}