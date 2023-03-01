# iappsxmlparser

The skeleton of this application was generated using JHipster 7.9.3, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.9.3](https://www.jhipster.tech/documentation-archive/v7.9.3).

## Running the app

Create docker image:
./mvnw -ntp verify -DskipTests -Pprod jib:dockerBuild

Start the app including PostGreSql, backend and frontend:
docker-compose -f src/main/docker/app.yml up -d

Login at localhost:8080 with admin/admin
Go to http://localhost:8080/admin/docs and use the POST /api/parseepaper endpoint. Type in a filename and copy-paste your xml from the pdf into the request body part, hit execute.
Then you can check created ePapers at http://localhost:8080/epaper

Stop:
docker-compose -f src/main/docker/app.yml down
