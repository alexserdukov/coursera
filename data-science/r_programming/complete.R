complete <- function(directory, id = 1 : 332){
  full_list <- list.files(directory, full.names = TRUE)
  for(i in id){
    csvFile <- read.csv(full_list[i])
    good <- complete.cases(csvFile)
    csvFile <- csvFile[good, ]
    num <- nrow(csvFile)
    if (i == id[1])
      df <- data.frame(id = i, nobs = num)
    else
      df <- rbind(df, c(i,num))
  }
  df
}