FROM python:3-alpine

COPY init_services.py /tmp/init_services.py

# WORKDIR /tmp

USER root
RUN python -m pip install --upgrade pip
RUN python -m pip install requests
USER 1001

ENTRYPOINT [ "python", "/tmp/init_services.py" ]