1. Overview
* 분산환경의 Timing데이터 수집, 추적 시스템
* Collect, Query Service, Database, Web UI로 구성
* 트리 구조로 이뤄지는 Trace(1) + Span(n) 셋 구조	
* Trace
	- 하나의 사용자 요청에 사용되는 작업의 단위
* Span
	- Span ID 하위의 Tracing ID	

2. Downsload and install
```sh
$ curl -sSL https://zipkin.io/quickstart.sh | bash -s
$ java -jar zipkin.jar
```
3. Web ui
> http://localhost:9411