corr <- function(directory, threshold = 0){
  good <- complete(directory)
  nobs <- good$nobs
  forCorr <- good$id[nobs > threshold]
  full_list <- list.files(directory, full.names = TRUE)
  vector <- numeric(length(forCorr))
  if(length(vector)>0)
  for(i in 1:length(vector)){
    csvFile <- read.csv(full_list[forCorr[i]])
    sulfates <- csvFile$sulfate
    nitrates <- csvFile$nitrate
    vector[i] <- cor(sulfates, nitrates, use="complete.obs")
  }
  vector
}