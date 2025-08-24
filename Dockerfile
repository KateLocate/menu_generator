FROM alpine:3.22
WORKDIR /app
RUN apk add npm make maven
COPY . .
RUN make react
RUN make jar
ENTRYPOINT ["make","runJar"]
EXPOSE ${PORT}
