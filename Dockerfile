FROM nunopreguica/sd2223tpbase

# working directory inside docker image
WORKDIR /home/sd

# copy the jar created by assembly to the docker image
COPY target/*jar-with-dependencies.jar sd2223.jar

# copy the file of properties to the docker image
COPY feeds.props feeds.props
