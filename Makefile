.PHONY: run test jar react

run:
	@echo "~~~Running Spring Boot App~~~"
	cd backend && mvn spring-boot:run

test:
	@echo "~~~Running Spring Boot App Tests~~~"
	cd backend && docker compose up --build \
	& cd backend && mvn test \
	&& docker compose down

jar:
	@echo "~~~Cleaning & Creating Jar~~~"
	cd backend && mvn clean package -DskipTests

react:
	@echo "~~~Building React App, Copying Into Static Folder.~~~"
	cd frontend/menu_app && \
 	npm install && npm run build && \
 	cd ../../ &&  \
 	mkdir backend/src/main/resources/static/build && cp -r frontend/menu_app/build backend/src/main/resources/static/
