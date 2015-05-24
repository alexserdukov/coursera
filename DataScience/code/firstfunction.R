add2 <- function(x,y) {
  x + y
}

above10 <- function(x){
  use <- x > 10
  x[use]
}


columnmean <- function(y, removeNA = TRUE){
  nc <- ncol(y)
  means <- numeric(nc)
  for (i in 1:nc){
    means[i] <- mean(y[, i], na.rm = removeNA)
  }
  means
}

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

pollutantmean1 <- function(directory, pollutant, id = 1:332){
  mean_vector <- c()
  # find all files in the specdata folder
  all_files <- as.character( list.files(directory) )
  file_paths <- paste(directory, all_files, sep="")
  for(i in id) {
    current_file <- read.csv(file_paths[i], header=T, sep=",")
    head(current_file)
    pollutant
    na_removed <- current_file[!is.na(current_file[, pollutant]), pollutant]
    mean_vector <- c(mean_vector, na_removed)
  }
  result <- mean(mean_vector)
  return(round(result, 3)) 
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
  print(forCorr)
  full_list <- list.files(directory, full.names = TRUE)
  vector <- rep(0, length(forCorr))
  for(i in forCorr){
    csvFile <- read.csv(full_list[i])
    sulfates <- csvFile$sulfate
    nitrates <- csvFile$nitrate
    vector[i] = cor(sulfates, nitrates, use="complete.obs")
  }
  vector
}
