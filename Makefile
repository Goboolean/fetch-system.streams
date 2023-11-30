test-app:
	@docker compose -f ./deploy/docker-compose.test.yml up --build --abort-on-container-exit
	@docker compose -f ./deploy/docker-compose.test.yml down