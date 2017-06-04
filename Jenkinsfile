stage 'build_Project'
node{
  if(isUnix()){
    sh './gradlew clean build --info'
  }
  else{
    bat 'gradle build --info'
  }
}

