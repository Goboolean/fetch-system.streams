test-app:
	@docker compose -f ./deploy/docker-compose.test.yml up --attach server --build --abort-on-container-exit

build-image:
	docker build -t fetch-system-streams -f ./deploy/Dockerfile .