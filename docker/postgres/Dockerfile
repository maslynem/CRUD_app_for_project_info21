FROM postgres:15.0
COPY ./scripts/* /docker-entrypoint-initdb.d/
RUN chmod -R 755 /docker-entrypoint-initdb.d/*
EXPOSE 5432
