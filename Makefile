.PHONY: list-commands run build run-backend test-backend build-backend build-frontend

list-commands:
	@LC_ALL=C $(MAKE) -pRrq -f $(firstword $(MAKEFILE_LIST)) : 2>/dev/null | \
	awk -v RS= -F: '/(^|\n)# Files(\n|$$)/,/(^|\n)# Finished Make data base/ {if ($$1 !~ "^[#.]") {print $$1}}' | \
	grep -E -v -e '^[^[:alnum:]]' -e '^$@$$'

run:
	@echo "~~~Running Menu App~~~"
	cd backend && docker compose up --build \
	& cd backend && java -jar target/menu-generator-0.0.1-SNAPSHOT.jar

run-backend:
	@echo "~~~Running Spring Boot App~~~"
	cd backend && mvn spring-boot:run

test-backend:
	@echo "~~~Running Spring Boot App Tests~~~"
	cd backend && docker compose up --build \
	& cd backend && mvn test \
	&& docker compose down

test-backend-ci:
	@echo "~~~Running Spring Boot App Tests~~~"
	cd backend && mvn test

build:
	@echo "~~~Build Menu App~~~"
	make build-frontend && make build-backend

build-backend:
	@echo "~~~Cleaning & Creating Jar~~~"
	cd backend && mvn clean package -DskipTests

build-frontend:
	@echo "~~~Building React App, Copying Into Static Folder.~~~"
	cd frontend/menu_app && \
 	npm install && npm run build && \
 	cd ../../ &&  \
 	cp -a frontend/menu_app/build/. backend/src/main/resources/static/
