# 로컬 인프라 구축 방법

AWS SQS를 로컬 환경에서 이용하기 위해 `localstack` 구축방법입니다.

## STEP.1

도커 실행시 새로운 큐를 자동으로 생성할 수 있도록 `init-queues.sh`를 실행 시켜줘야합니다.

따라서, 해당 디렉토리에 있는 `init-queues.sh`가 실행 권한을 가질 수 있게 아래와 같은 명령어를 입력해줍니다

```bash
# `init-queues.sh` 파일 경로에 따라 유동적으로 하시면 됩니다
sudo chmod +x ./docker/init-queues.sh
```

## STEP.2

작성된 인프라 환경은 `backend-network` 네트워크를 공유하며 실행되므로 도커 네트워크를 만들어야 합니다.

아래의 명령어를 입력해줍니다.

```bash
docker network create backend-network
```

## STEP.3

마지막으로 전체 도커 환경을 띄우시면 됩니다.

```bash
docker-compose -f ./docker/docker-compose.yml up -d
```
