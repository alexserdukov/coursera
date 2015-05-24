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

corr <- function(directory, threshold = 0){
  good <- complete(directory)
  nobs <- good$nobs
  forCorr <- good$id[nobs > threshold]
  full_list <- list.files(directory, full.names = TRUE)
  vector <- numeric(length(forCorr))
  for(i in 1:length(vector)){
    csvFile <- read.csv(full_list[forCorr[i]])
    sulfates <- csvFile$sulfate
    nitrates <- csvFile$nitrate
    vector[i] <- round(cor(sulfates, nitrates, use="complete.obs"), 5)
  }
  vector
}

