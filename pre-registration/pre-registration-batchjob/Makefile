all: package_app build_image run_app

.PHONY: all

package_app:
	#mvn clean package -Dmaven.test.skip=true

build_image:
	docker build . --file Dockerfile --tag pre-registration-demographic-service:debug

run_app:
	docker push wuriguinea/pre-registration-demographic-service:debug
